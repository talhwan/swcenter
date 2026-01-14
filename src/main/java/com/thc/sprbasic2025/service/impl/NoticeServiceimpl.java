package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Notice;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.NoticeDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.NoticeMapper;
import com.thc.sprbasic2025.repository.NoticeRepository;
import com.thc.sprbasic2025.service.NoticeService;
import com.thc.sprbasic2025.service.PermittedService;
import com.thc.sprbasic2025.util.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeServiceimpl implements NoticeService {

    final String target = "notice";

    final NoticeRepository noticeRepository;
    final NoticeMapper noticeMapper;
    final PermittedService permittedService;


    @Override
    public DefaultDto.CreateResDto create(NoticeDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);
        try{
            if(param.getFile() != null){
                param.setImg("/image/" + FileUpload.upload(param.getFile()));
            }
        } catch (Exception e){}
        DefaultDto.CreateResDto res = noticeRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    @Override
    public void update(NoticeDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Notice notice = noticeRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        notice.update(param);
        noticeRepository.save(notice);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(NoticeDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }
    @Override
    public void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(DefaultDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

    public NoticeDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        //permittedService.isPermitted(reqUserId, target, 200);
        NoticeDto.DetailResDto res = noticeMapper.detail(param.getId());
        return res;
    }
    @Override
    public NoticeDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    @Override
    public List<NoticeDto.DetailResDto> list(NoticeDto.ListReqDto param, Long reqUserId) {
        return detailList(noticeMapper.list(param), reqUserId);
    }
    public List<NoticeDto.DetailResDto> detailList(List<NoticeDto.DetailResDto> list, Long reqUserId){
        List<NoticeDto.DetailResDto> newList = new ArrayList<>();
        for(NoticeDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(NoticeDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(noticeMapper.pagedListCount(param));
        res.setList(detailList(noticeMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<NoticeDto.DetailResDto> scrollList(NoticeDto.ScrollListReqDto param, Long reqUserId) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        if("title".equals(param.getOrderby())){
            Long cursor = param.getCursor();
            if(cursor != null){
                NoticeDto.DetailResDto notice = noticeMapper.detail(cursor);
                if(notice != null){
                    param.setMark(notice.getTitle() + "_" + notice.getId());
                }
            }
        }
        return detailList(noticeMapper.scrollList(param), reqUserId);
    }
}
