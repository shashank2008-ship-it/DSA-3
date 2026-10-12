
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class KMPDataset {

    // Step 1: Create the LPS array
    static int[] createLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];

        int len = 0;
        int i = 1;

        lps[0] = 0;

        while (i < m) {

            // Characters match
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } // Characters do not match
            else {
                if (len != 0) {
                    // Use the previous LPS value
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    // Step 2: Search for the pattern in the dataset
    static void search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        if (m == 0) {
            System.out.println("Pattern cannot be empty.");
            return;
        }

        // Generate LPS array
        int[] lps = createLPS(pattern);

        System.out.println("\n--- LPS ARRAY ---");
        System.out.print("Pattern: ");

        for (int i = 0; i < m; i++) {
            System.out.print(pattern.charAt(i) + " ");
        }

        System.out.print("\nLPS:     ");

        for (int value : lps) {
            System.out.print(value + " ");
        }

        System.out.println("\n");

        // KMP searching process
        int i = 0;       // Index for text
        int j = 0;       // Index for pattern
        int count = 0;   // Number of matches

        long startTime = System.nanoTime();

        while (i < n) {

            // Characters match
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            // Entire pattern matched
            if (j == m) {
                int position = i - j;

                System.out.println(
                        "Pattern found at index: " + position
                );

                count++;

                // Continue to find overlapping matches
                j = lps[j - 1];
            } // Mismatch occurred
            else if (i < n
                    && text.charAt(i) != pattern.charAt(j)) {

                if (j != 0) {
                    // Avoid restarting from the beginning
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("\n--- SEARCH RESULTS ---");
        System.out.println("Dataset length: " + n + " characters");
        System.out.println("Pattern length: " + m + " characters");
        System.out.println("Total occurrences: " + count);
        System.out.printf(
                "Search time: %.3f milliseconds%n",
                (endTime - startTime) / 1_000_000.0
        );
    }

    // Step 3: Main method
    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("==================================");
            System.out.println(" KMP DATASET STRING SEARCH");
            System.out.println("==================================");

            System.out.print(
                    "Enter dataset file path (e.g. dataset.txt): "
            );

            String filePath = sc.nextLine().trim();

            // Read the complete dataset as text
            String text = Files.readString(Path.of(filePath));

            if (text.isEmpty()) {
                System.out.println("The dataset file is empty.");
                return;
            }

            System.out.println("\nDataset loaded successfully!");
            System.out.println(
                    "Total characters: " + text.length()
            );

            System.out.print(
                    "\nEnter the pattern you want to search: "
            );

            String pattern = sc.nextLine();

            if (pattern.isEmpty()) {
                System.out.println("Pattern cannot be empty.");
                return;
            }

            System.out.println("\nSearching dataset...");
            search(text, pattern);

        } catch (IOException e) {
            System.out.println(
                    "Error: Could not read the dataset file."
            );
            System.out.println(
                    "Check that the file path is correct."
            );
        } catch (java.nio.file.InvalidPathException e) {
            System.out.println("Error: Invalid file path.");
        }
    }
}
