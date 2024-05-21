package spring.bookList.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import spring.bookList.Repository.UserRepository;
import spring.bookList.model.User;
import spring.bookList.model.UserDetailsImpl;
import spring.bookList.model.UserUpdateRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	try {
            String sql = "SELECT * FROM users WHERE name = ?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, username);
            String password = (String)map.get("password");
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
            return new UserDetailsImpl(username, password, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found.", e);
        }
    }
    
    @Transactional
    public void register(String username, String password, String authority) {
    	String sql = "INSERT INTO users(name, password, authority) VALUES(?, ?, ?)";
    	jdbcTemplate.update(sql, username, passwordEncoder.encode(password), authority);
    }
    
    @Transactional
    public void updateAccount(UserUpdateRequest userUpdateReques) {
    	User user = findById(userUpdateReques.getId());
    	user.setName(userUpdateReques.getName());
    	user.setPassword(passwordEncoder.encode(userUpdateReques.getPassword()));
    	user.setAuthority(userUpdateReques.getAuthority());
    	userRepository.save(user);
    	
    }
    
    public boolean isExistUser(String username) {
    	String sql = "SELECT COUNT(*) FROM users WHERE name = ?";
    	int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { username });
    	if(count == 0) {
    		return false;
    	}
    	
    	return true;
    }
    
    public User findById(int id) {
    	return userRepository.findById(id).get();
    }
}
