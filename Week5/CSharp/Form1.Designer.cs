namespace DES_123
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
                components.Dispose();
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        private void InitializeComponent()
        {
            mainPanel = new TableLayoutPanel();
            encryptPanel = new Panel();
            lblPlainFormat = new Label();
            cboPlainFormat = new ComboBox();
            lblPlainText = new Label();
            txtPlainText = new RichTextBox();
            lblKeyTypeEncrypt = new Label();
            cboKeyTypeEncrypt = new ComboBox();
            lblKeyEncrypt = new Label();
            txtKeyEncrypt = new TextBox();
            lblCipherFormatEncrypt = new Label();
            cboCipherFormatEncrypt = new ComboBox();
            lblCipherText = new Label();
            txtCipherText = new RichTextBox();
            btnEncrypt = new Button();
            btnClearEncrypt = new Button();
            btnGenerateKeyEncrypt = new Button();
            btnCopyPlain = new Button();
            btnCopyKeyEncrypt = new Button();
            btnCopyCipher = new Button();
            btnLoadPlain = new Button();
            btnLoadKeyEncrypt = new Button();
            btnSavePlain = new Button();
            btnSaveKeyEncrypt = new Button();
            btnSaveCipher = new Button();
            decryptPanel = new Panel();
            lblCipherFormatDecrypt = new Label();
            cboCipherFormatDecrypt = new ComboBox();
            lblCipherTextDecrypt = new Label();
            txtCipherTextDecrypt = new RichTextBox();
            lblKeyTypeDecrypt = new Label();
            cboKeyTypeDecrypt = new ComboBox();
            lblKeyDecrypt = new Label();
            txtKeyDecrypt = new TextBox();
            lblPlainFormatDecrypt = new Label();
            cboPlainFormatDecrypt = new ComboBox();
            lblPlainTextDecrypt = new Label();
            txtPlainTextDecrypt = new RichTextBox();
            btnDecrypt = new Button();
            btnClearDecrypt = new Button();
            btnGenerateKeyDecrypt = new Button();
            btnCopyCipherDecrypt = new Button();
            btnCopyKeyDecrypt = new Button();
            btnCopyPlainDecrypt = new Button();
            btnLoadCipherDecrypt = new Button();
            btnLoadKeyDecrypt = new Button();
            btnSaveCipherDecrypt = new Button();
            btnSaveKeyDecrypt = new Button();
            btnSavePlainDecrypt = new Button();
            mainPanel.SuspendLayout();
            encryptPanel.SuspendLayout();
            decryptPanel.SuspendLayout();
            SuspendLayout();
            // 
            // mainPanel
            // 
            mainPanel.ColumnCount = 2;
            mainPanel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 41.995842F));
            mainPanel.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 58.004158F));
            mainPanel.Controls.Add(encryptPanel, 0, 0);
            mainPanel.Controls.Add(decryptPanel, 1, 0);
            mainPanel.Dock = DockStyle.Fill;
            mainPanel.Location = new Point(0, 0);
            mainPanel.Margin = new Padding(3, 4, 3, 4);
            mainPanel.Name = "mainPanel";
            mainPanel.RowCount = 1;
            mainPanel.RowStyles.Add(new RowStyle(SizeType.Percent, 100F));
            mainPanel.Size = new Size(1924, 713);
            mainPanel.TabIndex = 0;
            // 
            // encryptPanel
            // 
            encryptPanel.BackColor = Color.FromArgb(230, 245, 255);
            encryptPanel.Controls.Add(lblPlainFormat);
            encryptPanel.Controls.Add(cboPlainFormat);
            encryptPanel.Controls.Add(lblPlainText);
            encryptPanel.Controls.Add(txtPlainText);
            encryptPanel.Controls.Add(lblKeyTypeEncrypt);
            encryptPanel.Controls.Add(cboKeyTypeEncrypt);
            encryptPanel.Controls.Add(lblKeyEncrypt);
            encryptPanel.Controls.Add(txtKeyEncrypt);
            encryptPanel.Controls.Add(lblCipherFormatEncrypt);
            encryptPanel.Controls.Add(cboCipherFormatEncrypt);
            encryptPanel.Controls.Add(lblCipherText);
            encryptPanel.Controls.Add(txtCipherText);
            encryptPanel.Controls.Add(btnEncrypt);
            encryptPanel.Controls.Add(btnClearEncrypt);
            encryptPanel.Controls.Add(btnGenerateKeyEncrypt);
            encryptPanel.Controls.Add(btnCopyPlain);
            encryptPanel.Controls.Add(btnCopyKeyEncrypt);
            encryptPanel.Controls.Add(btnCopyCipher);
            encryptPanel.Controls.Add(btnLoadPlain);
            encryptPanel.Controls.Add(btnLoadKeyEncrypt);
            encryptPanel.Controls.Add(btnSavePlain);
            encryptPanel.Controls.Add(btnSaveKeyEncrypt);
            encryptPanel.Controls.Add(btnSaveCipher);
            encryptPanel.Dock = DockStyle.Fill;
            encryptPanel.Location = new Point(3, 4);
            encryptPanel.Margin = new Padding(3, 4, 3, 4);
            encryptPanel.Name = "encryptPanel";
            encryptPanel.Padding = new Padding(11, 13, 11, 13);
            encryptPanel.Size = new Size(802, 705);
            encryptPanel.TabIndex = 0;
            // 
            // lblPlainFormat
            // 
            lblPlainFormat.Location = new Point(11, 13);
            lblPlainFormat.Name = "lblPlainFormat";
            lblPlainFormat.Size = new Size(137, 33);
            lblPlainFormat.TabIndex = 0;
            lblPlainFormat.Text = "Định dạng bản rõ:";
            // 
            // cboPlainFormat
            // 
            cboPlainFormat.DropDownStyle = ComboBoxStyle.DropDownList;
            cboPlainFormat.Items.AddRange(new object[] { "Văn bản", "Hex", "Base64" });
            cboPlainFormat.Location = new Point(160, 13);
            cboPlainFormat.Margin = new Padding(3, 4, 3, 4);
            cboPlainFormat.Name = "cboPlainFormat";
            cboPlainFormat.Size = new Size(137, 28);
            cboPlainFormat.TabIndex = 0;
            // 
            // lblPlainText
            // 
            lblPlainText.Location = new Point(11, 60);
            lblPlainText.Name = "lblPlainText";
            lblPlainText.Size = new Size(91, 33);
            lblPlainText.TabIndex = 1;
            lblPlainText.Text = "Bản rõ:";
            // 
            // txtPlainText
            // 
            txtPlainText.Location = new Point(11, 93);
            txtPlainText.Margin = new Padding(3, 4, 3, 4);
            txtPlainText.Name = "txtPlainText";
            txtPlainText.Size = new Size(525, 105);
            txtPlainText.TabIndex = 1;
            txtPlainText.Text = "";
            // 
            // lblKeyTypeEncrypt
            // 
            lblKeyTypeEncrypt.Location = new Point(11, 213);
            lblKeyTypeEncrypt.Name = "lblKeyTypeEncrypt";
            lblKeyTypeEncrypt.Size = new Size(91, 33);
            lblKeyTypeEncrypt.TabIndex = 2;
            lblKeyTypeEncrypt.Text = "Loại khóa:";
            // 
            // cboKeyTypeEncrypt
            // 
            cboKeyTypeEncrypt.DropDownStyle = ComboBoxStyle.DropDownList;
            cboKeyTypeEncrypt.Items.AddRange(new object[] { "ASCII", "Hex" });
            cboKeyTypeEncrypt.Location = new Point(114, 213);
            cboKeyTypeEncrypt.Margin = new Padding(3, 4, 3, 4);
            cboKeyTypeEncrypt.Name = "cboKeyTypeEncrypt";
            cboKeyTypeEncrypt.Size = new Size(114, 28);
            cboKeyTypeEncrypt.TabIndex = 2;
            // 
            // lblKeyEncrypt
            // 
            lblKeyEncrypt.Location = new Point(11, 260);
            lblKeyEncrypt.Name = "lblKeyEncrypt";
            lblKeyEncrypt.Size = new Size(57, 33);
            lblKeyEncrypt.TabIndex = 3;
            lblKeyEncrypt.Text = "Khóa:";
            // 
            // txtKeyEncrypt
            // 
            txtKeyEncrypt.Location = new Point(69, 260);
            txtKeyEncrypt.Margin = new Padding(3, 4, 3, 4);
            txtKeyEncrypt.Name = "txtKeyEncrypt";
            txtKeyEncrypt.Size = new Size(342, 27);
            txtKeyEncrypt.TabIndex = 3;
            // 
            // lblCipherFormatEncrypt
            // 
            lblCipherFormatEncrypt.Location = new Point(11, 307);
            lblCipherFormatEncrypt.Name = "lblCipherFormatEncrypt";
            lblCipherFormatEncrypt.Size = new Size(137, 33);
            lblCipherFormatEncrypt.TabIndex = 4;
            lblCipherFormatEncrypt.Text = "Định dạng bản mã:";
            // 
            // cboCipherFormatEncrypt
            // 
            cboCipherFormatEncrypt.DropDownStyle = ComboBoxStyle.DropDownList;
            cboCipherFormatEncrypt.Items.AddRange(new object[] { "Văn bản", "Hex", "Base64" });
            cboCipherFormatEncrypt.Location = new Point(160, 307);
            cboCipherFormatEncrypt.Margin = new Padding(3, 4, 3, 4);
            cboCipherFormatEncrypt.Name = "cboCipherFormatEncrypt";
            cboCipherFormatEncrypt.Size = new Size(137, 28);
            cboCipherFormatEncrypt.TabIndex = 4;
            // 
            // lblCipherText
            // 
            lblCipherText.Location = new Point(11, 353);
            lblCipherText.Name = "lblCipherText";
            lblCipherText.Size = new Size(91, 33);
            lblCipherText.TabIndex = 5;
            lblCipherText.Text = "Bản mã:";
            // 
            // txtCipherText
            // 
            txtCipherText.Location = new Point(11, 387);
            txtCipherText.Margin = new Padding(3, 4, 3, 4);
            txtCipherText.Name = "txtCipherText";
            txtCipherText.ReadOnly = true;
            txtCipherText.Size = new Size(525, 105);
            txtCipherText.TabIndex = 5;
            txtCipherText.Text = "";
            // 
            // btnEncrypt
            // 
            btnEncrypt.BackColor = Color.LightGreen;
            btnEncrypt.Location = new Point(11, 507);
            btnEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnEncrypt.Name = "btnEncrypt";
            btnEncrypt.Size = new Size(91, 47);
            btnEncrypt.TabIndex = 6;
            btnEncrypt.Text = "Mã hóa";
            btnEncrypt.UseVisualStyleBackColor = false;
            btnEncrypt.Click += btnEncrypt_Click;
            // 
            // btnClearEncrypt
            // 
            btnClearEncrypt.BackColor = Color.LightCoral;
            btnClearEncrypt.Location = new Point(114, 507);
            btnClearEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnClearEncrypt.Name = "btnClearEncrypt";
            btnClearEncrypt.Size = new Size(91, 47);
            btnClearEncrypt.TabIndex = 7;
            btnClearEncrypt.Text = "Xóa";
            btnClearEncrypt.UseVisualStyleBackColor = false;
            btnClearEncrypt.Click += btnClearEncrypt_Click;
            // 
            // btnGenerateKeyEncrypt
            // 
            btnGenerateKeyEncrypt.BackColor = Color.LightYellow;
            btnGenerateKeyEncrypt.Location = new Point(217, 507);
            btnGenerateKeyEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnGenerateKeyEncrypt.Name = "btnGenerateKeyEncrypt";
            btnGenerateKeyEncrypt.Size = new Size(149, 47);
            btnGenerateKeyEncrypt.TabIndex = 8;
            btnGenerateKeyEncrypt.Text = "Tạo khóa tự động";
            btnGenerateKeyEncrypt.UseVisualStyleBackColor = false;
            btnGenerateKeyEncrypt.Click += btnGenerateKeyEncrypt_Click;
            // 
            // btnCopyPlain
            // 
            btnCopyPlain.BackColor = Color.LightBlue;
            btnCopyPlain.Location = new Point(11, 567);
            btnCopyPlain.Margin = new Padding(3, 4, 3, 4);
            btnCopyPlain.Name = "btnCopyPlain";
            btnCopyPlain.Size = new Size(149, 40);
            btnCopyPlain.TabIndex = 9;
            btnCopyPlain.Text = "Sao chép bản rõ";
            btnCopyPlain.UseVisualStyleBackColor = false;
            btnCopyPlain.Click += btnCopyPlain_Click;
            // 
            // btnCopyKeyEncrypt
            // 
            btnCopyKeyEncrypt.BackColor = Color.LightBlue;
            btnCopyKeyEncrypt.Location = new Point(171, 567);
            btnCopyKeyEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnCopyKeyEncrypt.Name = "btnCopyKeyEncrypt";
            btnCopyKeyEncrypt.Size = new Size(137, 40);
            btnCopyKeyEncrypt.TabIndex = 10;
            btnCopyKeyEncrypt.Text = "Sao chép khóa";
            btnCopyKeyEncrypt.UseVisualStyleBackColor = false;
            btnCopyKeyEncrypt.Click += btnCopyKeyEncrypt_Click;
            // 
            // btnCopyCipher
            // 
            btnCopyCipher.BackColor = Color.LightBlue;
            btnCopyCipher.Location = new Point(320, 567);
            btnCopyCipher.Margin = new Padding(3, 4, 3, 4);
            btnCopyCipher.Name = "btnCopyCipher";
            btnCopyCipher.Size = new Size(149, 40);
            btnCopyCipher.TabIndex = 11;
            btnCopyCipher.Text = "Sao chép bản mã";
            btnCopyCipher.UseVisualStyleBackColor = false;
            btnCopyCipher.Click += btnCopyCipher_Click;
            // 
            // btnLoadPlain
            // 
            btnLoadPlain.Location = new Point(11, 620);
            btnLoadPlain.Margin = new Padding(3, 4, 3, 4);
            btnLoadPlain.Name = "btnLoadPlain";
            btnLoadPlain.Size = new Size(114, 40);
            btnLoadPlain.TabIndex = 12;
            btnLoadPlain.Text = "Tải bản rõ";
            btnLoadPlain.Click += btnLoadPlain_Click;
            // 
            // btnLoadKeyEncrypt
            // 
            btnLoadKeyEncrypt.Location = new Point(137, 620);
            btnLoadKeyEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnLoadKeyEncrypt.Name = "btnLoadKeyEncrypt";
            btnLoadKeyEncrypt.Size = new Size(114, 40);
            btnLoadKeyEncrypt.TabIndex = 13;
            btnLoadKeyEncrypt.Text = "Tải khóa";
            btnLoadKeyEncrypt.Click += btnLoadKeyEncrypt_Click;
            // 
            // btnSavePlain
            // 
            btnSavePlain.Location = new Point(263, 620);
            btnSavePlain.Margin = new Padding(3, 4, 3, 4);
            btnSavePlain.Name = "btnSavePlain";
            btnSavePlain.Size = new Size(114, 40);
            btnSavePlain.TabIndex = 14;
            btnSavePlain.Text = "Lưu bản rõ";
            btnSavePlain.Click += btnSavePlain_Click;
            // 
            // btnSaveKeyEncrypt
            // 
            btnSaveKeyEncrypt.Location = new Point(389, 620);
            btnSaveKeyEncrypt.Margin = new Padding(3, 4, 3, 4);
            btnSaveKeyEncrypt.Name = "btnSaveKeyEncrypt";
            btnSaveKeyEncrypt.Size = new Size(114, 40);
            btnSaveKeyEncrypt.TabIndex = 15;
            btnSaveKeyEncrypt.Text = "Lưu khóa";
            btnSaveKeyEncrypt.Click += btnSaveKeyEncrypt_Click;
            // 
            // btnSaveCipher
            // 
            btnSaveCipher.Location = new Point(11, 667);
            btnSaveCipher.Margin = new Padding(3, 4, 3, 4);
            btnSaveCipher.Name = "btnSaveCipher";
            btnSaveCipher.Size = new Size(114, 40);
            btnSaveCipher.TabIndex = 16;
            btnSaveCipher.Text = "Lưu bản mã";
            btnSaveCipher.Click += btnSaveCipher_Click;
            // 
            // decryptPanel
            // 
            decryptPanel.BackColor = Color.FromArgb(255, 245, 230);
            decryptPanel.Controls.Add(lblCipherFormatDecrypt);
            decryptPanel.Controls.Add(cboCipherFormatDecrypt);
            decryptPanel.Controls.Add(lblCipherTextDecrypt);
            decryptPanel.Controls.Add(txtCipherTextDecrypt);
            decryptPanel.Controls.Add(lblKeyTypeDecrypt);
            decryptPanel.Controls.Add(cboKeyTypeDecrypt);
            decryptPanel.Controls.Add(lblKeyDecrypt);
            decryptPanel.Controls.Add(txtKeyDecrypt);
            decryptPanel.Controls.Add(lblPlainFormatDecrypt);
            decryptPanel.Controls.Add(cboPlainFormatDecrypt);
            decryptPanel.Controls.Add(lblPlainTextDecrypt);
            decryptPanel.Controls.Add(txtPlainTextDecrypt);
            decryptPanel.Controls.Add(btnDecrypt);
            decryptPanel.Controls.Add(btnClearDecrypt);
            decryptPanel.Controls.Add(btnGenerateKeyDecrypt);
            decryptPanel.Controls.Add(btnCopyCipherDecrypt);
            decryptPanel.Controls.Add(btnCopyKeyDecrypt);
            decryptPanel.Controls.Add(btnCopyPlainDecrypt);
            decryptPanel.Controls.Add(btnLoadCipherDecrypt);
            decryptPanel.Controls.Add(btnLoadKeyDecrypt);
            decryptPanel.Controls.Add(btnSaveCipherDecrypt);
            decryptPanel.Controls.Add(btnSaveKeyDecrypt);
            decryptPanel.Controls.Add(btnSavePlainDecrypt);
            decryptPanel.Dock = DockStyle.Fill;
            decryptPanel.Location = new Point(811, 4);
            decryptPanel.Margin = new Padding(3, 4, 3, 4);
            decryptPanel.Name = "decryptPanel";
            decryptPanel.Padding = new Padding(11, 13, 11, 13);
            decryptPanel.Size = new Size(1110, 705);
            decryptPanel.TabIndex = 1;
            decryptPanel.Paint += decryptPanel_Paint;
            // 
            // lblCipherFormatDecrypt
            // 
            lblCipherFormatDecrypt.Location = new Point(11, 13);
            lblCipherFormatDecrypt.Name = "lblCipherFormatDecrypt";
            lblCipherFormatDecrypt.Size = new Size(137, 33);
            lblCipherFormatDecrypt.TabIndex = 0;
            lblCipherFormatDecrypt.Text = "Định dạng bản mã:";
            // 
            // cboCipherFormatDecrypt
            // 
            cboCipherFormatDecrypt.DropDownStyle = ComboBoxStyle.DropDownList;
            cboCipherFormatDecrypt.Items.AddRange(new object[] { "Hex", "Base64" });
            cboCipherFormatDecrypt.Location = new Point(160, 13);
            cboCipherFormatDecrypt.Margin = new Padding(3, 4, 3, 4);
            cboCipherFormatDecrypt.Name = "cboCipherFormatDecrypt";
            cboCipherFormatDecrypt.Size = new Size(137, 28);
            cboCipherFormatDecrypt.TabIndex = 0;
            // 
            // lblCipherTextDecrypt
            // 
            lblCipherTextDecrypt.Location = new Point(11, 60);
            lblCipherTextDecrypt.Name = "lblCipherTextDecrypt";
            lblCipherTextDecrypt.Size = new Size(91, 33);
            lblCipherTextDecrypt.TabIndex = 1;
            lblCipherTextDecrypt.Text = "Bản mã:";
            // 
            // txtCipherTextDecrypt
            // 
            txtCipherTextDecrypt.Location = new Point(11, 93);
            txtCipherTextDecrypt.Margin = new Padding(3, 4, 3, 4);
            txtCipherTextDecrypt.Name = "txtCipherTextDecrypt";
            txtCipherTextDecrypt.Size = new Size(525, 105);
            txtCipherTextDecrypt.TabIndex = 1;
            txtCipherTextDecrypt.Text = "";
            // 
            // lblKeyTypeDecrypt
            // 
            lblKeyTypeDecrypt.Location = new Point(11, 213);
            lblKeyTypeDecrypt.Name = "lblKeyTypeDecrypt";
            lblKeyTypeDecrypt.Size = new Size(91, 33);
            lblKeyTypeDecrypt.TabIndex = 2;
            lblKeyTypeDecrypt.Text = "Loại khóa:";
            // 
            // cboKeyTypeDecrypt
            // 
            cboKeyTypeDecrypt.DropDownStyle = ComboBoxStyle.DropDownList;
            cboKeyTypeDecrypt.Items.AddRange(new object[] { "ASCII", "Hex" });
            cboKeyTypeDecrypt.Location = new Point(114, 213);
            cboKeyTypeDecrypt.Margin = new Padding(3, 4, 3, 4);
            cboKeyTypeDecrypt.Name = "cboKeyTypeDecrypt";
            cboKeyTypeDecrypt.Size = new Size(114, 28);
            cboKeyTypeDecrypt.TabIndex = 2;
            // 
            // lblKeyDecrypt
            // 
            lblKeyDecrypt.Location = new Point(11, 260);
            lblKeyDecrypt.Name = "lblKeyDecrypt";
            lblKeyDecrypt.Size = new Size(57, 33);
            lblKeyDecrypt.TabIndex = 3;
            lblKeyDecrypt.Text = "Khóa:";
            // 
            // txtKeyDecrypt
            // 
            txtKeyDecrypt.Location = new Point(69, 260);
            txtKeyDecrypt.Margin = new Padding(3, 4, 3, 4);
            txtKeyDecrypt.Name = "txtKeyDecrypt";
            txtKeyDecrypt.Size = new Size(342, 27);
            txtKeyDecrypt.TabIndex = 3;
            // 
            // lblPlainFormatDecrypt
            // 
            lblPlainFormatDecrypt.Location = new Point(11, 307);
            lblPlainFormatDecrypt.Name = "lblPlainFormatDecrypt";
            lblPlainFormatDecrypt.Size = new Size(137, 33);
            lblPlainFormatDecrypt.TabIndex = 4;
            lblPlainFormatDecrypt.Text = "Định dạng bản rõ:";
            // 
            // cboPlainFormatDecrypt
            // 
            cboPlainFormatDecrypt.DropDownStyle = ComboBoxStyle.DropDownList;
            cboPlainFormatDecrypt.Items.AddRange(new object[] { "Văn bản", "Hex", "Base64" });
            cboPlainFormatDecrypt.Location = new Point(160, 307);
            cboPlainFormatDecrypt.Margin = new Padding(3, 4, 3, 4);
            cboPlainFormatDecrypt.Name = "cboPlainFormatDecrypt";
            cboPlainFormatDecrypt.Size = new Size(137, 28);
            cboPlainFormatDecrypt.TabIndex = 4;
            // 
            // lblPlainTextDecrypt
            // 
            lblPlainTextDecrypt.Location = new Point(11, 353);
            lblPlainTextDecrypt.Name = "lblPlainTextDecrypt";
            lblPlainTextDecrypt.Size = new Size(91, 33);
            lblPlainTextDecrypt.TabIndex = 5;
            lblPlainTextDecrypt.Text = "Bản rõ:";
            // 
            // txtPlainTextDecrypt
            // 
            txtPlainTextDecrypt.Location = new Point(11, 387);
            txtPlainTextDecrypt.Margin = new Padding(3, 4, 3, 4);
            txtPlainTextDecrypt.Name = "txtPlainTextDecrypt";
            txtPlainTextDecrypt.ReadOnly = true;
            txtPlainTextDecrypt.Size = new Size(525, 105);
            txtPlainTextDecrypt.TabIndex = 5;
            txtPlainTextDecrypt.Text = "";
            // 
            // btnDecrypt
            // 
            btnDecrypt.BackColor = Color.LightGreen;
            btnDecrypt.Location = new Point(11, 507);
            btnDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnDecrypt.Name = "btnDecrypt";
            btnDecrypt.Size = new Size(91, 47);
            btnDecrypt.TabIndex = 6;
            btnDecrypt.Text = "Giải mã";
            btnDecrypt.UseVisualStyleBackColor = false;
            btnDecrypt.Click += btnDecrypt_Click;
            // 
            // btnClearDecrypt
            // 
            btnClearDecrypt.BackColor = Color.LightCoral;
            btnClearDecrypt.Location = new Point(114, 507);
            btnClearDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnClearDecrypt.Name = "btnClearDecrypt";
            btnClearDecrypt.Size = new Size(91, 47);
            btnClearDecrypt.TabIndex = 7;
            btnClearDecrypt.Text = "Xóa";
            btnClearDecrypt.UseVisualStyleBackColor = false;
            btnClearDecrypt.Click += btnClearDecrypt_Click;
            // 
            // btnGenerateKeyDecrypt
            // 
            btnGenerateKeyDecrypt.BackColor = Color.LightYellow;
            btnGenerateKeyDecrypt.Location = new Point(217, 507);
            btnGenerateKeyDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnGenerateKeyDecrypt.Name = "btnGenerateKeyDecrypt";
            btnGenerateKeyDecrypt.Size = new Size(149, 47);
            btnGenerateKeyDecrypt.TabIndex = 8;
            btnGenerateKeyDecrypt.Text = "Tạo khóa tự động";
            btnGenerateKeyDecrypt.UseVisualStyleBackColor = false;
            btnGenerateKeyDecrypt.Click += btnGenerateKeyDecrypt_Click;
            // 
            // btnCopyCipherDecrypt
            // 
            btnCopyCipherDecrypt.BackColor = Color.LightBlue;
            btnCopyCipherDecrypt.Location = new Point(11, 567);
            btnCopyCipherDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnCopyCipherDecrypt.Name = "btnCopyCipherDecrypt";
            btnCopyCipherDecrypt.Size = new Size(149, 40);
            btnCopyCipherDecrypt.TabIndex = 9;
            btnCopyCipherDecrypt.Text = "Sao chép bản mã";
            btnCopyCipherDecrypt.UseVisualStyleBackColor = false;
            btnCopyCipherDecrypt.Click += btnCopyCipherDecrypt_Click;
            // 
            // btnCopyKeyDecrypt
            // 
            btnCopyKeyDecrypt.BackColor = Color.LightBlue;
            btnCopyKeyDecrypt.Location = new Point(171, 567);
            btnCopyKeyDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnCopyKeyDecrypt.Name = "btnCopyKeyDecrypt";
            btnCopyKeyDecrypt.Size = new Size(137, 40);
            btnCopyKeyDecrypt.TabIndex = 10;
            btnCopyKeyDecrypt.Text = "Sao chép khóa";
            btnCopyKeyDecrypt.UseVisualStyleBackColor = false;
            btnCopyKeyDecrypt.Click += btnCopyKeyDecrypt_Click;
            // 
            // btnCopyPlainDecrypt
            // 
            btnCopyPlainDecrypt.BackColor = Color.LightBlue;
            btnCopyPlainDecrypt.Location = new Point(320, 567);
            btnCopyPlainDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnCopyPlainDecrypt.Name = "btnCopyPlainDecrypt";
            btnCopyPlainDecrypt.Size = new Size(149, 40);
            btnCopyPlainDecrypt.TabIndex = 11;
            btnCopyPlainDecrypt.Text = "Sao chép bản rõ";
            btnCopyPlainDecrypt.UseVisualStyleBackColor = false;
            btnCopyPlainDecrypt.Click += btnCopyPlainDecrypt_Click;
            // 
            // btnLoadCipherDecrypt
            // 
            btnLoadCipherDecrypt.Location = new Point(11, 620);
            btnLoadCipherDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnLoadCipherDecrypt.Name = "btnLoadCipherDecrypt";
            btnLoadCipherDecrypt.Size = new Size(114, 40);
            btnLoadCipherDecrypt.TabIndex = 12;
            btnLoadCipherDecrypt.Text = "Tải bản mã";
            btnLoadCipherDecrypt.Click += btnLoadCipherDecrypt_Click;
            // 
            // btnLoadKeyDecrypt
            // 
            btnLoadKeyDecrypt.Location = new Point(137, 620);
            btnLoadKeyDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnLoadKeyDecrypt.Name = "btnLoadKeyDecrypt";
            btnLoadKeyDecrypt.Size = new Size(114, 40);
            btnLoadKeyDecrypt.TabIndex = 13;
            btnLoadKeyDecrypt.Text = "Tải khóa";
            btnLoadKeyDecrypt.Click += btnLoadKeyDecrypt_Click;
            // 
            // btnSaveCipherDecrypt
            // 
            btnSaveCipherDecrypt.Location = new Point(263, 620);
            btnSaveCipherDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnSaveCipherDecrypt.Name = "btnSaveCipherDecrypt";
            btnSaveCipherDecrypt.Size = new Size(114, 40);
            btnSaveCipherDecrypt.TabIndex = 14;
            btnSaveCipherDecrypt.Text = "Lưu bản mã";
            btnSaveCipherDecrypt.Click += btnSaveCipherDecrypt_Click;
            // 
            // btnSaveKeyDecrypt
            // 
            btnSaveKeyDecrypt.Location = new Point(389, 620);
            btnSaveKeyDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnSaveKeyDecrypt.Name = "btnSaveKeyDecrypt";
            btnSaveKeyDecrypt.Size = new Size(114, 40);
            btnSaveKeyDecrypt.TabIndex = 15;
            btnSaveKeyDecrypt.Text = "Lưu khóa";
            btnSaveKeyDecrypt.Click += btnSaveKeyDecrypt_Click;
            // 
            // btnSavePlainDecrypt
            // 
            btnSavePlainDecrypt.Location = new Point(11, 667);
            btnSavePlainDecrypt.Margin = new Padding(3, 4, 3, 4);
            btnSavePlainDecrypt.Name = "btnSavePlainDecrypt";
            btnSavePlainDecrypt.Size = new Size(114, 40);
            btnSavePlainDecrypt.TabIndex = 16;
            btnSavePlainDecrypt.Text = "Lưu bản rõ";
            btnSavePlainDecrypt.Click += btnSavePlainDecrypt_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(1924, 713);
            Controls.Add(mainPanel);
            Margin = new Padding(3, 4, 3, 4);
            Name = "Form1";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "CÔNG CỤ MÃ HÓA VÀ GIẢI MÃ DES (ECB / PKCS#7)";
            WindowState = FormWindowState.Maximized;
            mainPanel.ResumeLayout(false);
            encryptPanel.ResumeLayout(false);
            encryptPanel.PerformLayout();
            decryptPanel.ResumeLayout(false);
            decryptPanel.PerformLayout();
            ResumeLayout(false);
        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel mainPanel;
        private System.Windows.Forms.Panel encryptPanel;
        private System.Windows.Forms.Panel decryptPanel;
        private System.Windows.Forms.Label lblPlainFormat;
        private System.Windows.Forms.ComboBox cboPlainFormat;
        private System.Windows.Forms.Label lblPlainText;
        private System.Windows.Forms.RichTextBox txtPlainText;
        private System.Windows.Forms.Label lblKeyTypeEncrypt;
        private System.Windows.Forms.ComboBox cboKeyTypeEncrypt;
        private System.Windows.Forms.Label lblKeyEncrypt;
        private System.Windows.Forms.TextBox txtKeyEncrypt;
        private System.Windows.Forms.Label lblCipherFormatEncrypt;
        private System.Windows.Forms.ComboBox cboCipherFormatEncrypt;
        private System.Windows.Forms.Label lblCipherText;
        private System.Windows.Forms.RichTextBox txtCipherText;
        private System.Windows.Forms.Button btnEncrypt;
        private System.Windows.Forms.Button btnClearEncrypt;
        private System.Windows.Forms.Button btnGenerateKeyEncrypt;
        private System.Windows.Forms.Button btnCopyPlain;
        private System.Windows.Forms.Button btnCopyKeyEncrypt;
        private System.Windows.Forms.Button btnCopyCipher;
        private System.Windows.Forms.Button btnLoadPlain;
        private System.Windows.Forms.Button btnLoadKeyEncrypt;
        private System.Windows.Forms.Button btnSavePlain;
        private System.Windows.Forms.Button btnSaveKeyEncrypt;
        private System.Windows.Forms.Button btnSaveCipher;
        private System.Windows.Forms.Label lblCipherFormatDecrypt;
        private System.Windows.Forms.ComboBox cboCipherFormatDecrypt;
        private System.Windows.Forms.Label lblCipherTextDecrypt;
        private System.Windows.Forms.RichTextBox txtCipherTextDecrypt;
        private System.Windows.Forms.Label lblKeyTypeDecrypt;
        private System.Windows.Forms.ComboBox cboKeyTypeDecrypt;
        private System.Windows.Forms.Label lblKeyDecrypt;
        private System.Windows.Forms.TextBox txtKeyDecrypt;
        private System.Windows.Forms.Label lblPlainFormatDecrypt;
        private System.Windows.Forms.ComboBox cboPlainFormatDecrypt;
        private System.Windows.Forms.Label lblPlainTextDecrypt;
        private System.Windows.Forms.RichTextBox txtPlainTextDecrypt;
        private System.Windows.Forms.Button btnDecrypt;
        private System.Windows.Forms.Button btnClearDecrypt;
        private System.Windows.Forms.Button btnGenerateKeyDecrypt;
        private System.Windows.Forms.Button btnCopyCipherDecrypt;
        private System.Windows.Forms.Button btnCopyKeyDecrypt;
        private System.Windows.Forms.Button btnCopyPlainDecrypt;
        private System.Windows.Forms.Button btnLoadCipherDecrypt;
        private System.Windows.Forms.Button btnLoadKeyDecrypt;
        private System.Windows.Forms.Button btnSaveCipherDecrypt;
        private System.Windows.Forms.Button btnSaveKeyDecrypt;
        private System.Windows.Forms.Button btnSavePlainDecrypt;
    }
}