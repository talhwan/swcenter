package com.thc.sprbasic2025.mapper;

import com.thc.sprbasic2025.dto.PermissionuserDto;

import java.util.List;

public interface PermissionuserMapper {
	PermissionuserDto.DetailResDto detail(Long id);
	List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param);

	List<PermissionuserDto.DetailResDto> pagedList(PermissionuserDto.PagedListReqDto param);
	int pagedListCount(PermissionuserDto.PagedListReqDto param);
	List<PermissionuserDto.DetailResDto> scrollList(PermissionuserDto.ScrollListReqDto param);
}