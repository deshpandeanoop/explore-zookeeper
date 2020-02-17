package com.explore.zookeeper.watchers;

import com.explore.zookeeper.model.ClusterInfo;
import com.explore.zookeeper.service.interfaces.IZookeeperService;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("masterNodeWatcher")
public class MasterNodeWatcher implements IZkChildListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MasterNodeWatcher.class);
    private final IZookeeperService zookeeperService;

    public MasterNodeWatcher(IZookeeperService zookeeperService) {
        this.zookeeperService = zookeeperService;
    }

    @Override
    public void handleChildChange(String path, List<String> list) throws Exception {
        LOGGER.info("Master has been crashed, trying to be master");

        zookeeperService.electMaster(ClusterInfo.getServerHostAndPort());
        ClusterInfo.getInstance().setMaster(zookeeperService.getMaster());

        LOGGER.info("Master has been elected and updated cluster info");
    }
}