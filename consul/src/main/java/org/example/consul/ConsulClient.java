package org.example.consul;

import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface ConsulClient {
    @RequestLine("GET /v1/kv/{path}?recurse")
    List<KVValue> findRecursive(@Param("path") String path);
}
