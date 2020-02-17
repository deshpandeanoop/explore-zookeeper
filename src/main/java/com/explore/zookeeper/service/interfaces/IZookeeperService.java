package com.explore.zookeeper.service.interfaces;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;

public interface IZookeeperService {

    void createClusterRoot();

    void createLeaderRoot();

    void electMaster(String hostAndPort);

    String getMaster();

    void addNodeToCluster(String node, String data);

    void registerChildrenChangeWatcher(IZkChildListener childListener, String path);

    void registerSessionStateWatcher(IZkStateListener stateListener);
}