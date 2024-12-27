package twinecipher;

import java.util.Scanner;

public class TwineCipher {

    private static final int NUM_ROUNDS = 36;
    private static final int[] S_BOX = {
        0x0, 0x4, 0x8, 0xC, 0x1, 0x5, 0x9, 0xD, 0x2, 0x6, 0xA, 0xE, 0x3, 0x7, 0xB, 0xF
    };
    private static final int[] PERMUTATION = {
        0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15
    };

    private final int[] roundKeys;

    public TwineCipher(int[] masterKey) {
        this.roundKeys = keySchedule(masterKey);
    }

    public int[] encrypt(int[] plaintext) {
        int left = plaintext[0];
        int right = plaintext[1];
        for (int i = 0; i < NUM_ROUNDS; i++) {
            int temp = right;
            right = left ^ roundFunction(right, roundKeys[i]);
            left = temp;
        }
        return new int[]{right, left};
    }

    public int[] decrypt(int[] ciphertext) {
        int left = ciphertext[1];
        int right = ciphertext[0];
        for (int i = NUM_ROUNDS - 1; i >= 0; i--) {
            int temp = left;
            left = right ^ roundFunction(left, roundKeys[i]);
            right = temp;
        }
        return new int[]{left, right};
    }

    private int roundFunction(int input, int roundKey) {
        int substituted = substitute(input);
        int permuted = permute(substituted);
        return permuted ^ roundKey;
    }

    private int substitute(int input) {
        int output = 0;
        for (int i = 0; i < 16; i += 4) {
            int nibble = (input >> i) & 0xF;
            output |= S_BOX[nibble] << i;
        }
        return output;
    }

    private int permute(int input) {
        int output = 0;
        for (int i = 0; i < 16; i++) {
            if ((input & (1 << i)) != 0) {
                output |= (1 << PERMUTATION[i]);
            }
        }
        return output;
    }

    private int[] keySchedule(int[] masterKey) {
        int[] roundKeys = new int[NUM_ROUNDS];
        for (int i = 0; i < NUM_ROUNDS; i++) {
            roundKeys[i] = masterKey[i % masterKey.length];
        }
        return roundKeys;
    }

    private static String toHexString(String input) {
        StringBuilder hex = new StringBuilder();
        for (char c : input.toCharArray()) {
            hex.append(String.format("%02X", (int) c));
        }
        return hex.toString();
    }

    private static String encryptText(TwineCipher twine, String plaintext) {
        // Pad if the plaintext length is odd
        if (plaintext.length() % 2 != 0) {
            plaintext += "X"; // Add padding character
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            int left = plaintext.charAt(i);
            int right = plaintext.charAt(i + 1);
            int[] encrypted = twine.encrypt(new int[]{left, right});
            ciphertext.append(String.format("%04X", encrypted[0])) // Convert to hex
                      .append(String.format("%04X", encrypted[1]));
        }
        return ciphertext.toString();
    }

    private static String decryptText(TwineCipher twine, String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 8) { // Process 4 hex digits per 16-bit integer
            int left = Integer.parseInt(ciphertext.substring(i, i + 4), 16);
            int right = Integer.parseInt(ciphertext.substring(i + 4, i + 8), 16);
            int[] decrypted = twine.decrypt(new int[]{left, right});
            plaintext.append((char) decrypted[0]).append((char) decrypted[1]);
        }
        // Remove padding character if present
        if (plaintext.charAt(plaintext.length() - 1) == 'X') {
            plaintext.deleteCharAt(plaintext.length() - 1);
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] masterKey = {0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF, 0x10};
        TwineCipher twine = new TwineCipher(masterKey);

        while (true) {
            System.out.println("\nChoose an operation:");
            System.out.println("1. Encrypt text");
            System.out.println("2. Decrypt text");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter text to encrypt: ");
                String plaintext = scanner.nextLine();
                String hexPlaintext = toHexString(plaintext);
                System.out.println("Hexadecimal representation of input: " + hexPlaintext);

                String ciphertext = encryptText(twine, plaintext);
                System.out.println("Ciphertext (Hex): " + ciphertext);
            } else if (choice == 2) {
                System.out.print("Enter ciphertext (Hex): ");
                String ciphertext = scanner.nextLine();
                String plaintext = decryptText(twine, ciphertext);
                System.out.println("Decrypted text: " + plaintext);
            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
      }
