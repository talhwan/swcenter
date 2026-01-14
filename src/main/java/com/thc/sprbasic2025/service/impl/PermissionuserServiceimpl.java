package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Permissionuser;
import com.thc.sprbasic2025.domain.User;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PermissionuserDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.PermissionuserMapper;
import com.thc.sprbasic2025.repository.PermissionuserRepository;
import com.thc.sprbasic2025.repository.UserRepository;
import com.thc.sprbasic2025.service.PermissionuserService;
import com.thc.sprbasic2025.service.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PermissionuserServiceimpl implements PermissionuserService {

    final String target = "permission";

    final PermissionuserRepository permissionuserRepository;
    final UserRepository userRepository;
    final PermissionuserMapper permissionuserMapper;
    final PermittedService permittedService;

    /**/
    @Override
    public DefaultDto.CreateResDto create(PermissionuserDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);

        //유저 아이디 입력한거 최대한 보정하기!
        User user = userRepository.findByUsername(param.getUsername());
        if(user == null){
            user = userRepository.findById(Long.parseLong(param.getUsername())).orElse(null);
        }
        if(user == null){
            throw new RuntimeException("no user");
        }
        param.setUserId(user.getId());

        //혹시 이미 존재하는 데이터 일수도 있으니까 확인!
        Permissionuser permissionuser = permissionuserRepository.findByPermissionIdAndUserId(param.getPermissionId(), param.getUserId());
        if(permissionuser == null) {
            permissionuser = param.toEntity();
        } else {
            permissionuser.setDeleted(false);
        }
        return permissionuserRepository.save(permissionuser).toCreateResDto();
    }

    @Override
    public void update(PermissionuserDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Permissionuser permissionuser = permissionuserRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        permissionuser.update(param);
        permissionuserRepository.save(permissionuser);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(PermissionuserDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }
    @Override
    public void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(DefaultDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

    public PermissionuserDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 200);
        PermissionuserDto.DetailResDto res = permissionuserMapper.detail(param.getId());
        return res;
    }
    @Override
    public PermissionuserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    @Override
    public List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param, Long reqUserId) {
        return detailList(permissionuserMapper.list(param), reqUserId);
    }
    public List<PermissionuserDto.DetailResDto> detailList(List<PermissionuserDto.DetailResDto> list, Long reqUserId){
        List<PermissionuserDto.DetailResDto> newList = new ArrayList<>();
        for(PermissionuserDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(PermissionuserDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(permissionuserMapper.pagedListCount(param));
        res.setList(detailList(permissionuserMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<PermissionuserDto.DetailResDto> scrollList(PermissionuserDto.ScrollListReqDto param, Long reqUserId) {
        param.init();

        //타이틀 로 스크롤 더 요청하는 경우 어쩔수 없이 작업!
        /*if("title".equals(param.getOrderby())){
            String mark = param.getMark();
            if(mark != null && !mark.isEmpty()){
                PermissionuserDto.DetailResDto permissionuser = permissionuserMapper.detail(Long.parseLong(mark));
                if(permissionuser != null){
                    mark = permissionuser.getTitle() + "_" + permissionuser.getId();
                    param.setMark(mark);
                }
            }
        }*/

        return detailList(permissionuserMapper.scrollList(param), reqUserId);
    }


}
