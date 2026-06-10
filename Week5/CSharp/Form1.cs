using DocumentFormat.OpenXml;
using DocumentFormat.OpenXml.Packaging;
using DocumentFormat.OpenXml.Wordprocessing;
using System;
using System.IO;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Windows.Forms;

namespace DES_123
{
    public partial class Form1 : Form
    {
        // Biến theo dõi thay đổi
        private string originalPlainText = "";
        private string originalKeyEncrypt = "";
        private string originalCipherText = "";
        private string originalKeyDecrypt = "";
        private bool isPlainTextModified = false;
        private bool isKeyEncryptModified = false;
        private bool isCipherTextModified = false;
        private bool isKeyDecryptModified = false;
        private bool isProcessingChange = false;

        public Form1()
        {
            InitializeComponent();
            InitializeCustomSettings();
            this.WindowState = FormWindowState.Maximized;
            this.Size = Screen.PrimaryScreen.Bounds.Size;
        }

        private void InitializeCustomSettings()
        {
            // Encrypt panel
            cboPlainFormat.SelectedIndex = 0;
            cboKeyTypeEncrypt.SelectedIndex = 0;
            cboCipherFormatEncrypt.SelectedIndex = 0;

            // Decrypt panel
            cboCipherFormatDecrypt.Items.Clear();
            cboCipherFormatDecrypt.Items.AddRange(new object[] { "Hex", "Base64" });
            cboCipherFormatDecrypt.SelectedIndex = 0; // Mặc định là Hex

            cboKeyTypeDecrypt.SelectedIndex = 0;
            cboPlainFormatDecrypt.SelectedIndex = 0;

            // Đăng ký sự kiện theo dõi thay đổi
            txtPlainText.TextChanged += TxtPlainText_TextChanged;
            txtKeyEncrypt.TextChanged += TxtKeyEncrypt_TextChanged;
            txtCipherTextDecrypt.TextChanged += TxtCipherTextDecrypt_TextChanged;
            txtKeyDecrypt.TextChanged += TxtKeyDecrypt_TextChanged;
        }

        // ==================== TẠO KHÓA ====================
        // Tạo khóa ASCII ngẫu nhiên (8 ký tự)
        private string GenerateRandomAsciiKey()
        {
            const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            byte[] randomBytes = new byte[8];
            using (var rng = RandomNumberGenerator.Create())
                rng.GetBytes(randomBytes);
            return new string(randomBytes.Select(b => chars[b % chars.Length]).ToArray());
        }

        // Tạo khóa Hex ngẫu nhiên (16 ký tự = 8 byte)
        private string GenerateRandomHexKey()
        {
            byte[] keyBytes = new byte[8];
            using (var rng = RandomNumberGenerator.Create())
                rng.GetBytes(keyBytes);
            return BitConverter.ToString(keyBytes).Replace("-", "");
        }

        // ==================== CHUYỂN ĐỔI DỮ LIỆU ====================
        private byte[] ConvertPlainTextToBytes(string text, string format)
        {
            switch (format)
            {
                case "Hex":
                    // Loại bỏ khoảng trắng, tab, xuống dòng
                    text = text.Replace(" ", "").Replace("\r", "").Replace("\n", "").Replace("\t", "");
                    if (text.Length % 2 != 0)
                    {
                        MessageBox.Show("Dữ liệu Hex có số ký tự lẻ! Không thể chuyển đổi.", "Lỗi");
                        return null;
                    }
                    return Enumerable.Range(0, text.Length / 2)
                        .Select(x => Convert.ToByte(text.Substring(x * 2, 2), 16))
                        .ToArray();
                case "Base64":
                    return Convert.FromBase64String(text);
                case "Văn bản":
                default:
                    return Encoding.UTF8.GetBytes(text);
            }
        }

