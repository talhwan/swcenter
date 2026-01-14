package com.thc.sprbasic2025.mapper;

import com.thc.sprbasic2025.dto.PopupDto;

import java.util.List;

public interface PopupMapper {
	/**/
	PopupDto.DetailResDto detail(Long id);
	List<PopupDto.DetailResDto> list(PopupDto.ListReqDto param);

	List<PopupDto.DetailResDto> pagedList(PopupDto.PagedListReqDto param);
	int pagedListCount(PopupDto.PagedListReqDto param);
	List<PopupDto.DetailResDto> scrollList(PopupDto.ScrollListReqDto param);
}