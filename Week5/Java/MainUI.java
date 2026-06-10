import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * MainUI.java  –  Phiên bản nâng cấp
 *
 * Giao diện hai panel song song: MÃ HÓA (trái) | GIẢI MÃ (phải)
 * Giữ nguyên toàn bộ DESAlgorithm.java, chỉ refactor UI.
 */
public class MainUI extends JFrame {

    // ============================================================
    // MÀU SẮC & FONT
    // ============================================================
    private static final Color COLOR_BG       = new Color(0xF0F4F8);
    private static final Color COLOR_PANEL    = new Color(0xFFFFFF);
    private static final Color COLOR_PRIMARY  = new Color(0x1565C0);
    private static final Color COLOR_ENCRYPT  = new Color(0x2E7D32);
    private static final Color COLOR_DECRYPT  = new Color(0x1565C0);
    private static final Color COLOR_CLEAR    = new Color(0xC62828);
    private static final Color COLOR_OTHER    = new Color(0x546E7A);
    private static final Color COLOR_KEYGEN   = new Color(0x6A1B9A);
    private static final Color COLOR_DETAIL   = new Color(0xE65100);
    private static final Color COLOR_TEXT     = new Color(0x212121);
    private static final Color COLOR_BORDER   = new Color(0xCFD8DC);
    private static final Color COLOR_RESULT_BG= new Color(0xF8FAFB);
    private static final Color COLOR_HDR_ENC  = new Color(0xE8F5E9);
    private static final Color COLOR_HDR_DEC  = new Color(0xE3F2FD);

