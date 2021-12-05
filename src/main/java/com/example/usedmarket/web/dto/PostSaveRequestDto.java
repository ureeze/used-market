package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.security.dto.SessionMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;

    private String content;

    private String bookName;

    private int stock;

    private int unitPrice;

    private String category;

    private String imgUrl;

    @Builder
    public PostSaveRequestDto(String title, String content, String bookName, int stock, int unitPrice,String imgUrl, String category) {
        this.title = title;
        this.content = content;
        this.bookName = bookName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public Book toBook(){
        return Book.builder()
                .bookName(this.bookName)
                .stock(this.stock)
                .unitPrice(this.unitPrice)
                .category(this.category)
                .imgUrl(this.imgUrl)
                .build();
    }

    public Post toPost(SessionMember sessionMember){
        // requestDto 로 POST 생성
        Post post = Post.builder()
                .title(this.title)
                .content(this.content)
                .status(PostStatus.SELL)
                .member(sessionMember.toMember())
                .build();
        // requestDto 를 이용해 Book 생성 후 POST 에 Book 추가
        post.getBookList().add(toBook());
        return post;
    }
}
