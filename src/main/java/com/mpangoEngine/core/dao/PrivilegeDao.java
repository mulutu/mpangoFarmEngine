package com.mpangoEngine.core.dao;

import com.mpangoEngine.core.model.Privilege;

public interface PrivilegeDao {
	Privilege findPrivilegeByName(String name);

	public void savePrivilege(Privilege privilege);
}
