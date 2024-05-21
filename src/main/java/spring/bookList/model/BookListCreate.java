package spring.bookList.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BookListCreate implements Serializable {	
	@NotBlank(message = "タイトルを入力してください")
	@Size(max = 80, message = "タイトルは80文字以内で入力してください")
	private String title;
	
	@NotBlank(message = "著者を入力してください")
	@Size(max = 80,message = "著者は80文字以内で入力してください")
	private String author;
	
	@NotBlank(message = "出版社を入力してください")
	@Size(max = 80, message = "出版社は80文字以内で入力してください")
	private String publisher;
	
	private String type;
	
	@NotBlank(message = "内容を入力してください")
	@Size(max = 1000, message ="内容は1000文字以内で入力してください")
	private String contents;

}
