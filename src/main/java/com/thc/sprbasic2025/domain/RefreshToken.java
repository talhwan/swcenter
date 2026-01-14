package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
public class RefreshToken extends AuditingFields{
    Long userId;
    @Column(unique=true) String content;

    protected RefreshToken(){}
    private RefreshToken(Boolean deleted, Long userId, String content){
        this.deleted = deleted;
        this.userId = userId;
        this.content = content;
    }
    public static RefreshToken of(String content, Long userId){
        return new RefreshToken(false, userId,content);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }
}
