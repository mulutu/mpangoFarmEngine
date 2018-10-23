package com.mpangoEngine.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpangoEngine.core.dao.RoleDao;
import com.mpangoEngine.core.model.Role;
import com.mpangoEngine.core.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao userRoleDao;

	@Override
	public List<Role> getUserRoleDetails() {
		return userRoleDao.getUserRoleDetails();
	}

}
