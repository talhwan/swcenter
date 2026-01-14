package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Faq;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.FaqDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.FaqMapper;
import com.thc.sprbasic2025.repository.FaqRepository;
import com.thc.sprbasic2025.service.FaqService;
import com.thc.sprbasic2025.service.PermittedService;
import com.thc.sprbasic2025.util.FileUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FaqServiceimpl implements FaqService {

    final String target = "faq";

    final FaqRepository faqRepository;
    final FaqMapper faqMapper;
    final PermittedService permittedService;

    @Override
    public FaqDto.SequenceResDto sequence(FaqDto.SequenceReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);

        //이동할 주제!
        Faq faq = faqRepository.findById(param.getId()).orElse(null);
        // 지금 현재 위치!!
        int nowSequence = faq.getSequence();
        int toSequence = 0;

        Boolean way = param.getWay();
        if(way){
            //적은 숫자로 바꿈!
            toSequence = nowSequence - 1;
            if(toSequence < 1){
                //안되는데?
                return FaqDto.SequenceResDto.builder().result(false).build();
            }
        } else {
            //높은 숫자로 바꿈!
            toSequence = nowSequence + 1;
            int count = faqMapper.pagedListCount(FaqDto.PagedListReqDto.builder().build());
            if(toSequence > count){
                //안되는데?
                return FaqDto.SequenceResDto.builder().result(false).build();
            }
        }
        //이동 강제로 당하는 애!!
        Faq targetFaq = faqRepository.findBySequence(toSequence);
        targetFaq.setSequence(0);
        faqRepository.save(targetFaq);

        faq.setSequence(toSequence);
        faqRepository.save(faq);

        targetFaq.setSequence(nowSequence);
        faqRepository.save(targetFaq);
        return FaqDto.SequenceResDto.builder().result(true).build();
    }

    /**/

    @Override
    public DefaultDto.CreateResDto create(FaqDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);

        int count = faqMapper.pagedListCount(FaqDto.PagedListReqDto.builder().build());
        param.setSequence(++count);

        try{
            if(param.getFile() != null){
                param.setImg("/image/" + FileUpload.upload(param.getFile()));
            }
        } catch (Exception e){}
        DefaultDto.CreateResDto res = faqRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    @Override
    public void update(FaqDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Faq faq = faqRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        faq.update(param);
        faqRepository.save(faq);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(FaqDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }
    @Override
    public void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(DefaultDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

    public FaqDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 200);
        FaqDto.DetailResDto res = faqMapper.detail(param.getId());
        return res;
    }
    @Override
    public FaqDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    @Override
    public List<FaqDto.DetailResDto> list(FaqDto.ListReqDto param, Long reqUserId) {
        return detailList(faqMapper.list(param), reqUserId);
    }
    public List<FaqDto.DetailResDto> detailList(List<FaqDto.DetailResDto> list, Long reqUserId){
        List<FaqDto.DetailResDto> newList = new ArrayList<>();
        for(FaqDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(FaqDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(faqMapper.pagedListCount(param));
        res.setList(detailList(faqMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<FaqDto.DetailResDto> scrollList(FaqDto.ScrollListReqDto param, Long reqUserId) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        if("title".equals(param.getOrderby())){
            Long cursor = param.getCursor();
            if(cursor != null){
                FaqDto.DetailResDto faq = faqMapper.detail(cursor);
                if(faq != null){
                    param.setMark(faq.getTitle() + "_" + faq.getId());
                }
            }
        }
        return detailList(faqMapper.scrollList(param), reqUserId);
    }
}