        private byte[] ConvertCipherTextToBytes(string text, string format)
        {
            switch (format)
            {
                case "Hex":
                    text = text.Replace(" ", "").Replace("\r", "").Replace("\n", "").Replace("\t", "");
                    if (text.Length % 2 != 0)
                    {
                        MessageBox.Show("Dữ liệu Hex có số ký tự lẻ! Không thể chuyển đổi.", "Lỗi");
                        return null;
                    }
                    return Enumerable.Range(0, text.Length / 2)
                        .Select(x => Convert.ToByte(text.Substring(x * 2, 2), 16))
                        .ToArray();
                case "Base64":
                    return Convert.FromBase64String(text);
                default:
                    throw new ArgumentException($"Định dạng bản mã không hợp lệ: {format}. Chỉ hỗ trợ Hex hoặc Base64.");
            }
        }
        private string ConvertBytesToFormat(byte[] data, string format)
        {
            switch (format)
            {
                case "Hex":
                    return BitConverter.ToString(data).Replace("-", "");
                case "Base64":
                    return Convert.ToBase64String(data);
                case "Văn bản":
                default:
                    return Encoding.UTF8.GetString(data);
            }
        }

        private byte[] ConvertKeyToBytes(string key, string keyType)
        {
            if (keyType == "Hex")
                return Enumerable.Range(0, key.Length / 2)
                    .Select(x => Convert.ToByte(key.Substring(x * 2, 2), 16))
                    .ToArray();
            else
                return Encoding.ASCII.GetBytes(key);
        }

