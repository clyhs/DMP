package org.dmp.core.db.hive;


public interface PasswordRepository {

	Long count();
	
	void processPasswordFile(String inputFile);

}