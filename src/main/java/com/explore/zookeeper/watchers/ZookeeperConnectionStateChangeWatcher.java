package com.explore.zookeeper.watchers;

import com.explore.zookeeper.model.ClusterInfo;
import com.explore.zookeeper.service.interfaces.ISyncService;
import com.explore.zookeeper.service.interfaces.IZookeeperService;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("zookeeperConnectionStateChangeWatcher")
public class ZookeeperConnectionStateChangeWatcher implements IZkStateListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConnectionStateChangeWatcher.class);
    private final IZookeeperService zookeeperService;
    private final ISyncService syncService;

    public ZookeeperConnectionStateChangeWatcher(IZookeeperService zookeeperService,
                                                 ISyncService syncService) {
        this.zookeeperService = zookeeperService;
        this.syncService = syncService;
    }

    @Override
    public void handleStateChanged(Watcher.Event.KeeperState keeperState) throws Exception {
        LOGGER.info("Zookeeper state changed to {}", keeperState);
    }

    @Override
    public void handleNewSession() throws Exception {
        ClusterInfo clusterInfo = ClusterInfo.getInstance();
        String ip = ClusterInfo.getServerHostAndPort();

        zookeeperService.addNodeToCluster(ip, "cluster node");
        clusterInfo.getNodes().add(ip);

        zookeeperService.electMaster(ip);
        clusterInfo.setMaster(zookeeperService.getMaster());

        syncService.syncPersons(clusterInfo.getMaster());
    }

    @Override
    public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
        LOGGER.error("Exception occurred while establishing session with zookeeper");
    }
}