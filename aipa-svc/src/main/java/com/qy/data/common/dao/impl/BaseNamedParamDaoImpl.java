package com.qy.data.common.dao.impl;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * @author qy
 * @date 2016
 */
public class BaseNamedParamDaoImpl {

	protected NamedParameterJdbcTemplate jdbcTemplate;
	
	//protected NamedParameterJdbcTemplate jdbcTemplateReadOnly;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
//	public void setDataSourceReadOnly(DataSource dataSource) {
//		this.jdbcTemplateReadOnly = new NamedParameterJdbcTemplate(dataSource);
//	}
	
	protected int getTableIndex(String indexName, int mod){

		return Math.abs(indexName.hashCode()%mod);
	}
	
	protected int getTableIndex(long indexId, int mod){

		return (int) Math.abs(indexId % mod);
	}

}
