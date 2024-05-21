package spring.bookList.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupForm {
	@NotBlank(message = "ユーザー名を入力してください")
	@Size(min = 1, max = 50, message = "ユーザー名は1文字以上50文字以下で入力してください")
	private String username;
	
	@NotBlank(message = "パスワードを入力してください")
	@Size(min = 6, max = 20, message = "パスワードは6文字以上20文字以下で入力してください")
	private String password;
	
	private String authority;
	
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
