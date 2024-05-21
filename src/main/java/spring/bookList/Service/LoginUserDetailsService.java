package spring.bookList.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import spring.bookList.Repository.UserRepository;
import spring.bookList.model.LoginUserDetails;
import spring.bookList.model.User;

@Service
public class LoginUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		//入力された名前をキーに「usersテーブル」のレコードを1件取得
		User user = userRepository.findUser(name);
		
		//取得できなかった場合はExceptionを投げる
		if(user == null) {
			throw new AuthenticationCredentialsNotFoundException("ユーザーＩＤまたはパスワードが誤っています");
		}
		
		return new LoginUserDetails(user);
	}

}
