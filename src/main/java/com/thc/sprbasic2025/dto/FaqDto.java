package com.thc.sprbasic2025.dto;

import com.thc.sprbasic2025.domain.Faq;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

public class FaqDto {


    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class SequenceReqDto {
        Long id;
        Boolean way;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class SequenceResDto {
        Boolean result;
    }

    /**/

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateReqDto {
        Integer sequence;
        String title;
        String content;
        String img;

        private MultipartFile file;

        public Faq toEntity(){
            return Faq.of(getSequence(), getTitle(), getContent(), getImg());
        }
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateReqDto extends DefaultDto.UpdateReqDto{
        Integer sequence;
        String title;
        String content;
        String img;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class DetailResDto extends DefaultDto.DetailResDto{
        Integer sequence;
        String title;
        String content;
        String img;
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
