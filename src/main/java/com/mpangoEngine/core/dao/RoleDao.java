package com.mpangoEngine.core.dao;

import java.util.List;
import com.mpangoEngine.core.model.Role;

public interface RoleDao {
	List<Role> getUserRoleDetails();
	
	Role findByRole(String role);
	
	public void saveRole(Role role);
}
