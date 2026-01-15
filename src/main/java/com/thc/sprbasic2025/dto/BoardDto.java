package com.thc.sprbasic2025.dto;

import com.thc.sprbasic2025.domain.Board;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

public class BoardDto {

    /**/

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateReqDto {
        Long userId;
        String title;
        String content;

        public Board toEntity(){
            return Board.of(userId, title, content);
        }
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateReqDto extends DefaultDto.UpdateReqDto{
        String title;
        String content;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class DetailResDto extends DefaultDto.DetailResDto{
        Long userId;
        String title;
        String content;
        Integer countread;

        String userUsername;
        String userNick;
        String userImg;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ListReqDto extends DefaultDto.ListReqDto{
        String title;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class PagedListReqDto extends DefaultDto.PagedListReqDto{
        String title;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ScrollListReqDto extends DefaultDto.ScrollListReqDto{
        String title;
    }
}
