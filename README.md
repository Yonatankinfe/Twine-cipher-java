# Twine-cipher-java
A Java implementation of the TWINE lightweight block cipher, designed for resource-constrained environments such as IoT devices. This repository provides encryption and decryption functions, with support for 64-bit block size and key lengths of 80 or 128 bits.
# TWINE Cipher Implementation in Java

This repository provides an implementation of the TWINE lightweight block cipher in Java. It supports encryption and decryption of 64-bit blocks using a user-defined master key. The TWINE cipher is ideal for resource-constrained environments such as IoT devices due to its simplicity and efficiency.

## Features

+ **Block Size**: 64 bits
+ **Key Sizes Supported**: 80 bits or 128 bits
+ **Number of Rounds**: 36
+ **S-Box Substitution and Permutation** for security
+ **Command-Line Interface** for encrypting and decrypting text

---

## Code Overview

### Package

The code resides in the `twinecipher` package and includes all essential functions for encryption, decryption, and key scheduling.

### Key Components

#### 1. Constants
+ **`NUM_ROUNDS`**: The number of rounds in the encryption/decryption process (36 rounds).
+ **`S_BOX`**: A substitution table for non-linear transformations.
+ **`PERMUTATION`**: A permutation table to shuffle bits for diffusion.

#### 2. Core Methods

- **Encryption and Decryption**: Implements Feistel network logic for TWINE cipher.
  + **`encrypt(int[] plaintext)`**: Encrypts a 64-bit block (split into two 32-bit halves).
  + **`decrypt(int[] ciphertext)`**: Decrypts a 64-bit block back to plaintext.

- **Round Function**:
  + **`roundFunction(int input, int roundKey)`**: Combines substitution, permutation, and key mixing for each round.

- **Substitution and Permutation**:
  + **`substitute(int input)`**: Applies the S-Box substitution to 4-bit nibbles.
  + **`permute(int input)`**: Rearranges bits using the permutation table.

- **Key Schedule**:
  + **`keySchedule(int[] masterKey)`**: Derives 36 round keys from the user-provided master key.

#### 3. Text Processing

+ **`encryptText`**: Encrypts user input text by converting characters into blocks and applying the TWINE encryption algorithm.
+ **`decryptText`**: Decrypts hex-formatted ciphertext back into readable text.
+ **`toHexString`**: Converts plaintext into its hexadecimal representation for better compatibility.

#### 4. Interactive Menu

A user-friendly interface is provided via the `main` method to:
+ Encrypt plaintext.
+ Decrypt ciphertext.
+ Exit the program.

---

## How to Use

### Requirements

+ Java Development Kit (JDK) 8 or higher.
