package com.mpangoEngine.core.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Expense;
import com.mpangoEngine.core.model.Income;
import com.mpangoEngine.core.model.Project;

@Transactional
@Repository
public interface ProjectDao {
	
	Project findProjectById(int projid);
	
	List<Project> findAll();
	
	List<Project> findAllProjectsByUser(int userId);
	
	boolean existsById(int id);
	
	void save(Project project);
	
	List<Income> findAllIncomesByProject(int projid);
	
	List<Expense> findAllExpensesByProject(int projid);
	
	List<Map<String, Object>> findProjectDetails(int projid);


	
	
}
