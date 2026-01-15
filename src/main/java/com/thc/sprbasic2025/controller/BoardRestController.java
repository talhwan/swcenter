package com.thc.sprbasic2025.controller;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.BoardDto;
import com.thc.sprbasic2025.security.PrincipalDetails;
import com.thc.sprbasic2025.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardRestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    final BoardService boardService;

    public Long getReqUserId(PrincipalDetails principalDetails){
        if(principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null){
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<DefaultDto.CreateResDto> create(@RequestBody BoardDto.CreateReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(boardService.create(params, reqUserId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody BoardDto.UpdateReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        boardService.update(params, reqUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public ResponseEntity<Void> delete(@RequestBody DefaultDto.DeleteReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        boardService.delete(params, reqUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/list")
    public ResponseEntity<Void> deleteList(@RequestBody DefaultDto.DeleteListReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        boardService.deleteList(params, reqUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //@PreAuthorize("permitAll()")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<BoardDto.DetailResDto> detail(DefaultDto.DetailReqDto params
            , @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(boardService.detail(params, reqUserId));
    }

    //@PreAuthorize("permitAll()")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<List<BoardDto.DetailResDto>> list(BoardDto.ListReqDto params
            , @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(boardService.list(params, reqUserId));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/pagedList")
    public ResponseEntity<DefaultDto.PagedListResDto> pagedList(BoardDto.PagedListReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        logger.info("reqUserId : " + reqUserId);
        return ResponseEntity.ok(boardService.pagedList(params, reqUserId));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/scrollList")
    public ResponseEntity<List<BoardDto.DetailResDto>> scrollList(BoardDto.ScrollListReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(boardService.scrollList(params, reqUserId));
    }

}
