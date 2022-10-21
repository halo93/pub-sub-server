package com.d7001d.pubsubserver.service;

import com.d7001d.pubsubserver.config.PubSubUtil;
import com.d7001d.pubsubserver.config.RedisKeyType;
import com.d7001d.pubsubserver.exception.InternalServerErrorException;
import com.d7001d.pubsubserver.model.Data;
import com.d7001d.pubsubserver.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DataProcessingService {

    private final DataRepository dataRepository;
    private static final String PIVOT_RECORD_KEY = PubSubUtil.generateRedisKeyWithoutId(RedisKeyType.PIVOT.toString());
    private static final String USER_RECORD_KEY = PubSubUtil.generateRedisKeyWithoutId(RedisKeyType.USER.toString());
    private static final double COSINE_SIMILARITY_THRESHOLD = 0.3;

    public Data saveChunk(List<Integer> chunk) {
        if (!dataRepository.existsByKey(PIVOT_RECORD_KEY)) {
            return saveBrandNewChunk(chunk);
        } else {
            Map.Entry<String, Double> maxCosineSimilarityEntry = findMaxCosineSimilarityWithPivotData(chunk);
            if (maxCosineSimilarityEntry.getValue() < COSINE_SIMILARITY_THRESHOLD) {
                return saveBrandNewChunk(chunk);
            } else {
                Data userData = Data.builder().key(maxCosineSimilarityEntry.getKey())
                        .chunk(chunk).createdAt(Instant.now()).build();
                Data savedUserData = dataRepository.save(userData);
                if (Objects.isNull(savedUserData.getId())) {
                    throw new InternalServerErrorException();
                }
                return savedUserData;
            }

        }
    }

    private Map.Entry<String, Double> findMaxCosineSimilarityWithPivotData(List<Integer> chunk) {
        List<Data> allPivotData = findAllPivotDataByKey();
        Map<String, Double> cosineSimilarityMap = allPivotData.stream().collect(Collectors.toMap(
                e -> USER_RECORD_KEY + e.getChunk().stream().map(String::valueOf).collect(Collectors.joining()),
                e1 -> PubSubUtil.cosineSimilarity(chunk, e1.getChunk())
        ));
        return cosineSimilarityMap.entrySet().stream()
                .max(Map.Entry.comparingByValue()).get();
    }

    private Data saveBrandNewChunk(List<Integer> chunk) {
        Data pivotData = Data.builder().key(PIVOT_RECORD_KEY).chunk(chunk).createdAt(Instant.now()).build();
        Data savedPivotdata = dataRepository.save(pivotData);
        String uniqueUserRecordKey = USER_RECORD_KEY + chunk.stream().map(String::valueOf).collect(Collectors.joining());
        Data userData = Data.builder().key(uniqueUserRecordKey).chunk(chunk).createdAt(Instant.now()).build();
        Data savedUserData = dataRepository.save(userData);
        if (Objects.isNull(savedPivotdata.getId()) || Objects.isNull(savedUserData.getId())) {
            throw new InternalServerErrorException();
        }
        return savedUserData;
    }

    public List<Data> findAllPivotDataByKey() {
        return dataRepository.findAllByKey(PIVOT_RECORD_KEY);
    }

    public void deleteAllPivotDataByKey() {
        findAllPivotDataByKey().forEach(e -> dataRepository.deleteById(e.getId()));
    }

    public void deleteAllData() {
        dataRepository.deleteAll();
    }

    public List<List<Integer>> findTrunksForMessageByKey(List<Integer> ids) {
        Map.Entry<String, Double> maxCosineSimilarityWithPivotData = findMaxCosineSimilarityWithPivotData(ids);
        if (maxCosineSimilarityWithPivotData.getValue() < COSINE_SIMILARITY_THRESHOLD) {
            return Collections.emptyList();
        }
        return dataRepository.findAllByKey(maxCosineSimilarityWithPivotData.getKey())
                .stream().map(Data::getChunk).collect(Collectors.toList());

    }
}
