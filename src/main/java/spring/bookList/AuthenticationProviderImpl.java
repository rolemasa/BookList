package spring.bookList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import spring.bookList.Service.LoginUserDetailsService;

@Component
public class AuthenticationProviderImpl implements AuthenticationProvider {
	
	@Autowired
	LoginUserDetailsService loginUserDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationCredentialsNotFoundException {
		authentication.isAuthenticated(); //この時点ではfalse
		
		//入力されたユーザ名とパスワードを取得
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		//userの検索
		UserDetails user = loginUserDetailsService.loadUserByUsername(name);
		
		if(!user.getPassword().equals(password)) {
			throw new AuthenticationCredentialsNotFoundException("ユーザIDまたはパスワードが誤っています");	
		}
		
		//入力されたユーザ名とパスワードを保持するAuthenticationを作成
		UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(user,
					authentication.getCredentials(), user.getAuthorities());
		
		//認証情報にUserDetailsをセットする
		authenticationResult.setDetails(authentication.getDetails());
		authenticationResult.isAuthenticated(); //この時点ではtrue
		return authenticationResult;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		//POSTで送信されたユーザ名とパスワードで認証を行う
		return UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication);
	}
}
