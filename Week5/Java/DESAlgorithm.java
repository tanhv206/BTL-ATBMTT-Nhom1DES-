import java.util.ArrayList;
import java.util.List;

/**
 * DESAlgorithm.java
 * Triển khai thuật toán DES (Data Encryption Standard) hoàn toàn từ đầu.
 * Không sử dụng bất kỳ thư viện mật mã nào.
 *
 * Tác giả: Sinh viên trường HaUI
 * Mục đích: Giáo dục - minh họa từng bước của DES
 */
public class DESAlgorithm {

    // ============================================================
    // BẢNG HOÁN VỊ BAN ĐẦU (Initial Permutation - IP)
    // ============================================================
    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // ============================================================
    // BẢNG HOÁN VỊ CUỐI (Final Permutation - FP = IP^-1)
    // ============================================================
    private static final int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41,  9, 49, 17, 57, 25
    };

    // ============================================================
    // BẢNG MỞ RỘNG (Expansion Table - E)
    // Mở rộng 32 bit → 48 bit
    // ============================================================
    private static final int[] E = {
            32,  1,  2,  3,  4,  5,
            4,  5,  6,  7,  8,  9,
            8,  9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32,  1
    };

    // ============================================================
    // BẢNG HOÁN VỊ P (P-Box)
    // Hoán vị 32 bit sau khi qua S-Box
    // ============================================================
    private static final int[] P = {
            16,  7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2,  8, 24, 14,
            32, 27,  3,  9,
            19, 13, 30,  6,
            22, 11,  4, 25
    };

    // ============================================================
    // BẢNG PC-1 (Permuted Choice 1)
    // Chọn 56 bit từ 64 bit khóa (bỏ các bit kiểm tra chẵn lẻ)
    // ============================================================
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17,  9,
            1, 58, 50, 42, 34, 26, 18,
            10,  2, 59, 51, 43, 35, 27,
            19, 11,  3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14,  6, 61, 53, 45, 37, 29,
            21, 13,  5, 28, 20, 12,  4
    };

    // ============================================================
    // BẢNG PC-2 (Permuted Choice 2)
    // Chọn 48 bit từ 56 bit để tạo khóa con
    // ============================================================
    private static final int[] PC2 = {
            14, 17, 11, 24,  1,  5,
            3, 28, 15,  6, 21, 10,
            23, 19, 12,  4, 26,  8,
            16,  7, 27, 20, 13,  2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    // ============================================================
    // LỊCH DỊCH TRÁI (Left Shift Schedule)
    // Số bit dịch trái tại mỗi vòng khi sinh khóa con
    // ============================================================
    private static final int[] SHIFT_SCHEDULE = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    // ============================================================
    // 8 S-BOX (Substitution Boxes)
    // Mỗi S-Box: 4 hàng × 16 cột, ánh xạ 6 bit → 4 bit
    // ============================================================
    private static final int[][][] S_BOX = {
            // S1
            {
                    {14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
                    { 0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8},
                    { 4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
                    {15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13}
            },
            // S2
            {
                    {15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10},
                    { 3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5},
                    { 0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
                    {13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9}
            },
            // S3
            {
                    {10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8},
                    {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1},
                    {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7},
                    { 1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12}
            },
            // S4
            {
                    { 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
                    {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9},
                    {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4},
                    { 3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14}
            },
            // S5
            {
                    { 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
                    {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6},
                    { 4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
                    {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3}
            },
            // S6
            {
                    {12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11},
                    {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8},
                    { 9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6},
                    { 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13}
            },
            // S7
            {
                    { 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
                    {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6},
                    { 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
                    { 6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12}
            },
            // S8
            {
                    {13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7},
                    { 1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
                    { 7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
                    { 2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11}
            }
    };

    // ============================================================
    // LỚP CHỨA KẾT QUẢ CHI TIẾT THUẬT TOÁN
    // Dùng để hiển thị từng bước trong hộp thoại chi tiết
    // ============================================================
    public static class AlgorithmDetails {
        public String[] subkeys = new String[16];        // 16 khóa con dạng hex
        public List<BlockDetail> blocks = new ArrayList<>();  // Chi tiết từng khối
    }

    public static class BlockDetail {
        public int blockIndex;
        public String originalBlock;       // Khối gốc dạng hex
        public String afterIP;             // Sau hoán vị IP
        public String L0, R0;              // L0, R0 ban đầu
        public RoundDetail[] rounds = new RoundDetail[16];
        public String afterFinalSwap;      // Sau hoán đổi cuối
        public String cipherBlock;         // Khối mã hóa sau FP
    }

    public static class RoundDetail {
        public int roundNumber;
        public String expandedR;   // R mở rộng 48 bit
        public String subkey;      // Khóa con Ki
        public String xorResult;   // XOR(expandedR, subkey)
        public String sboxOutput;  // Đầu ra S-Box 32 bit
        public String pboxOutput;  // Đầu ra P-Box 32 bit
        public String Li, Ri;      // Li, Ri sau vòng
    }

    // ============================================================
    // SINH KHÓA CON (Key Schedule)
    // Tạo 16 khóa con 48 bit từ khóa 64 bit
    // ============================================================

    /**
     * Sinh 16 khóa con từ khóa 8 byte.
     * @param keyBytes mảng 8 byte khóa
     * @return mảng 16 phần tử, mỗi phần tử là mảng 48 bit (0/1)
     */
    public static int[][] generateSubkeys(byte[] keyBytes) {
        // Chuyển 8 byte thành 64 bit
        int[] keyBits = bytesToBits(keyBytes, 64);

        // Áp dụng PC-1: chọn 56 bit từ 64 bit
        int[] permuted56 = permute(keyBits, PC1);

        // Chia thành C0 (28 bit) và D0 (28 bit)
        int[] C = new int[28];
        int[] D = new int[28];
        System.arraycopy(permuted56, 0, C, 0, 28);
        System.arraycopy(permuted56, 28, D, 0, 28);

        // Tạo 16 khóa con
        int[][] subkeys = new int[16][];
        for (int round = 0; round < 16; round++) {
            // Dịch trái C và D theo lịch
            C = leftShift(C, SHIFT_SCHEDULE[round]);
            D = leftShift(D, SHIFT_SCHEDULE[round]);

            // Ghép C và D thành 56 bit
            int[] CD = new int[56];
            System.arraycopy(C, 0, CD, 0, 28);
            System.arraycopy(D, 0, CD, 28, 28);

            // Áp dụng PC-2: chọn 48 bit
            subkeys[round] = permute(CD, PC2);
        }
        return subkeys;
    }

    // ============================================================
    // MÃ HÓA MỘT KHỐI 8 BYTE
    // ============================================================

    /**
     * Mã hóa một khối 8 byte bằng DES.
     * @param block mảng đúng 8 byte
     * @param subkeys 16 khóa con
     * @return mảng 8 byte đã mã hóa
     */
    public static byte[] encryptBlock(byte[] block, int[][] subkeys) {
        return desProcess(block, subkeys, true);
    }

    /**
     * Giải mã một khối 8 byte bằng DES.
     * @param block mảng đúng 8 byte
     * @param subkeys 16 khóa con
     * @return mảng 8 byte đã giải mã
     */
    public static byte[] decryptBlock(byte[] block, int[][] subkeys) {
        return desProcess(block, subkeys, false);
    }

    /**
     * Lõi DES: xử lý một khối 64 bit qua 16 vòng Feistel.
     * @param block  8 byte đầu vào
     * @param subkeys 16 khóa con
     * @param encrypt true = mã hóa, false = giải mã
     * @return 8 byte đầu ra
     */
    private static byte[] desProcess(byte[] block, int[][] subkeys, boolean encrypt) {
        // Chuyển 8 byte → 64 bit
        int[] bits = bytesToBits(block, 64);

        // Hoán vị ban đầu IP
        int[] ipResult = permute(bits, IP);

        // Chia thành L (32 bit trái) và R (32 bit phải)
        int[] L = new int[32];
        int[] R = new int[32];
        System.arraycopy(ipResult, 0, L, 0, 32);
        System.arraycopy(ipResult, 32, R, 0, 32);

        // 16 vòng Feistel
        for (int round = 0; round < 16; round++) {
            // Khi giải mã, dùng khóa con theo thứ tự ngược
            int keyIndex = encrypt ? round : (15 - round);

            // Mở rộng R: 32 bit → 48 bit
            int[] expandedR = permute(R, E);

            // XOR với khóa con
            int[] xored = xor(expandedR, subkeys[keyIndex]);

            // Qua S-Box: 48 bit → 32 bit
            int[] sboxOut = sboxSubstitution(xored);

            // Hoán vị P
            int[] pboxOut = permute(sboxOut, P);

            // Tính Li, Ri mới
            int[] newR = xor(L, pboxOut);
            int[] newL = R;

            L = newL;
            R = newR;
        }

        // Hoán đổi cuối: nối R32 || L32
        int[] preFP = new int[64];
        System.arraycopy(R, 0, preFP, 0, 32);
        System.arraycopy(L, 0, preFP, 32, 32);

        // Hoán vị cuối FP
        int[] fpResult = permute(preFP, FP);

        // Chuyển 64 bit → 8 byte
        return bitsToBytes(fpResult);
    }

    // ============================================================
    // MÃ HÓA VÀ GIẢI MÃ TOÀN BỘ DỮ LIỆU (ECB + PKCS#7)
    // ============================================================

    /**
     * Mã hóa toàn bộ dữ liệu với PKCS#7 padding, ECB mode.
     * @param plaintext mảng byte dữ liệu gốc
     * @param keyBytes  mảng 8 byte khóa
     * @return mảng byte dữ liệu đã mã hóa
     */
    public static byte[] encrypt(byte[] plaintext, byte[] keyBytes) {
        int[][] subkeys = generateSubkeys(keyBytes);
        byte[] padded = pkcs7Pad(plaintext);
        byte[] ciphertext = new byte[padded.length];

        for (int i = 0; i < padded.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(padded, i, block, 0, 8);
            byte[] encBlock = encryptBlock(block, subkeys);
            System.arraycopy(encBlock, 0, ciphertext, i, 8);
        }
        return ciphertext;
    }

    /**
     * Giải mã toàn bộ dữ liệu và loại bỏ PKCS#7 padding.
     * @param ciphertext mảng byte dữ liệu mã hóa
     * @param keyBytes   mảng 8 byte khóa
     * @return mảng byte dữ liệu gốc
     * @throws IllegalArgumentException nếu độ dài không hợp lệ
     */
    public static byte[] decrypt(byte[] ciphertext, byte[] keyBytes) {
        if (ciphertext.length == 0 || ciphertext.length % 8 != 0) {
            throw new IllegalArgumentException(
                    "Độ dài dữ liệu mã hóa phải là bội số của 8 byte. " +
                            "Hiện tại: " + ciphertext.length + " byte.");
        }
        int[][] subkeys = generateSubkeys(keyBytes);
        byte[] plaintext = new byte[ciphertext.length];

        for (int i = 0; i < ciphertext.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(ciphertext, i, block, 0, 8);
            byte[] decBlock = decryptBlock(block, subkeys);
            System.arraycopy(decBlock, 0, plaintext, i, 8);
        }
        return removePaddingIfExists(plaintext);
    }

    public static byte[] removePaddingIfExists(byte[] data) {

        if (data.length == 0) return data;

        int padLen = data[data.length - 1] & 0xFF;

        if (padLen < 1 || padLen > 8 || padLen > data.length)
            return data;

        for (int i = data.length - padLen; i < data.length; i++) {
            if ((data[i] & 0xFF) != padLen)
                return data;
        }

        byte[] result = new byte[data.length - padLen];
        System.arraycopy(data, 0, result, 0, result.length);

        return result;
    }

    // ============================================================
    // MÃ HÓA VỚI THU THẬP CHI TIẾT THUẬT TOÁN
    // ============================================================

    /**
     * Mã hóa và thu thập chi tiết từng bước để hiển thị giáo dục.
     * @param plaintext mảng byte dữ liệu gốc
     * @param keyBytes  mảng 8 byte khóa
     * @return đối tượng AlgorithmDetails chứa toàn bộ chi tiết
     */
    public static AlgorithmDetails encryptWithDetails(byte[] plaintext, byte[] keyBytes) {
        AlgorithmDetails details = new AlgorithmDetails();
        int[][] subkeys = generateSubkeys(keyBytes);

        // Lưu các khóa con dạng hex
        for (int i = 0; i < 16; i++) {
            details.subkeys[i] = bitsToHex(subkeys[i]);
        }

        byte[] padded = pkcs7Pad(plaintext);

        // Xử lý từng khối
        for (int blockIdx = 0; blockIdx < padded.length; blockIdx += 8) {
            byte[] block = new byte[8];
            System.arraycopy(padded, blockIdx, block, 0, 8);

            BlockDetail bd = new BlockDetail();
            bd.blockIndex = blockIdx / 8;
            bd.originalBlock = bytesToHex(block);

            // Bắt đầu xử lý khối
            int[] bits = bytesToBits(block, 64);
            int[] ipResult = permute(bits, IP);
            bd.afterIP = bitsToHex(ipResult);

            int[] L = new int[32];
            int[] R = new int[32];
            System.arraycopy(ipResult, 0, L, 0, 32);
            System.arraycopy(ipResult, 32, R, 0, 32);

            bd.L0 = bitsToHex(L);
            bd.R0 = bitsToHex(R);

            // 16 vòng
            for (int round = 0; round < 16; round++) {
                RoundDetail rd = new RoundDetail();
                rd.roundNumber = round + 1;

                int[] expandedR = permute(R, E);
                rd.expandedR = bitsToHex(expandedR);
                rd.subkey = bitsToHex(subkeys[round]);

                int[] xored = xor(expandedR, subkeys[round]);
                rd.xorResult = bitsToHex(xored);

                int[] sboxOut = sboxSubstitution(xored);
                rd.sboxOutput = bitsToHex(sboxOut);

                int[] pboxOut = permute(sboxOut, P);
                rd.pboxOutput = bitsToHex(pboxOut);

                int[] newR = xor(L, pboxOut);
                int[] newL = R;

                L = newL;
                R = newR;

                rd.Li = bitsToHex(L);
                rd.Ri = bitsToHex(R);

                bd.rounds[round] = rd;
            }

            // Hoán đổi cuối
            int[] preFP = new int[64];
            System.arraycopy(R, 0, preFP, 0, 32);
            System.arraycopy(L, 0, preFP, 32, 32);
            bd.afterFinalSwap = bitsToHex(preFP);

            // Hoán vị FP
            int[] fpResult = permute(preFP, FP);
            byte[] cipherBlock = bitsToBytes(fpResult);
            bd.cipherBlock = bytesToHex(cipherBlock);

            details.blocks.add(bd);
        }

        return details;
    }

    // ============================================================
    // ĐỆM PKCS#7
    // ============================================================

    /**
     * Áp dụng đệm PKCS#7 để độ dài là bội số của 8.
     * Nếu độ dài đã là bội số của 8, thêm một khối đệm 8 byte.
     */
    public static byte[] pkcs7Pad(byte[] data) {

        // Nếu đã đủ 8 byte thì không padding
        if (data.length % 8 == 0) {
            return data.clone();
        }

        int padLen = 8 - (data.length % 8);

        byte[] padded = new byte[data.length + padLen];
        System.arraycopy(data, 0, padded, 0, data.length);

        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) padLen;
        }

        return padded;
    }

    /**
     * Loại bỏ đệm PKCS#7 sau giải mã.
     * @throws IllegalArgumentException nếu đệm không hợp lệ
     */
    public static byte[] pkcs7Unpad(byte[] data) {
        if (data.length == 0) {
            throw new IllegalArgumentException("Dữ liệu giải mã rỗng.");
        }
        int padLen = data[data.length - 1] & 0xFF;
        if (padLen < 1 || padLen > 8 || padLen > data.length) {
            throw new IllegalArgumentException(
                    "Đệm PKCS#7 không hợp lệ. Giá trị đệm: " + padLen);
        }
        // Kiểm tra tất cả byte đệm đều có giá trị đúng
        for (int i = data.length - padLen; i < data.length; i++) {
            if ((data[i] & 0xFF) != padLen) {
                throw new IllegalArgumentException(
                        "Đệm PKCS#7 không hợp lệ. Khóa hoặc dữ liệu có thể sai.");
            }
        }
        byte[] unpadded = new byte[data.length - padLen];
        System.arraycopy(data, 0, unpadded, 0, unpadded.length);
        return unpadded;
    }

    // ============================================================
    // CÁC HÀM TIỆN ÍCH NỘI BỘ
    // ============================================================

    /**
     * Hoán vị mảng bit theo bảng hoán vị cho trước.
     * Chú ý: bảng hoán vị dùng chỉ số bắt đầu từ 1.
     */
    private static int[] permute(int[] input, int[] table) {
        int[] output = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            output[i] = input[table[i] - 1];
        }
        return output;
    }

    /**
     * XOR hai mảng bit có cùng độ dài.
     */
    private static int[] xor(int[] a, int[] b) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    /**
     * Dịch trái vòng (circular left shift) mảng bit 28 phần tử.
     */
    private static int[] leftShift(int[] bits, int shift) {
        int[] result = new int[bits.length];
        for (int i = 0; i < bits.length; i++) {
            result[i] = bits[(i + shift) % bits.length];
        }
        return result;
    }

    /**
     * Thay thế S-Box: 48 bit → 32 bit.
     * Chia 48 bit thành 8 nhóm 6 bit, mỗi nhóm qua một S-Box.
     */
    private static int[] sboxSubstitution(int[] input48) {
        int[] output = new int[32];
        for (int i = 0; i < 8; i++) {
            int offset = i * 6;
            // Bit đầu và bit cuối tạo chỉ số hàng (0-3)
            int row = (input48[offset] << 1) | input48[offset + 5];
            // 4 bit giữa tạo chỉ số cột (0-15)
            int col = (input48[offset + 1] << 3)
                    | (input48[offset + 2] << 2)
                    | (input48[offset + 3] << 1)
                    |  input48[offset + 4];
            int val = S_BOX[i][row][col];
            // Chuyển giá trị 4 bit vào mảng kết quả
            for (int b = 0; b < 4; b++) {
                output[i * 4 + b] = (val >> (3 - b)) & 1;
            }
        }
        return output;
    }

    /**
     * Chuyển mảng byte thành mảng bit (MSB trước).
     * @param numBits số bit cần lấy
     */
    public static int[] bytesToBits(byte[] bytes, int numBits) {
        int[] bits = new int[numBits];
        for (int i = 0; i < numBits; i++) {
            int byteIdx = i / 8;
            int bitIdx  = 7 - (i % 8);
            bits[i] = (bytes[byteIdx] >> bitIdx) & 1;
        }
        return bits;
    }

    /**
     * Chuyển mảng bit thành mảng byte.
     */
    public static byte[] bitsToBytes(int[] bits) {
        byte[] bytes = new byte[bits.length / 8];
        for (int i = 0; i < bytes.length; i++) {
            int val = 0;
            for (int b = 0; b < 8; b++) {
                val = (val << 1) | bits[i * 8 + b];
            }
            bytes[i] = (byte) val;
        }
        return bytes;
    }

    /**
     * Chuyển mảng byte thành chuỗi Hex chữ hoa.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b & 0xFF));
        }
        return sb.toString();
    }

    /**
     * Chuyển chuỗi Hex thành mảng byte.
     * @throws IllegalArgumentException nếu chuỗi không hợp lệ
     */
    public static byte[] hexToBytes(String hex) {
        hex = hex.trim().toUpperCase();
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException(
                    "Chuỗi Hex phải có số ký tự chẵn. Độ dài hiện tại: " + hex.length());
        }
        if (!hex.matches("[0-9A-F]*")) {
            throw new IllegalArgumentException(
                    "Chuỗi Hex chỉ được chứa ký tự 0-9 và A-F.");
        }
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    /**
     * Chuyển mảng bit thành chuỗi Hex (dùng cho hiển thị chi tiết).
     */
    public static String bitsToHex(int[] bits) {
        // Gom thành byte để hiển thị
        int numBytes = (bits.length + 7) / 8;
        byte[] bytes = new byte[numBytes];
        for (int i = 0; i < bits.length; i++) {
            int byteIdx = i / 8;
            int bitIdx  = 7 - (i % 8);
            if (bits[i] == 1) {
                bytes[byteIdx] |= (byte)(1 << bitIdx);
            }
        }
        return bytesToHex(bytes);
    }

    /**
     * Mã hóa Base64 thủ công.
     */
    public static String bytesToBase64(byte[] data) {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < data.length) {
            int b0 = data[i++] & 0xFF;
            int b1 = (i < data.length) ? (data[i++] & 0xFF) : 0;
            int b2 = (i < data.length) ? (data[i++] & 0xFF) : 0;
            sb.append(CHARS.charAt(b0 >> 2));
            sb.append(CHARS.charAt(((b0 & 3) << 4) | (b1 >> 4)));
            sb.append(CHARS.charAt(((b1 & 0xF) << 2) | (b2 >> 6)));
            sb.append(CHARS.charAt(b2 & 0x3F));
        }
        // Thêm ký tự đệm '='
        int mod = data.length % 3;
        if (mod == 1) {
            sb.setCharAt(sb.length() - 2, '=');
            sb.setCharAt(sb.length() - 1, '=');
        } else if (mod == 2) {
            sb.setCharAt(sb.length() - 1, '=');
        }
        return sb.toString();
    }

    /**
     * Giải mã Base64 thủ công.
     * @throws IllegalArgumentException nếu chuỗi không hợp lệ
     */
    public static byte[] base64ToBytes(String base64) {
        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        base64 = base64.trim().replaceAll("\\s", "");
        if (base64.length() % 4 != 0) {
            throw new IllegalArgumentException(
                    "Chuỗi Base64 không hợp lệ (độ dài phải là bội số của 4).");
        }
        int outLen = (base64.length() / 4) * 3;
        if (base64.endsWith("==")) outLen -= 2;
        else if (base64.endsWith("=")) outLen -= 1;

        byte[] out = new byte[outLen];
        int outIdx = 0;
        for (int i = 0; i < base64.length(); i += 4) {
            int v0 = decodeBase64Char(CHARS, base64.charAt(i));
            int v1 = decodeBase64Char(CHARS, base64.charAt(i + 1));
            int v2 = base64.charAt(i + 2) == '=' ? 0 : decodeBase64Char(CHARS, base64.charAt(i + 2));
            int v3 = base64.charAt(i + 3) == '=' ? 0 : decodeBase64Char(CHARS, base64.charAt(i + 3));
            int combined = (v0 << 18) | (v1 << 12) | (v2 << 6) | v3;
            if (outIdx < outLen) out[outIdx++] = (byte)((combined >> 16) & 0xFF);
            if (outIdx < outLen) out[outIdx++] = (byte)((combined >> 8) & 0xFF);
            if (outIdx < outLen) out[outIdx++] = (byte)(combined & 0xFF);
        }
        return out;
    }

    private static int decodeBase64Char(String chars, char c) {
        int idx = chars.indexOf(c);
        if (idx < 0) {
            throw new IllegalArgumentException(
                    "Ký tự không hợp lệ trong chuỗi Base64: '" + c + "'");
        }
        return idx;
    }
}