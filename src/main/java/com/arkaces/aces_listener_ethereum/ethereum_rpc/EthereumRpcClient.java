package com.arkaces.aces_listener_ethereum.ethereum_rpc;

import com.arkaces.aces_listener_ethereum.RpcRequest;
import com.arkaces.aces_server.common.json.NiceObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class EthereumRpcClient {

    private final RestTemplate ethereumRpcRestTemplate;

    private final RpcRequestFactory rpcRequestFactory = new RpcRequestFactory();

    private final NiceObjectMapper objectMapper = new NiceObjectMapper(new ObjectMapper());

    public Block getLatestBlock() {
        HttpEntity<String> requestEntity = getRequestEntity("eth_getBlockByNumber", Arrays.asList("latest", true));
        return ethereumRpcRestTemplate
            .exchange(
                "/",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<RpcResponse<Block>>() {}
            )
            .getBody()
            .getResult();
    }

    public Block getBlockByHash(String hash) {
        HttpEntity<String> requestEntity = getRequestEntity("eth_getBlockByHash", Arrays.asList(hash, true));
        return ethereumRpcRestTemplate
            .exchange(
                    "/",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<RpcResponse<Block>>() {}
            )
            .getBody()
            .getResult();
    }

    private HttpEntity<String> getRequestEntity(String method, List<Object> params) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");

        RpcRequest rpcRequest = rpcRequestFactory.create(method, params);
        String body = objectMapper.writeValueAsString(rpcRequest);

        return new HttpEntity<>(body, headers);
    }

}
