package spring.bookList;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecuritySession {

	public String getUsername() {
		//SecurityContextHolderから
		//org.springframework.secyrity.core.Authenticationオブジェクトを取得
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if(authentication != null) {
			//AuthenticationオブジェクトからUserDetailsオブジェクトを取得
			Object principal = authentication.getPrincipal();
			if(principal instanceof UserDetails) {
				//UserDetailsオブジェクトから、ユーザー名を取得
				return ((UserDetails) principal).getUsername();
			}
		}
		return null;
	}
}
