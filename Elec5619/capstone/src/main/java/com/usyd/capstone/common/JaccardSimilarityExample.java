package com.usyd.capstone.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JaccardSimilarityExample {

    public static double calculateJaccardSimilarity(List<String> listA, List<String> listB) {
        Set<String> setA = new HashSet<>(listA);
        Set<String> setB = new HashSet<>(listB);

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        if (union.isEmpty()) {
            return 0.0; // 避免除以零
        }

        return (double) intersection.size() / union.size();
    }
}
