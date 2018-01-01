package com.arkaces.aces_listener_ethereum.ethereum_rpc;

import com.arkaces.aces_listener_ethereum.RpcRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RpcRequestFactory {

    public RpcRequest create(String method, List<Object> params) {
        RpcRequest request = new RpcRequest();
        request.setMethod(method);
        request.setParams(params);
        return request;
    }
}
