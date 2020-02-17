package com.explore.zookeeper.enums;

public enum ZNodes {
    CLUSTER_ROOT("/CLUSTER_ROOT"), LEADER_ROOT("/LEADER_ROOT"), LEADER_NODE("/LEADER_NODE");

    private String val;

    ZNodes(String val) {
        this.val = val;
    }

    public  String val(){
        return val;
    }
}
