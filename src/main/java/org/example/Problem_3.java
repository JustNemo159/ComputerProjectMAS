package org.example;


import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Problem_3 {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập số lần mô phỏng (trials): ");
        int trials = scanner.nextInt();

        System.out.print("Nhập số lần phản xạ tối đa để xuất CSV (ví dụ 10): ");
        int bounces = scanner.nextInt();

        System.out.print("Nhập vị trí a (0 đến 0.5): ");
        double a = scanner.nextDouble();

        System.out.print("Nhập góc bắn x (đơn vị radian, từ 0 đến π): ");
        double angle = scanner.nextDouble();

        // 1. Mô phỏng 1 đường đi và xuất ra file CSV
        List<double[]> path = simulatePath(a, 0, angle, bounces);
        exportToCSV(path, "billiard_path.csv");

        // 2. Tính kỳ vọng E(R) với mô phỏng
        double expectedR = estimateExpectedR(trials);
        System.out.printf("Kỳ vọng E(R) (vị trí bi quay lại cạnh OA): %.6f\n", expectedR);
    }

    // Phần 1: Mô phỏng và phản xạ
    public static List<double[]> simulatePath(double x0, double y0, double angle, int maxBounces) {
        List<double[]> path = new ArrayList<>();
        double x = x0, y = y0;
        double dx = Math.cos(angle), dy = Math.sin(angle);
        path.add(new double[]{x, y});

        for (int bounce = 0; bounce < maxBounces; bounce++) {
            double tx = dx > 0 ? (1 - x) / dx : (dx < 0 ? -x / dx : Double.POSITIVE_INFINITY);
            double ty = dy > 0 ? (1 - y) / dy : (dy < 0 ? -y / dy : Double.POSITIVE_INFINITY);
            double t = Math.min(tx, ty);

            x += dx * t;
            y += dy * t;
            path.add(new double[]{x, y});

            if (Math.abs(x - 1) < 1e-9 || Math.abs(x) < 1e-9) dx = -dx;
            if (Math.abs(y - 1) < 1e-9 || Math.abs(y) < 1e-9) dy = -dy;
        }

        return path;
    }

    // Ghi file CSV
    public static void exportToCSV(List<double[]> path, String filename) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("x,y\n");
            for (double[] p : path) {
                writer.write(p[0] + "," + p[1] + "\n");
            }
        }
        System.out.println("Đã xuất đường đi vào " + filename);
    }

    // Phần 2: Tính E(R)
    public static double estimateExpectedR(int trials) {
        Random random = new Random();
        double sum = 0;

        for (int i = 0; i < trials; i++) {
            double a = random.nextDouble() * 0.5;
            double angle = random.nextDouble() * Math.PI;

            double r = simulateReturnToOA(a, 0, angle);
            sum += r;
        }

        return sum / trials;
    }

    // Tính R – vị trí lần đầu quay lại cạnh OA
    public static double simulateReturnToOA(double x0, double y0, double angle) {
        double x = x0, y = y0;
        double dx = Math.cos(angle), dy = Math.sin(angle);

        while (true) {
            double tx = dx > 0 ? (1 - x) / dx : (dx < 0 ? -x / dx : Double.POSITIVE_INFINITY);
            double ty = dy > 0 ? (1 - y) / dy : (dy < 0 ? -y / dy : Double.POSITIVE_INFINITY);
            double t = Math.min(tx, ty);

            x += dx * t;
            y += dy * t;

            if (Math.abs(x - 1) < 1e-9 || Math.abs(x) < 1e-9) dx = -dx;
            if (Math.abs(y - 1) < 1e-9 || Math.abs(y) < 1e-9) dy = -dy;

            // Khi quay lại cạnh OA
            if (Math.abs(y) < 1e-6) {
                return x;
            }
        }
    }
}