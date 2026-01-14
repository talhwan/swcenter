package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.FaqDto;
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
public class Faq extends AuditingFields{
    Integer sequence;
    String title;
    String content;
    String img;

    protected Faq(){}
    private Faq(Integer sequence, String title, String content, String img){
        this.sequence = sequence;
        this.title = title;
        this.content = content;
        this.img = img;
    }
    public static Faq of(Integer sequence, String title, String content, String img){
        return new Faq(sequence, title, content, img);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(FaqDto.UpdateReqDto param) {
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getSequence() != null){ setSequence(param.getSequence()); }
        if(param.getTitle() != null){ setTitle(param.getTitle()); }
        if(param.getContent() != null){ setContent(param.getContent()); }
        if(param.getImg() != null){ setImg(param.getImg()); }
    }
}
