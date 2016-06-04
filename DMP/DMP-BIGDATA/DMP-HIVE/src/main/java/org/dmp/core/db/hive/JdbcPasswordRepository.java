package org.dmp.core.db.hive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

@Repository
public class JdbcPasswordRepository implements PasswordRepository, ResourceLoaderAware {

	private @Autowired JdbcOperations jdbcOperations;

	private @Value("${hive.table}")	String tableName;
	
	private ResourceLoader resourceLoader;

	public Long count() {
		return jdbcOperations.queryForLong("select count(*) from " + tableName);
	}

	public void processPasswordFile(String inputFile) {
		//TODO 
		
		SimpleJdbcTestUtils.executeSqlScript(new SimpleJdbcTemplate(jdbcOperations),
				resourceLoader.getResource(inputFile),
				true);
		

	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
