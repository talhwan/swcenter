package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Popup;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PopupDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.PopupMapper;
import com.thc.sprbasic2025.repository.PopupRepository;
import com.thc.sprbasic2025.service.PopupService;
import com.thc.sprbasic2025.service.PermittedService;
import com.thc.sprbasic2025.util.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PopupServiceimpl implements PopupService {

    final String target = "popup";

    final PopupRepository popupRepository;
    final PopupMapper popupMapper;
    final PermittedService permittedService;

    @Override
    public PopupDto.SequenceResDto sequence(PopupDto.SequenceReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);

        //이동할 주제!
        Popup popup = popupRepository.findById(param.getId()).orElse(null);
        // 지금 현재 위치!!
        int nowSequence = popup.getSequence();
        int toSequence = 0;

        Boolean way = param.getWay();
        if(way){
            //적은 숫자로 바꿈!
            toSequence = nowSequence - 1;
            if(toSequence < 1){
                //안되는데?
                return PopupDto.SequenceResDto.builder().result(false).build();
            }
        } else {
            //높은 숫자로 바꿈!
            toSequence = nowSequence + 1;
            int count = popupMapper.pagedListCount(PopupDto.PagedListReqDto.builder().build());
            if(toSequence > count){
                //안되는데?
                return PopupDto.SequenceResDto.builder().result(false).build();
            }
        }
        //이동 강제로 당하는 애!!
        Popup targetPopup = popupRepository.findBySequence(toSequence);
        targetPopup.setSequence(0);
        popupRepository.save(targetPopup);

        popup.setSequence(toSequence);
        popupRepository.save(popup);

        targetPopup.setSequence(nowSequence);
        popupRepository.save(targetPopup);
        return PopupDto.SequenceResDto.builder().result(true).build();
    }

    /**/

    @Override
    public DefaultDto.CreateResDto create(PopupDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);

        int count = popupMapper.pagedListCount(PopupDto.PagedListReqDto.builder().build());
        param.setSequence(++count);

        try{
            if(param.getFile() != null){
                param.setImg("/image/" + FileUpload.upload(param.getFile()));
            }
        } catch (Exception e){}
        DefaultDto.CreateResDto res = popupRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    @Override
    public void update(PopupDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Popup popup = popupRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        popup.update(param);
        popupRepository.save(popup);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(PopupDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }
    @Override
    public void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(DefaultDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

    public PopupDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        //permittedService.isPermitted(reqUserId, target, 200);
        PopupDto.DetailResDto res = popupMapper.detail(param.getId());
        return res;
    }
    @Override
    public PopupDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    @Override
    public List<PopupDto.DetailResDto> list(PopupDto.ListReqDto param, Long reqUserId) {
        return detailList(popupMapper.list(param), reqUserId);
    }
    public List<PopupDto.DetailResDto> detailList(List<PopupDto.DetailResDto> list, Long reqUserId){
        List<PopupDto.DetailResDto> newList = new ArrayList<>();
        for(PopupDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(PopupDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(popupMapper.pagedListCount(param));
        res.setList(detailList(popupMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<PopupDto.DetailResDto> scrollList(PopupDto.ScrollListReqDto param, Long reqUserId) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        if("title".equals(param.getOrderby())){
            Long cursor = param.getCursor();
            if(cursor != null){
                PopupDto.DetailResDto popup = popupMapper.detail(cursor);
                if(popup != null){
                    param.setMark(popup.getTitle() + "_" + popup.getId());
                }
            }
        }
        return detailList(popupMapper.scrollList(param), reqUserId);
    }
}
