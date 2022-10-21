package com.d7001d.pubsubserver.config;

import java.util.List;

public class PubSubUtil {

    public static String generateRedisKeyWithoutId(String type) {
        return String.format(":%s:", type);
    }

    public static double cosineSimilarity(List<Integer> vectorA, List<Integer> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
