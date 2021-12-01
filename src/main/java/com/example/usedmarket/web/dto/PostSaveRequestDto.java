package com.example.usedmarket.web.dto;

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

    private int inventoryQuantity;

    private int unitPrice;

    private String category;


    @Builder
    public PostSaveRequestDto(String title, String content, String bookName, int inventoryQuantity, int unitPrice, String category) {
        this.title = title;
        this.content = content;
        this.bookName = bookName;
        this.inventoryQuantity = inventoryQuantity;
        this.unitPrice = unitPrice;
        this.category = category;
    }
}
