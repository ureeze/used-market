package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveRequestDto {

    //포스트 제목
    @NotNull
    private String postTitle;

    //포스트 상세내용
    @NotNull
    private String postContent;

    //책 제목
    @NotNull
    private String bookTitle;

    //판매 권 수
    @NotNull
    private int stock;

    //책 권당 가격
    @NotNull
    private int unitPrice;

    //책 카테고리
    @NotNull
    private String bookCategory;

    //책 상태
    @NotNull
    private String bookStatus;

    //책 img URL
    private String bookImgUrl;


    public Book toBook() {
        return Book.builder()
                .title(this.bookTitle)
                .stock(this.stock)
                .unitPrice(this.unitPrice)
                .category(this.bookCategory)
                .bookStatus(BookStatus.valueOf(this.bookStatus))
                .deleted(false)
                .imgUrl(this.bookImgUrl)
                .build();
    }

    public Post toPost(UserEntity user) {
        // requestDto 로 POST 생성
        Post post = Post.builder()
                .title(this.postTitle)
                .content(this.postContent)
                .status(PostStatus.SELL)
                .deleted(false)
                .userEntity(user)
                .build();
        return post;
    }
}
