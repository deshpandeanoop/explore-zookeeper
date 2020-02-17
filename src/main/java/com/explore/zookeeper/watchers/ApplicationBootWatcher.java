package com.explore.zookeeper.watchers;

import com.explore.zookeeper.enums.ZNodes;
import com.explore.zookeeper.model.ClusterInfo;
import com.explore.zookeeper.service.interfaces.ISyncService;
import com.explore.zookeeper.service.interfaces.IZookeeperService;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBootWatcher implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBootWatcher.class);
    private final IZookeeperService zookeeperService;
    private final IZkChildListener clusterNodesWatcher;
    private final IZkChildListener masterNodeWatcher;
    private final IZkStateListener stateListener;
    private final ISyncService syncService;

    public ApplicationBootWatcher(IZookeeperService zookeeperService,
                                 @Qualifier("clusterNodesWatcher") IZkChildListener clusterNodesWatcher,
                                 @Qualifier("masterNodeWatcher") IZkChildListener masterNodeWatcher,
                                 @Qualifier("zookeeperConnectionStateChangeWatcher") IZkStateListener stateListener,
                                  ISyncService syncService) {
        this.zookeeperService = zookeeperService;
        this.clusterNodesWatcher = clusterNodesWatcher;
        this.masterNodeWatcher = masterNodeWatcher;
        this.stateListener = stateListener;
        this.syncService = syncService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOGGER.info("Application bootstrap watcher invoked");
        zookeeperService.createClusterRoot();
        zookeeperService.createLeaderRoot();

        ClusterInfo clusterInfo = ClusterInfo.getInstance();
        String ip = ClusterInfo.getServerHostAndPort();

        zookeeperService.addNodeToCluster(ip, "cluster node");
        clusterInfo.getNodes().add(ip);

        zookeeperService.electMaster(ip);
        clusterInfo.setMaster(zookeeperService.getMaster());

        syncService.syncPersons(clusterInfo.getMaster());

        zookeeperService.registerChildrenChangeWatcher(clusterNodesWatcher, ZNodes.CLUSTER_ROOT.val());
        zookeeperService.registerChildrenChangeWatcher(masterNodeWatcher, ZNodes.LEADER_ROOT.val());
        zookeeperService.registerSessionStateWatcher(stateListener);
    }
}