        // ==================== MÃ HÓA DES ====================
        private string EncryptDES(string plainText, string key, string keyType, string outputFormat, string inputFormat)
        {
            try
            {
                byte[] plainBytes = ConvertPlainTextToBytes(plainText, inputFormat);
                byte[] keyBytes = ConvertKeyToBytes(key, keyType);

                if (keyBytes.Length != 8)
                {
                    MessageBox.Show("Khóa DES phải có độ dài 8 byte!", "Lỗi khóa", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return null;
                }

                using (var des = System.Security.Cryptography.DES.Create())
                {
                    des.Mode = CipherMode.ECB;
                    des.Padding = PaddingMode.None;  // ← QUAN TRỌNG: TẮT PADDING
                    des.Key = keyBytes;

                    // Nếu dữ liệu chưa đủ 8 byte, tự thêm padding zeros
                    if (plainBytes.Length % 8 != 0)
                    {
                        int newLen = ((plainBytes.Length / 8) + 1) * 8;
                        byte[] padded = new byte[newLen];
                        Array.Copy(plainBytes, padded, plainBytes.Length);
                        plainBytes = padded;
                    }

                    using (var ms = new MemoryStream())
                    using (var cs = new CryptoStream(ms, des.CreateEncryptor(), CryptoStreamMode.Write))
                    {
                        cs.Write(plainBytes, 0, plainBytes.Length);
                        cs.FlushFinalBlock();
                        return ConvertBytesToFormat(ms.ToArray(), outputFormat);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Lỗi mã hóa: {ex.Message}", "Lỗi", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        // ==================== GIẢI MÃ DES ====================
        private string DecryptDES(string cipherText, string key, string keyType, string inputFormat, string outputFormat)
        {
            try
            {
                byte[] cipherBytes = ConvertCipherTextToBytes(cipherText, inputFormat);
                byte[] keyBytes = ConvertKeyToBytes(key, keyType);

                if (keyBytes.Length != 8)
                {
                    MessageBox.Show("Khóa DES phải có độ dài 8 byte!", "Lỗi khóa", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return null;
                }

                using (var des = System.Security.Cryptography.DES.Create())
                {
                    des.Mode = CipherMode.ECB;
                    des.Padding = PaddingMode.None;  // ← TẮT PADDING
                    des.Key = keyBytes;

                    using (var ms = new MemoryStream(cipherBytes))
                    using (var cs = new CryptoStream(ms, des.CreateDecryptor(), CryptoStreamMode.Read))
                    {
                        byte[] decryptedBytes;
                        using (var msOutput = new MemoryStream())
                        {
                            cs.CopyTo(msOutput);
                            decryptedBytes = msOutput.ToArray();
                        }

                        // Loại bỏ zeros padding (nếu có)
                        int end = decryptedBytes.Length;
                        while (end > 0 && decryptedBytes[end - 1] == 0) end--;
                        if (end < decryptedBytes.Length)
                        {
                            Array.Resize(ref decryptedBytes, end);
                        }

                        return ConvertBytesToFormat(decryptedBytes, outputFormat);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Lỗi giải mã: {ex.Message}", "Lỗi", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        // ==================== HỖ TRỢ FILE WORD ====================
        private string ReadWordFile(string filePath)
        {
            try
            {
                using (WordprocessingDocument wordDoc = WordprocessingDocument.Open(filePath, false))
                {
                    StringBuilder text = new StringBuilder();
                    Body body = wordDoc.MainDocumentPart.Document.Body;

                    if (body == null) return "";

                    foreach (Paragraph para in body.Elements<Paragraph>())
                    {
                        foreach (Run run in para.Elements<Run>())
                        {
                            foreach (Text t in run.Elements<Text>())
                            {
                                text.Append(t.Text);
                            }
                        }
                        text.AppendLine();
                    }
                    return text.ToString();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Lỗi đọc file Word: {ex.Message}", "Lỗi", MessageBoxButtons.OK, MessageBoxIcon.Error);
                return null;
            }
        }

        private void SaveToWordFile(string content, string filePath)
        {
            try
            {
                using (WordprocessingDocument wordDoc = WordprocessingDocument.Create(filePath, WordprocessingDocumentType.Document))
                {
                    MainDocumentPart mainPart = wordDoc.AddMainDocumentPart();
                    mainPart.Document = new Document();
                    Body body = new Body();
                    Paragraph para = new Paragraph();
                    Run run = new Run();
                    Text text = new Text(content);
                    run.Append(text);
                    para.Append(run);
                    body.Append(para);
                    mainPart.Document.Append(body);
                }
                MessageBox.Show("✅ Đã lưu file Word thành công!", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Lỗi lưu file Word: {ex.Message}", "Lỗi", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // ==================== KIỂM TRA ĐỊNH DẠNG ====================
        private bool IsValidHex(string text)
        {
            if (string.IsNullOrWhiteSpace(text)) return false;
            if (text.Length % 2 != 0) return false;
            foreach (char c in text)
            {
                if (!char.IsDigit(c) && !(c >= 'A' && c <= 'F') && !(c >= 'a' && c <= 'f'))
                    return false;
            }
            return true;
        }

        private bool IsValidBase64(string text)
        {
            if (string.IsNullOrWhiteSpace(text)) return false;
            try
            {
                Convert.FromBase64String(text);
                return true;
            }
            catch
            {
                return false;
            }
        }

        // ==================== KIỂM TRA VÀ XỬ LÝ KHÓA ====================
        private bool ValidateAndFixKey(ref string key, string keyType, string context)
        {
            if (string.IsNullOrWhiteSpace(key))
            {
                DialogResult result = MessageBox.Show(
                    $"❌ Khóa {context} không được để trống!\n\nBạn có muốn tạo khóa tự động không?",
                    "Lỗi khóa",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Warning);

                if (result == DialogResult.Yes)
                {
                    if (keyType == "Hex")
                        key = GenerateRandomHexKey();
                    else
                        key = GenerateRandomAsciiKey();
                    return true;
                }
                return false;
            }

            int requiredBytes = 8;
            int requiredChars;

            if (keyType == "Hex")
            {
                requiredChars = 16;

                // Loại bỏ khoảng trắng và viết hoa
                key = key.Replace(" ", "").Replace("\r", "").Replace("\n", "").ToUpper();

                // Kiểm tra ký tự hợp lệ
                if (key.Any(c => !char.IsDigit(c) && (c < 'A' || c > 'F')))
                {
                    MessageBox.Show(
                        "❌ Khóa Hex chỉ được chứa ký tự 0-9, A-F!",
                        "Lỗi định dạng",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Warning);
                    return false;
                }

                // So sánh key.Length (số ký tự) thay vì keyBytes.Length
                if (key.Length < requiredChars)
                {
                    int missingChars = requiredChars - key.Length;

                    DialogResult result = MessageBox.Show(
                        $"⚠ Khóa {context} hiện tại có {key.Length} ký tự.\n" +
                        $"Yêu cầu: {requiredChars} ký tự Hex ({requiredBytes} byte).\n" +
                        $"Còn thiếu {missingChars} ký tự.\n\n" +
                        $"Bạn muốn:\n" +
                        $"• Nhấn YES: Tự động thêm ký tự NGẪU NHIÊN (0-9, A-F) vào cuối\n" +
                        $"• Nhấn NO: Tự nhập thủ công",
                        "Khóa Hex chưa đủ độ dài",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        string hexChars = "0123456789ABCDEF";
                        byte[] randBuf = new byte[missingChars];
                        using (var rng = RandomNumberGenerator.Create())
                            rng.GetBytes(randBuf);
                        foreach (byte b in randBuf)
                            key += hexChars[b % 16];

                        MessageBox.Show(
                            $"✅ Đã tự động thêm {missingChars} ký tự NGẪU NHIÊN vào cuối.\nKhóa mới: {key}",
                            "Thông báo",
                            MessageBoxButtons.OK,
                            MessageBoxIcon.Information);
                        return true;
                    }
                    return false;
                }

                // Nếu dư ký tự, cắt bớt
                if (key.Length > requiredChars)
                {
                    key = key.Substring(0, requiredChars);
                    MessageBox.Show(
                        $"⚠ Khóa Hex quá dài, đã tự động cắt còn {requiredChars} ký tự.\nKhóa mới: {key}",
                        "Thông báo",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Warning);
                }
                return true;
            }
            else // ASCII
            {
                requiredChars = 8;

                // Kiểm tra ký tự non-ASCII
                if (key.Any(c => c > 127))
                {
                    MessageBox.Show(
                        "❌ Khóa ASCII chỉ được chứa ký tự ASCII (mã 0–127).\n\nVui lòng không dùng ký tự tiếng Việt có dấu hoặc ký tự đặc biệt ngoài bảng ASCII.",
                        "Lỗi định dạng",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Warning);
                    return false;
                }

                byte[] keyBytes = Encoding.ASCII.GetBytes(key);

                if (keyBytes.Length == requiredBytes)
                {
                    return true;
                }
                else if (keyBytes.Length < requiredBytes)
                {
                    int missingBytes = requiredBytes - keyBytes.Length;
                    int missingChars = missingBytes;

                    DialogResult result = MessageBox.Show(
                        $"⚠ Khóa {context} hiện tại có {keyBytes.Length} byte / {key.Length} ký tự.\n" +
                        $"Yêu cầu: {requiredBytes} byte / {requiredChars} ký tự.\n" +
                        $"Còn thiếu {missingBytes} byte ({missingChars} ký tự).\n\n" +
                        $"Bạn muốn:\n" +
                        $"• Nhấn YES: Tự động thêm ký tự NGẪU NHIÊN vào cuối\n" +
                        $"• Nhấn NO: Tự nhập thủ công",
                        "Khóa ASCII chưa đủ độ dài",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        byte[] randBuf = new byte[missingChars];
                        using (var rng = RandomNumberGenerator.Create())
                            rng.GetBytes(randBuf);

                        string asciiChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                        foreach (byte b in randBuf)
                            key += asciiChars[b % asciiChars.Length];
                        if (key.Length > requiredChars) key = key.Substring(0, requiredChars);

                        MessageBox.Show(
                            $"✅ Đã tự động thêm {missingChars} ký tự NGẪU NHIÊN vào cuối.\nKhóa mới: {key}",
                            "Thông báo",
                            MessageBoxButtons.OK,
                            MessageBoxIcon.Information);
                        return true;
                    }
                    return false;
                }
                else
                {
                    key = key.Substring(0, requiredChars);
                    MessageBox.Show(
                        $"⚠ Khóa ASCII quá dài, đã tự động cắt bớt còn {requiredChars} ký tự ({requiredBytes} byte).\nKhóa mới: {key}",
                        "Thông báo",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Warning);
                    return true;
                }
            }
        }

        // ==================== THEO DÕI THAY ĐỔI ====================
        private void TxtPlainText_TextChanged(object sender, EventArgs e)
        {
            if (isProcessingChange) return;
            if (!string.IsNullOrEmpty(originalPlainText) && !isPlainTextModified)
            {
                if (txtPlainText.Text != originalPlainText)
                {
                    DialogResult result = MessageBox.Show(
                        "⚠ Bản rõ đã bị thay đổi so với file gốc!\n\n" +
                        "Bạn muốn:\n" +
                        "• Nhấn YES: Tiếp tục thay đổi (cập nhật nội dung mới)\n" +
                        "• Nhấn NO: Giữ nguyên nội dung cũ",
                        "Xác nhận thay đổi",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        originalPlainText = txtPlainText.Text;
                        isPlainTextModified = true;
                    }
                    else
                    {
                        isProcessingChange = true;
                        txtPlainText.Text = originalPlainText;
                        isProcessingChange = false;
                        isPlainTextModified = false;
                    }
                }
            }
        }

        private void TxtKeyEncrypt_TextChanged(object sender, EventArgs e)
        {
            if (isProcessingChange) return;
            if (!string.IsNullOrEmpty(originalKeyEncrypt) && !isKeyEncryptModified)
            {
                if (txtKeyEncrypt.Text != originalKeyEncrypt)
                {
                    DialogResult result = MessageBox.Show(
                        "⚠ Khóa mã hóa đã bị thay đổi so với file gốc!\n\n" +
                        "Bạn muốn:\n" +
                        "• Nhấn YES: Tiếp tục thay đổi (cập nhật khóa mới)\n" +
                        "• Nhấn NO: Giữ nguyên khóa cũ",
                        "Xác nhận thay đổi",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        originalKeyEncrypt = txtKeyEncrypt.Text;
                        isKeyEncryptModified = true;
                    }
                    else
                    {
                        isProcessingChange = true;
                        txtKeyEncrypt.Text = originalKeyEncrypt;
                        isProcessingChange = false;
                        isKeyEncryptModified = false;
                    }
                }
            }
        }

        private void TxtCipherTextDecrypt_TextChanged(object sender, EventArgs e)
        {
            if (isProcessingChange) return;
            if (!string.IsNullOrEmpty(originalCipherText) && !isCipherTextModified)
            {
                if (txtCipherTextDecrypt.Text != originalCipherText)
                {
                    DialogResult result = MessageBox.Show(
                        "⚠ Bản mã đã bị thay đổi so với file gốc!\n\n" +
                        "Bạn muốn:\n" +
                        "• Nhấn YES: Tiếp tục thay đổi (cập nhật nội dung mới)\n" +
                        "• Nhấn NO: Giữ nguyên nội dung cũ",
                        "Xác nhận thay đổi",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        originalCipherText = txtCipherTextDecrypt.Text;
                        isCipherTextModified = true;
                    }
                    else
                    {
                        isProcessingChange = true;
                        txtCipherTextDecrypt.Text = originalCipherText;
                        isProcessingChange = false;
                        isCipherTextModified = false;
                    }
                }
            }
        }

        private void TxtKeyDecrypt_TextChanged(object sender, EventArgs e)
        {
            if (isProcessingChange) return;
            if (!string.IsNullOrEmpty(originalKeyDecrypt) && !isKeyDecryptModified)
            {
                if (txtKeyDecrypt.Text != originalKeyDecrypt)
                {
                    DialogResult result = MessageBox.Show(
                        "⚠ Khóa giải mã đã bị thay đổi so với file gốc!\n\n" +
                        "Bạn muốn:\n" +
                        "• Nhấn YES: Tiếp tục thay đổi (cập nhật khóa mới)\n" +
                        "• Nhấn NO: Giữ nguyên khóa cũ",
                        "Xác nhận thay đổi",
                        MessageBoxButtons.YesNo,
                        MessageBoxIcon.Question);

                    if (result == DialogResult.Yes)
                    {
                        originalKeyDecrypt = txtKeyDecrypt.Text;
                        isKeyDecryptModified = true;
                    }
                    else
                    {
                        isProcessingChange = true;
                        txtKeyDecrypt.Text = originalKeyDecrypt;
                        isProcessingChange = false;
                        isKeyDecryptModified = false;
                    }
                }
            }
        }

        // ==================== SỰ KIỆN MÃ HÓA ====================
        private void btnEncrypt_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtPlainText.Text))
            {
                MessageBox.Show("Vui lòng nhập bản rõ!");
                return;
            }

            // Loại bỏ khoảng trắng thừa
            string plainText = txtPlainText.Text.Trim();

            string key = txtKeyEncrypt.Text.Trim();
            string keyType = cboKeyTypeEncrypt.SelectedItem.ToString();

            if (!ValidateAndFixKey(ref key, keyType, "mã hóa"))
                return;

            if (txtKeyEncrypt.Text != key)
            {
                isProcessingChange = true;
                txtKeyEncrypt.Text = key;
                isProcessingChange = false;
                originalKeyEncrypt = key;
            }

            string plainFormat = cboPlainFormat.SelectedItem.ToString();

            // DEBUG: Kiểm tra độ dài dữ liệu
            byte[] testBytes = ConvertPlainTextToBytes(plainText, plainFormat);
            MessageBox.Show($"Dữ liệu đầu vào: {plainText}\nĐộ dài byte: {testBytes.Length}", "Debug");

            if (plainFormat == "Hex")
            {
                if (!IsValidHex(plainText))
                {
                    MessageBox.Show("❌ Bản rõ không đúng định dạng Hex!");
                    return;
                }
            }
            else if (plainFormat == "Base64")
            {
                if (!IsValidBase64(plainText))
                {
                    MessageBox.Show("❌ Bản rõ không đúng định dạng Base64!");
                    return;
                }
            }

            string result = EncryptDES(
                plainText,
                key,
                keyType,
                cboCipherFormatEncrypt.SelectedItem.ToString(),
                plainFormat);

            if (result != null)
            {
                txtCipherText.Text = result;
                MessageBox.Show("✅ Mã hóa thành công!");
            }
        }

        // ==================== SỰ KIỆN GIẢI MÃ ====================
        private void btnDecrypt_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtCipherTextDecrypt.Text))
            {
                MessageBox.Show("Vui lòng nhập bản mã!", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                return;
            }

            string key = txtKeyDecrypt.Text;
            string keyType = cboKeyTypeDecrypt.SelectedItem.ToString();

            if (!ValidateAndFixKey(ref key, keyType, "giải mã"))
                return;

            if (txtKeyDecrypt.Text != key)
            {
                isProcessingChange = true;
                txtKeyDecrypt.Text = key;
                isProcessingChange = false;
                originalKeyDecrypt = key;
            }

            string cipherFormat = cboCipherFormatDecrypt.SelectedItem.ToString();

            string cipherInput = txtCipherTextDecrypt.Text;
            if (cipherFormat == "Hex")
            {
                cipherInput = cipherInput.Replace(" ", "").Replace("\r", "").Replace("\n", "").Replace("\t", "");
                if (!IsValidHex(cipherInput))
                {
                    MessageBox.Show(
                        "❌ Bản mã không đúng định dạng Hex!\n\n" +
                        "Yêu cầu:\n" +
                        "• Chỉ chứa ký tự 0-9, A-F, a-f\n" +
                        "• Số lượng ký tự phải là số chẵn\n" +
                        "• Không chứa ký tự lạ hoặc khoảng trắng",
                        "Lỗi định dạng", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }
            }
            else if (cipherFormat == "Base64")
            {
                cipherInput = cipherInput.Trim();
                if (!IsValidBase64(cipherInput))
                {
                    MessageBox.Show("❌ Bản mã không đúng định dạng Base64!", "Lỗi định dạng", MessageBoxButtons.OK, MessageBoxIcon.Warning);
                    return;
                }
            }

            string result = DecryptDES(
                cipherInput,
                key,
                keyType,
                cipherFormat,
                cboPlainFormatDecrypt.SelectedItem.ToString());

            if (result != null)
            {
                txtPlainTextDecrypt.Text = result;
                MessageBox.Show("✅ Giải mã thành công!", "Thông báo", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                MessageBox.Show("❌ Giải mã thất bại!", "Lỗi", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        // ==================== XÓA ====================
        private void btnClearEncrypt_Click(object sender, EventArgs e)
        {
            txtPlainText.Clear();
            txtKeyEncrypt.Clear();
            txtCipherText.Clear();
            originalPlainText = "";
            originalKeyEncrypt = "";
            isPlainTextModified = false;
            isKeyEncryptModified = false;
        }

        private void btnClearDecrypt_Click(object sender, EventArgs e)
        {
            txtCipherTextDecrypt.Clear();
            txtKeyDecrypt.Clear();
            txtPlainTextDecrypt.Clear();
            originalCipherText = "";
            originalKeyDecrypt = "";
            isCipherTextModified = false;
            isKeyDecryptModified = false;
        }

        // ==================== TẠO KHÓA TỰ ĐỘNG ====================
        private void btnGenerateKeyEncrypt_Click(object sender, EventArgs e)
        {
            string keyType = cboKeyTypeEncrypt.SelectedItem.ToString();
            if (keyType == "Hex")
                txtKeyEncrypt.Text = GenerateRandomHexKey();
            else
                txtKeyEncrypt.Text = GenerateRandomAsciiKey();
        }

        private void btnGenerateKeyDecrypt_Click(object sender, EventArgs e)
        {
            string keyType = cboKeyTypeDecrypt.SelectedItem.ToString();
            if (keyType == "Hex")
                txtKeyDecrypt.Text = GenerateRandomHexKey();
            else
                txtKeyDecrypt.Text = GenerateRandomAsciiKey();
        }

        // ==================== SAO CHÉP ====================
        private void btnCopyPlain_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtPlainText.Text))
                Clipboard.SetText(txtPlainText.Text);
        }

        private void btnCopyKeyEncrypt_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtKeyEncrypt.Text))
                Clipboard.SetText(txtKeyEncrypt.Text);
        }

        private void btnCopyCipher_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtCipherText.Text))
                Clipboard.SetText(txtCipherText.Text);
        }

        private void btnCopyCipherDecrypt_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtCipherTextDecrypt.Text))
                Clipboard.SetText(txtCipherTextDecrypt.Text);
        }

        private void btnCopyKeyDecrypt_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtKeyDecrypt.Text))
                Clipboard.SetText(txtKeyDecrypt.Text);
        }

