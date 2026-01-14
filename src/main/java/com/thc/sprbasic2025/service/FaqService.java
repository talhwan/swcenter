package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.FaqDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FaqService {
    FaqDto.SequenceResDto sequence(FaqDto.SequenceReqDto param, Long reqUserId);
    /**/
    DefaultDto.CreateResDto create(FaqDto.CreateReqDto param, Long reqUserId);
    void update(FaqDto.UpdateReqDto param, Long reqUserId);
    void delete(DefaultDto.DeleteReqDto param, Long reqUserId);
    void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId);
    FaqDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<FaqDto.DetailResDto> list(FaqDto.ListReqDto param, Long reqUserId);
    DefaultDto.PagedListResDto pagedList(FaqDto.PagedListReqDto param, Long reqUserId);
    List<FaqDto.DetailResDto> scrollList(FaqDto.ScrollListReqDto param, Long reqUserId);
}
