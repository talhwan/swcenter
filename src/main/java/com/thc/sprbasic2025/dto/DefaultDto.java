package com.thc.sprbasic2025.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

public class DefaultDto {

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class BaseDto {
        String empty; // 필드가 아무것도 없으면 상속한 후 빌드가 잘 안되어요..
        public BaseDto afterBuild(BaseDto param) {
            //param => reqDto 를 넣어주면!!
            BeanUtils.copyProperties(param, this);
            //this 인 서비스 디티오를 돌려줍니다! 안에 있는 모든 필드값 카피도 해줍니다!
            return this;
        }
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class CreateResDto {
        Long id;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateReqDto extends BaseDto{
        Long id;
        Boolean deleted;
    }
    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DeleteReqDto {
        Long id;
    }
    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DeleteListReqDto {
        List<Long> ids;
    }
    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DetailReqDto {
        Long id;
    }

    @Getter @Setter @SuperBuilder
    @NoArgsConstructor @AllArgsConstructor
    public static class DetailResDto {
        Long id;
        Boolean deleted;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ListReqDto {
        Boolean deleted;
        String sdate;
        String fdate;

        String keyword;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class PagedListReqDto {
        Boolean deleted;
        String sdate;
        String fdate;

        String keyword;

        Integer offset;
        Integer callpage;
        Integer perpage;
        String orderway;
        String orderby;

        public DefaultDto.PagedListResDto init(int listsize){
            Integer perpage = getPerpage(); //한번에 볼 글 갯수
            if(perpage == null || perpage < 1){
                perpage = 10;
            }
            if(perpage > 100){
                perpage = 100;
            }
            setPerpage(perpage);

            int totalpage = listsize / perpage; // 101 / 10 = >10 ?? 11이 되어야 하는데?
            if(listsize % perpage > 0){
                totalpage++;
            }

            Integer callpage = getCallpage();
            if(callpage == null || callpage < 1){
                //페이지 수가 없거나, 1보다 작은 페이수를 호출할 때
                callpage = 1;
            }
            if(callpage > totalpage){
                //전체 페이지보다 더 다음 페이지를 호출할때!
                callpage = totalpage;
            }
            if(callpage < 1){
                callpage = 1;
            }
            setCallpage(callpage);

            String orderby = getOrderby();
            if(orderby == null || orderby.isEmpty()){
                orderby = "id";
            }
            setOrderby(orderby);

            String orderway = getOrderway();
            if(orderway == null || orderway.isEmpty()){
                orderway = "DESC";
            }
            setOrderway(orderway);

            int offset = (callpage - 1) * perpage;
            System.out.println("offset : " + offset);
            setOffset(offset);

            return DefaultDto.PagedListResDto.builder()
                    .totalpage(totalpage)
                    .callpage(getCallpage())
                    .perpage(getPerpage())
                    .listsize(listsize)
                    .build();
        }
    }

    @Getter @Setter @SuperBuilder
    @NoArgsConstructor @AllArgsConstructor
    public static class PagedListResDto {
        Integer listsize;
        Integer totalpage;
        Integer callpage;
        Integer perpage;
        Object list;
    }


    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ScrollListReqDto {
        Boolean deleted;
        String sdate;
        String fdate;

        String keyword;

        Long cursor;
        String mark;

        Integer perpage;
        String orderway;
        String orderby;

        public void init(){
            setMark(null);

            Integer perpage = getPerpage(); //한번에 볼 글 갯수
            if(perpage == null || perpage < 1){
                perpage = 10;
            }
            if(perpage > 100){
                perpage = 100;
            }
            setPerpage(perpage);

            String orderby = getOrderby();
            if(orderby == null || orderby.isEmpty()){
                orderby = "id";
            }
            setOrderby(orderby);

            String orderway = getOrderway();
            if(orderway == null || orderway.isEmpty()){
                orderway = "DESC";
            }
            setOrderway(orderway);
        }
    }

}
