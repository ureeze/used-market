package com.example.usedmarket.web.dto;

import lombok.*;

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
}
