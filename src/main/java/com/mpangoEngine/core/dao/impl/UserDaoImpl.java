package com.mpangoEngine.core.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.MyUser;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;



@Component
@Transactional
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	public static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public UserDaoImpl(DataSource dataSource) {
		super();
		setDataSource(dataSource);
	}

	@Override
	public List<MyUser> getAllUsers() {
		List<MyUser> users = new ArrayList<>();
		return users;
	}

	@Override
	public MyUser getUserDetails(int userId) {
		String Query = "SELECT id, confirmationToken, email, enabled, firstName, lastName, username  FROM users WHERE id = "
				+ userId;

		logger.debug("UserDaoImpl->getUserDetails() >>> Query {} ", Query);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		MyUser user = new MyUser();

		for (Map<String, Object> row : rows) {
			user.setId((int) row.get("id"));
			user.setConfirmationToken((String) row.get("confirmationToken"));
			user.setEmail((String) row.get("email"));
			user.setEnabled((Boolean) row.get("enabled"));
			user.setFirstName((String) row.get("firstName"));
			user.setLastName((String) row.get("lastName"));
			user.setUsername((String) row.get("username"));
		}
		return user;
	}

	@Override
	public MyUser findUserByUserName(String username) {
		String Query = "SELECT id, confirmationToken, email, enabled, firstName, lastName, username, password  FROM users WHERE username = '"
				+ username + "'";

		logger.debug("UserDaoImpl->getUserDetails() >>> Query {} ", Query);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		MyUser user = new MyUser();

		for (Map<String, Object> row : rows) {
			user.setId((int) row.get("id"));
			user.setConfirmationToken((String) row.get("confirmationToken"));
			user.setEmail((String) row.get("email"));
			user.setEnabled((Boolean) row.get("enabled"));
			user.setFirstName((String) row.get("firstName"));
			user.setLastName((String) row.get("lastName"));
			user.setUsername((String) row.get("username"));
			user.setPassword((String) row.get("password"));
		}
		return user;
	}

	@Override
	public MyUser findUserByEmail(String email) {
		String Query = "SELECT id, confirmationToken, email, enabled, firstName, lastName, username  FROM users WHERE email = '"
				+ email + "'";

		logger.debug("UserDaoImpl->getUserDetails() >>> Query {} ", Query);

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(Query);

		MyUser user = new MyUser();

		for (Map<String, Object> row : rows) {
			user.setId((int) row.get("id"));
			user.setConfirmationToken((String) row.get("confirmationToken"));
			user.setEmail((String) row.get("email"));
			user.setEnabled((Boolean) row.get("enabled"));
			user.setFirstName((String) row.get("firstName"));
			user.setLastName((String) row.get("lastName"));
			user.setUsername((String) row.get("username"));
		}
		return user;
	}

	@Override
	public List<String> getRoleNames(int userId) {
		String sql = "SELECT `roles`.name FROM `user_role`, `roles` WHERE roles.id=user_role.role_id AND user_role.user_id= ? ";
		Object[] params = new Object[] { userId };
		List<String> roles = this.getJdbcTemplate().queryForList(sql, params, String.class);
		return roles;
	}

	
	public int saveUser2(MyUser user) {
		String sql = "INSERT INTO users (`confirmationToken`, `email`,`enabled`, `firstName`,`lastname`, `password`,`username`) VALUES (?,?,?,?,?,?,?)";

		logger.debug("UserDaoImpl->saveUser() >>> user {} ", user);

		Object[] params = { user.getConfirmationToken(), user.getEmail(), user.isEnabled(), user.getFirstName(),
				user.getLastName(), user.getPassword(), user.getUsername() };
		int[] types = { Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR };

		return getJdbcTemplate().update(sql, params, types);

	}

	@Override
	public int saveUser(MyUser user) {
		String sql = "INSERT INTO users (`confirmationToken`, `email`,`enabled`, `firstName`,`lastname`, `password`,`username`) VALUES (?,?,?,?,?,?,?)";

		logger.debug("UserDaoImpl->saveUser() >>> user {} ", user);

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			java.sql.PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getConfirmationToken());
			ps.setString(2, user.getEmail());
			ps.setBoolean(3, user.isEnabled());
			ps.setString(4, user.getFirstName());
			ps.setString(5, user.getLastName());
			ps.setString(6, user.getPassword());
			ps.setString(7, user.getUsername());
			return ps;
		}, keyHolder);

		return keyHolder.getKey().intValue();
	}


	@Override
	public MyUser findByConfirmationToken(String confirmationToken) {
		MyUser user = new MyUser();
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		logger.debug("UserDaoImpl->loadUserByUsername() >>> username {} ", username);

		MyUser userEntity = this.findUserByUserName(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return buildUserFromUserEntity(userEntity);
	}

	public UserDetails buildUserFromUserEntity(MyUser MyUser) {

		logger.debug("UserDaoImpl->buildUserFromUserEntity() >>> user {} ", MyUser);

		String username = MyUser.getUsername();
		String password = MyUser.getPassword();
		boolean enabled = MyUser.isEnabled();

		boolean accountNonExpired = MyUser.isEnabled();
		boolean credentialsNonExpired = MyUser.isEnabled();
		boolean accountNonLocked = MyUser.isEnabled();

		int userID = MyUser.getId();

		// Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		List<String> roleNames = this.getRoleNames(userID);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		// UserDetails userDetails = (UserDetails) new User(username, password,
		// grantList);

		UserDetails user = (UserDetails) new User(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, grantList);

		return user;

	}

	@Override
	public void createAccounts(MyUser user) {

		logger.debug("UserDaoImpl->createAccounts() >>> user {} ", user);

		String sql1 = "insert into accounts(`account_name`,`account_code`,`account_type_id`,`user_id`,`description`) values ('Wages', '123458',21,?,'Wages')";
		String sql2 = "insert into accounts(`account_name`,`account_code`,`account_type_id`,`user_id`,`description`) values ('Sales', '163859',17,?,'Sales')";
		String sql3 = "insert into accounts(`account_name`,`account_code`,`account_type_id`,`user_id`,`description`) values ('Cash at Bank', '163459',1,?,'Cash at Bank')";
		String sql4 = "insert into accounts(`account_name`,`account_code`,`account_type_id`,`user_id`,`description`) values ('M-Pesa', '163458',1,?,'M-Pesa')";

		Object[] params = { user.getId() };
		int[] types = { Types.INTEGER };

		getJdbcTemplate().update(sql1, params, types);
		getJdbcTemplate().update(sql2, params, types);
		getJdbcTemplate().update(sql3, params, types);
		getJdbcTemplate().update(sql4, params, types);
	}


}
