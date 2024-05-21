	package spring.bookList.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class UserCreate implements Serializable {
	
	@NotBlank(message = "ユーザー名を入力してください")
	@Size(max=100, message="ユーザー名は100文字以内で入力してください")
	private String name;
	
	@NotBlank(message = "パスワードを入力してください")
	@Size(min = 8, message = "パスワードは8文字以上で入力してください")
	private String password;
	
	private String authority;
}
