package org.dmp.conf;

public class Config<T> {
	
	public Config(String key){
		this.key=key;
	}
	
	public Config(String key,T value){
		this.key=key;
		this.value=value;
	}
	private String key;
	private T value;
	private String type;
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	public String getKey() {
		return key;
	}
	
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	
	
}
