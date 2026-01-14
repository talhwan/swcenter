package com.thc.sprbasic2025.security;

import com.thc.sprbasic2025.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class PrincipalDetails implements UserDetails {
	
	private final User user;
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	public User getUser() {
        return user;
    }

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
    /**
	 *  User Role 파싱하는 함수.
	 *  @return Collection<? extends GrantedAuthority> authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		/*
		user.getRoleList().forEach(userRoleType->{
			authorities.add(()->userRoleType.getRoleType().getTypeName());
		});
		*/
		authorities.add(()->"ROLE_USER");
		return authorities;
	}

}