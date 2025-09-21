/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasperpus;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import static tugasperpus.koneksi.koneksiDb;
 
/**
 *
 * @author firzi
 */
public final class Pengembalianperpus extends javax.swing.JFrame {
    Connection koneksi = null;
    private DefaultTableModel tabmode;
    private Map<String, String> kembaliMap = new HashMap<>();
   
    
    /**
     * Creates new form Pengembalianperpus
     */
    public Pengembalianperpus() {
        initComponents();
        setLocationRelativeTo(null);
        koneksi = koneksiDb();
        datapengembalian ();
       loadComboBoxNis();
       loadComboBoxKodebuku();
       loadComboBoxNip(); 
        Locale locale = new Locale ("id","ID");
       Locale.setDefault(locale);
       cnis.setSelectedItem(null);
       cnip.setSelectedItem(null);
       ckode.setSelectedItem(null);

     
    // Tambahkan ActionListener pada ComboBox Nis
    cnis.addActionListener(e -> loadDataNis());
    ckode.addActionListener(e -> loadDataKodebuku());
    cnip.addActionListener(e -> loadDataNip());
       
     

        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.addItem("Nis");
        comboBox.addItem("Nama");
        comboBox.addItem("Kodebuku");

    }
    
private void loadComboBoxNis() {
    try {
        String sql = "SELECT Nis FROM db_anggota";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            cnis.addItem(rs.getString("Nis"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Load Nis: " + e.getMessage());
    }
}
 
private void loadComboBoxKodebuku() {
    try {
        String sql = "SELECT Kode_buku FROM db_buku";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ckode.addItem(rs.getString("Kode_buku"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Load Kode_buku: " + e.getMessage());
    }
}
 
private void loadComboBoxNip() {
    try {
        String sql = "SELECT Nip FROM db_pegawai";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            cnip.addItem(rs.getString("Nip"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Load Nip: " + e.getMessage());
    }
}
 
private void loadDataNis() {
    String nis = cnis.getSelectedItem() != null ? cnis.getSelectedItem().toString() : "";
    if (nis.isEmpty()) return;

    try {
        // Query untuk mendapatkan data Nama dari Form Anggota
        String sqlAnggota = "SELECT Nama FROM db_anggota WHERE Nis = ?";
        PreparedStatement psAnggota = koneksi.prepareStatement(sqlAnggota);
        psAnggota.setString(1, nis);
        ResultSet rsAnggota = psAnggota.executeQuery();

        if (rsAnggota.next()) {
            tnama.setText(rsAnggota.getString("Nama")); // TextField Nama dari Anggota
        }
      
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

private void loadDataKodebuku() {
    String kodebuku = ckode.getSelectedItem() != null ? ckode.getSelectedItem().toString() : "";
    if (kodebuku.isEmpty()) return;

    try {
        // Query untuk mendapatkan Kode Buku dan Judul Buku
        String sqlBuku = "SELECT Kode_buku, Judul_buku FROM db_buku WHERE Kode_buku = ?";
        PreparedStatement psBuku = koneksi.prepareStatement(sqlBuku);
        psBuku.setString(1, kodebuku);
        ResultSet rsBuku = psBuku.executeQuery();

        if (rsBuku.next()) {
            tjudul.setText(rsBuku.getString("Judul_buku")); // Pastikan ini sesuai dengan TextField yang benar
        }
      
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

private void loadDataNip() {
    String nip = cnip.getSelectedItem() != null ? cnip.getSelectedItem().toString() : "";
    if (nip.isEmpty()) return;

    try {
        // Query untuk mendapatkan Nip dan Nama Pegawai
        String sqlPegawai = "SELECT Nip, Nama_pegawai FROM db_pegawai WHERE Nip = ?";
        PreparedStatement psPegawai = koneksi.prepareStatement(sqlPegawai);
        psPegawai.setString(1, nip);
        ResultSet rsPegawai = psPegawai.executeQuery();

        if (rsPegawai.next()) {
            tpegawai.setText(rsPegawai.getString("Nama_pegawai")); // Pastikan ini sesuai dengan TextField yang benar
        }
      
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}


protected void kosong(){
    cnis.setSelectedIndex(-1);    
    tnama.setText("");
    cnip.setSelectedIndex(-1);     
    tpegawai.setText("");
    ckode.setSelectedIndex(-1);    
    tjudul.setText("");
    cstatus.setSelectedIndex(0);   
    cpinjam.setSelectedIndex(0);   
    dkembali.setDate(null);  
   }

protected void datapengembalian() {
    Object[] Baris = {"Nis", "Nama", "Nip", "Pegawai", "Kode_buku", "Judul_buku", "Tglpengembalian", "Status", "Jumlahmeminjam"};
    tabmode = new DefaultTableModel(null, Baris);
    Tablepengembalian.setModel(tabmode); // Tabel di form pengembalian

    String sql = "SELECT * FROM db_pengembalian"; // Ambil data dari tabel peminjaman
    try {
        Statement stat = koneksi.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String nis = hasil.getString("Nis");
            String nama = hasil.getString("Nama");
            String nip = hasil.getString("Nip");
            String pegawai = hasil.getString("Pegawai");
            String kodeBuku = hasil.getString("Kode_buku");
            String judulBuku = hasil.getString("Judul_buku");
            String tglPengembalian = hasil.getString("Tglpengembalian");
            String status = hasil.getString("Status");
            String jumlahMeminjam = hasil.getString("Jumlahmeminjam");

       
            String[] data = {nis, nama, nip, pegawai, kodeBuku, judulBuku, tglPengembalian, status, jumlahMeminjam};
            tabmode.addRow(data);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Data: " + e.getMessage());
    }
}



  private Connection koneksiDb(){
    Connection koneksi = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login", "root", "");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Koneksi Gagal: " + e.getMessage());
    }
    return koneksi;
}
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tcari = new javax.swing.JTextField();
        btncari = new javax.swing.JButton();
        ckategori = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        tnama = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cnip = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        tpegawai = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tjudul = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        dkembali = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();
        cpinjam = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cnis = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        ckode = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cstatus = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablepengembalian = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnsave = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        btnrefresh = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btnprint = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmenuback = new javax.swing.JMenu();
        jback = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        mreport = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        mdataanggota = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mdatapegawai = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        mdatapeminjaman = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        mdatapengembalian = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        mdenda = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/Administrasii_Perpus.png")).getImage());
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(tcari, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 207, -1));

        btncari.setBackground(new java.awt.Color(255, 255, 51));
        btncari.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btncari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btncari.setText("CARI");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });
        getContentPane().add(btncari, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 380, 80, -1));

        ckategori.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        ckategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nis", "Nama", "Kodebuku" }));
        getContentPane().add(ckategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Nama");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 181, -1, -1));
        getContentPane().add(tnama, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 183, 161, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setText("NIP");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 225, -1, -1));

        getContentPane().add(cnip, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 226, 161, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setText("Pustakawan");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 266, -1, -1));
        getContentPane().add(tpegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 267, 161, -1));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setText("Judul Buku");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 182, -1, -1));
        getContentPane().add(tjudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 183, 153, -1));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel8.setText("Tgl Pengembalian");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 226, -1, -1));
        getContentPane().add(dkembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 226, 136, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setText("Total Kembali");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 270, -1, -1));

        cpinjam.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cpinjam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        getContentPane().add(cpinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 270, 50, -1));

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Administrasi Perpustakaan");

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("SDIT Hidayatullah");

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Jl. Cilangkap Baru No.99, RT.1/RW.1, Cilangkap, Kec. Cipayung, Kota Jakarta Timur");

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Instagram : tkit_cahayabintang ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(jLabel13))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(266, 266, 266))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 110));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setText("NIS");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 144, 30, -1));

        getContentPane().add(cnis, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 144, 161, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setText("Kode Buku");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 143, -1, -1));

        getContentPane().add(ckode, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 144, 153, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel10.setText("Status");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(751, 146, -1, -1));

        cstatus.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pinjam", "Kembali" }));
        cstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cstatusActionPerformed(evt);
            }
        });
        getContentPane().add(cstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(849, 145, 85, -1));

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_SD (1).png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, -1, 110, 110));

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("DATA PENGEMBALIAN");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, 37));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 300, 110));

        Tablepengembalian.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Tablepengembalian.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        Tablepengembalian.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nis", "Nama", "Nip", "Pustakawan", "Kodebuku", "Judulbuku", "Tglpengembalian", "Status", "Jumlahmeminjam"
            }
        ));
        Tablepengembalian.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablepengembalianMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tablepengembalian);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 960, 190));

        jPanel3.setBackground(new java.awt.Color(51, 255, 51));

        btnsave.setBackground(new java.awt.Color(255, 255, 51));
        btnsave.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/simpanlogo.png"))); // NOI18N
        btnsave.setText("SIMPAN");
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });

        btndelete.setBackground(new java.awt.Color(255, 255, 51));
        btndelete.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btndelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hapuslogo.png"))); // NOI18N
        btndelete.setText("HAPUS");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });

        btnrefresh.setBackground(new java.awt.Color(255, 255, 51));
        btnrefresh.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnrefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refreshlogo.png"))); // NOI18N
        btnrefresh.setText("REFRESH");
        btnrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrefreshActionPerformed(evt);
            }
        });

        btnedit.setBackground(new java.awt.Color(255, 255, 51));
        btnedit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editlogo.png"))); // NOI18N
        btnedit.setText("EDIT");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });

        btnprint.setBackground(new java.awt.Color(255, 255, 51));
        btnprint.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printerlogo.png"))); // NOI18N
        btnprint.setText("PRINT");
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });

        btnexit.setBackground(new java.awt.Color(255, 255, 51));
        btnexit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exitlogo.png"))); // NOI18N
        btnexit.setText("KELUAR");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnsave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btndelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnrefresh)
                .addGap(55, 526, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnexit, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnprint, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnprint)
                    .addComponent(btnexit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnsave)
                    .addComponent(btndelete)
                    .addComponent(btnrefresh)
                    .addComponent(btnedit))
                .addGap(21, 21, 21))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1000, 260));

        jPanel4.setBackground(new java.awt.Color(102, 255, 102));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 1000, 250));

        jmenuback.setText("Menu Utama");
        jmenuback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenubackActionPerformed(evt);
            }
        });

        jback.setText("Menu Utama");
        jback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbackActionPerformed(evt);
            }
        });
        jmenuback.add(jback);

        jMenuBar1.add(jmenuback);
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Buku");

        mreport.setText("Data Buku");
        mreport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mreportActionPerformed(evt);
            }
        });
        jMenu3.add(mreport);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Anggota");

        mdataanggota.setText("Data Anggota");
        mdataanggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdataanggotaActionPerformed(evt);
            }
        });
        jMenu4.add(mdataanggota);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Pustakawan");

        mdatapegawai.setText("Data Pustakawan");
        mdatapegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapegawaiActionPerformed(evt);
            }
        });
        jMenu5.add(mdatapegawai);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Peminjaman");

        mdatapeminjaman.setText("Data Peminjaman");
        mdatapeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapeminjamanActionPerformed(evt);
            }
        });
        jMenu6.add(mdatapeminjaman);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Pengembalian");

        mdatapengembalian.setText("Data Pengembalian");
        mdatapengembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapengembalianActionPerformed(evt);
            }
        });
        jMenu7.add(mdatapengembalian);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("Denda");

        mdenda.setText("Denda");
        mdenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdendaActionPerformed(evt);
            }
        });
        jMenu8.add(mdenda);

        jMenuBar1.add(jMenu8);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
       int rowIndex = Tablepengembalian.getSelectedRow(); 
    
    if (rowIndex == -1) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan diubah");
        return;
    }

    try {
        // Koneksi ke database
        Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login", "root", "");
        
        // Mengambil nilai dari komponen input
        String nis = (String) cnis.getSelectedItem(); 
        String nama = tnama.getText();                
        String nip = (String) cnip.getSelectedItem(); 
        String pegawai = tpegawai.getText();    
        String kodeBuku = (String) ckode.getSelectedItem(); 
        String judulBuku = tjudul.getText(); 
        
        // Pastikan Tgl Pengembalian valid
        String tglPengembalian = null;
        if (dkembali.getDate() != null) {
            tglPengembalian = new SimpleDateFormat("yyyy-MM-dd").format(dkembali.getDate());
        } else {
            JOptionPane.showMessageDialog(null, "Tanggal pengembalian tidak boleh kosong.");
            return; // Menghentikan eksekusi jika tanggal kosong
        }

        String status = (String) cstatus.getSelectedItem(); 
        String jumlahMeminjam = (String) cpinjam.getSelectedItem(); 

        // Debugging - cetak semua nilai untuk memastikan kevalidan
        System.out.println("Nis: " + nis);
        System.out.println("Nama: " + nama);
        System.out.println("Nip: " + nip);
        System.out.println("Pegawai: " + pegawai);
        System.out.println("Kode_buku: " + kodeBuku);
        System.out.println("Judul_buku: " + judulBuku);
        System.out.println("Tglpengembalian: " + tglPengembalian);
        System.out.println("Status: " + status);
        System.out.println("Jumlahmeminjam: " + jumlahMeminjam);

        // SQL untuk update data
        String sql = "UPDATE db_pengembalian SET Nis=?, Nama=?, Nip=?, Pegawai=?, Kode_buku=?, Judul_buku=?, Tglpengembalian=?, Status=?, Jumlahmeminjam=? WHERE Nis=? AND Tglpengembalian=?";
        PreparedStatement stat = koneksi.prepareStatement(sql);
        
        // Set parameter untuk PreparedStatement
        stat.setString(1, nis);
        stat.setString(2, nama);
        stat.setString(3, nip);
        stat.setString(4, pegawai);
        stat.setString(5, kodeBuku);
        stat.setString(6, judulBuku);
        stat.setString(7, tglPengembalian);
        stat.setString(8, status);
        stat.setString(9, jumlahMeminjam);
        stat.setString(10, tabmode.getValueAt(rowIndex, 0).toString()); // Asumsikan NIS adalah kolom pertama
        stat.setString(11, tabmode.getValueAt(rowIndex, 6).toString()); // Asumsikan Tgl Pengembalian adalah kolom ketujuh

        // Eksekusi update
        int result = stat.executeUpdate();
        
        if (result == 1) {
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            kosong(); // Mengosongkan form jika diperlukan
            tnama.requestFocus();
            tpegawai.requestFocus();
            tjudul.requestFocus();
            datapengembalian(); // Memanggil method untuk memperbarui tampilan tabel
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah data");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btneditActionPerformed

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed
        // TODO add your handling code here:
            try {
        // Membuat koneksi ke database
        Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login", "root", "");

        // SQL untuk memasukkan data ke dalam tabel db_peminjaman
        String sql = "INSERT INTO db_pengembalian (Nis, Nama, Nip, Pegawai, Kode_buku, Judul_buku, Tglpengembalian, Status, Jumlahmeminjam) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = koneksi.prepareStatement(sql);

        ps.setString(1, cnis.getSelectedItem().toString());   // NIS dari ComboBox
        ps.setString(2, tnama.getText());                     // Nama dari TextField
        ps.setString(3, cnip.getSelectedItem().toString());   // NIP dari ComboBox
        ps.setString(4, tpegawai.getText());                  // Pegawai dari TextField
        ps.setString(5, ckode.getSelectedItem().toString());   
        ps.setString(6, tjudul.getText());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalPeminjaman = sdf.format(dkembali.getDate());
        ps.setString(7, tanggalPeminjaman); 
        ps.setString(8, cstatus.getSelectedItem().toString());  // Status dari ComboBox
        ps.setString(9, cpinjam.getSelectedItem().toString());  // Jumlah Meminjam dari ComboBox

        // Eksekusi query
        int result = ps.executeUpdate();
        if (result == 1) {
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            datapengembalian();  // Refresh data di tabel
            kosong();          // Kosongkan semua TextField dan ComboBox
            
            // Reset ComboBox ke posisi awal (null atau kosong)
            cnis.setSelectedIndex(-1); 
            cnip.setSelectedIndex(-1);  
            ckode.setSelectedIndex(-1);
            cstatus.setSelectedIndex(-1);
            cpinjam.setSelectedIndex(-1);
        } else {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    } catch (NullPointerException e) {
        JOptionPane.showMessageDialog(null, "Pastikan semua field diisi dengan benar.");
    }
    }//GEN-LAST:event_btnsaveActionPerformed

    private void TablepengembalianMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablepengembalianMouseClicked
        // TODO add your handling code here:
        int bar = Tablepengembalian.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        String f = tabmode.getValueAt(bar, 5).toString();
        String g = tabmode.getValueAt(bar, 6).toString();
        String h = tabmode.getValueAt(bar, 7).toString();
        String i = tabmode.getValueAt(bar, 8).toString();
        
      cnis.setSelectedItem(a);               
    tnama.setText(b);                      
    cnip.setSelectedItem(c);               
    tpegawai.setText(d);                   
    ckode.setSelectedItem(e);             
    tjudul.setText(f);
    dkembali.setDate(parseDate(g));  
    cstatus.setSelectedItem(h);            
    cpinjam.setSelectedItem(i);      
}

private Date parseDate(String dateString) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Ganti dengan format yang sesuai
    try {
        return sdf.parse(dateString);
    } catch (ParseException e) {
        e.printStackTrace(); // Tangani kesalahan parsing
        return null; // Jika terjadi kesalahan, kembalikan null
     }     
    }//GEN-LAST:event_TablepengembalianMouseClicked

    private void btnrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrefreshActionPerformed
        // TODO add your handling code here:
      try {
            datapengembalian();  // Menggunakan method yang sudah ada
            kosong();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Refresh: " + e.getMessage());
        }
    }//GEN-LAST:event_btnrefreshActionPerformed

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexitActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnexitActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        // TODO add your handling code here:
         int[] selectedRows = Tablepengembalian.getSelectedRows();

    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(null, "Pilih Data yang akan di Hapus");
        return;
    }

    // Buat koneksi dan statement di luar loop
    try {
        String sql = "DELETE FROM db_pengembalian WHERE Nis=?";
        PreparedStatement stat = koneksi.prepareStatement(sql);

        // Loop untuk menghapus semua data yang dipilih
        for (int i = 0; i < selectedRows.length; i++) {
            int rowIndex = selectedRows[i];
            String nis = tabmode.getValueAt(rowIndex, 0).toString();
            stat.setString(1, nis);

            // Eksekusi perintah untuk menghapus data
            stat.executeUpdate();
        }

        // Setelah semua baris dihapus, tampilkan pesan sekali saja
        JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");

        // Perbarui tampilan tabel setelah penghapusan
        datapengembalian();
        kosong();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btndeleteActionPerformed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        // TODO add your handling code here:
      String kategori = ckategori.getSelectedItem().toString();
        String Cari = tcari.getText();

        if (Cari.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Masukkan kata kunci untuk mencari!");
            return;
        }

        String sql = "";
        switch (kategori) {
            case "Nis":
                sql = "SELECT * FROM db_pengembalian WHERE Nis LIKE '%" + Cari + "%'";
                break;
            case "Nama":
                sql = "SELECT * FROM db_pengembalian WHERE Nama LIKE '%" + Cari + "%'";
                break;
            case "Kode_buku":
                sql = "SELECT * FROM db_pengembalian WHERE Kodebuku LIKE '%" + Cari + "%'";
                break;
        }

        try {
            java.sql.Statement stat = koneksi.createStatement();
            ResultSet hasil = stat.executeQuery(sql);

            DefaultTableModel model = (DefaultTableModel) Tablepengembalian.getModel();
            model.setRowCount(0);

            while (hasil.next()) {
                String nis = hasil.getString("Nis");
                String nama = hasil.getString("Nama");
                String nip = hasil.getString("Nip");
                String pegawai = hasil.getString("Pegawai");
                String kodebuku = hasil.getString("Kode_buku");
                String judulbuku = hasil.getString("Judul_buku");
                String tglpeminjaman = hasil.getString("Tglpeminjaman");
                String tglpengembalian = hasil.getString("Tglpengembalian");
                String status = hasil.getString("Status");
                String jumlahmeminjam = hasil.getString("Jumlahmeminjam");

                model.addRow(new Object[]{nis, nama, nip, pegawai, kodebuku, judulbuku, tglpeminjaman, tglpengembalian, status, jumlahmeminjam});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error pencarian data: " + e.getMessage());
        }
    
    }//GEN-LAST:event_btncariActionPerformed

    private void cstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cstatusActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cstatusActionPerformed

    private void mreportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mreportActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_mreportActionPerformed

    private void jmenubackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenubackActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jmenubackActionPerformed

    private void jbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbackActionPerformed
        // TODO add your handling code here:
         dispose();

        MenuUtama MenuUtama= new MenuUtama();
        MenuUtama.setVisible(true);
    }//GEN-LAST:event_jbackActionPerformed

    private void mdataanggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdataanggotaActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Anggotaperpus Anggotaperpus= new Anggotaperpus();
        Anggotaperpus.setVisible(true);
    }//GEN-LAST:event_mdataanggotaActionPerformed

    private void mdatapegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdatapegawaiActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Pegawaiperpus Pegawaiperpus= new Pegawaiperpus();
        Pegawaiperpus.setVisible(true);
    }//GEN-LAST:event_mdatapegawaiActionPerformed

    private void mdatapeminjamanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdatapeminjamanActionPerformed
        // TODO add your handling code here:
         dispose();
        
        Peminjamanperpus Peminjamanperpus= new Peminjamanperpus();
        Peminjamanperpus.setVisible(true);
    }//GEN-LAST:event_mdatapeminjamanActionPerformed

    private void mdatapengembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdatapengembalianActionPerformed
        // TODO add your handling code here:
         dispose();
        
        Pengembalianperpus Pengembalianperpus= new Pengembalianperpus();
        Pengembalianperpus.setVisible(true);
    }//GEN-LAST:event_mdatapengembalianActionPerformed

    private void mdendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdendaActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Dendaperpus Dendaperpus= new Dendaperpus();
        Dendaperpus.setVisible(true);
    }//GEN-LAST:event_mdendaActionPerformed

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
        // TODO add your handling code here:
          try {
        String namaFile = "src/Laporan/LaporanPengembalian.jasper";
        Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login","root",""); 
        HashMap<String, Object> parameter = new HashMap<>();
        File report_file = new File(namaFile);
        if (report_file.exists()) {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(report_file);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, koneksi);
            JasperViewer.viewReport(jasperPrint, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
        } else {
            JOptionPane.showMessageDialog(null, "File laporan tidak ditemukan.");
        }
    } catch (JRException e) {
        JOptionPane.showMessageDialog(null, "Error loading report: " + e.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
     }    
    }//GEN-LAST:event_btnprintActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Pengembalianperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pengembalianperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pengembalianperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pengembalianperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Pengembalianperpus().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablepengembalian;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnrefresh;
    private javax.swing.JButton btnsave;
    private javax.swing.JComboBox<String> ckategori;
    private javax.swing.JComboBox<String> ckode;
    private javax.swing.JComboBox<String> cnip;
    private javax.swing.JComboBox<String> cnis;
    private javax.swing.JComboBox<String> cpinjam;
    private javax.swing.JComboBox<String> cstatus;
    private com.toedter.calendar.JDateChooser dkembali;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jback;
    private javax.swing.JMenu jmenuback;
    private javax.swing.JMenuItem mdataanggota;
    private javax.swing.JMenuItem mdatapegawai;
    private javax.swing.JMenuItem mdatapeminjaman;
    private javax.swing.JMenuItem mdatapengembalian;
    private javax.swing.JMenuItem mdenda;
    private javax.swing.JMenuItem mreport;
    private javax.swing.JTextField tcari;
    private javax.swing.JTextField tjudul;
    private javax.swing.JTextField tnama;
    private javax.swing.JTextField tpegawai;
    // End of variables declaration//GEN-END:variables

    private static class jButton extends JButton {

        public jButton(String edit) {
        }
    }

  
    
}
