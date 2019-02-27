package com.mpangoEngine.core.dao.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.mpangoEngine.core.dao.FarmDao;
import com.mpangoEngine.core.model.Farm;

@Component
@Transactional
public class FarmDaoImpl  extends JdbcDaoSupport implements FarmDao {
	
	public static final Logger logger = LoggerFactory.getLogger(FarmDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	public FarmDaoImpl(DataSource dataSource) {
	    super();
	    setDataSource(dataSource);
	}


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
	public int save(Farm farm) {
		String sql = "INSERT INTO farm "
				+ "(`id`, `date_created`, `description`, `farm_name`, `location`, `size`, `user_id`) "
				+ "VALUES (?, ?, ?, ?, ?, ? ,?)";		
		
		logger.debug("FarmDaoImpl->save() >>> getId {} ", farm.getId());
		logger.debug("FarmDaoImpl->save() >>> getDateCreated {} ", farm.getDateCreated());
		logger.debug("FarmDaoImpl->save() >>> getDescription {} ", farm.getDescription());
		logger.debug("FarmDaoImpl->save() >>> getFarmName {} ", farm.getFarmName());
		logger.debug("FarmDaoImpl->save() >>> getLocation {} ", farm.getLocation());
		logger.debug("FarmDaoImpl->save() >>> getSize {} ", farm.getSize());
		logger.debug("FarmDaoImpl->save() >>> getUserId {} ", farm.getUserId());
		
		Object[] params = { farm.getId(), new Date(), farm.getDescription(), farm.getFarmName(), farm.getLocation(), farm.getSize(), farm.getUserId() };
		int[] types = {   Types.INTEGER,  Types.DATE, Types.VARCHAR,         Types.VARCHAR,      Types.VARCHAR,      Types.INTEGER,  Types.INTEGER };

		return getJdbcTemplate().update( sql, params, types );			
	}



}
