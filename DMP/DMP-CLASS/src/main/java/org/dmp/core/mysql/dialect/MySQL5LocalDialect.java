package org.dmp.core.mysql.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;

public class MySQL5LocalDialect extends MySQL5InnoDBDialect {

	public MySQL5LocalDialect() {  
		super();  
		registerFunction("convert", new SQLFunctionTemplate(StringType.INSTANCE,  "convert(?1 using ?2)"));  
	}  
		 
}
