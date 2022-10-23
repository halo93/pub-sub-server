package com.d7001d.pubsubserver.utils;

public class Utility {

    public static int calculateDotProduct(int[] vectorA, int[] vectorB) {
        int dotProduct = 0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
        }
        return dotProduct;
    }
}
