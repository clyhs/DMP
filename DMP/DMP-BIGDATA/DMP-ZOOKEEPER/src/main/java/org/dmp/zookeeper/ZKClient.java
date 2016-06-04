package org.dmp.zookeeper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.dmp.conf.BaseConstants;
import org.dmp.conf.Configuration;
import org.dmp.util.ExceptionUtil;
import org.dmp.util.SystemUtils;
import org.dmp.zookeeper.exception.ZkException;
import org.dmp.zookeeper.exception.ZkInterruptedException;
import org.dmp.zookeeper.exception.ZkNoNodeException;
import org.dmp.zookeeper.exception.ZkNodeExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZKClient implements Watcher {
	
	private static ZKClient instance=null;
	private static final Logger LOG = LoggerFactory.getLogger(ZKClient.class);
	private IZkConnection connection;
	private ZkLock zkLock = new ZkLock();
	private KeeperState currentState;
	private IZKListener zkListener;
	private static Configuration config;
	
	/**
	 * 获得zkclient的链接实例,全局唯一行
	* @Title: getInstance
	* @Description: TODO
	* @param @return    设定文件
	* @return ZKClient    返回类型
	* @throws
	 */
	public static synchronized ZKClient getInstance() {
		if(null==instance){
			config=Configuration.getIntance();
			String zookeeperQuorum=config.getString(BaseConstants.ZOOKEEPR_QUORUM, "");
			instance=new ZKClient(zookeeperQuorum);
		}
		return instance;
	}
	
	private List<IZKListener> nodeListeners=new CopyOnWriteArrayList<IZKListener>();
	private List<String> listenerPath=new CopyOnWriteArrayList<String>();
	
	/**
	 * 新增监听路径
	* @Title: addListenerPath
	* @Description: TODO
	* @param @param path    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void addListenerPath(String path){
		if(!listenerPath.contains(path)){
			listenerPath.add(path);
		}
	}

	/**
	 * 设置当前状态
	* @Title: setCurrentState
	* @Description: TODO
	* @param @param currentState    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void setCurrentState(KeeperState currentState) {
		zkLock.lock();
		try {
			this.currentState = currentState;
		} finally {
			zkLock.unlock();
		}
	}

	protected ZKClient(String server)  {
		connection = new ZkConnection(server);
		connection.connect(this);
		SystemUtils.addShutdownHook(new ZKShutdownHook());
	}

	/**
	 * 
	* @Title: createPersistent 创建zk节点
	* @param @param path
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void createPersistent(String path) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		createPersistent(path, true);
	}
	/**
	 * 
	* @Title: createPersistentSequential 创建zk节点
	* @Description: TODO
	* @param @param path
	* @param @return
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return String    返回类型
	* @throws
	 */
	public String  createPersistentSequential(String path) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		return createPersistentSequential(path, true);
	}

	/**
	 * 
	* @Title: createPersistent 创建zk节点
	* @Description: TODO
	* @param @param path
	* @param @param createParents
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void createPersistent(String path, boolean createParents) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		try {
			create(path, null, CreateMode.PERSISTENT);
		} catch (ZkNodeExistsException e) {
			if (!createParents) {
				throw e;
			}
		} catch (NoNodeException e) {
			if (!createParents) {
				throw e;
			}
			String parentDir = path.substring(0, path.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createPersistent(path, createParents);
		}catch(ZkNoNodeException e){
			if (!createParents) {
				throw e;
			}
			String parentDir = path.substring(0, path.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createPersistent(path, createParents);
		}
	}
	
	/**
	 * 
	* @Title: createPersistentSequential 创建zk节点
	* @Description: TODO
	* @param @param path
	* @param @param createParents
	* @param @return
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return String    返回类型
	* @throws
	 */
	public String createPersistentSequential(String path, boolean createParents) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		try {
			return create(path, null, CreateMode.PERSISTENT_SEQUENTIAL);
		} catch (ZkNodeExistsException e) {
			if (!createParents) {
				throw e;
			}
		} catch (NoNodeException e) {
			if (!createParents) {
				throw e;
			}
			String tmpPath=path;
			if(tmpPath.trim().endsWith("/")){
				tmpPath=tmpPath.substring(0, tmpPath.lastIndexOf("/"));
			}
			
			String parentDir = tmpPath.substring(0, tmpPath.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createPersistent(tmpPath, createParents);
			return createPersistentSequential(path);
		}catch(ZkNoNodeException e){
			if (!createParents) {
				throw e;
			}
			
			String tmpPath=path;
			if(tmpPath.trim().endsWith("/")){
				tmpPath=tmpPath.substring(0, tmpPath.lastIndexOf("/"));
			}
			
			String parentDir = tmpPath.substring(0, tmpPath.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createPersistent(tmpPath, createParents);
			return createPersistentSequential(path);
		}
		return null;
	}
	
	/**
	 * 
	* @Title: createEphemeral 创建zk节点
	* @Description: TODO
	* @param @param path
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void createEphemeral(String path) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		createEphemeral(path, true);
	}

	/**
	 * 
	* @Title: createEphemeral 创建zk节点
	* @Description: TODO
	* @param @param path
	* @param @param createParents
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void createEphemeral(String path, boolean createParents) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		try {
			create(path, null, CreateMode.EPHEMERAL);
		} catch (ZkNodeExistsException e) {
			if (!createParents) {
				throw e;
			}
		} catch (NoNodeException e) {
			if (!createParents) {
				throw e;
			}
			String parentDir = path.substring(0, path.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createEphemeral(path, createParents);
		}catch(ZkNoNodeException e){
			if (!createParents) {
				throw e;
			}
			String parentDir = path.substring(0, path.lastIndexOf('/'));
			createPersistent(parentDir, createParents);
			createEphemeral(path, createParents);
		}
	}

	/**
	 * 
	* @Title: create 创建zk节点
	* @Description: TODO 
	* @param @param path 节点路径
	* @param @param data 初始化数据内容
	* @param @param mode 方式
	* @param @return
	* @param @throws IllegalArgumentException
	* @param @throws ZkException
	* @param @throws RuntimeException
	* @param @throws KeeperException
	* @param @throws InterruptedException    设定文件
	* @return String    返回类型
	* @throws
	 */
	public String create(final String path, final byte[] data, final CreateMode mode) throws IllegalArgumentException, ZkException, RuntimeException, KeeperException, InterruptedException {
		if (path == null) {
			throw new NullPointerException("path must not be null.");
		}
		return retryUntilConnected(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return connection.create(path, data, mode);
			}

		});
	}

	/**
	 * 
	* @Title: delete 删除指定
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public boolean delete(final String path) {
		try {
			retryUntilConnected(new Callable<Object>() {

				@Override
				public Object call() throws Exception {
					connection.delete(path);
					return null;
				}
			});

			return true;
		} catch (ZkNoNodeException e) {
			return false;
		}
	}

	/**
	 * 递归删除
	* @Title: deleteRecursive
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public boolean deleteRecursive(String path) {
		List<String> children;
		try {
			children = getChildren(path, false);
		} catch (ZkNoNodeException e) {
			return true;
		}

		for (String subPath : children) {
			if (!deleteRecursive(path + "/" + subPath)) {
				return false;
			}
		}

		return delete(path);
	}
	
	/**
	 * 返回指定路径节点的子节点名单
	* @Title: getChildren
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return List<String>    返回类型
	* @throws
	 */
	public List<String> getChildren(final String path){
		return getChildren(path,hasListeners(path));
	}

	/**
	 * 返回指定节点的子节点名单
	* @Title: getChildren
	* @Description: TODO
	* @param @param path
	* @param @param watch
	* @param @return    设定文件
	* @return List<String>    返回类型
	* @throws
	 */
	public List<String> getChildren(final String path, final boolean watch) {
		if (path == null) {
			throw new NullPointerException("path must not be null.");
		}
		return retryUntilConnected(new Callable<List<String>>() {

			@Override
			public List<String> call() throws Exception {
				return connection.getChildren(path, watch);
			}

		});
	}

	
	public byte[] readData(String path) {
		return readData(path, hasListeners(path));
	}

	public byte[] readData(String path, boolean returnNullIfPathNotExists) {
		try {
			return readData(path, null);
		} catch (ZkNoNodeException e) {
			if (!returnNullIfPathNotExists) {
				throw e;
			}
		}
		return null;
	}

	public byte[] readData(String path, Stat stat) {
		return readData(path, stat, hasListeners(path));
	}
	/**
	 * 
	* @Title: 获取这个 path 对应的目录节点存储的数据
	* @Description: TODO
	* @param @param path 目录节点
	* @param @param stat 版本信息
	* @param @param watch 是否监控
	* @param @return    设定文件
	* @return byte[]    返回类型
	* @throws
	 */
	protected byte[] readData(final String path, final Stat stat, final boolean watch) {
		return retryUntilConnected(new Callable<byte[]>() {

			@Override
			public byte[] call() throws Exception {
				return connection.readData(path, stat, watch);
			}
		});
	}

	/**
	 * 设置数据
	* @Title: writeData
	* @Description: TODO
	* @param @param path
	* @param @param data
	* @param @param expectedVersion
	* @param @return    设定文件
	* @return Stat    返回类型
	* @throws
	 */
	public Stat writeData(final String path, final byte[] data, final int expectedVersion) {
		return (Stat) retryUntilConnected(new Callable<Object>() {

			@Override
			public Object call() throws Exception {
				Stat stat = connection.writeData(path, data, expectedVersion);
				return stat;
			}
		});
	}
	/**
	 * 判断某个 path 是否存在，并设置监控这个目录节点
	* @Title: watchForData
	* @Description: TODO
	* @param @param path    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void watchForData(final String path) {
		retryUntilConnected(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				connection.exists(path, true);
				return null;
			}
		});
	}

	/**
	 * 判断某个 path 是否存在，并设置监控这个目录节点,返回改节点的子节点名单
	 * @param path
	 * @return the current children of the path or null if the zk node with the
	 *         given path doesn't exist.
	 */
	public List<String> watchForChilds(final String path) {

		return retryUntilConnected(new Callable<List<String>>() {
			@Override
			public List<String> call() throws Exception {
				exists(path, true);
				try {
					return getChildren(path, true);
				} catch (ZkNoNodeException e) {
				}
				return null;
			}
		});
	}
	
	/**
	 * 判断某个 path 是否存在，并设置监控这个目录节点,并监控子名单	* @Title: watchForChildren
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return List<String>    返回类型
	* @throws
	 */
	public List<String> watchForChildren(final String path){
		return retryUntilConnected(new Callable<List<String>>() {
			@Override
			public List<String> call() throws Exception {
				exists(path, true);
				try {
					List<String> listChildren= getChildren(path, true);
					String tmp=path;
					if(!path.endsWith("/")){
						tmp=path+"/";
					}
					for(String str:listChildren){
						exists(tmp+str,true);
					}
					return listChildren;
					
				} catch (ZkNoNodeException e) {
				}
				return null;
			}
		});
	}

	/**
	 * 判断某个 path 是否存在，并设置是否监控这个目录节点
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return List<String>    返回类型
	* @throws
	 */
	public boolean exists(final String path, final boolean watch) {
		return retryUntilConnected(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return connection.exists(path, watch);
			}
		});
	}

	/**
	 * 判断某个 path 是否存在，并根据监控目录设置是否监控这个目录节点
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return List<String>    返回类型
	* @throws
	 */
	public boolean exists(final String path) {
		return exists(path, hasListeners(path));
	}

	/**
	 * 新增监控目录
	* @Title: hasListeners
	* @Description: TODO
	* @param @param path
	* @param @return    设定文件
	* @return boolean    返回类型
	* @throws
	 */
	public boolean hasListeners(String path) {
		return listenerPath.contains(path);
	}
	
	public boolean addListener(String path,IZKListener listener){
		nodeListeners.add(listener);
		return true;
	}
	
	public void setListener(IZKListener listener){
		nodeListeners.add(listener);
	}
	
	public void removeListener(String path){
		nodeListeners.remove(path);
	}

	/**
	 * 收到来自Server的Watcher通知后的处理。
	 * (非 Javadoc)
	* <p>Title: process</p>
	* <p>Description: </p>
	* @param event
	* @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
	 */
	@Override
	public void process(WatchedEvent event) {

		System.out.println(event.getPath()+"|"+event.getType());
		boolean stateChanged = event.getPath() == null;
		boolean znodeChanged = event.getPath() != null;
		boolean dataChanged = event.getType() == EventType.NodeDataChanged || event.getType() == EventType.NodeDeleted || event.getType() == EventType.NodeCreated
				|| event.getType() == EventType.NodeChildrenChanged;

		zkLock.lock();
		try {
			if(null!=event.getPath()){
				watchForData(event.getPath());
			}
			if (stateChanged) {
				processStateChanged(event);
			}
			//if(zkListener==null){
			//	return;
			//}
			if (znodeChanged) {
				if(event.getType()==EventType.NodeDeleted){
					for(IZKListener listener:nodeListeners){
						listener.nodeDeleted(event.getPath());
					}
					//zkListener.nodeDeleted(event.getPath());
				}else if(event.getType()==EventType.NodeCreated){
					for(IZKListener listener:nodeListeners){
						listener.nodeCreated(event.getPath());
					}
					//zkListener.nodeCreated(event.getPath());
				}
				
			}
			if (dataChanged) {
				if(event.getType()==EventType.NodeDataChanged){
					byte[] buffer=this.readData(event.getPath());
					for(IZKListener listener:nodeListeners){
						listener.nodeDataChanged(event.getPath(), buffer);
					}
					//zkListener.nodeDataChanged(event.getPath(), buffer);
				}else if(event.getType()==EventType.NodeChildrenChanged){
					watchForChildren(event.getPath());
					for(IZKListener listener:nodeListeners){
						listener.nodeChildrenChanged(event.getPath());
					}
					//zkListener.nodeChildrenChanged(event.getPath());
				}
			}
			
		} finally {
			zkLock.unlock();
		}
	}

	private void processStateChanged(WatchedEvent event) {
		setCurrentState(event.getState());
		if (event.getState() == KeeperState.Expired) {
			try {
				reconnect();
			} catch (InterruptedException e) {
				throw new ZkInterruptedException(e);
			}
		}
	}

	private void processDataChanaged(WatchedEvent event) {
			
	}

	private void reconnect() throws InterruptedException {
		zkLock.lock();
		try {
			connection.close();
			connection.connect(this);
		} finally {
			zkLock.unlock();
		}
	}
	/**
	 * 
	* @Title: retryUntilConnected
	* @Description: TODO
	* @param @param callable
	* @param @return    设定文件
	* @return T    返回类型
	* @throws
	 */
	private <T> T retryUntilConnected(Callable<T> callable) {
		while (true) {
			try {
				return callable.call();
			} catch (ConnectionLossException e) {
				Thread.yield();
				waitUntilConnected();
			} catch (SessionExpiredException e) {
				Thread.yield();
				waitUntilConnected();
			} catch (KeeperException e) {
				throw ZkException.create(e);
			} catch (InterruptedException e) {
				throw new ZkInterruptedException(e);
			} catch (Exception e) {
				throw ExceptionUtil.convertToRuntimeException(e);
			}
		}
	}

	private void waitUntilConnected() throws ZkInterruptedException {
		waitUntilConnected(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
	}

	private boolean waitUntilConnected(long time, TimeUnit timeUnit) throws ZkInterruptedException {
		return waitForKeeperState(KeeperState.SyncConnected, time, timeUnit);
	}

	private boolean waitForKeeperState(KeeperState keeperState, long time, TimeUnit timeUnit) throws ZkInterruptedException {

		Date timeout = new Date(System.currentTimeMillis() + timeUnit.toMillis(time));

		LOG.debug("Waiting for keeper state " + keeperState);
		acquireEventLock();
		try {
			boolean stillWaiting = true;
			while (currentState != keeperState) {
				if (!stillWaiting) {
					return false;
				}
				stillWaiting = zkLock.getStateChangedCondition().awaitUntil(timeout);
			}
			LOG.debug("State is " + currentState);
			return true;
		} catch (InterruptedException e) {
			throw new ZkInterruptedException(e);
		} finally {
			zkLock.unlock();
		}
	}

	private void acquireEventLock() {
		try {
			zkLock.lockInterruptibly();
		} catch (InterruptedException e) {
			throw new ZkInterruptedException(e);
		}
	}
	
	/**
	 * 断开与zk连接
	* @Title: close
	* @Description: TODO
	* @param     设定文件
	* @return void    返回类型
	* @throws
	 */
	public  void close(){
		
		zkLock.lock();
		try{
			try {
				connection.close();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally{zkLock.unlock();}
	}
	
	class ZKShutdownHook extends Thread{

		@Override
		public void run() {
			close();
			LOG.info("ONZKShutdownHook");
		}
		
	}
}
