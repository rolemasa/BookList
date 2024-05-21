package spring.bookList.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.bookList.Repository.BookListRepository;
import spring.bookList.model.Books;
import spring.bookList.model.BookListUpdateRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class BookListService {
	@Autowired
	private BookListRepository bookListRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	// 全検索
	public List<Books> searchAll() {
		return bookListRepository.findAll();
	}
	
	// 主キー検索
	public Books findById(Integer id) {
		return bookListRepository.findById(id).get();
	}
	
	//書籍新規登録
	public void register(String title, String author, String publisher, String type, String contents) {
		String sql ="INSERT INTO books(title, author, publisher, type, contents) VALUES(?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, title, author, publisher, type, contents);
	}
	
	// 書籍情報更新
	public void update(BookListUpdateRequest bookListUpdateRequest) {
		Books bookList = findById(bookListUpdateRequest.getId());
		bookList.setTitle(bookListUpdateRequest.getTitle());
		bookList.setAuthor(bookListUpdateRequest.getAuthor());
		bookList.setPublisher(bookListUpdateRequest.getPublisher());
		bookList.setType(bookListUpdateRequest.getType());
		bookList.setContents(bookListUpdateRequest.getContents());
		bookListRepository.save(bookList);
	}
	
	// 同一書籍登録確認
	public boolean isExistBook(String title) {
    	String sql = "SELECT COUNT(*) FROM books WHERE title = ?";
    	int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { title });
    	if(count == 0) {
    		return false;
    	}
    	
    	return true;
    }
}
