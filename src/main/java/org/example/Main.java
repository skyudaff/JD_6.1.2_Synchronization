package org.example;

import java.util.*;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int freq = getCount(route);
                synchronized (sizeToFreq) {
                    sizeToFreq.put(freq, sizeToFreq.getOrDefault(freq, 0) + 1);
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        printResult();
    }

    public static int getCount(String route) {
        int count = 0;
        for (char ch : route.toCharArray()) {
            if (ch == 'R') count++;
        }
        return count;
    }

    public static void printResult() {
        int maxFreq = 0;
        for (int freq : sizeToFreq.keySet()) {
            maxFreq = Math.max(maxFreq, freq);
        }

        System.out.println("Самое частое количество повторений " + maxFreq + " (встретилось " + sizeToFreq.get(maxFreq) + " раз)");
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getKey() != maxFreq) {
                System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}