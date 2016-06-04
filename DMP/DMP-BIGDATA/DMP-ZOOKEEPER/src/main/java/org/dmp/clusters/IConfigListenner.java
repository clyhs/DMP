package org.dmp.clusters;

public interface IConfigListenner {

	public void onAdded(String key,String type,String value);
	public void onRemoved(String key);
	public void onChanged(String key,String type,String value);
}
