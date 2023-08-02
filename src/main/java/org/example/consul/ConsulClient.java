package org.example.consul;

import feign.RequestLine;
import feign.Param;

import java.util.List;

public interface ConsulClient {

    @RequestLine("GET /v1/kv/{path}?recurse=true")
    List<KVValue> findRecursive(@Param("path") String path);

    @RequestLine("GET /v1/kv/{path}?keys=true")
    List<Key> findKeys(@Param("path") String path);

}