    private static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_MONO   = new Font("Consolas", Font.PLAIN, 13);
    private static final Font FONT_BTN    = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  14);

    // ============================================================
    // PANEL MÃ HÓA
    // ============================================================
    private JRadioButton encRbPlainText, encRbPlainHex, encRbPlainBase64;
    private ButtonGroup  encBgPlain;
    private JTextArea    encTaPlain;

    private JRadioButton encRbKeyAscii, encRbKeyHex;
    private ButtonGroup  encBgKey;
    private JTextField   encTfKey;

    private JRadioButton encRbCipherHex, encRbCipherBase64;
    private ButtonGroup  encBgCipher;
    private JTextArea    encTaCipher;

    // ── Snapshot toàn vẹn trong RAM – chỉ tồn tại trong phiên chạy ──
    // Được ghi sau mỗi lần mã hóa thành công.
    // Integrity Check so sánh với giá trị này khi giải mã.
    // Không bao giờ ghi ra file.
    private String ramCipherHash = null;   // SHA-256 của bản mã vừa mã hóa
    private String ramKeyHash    = null;   // SHA-256 của khóa vừa dùng mã hóa

    // ============================================================
    // PANEL GIẢI MÃ
    // ============================================================
    private JRadioButton decRbCipherHex, decRbCipherBase64;
    private ButtonGroup  decBgCipher;
    private JTextArea    decTaCipher;

    private JRadioButton decRbKeyAscii, decRbKeyHex;
    private ButtonGroup  decBgKey;
    private JTextField   decTfKey;

    private JRadioButton decRbPlainText, decRbPlainHex, decRbPlainBase64;
    private ButtonGroup  decBgPlain;
    private JTextArea    decTaPlain;



    // ============================================================
    // KHỞI TẠO
    // ============================================================
    public MainUI() {
        setTitle("CÔNG CỤ MÃ HÓA VÀ GIẢI MÃ DES");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setPreferredSize(new Dimension(1280, 860));
        getContentPane().setBackground(COLOR_BG);

        buildUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ============================================================
    // XÂY DỰNG GIAO DIỆN CHÍNH
    // ============================================================
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(COLOR_BG);

        // Tiêu đề trên cùng
        JLabel titleLbl = new JLabel("CÔNG CỤ MÃ HÓA VÀ GIẢI MÃ DES  (ECB / PKCS#7)",
                SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(COLOR_PRIMARY);
        titleLbl.setBorder(new EmptyBorder(14, 0, 10, 0));
        root.add(titleLbl, BorderLayout.NORTH);

        // Hai panel song song
        JPanel center = new JPanel(new GridLayout(1, 2, 12, 0));
        center.setBackground(COLOR_BG);
        center.setBorder(new EmptyBorder(0, 12, 12, 12));

        JScrollPane encScroll = new JScrollPane(buildEncryptPanel());
        encScroll.setBorder(null);
        encScroll.getVerticalScrollBar().setUnitIncrement(16);

        JScrollPane decScroll = new JScrollPane(buildDecryptPanel());
        decScroll.setBorder(null);
        decScroll.getVerticalScrollBar().setUnitIncrement(16);

        center.add(encScroll);
        center.add(decScroll);

        root.add(center, BorderLayout.CENTER);

        JScrollPane outerScroll = new JScrollPane(root);
        outerScroll.setBorder(null);
        add(outerScroll);
    }

    // ============================================================
    // PANEL MÃ HÓA
    // ============================================================
    private JPanel buildEncryptPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Header
        panel.add(buildPanelHeader("MÃ HÓA", COLOR_HDR_ENC, COLOR_ENCRYPT));
        panel.add(vgap(8));

        // Định dạng bản rõ
        panel.add(buildRadioCard("Định dạng bản rõ",
                encRbPlainText   = createRadio("Văn bản", true),
                encRbPlainHex    = createRadio("Hex",     false),
                encRbPlainBase64 = createRadio("Base64",  false)));
        encBgPlain = new ButtonGroup();
        encBgPlain.add(encRbPlainText);
        encBgPlain.add(encRbPlainHex);
        encBgPlain.add(encRbPlainBase64);
        panel.add(vgap(6));

        // Bản rõ input
        encTaPlain = createTextArea(5);
        panel.add(buildTextAreaCard("Bản rõ", encTaPlain));
        panel.add(vgap(6));

        // Khóa
        encTfKey = createTextField();
        encRbKeyAscii = createRadio("ASCII", true);
        encRbKeyHex   = createRadio("Hex",   false);
        encBgKey      = new ButtonGroup();
        encBgKey.add(encRbKeyAscii);
        encBgKey.add(encRbKeyHex);
        panel.add(buildKeyCard("Loại khóa", encRbKeyAscii, encRbKeyHex, encTfKey));
        panel.add(vgap(6));

        // Định dạng bản mã
        encRbCipherHex    = createRadio("Hex",    true);
        encRbCipherBase64 = createRadio("Base64", false);
        encBgCipher = new ButtonGroup();
        encBgCipher.add(encRbCipherHex);
        encBgCipher.add(encRbCipherBase64);
        panel.add(buildRadioCard("Định dạng bản mã", encRbCipherHex, encRbCipherBase64));
        panel.add(vgap(6));

        // Các nút
        panel.add(buildEncryptButtons());
        panel.add(vgap(6));

        // Bản mã output
        encTaCipher = createTextArea(5);
        encTaCipher.setEditable(false);
        encTaCipher.setBackground(COLOR_RESULT_BG);
        panel.add(buildTextAreaCard("Bản mã", encTaCipher));

        return panel;
    }

    private JPanel buildEncryptButtons() {
        JPanel outer = createCard();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));

        // Hàng 1: hành động chính
        outer.add(buildButtonRow(
                makeBtn("Mã hóa",             COLOR_ENCRYPT, e -> doEncrypt()),
                makeBtn("Xóa",                COLOR_CLEAR,   e -> doEncryptClear()),
                makeBtn("Tạo khóa tự động",   COLOR_KEYGEN,  e -> doEncGenKey())
        ));
        outer.add(vgap(4));

        // Hàng 2: sao chép
        outer.add(buildButtonRow(
                makeBtn("Sao chép bản rõ",   COLOR_OTHER, e -> doCopy(encTaPlain,   "bản rõ")),
                makeBtn("Sao chép khóa",     COLOR_OTHER, e -> doCopy(encTfKey,     "khóa")),
                makeBtn("Sao chép bản mã",   COLOR_OTHER, e -> doCopy(encTaCipher,  "bản mã"))
        ));
        outer.add(vgap(4));

        // Hàng 3: tải / lưu
        outer.add(buildButtonRow(
                makeBtn("Tải bản rõ",  COLOR_OTHER, e -> doLoadPlainEnc()),
                makeBtn("Tải khóa",    COLOR_OTHER, e -> doLoadKeyEnc()),
                makeBtn("Lưu bản rõ",  COLOR_OTHER, e -> doSavePlainEnc()),
                makeBtn("Lưu bản mã",  COLOR_OTHER, e -> doSaveCipherEnc()),
                makeBtn("Lưu khóa",    COLOR_OTHER, e -> doSaveKeyEnc())
        ));
        outer.add(vgap(4));

        // Hàng 4: chi tiết
        outer.add(buildButtonRow(
                makeBtn("Chi tiết thuật toán", COLOR_DETAIL, e -> doShowDetailsEnc())
        ));

        return outer;
    }

    // ============================================================
    // PANEL GIẢI MÃ
    // ============================================================
    private JPanel buildDecryptPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));

        // Header
        panel.add(buildPanelHeader("GIẢI MÃ", COLOR_HDR_DEC, COLOR_DECRYPT));
        panel.add(vgap(8));

        // Định dạng bản mã
        decRbCipherHex    = createRadio("Hex",    true);
        decRbCipherBase64 = createRadio("Base64", false);
        decBgCipher = new ButtonGroup();
        decBgCipher.add(decRbCipherHex);
        decBgCipher.add(decRbCipherBase64);
        panel.add(buildRadioCard("Định dạng bản mã", decRbCipherHex, decRbCipherBase64));
        panel.add(vgap(6));

        // Bản mã input
        decTaCipher = createTextArea(5);
        panel.add(buildTextAreaCard("Bản mã", decTaCipher));
        panel.add(vgap(6));

        // Khóa
        decTfKey    = createTextField();
        decRbKeyAscii = createRadio("ASCII", true);
        decRbKeyHex   = createRadio("Hex",   false);
        decBgKey      = new ButtonGroup();
        decBgKey.add(decRbKeyAscii);
        decBgKey.add(decRbKeyHex);
        panel.add(buildKeyCard("Loại khóa", decRbKeyAscii, decRbKeyHex, decTfKey));
        panel.add(vgap(6));

        // Định dạng bản rõ
        panel.add(buildRadioCard("Định dạng bản rõ",
                decRbPlainText   = createRadio("Văn bản", true),
                decRbPlainHex    = createRadio("Hex",     false),
                decRbPlainBase64 = createRadio("Base64",  false)));
        decBgPlain = new ButtonGroup();
        decBgPlain.add(decRbPlainText);
        decBgPlain.add(decRbPlainHex);
        decBgPlain.add(decRbPlainBase64);
        panel.add(vgap(6));

        // Các nút
        panel.add(buildDecryptButtons());
        panel.add(vgap(6));

        // Bản rõ output
        decTaPlain = createTextArea(5);
        decTaPlain.setEditable(false);
        decTaPlain.setBackground(COLOR_RESULT_BG);
        panel.add(buildTextAreaCard("Bản rõ", decTaPlain));

        return panel;
    }

    private JPanel buildDecryptButtons() {
        JPanel outer = createCard();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));

        outer.add(buildButtonRow(
                makeBtn("Giải mã",           COLOR_DECRYPT, e -> doDecrypt()),
                makeBtn("Xóa",               COLOR_CLEAR,   e -> doDecryptClear()),
                makeBtn("Tạo khóa tự động",  COLOR_KEYGEN,  e -> doDecGenKey())
        ));
        outer.add(vgap(4));

        outer.add(buildButtonRow(
                makeBtn("Sao chép bản mã",  COLOR_OTHER, e -> doCopy(decTaCipher, "bản mã")),
                makeBtn("Sao chép khóa",    COLOR_OTHER, e -> doCopy(decTfKey,    "khóa")),
                makeBtn("Sao chép bản rõ",  COLOR_OTHER, e -> doCopy(decTaPlain,  "bản rõ"))
        ));
        outer.add(vgap(4));

        outer.add(buildButtonRow(
                makeBtn("Tải bản mã",  COLOR_OTHER, e -> doLoadCipherDec()),
                makeBtn("Tải khóa",    COLOR_OTHER, e -> doLoadKeyDec()),
                makeBtn("Lưu bản rõ",  COLOR_OTHER, e -> doSavePlainDec()),
                makeBtn("Lưu bản mã",  COLOR_OTHER, e -> doSaveCipherDec()),
                makeBtn("Lưu khóa",    COLOR_OTHER, e -> doSaveKeyDec())
        ));
        outer.add(vgap(4));

        outer.add(buildButtonRow(
                makeBtn("Chi tiết thuật toán", COLOR_DETAIL, e -> doShowDetailsDec())
        ));

        return outer;
    }

    // ============================================================
    // XỬ LÝ – MÃ HÓA
    // ============================================================
    private void doEncrypt() {
        try {
            byte[] keyBytes = readAndValidateKey(encTfKey, encRbKeyAscii, encRbKeyHex);
            if (keyBytes == null) return;

            String plainText = encTaPlain.getText();
            if (plainText == null || plainText.isEmpty()) {
                showError("Chưa nhập bản rõ.");
                return;
            }

            byte[] inputBytes = parsePlainInput(plainText, encRbPlainText, encRbPlainHex, encRbPlainBase64);
            if (inputBytes == null) return;

            byte[] cipherBytes = DESAlgorithm.encrypt(inputBytes, keyBytes);

            String result = formatCipherOutput(cipherBytes, encRbCipherHex, encRbCipherBase64);
            encTaCipher.setText(result);

            // ── Lưu snapshot toàn vẹn vào RAM (chỉ trong phiên này) ─
            ramCipherHash = sha256Hex(result.trim());
            ramKeyHash    = sha256Hex(encTfKey.getText().trim());
            // ──────────────────────────────────────────────────────

            showInfo("Mã hóa thành công.");

        } catch (Exception ex) {
            showError("Lỗi mã hóa: " + ex.getMessage());
        }
    }

    private void doEncryptClear() {
        encTaPlain.setText("");
        encTaCipher.setText("");
        encTfKey.setText("");
        encRbPlainText.setSelected(true);
        encRbKeyAscii.setSelected(true);
        encRbCipherHex.setSelected(true);
        // Xóa snapshot RAM
        ramCipherHash = null;
        ramKeyHash    = null;
        showInfo("Đã xóa dữ liệu thành công.");
    }

    private void doEncGenKey() {
        String key = generateKey(encRbKeyAscii, encRbKeyHex);
        if (key != null) {
            encTfKey.setText(key);
            showInfo("Đã tạo khóa ngẫu nhiên thành công.");
        }
    }

    // ============================================================
    // XỬ LÝ – GIẢI MÃ
    // ============================================================
    private void doDecrypt() {
        try {
            // 1. Khóa
            byte[] keyBytes = readAndValidateKey(decTfKey, decRbKeyAscii, decRbKeyHex);
            if (keyBytes == null) return;

            // 2. Bản mã
            String cipherText = decTaCipher.getText();
            if (cipherText == null || cipherText.trim().isEmpty()) {
                showError("Chưa nhập bản mã.");
                return;
            }

            byte[] cipherBytes = parseCipherInput(cipherText, decRbCipherHex, decRbCipherBase64);
            if (cipherBytes == null) return;

            // 3. Kiểm tra tính toàn vẹn (chỉ trong cùng phiên chạy)
            if (ramCipherHash != null || ramKeyHash != null) {
                boolean cipherTampered = false;
                boolean keyTampered    = false;

                if (ramCipherHash != null) {
                    cipherTampered = !sha256Hex(cipherText.trim()).equals(ramCipherHash);
                }
                if (ramKeyHash != null) {
                    keyTampered = !sha256Hex(decTfKey.getText().trim()).equals(ramKeyHash);
                }

                if (cipherTampered && keyTampered) {
                    showError("Bản mã và khóa đã bị sửa đổi.\nKhông thể thực hiện giải mã.");
                    return;
                } else if (cipherTampered) {
                    showError("Bản mã đã bị sửa đổi.\nKhông thể thực hiện giải mã.");
                    return;
                } else if (keyTampered) {
                    showError("Khóa đã bị sửa đổi.\nKhông thể thực hiện giải mã.");
                    return;
                }
            }

            // 4. Kiểm tra độ dài bản mã
            if (cipherBytes.length == 0 || cipherBytes.length % 8 != 0) {
                showError("Độ dài bản mã phải là bội số của 8 byte.\n" +
                        "Độ dài hiện tại: " + cipherBytes.length + " byte.\n" +
                        "Vui lòng chọn đúng định dạng bản mã (Hex hoặc Base64).");
                return;
            }

            // 5. Giải mã
            byte[] plainBytes;
            try {
                plainBytes = DESAlgorithm.decrypt(cipherBytes, keyBytes);
            } catch (Exception ex) {
                showError("Dữ liệu bản mã không hợp lệ hoặc đã bị thay đổi.\n" + ex.getMessage());
                return;
            }

            // 6. Hiển thị
            String result = formatPlainOutput(plainBytes, decRbPlainText, decRbPlainHex, decRbPlainBase64);
            if (result != null) {
                decTaPlain.setText(result);
                showInfo("Giải mã thành công.");
            }

        } catch (Exception ex) {
            showError("Lỗi giải mã: " + ex.getMessage());
        }
    }

    private void doDecryptClear() {
        decTaCipher.setText("");
        decTaPlain.setText("");
        decTfKey.setText("");
        decRbCipherHex.setSelected(true);
        decRbKeyAscii.setSelected(true);
        decRbPlainText.setSelected(true);
        // Xóa snapshot RAM
        ramCipherHash = null;
        ramKeyHash    = null;
        showInfo("Đã xóa dữ liệu thành công.");
    }

    private void doDecGenKey() {
        String key = generateKey(decRbKeyAscii, decRbKeyHex);
        if (key != null) {
            decTfKey.setText(key);
            showInfo("Đã tạo khóa ngẫu nhiên thành công.");
        }
    }

    // ============================================================
    // SAO CHÉP
    // ============================================================
    /** Sao chép từ JTextArea */
    private void doCopy(JTextArea ta, String label) {
        String text = ta.getText();
        if (text == null || text.trim().isEmpty()) {
            showError("Không có dữ liệu để sao chép.");
            return;
        }
        copyToClipboard(text);
        showInfo("Đã sao chép " + label + " thành công.");
    }

    /** Sao chép từ JTextField */
    private void doCopy(JTextField tf, String label) {
        String text = tf.getText();
        if (text == null || text.trim().isEmpty()) {
            showError("Không có dữ liệu để sao chép.");
            return;
        }
        copyToClipboard(text);
        showInfo("Đã sao chép " + label + " thành công.");
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(text), null);
    }

    // ============================================================
    // TẢI / LƯU – PANEL MÃ HÓA
    // ============================================================
    private void doLoadPlainEnc() {
        String content = loadTextFile("Tải bản rõ");
        if (content != null) {
            encTaPlain.setText(content);
            showInfo("Đã tải bản rõ thành công.");
        }
    }

    private void doLoadKeyEnc() {
        String content = loadTextFile("Tải khóa");
        if (content != null) {
            encTfKey.setText(content.trim());
            showInfo("Đã tải khóa thành công.");
        }
    }

    /** Lưu bản mã (chỉ nội dung bản mã, không kèm hash) */
    private void doSaveCipherEnc() {
        saveTextFile(
                encTaCipher.getText(),
                "Lưu bản mã",
                "DES_Cipher",
                "Chưa có bản mã để lưu.",
                "Đã lưu bản mã thành công."
        );
    }

    /** Lưu khóa (chỉ nội dung khóa, không kèm hash) */
    private void doSaveKeyEnc() {
        saveTextFile(
                encTfKey.getText().trim(),
                "Lưu khóa",
                "DES_Key",
                "Chưa có khóa để lưu.",
                "Đã lưu khóa thành công."
        );
    }

    /**
     * Tự động đề xuất tên file không trùng trong thư mục hiện tại (user.home).
     * Ví dụ prefix="DES_Cipher" → "DES_Cipher_1.txt", "DES_Cipher_2.txt", ...
     */
    private File suggestFileName(String prefix) {
        File dir = new File(System.getProperty("user.home"));
        int n = 1;
        File candidate;
        do {
            candidate = new File(dir, prefix + "_" + n + ".txt");
            n++;
        } while (candidate.exists());
        return candidate;
    }

    /**
     * Hàm dùng chung: lưu chuỗi text thuần ra file .txt.
     * @param filePrefix  Tiền tố tên file đề xuất (ví dụ "DES_Cipher").
     *                    Nếu người dùng không đổi tên, chương trình tự chọn
     *                    tên tiếp theo chưa tồn tại (không ghi đè).
     */
    private void saveTextFile(String content, String dialogTitle,
                              String filePrefix, String emptyMessage,
                              String successMessage) {
        if (content == null || content.trim().isEmpty()) {
            showError(emptyMessage);
            return;
        }

        // Đề xuất tên file tự động, tăng số thứ tự nếu đã tồn tại
        File suggested = suggestFileName(filePrefix);

        JFileChooser fc = createFileChooser(dialogTitle, suggested.getAbsolutePath());
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = ensureTxtExtension(fc.getSelectedFile());

        // Nếu file đã tồn tại và người dùng không đổi tên → tăng số thứ tự
        if (file.exists()) {
            // Kiểm tra xem người dùng có giữ nguyên tên đề xuất không
            String chosenName = file.getName().replaceAll("\\.txt$", "");
            // Tự động tìm tên mới không trùng trong cùng thư mục
            File dir = file.getParentFile();
            int n = 1;
            File safe;
            do {
                safe = new File(dir, filePrefix + "_" + n + ".txt");
                n++;
            } while (safe.exists());

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Tệp \"" + file.getName() + "\" đã tồn tại.\n" +
                            "Bạn có muốn lưu với tên mới: \"" + safe.getName() + "\" không?",
                    "Tệp đã tồn tại",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                file = safe;
            } else if (choice == JOptionPane.NO_OPTION) {
                // Người dùng chọn ghi đè thủ công → giữ nguyên file
            } else {
                return; // Hủy
            }
        }

        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            w.write(content);
            showInfo(successMessage + "\nTệp: " + file.getAbsolutePath());
        } catch (IOException ex) {
            showError("Lỗi khi lưu tệp: " + ex.getMessage());
        }
    }

    /** Lưu bản rõ từ panel Mã hóa */
    private void doSavePlainEnc() {
        saveTextFile(
                encTaPlain.getText(),
                "Lưu bản rõ",
                "DES_Plaintext",
                "Không có bản rõ để lưu.",
                "Đã lưu bản rõ thành công."
        );
    }

    // ============================================================
    // TẢI / LƯU – PANEL GIẢI MÃ
    // ============================================================
    /** Tải bản mã (chỉ đọc nội dung bản mã thuần) */
    private void doLoadCipherDec() {
        String content = loadTextFile("Tải bản mã");
        if (content != null) {
            decTaCipher.setText(content.trim());
            showInfo("Đã tải bản mã thành công.");
        }
    }

    /** Tải khóa (chỉ đọc nội dung khóa thuần) */
    private void doLoadKeyDec() {
        String content = loadTextFile("Tải khóa");
        if (content != null) {
            decTfKey.setText(content.trim());
            showInfo("Đã tải khóa thành công.");
        }
    }

    private void doSavePlainDec() {
        saveTextFile(
                decTaPlain.getText(),
                "Lưu bản rõ",
                "DES_Plaintext",
                "Không có bản rõ để lưu.",
                "Đã lưu bản rõ thành công."
        );
    }

    private void doSaveKeyDec() {
        saveTextFile(
                decTfKey.getText().trim(),
                "Lưu khóa",
                "DES_Key",
                "Chưa có khóa để lưu.",
                "Đã lưu khóa thành công."
        );
    }

    /** Lưu bản mã từ panel Giải mã (chỉ nội dung bản mã, không kèm hash) */
    private void doSaveCipherDec() {
        saveTextFile(
                decTaCipher.getText(),
                "Lưu bản mã",
                "DES_Cipher",
                "Không có bản mã để lưu.",
                "Đã lưu bản mã thành công."
        );
    }

    // ============================================================
    // CHI TIẾT THUẬT TOÁN
    // ============================================================
    private void doShowDetailsEnc() {
        try {
            byte[] keyBytes = readAndValidateKey(encTfKey, encRbKeyAscii, encRbKeyHex);
            if (keyBytes == null) return;

            String plainText = encTaPlain.getText();
            if (plainText == null || plainText.isEmpty()) {
                showError("Chưa nhập bản rõ.");
                return;
            }
            byte[] inputBytes = parsePlainInput(plainText, encRbPlainText, encRbPlainHex, encRbPlainBase64);
            if (inputBytes == null) return;

            DESAlgorithm.AlgorithmDetails details = DESAlgorithm.encryptWithDetails(inputBytes, keyBytes);
            showDetailsDialog(buildDetailsText(details).toString());

        } catch (Exception ex) {
            showError("Lỗi khi tạo chi tiết thuật toán: " + ex.getMessage());
        }
    }

    private void doShowDetailsDec() {
        try {
            byte[] keyBytes = readAndValidateKey(decTfKey, decRbKeyAscii, decRbKeyHex);
            if (keyBytes == null) return;

            String cipherText = decTaCipher.getText();
            if (cipherText == null || cipherText.trim().isEmpty()) {
                showError("Chưa nhập bản mã.");
                return;
            }
            byte[] cipherBytes = parseCipherInput(cipherText, decRbCipherHex, decRbCipherBase64);
            if (cipherBytes == null) return;

            // Chi tiết dùng bản rõ giải mã được
            if (cipherBytes.length % 8 != 0) {
                showError("Độ dài bản mã không hợp lệ để hiển thị chi tiết.");
                return;
            }
            byte[] plainBytes = DESAlgorithm.decrypt(cipherBytes, keyBytes);
            DESAlgorithm.AlgorithmDetails details = DESAlgorithm.encryptWithDetails(plainBytes, keyBytes);
            showDetailsDialog(buildDetailsText(details).toString());

        } catch (Exception ex) {
            showError("Lỗi khi tạo chi tiết thuật toán: " + ex.getMessage());
        }
    }

    /** Giữ nguyên nội dung chi tiết từ code gốc */
    private StringBuilder buildDetailsText(DESAlgorithm.AlgorithmDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("    CHI TIẾT THUẬT TOÁN DES (ECB MODE)\n");
        sb.append("========================================\n\n");

        sb.append("╔══════════════════════════════════════╗\n");
        sb.append("║         LỊCH SINH KHÓA CON           ║\n");
        sb.append("╚══════════════════════════════════════╝\n");
        for (int i = 0; i < 16; i++) {
            sb.append(String.format("  K%-2d = %s\n", i + 1, details.subkeys[i]));
        }

        for (DESAlgorithm.BlockDetail bd : details.blocks) {
            sb.append("\n╔══════════════════════════════════════╗\n");
            sb.append(String.format("║         KHỐI %3d                      ║\n", bd.blockIndex + 1));
            sb.append("╚══════════════════════════════════════╝\n");
            sb.append("  Khối gốc          : ").append(bd.originalBlock).append("\n");
            sb.append("  Sau hoán vị IP    : ").append(bd.afterIP).append("\n");
            sb.append("  L0                : ").append(bd.L0).append("\n");
            sb.append("  R0                : ").append(bd.R0).append("\n");

            for (int r = 0; r < 16; r++) {
                DESAlgorithm.RoundDetail rd = bd.rounds[r];
                sb.append("\n  ── Vòng ").append(rd.roundNumber).append(" ──────────────────────\n");
                sb.append("    R mở rộng (E)   : ").append(rd.expandedR).append("\n");
                sb.append("    Khóa con K").append(String.format("%-2d", rd.roundNumber))
                        .append("      : ").append(rd.subkey).append("\n");
                sb.append("    XOR (E⊕K)       : ").append(rd.xorResult).append("\n");
                sb.append("    Đầu ra S-Box    : ").append(rd.sboxOutput).append("\n");
                sb.append("    Đầu ra P-Box    : ").append(rd.pboxOutput).append("\n");
                sb.append("    L").append(rd.roundNumber).append("                : ")
                        .append(rd.Li).append("\n");
                sb.append("    R").append(rd.roundNumber).append("                : ")
                        .append(rd.Ri).append("\n");
            }

            sb.append("\n  Sau hoán đổi cuối : ").append(bd.afterFinalSwap).append("\n");
            sb.append("  Khối mã hóa (FP)  : ").append(bd.cipherBlock).append("\n");
        }
        sb.append("\n========================================\n");
        return sb;
    }

    /** Giữ nguyên hộp thoại chi tiết từ code gốc */
    private void showDetailsDialog(String content) {
        JDialog dialog = new JDialog(this, "Chi tiết thuật toán DES", false);
        dialog.setSize(680, 620);
        dialog.setLocationRelativeTo(this);

        JTextArea ta = new JTextArea(content);
        ta.setFont(new Font("Consolas", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setBackground(new Color(0xFAFAFA));
        ta.setForeground(COLOR_TEXT);
        ta.setBorder(new EmptyBorder(10, 14, 10, 14));

        JScrollPane scroll = new JScrollPane(ta);
        scroll.setBorder(null);
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(COLOR_BG);
        JButton btnClose = createButton("Đóng", COLOR_OTHER);
        btnClose.addActionListener(e -> dialog.dispose());
        btns.add(btnClose);
        dialog.add(btns, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ============================================================
    // VALIDATE KHÓA (với xử lý thiếu / thừa ký tự)
    // ============================================================
    private byte[] readAndValidateKey(JTextField tfKey,
                                      JRadioButton rbAscii,
                                      JRadioButton rbHex) {
        String keyStr = tfKey.getText();  // không trim để giữ nguyên

        // --- Chưa nhập khóa ---
        if (keyStr == null || keyStr.trim().isEmpty()) {
            showError("Chưa nhập khóa.\nVui lòng nhập khóa hoặc sử dụng chức năng Tạo khóa tự động.");
            return null;
        }

        if (rbAscii.isSelected()) {
            int required = 8;
            int len = keyStr.length();

            if (len < required) {
                int missing = required - len;
                int choice = JOptionPane.showOptionDialog(this,
                        "Khóa ASCII đang thiếu " + missing + " ký tự.\nBạn muốn làm gì?",
                        "Thiếu ký tự", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null,
                        new String[]{"Quay lại chỉnh sửa", "Tự động bổ sung"}, "Quay lại chỉnh sửa");
                if (choice == 1) {
                    String padding = randomAsciiChars(missing);
                    String newKey = keyStr + padding;
                    tfKey.setText(newKey);
                    showInfo("Đã tự động bổ sung " + missing + " ký tự.\nKhóa mới: " + newKey);
                    keyStr = newKey;
                } else {
                    return null;
                }
            } else if (len > required) {
                int excess = len - required;
                int choice = JOptionPane.showOptionDialog(this,
                        "Khóa ASCII đang thừa " + excess + " ký tự.\nBạn muốn làm gì?",
                        "Thừa ký tự", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null,
                        new String[]{"Quay lại chỉnh sửa", "Tự động điều chỉnh"}, "Quay lại chỉnh sửa");
                if (choice == 1) {
                    String newKey = keyStr.substring(0, required);
                    tfKey.setText(newKey);
                    showInfo("Đã tự động cắt bớt " + excess + " ký tự.\nKhóa mới: " + newKey);
                    keyStr = newKey;
                } else {
                    return null;
                }
            }

            // Kiểm tra ký tự ASCII hợp lệ
            for (char c : keyStr.toCharArray()) {
                if (c < 0x20 || c > 0x7E) {
                    showError("Khóa ASCII chứa ký tự không hợp lệ: '" + c + "'\n" +
                            "Chỉ được dùng ký tự ASCII có mã từ 0x20 đến 0x7E.");
                    return null;
                }
            }
            return keyStr.getBytes(StandardCharsets.US_ASCII);

        } else {
            // Hex key
            keyStr = keyStr.trim();
            int required = 16;
            int len = keyStr.length();

            // Kiểm tra ký tự hex hợp lệ trước
            if (!keyStr.isEmpty() && !keyStr.matches("[0-9A-Fa-f]+")) {
                showError("Khóa Hex chỉ được chứa ký tự 0-9, A-F, a-f.");
                return null;
            }

            if (len < required) {
                int missing = required - len;
                int choice = JOptionPane.showOptionDialog(this,
                        "Khóa Hex đang thiếu " + missing + " ký tự.\nBạn muốn làm gì?",
                        "Thiếu ký tự", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null,
                        new String[]{"Quay lại chỉnh sửa", "Tự động bổ sung"}, "Quay lại chỉnh sửa");
                if (choice == 1) {
                    String padding = randomHexChars(missing);
                    String newKey = keyStr + padding;
                    tfKey.setText(newKey);
                    showInfo("Đã tự động bổ sung " + missing + " ký tự hex.\nKhóa mới: " + newKey);
                    keyStr = newKey;
                } else {
                    return null;
                }
            } else if (len > required) {
                int excess = len - required;
                int choice = JOptionPane.showOptionDialog(this,
                        "Khóa Hex đang thừa " + excess + " ký tự.\nBạn muốn làm gì?",
                        "Thừa ký tự", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null,
                        new String[]{"Quay lại chỉnh sửa", "Tự động điều chỉnh"}, "Quay lại chỉnh sửa");
                if (choice == 1) {
                    String newKey = keyStr.substring(0, required);
                    tfKey.setText(newKey);
                    showInfo("Đã tự động cắt bớt " + excess + " ký tự.\nKhóa mới: " + newKey);
                    keyStr = newKey;
                } else {
                    return null;
                }
            }

            try {
                return DESAlgorithm.hexToBytes(keyStr);
            } catch (Exception ex) {
                showError("Khóa Hex không hợp lệ: " + ex.getMessage());
                return null;
            }
        }
    }

    // ============================================================
    // PARSE INPUT / FORMAT OUTPUT
    // ============================================================
    private byte[] parsePlainInput(String input, JRadioButton rbText,
                                   JRadioButton rbHex, JRadioButton rbBase64) {
        if (rbText.isSelected()) {
            return input.getBytes(StandardCharsets.UTF_8);

        } else if (rbHex.isSelected()) {
            String hexStr = input.trim().replaceAll("\\s+", "");
            if (hexStr.isEmpty()) { showError("Dữ liệu Hex trống."); return null; }
            if (hexStr.length() % 2 != 0) {
                showError("Dữ liệu Hex không hợp lệ.\nSố ký tự phải là số chẵn."); return null;
            }
            if (!hexStr.matches("[0-9A-Fa-f]+")) {
                showError("Dữ liệu Hex không hợp lệ.\nChỉ được dùng: 0-9, A-F, a-f."); return null;
            }
            try {
                return DESAlgorithm.hexToBytes(hexStr);
            } catch (Exception ex) {
                showError("Lỗi đọc Hex: " + ex.getMessage()); return null;
            }

        } else {
            try {
                return DESAlgorithm.base64ToBytes(input.trim());
            } catch (Exception ex) {
                showError("Dữ liệu Base64 không hợp lệ: " + ex.getMessage()); return null;
            }
        }
    }

    private byte[] parseCipherInput(String input, JRadioButton rbHex, JRadioButton rbBase64) {
        if (rbHex.isSelected()) {
            String hexStr = input.trim().replaceAll("\\s+", "");
            if (hexStr.isEmpty()) { showError("Dữ liệu Hex trống."); return null; }
            if (hexStr.length() % 2 != 0) {
                showError("Dữ liệu Hex không hợp lệ.\nSố ký tự phải là số chẵn."); return null;
            }
            if (!hexStr.matches("[0-9A-Fa-f]+")) {
                showError("Dữ liệu Hex không hợp lệ.\nChỉ được dùng: 0-9, A-F, a-f."); return null;
            }
            try {
                return DESAlgorithm.hexToBytes(hexStr);
            } catch (Exception ex) {
                showError("Lỗi đọc Hex: " + ex.getMessage()); return null;
            }
        } else {
            try {
                return DESAlgorithm.base64ToBytes(input.trim());
            } catch (Exception ex) {
                showError("Dữ liệu Base64 không hợp lệ: " + ex.getMessage()); return null;
            }
        }
    }

    private String formatCipherOutput(byte[] bytes, JRadioButton rbHex, JRadioButton rbBase64) {
        if (rbHex.isSelected()) return DESAlgorithm.bytesToHex(bytes);
        return DESAlgorithm.bytesToBase64(bytes);
    }

    private String formatPlainOutput(byte[] bytes, JRadioButton rbText,
                                     JRadioButton rbHex, JRadioButton rbBase64) {
        if (rbHex.isSelected())    return DESAlgorithm.bytesToHex(bytes);
        if (rbBase64.isSelected()) return DESAlgorithm.bytesToBase64(bytes);

        // Văn bản
        String result = new String(bytes, StandardCharsets.UTF_8);
        if (result.indexOf('\uFFFD') >= 0) {
            showWarning("Kết quả giải mã chứa ký tự không hợp lệ với UTF-8.\n" +
                    "Hiển thị dưới dạng Hex.");
            return DESAlgorithm.bytesToHex(bytes);
        }
        return result;
    }

    // ============================================================
    // TẠO KHÓA TỰ ĐỘNG
    // ============================================================
    private String generateKey(JRadioButton rbAscii, JRadioButton rbHex) {
        if (rbAscii.isSelected()) return randomAsciiChars(8);
        return randomHexChars(16);
    }

    private String randomAsciiChars(int count) {
        // Phạm vi ASCII in được 0x20–0x7E
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
        return sb.toString();
    }

    private String randomHexChars(int count) {
        String chars = "0123456789ABCDEF";
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
        return sb.toString();
    }

    // ============================================================
    // SHA-256
    // ============================================================
    private String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return DESAlgorithm.bytesToHex(hash);
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi SHA-256: " + ex.getMessage());
        }
    }

    // ============================================================
    // TIỆN ÍCH FILE
    // ============================================================
    private String loadTextFile(String title) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Tệp văn bản (*.txt)", "txt"));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return null;
        try {
            return readRawFile(fc.getSelectedFile());
        } catch (IOException ex) {
            showError("Lỗi đọc tệp: " + ex.getMessage());
            return null;
        }
    }

    private String readRawFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            boolean first = true;
            while ((line = r.readLine()) != null) {
                if (!first) sb.append('\n');
                sb.append(line);
                first = false;
            }
        }
        return sb.toString();
    }


    private JFileChooser createFileChooser(String title, String defaultFile) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(title);
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Tệp văn bản (*.txt)", "txt"));
        if (defaultFile != null) fc.setSelectedFile(new File(defaultFile));
        return fc;
    }

    private File ensureTxtExtension(File file) {
        if (!file.getName().toLowerCase().endsWith(".txt"))
            return new File(file.getAbsolutePath() + ".txt");
        return file;
    }

    // ============================================================
    // BUILDER HELPERS – GIAO DIỆN
    // ============================================================
    private JPanel buildPanelHeader(String title, Color bg, Color fg) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        p.setBackground(bg);
        p.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(2, 2, 2, 2)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(fg);
        p.add(lbl);
        return p;
    }

    private JPanel buildRadioCard(String label, JRadioButton... radios) {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 6));
        panel.add(createLabel(label), BorderLayout.NORTH);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setBackground(COLOR_PANEL);
        for (JRadioButton rb : radios) row.add(rb);
        panel.add(row, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildKeyCard(String label,
                                JRadioButton rbAscii, JRadioButton rbHex,
                                JTextField tfKey) {
        JPanel panel = createCard();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets  = new Insets(0, 0, 6, 0);

        g.gridx = 0; g.gridy = 0;
        panel.add(createLabel(label), g);

        g.gridy = 1; g.insets = new Insets(0, 0, 8, 0);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        row.setBackground(COLOR_PANEL);
        row.add(rbAscii); row.add(rbHex);
        panel.add(row, g);

        g.gridy = 2; g.insets = new Insets(0, 0, 4, 0);
        panel.add(createLabel("Khóa"), g);

        g.gridy = 3; g.insets = new Insets(0, 0, 0, 0);
        panel.add(tfKey, g);
        return panel;
    }

    private JPanel buildTextAreaCard(String label, JTextArea ta) {
        JPanel panel = createCard();
        panel.setLayout(new BorderLayout(0, 6));
        panel.add(createLabel(label), BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBorder(null);
        sp.setPreferredSize(new Dimension(400, 110));
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildButtonRow(JButton... buttons) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        row.setBackground(COLOR_PANEL);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (JButton b : buttons) row.add(b);
        return row;
    }

    private JButton makeBtn(String text, Color color, ActionListener al) {
        JButton btn = createButton(text, color);
        btn.addActionListener(al);
        return btn;
    }

    private JPanel createCard() {
        JPanel p = new JPanel();
        p.setBackground(COLOR_PANEL);
        p.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return p;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(COLOR_PRIMARY);
        return lbl;
    }

    private JRadioButton createRadio(String text, boolean selected) {
        JRadioButton rb = new JRadioButton(text, selected);
        rb.setFont(FONT_NORMAL);
        rb.setForeground(COLOR_TEXT);
        rb.setBackground(COLOR_PANEL);
        rb.setFocusPainted(false);
        return rb;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_MONO);
        tf.setForeground(COLOR_TEXT);
        tf.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(5, 8, 5, 8)));
        tf.setToolTipText("ASCII: đúng 8 ký tự | Hex: đúng 16 ký tự 0-9, A-F");
        return tf;
    }

    private JTextArea createTextArea(int rows) {
        JTextArea ta = new JTextArea(rows, 30);
        ta.setFont(FONT_MONO);
        ta.setForeground(COLOR_TEXT);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 8, 6, 8)));
        return ta;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.darker()); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    // ============================================================
    // THÔNG BÁO
    // ============================================================
    private void showError  (String msg) { JOptionPane.showMessageDialog(this, msg, "Lỗi",    JOptionPane.ERROR_MESSAGE);       }
    private void showInfo   (String msg) { JOptionPane.showMessageDialog(this, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE); }
    private void showWarning(String msg) { JOptionPane.showMessageDialog(this, msg, "Cảnh báo",  JOptionPane.WARNING_MESSAGE);    }

    // ============================================================
    // ĐIỂM VÀO
    // ============================================================
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(MainUI::new);
    }
}