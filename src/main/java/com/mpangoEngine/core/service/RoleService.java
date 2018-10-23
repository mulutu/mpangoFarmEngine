package com.mpangoEngine.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mpangoEngine.core.model.Role;

@Service
public interface RoleService {
	List<Role> getUserRoleDetails();
}
