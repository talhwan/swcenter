package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.PermissionDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    /**/
    DefaultDto.CreateResDto create(PermissionDto.CreateReqDto param, Long reqUserId);
    void update(PermissionDto.UpdateReqDto param, Long reqUserId);
    void delete(DefaultDto.DeleteReqDto param, Long reqUserId);
    void deleteList(DefaultDto.DeleteListReqDto param, Long reqUserId);
    PermissionDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<PermissionDto.DetailResDto> list(PermissionDto.ListReqDto param, Long reqUserId);
    DefaultDto.PagedListResDto pagedList(PermissionDto.PagedListReqDto param, Long reqUserId);
    List<PermissionDto.DetailResDto> scrollList(PermissionDto.ScrollListReqDto param, Long reqUserId);
}
