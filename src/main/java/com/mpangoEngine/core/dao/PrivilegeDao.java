/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpangoEngine.core.dao;

import com.mpangoEngine.core.model.Privilege;

/**
 *
 * @author jamulutu
 */
public interface PrivilegeDao {
	Privilege findPrivilegeByName(String name);

	public void savePrivilege(Privilege privilege);
}
