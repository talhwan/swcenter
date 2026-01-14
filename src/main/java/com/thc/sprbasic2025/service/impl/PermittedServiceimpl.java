package com.thc.sprbasic2025.service.impl;

import com.thc.sprbasic2025.domain.Permission;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PermissionDto;
import com.thc.sprbasic2025.dto.PermissiondetailDto;
import com.thc.sprbasic2025.exception.NoPermissionException;
import com.thc.sprbasic2025.mapper.PermissionMapper;
import com.thc.sprbasic2025.repository.PermissionRepository;
import com.thc.sprbasic2025.service.PermissionService;
import com.thc.sprbasic2025.service.PermissiondetailService;
import com.thc.sprbasic2025.service.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PermittedServiceimpl implements PermittedService {

    final PermissionMapper permissionMapper;

    @Override
    public void isPermitted(Long userId, String target, int func) {
        // -200인 경우에는, 그냥 무사 통과를 부탁한것!!
        if(userId != (long) -200){
            if(!permitted(PermissionDto.PermittedReqDto.builder().userId(userId).target(target).func(func).build())){
                throw new NoPermissionException("no auth");
            }
        }
    }
    @Override
    public boolean permitted(PermissionDto.PermittedReqDto param) {
        return (permissionMapper.permitted(param) > 0);
        //return true;
    }
}
