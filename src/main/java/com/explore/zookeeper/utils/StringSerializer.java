package com.explore.zookeeper.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class StringSerializer implements ZkSerializer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object object) throws ZkMarshallingError {
        try{
            return objectMapper.writeValueAsBytes(object);
        }catch (Exception exception){
            throw new RuntimeException("Cannot serialize zookeeper's request data", exception);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try{
            return objectMapper.readValue(bytes, Object.class);
        }catch (Exception exception){
            throw new RuntimeException("Cannot deserialize zookeeper's response", exception);
        }
    }
}