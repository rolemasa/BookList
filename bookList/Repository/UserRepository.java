package spring.bookList.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import spring.bookList.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("select t from User t WHERE t.name = ?1")
	User findUser(String name);
}
