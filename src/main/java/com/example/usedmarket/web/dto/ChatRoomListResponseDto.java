package com.example.usedmarket.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomListResponseDto implements Serializable {
    private List<ChatRoomResponseDto> chatRoomListByBuyer;
    private List<ChatRoomResponseDto> chatRoomListBySeller;

}