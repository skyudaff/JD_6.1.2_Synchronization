package org.example;

import java.util.*;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    private static final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int freq = getCount(route);
                synchronized (monitor) {
                    sizeToFreq.put(freq, sizeToFreq.getOrDefault(freq, 0) + 1);
                    monitor.notify();
                }
            });
            threads[i].start();
        }

        Thread printResultThread = new Thread(() -> {
            int maxFreq = 0;
            while (!Thread.interrupted()) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                for (int freq : sizeToFreq.keySet()) {
                    if (freq > maxFreq) maxFreq = freq;
                }
                System.out.println("Текущий лидер частот: " + maxFreq + " (встретилось " + sizeToFreq.get(maxFreq) + " раз)");
            }
        });
        printResultThread.start();

        for (Thread thread : threads) {
            thread.join();
        }
        printResultThread.interrupt();
    }

    public static int getCount(String route) {
        int count = 0;
        for (char ch : route.toCharArray()) {
            if (ch == 'R') count++;
        }
        return count;
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