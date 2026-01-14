package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PopupDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PopupService {
    PopupDto.SequenceResDto sequence(PopupDto.SequenceReqDto param, Long reqUserId);
    /**/
    DefaultDto.CreateResDto create(PopupDto.CreateReqDto param, Long reqUserId);
    void update(PopupDto.UpdateReqDto param, Long reqUserId);
    void delete(DefaultDto.DeleteReqDto param, Long reqUserId);
    void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId);
    PopupDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<PopupDto.DetailResDto> list(PopupDto.ListReqDto param, Long reqUserId);
    DefaultDto.PagedListResDto pagedList(PopupDto.PagedListReqDto param, Long reqUserId);
    List<PopupDto.DetailResDto> scrollList(PopupDto.ScrollListReqDto param, Long reqUserId);
}
