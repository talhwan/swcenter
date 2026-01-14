package com.thc.sprbasic2025.dto;

import com.thc.sprbasic2025.domain.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

public class UserDto {

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class NickReqDto{
        String nick;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class LoginReqDto{
        String username;
        String password;
    }

    /**/

    @Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CreateReqDto {
        String username;
        String password;
        String code;
        String name;
        String nick;
        String phone;
        Integer birthyear;
        String gender;
        String region;
        String img;
        Integer rfrom;

        public User toEntity(){
            return User.of(getUsername(), getPassword(), getCode(), getName(), getNick(), getPhone(), getBirthyear(), getGender(), getRegion(), getImg(), getRfrom());
        }
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class UpdateReqDto extends DefaultDto.UpdateReqDto{
        String password;
        String code;
        String name;
        String nick;
        String phone;
        Integer birthyear;
        String gender;
        String region;
        String img;

        Integer rfrom;
        String fcm;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class DetailResDto extends DefaultDto.DetailResDto{
        String username;
        String code;
        String name;
        String nick;
        String phone;
        Integer birthyear;
        String gender;
        String region;
        String img;

        Integer rfrom;
        String fcm;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ListReqDto extends DefaultDto.ListReqDto{
        String code;
        String name;
        Integer birthyear;
        String gender;
        String region;
        Integer rfrom;
        String fcm;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class PagedListReqDto extends DefaultDto.PagedListReqDto{
        String code;
        String name;
        Integer birthyear;
        String gender;
        String region;
        Integer rfrom;
        String fcm;
    }
    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class ScrollListReqDto extends DefaultDto.ScrollListReqDto{
        String code;
        String name;
        Integer birthyear;
        String gender;
        String region;
        Integer rfrom;
        String fcm;
    }
}
