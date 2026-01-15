package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.BoardDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    /**/
    DefaultDto.CreateResDto create(BoardDto.CreateReqDto param, Long reqUserId);
    void update(BoardDto.UpdateReqDto param, Long reqUserId);
    void delete(DefaultDto.DeleteReqDto param, Long reqUserId);
    void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId);
    BoardDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<BoardDto.DetailResDto> list(BoardDto.ListReqDto param, Long reqUserId);
    DefaultDto.PagedListResDto pagedList(BoardDto.PagedListReqDto param, Long reqUserId);
    List<BoardDto.DetailResDto> scrollList(BoardDto.ScrollListReqDto param, Long reqUserId);
}
