package com.mpangoEngine.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Role;

public interface UserService {

	MyUser getUserDetails(int userid);
	
	List<MyUser> getAllUsers();
	
	MyUser findUserByEmail(String email);	
	
	Collection<Role> getUserRoles(String username);
	
	MyUser findUserByUserName(String username);
	
	MyUser findByConfirmationToken(String confirmationToken);
	
	public void saveUser(MyUser user);
	
	public void enableUser(MyUser user);
	
	//UserDetails loadUserByUsername(String username);
	
	public UserDetails loadUserByUsername(String username);

}
