package spring.bookList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import spring.bookList.Service.LoginUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private LoginUserDetailsService loginUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		  // アクセス権限の設定
		http.authorizeRequests()
			// 制限なし
        	.antMatchers("/login").permitAll()
			// '/admin'は、'ADMIN'ロールのみアクセス可
    		.antMatchers("/account/**").hasAuthority("Role_ADMIN")
        	// 他は制限あり
        	.anyRequest().authenticated();

		http.formLogin()
			// 制限なし
			.loginPage("/login")	
			.defaultSuccessUrl("/")
			.permitAll();
		
		http.logout()
		.permitAll()
        // ログアウト処理のURL
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        // ログアウト成功時の遷移先URL
        .logoutSuccessUrl("/login")
        // ログアウト時に削除するクッキー名
        .deleteCookies("JSESSIONID")
        // ログアウト時のセッション破棄を有効化
        .invalidateHttpSession(true);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/h2-console/**");
		web.ignoring().antMatchers("/css/**");
	}
	
	//入力されたパスワードをBCrypt方式でハッシュ化するメソッド
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // パスワードの暗号化用に、BCryptを使用
        return new BCryptPasswordEncoder();
      }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      // インメモリでユーザ認証処理
      auth.userDetailsService(loginUserDetailsService).passwordEncoder(passwordEncoder());

    }
}
