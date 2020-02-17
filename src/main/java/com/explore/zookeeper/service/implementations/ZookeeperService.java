package com.explore.zookeeper.service.implementations;

import static com.explore.zookeeper.enums.ZNodes.*;
import com.explore.zookeeper.service.interfaces.IZookeeperService;
import com.explore.zookeeper.utils.StringSerializer;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ZookeeperService implements IZookeeperService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperService.class);
    private final ZkClient zkClient;

    public ZookeeperService() {
        this.zkClient = new ZkClient("localhost:2181", 12000, 3000, new StringSerializer());
        LOGGER.info("Created zookeeper client successfully");
    }

    @Override
    public void createClusterRoot() {
        LOGGER.info("Creating cluster node if doesn't exists");
        if(! zkClient.exists(CLUSTER_ROOT.val())){
            zkClient.create(CLUSTER_ROOT.val(), "cluster node", CreateMode.PERSISTENT);
        }
    }

    @Override
    public void createLeaderRoot() {
        LOGGER.info("Creating master node if doesn't exists");
        if(! zkClient.exists(LEADER_ROOT.val())){
            zkClient.create(LEADER_ROOT.val(), "leader root node", CreateMode.PERSISTENT);
        }
    }

    @Override
    public void electMaster(String hostAndPort) {
        try{
            String zNode = LEADER_ROOT.val()+LEADER_NODE.val();
            zkClient.create(zNode, hostAndPort, CreateMode.EPHEMERAL);
        }catch (ZkNodeExistsException zkNodeExistsException){
            LOGGER.info("Leader has been already elected, Here is elected master {}", getMaster());
        }
    }

    @Override
    public String getMaster() {
        return zkClient.readData(LEADER_ROOT.val() + LEADER_NODE.val(), null);
    }

    @Override
    public void addNodeToCluster(String node, String data) {
        String zNode = CLUSTER_ROOT.val() + "/" + node;

        if(zkClient.exists(zNode)){
            LOGGER.info("zNode : {} already exits ", node);
            return;
        }
        zkClient.create(zNode, data, CreateMode.EPHEMERAL);
        LOGGER.info("Added node {} to cluster", node);
    }

    @Override
    public void registerChildrenChangeWatcher(IZkChildListener childListener, String path) {
        zkClient.subscribeChildChanges(path, childListener);
    }

    @Override
    public void registerSessionStateWatcher(IZkStateListener stateListener) {
        zkClient.subscribeStateChanges(stateListener);
    }
}