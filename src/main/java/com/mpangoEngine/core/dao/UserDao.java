package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.mpangoEngine.core.model.MyUser;

public interface UserDao {

	MyUser getUserDetails(int userid);
	
	MyUser findUserByUserName(String username);
	
	MyUser findUserByEmail(String email);
	
	MyUser findByConfirmationToken(String confirmationToken);
	
	List<MyUser> getAllUsers();
	
	public int saveUser(MyUser user);
	
	public List<String> getRoleNames(int userId);
	
	public UserDetails loadUserByUsername(String username);
	
	public void createAccounts(MyUser user);

}
