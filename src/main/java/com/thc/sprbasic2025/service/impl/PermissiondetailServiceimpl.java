package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Permissiondetail;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PermissiondetailDto;
import com.thc.sprbasic2025.dto.PermissionDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.PermissiondetailMapper;
import com.thc.sprbasic2025.repository.PermissiondetailRepository;
import com.thc.sprbasic2025.service.PermissiondetailService;
import com.thc.sprbasic2025.service.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissiondetailServiceimpl implements PermissiondetailService {

    final String target = "permission";

    final PermissiondetailRepository permissiondetailRepository;
    final PermissiondetailMapper permissiondetailMapper;
    final PermittedService permittedService;

    @Override
    public PermissiondetailDto.AllowResDto allow(PermissionDto.ExistReqDto param, Long reqUserId) {
        List<PermissiondetailDto.DetailResDto> list = permissiondetailMapper.access(param);
        System.out.println("list : " + list);
        return PermissiondetailDto.AllowResDto.builder().allowed(!(list == null || list.isEmpty())).build();
    }
    @Override
    public List<PermissiondetailDto.DetailResDto> access(PermissionDto.ExistReqDto param, Long reqUserId) {
        System.out.println("param : " + param.getFunc());
        System.out.println("param : " + param.getUserId());
        return detailList(permissiondetailMapper.access(param), (long) -200);
    }

    @Override
    public DefaultDto.CreateResDto toggle(PermissiondetailDto.ToggleReqDto param, Long reqUserId) {
        Permissiondetail permissiondetail = permissiondetailRepository.findByPermissionIdAndTargetAndFunc(param.getPermissionId(), param.getTarget(), param.getFunc());
        if(permissiondetail == null) {
            //없는데 생성하라고 하네!
            if(param.getAlive()){
                return create(PermissiondetailDto.CreateReqDto.builder()
                        .permissionId(param.getPermissionId())
                        .target(param.getTarget())
                        .func(param.getFunc())
                        .build(), reqUserId);
            }
        } else {
            permittedService.isPermitted(reqUserId, target, 120);
            //있는데 바꿔주기!
            permissiondetail.setDeleted(!param.getAlive());
            return permissiondetailRepository.save(permissiondetail).toCreateResDto();
        }
        return DefaultDto.CreateResDto.builder().id((long) -100).build();
    }

    /**/
    @Override
    public DefaultDto.CreateResDto create(PermissiondetailDto.CreateReqDto param, Long reqUserId) {
        DefaultDto.CreateResDto res = permissiondetailRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    @Override
    public void update(PermissiondetailDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Permissiondetail permissiondetail = permissiondetailRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        permissiondetail.update(param);
        permissiondetailRepository.save(permissiondetail);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(PermissiondetailDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }
    @Override
    public void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(DefaultDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

    public PermissiondetailDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 200);
        PermissiondetailDto.DetailResDto res = permissiondetailMapper.detail(param.getId());
        return res;
    }
    @Override
    public PermissiondetailDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    @Override
    public List<PermissiondetailDto.DetailResDto> list(PermissiondetailDto.ListReqDto param, Long reqUserId) {
        return detailList(permissiondetailMapper.list(param), reqUserId);
    }
    public List<PermissiondetailDto.DetailResDto> detailList(List<PermissiondetailDto.DetailResDto> list, Long reqUserId){
        List<PermissiondetailDto.DetailResDto> newList = new ArrayList<>();
        for(PermissiondetailDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(PermissiondetailDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(permissiondetailMapper.pagedListCount(param));
        res.setList(detailList(permissiondetailMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<PermissiondetailDto.DetailResDto> scrollList(PermissiondetailDto.ScrollListReqDto param, Long reqUserId) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        /*if("title".equals(param.getOrderby())){
            String mark = param.getMark();
            if(mark != null && !mark.isEmpty()){
                PermissiondetailDto.DetailResDto permissiondetail = permissiondetailMapper.detail(Long.parseLong(mark));
                if(permissiondetail != null){
                    mark = permissiondetail.getTitle() + "_" + permissiondetail.getId();
                    param.setMark(mark);
                }
            }
        }*/

        return detailList(permissiondetailMapper.scrollList(param), reqUserId);
    }


}
