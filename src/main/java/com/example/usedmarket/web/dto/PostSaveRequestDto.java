package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.security.dto.SessionMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    //포스트 제목
    @NotNull
    private String title;

    //포스트 상세내용
    @NotNull
    private String content;

    //책 제목
    @NotNull
    private String bookName;

    //판매 권 수
    @NotNull
    private int stock;

    //책 권당 가격
    @NotNull
    private int unitPrice;

    //책 카테고리
    @NotNull
    private String category;

    //책 상태
    @NotNull
    private String bookStatus;

    //책 img URL
    private String imgUrl;

    @Builder
    public PostSaveRequestDto(String title, String content, String bookName, int stock, int unitPrice, String imgUrl, String category, String bookStatus) {
        this.title = title;
        this.content = content;
        this.bookName = bookName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.imgUrl = imgUrl;
        this.category = category;
        this.bookStatus = bookStatus;
    }

    public Book toBook() {
        return Book.builder()
                .bookName(this.bookName)
                .stock(this.stock)
                .unitPrice(this.unitPrice)
                .category(this.category)
                .bookStatus(BookStatus.valueOf(this.bookStatus))
                .imgUrl(this.imgUrl)
                .build();
    }

    public Post toPost(Member member) {
        // requestDto 로 POST 생성
        Post post = Post.builder()
                .title(this.title)
                .content(this.content)
                .status(PostStatus.SELL)
                .member(member)
                .build();
        // requestDto 를 이용해 Book 생성 후 POST 에 Book 추가
        post.addBook(toBook());
        return post;
    }
}
