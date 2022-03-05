package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.OrderCancelResponseDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderedBookDetailsResponseDto;
import com.example.usedmarket.web.exception.*;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderedBookRepository orderedBookRepository;
    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    /*
     * 주문 저장
     * @param userPrincipal - 현재 사용자
     * @param requestDto - 주문 요청정보
     * @return 주문관련정보를 담은 OrderResponseDto 를 반환
     */
    @Caching(evict = {
            //해당 사용자에 대한 주문 전체 조회
            @CacheEvict(key = "'order-user-'+#userPrincipal.id", value = "OrderAll-user"),
            //현재 사용자가 주문한 책 목록 조회
            @CacheEvict(key = "'orderedBook-user-'+#userPrincipal.id", value = "OrderedBookAll")
    })
    @Transactional
    @Override
    public OrderConfirmResponseDto save(@LoginUser UserPrincipal userPrincipal, OrderRequestDto requestDto) {
        //UserEntity 탐색
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        //POST 탐색
        Post post = postRepository.findById(requestDto.getPostId()).get();

        //UserEntity 와 POST 를 통한 Order 생성
        Order order = requestDto.createOrder(userEntity, post);

        //Book 탐색
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(() -> new BookNotFoundException("책이 존재하지 않습니다."));

        //OrderedBook 생성
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);

        //결제서비스

        //Order 를 Repository 를 통해 DB 에 저장
        orderRepository.save(order);

        //Book 재고 조정
        //재고 0 일시 자동 판매종료
        int updatedBookStock = book.stockDown(requestDto.getBookAmount());
        if (updatedBookStock == 0) {
            post.changeToSoldOut();
        } else if (updatedBookStock < 0) {
            throw new BookIsNotExistException("책이 존재하지 않습니다.");
        }

        //주문관련정보를 담은 OrderConfirmResponseDto 를 반환
        return OrderConfirmResponseDto.toDto(order, orderedBook);
    }

    /*
     * 주문 ID 값에 의한 주문 조회
     * @param userPrincipal - 현재 사용자
     * @param orderId - 주문 ID 값
     * @return
     */
    @Cacheable(key = "'order-'+#orderId", value = "OrderDetails")
    @Transactional(readOnly = true)
    @Override
    public OrderConfirmResponseDto findById(@LoginUser UserPrincipal userPrincipal, Long orderId) {

        // 주문 조회
        Order findOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("주문이 존재하지 않습니다."));

        // 조회 요청 사용자와 ORDER 의 주문자가 일치하는지 확인
        if (userPrincipal.getId() != findOrder.getUser().getId()) {
            throw new IllegalArgumentException("사용자 ID 불일치로 조회할 수 없습니다.");
        }

        //주문과 주문된 책의 정보를 OrderResponseDto 로 반환
        return OrderConfirmResponseDto.toDto(findOrder, findOrder.getOrderedBookList().get(0));
    }

    /*
     * 해당 사용자에 대한 주문 전체 조회
     * @param userPrincipal - 현재 사용자
     * @return 해당 userPrincipal 이 주문한 모든 Order 들을 반환
     */
    @Cacheable(key = "'order-user-'+#userPrincipal.id", value = "OrderAll-user")
    @Transactional(readOnly = true)
    @Override
    public List<OrderConfirmResponseDto> findAll(@LoginUser UserPrincipal userPrincipal) {
        log.info("findAll method call...");
        List<Order> orderList = orderRepository.findByUserId(userPrincipal.getId());
        List<OrderConfirmResponseDto> responseDtoList = new ArrayList<>();
        for (Order order : orderList) {
            List<OrderedBook> orderedBookList = order.getOrderedBookList();
            for (OrderedBook orderedBook : orderedBookList) {
                responseDtoList.add(OrderConfirmResponseDto.toDto(order, orderedBook));
            }
        }
        return responseDtoList;
    }

    /*
     * 해당 사용자의 주문 취소
     * @param userPrincipal - 현재 사용자
     * @param orderId - ORDER 의 ID 값
     * @return
     */
    @Caching(evict = {
            //해당 사용자에 대한 주문 전체 조회
            @CacheEvict(key = "'order-user-'+#userPrincipal.id", value = "OrderAll-user"),
            //주문 ID 값에 의한 주문 조회
            @CacheEvict(key = "'order-'+#orderId", value = "OrderDetails"),
            //현재 사용자가 주문한 책 목록 조회
            @CacheEvict(key = "'orderedBook-user-'+#userPrincipal.id", value = "OrderedBookAll")
    })
    @Transactional
    @Override
    public OrderCancelResponseDto cancel(@LoginUser UserPrincipal userPrincipal, Long orderId) {

        //사용자 탐색
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        //주문 ID 에 의한 주문 탐색
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("해당 주문이 존재하지 않습니다."));

        //주문된 책 탐색
        OrderedBook orderedBook = orderedBookRepository.findById(order.getOrderedBookList().get(0).getId())
                .orElseThrow(() -> new OrderedBookNotFoundException("주문한 책이 존재하지 않습니다. 문의 글을 남겨주시기 바랍니다."));

        //재고 증가시킬 Book 탐색
        Book book = bookRepository.findById(orderedBook.getBook().getId()).orElse(null);

        //결제완료의 경우에만 취소가능
        if (order.getDeliveryStatus().equals(DeliveryStatus.PAYMENT_COMPLETED)) {
            // 결제 취소

            // 책 재고 조정
            if (book != null) {
                book.stockUp(orderedBook.getAmount());
            }

            // 주문된 책 삭제
            if (orderedBook.isDeletable(order)) {
                orderedBook.deleted();
            }

            // 주문 취소
            order.cancel(userEntity);

            // 포스트 상태 점검
            // SOLD_OUT(품절) 상태일 시 SELL(판매중)으로 변경
            Post post = order.getPost();
            if (post.isSoldOut()) {
                post.changeToStatusIsSell();
            }

            log.info("===주문 취소 완료===");

        } else if (order.getDeliveryStatus().equals(DeliveryStatus.BEING_DELIVERED)) {
            //배송중인 경우 취소불가
            throw new OrderCancellationNotAllowed("이미 배송 중이므로 주문 취소 불가합니다.");
        } else if (order.getDeliveryStatus().equals(DeliveryStatus.DELIVERY_COMPLETED)) {
            //배송완료인 경우 취소불가
            throw new OrderCancellationNotAllowed("배송완료이므로 주문 취소 불가합니다.");
        } else if (order.getDeliveryStatus().equals(DeliveryStatus.CANCEL_COMPLETED)) {
            //취소완료인 경우 취소불가
            throw new OrderCancellationNotAllowed("이미 취소된 주문입니다.");
        }
        return OrderCancelResponseDto.builder().cancelOrderId(orderId).deliveryStatus(DeliveryStatus.CANCEL_COMPLETED.name()).build();
    }
}
