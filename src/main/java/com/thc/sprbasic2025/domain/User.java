package com.thc.sprbasic2025.domain;

import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Table(indexes = {@Index(columnList = "deleted, username, rfrom")})
@Entity
public class User extends AuditingFields{
    @Column(length = 100, unique = true) String username;
    String password;
    @Column(unique = true, nullable = false) String code;
    String name;
    @Column(unique = true, nullable = false) String nick;
    String phone;
    Integer birthyear;
    String gender;
    String region;
    String img;

    Integer rfrom; // 1000 수동 가입, 2100 전화번호, 3100구글 3200애플 3300카카오 3400네이버 3500깃허브
    String fcm;

    protected User(){}
    private User(Boolean deleted, String username, String password, String code, String name, String nick, String phone, Integer birthyear, String gender, String region, String img, Integer rfrom, String fcm){
        this.deleted = deleted;
        this.username = username;
        this.password = password;
        this.code = code;
        this.name = name;
        this.nick = nick;
        this.phone = phone;
        this.birthyear = birthyear;
        this.gender = gender;
        this.region = region;
        this.img = img;

        this.rfrom = rfrom;
        this.fcm = fcm;
    }
    public static User of(String username, String password, String code, String name, String nick, String phone, Integer birthyear, String gender, String region, String img, Integer rfrom){
        return new User(false, username, password, code, name, nick, phone, birthyear, gender, region, img, rfrom, null);
    }
    public DefaultDto.CreateResDto toCreateResDto() {
        return DefaultDto.CreateResDto.builder().id(getId()).build();
    }

    public void update(UserDto.UpdateReqDto param){
        if(param.getDeleted() != null){ setDeleted(param.getDeleted()); }
        if(param.getPassword() != null){ setPassword(param.getPassword()); }
        if(param.getCode() != null){ setCode(param.getCode()); }
        if(param.getName() != null){ setName(param.getName()); }
        if(param.getNick() != null){ setNick(param.getNick()); }
        if(param.getPhone() != null){ setPhone(param.getPhone()); }
        if(param.getBirthyear() != null){ setBirthyear(param.getBirthyear()); }
        if(param.getGender() != null){ setGender(param.getGender()); }
        if(param.getRegion() != null){ setRegion(param.getRegion()); }
        if(param.getImg() != null){ setImg(param.getImg()); }
        if(param.getFcm() != null){ setFcm(param.getFcm()); }
    }
}
