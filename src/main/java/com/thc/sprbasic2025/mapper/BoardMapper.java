package com.thc.sprbasic2025.mapper;

import com.thc.sprbasic2025.dto.BoardDto;

import java.util.List;

public interface BoardMapper {
	/**/
	BoardDto.DetailResDto detail(Long id);
	List<BoardDto.DetailResDto> list(BoardDto.ListReqDto param);

	List<BoardDto.DetailResDto> pagedList(BoardDto.PagedListReqDto param);
	int pagedListCount(BoardDto.PagedListReqDto param);
	List<BoardDto.DetailResDto> scrollList(BoardDto.ScrollListReqDto param);
}