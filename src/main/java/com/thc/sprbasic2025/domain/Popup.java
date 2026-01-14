package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.PopupDto;
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
public class Popup extends AuditingFields{
    Integer sequence;
    String title;
    String content;
    String url;
    String img;

    protected Popup(){}
    private Popup(Integer sequence, String title, String content, String url, String img){
        this.sequence = sequence;
        this.title = title;
        this.content = content;
        this.url = url;
        this.img = img;
    }
    public static Popup of(Integer sequence, String title, String content, String url, String img){
        return new Popup(sequence, title, content, url, img);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(PopupDto.UpdateReqDto param) {
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getSequence() != null){ setSequence(param.getSequence()); }
        if(param.getTitle() != null){ setTitle(param.getTitle()); }
        if(param.getContent() != null){ setContent(param.getContent()); }
        if(param.getUrl() != null){ setUrl(param.getUrl()); }
        if(param.getImg() != null){ setImg(param.getImg()); }
    }
}
