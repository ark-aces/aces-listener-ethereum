package com.arkaces.aces_listener_ethereum.ethereum_rpc;

import lombok.Data;

@Data
public class RpcResponse<T> {
    private T result;
    private Object error;
    private String id;
}
