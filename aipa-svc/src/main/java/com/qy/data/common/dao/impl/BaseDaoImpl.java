package com.qy.data.common.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author qy
 * @date 2016
 */
public class BaseDaoImpl {

	protected JdbcTemplate jdbcTemplate;
	
	protected JdbcTemplate jdbcTemplateReadOnly;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setDataSourceReadOnly(DataSource dataSource) {
		this.jdbcTemplateReadOnly = new JdbcTemplate(dataSource);
	}

	protected <T> T query(String sql, RowMapper<T> mapper, Object... args) {
		List<T> results = this.jdbcTemplate.query(sql, mapper, args);
		if (null == results || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
	protected <T> T queryReadOnly(String sql, RowMapper<T> mapper, Object... args) {
		List<T> results = this.jdbcTemplateReadOnly.query(sql, mapper, args);
		if (null == results || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}
	
	protected int getTableIndex(String indexName, int mod){

		return Math.abs(indexName.hashCode()%mod);
	}
	
	protected int getTableIndex(long indexId, int mod){

		return (int) Math.abs(indexId % mod);
	}

	protected <T> List<T> queryList(String sql, RowMapper<T> mapper, Object... args) {
		List<T> results = this.jdbcTemplate.query(sql, mapper, args);
		if (null == results) {
			return new ArrayList<T>();
		}
		return results;
	}
	
	public static void main(String[] argc) throws IOException{
		System.out.println(Math.abs("8821743970015527040".hashCode()%100));
//		String ex_sql = "SELECT commentId, count(0) FROM `comment`.`comment_action_abc` where commentid=def;";
//		
//		File file = new File("D://ids.txt");
//		BufferedReader reader = new BufferedReader(new FileReader(file));
//		String tempString = null;
//		while ((tempString = reader.readLine()) != null) {
//            // 显示行号;
//            Long index = Long.valueOf(tempString);
//            System.out.println(ex_sql.replace("abc", String.valueOf(Math.abs(index % 100))).replace("def", String.valueOf(index)));
//        }
		
//		for (int i=0;i<100; ++i) {
//			System.out.println("drop TABLE user_feed_" +i + ";");
//			
//		}
		
	}
}
