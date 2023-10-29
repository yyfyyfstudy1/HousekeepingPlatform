package com.usyd.capstone.common.util;

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

    public static String removeSpecialCharactersAndSpaces(String input) {
        // 去除换行符
        String noNewLines = input.replaceAll("\\r?\\n", "");
        System.out.println(noNewLines);

        // 去除特殊符号（只保留字母，数字和空格）
        String noSpecialChars = noNewLines.replaceAll("[^a-zA-Z0-9\\s]", "");
        System.out.println(noSpecialChars);

        return noNewLines;
    }
}
