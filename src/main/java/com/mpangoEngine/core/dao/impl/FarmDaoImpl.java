package com.mpangoEngine.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.model.Farm;

@Component
@Transactional
public class FarmDaoImpl implements FarmDao {
	
	public static final Logger logger = LoggerFactory.getLogger(FarmDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Farm findFarmById(int id) {
		String query = "SELECT * FROM farm WHERE id = ?";

		Farm farm = (Farm) jdbcTemplate.queryForObject(query, new Object[] { id }, new BeanPropertyRowMapper(Farm.class));

		return farm;
	}

	@Override
	public List<Farm> findAllById(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Farm> findAllFarmsByUserId(int userId) {
		String query = "SELECT id, date_created, description, farm_name, location, size, user_id FROM farm WHERE user_id =" + userId;
		
		List<Farm> farms = new ArrayList<Farm>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

		for (Map<String, Object> row : rows) {

			Farm farmObj = new Farm();
			
			farmObj.setId( (int) row.get("id"));
			farmObj.setDateCreated((Date) row.get("date_created"));
			farmObj.setDescription((String) row.get("description"));
			farmObj.setFarmName((String) row.get("farm_name"));
			farmObj.setLocation((String) row.get("location"));
			farmObj.setSize((int) row.get("size"));
			farmObj.setUserId((int) row.get("user_id"));

			farms.add(farmObj);
		}
		return farms;
	}

	@Override
	public void save(Farm farm) {
		// TODO Auto-generated method stub
		
	}



}
