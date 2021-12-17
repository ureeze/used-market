package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.exception.*;
import com.example.usedmarket.web.security.dto.SessionMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderedBookRepository orderedBookRepository;
    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    /*
     * 주문 저장
     * @param sessionMember - 현재 세션 유저
     * @param requestDto - 주문 요청정보
     * @return 주문관련정보를 담은 OrderResponseDto 를 반환
     */
    @Transactional
    @Override
    public OrderConfirmResponseDto save(SessionMember sessionMember, OrderRequestDto requestDto) {
        //Member 탐색
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        //POST 탐색
        Post post = postRepository.findById(requestDto.getPostId()).get();

        //Member 와 POST 를 통한 Order 생성
        Order order = requestDto.createOrder(member, post);

        //Book 탐색
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(() -> new BookNotFoundException("책이 존재하지 않습니다."));

        //OrderedBook 생성
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);

        //결제서비스


        //Order 를 Repository 를 통해 DB 에 저장
        Order savedOrder = orderRepository.save(order);

        //주문관련정보를 담은 OrderResponseDto 를 반환
        return OrderConfirmResponseDto.toDto(savedOrder, orderedBook);
    }

    /*
     * ID 값을 통한 주문 조회
     * @param id - 주문 ID 값
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public OrderConfirmResponseDto findById(Long id) {
        // 주문 조회
        Order findOrder = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        //주문과 주문된 책의 정보를 OrderResponseDto 로 반환
        return OrderConfirmResponseDto.toDto(findOrder, findOrder.getOrderedBookList().get(0));
    }

    /*
     * 해당 세션의 전체 주문 조회
     * @param sessionMember - 현재 세션 유저
     * @return 해당 Member 가 주문한 모든 Order 들을 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<OrderConfirmResponseDto> findAll(SessionMember sessionMember) {
        // Member 탐색
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        return orderRepository.findByMemberId(member.getId()).stream()
                .map(order -> OrderConfirmResponseDto.toDto(order, order.getOrderedBookList().get(0)))
                .collect(Collectors.toList());
    }

    /*
     * 해당 세션의 주문 취소
     * @param sessionMember - 현재 세션 유저
     * @param id - ORDER 의 ID 값
     * @return
     */
    @Transactional
    @Override
    public void cancel(SessionMember sessionMember, Long orderId) {

        //사용자 탐색
        Member member = memberRepository.findById(sessionMember.getId()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        //주문 탐색
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("해당 주문이 존재하지 않습니다."));

        //주문된 책 탐색
        OrderedBook orderedBook = orderedBookRepository.findById(order.getOrderedBookList().get(0).getId())
                .orElseThrow(() -> new OrderedBookNotFoundException("주문한 책이 존재하지 않습니다. 문의 글을 남겨주시기 바랍니다."));

        //재고 증가시킬 Book 탐색
        Book book = bookRepository.findById(orderedBook.getBook().getId()).orElse(null);

        //결제완료의 경우에만 취소가능
        if (order.getDeliveryStatus().equals(DeliveryStatus.PAYMENT_COMPLETED)) {
            //결제 취소

            // 책 재고 조정
            if (book != null) {
                book.stockUp(orderedBook.getCount());
            }

            // 주문된 책 삭제
            if (orderedBook.isDeletable(order)) {
                orderedBook.deleted();
            }

            //주문 취소
            order.cancel(member);

        } else if (order.getDeliveryStatus().equals(DeliveryStatus.BEING_DELIVERED)) {
            //배송중인 경우 취소불가
            throw new OrderCancellationNotAllowed("이미 배송 중이므로 주문 취소 불가합니다.");
        } else if (order.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            //배송완료인 경우 취소불가
            throw new OrderCancellationNotAllowed("배송완료이므로 주문 취소 불가합니다.");
        } else if (order.getDeliveryStatus().equals(DeliveryStatus.PAYMENT_COMPLETED)) {
            //취소완료인 경우 취소불가
            throw new OrderCancellationNotAllowed("이미 취소된 주문입니다.");
        }
    }
}
