package com.d7001d.pubsubserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
@RedisHash
public class Data {

    private String id;

    @Indexed
    private String key;
    private List<Integer> chunk;
}
