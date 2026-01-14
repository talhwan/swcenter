package com.thc.sprbasic2025.dto;

import com.thc.sprbasic2025.domain.Permissiondetail;
import lombok.*;
import lombok.experimental.SuperBuilder;

public class PermissiondetailDto {

    @NoArgsConstructor @AllArgsConstructor @SuperBuilder @Getter @Setter
    public static class AllowReqDto extends DefaultDto.BaseDto {
        private String target;
    }
    @NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
    public static class AllowResDto{
        private Boolean allowed;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ToggleReqDto{
        Boolean alive; //true면 생성, false면 삭제!
        Long permissionId;
        String target;
        Integer func;
    }
    /**/
    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateReqDto {
        Long permissionId;
        String target;
        Integer func;

        public Permissiondetail toEntity(){
            return Permissiondetail.of(getPermissionId(), getTarget(), getFunc());
        }
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateReqDto extends DefaultDto.UpdateReqDto{
        String target;
        Integer func;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class DetailResDto extends DefaultDto.DetailResDto{
        Long permissionId;
        String target;
        Integer func;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ListReqDto extends DefaultDto.ListReqDto{
        Long permissionId;
        String target;
        Integer func;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class PagedListReqDto extends DefaultDto.PagedListReqDto{
        Long permissionId;
        String target;
        Integer func;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ScrollListReqDto extends DefaultDto.ScrollListReqDto{
        Long permissionId;
        String target;
        Integer func;
    }
}
