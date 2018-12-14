package com.mpangoEngine.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mpangoEngine.core.dao.PrivilegeDao;
import com.mpangoEngine.core.dao.RoleDao;
import com.mpangoEngine.core.dao.UserDao;
import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.model.Privilege;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	boolean alreadySetup = false;

	@Autowired
	DataSource dataSource;

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao RoleDao;

	@Autowired
	private PrivilegeDao privilegeDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public List<MyUser> getUserDetails(long userid) {
		return userDao.getUserDetails(userid);

	}

	@Override
	@Transactional
	public MyUser findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	@Override
	@Transactional
	public List<MyUser> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void enableUser(MyUser user) {
		// user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		userDao.saveUser(user);
	}

	@Override
	@Transactional
	public void saveUser(MyUser user) {
		if (alreadySetup)
			return;
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(false);

		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

		Role role = RoleDao.findByRole("ROLE_USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(role)));
		userDao.saveUser(user);

		alreadySetup = true;
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {
		Privilege privilege = null;

		try {
			privilege = privilegeDao.findPrivilegeByName(name);
		} catch (NoResultException e) {
			System.out.println("privilege not found >>>>>>>>>>>>>   " + name);
		}
		if (privilege == null) {
			Privilege newprivilege = new Privilege(name);
			privilegeDao.savePrivilege(newprivilege);
		}
		return privilege;
	}

	@Transactional
	private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
		Role role = null;

		try {
			role = RoleDao.findByRole(name);
		} catch (NoResultException e) {
		}
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			RoleDao.saveRole(role);
		}
		return role;
	}

	@Override
	@Transactional
	public Collection<Role> getUserRoles(String username) {
		Collection<Role> roles = null;
		MyUser user = userDao.findUserByUserName(username);
		roles = user.getRoles();
		return roles;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MyUser userEntity = userDao.findUserByUserName(username);
		if (userEntity == null ) {
			throw new UsernameNotFoundException("user not found");
		}			
		return buildUserFromUserEntity(userEntity);	
	}
	
	// http://codehustler.org/blog/spring-security-tutorial-form-login/
	// https://stackoverflow.com/questions/2683308/spring-security-3-database-authentication-with-hibernate/2701722#2701722

	@Transactional
	public UserDetails buildUserFromUserEntity(MyUser MyUser) {

		String username = MyUser.getUsername();
		String password = MyUser.getPassword();
		boolean enabled = MyUser.isEnabled();
		boolean accountNonExpired = MyUser.isEnabled();
		boolean credentialsNonExpired = MyUser.isEnabled();
		boolean accountNonLocked = MyUser.isEnabled();
		
		long userID = MyUser.getId();

		//Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		List<String> roleNames = userDao.getRoleNames(userID);
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (roleNames != null) {
            for (String role : roleNames) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }		
        //UserDetails userDetails = (UserDetails) new User(username, password, grantList); 

        UserDetails user = (UserDetails) new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantList);
		
        return user;
        
	}	

	@Override
	@Transactional
	public MyUser findUserByUserName(String username) throws UsernameNotFoundException {
		MyUser user = userDao.findUserByUserName(username);
		return user;
	}

	@Override
	@Transactional
	public MyUser findByConfirmationToken(String confirmationToken) {
		MyUser user = userDao.findByConfirmationToken(confirmationToken);
		return user;
	}

}