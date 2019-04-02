package com.mpangoEngine.core.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mpangoEngine.core.model.Farm;

@Transactional
@Repository
public interface FarmDao {
	
	int save(Farm farm);
	
	int updateFarm(Farm farm);
	
	Farm findFarmById(int id);
	
	List<Farm> findAllById(int userId);	
	
	public List<Farm> findAllFarmsByUserId(int userId);
}
