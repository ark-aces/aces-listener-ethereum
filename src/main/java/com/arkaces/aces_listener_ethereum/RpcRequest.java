package com.arkaces.aces_listener_ethereum;

import lombok.Data;

import java.util.List;

@Data
public class RpcRequest {
    private String jsonrpc = "1.0";
    private String id = "curltext";
    private String method;
    private List<Object> params;
}