        private void btnCopyPlainDecrypt_Click(object sender, EventArgs e)
        {
            if (!string.IsNullOrWhiteSpace(txtPlainTextDecrypt.Text))
                Clipboard.SetText(txtPlainTextDecrypt.Text);
        }

        // ==================== TẢI/LƯU FILE ====================
        private void btnLoadPlain_Click(object sender, EventArgs e)
        {
            using (var ofd = new OpenFileDialog())
            {
                ofd.Filter = "All Files|*.*";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string filePath = ofd.FileName;
                    string ext = Path.GetExtension(filePath).ToLower();

                    if (ext == ".docx" || ext == ".doc")
                    {
                        string content = ReadWordFile(filePath);
                        if (content != null)
                        {
                            txtPlainText.Text = content;
                            originalPlainText = content;
                            isPlainTextModified = false;
                            MessageBox.Show("Đã tải file Word thành công!", "Thành công");
                        }
                    }
                    else
                    {
                        string content = File.ReadAllText(filePath, Encoding.UTF8);
                        txtPlainText.Text = content;
                        originalPlainText = content;
                        isPlainTextModified = false;
                        MessageBox.Show("Đã tải file Text thành công!", "Thành công");
                    }
                }
            }
        }

        private void btnSavePlain_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Text Files|*.txt|Word Files|*.docx";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    string ext = Path.GetExtension(sfd.FileName).ToLower();
                    if (ext == ".docx")
                        SaveToWordFile(txtPlainText.Text, sfd.FileName);
                    else
                    {
                        File.WriteAllText(sfd.FileName, txtPlainText.Text, Encoding.UTF8);
                        MessageBox.Show("✅ Đã lưu file!", "Thông báo");
                    }
                    originalPlainText = txtPlainText.Text;
                    isPlainTextModified = false;
                }
            }
        }

        private void btnLoadKeyEncrypt_Click(object sender, EventArgs e)
        {
            using (var ofd = new OpenFileDialog())
            {
                ofd.Filter = "All Files|*.*";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string content = File.ReadAllText(ofd.FileName, Encoding.UTF8).Trim();
                    txtKeyEncrypt.Text = content;
                    originalKeyEncrypt = content;
                    isKeyEncryptModified = false;
                }
            }
        }

        private void btnSaveKeyEncrypt_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Key Files|*.key";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(sfd.FileName, txtKeyEncrypt.Text, Encoding.UTF8);
                    MessageBox.Show("✅ Đã lưu khóa!", "Thông báo");
                    originalKeyEncrypt = txtKeyEncrypt.Text;
                    isKeyEncryptModified = false;
                }
            }
        }

        private void btnSaveCipher_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Text Files|*.txt|Encrypted Files|*.enc";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(sfd.FileName, txtCipherText.Text, Encoding.UTF8);
                    MessageBox.Show("✅ Đã lưu bản mã!", "Thông báo");
                }
            }
        }

        private void btnLoadCipherDecrypt_Click(object sender, EventArgs e)
        {
            using (var ofd = new OpenFileDialog())
            {
                ofd.Filter = "All Files|*.*";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string filePath = ofd.FileName;
                    string ext = Path.GetExtension(filePath).ToLower();

                    if (ext == ".docx" || ext == ".doc")
                    {
                        string content = ReadWordFile(filePath);
                        if (content != null)
                        {
                            txtCipherTextDecrypt.Text = content;
                            originalCipherText = content;
                            isCipherTextModified = false;
                        }
                    }
                    else
                    {
                        string content = File.ReadAllText(filePath, Encoding.UTF8).Trim();
                        txtCipherTextDecrypt.Text = content;
                        originalCipherText = content;
                        isCipherTextModified = false;
                    }
                }
            }
        }

        private void btnLoadKeyDecrypt_Click(object sender, EventArgs e)
        {
            using (var ofd = new OpenFileDialog())
            {
                ofd.Filter = "All Files|*.*";
                if (ofd.ShowDialog() == DialogResult.OK)
                {
                    string content = File.ReadAllText(ofd.FileName, Encoding.UTF8).Trim();
                    txtKeyDecrypt.Text = content;
                    originalKeyDecrypt = content;
                    isKeyDecryptModified = false;
                }
            }
        }

        private void btnSaveKeyDecrypt_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Key Files|*.key";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(sfd.FileName, txtKeyDecrypt.Text, Encoding.UTF8);
                    MessageBox.Show("✅ Đã lưu khóa!", "Thông báo");
                    originalKeyDecrypt = txtKeyDecrypt.Text;
                    isKeyDecryptModified = false;
                }
            }
        }

        private void btnSaveCipherDecrypt_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Text Files|*.txt|Encrypted Files|*.enc";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(sfd.FileName, txtCipherTextDecrypt.Text, Encoding.UTF8);
                    MessageBox.Show("✅ Đã lưu bản mã!", "Thông báo");
                }
            }
        }

        private void btnSavePlainDecrypt_Click(object sender, EventArgs e)
        {
            using (var sfd = new SaveFileDialog())
            {
                sfd.Filter = "Text Files|*.txt";
                if (sfd.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(sfd.FileName, txtPlainTextDecrypt.Text, Encoding.UTF8);
                    MessageBox.Show("✅ Đã lưu bản rõ!", "Thông báo");
                }
            }
        }

        private void decryptPanel_Paint(object sender, PaintEventArgs e)
        {
            // Không cần code
        }
    }
}