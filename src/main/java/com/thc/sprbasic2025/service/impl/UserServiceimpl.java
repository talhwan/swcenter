package com.thc.sprbasic2025.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.thc.sprbasic2025.domain.User;
import com.thc.sprbasic2025.dto.UserDto;
import com.thc.sprbasic2025.dto.DefaultDto;
import com.thc.sprbasic2025.exception.NoMatchingDataException;
import com.thc.sprbasic2025.mapper.UserMapper;
import com.thc.sprbasic2025.repository.UserRepository;
import com.thc.sprbasic2025.security.AuthService;
import com.thc.sprbasic2025.security.ExternalProperties;
import com.thc.sprbasic2025.service.PermittedService;
import com.thc.sprbasic2025.service.UserService;
import com.thc.sprbasic2025.util.MailBox;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserServiceimpl implements UserService {

    final String target = "user";

    final UserRepository userRepository;
    final UserMapper userMapper;
    final AuthService authService;
    final BCryptPasswordEncoder bCryptPasswordEncoder;
    final PermittedService permittedService;
    final MailBox mailBox;
    final ExternalProperties externalProperties;

    @Override
    public String google(String idTokenString) {

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(externalProperties.getGoogleClientId())) // application.yml로 주입
                .build();

        String googleSub = null;
        String name = null;
        String email = null;

        try{
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID token");
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            googleSub = payload.getSubject();      // Google 고유 ID
            email = payload.getEmail();
            name = (String) payload.get("name");

            System.out.println("GOOGLE SUB: " + googleSub);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(googleSub == null){
            // 구글 정보 조회 실패...
            throw new RuntimeException("Google Info not found");
        }

        if(!email.contains("@handong.")){
            // 구글 정보 조회 실패...
            return "not_valid_email";
        }

        String username = googleSub;
        Long id = null;
        User user = userRepository.findByUsername(username);
        if(user == null){
            String password = UUID.randomUUID().toString().replace("-", "").substring(0,16);
            DefaultDto.CreateResDto res = create(UserDto.CreateReqDto.builder().username(username).password(password).nick(name).rfrom(3100).build(), (long) -200);
            id = res.getId();
        } else {
            id = user.getId();
        }
        String refreshToken = authService.createRefreshToken(id);
        return refreshToken;
    }

    @Override
    public boolean nick(UserDto.NickReqDto param, Long reqUserId) {
        User user = userRepository.findByNick(param.getNick());
        if(user != null){
            if(user.getId().equals(reqUserId)){
                return true;
            }
        }
        return (user == null);
    }
    @Override
    public DefaultDto.CreateResDto signup(UserDto.CreateReqDto param) {
        // 권한이 없어도 하게 해줘야 함..
        param.setRfrom(1000);
        return create(param, (long) -200);
    }

    /**/

    @Override
    public DefaultDto.CreateResDto create(UserDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);
        User user = userRepository.findByUsername(param.getUsername());
        if(user != null){
            throw new RuntimeException("already exist");
        }
        param.setPassword(bCryptPasswordEncoder.encode(param.getPassword()));
        //닉네임, 코드은 그냥 자동 생성..
        String code = UUID.randomUUID().toString().replace("-", "").substring(0,8);
        param.setCode(code);

        String nick = param.getNick();
        if(nick == null || nick.isEmpty()){
            param.setNick(code);
        } else {
            User userForNick = userRepository.findByNick(nick);
            if(userForNick != null){
                param.setNick(code);
            }
        }

        User newUser = userRepository.save(param.toEntity());
        return newUser.toCreateResDto();
    }

    @Override
    public void update(UserDto.UpdateReqDto param, Long reqUserId) {
        if(param.getId() == 0){ param.setId(reqUserId); }
        if(!param.getId().equals(reqUserId)){
            permittedService.isPermitted(reqUserId, target, 120);
        }

        User user = userRepository.findById(param.getId()).orElseThrow(() -> new NoMatchingDataException("no data"));
        if(param.getPassword() != null){ param.setPassword(bCryptPasswordEncoder.encode(param.getPassword())); }
        user.update(param);

        //System.out.println(user.getBirthyear());

        userRepository.save(user);
    }

    @Override
    public void delete(DefaultDto.DeleteReqDto param, Long reqUserId) {
        update(UserDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }

    public UserDto.DetailResDto get(DefaultDto.DetailReqDto param, Long reqUserId) {
        //본인 정보인 경우 확인
        if(!param.getId().equals(reqUserId)){
            permittedService.isPermitted(reqUserId, target, 200);
        }
        UserDto.DetailResDto res = userMapper.detail(param.getId());
        return res;
    }
    @Override
    public UserDto.DetailResDto detail(DefaultDto.DetailReqDto param, Long reqUserId) {
        //본인 정보인 경우 확인
        if(param.getId() == 0){ param.setId(reqUserId); }
        return get(param, reqUserId);
    }

    @Override
    public List<UserDto.DetailResDto> list(UserDto.ListReqDto param, Long reqUserId) {
        return detailList(userMapper.list(param),reqUserId);
    }
    public List<UserDto.DetailResDto> detailList(List<UserDto.DetailResDto> list, Long reqUserId){
        List<UserDto.DetailResDto> newList = new ArrayList<>();
        for(UserDto.DetailResDto each : list){
            newList.add(get(DefaultDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    @Override
    public DefaultDto.PagedListResDto pagedList(UserDto.PagedListReqDto param, Long reqUserId) {
        DefaultDto.PagedListResDto res = param.init(userMapper.pagedListCount(param));
        res.setList(detailList(userMapper.pagedList(param), reqUserId));
        return res;
    }

    @Override
    public List<UserDto.DetailResDto> scrollList(UserDto.ScrollListReqDto param, Long reqUserId) {
        param.init();
        return detailList(userMapper.scrollList(param), reqUserId);
    }


}
