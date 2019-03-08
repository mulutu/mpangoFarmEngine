package com.mpangoEngine.core.dao.impl;

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
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.MyUser;

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
		String Query = "SELECT id, confirmationToken, email, enabled, firstName, lastName, username  FROM users WHERE username = "
				+ username;

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
	public MyUser findUserByEmail(String email) {
		String Query = "SELECT id, confirmationToken, email, enabled, firstName, lastName, username  FROM users WHERE email = "
				+ email;

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
	public List<String> getRoleNames(long userId) {
		String sql = "SELECT `role`.name FROM `user_role`, `role` WHERE role.id=user_role.role_id AND user_role.user_id= ? ";
		Object[] params = new Object[] { userId };
		List<String> roles = this.getJdbcTemplate().queryForList(sql, params, String.class);
		return roles;
	}

	@Override
	public int saveUser(MyUser user) {
		String sql = "INSERT INTO users " + "(`confirmationToken`, `email`, `password`,`username`) "
				+ "VALUES (?,?,?,?)";

		logger.debug("UserDaoImpl->saveUser() >>> user {} ", user);

		Object[] params = { user.getConfirmationToken(), user.getEmail(), user.getPassword(), user.getUsername() };
		int[] types = { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };

		return getJdbcTemplate().update(sql, params, types);
	}

	@Override
	public MyUser findByConfirmationToken(String confirmationToken) {
		MyUser user = new MyUser();
		return user;
	}
}
