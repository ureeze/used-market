package com.example.usedmarket.web.dto;

import lombok.*;
import org.json.simple.JSONObject;

import java.io.Serializable;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NaverBookInfo implements Serializable {

    private String title;
    private String link;
    private String image;
    private String author;
    private String price;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String description;

    public static NaverBookInfo toDto(JSONObject book){
        return NaverBookInfo.builder()
                .title((String) book.get("title"))
                .link((String) book.get("link"))
                .image((String) book.get("image"))
                .author((String) book.get("author"))
                .price((String) book.get("price"))
                .discount((String) book.get("discount"))
                .publisher((String) book.get("publisher"))
                .pubdate((String) book.get("pubdate"))
                .isbn((String) book.get("isbn"))
                .description((String) book.get("description"))
                .build();
    }
}
