package org.example;

import java.util.*;

public class Problem_1 {

    public static List<String> Toss(double p, int n) {
        List<String> results = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            results.add(random.nextDouble() < p ? "H" : "T");
        }
        return results;
    }

    public static int Runs(List<String> results) {
        if (results.isEmpty()) return 0;
        int runCount = 1;
        for (int i = 1; i < results.size(); i++) {
            if (!results.get(i).equals(results.get(i - 1))) {
                runCount++;
            }
        }
        return runCount;
    }

    public static void calculateProbability(List<String> results) {
        long countH = results.stream().filter(r -> r.equals("H")).count();
        double pH = (double) countH / results.size();
        double pT = 1.0 - pH;
        System.out.printf("  - p(H) = %.4f, p(T) = %.4f%n", pH, pT);
    }

    public static int tossUntilHGreaterThanT(double p) {
        int h = 0, t = 0, total = 0;
        Random random = new Random();
        final int MAX_LIMIT = 10000;

        while (h <= t && total < MAX_LIMIT) {
            total++;
            if (random.nextDouble() < p) {
                h++;
            } else {
                t++;
            }
        }

        return total;
    }

    public static String randomStringS(int a, int b) {
        StringBuilder sb = new StringBuilder();
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < a; i++) chars.add('A');
        for (int i = 0; i < b; i++) chars.add('B');
        Collections.shuffle(chars);
        for (char c : chars) sb.append(c);
        return sb.toString();
    }

    public static int runA(String s) {
        int count = 0;
        boolean inRun = false;
        for (char c : s.toCharArray()) {
            if (c == 'A') {
                if (!inRun) {
                    count++;
                    inRun = true;
                }
            } else {
                inRun = false;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double p;
        int n;
        final int N = 1000;

        while (true) {
            try {
                System.out.print("Nhập xác suất P(H): ");
                p = scanner.nextDouble();
                if (p < 0 || p > 1) throw new InputMismatchException();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập một số thực hợp lệ trong khoảng [0, 1]");
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("Nhập số lần tung n > 0: ");
                n = scanner.nextInt();
                if (n <= 0) throw new InputMismatchException();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Vui lòng nhập một số nguyên dương.");
                scanner.nextLine();
            }
        }

        System.out.println("\n===================== PHẦN 1: Toss(p, n) =====================");
        List<String> results = Toss(p, n);
        System.out.println("  - Kết quả tung đồng xu: " + results);

        System.out.println("\n================ PHẦN 2: Kiểm tra xác suất ===================");
        calculateProbability(results);

        System.out.println("\n========== PHẦN 3: Số runs & Tính Mean và Variance ==========");
        int runCount = Runs(results);
        System.out.println("  - Số runs (thay đổi H <-> T): " + runCount);

        double total = 0, totalSquared = 0;
        for (int i = 0; i < N; i++) {
            int r = Runs(Toss(p, n));
            total += r;
            totalSquared += r * r;
        }
        double mean = total / N;
        double variance = totalSquared / N - mean * mean;

        System.out.printf("  - Kỳ vọng (Mean) của số runs ≈ %.4f%n", mean);
        System.out.printf("  - Phương sai (Variance) ≈ %.4f%n", variance);
        System.out.printf("  - Lý thuyết: Mean ≈ %.4f, Variance ≈ %.4f%n",
                1 + 2 * (n - 1) * p * (1 - p),
                2 * p * (1 - p) * (2 * n - 3 - 2 * p * (1 - p) * (3 * n - 5)));

        System.out.println("\n======= PHẦN 4: Tung đến khi số H > T (E(X)) ================");
        int totalX = 0;
        for (int i = 0; i < N; i++) {
            totalX += tossUntilHGreaterThanT(p);
            if ((i + 1) % 500 == 0) {
                System.out.println(" test " + (i + 1) + " / " + N + " lần...");
            }
        }
        double expectedX = (double) totalX / N;
        System.out.printf("  - Kỳ vọng số lần tung để H > T: E(X) ≈ %.4f%n", expectedX);

        System.out.println("\n======= PHẦN 5: Sinh chuỗi S(a,b) và tính RunA ==============");
        System.out.print("Nhập số ký tự A (a): ");
        int a = scanner.nextInt();
        System.out.print("Nhập số ký tự B (b): ");
        int b = scanner.nextInt();

        int totalRunA = 0;
        for (int i = 0; i < N; i++) {
            String s = randomStringS(a, b);
            totalRunA += runA(s);
        }
        double approxRunA = (double) totalRunA / N;
        double exactRunA = (double) a * (b + 1) / (a + b);

        System.out.printf("  - Giá trị trung bình RunA (mô phỏng): %.4f%n", approxRunA);
        System.out.printf("  - Giá trị trung bình RunA (lý thuyết): %.4f%n", exactRunA);

        scanner.close();
    }
}
