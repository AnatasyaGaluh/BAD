/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Anatasya
 */
import java.util.InputMismatchException;
import java.util.Scanner;

public class Rerata {
    public static void main(String[] args) {
        hitungRataRata();
    }

    public static void hitungRataRata() {
        Scanner scanner = new Scanner(System.in);
        double total = 0;
        int count = 0;

        while (true) {
            try {
                System.out.print("Masukkan bilangan "
                        + "(ketik '-' untuk selesai): ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("-")) {
                    break;
                }

                double bilangan = Double.parseDouble(input);
                total += bilangan;
                count++;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid. "
                        + "Mohon masukkan bilangan numerik.");
            }
        }

        if (count == 0) {
            System.out.println("Tidak ada bilangan yang dimasukkan.");
        } else {
            double rataRata = total / count;
            System.out.println("Rata-rata dari bilangan yang dimasukkan "
                    + "adalah: " + rataRata);
        }
    }
}

