package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.BoardDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(indexes = {@Index(columnList = "deleted")})
@Entity
public class Board extends AuditingFields{
    Long userId;

    String title;
    String content;

    Integer countread;

    protected Board(){}
    private Board(Long userId, String title, String content, Integer countread) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.countread = countread;
    }
    public static Board of(Long userId, String title, String content){
        return new Board(userId, title, content, 0);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(BoardDto.UpdateReqDto param) {
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getTitle() != null){ setTitle(param.getTitle()); }
        if(param.getContent() != null){ setContent(param.getContent()); }
    }
}
