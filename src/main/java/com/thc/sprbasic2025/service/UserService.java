package com.thc.sprbasic2025.service;

import com.thc.sprbasic2025.dto.UserDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    boolean nick(UserDto.NickReqDto param, Long reqUserId);
    DefaultDto.CreateResDto signup(UserDto.CreateReqDto param, Long reqUserId);
    /**/
    DefaultDto.CreateResDto create(UserDto.CreateReqDto param, Long reqUserId);
    void update(UserDto.UpdateReqDto param, Long reqUserId);
    void delete(DefaultDto.DeleteReqDto param, Long reqUserId);
    UserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId);
    List<UserDto.DetailResDto> list(UserDto.ListReqDto param, Long reqUserId);
    DefaultDto.PagedListResDto pagedList(UserDto.PagedListReqDto param, Long reqUserId);
    List<UserDto.DetailResDto> scrollList(UserDto.ScrollListReqDto param, Long reqUserId);
}
