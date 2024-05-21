package spring.bookList.model;

import org.springframework.security.core.authority.AuthorityUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUserDetails extends org.springframework.security.core.userdetails.User {
	private final spring.bookList.model.User user;
	
	//認証処理
	public LoginUserDetails(spring.bookList.model.User user) {
		super(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList("Role_" + user.getAuthority()));
		this.user = user;
	}
	
	public spring.bookList.model.User getUserName(){
		return this.user;
	}

}
