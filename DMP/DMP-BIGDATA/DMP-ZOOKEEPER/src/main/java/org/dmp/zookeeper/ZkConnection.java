package org.dmp.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
import org.apache.zookeeper.data.Stat;
import org.dmp.zookeeper.exception.ZkException;


public class ZkConnection implements IZkConnection {

    private static final Logger LOG = Logger.getLogger(ZkConnection.class);

    private static final int DEFAULT_SESSION_TIMEOUT = 30000;

    private ZooKeeper zk = null;
    private Lock zookeeperLock = new ReentrantLock();

    private final String servers;
    private final int sessionTimeOut;

    public ZkConnection(String zkServers) {
        this(zkServers, DEFAULT_SESSION_TIMEOUT);
    }

    public ZkConnection(String zkServers, int sessionTimeOut) {
        servers = zkServers;
        this.sessionTimeOut = sessionTimeOut;
    }


    @Override
    public void connect(Watcher watcher) {
        zookeeperLock.lock();
        try {
            if (zk != null) {
                throw new IllegalStateException("zk client has already been started");
            }
            try {
                LOG.debug("Creating new ZookKeeper instance to connect to " + servers + ".");
                zk = new ZooKeeper(servers, sessionTimeOut, watcher);
            } catch (IOException e) {
                throw new ZkException("Unable to connect to " + servers, e);
            }
        } finally {
            zookeeperLock.unlock();
        }
    }

    public void close() throws InterruptedException {
        zookeeperLock.lock();
        try {
            if (zk != null) {
                LOG.debug("Closing ZooKeeper connected to " + servers);
                zk.close();
                zk = null;
            }
        } finally {
            zookeeperLock.unlock();
        }
    }

    public String create(String path, byte[] data, CreateMode mode) throws KeeperException, InterruptedException {
        return zk.create(path, data, Ids.OPEN_ACL_UNSAFE, mode);
    }

    public void delete(String path) throws InterruptedException, KeeperException {
        zk.delete(path, -1);
    }

    public boolean exists(String path, boolean watch) throws KeeperException, InterruptedException {
        return zk.exists(path, watch) != null;
    }

    public List<String> getChildren(final String path, final boolean watch) throws KeeperException, InterruptedException {
        return zk.getChildren(path, watch);
    }

    public byte[] readData(String path, Stat stat, boolean watch) throws KeeperException, InterruptedException {
        return zk.getData(path, watch, stat);
    }

    public Stat writeData(String path, byte[] data) throws KeeperException, InterruptedException {
        return writeData(path, data, -1);
    }

    public Stat writeData(String path, byte[] data, int version) throws KeeperException, InterruptedException {
        return zk.setData(path, data, version);
    }

    public States getZookeeperState() {
        return zk != null ? zk.getState() : null;
    }

    public ZooKeeper getZookeeper() {
        return zk;
    }

    @Override
    public long getCreateTime(String path) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        if (stat != null) {
            return stat.getCtime();
        }
        return -1;
    }

    @Override
    public String getServers() {
        return servers;
    }
}
