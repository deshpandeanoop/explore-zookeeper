package com.explore.zookeeper.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClusterInfo {
    private static final ClusterInfo clusterInfo = new ClusterInfo();
    private final List<String> nodes = new ArrayList<>();
    private String master;

    private ClusterInfo(){

    }
    public static ClusterInfo getInstance(){
        return clusterInfo;
    }


    public List<String> getNodes() {
        return nodes;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public static String getServerHostAndPort(){
        try{
            return InetAddress.getLocalHost().getHostAddress()+":"+System.getProperty("server.port");
        }catch (UnknownHostException unKnownHostException){
            // This case will never happen, so eating away the exception
        }
        return "";
    }

    public boolean isMaster(){
        return null !=master && master.equals(getServerHostAndPort());
    }

    public List<String> getChildNodes(){
        return nodes.stream()
                .filter(node -> !master.equals(node))
                .collect(Collectors.toList());
    }
}