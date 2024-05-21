package spring.bookList.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import spring.bookList.Service.BookListService;
import spring.bookList.Service.UserDetailsServiceImpl;
import spring.bookList.model.Books;
import spring.bookList.model.BookListCreate;
import spring.bookList.model.BookListUpdateRequest;
import spring.bookList.model.SignupForm;
import spring.bookList.model.User;
import spring.bookList.model.UserCreate;
import spring.bookList.model.UserUpdateRequest;

@Controller
@RequestMapping("/")
public class BookListController {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate npJdbcTemplate;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private BookListService bookListService;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@GetMapping("/")
	public String index(Model model) {
		List<Books> bookLists = bookListService.searchAll();
		model.addAttribute("bookLists", bookLists);
		return "index";			
	}
	
	@GetMapping("/bookList/{id}")
	public String bookList(@PathVariable int id, Model model) {
		Books bookList = bookListService.findById(id);
		model.addAttribute("bookList", bookList);
		return "bookList";
	}
	
	@GetMapping("/form")
	public String form(BookListCreate bookListCreate) {
		return "form";
	}
	
	@PostMapping("/create")
	public String create(@Valid BookListCreate bookListCreate, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "form";
		}
		
		if(bookListService.isExistBook(bookListCreate.getTitle())) {
			model.addAttribute("createError", "タイトル" + bookListCreate.getTitle() + "は既に登録されています");
			return "form";
		}
		
		try {
			bookListService.register(bookListCreate.getTitle(),bookListCreate.getAuthor(),bookListCreate.getPublisher(),
					bookListCreate.getType(),bookListCreate.getContents());
		} catch (DataAccessException e) {
			model.addAttribute("createError", "登録に失敗しました");
			return "form";
		}
		
		return "redirect:/";
	}
	
	
	  @GetMapping("/bookList/{id}/edit")
	  public String bookEdit(@PathVariable int id, Model model) {
		  Books bookList = bookListService.findById(id);
		  BookListUpdateRequest bookListUpdateRequest = new BookListUpdateRequest();
		  bookListUpdateRequest.setId(bookList.getId());
		  bookListUpdateRequest.setTitle(bookList.getTitle());
		  bookListUpdateRequest.setAuthor(bookList.getAuthor());
		  bookListUpdateRequest.setPublisher(bookList.getPublisher());
		  bookListUpdateRequest.setType(bookList.getType());
		  bookListUpdateRequest.setContents(bookList.getContents());
		  model.addAttribute("bookListUpdateRequest", bookListUpdateRequest); 
		  
		  return  "bookListEdit";
	  }
	 
	
	@RequestMapping(value = "/bookList/update", method = RequestMethod.POST)
	public String bookListUpdate(@Validated @ModelAttribute BookListUpdateRequest bookListUpdateRequest, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for(ObjectError error : bindingResult.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "bookListEdit";
		}
		
		bookListService.update(bookListUpdateRequest);
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/account")
	public String account(Model model) {
		String sql = "SELECT id, name, authority FROM users";
		
		RowMapper<User> rowMapper = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User userList = new User();
				userList.setId(rs.getInt("id"));
				userList.setName(rs.getString("name"));
				userList.setAuthority(rs.getString("authority"));
				return userList;
			}
		};
		
		List<User> userLists = jdbcTemplate.query(sql,  rowMapper);
		model.addAttribute("userLists", userLists);
		return "userlist";
	}
	
	@GetMapping("/account/signup")
	public String newSignup(SignupForm signupForm) {
		return "signup";
	}
	
	@PostMapping("/account/signup")
	public String signup(@Validated SignupForm signupForm, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "signup";
		}
		
		if(userDetailsServiceImpl.isExistUser(signupForm.getUsername())) {
			model.addAttribute("signupError", "ユーザー名" + signupForm.getUsername() + "は既に登録されています");
			return "signup";
		}
		
		try {
			userDetailsServiceImpl.register(signupForm.getUsername(),signupForm.getPassword(), signupForm.getAuthority());
		} catch (DataAccessException e) {
			model.addAttribute("signupError", "ユーザー登録に失敗しました");
			return "signup";
		}
		
		return "redirect:/account";
	}
	
	@GetMapping("/account/{id}/edit")
    public String edit(@PathVariable int id, Model model) {
		User user = userDetailsServiceImpl.findById(id);
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.setId(user.getId());
		userUpdateRequest.setName(user.getName());
		userUpdateRequest.setPassword(user.getPassword());
		userUpdateRequest.setAuthority(user.getAuthority());
		model.addAttribute("userUpdateRequest", userUpdateRequest);
		return "userEdit";
    }
    
	@RequestMapping(value="/account/update", method=RequestMethod.POST) 
	public String userUpdate(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result, Model model) {		
		if(result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			
			for(ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "userEdit";
		}
		userDetailsServiceImpl.updateAccount(userUpdateRequest);
		return "redirect:/account";
	}
}
