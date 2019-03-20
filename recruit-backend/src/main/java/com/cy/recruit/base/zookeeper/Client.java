package com.cy.recruit.base.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class Client {

    public ZooKeeper getClient(){
        try{
            return new ZooKeeper("127.0.0.1:2181", 5000, null);
        }catch (Exception e){
            System.out.println("连接节点失败");
            return null;
        }
    }

    public void createNode(String path, String value){
        try{
            ZooKeeper zooKeeper = getClient();
            if(zooKeeper!=null){
                Stat hasRoot = zooKeeper.exists("root", false);
                if(hasRoot == null){
                    zooKeeper.create("root", "root".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
                }

                zooKeeper.create(path, value.getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
            }
        }catch (Exception e){
            System.out.println("创建节点失败");
        }
    }


}
