package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Project;

@Transactional
@Repository
public interface ProjectDao {
	
	Project findProjectById(int projid);
	
	List<Project> findAll();
	
	List<Project> findAllProjectsByUser(int userId);
	
	boolean existsById(int id);
	
	int save(Project project);
	
}
