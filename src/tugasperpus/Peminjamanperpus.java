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


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
public class Peminjamanperpus extends javax.swing.JFrame {
    Connection koneksi = null;
    private DefaultTableModel tabmode;
    

    /**
     * Creates new form Peminjamanperpus
     */
    public Peminjamanperpus() {
       initComponents();
       setLocationRelativeTo(null);
       koneksi = koneksiDb();
       loadComboBoxNis();
       loadComboBoxKodebuku();
       loadComboBoxNip();
       loadDataPeminjaman();
        Locale locale = new Locale ("id","ID");
       Locale.setDefault(locale);
       cnis.setSelectedItem(null);
       cnip.setSelectedItem(null);
       ckode.setSelectedItem(null);
      
       datapeminjaman();
    // Tambahkan ActionListener pada ComboBox Nis
    cnis.addActionListener(e -> loadDataNis());
    ckode.addActionListener(e -> loadDataKodebuku());
    cnip.addActionListener(e -> loadDataNip());
        
        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.addItem("Nis");
        comboBox.addItem("Nama");
        
        
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
    dpinjam.setDate(null);  
    cstatus.setSelectedIndex(0);   
    cpinjam.setSelectedIndex(0);   
    dtempo.setDate(null);  
    
   }
  
  protected void datapeminjaman() {
    Object[] Baris = {"Nis", "Nama", "Nip", "Pegawai", "Kode_buku", "Judul_buku", "Tglpeminjaman", "Status", "Jumlahmeminjam", "Jatuh_tempo"};
    tabmode = new DefaultTableModel(null, Baris);
    Tablepinjam.setModel(tabmode);
    
    String sql = "SELECT * FROM db_peminjaman";
    
    try {
        java.sql.Statement stat = koneksi.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String nis = hasil.getString("Nis");
            String nama = hasil.getString("Nama");
            String nip = hasil.getString("Nip");
            String pegawai = hasil.getString("Pegawai");
            String kodeBuku = hasil.getString("Kode_buku");
            String judulBuku = hasil.getString("Judul_buku");
            String tglPeminjaman = hasil.getString("Tglpeminjaman");
            String status = hasil.getString("Status"); // Ini harus tetap "Pinjam"
            String jumlahMeminjam = hasil.getString("Jumlahmeminjam");
            String jatuhtempo = hasil.getString("Jatuh_tempo");

            String[] data = {nis, nama, nip, pegawai, kodeBuku, judulBuku, tglPeminjaman, status, jumlahMeminjam, jatuhtempo};
            tabmode.addRow(data);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Data di form peminjaman: " + e.getMessage());
    }
}
  
protected void loadDataPeminjaman() {
    Object[] Baris = {"Nis", "Nama", "Nip", "Pegawai", "Kode_buku",  "Judul_buku", "Tglpeminjaman", "Status", "Jumlahmeminjam", "Jatuh_tempo"};
    tabmode = new DefaultTableModel(null, Baris);
    Tablepinjam.setModel(tabmode);

    // Ambil data peminjaman yang hanya memiliki status "Pinjam"
    String sql = "SELECT * FROM db_peminjaman WHERE Status = 'Pinjam'"; 

    try {
        Statement stat = koneksiDb().createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()) {
            String nis = hasil.getString("Nis");
            String nama = hasil.getString("Nama");
            String nip = hasil.getString("Nip");
            String pegawai = hasil.getString("Pegawai");
            String kodeBuku = hasil.getString("Kode_buku");
            String judulBuku = hasil.getString("Judul_buku");
            String tglPeminjaman = hasil.getString("Tglpeminjaman");
            String status = hasil.getString("Status"); // Ambil status
            String jumlahMeminjam = hasil.getString("Jumlahmeminjam");
            String jatuhtempo = hasil.getString("Jatuh_tempo");

            // Tambahkan data ke tabel
            String[] data = {nis, nama, nip, pegawai, kodeBuku, judulBuku, tglPeminjaman, status, jumlahMeminjam, jatuhtempo};
            tabmode.addRow(data); // Tambahkan data ke tabel
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error Data: " + e.getMessage());
    }
}


private Connection koneksi(){
    try {
        Class.forName("com.mysql.jdbc.Driver");
        Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login","root","");
        return koneksi;
        
    }catch (Exception e){
        System.out.println("Koneksi Gagal:" + e.getMessage());
        return null;
    }
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        dpinjam = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablepinjam = new javax.swing.JTable();
        tcaripinjam = new javax.swing.JTextField();
        btncari = new javax.swing.JButton();
        btndelete = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        ckategori = new javax.swing.JComboBox<>();
        cstatus = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        dtempo = new com.toedter.calendar.JDateChooser();
        tjudul = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ckode = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cpinjam = new javax.swing.JComboBox<>();
        btnsave = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btnclear = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        btnprint = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tpegawai = new javax.swing.JTextField();
        cnip = new javax.swing.JComboBox<>();
        tnama = new javax.swing.JTextField();
        cnis = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmenuback = new javax.swing.JMenu();
        jback = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mdatabuku = new javax.swing.JMenuItem();
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

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setText("Nama");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 177, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setText("Judul Buku");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 177, -1, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setText("Tgl Peminjaman");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 247, -1, -1));
        getContentPane().add(dpinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 247, 152, -1));

        Tablepinjam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NIS", "Nama", "NIP", "Pustakawan", "Kodebuku", "Tgl Pinjam", "Judul Buku", "Status", "Jumlahpinjam", "Jatuh Tempo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        Tablepinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablepinjamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tablepinjam);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 423, 1050, 185));
        getContentPane().add(tcaripinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 390, 185, -1));

        btncari.setBackground(new java.awt.Color(255, 255, 51));
        btncari.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btncari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btncari.setText("CARI");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });
        getContentPane().add(btncari, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 390, -1, -1));

        btndelete.setBackground(new java.awt.Color(255, 255, 51));
        btndelete.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btndelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hapuslogo.png"))); // NOI18N
        btndelete.setText("HAPUS");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });
        getContentPane().add(btndelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 290, 100, -1));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setText("Status");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(328, 294, -1, -1));

        ckategori.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        ckategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NIS", "Nama", "Kodebuku" }));
        ckategori.setToolTipText("");
        getContentPane().add(ckategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 76, -1));

        cstatus.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pinjam", "Kembali" }));
        cstatus.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cstatusActionPerformed(evt);
            }
        });
        getContentPane().add(cstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 293, 90, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setText("NIP");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 247, -1, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setText("Pustakawan");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 294, -1, -1));

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel12.setText("Jatuh Tempo");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(683, 177, -1, -1));
        getContentPane().add(dtempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 177, 160, -1));
        getContentPane().add(tjudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(466, 178, 152, -1));

        jPanel1.setBackground(new java.awt.Color(0, 204, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Administrasi Perpustakaan");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 6, -1, -1));

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("SDIT Hidayatullah");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, -1, -1));

        jLabel14.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Jl. Cilangkap Baru No.99, RT.1/RW.1, Cilangkap, Kec. Cipayung, Kota Jakarta Timur");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 69, -1, -1));

        jLabel15.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Instagram : tkit_cahayabintang ");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 94, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 120));

        jPanel2.setBackground(new java.awt.Color(153, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_SD (1).png"))); // NOI18N
        jLabel16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, 150, 120));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PEMINJAMAN");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 180, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 390, 120));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel9.setText("NIS");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Kode Buku");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, -1, -1));

        getContentPane().add(ckode, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 140, 152, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel10.setText("Jumlah Pinjam");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 140, -1, -1));

        cpinjam.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cpinjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cpinjamActionPerformed(evt);
            }
        });
        getContentPane().add(cpinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 140, 52, -1));

        btnsave.setBackground(new java.awt.Color(255, 255, 51));
        btnsave.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/simpanlogo.png"))); // NOI18N
        btnsave.setText("SIMPAN");
        btnsave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnsave.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnsave, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 230, 105, -1));

        btnedit.setBackground(new java.awt.Color(255, 255, 51));
        btnedit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editlogo.png"))); // NOI18N
        btnedit.setText("UBAH");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });
        getContentPane().add(btnedit, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 230, 90, -1));

        btnclear.setBackground(new java.awt.Color(255, 255, 51));
        btnclear.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refreshlogo.png"))); // NOI18N
        btnclear.setText("REFRESH");
        btnclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclearActionPerformed(evt);
            }
        });
        getContentPane().add(btnclear, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 350, -1, -1));

        btnexit.setBackground(new java.awt.Color(255, 255, 51));
        btnexit.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exitlogo.png"))); // NOI18N
        btnexit.setText("KELUAR");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });
        getContentPane().add(btnexit, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 290, -1, -1));

        btnprint.setBackground(new java.awt.Color(255, 255, 51));
        btnprint.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printerlogo.png"))); // NOI18N
        btnprint.setText("PRINT");
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });
        getContentPane().add(btnprint, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 350, 95, -1));

        jPanel3.setBackground(new java.awt.Color(102, 255, 51));

        cnis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cnisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cnis, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(tnama, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cnip, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(807, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(cnis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(cnip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(tpegawai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 1080, 260));

        jPanel4.setBackground(new java.awt.Color(102, 255, 102));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 1080, 240));

        jmenuback.setText("Menu Utama");
        jmenuback.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
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

        jMenu3.setText("Buku");
        jMenu3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdatabuku.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        mdatabuku.setText("Data Buku");
        mdatabuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatabukuActionPerformed(evt);
            }
        });
        jMenu3.add(mdatabuku);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Anggota");
        jMenu4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdataanggota.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        mdataanggota.setText("Data Anggota");
        mdataanggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdataanggotaActionPerformed(evt);
            }
        });
        jMenu4.add(mdataanggota);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Pustakawan");
        jMenu5.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdatapegawai.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        mdatapegawai.setText("Data Pustawakan");
        mdatapegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapegawaiActionPerformed(evt);
            }
        });
        jMenu5.add(mdatapegawai);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Peminjaman");
        jMenu6.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdatapeminjaman.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        mdatapeminjaman.setText("Data Peminjaman");
        mdatapeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapeminjamanActionPerformed(evt);
            }
        });
        jMenu6.add(mdatapeminjaman);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Pengembalian");
        jMenu7.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdatapengembalian.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        mdatapengembalian.setText("Data Pengembalian");
        mdatapengembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdatapengembalianActionPerformed(evt);
            }
        });
        jMenu7.add(mdatapengembalian);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("Denda");
        jMenu8.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        mdenda.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
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

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed
        // TODO add your handling code here:
        try {
        // Membuat koneksi ke database
        Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login", "root", "");

        // SQL untuk memasukkan data ke dalam tabel db_peminjaman
        String sql = "INSERT INTO db_peminjaman (Nis, Nama, Nip, Pegawai, Kode_buku, Judul_buku, Tglpeminjaman, Status, Jumlahmeminjam, Jatuh_tempo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = koneksi.prepareStatement(sql);

        ps.setString(1, cnis.getSelectedItem().toString());   // NIS dari ComboBox
        ps.setString(2, tnama.getText());                     // Nama dari TextField
        ps.setString(3, cnip.getSelectedItem().toString());   // NIP dari ComboBox
        ps.setString(4, tpegawai.getText());                  // Pegawai dari TextField
        ps.setString(5, ckode.getSelectedItem().toString()); 
        ps.setString(6, tjudul.getText());   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalPeminjaman = sdf.format(dpinjam.getDate());
        ps.setString(7, tanggalPeminjaman);   
        ps.setString(8, cstatus.getSelectedItem().toString());  // Status dari ComboBox
        ps.setString(9, cpinjam.getSelectedItem().toString());  // Jumlah Meminjam dari ComboBox
        String jatuhtempo = sdf.format(dtempo.getDate());
        ps.setString(10, jatuhtempo);
        // Eksekusi query
        int result = ps.executeUpdate();
        if (result == 1) {
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
            datapeminjaman();  // Refresh data di tabel
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

    private void TablepinjamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablepinjamMouseClicked
        // TODO add your handling code here:
         int bar = Tablepinjam.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        String f = tabmode.getValueAt(bar, 5).toString();
        String g = tabmode.getValueAt(bar, 6).toString();
        String h = tabmode.getValueAt(bar, 7).toString();
        String i = tabmode.getValueAt(bar, 8).toString();
        String j = tabmode.getValueAt(bar, 9).toString();
        
    cnis.setSelectedItem(a);               // NIS
    tnama.setText(b);                      // Nama
    cnip.setSelectedItem(c);               // NIP
    tpegawai.setText(d);                   // Nama Pegawai
    ckode.setSelectedItem(e);              // Kode Buku
    tjudul.setText(f);  
    dpinjam.setDate(parseDate(g));  
    cstatus.setSelectedItem(h);            // Status
    cpinjam.setSelectedItem(i); 
    dtempo.setDate(parseDate(j));  
    
    
}

private Date parseDate(String dateString) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Ganti dengan format yang sesuai
    try {
        return sdf.parse(dateString);
    } catch (ParseException e) {
        e.printStackTrace(); // Tangani kesalahan parsing
        return null; // Jika terjadi kesalahan, kembalikan null
     }     
    }//GEN-LAST:event_TablepinjamMouseClicked

    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        // TODO add your handling code here:
            int rowIndex = Tablepinjam.getSelectedRow(); 

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
        
        String tglPeminjaman = null;
        if (dpinjam.getDate() != null) {
            tglPeminjaman = new SimpleDateFormat("yyyy-MM-dd").format(dpinjam.getDate());
        } else {
            JOptionPane.showMessageDialog(null, "Tanggal peminjaman tidak boleh kosong.");
            return; // Menghentikan eksekusi jika tanggal kosong
        }

        // Mengambil tanggal pengembalian dari komponen dkembali
        String jatuhtempo = null;
        if (dtempo.getDate() != null) {
            jatuhtempo = new SimpleDateFormat("yyyy-MM-dd").format(dtempo.getDate());
        } else {
            JOptionPane.showMessageDialog(null, "Tanggal jatuh tempo tidak boleh kosong.");
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
        System.out.println("Tglpeminjaman: " + tglPeminjaman);
        System.out.println("Status: " + status);
        System.out.println("Jumlahmeminjam: " + jumlahMeminjam);
        System.out.println("Jatuh_tempo: " + jatuhtempo);

        // SQL untuk update data
        String sql = "UPDATE db_peminjaman SET Nis=?, Nama=?, Nip=?, Pegawai=?, Kode_buku=?, Judul_buku=?, Tglpeminjaman=?, Tglpengembalian=?, Status=?, Jumlahmeminjam=?, Jatuh_tempo=? WHERE Nis=? AND Tglpeminjaman=?";
        PreparedStatement stat = koneksi.prepareStatement(sql);
        
        // Set parameter untuk PreparedStatement
        stat.setString(1, nis);
        stat.setString(2, nama);
        stat.setString(3, nip);
        stat.setString(4, pegawai);
        stat.setString(5, kodeBuku);
        stat.setString(6, judulBuku);
        stat.setString(7, tglPeminjaman);
        stat.setString(8, status);
        stat.setString(9, jumlahMeminjam);
        stat.setString(10, jatuhtempo);
        stat.setString(11, tabmode.getValueAt(rowIndex, 0).toString()); // Asumsikan NIS adalah kolom pertama
        stat.setString(12, tabmode.getValueAt(rowIndex, 6).toString()); // Asumsikan Tgl Pengembalian adalah kolom ketujuh

        // Eksekusi update
        int result = stat.executeUpdate();
        
        if (result == 1) {
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            kosong(); // Mengosongkan form jika diperlukan
            tnama.requestFocus();
            tpegawai.requestFocus();
            tjudul.requestFocus();
            datapeminjaman(); // Memanggil method untuk memperbarui tampilan tabel
        } else {
            JOptionPane.showMessageDialog(null, "Gagal mengubah data");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btneditActionPerformed

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexitActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnexitActionPerformed

    private void btnclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnclearActionPerformed
        // TODO add your handling code here:
        kosong();
        datapeminjaman();
    }//GEN-LAST:event_btnclearActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        // TODO add your handling code here:
        int[] selectedRows = Tablepinjam.getSelectedRows();

    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(null, "Pilih Data yang akan di Hapus");
        return;
    }

    // Buat koneksi dan statement di luar loop
    try {
        String sql = "DELETE FROM db_peminjaman WHERE Nis=?";
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
        datapeminjaman();
        kosong();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btndeleteActionPerformed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        // TODO add your handling code here:
        String selectedItem = (String) ckategori.getSelectedItem();
        String caripinjam = tcaripinjam.getText();
        
        
        
        Object[] Baris = {"Nis", "Nama", "Nip", "Pegawai", "Kode_buku", "Judul_buku", "Tglpeminjaman", "Status", "Jumlahmeminjam", "Jatuh_tempo"};
        tabmode = new DefaultTableModel(null, Baris);
        Tablepinjam.setModel(tabmode);
        
        String sql = "SELECT * FROM db_peminjaman WHERE Nis LIKE ? OR Nama LIKE ?";
       
        try {
            PreparedStatement stat = koneksi.prepareStatement(sql);
            stat.setString(1, "%" + caripinjam + "%");
            stat.setString(2, "%" + caripinjam + "%");
            
            
            
            ResultSet hasil = stat.executeQuery();
            while(hasil.next()){
                String a = hasil.getString("Nis");
                String b = hasil.getString("Nama");
                String c = hasil.getString("Nip");
                String d = hasil.getString("Pegawai");
                String e = hasil.getString("Kode_buku");
                String f = hasil.getString("Judul_buku");
                String g = hasil.getString("Tglpeminjaman");
                String h = hasil.getString("Status");
                String i = hasil.getString("Jumlahmeminjam");
                String j = hasil.getString("Jatuh_tempo");
                
                
                String[] data={a, b, c, d, e, f, g, h, i, j};
                tabmode.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erorr : " + e.getMessage());
        }
    }//GEN-LAST:event_btncariActionPerformed

    private void cstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cstatusActionPerformed

    private void mdatabukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdatabukuActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Bukuperpus Bukuperpus= new Bukuperpus();
        Bukuperpus.setVisible(true);
    }//GEN-LAST:event_mdatabukuActionPerformed

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

    private void cpinjamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cpinjamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cpinjamActionPerformed

    private void cnisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cnisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cnisActionPerformed

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
        // TODO add your handling code here:
        try {
        String namaFile = "src/Laporan/LaporanPeminjaman.jasper";
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
            java.util.logging.Logger.getLogger(Peminjamanperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Peminjamanperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Peminjamanperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Peminjamanperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Peminjamanperpus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablepinjam;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btnclear;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnsave;
    private javax.swing.JComboBox<String> ckategori;
    private javax.swing.JComboBox<String> ckode;
    private javax.swing.JComboBox<String> cnip;
    private javax.swing.JComboBox<String> cnis;
    private javax.swing.JComboBox<String> cpinjam;
    private javax.swing.JComboBox<String> cstatus;
    private com.toedter.calendar.JDateChooser dpinjam;
    private com.toedter.calendar.JDateChooser dtempo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JMenuItem mdatabuku;
    private javax.swing.JMenuItem mdatapegawai;
    private javax.swing.JMenuItem mdatapeminjaman;
    private javax.swing.JMenuItem mdatapengembalian;
    private javax.swing.JMenuItem mdenda;
    private javax.swing.JTextField tcaripinjam;
    private javax.swing.JTextField tjudul;
    private javax.swing.JTextField tnama;
    private javax.swing.JTextField tpegawai;
    // End of variables declaration//GEN-END:variables

    private void loadComboBox(String select_nisnama_FROM_Anggotaperpus, JComboBox<String> cnis, JComboBox<String> cnama) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateTextField() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
