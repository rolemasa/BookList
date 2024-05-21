package spring.bookList.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
	// ユーザー名からユーザー情報を取得
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
