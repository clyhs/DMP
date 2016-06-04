
package org.dmp.zookeeper;
public interface IZKListener {
	void nodeCreated(String path);
	void nodeDeleted(String path);
	void nodeDataChanged(String path,byte [] value);
	void nodeChildrenChanged(String path);
}
