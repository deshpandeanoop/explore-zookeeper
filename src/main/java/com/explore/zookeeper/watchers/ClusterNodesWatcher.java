package com.explore.zookeeper.watchers;

import com.explore.zookeeper.model.ClusterInfo;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("clusterNodesWatcher")
public class ClusterNodesWatcher implements IZkChildListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterNodesWatcher.class);

    @Override
    public void handleChildChange(String path, List<String> clusterNodes) throws Exception {
        LOGGER.info("ClusterNodeWatcher invoked, updating cluster information");
        ClusterInfo.getInstance().getNodes().clear();
        ClusterInfo.getInstance().getNodes().addAll(clusterNodes);
    }
}
