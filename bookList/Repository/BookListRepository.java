package spring.bookList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.bookList.model.Books;
import spring.bookList.model.User;

@Repository
public interface BookListRepository extends JpaRepository<Books, Integer> {
	/*
	 * @Query("select t from books t WHERE t.title = ?1") Books findTitle(String
	 * title);
	 */
}
