package org.example;

import java.util.*;

class Problem_2 {

    // Part a: Exact computation of derangement probability
    public static double exactDerangementProbability(int n) {
        int total = factorial(n);
        int derangements = countDerangements(n);
        return (double) derangements / total;
    }

    // Part a: Approximate computation of derangement probability
    public static double approximateDerangementProbability(int n, int trials) {
        int derangements = 0;
        Random rand = new Random();

        for (int i = 0; i < trials; i++) {
            List<Integer> perm = new ArrayList<>();
            for (int j = 1; j <= n; j++) perm.add(j);
            Collections.shuffle(perm, rand);

            boolean isDerangement = true;
            for (int j = 0; j < n; j++) {
                if (perm.get(j) == j + 1) {
                    isDerangement = false;
                    break;
                }
            }
            if (isDerangement) derangements++;
        }
        return (double) derangements / trials;
    }

    // Factorial helper
    private static int factorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    // Derangement count using recursive formula
    private static int countDerangements(int n) {
        if (n == 0) return 1;
        if (n == 1) return 0;
        return (n - 1) * (countDerangements(n - 1) + countDerangements(n - 2));
    }

    // Part b: Hill distance between two numbers
    public static int hillDistance(int a, int b) {
        if (a < b) return 2 * (b - a);
        else if (a > b) return a - b;
        else return 0;
    }

    // Part b: Hill distance for a list
    public static int totalHillDistance(List<Integer> A) {
        int sum = 0;
        for (int i = 0; i < A.size() - 1; i++) {
            sum += hillDistance(A.get(i), A.get(i + 1));
        }
        return sum;
    }

    // Part b: Expected hill distance over random permutations
    public static double expectedHillDistance(int n, int trials) {
        int total = 0;
        Random rand = new Random();

        for (int i = 0; i < trials; i++) {
            List<Integer> perm = new ArrayList<>();
            for (int j = 1; j <= n; j++) perm.add(j);
            Collections.shuffle(perm, rand);
            total += totalHillDistance(perm);
        }
        return (double) total / trials;
    }

    // Part c: Length of LIS
    public static int lengthOfLIS(List<Integer> A) {
        List<Integer> lis = new ArrayList<>();
        for (int num : A) {
            int i = Collections.binarySearch(lis, num);
            if (i < 0) i = -(i + 1);
            if (i == lis.size()) lis.add(num);
            else lis.set(i, num);
        }
        return lis.size();
    }

    // Part c: Expected length of LIS over random permutations
    public static double expectedLISLength(int n, int trials) {
        int total = 0;
        Random rand = new Random();

        for (int i = 0; i < trials; i++) {
            List<Integer> perm = new ArrayList<>();
            for (int j = 1; j <= n; j++) perm.add(j);
            Collections.shuffle(perm, rand);
            total += lengthOfLIS(perm);
        }
        return (double) total / trials;
    }

    // Main method to test all parts
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số nguyên dương n (kích thước hoán vị): ");
        int n = scanner.nextInt();

        System.out.print("Nhập số lần thử nghiệm (dùng cho phương pháp gần đúng): ");
        int trials = scanner.nextInt();

        System.out.printf("Xác suất chính xác hoán vị lệch chỗ (derangement) khi n = %d là: %.6f\n", n, exactDerangementProbability(n));
        System.out.printf("Xác suất gần đúng hoán vị lệch chỗ (derangement) khi n = %d là: %.6f (qua %d lần thử)\n", n, approximateDerangementProbability(n, trials), trials);

        System.out.printf("Kỳ vọng tổng khoảng cách đồi núi (hill-distance) với hoán vị ngẫu nhiên của %d phần tử là: %.6f\n", n, expectedHillDistance(n, trials));
        System.out.printf("Kỳ vọng độ dài dãy con tăng dài nhất (LIS) với hoán vị ngẫu nhiên của %d phần tử là: %.6f\n", n, expectedLISLength(n, trials));
    }
}
