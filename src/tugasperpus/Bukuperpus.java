/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tugasperpus;
import java.util.List;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import static tugasperpus.koneksi.koneksiDb;
/**
 *
 * @author firzi
 */
public final class Bukuperpus extends javax.swing.JFrame {
    Connection koneksi = null;
    
    private DefaultTableModel tabmode;
     private Statement stmt;
    
    /** Creates new form Bukuperpus */
    public Bukuperpus() {
        initComponents();
        setLocationRelativeTo(null);
        setTableColumnAlignmentCenter();
        koneksi = koneksiDb();
        databuku ();
      
       Locale locale = new Locale ("id","ID");
       Locale.setDefault(locale);
        
        JComboBox<Object> comboBox = new JComboBox<>();
        comboBox.addItem("Kode_buku");
        comboBox.addItem("Judul_buku");
        
       
    }
    
   


 protected void aktif() {
    tkodebuku.setEnabled(true);
    tjudul.setEnabled(true);
    tpengarang.setEnabled(true);
    tpenerbit.setEnabled(true);
    tkodebuku.requestFocus();
}    

  
 protected void kosong() {
    tkodebuku.setText("");
    tjudul.setText("");
    tpengarang.setText("");
    tpenerbit.setText("");
    tterbit.setText("");
    tjumlah.setText("");
    tcaribuku.setText("");
}

protected void databuku()   {
    Object[] Baris ={"Kode_buku", "Judul_buku", "Jumlah_buku", "Pengarang", "Penerbit", "Tahun_terbit"};
    tabmode = new DefaultTableModel(null, Baris);
    Tablebuku.setModel(tabmode);
    String sql = "SELECT * FROM db_buku";
    try {
        java.sql.Statement stat = koneksi.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while (hasil.next()){
           String a = hasil.getString("Kode_buku");
           String b = hasil.getString("Judul_buku");
           String c = hasil.getString("Jumlah_buku");
           String d = hasil.getString("Pengarang");
           String e = hasil.getString("Penerbit");
           String f = hasil.getString("Tahun_terbit");
        
           String[] data={a,b,c,d,e,f};
           tabmode.addRow(data);
      }
    } catch (SQLException e) {
        System.out.println("Error Data:" + e.getMessage());
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



private void saveBuku(String kodebuku, String judul, String jumlah, String pengarang, String penerbit, String tahunterbit) {
    try {
        // Mengecek jumlah data buku yang ada di tabel
        int count = getDataCount();

        // Jika jumlah data sudah mencapai 500, tidak menambahkan data baru
        if (count >= 500) {
            JOptionPane.showMessageDialog(this, "Jumlah data buku sudah mencapai 500, tidak bisa menambah lagi.");
            return; // Jika sudah 500, keluar dari fungsi dan tidak menambah data
        }

        // Jika jumlah data belum mencapai 500, simpan data buku baru
        String query = "INSERT INTO db_buku (kode_buku, judul_buku, jumlah_buku, pengarang, penerbit, tahun_terbit) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = koneksi.prepareStatement(query)) {
            pstmt.setString(1, kodebuku);
            pstmt.setString(2, judul);
            pstmt.setString(3, jumlah);
            pstmt.setString(4, pengarang);
            pstmt.setString(5, penerbit);
            pstmt.setString(6, tahunterbit);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data buku disimpan.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
}

// Fungsi untuk mendapatkan jumlah data buku di tabel
private int getDataCount() {
    int count = 0;
    try {
        // Query untuk menghitung jumlah baris di tabel db_buku
        String sql = "SELECT COUNT(*) FROM db_buku";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            count = rs.getInt(1); // Ambil hasil hitung jumlah baris
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    return count;
}



      /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        tkodebuku = new javax.swing.JTextField();
        tjudul = new javax.swing.JTextField();
        tpengarang = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tablebuku = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tjumlah = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tpenerbit = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tterbit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btndelete = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        btnclear = new javax.swing.JButton();
        btnedit = new javax.swing.JButton();
        btnsave = new javax.swing.JButton();
        btncari = new javax.swing.JButton();
        tcaribuku = new javax.swing.JTextField();
        cpencarian = new javax.swing.JComboBox<>();
        btnprint = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmenuback = new javax.swing.JMenu();
        jbackmenu = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jreport = new javax.swing.JMenu();
        mbukudata = new javax.swing.JMenuItem();
        jreport1 = new javax.swing.JMenu();
        manggotadata = new javax.swing.JMenuItem();
        jreport2 = new javax.swing.JMenu();
        mpegawaidata = new javax.swing.JMenuItem();
        jreport3 = new javax.swing.JMenu();
        mpeminjamandata = new javax.swing.JMenuItem();
        jreport4 = new javax.swing.JMenu();
        mpengembaliandata = new javax.swing.JMenuItem();
        jreport5 = new javax.swing.JMenu();
        mdenda = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/images/Administrasii_Perpus.png")).getImage());
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(tkodebuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 133, 250, -1));
        getContentPane().add(tjudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 172, 250, -1));
        getContentPane().add(tpengarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 250, -1));

        jPanel2.setBackground(new java.awt.Color(0, 204, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Administrasi Perpustakaan");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 430, -1));

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("SDIT Hidayatullah");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 50, -1, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Jl. Cilangkap Baru No.99, RT.1/RW.1, Cilangkap, Kec. Cipayung, Kota Jakarta Timur");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, 21));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Instagram : tkit_cahayabintang ");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 178, 20));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 120));

        Tablebuku.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Buku", "Judul Buku", "Jumlah Buku", "Pengarang", "Penerbit", "Tahun Terbit"
            }
        ));
        Tablebuku.setColumnSelectionAllowed(true);
        Tablebuku.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Tablebuku.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablebukuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tablebuku);
        Tablebuku.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 1280, 220));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("KODE BUKU");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 132, 91, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setText("JUDUL BUKU");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 171, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setText("JUMLAH BUKU");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 215, -1, -1));
        getContentPane().add(tjumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 216, 98, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setText("PENGARANG");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 259, 90, -1));

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel9.setText("PENERBIT");

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setText("TAHUN TERBIT");

        btndelete.setBackground(new java.awt.Color(255, 204, 51));
        btndelete.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btndelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/hapuslogo.png"))); // NOI18N
        btndelete.setText("HAPUS");
        btndelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndeleteActionPerformed(evt);
            }
        });

        btnexit.setBackground(new java.awt.Color(255, 204, 51));
        btnexit.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exitlogo.png"))); // NOI18N
        btnexit.setText("EXIT");
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });

        btnclear.setBackground(new java.awt.Color(255, 204, 51));
        btnclear.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refreshlogo.png"))); // NOI18N
        btnclear.setText("REFRESH");
        btnclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnclearActionPerformed(evt);
            }
        });

        btnedit.setBackground(new java.awt.Color(255, 204, 51));
        btnedit.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnedit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editlogo.png"))); // NOI18N
        btnedit.setText("EDIT");
        btnedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditActionPerformed(evt);
            }
        });

        btnsave.setBackground(new java.awt.Color(255, 204, 51));
        btnsave.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/simpanlogo.png"))); // NOI18N
        btnsave.setText("SIMPAN");
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });

        btncari.setBackground(new java.awt.Color(255, 204, 51));
        btncari.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btncari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search.png"))); // NOI18N
        btncari.setText("CARI");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });

        tcaribuku.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tcaribukuActionPerformed(evt);
            }
        });

        cpencarian.setBackground(new java.awt.Color(255, 204, 153));
        cpencarian.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        cpencarian.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kode buku", "Judul buku" }));

        btnprint.setBackground(new java.awt.Color(255, 255, 51));
        btnprint.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printerlogo.png"))); // NOI18N
        btnprint.setText("PRINT");
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(btnsave)
                            .addComponent(btnedit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(cpencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(tcaribuku, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btncari)
                        .addGap(383, 383, 383)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tterbit, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tpenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnclear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnprint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btndelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnexit, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(65, 65, 65))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tpenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tterbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addComponent(btnedit))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(btnsave)
                            .addGap(56, 56, 56)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btndelete)
                            .addComponent(btnexit))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnclear)
                            .addComponent(btnprint))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tcaribuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btncari)
                            .addComponent(cpencarian, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(272, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 1310, 480));

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));

        jLabel12.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DATA BUKU");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_SD (1).png"))); // NOI18N
        jLabel14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(136, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(100, 100, 100)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 540, 120));

        jmenuback.setText("Menu Utama");
        jmenuback.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmenubackActionPerformed(evt);
            }
        });

        jbackmenu.setText("Menu Utama");
        jbackmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbackmenuActionPerformed(evt);
            }
        });
        jmenuback.add(jbackmenu);

        jMenuBar1.add(jmenuback);
        jMenuBar1.add(jMenu2);

        jreport.setText("Buku");

        mbukudata.setText("Data Buku");
        mbukudata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mbukudataActionPerformed(evt);
            }
        });
        jreport.add(mbukudata);

        jMenuBar1.add(jreport);

        jreport1.setText("Anggota");

        manggotadata.setText("Data Anggota");
        manggotadata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manggotadataActionPerformed(evt);
            }
        });
        jreport1.add(manggotadata);

        jMenuBar1.add(jreport1);

        jreport2.setText("Pustakawan");

        mpegawaidata.setText("Data Pustawakan");
        mpegawaidata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mpegawaidataActionPerformed(evt);
            }
        });
        jreport2.add(mpegawaidata);

        jMenuBar1.add(jreport2);

        jreport3.setText("Peminjaman");

        mpeminjamandata.setText("Data Peminjaman");
        mpeminjamandata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mpeminjamandataActionPerformed(evt);
            }
        });
        jreport3.add(mpeminjamandata);

        jMenuBar1.add(jreport3);

        jreport4.setText("Pengembalian");

        mpengembaliandata.setText("Data Pengembalian");
        mpengembaliandata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mpengembaliandataActionPerformed(evt);
            }
        });
        jreport4.add(mpengembaliandata);

        jMenuBar1.add(jreport4);

        jreport5.setText("Denda");

        mdenda.setText("Data Denda");
        mdenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdendaActionPerformed(evt);
            }
        });
        jreport5.add(mdenda);

        jMenuBar1.add(jreport5);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mbukudataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mbukudataActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Bukuperpus Bukuperpus= new Bukuperpus();
        Bukuperpus.setVisible(true);
    }//GEN-LAST:event_mbukudataActionPerformed

    private void jmenubackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmenubackActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jmenubackActionPerformed

    private void jbackmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbackmenuActionPerformed
        // TODO add your handling code here:
         dispose();

        MenuUtama MenuUtama= new MenuUtama();
        MenuUtama.setVisible(true);
    }//GEN-LAST:event_jbackmenuActionPerformed

    private void manggotadataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manggotadataActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Anggotaperpus Anggotaperpus= new Anggotaperpus();
        Anggotaperpus.setVisible(true);
    }//GEN-LAST:event_manggotadataActionPerformed

    private void mpegawaidataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mpegawaidataActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Pegawaiperpus Pegawaiperpus= new Pegawaiperpus();
        Pegawaiperpus.setVisible(true);
    }//GEN-LAST:event_mpegawaidataActionPerformed

    private void mpeminjamandataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mpeminjamandataActionPerformed
        // TODO add your handling code here:
         dispose();
        
        Peminjamanperpus Peminjamanperpus= new Peminjamanperpus();
        Peminjamanperpus.setVisible(true);
    }//GEN-LAST:event_mpeminjamandataActionPerformed

    private void mpengembaliandataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mpengembaliandataActionPerformed
        // TODO add your handling code here:
         dispose();
        
        Pengembalianperpus Pengembalianperpus= new Pengembalianperpus();
        Pengembalianperpus.setVisible(true);
    }//GEN-LAST:event_mpengembaliandataActionPerformed

    private void btndeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndeleteActionPerformed
        // TODO add your handling code here:
         int[] selectedRows = Tablebuku.getSelectedRows();

    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(null, "Pilih Data yang akan di Hapus");
        return;
    }

    // Buat koneksi dan statement di luar loop
    try {
        String sql = "DELETE FROM db_buku WHERE Kode_buku=?";
        PreparedStatement stat = koneksi.prepareStatement(sql);

        // Loop untuk menghapus semua data yang dipilih
        for (int i = 0; i < selectedRows.length; i++) {
            int rowIndex = selectedRows[i];
            String kodebuku = tabmode.getValueAt(rowIndex, 0).toString();
            stat.setString(1, kodebuku);

            // Eksekusi perintah untuk menghapus data
            stat.executeUpdate();
        }

        // Setelah semua baris dihapus, tampilkan pesan sekali saja
        JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");

        // Perbarui tampilan tabel setelah penghapusan
        databuku();
        kosong();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btndeleteActionPerformed

    private void TablebukuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablebukuMouseClicked
        // TODO add your handling code here:
        int bar = Tablebuku.getSelectedRow();
        String a = tabmode.getValueAt(bar, 0).toString();
        String b = tabmode.getValueAt(bar, 1).toString();
        String c = tabmode.getValueAt(bar, 2).toString();
        String d = tabmode.getValueAt(bar, 3).toString();
        String e = tabmode.getValueAt(bar, 4).toString();
        String f = tabmode.getValueAt(bar, 5).toString();

        tkodebuku.setText(a);
        tjudul.setText(b);
        tjumlah.setText(c);
        tpengarang.setText(d);
        tpenerbit.setText(e);
        tterbit.setText(f);
    }//GEN-LAST:event_TablebukuMouseClicked
private void setTableColumnAlignmentCenter() {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

    // Terapkan renderer ke semua kolom
    for (int i = 0; i < Tablebuku.getColumnCount(); i++) {
        Tablebuku.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}
    
    private void btneditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditActionPerformed
        // TODO add your handling code here:
        int rowIndex = Tablebuku.getSelectedRow();

        if (rowIndex != -1){
        } else {
            JOptionPane.showMessageDialog(null, "Pilih data yang akan di ubah");
            return;
        }
        try {
            Connection konn = DriverManager.getConnection("jdbc:mysql://localhost/db_login","root","");
            String kodebuku = tkodebuku.getText();
            String judul = tjudul.getText();
            String jumlah = tjumlah.getText();
            String pengarang = tpengarang.getText();
            String penerbit = tpenerbit.getText();
            String tahunterbit = tterbit.getText();
            String kodebukubaru = tkodebuku.getText();
            String kodebukulama = tabmode.getValueAt(rowIndex, 0).toString();

            if(kodebukubaru.equals(kodebukulama)){
            } else {
                JOptionPane.showMessageDialog(null, "Kode buku tidak dapat diubah.");
                return;
            }

            String sql = "UPDATE db_buku SET Kode_buku=?, Judul_buku=?, Jumlah_buku=?, Pengarang=?, Penerbit=?, Tahun_terbit=? WHERE Kode_buku=?";
            PreparedStatement ps = konn.prepareStatement(sql);
            ps.setString(1, kodebuku);
            ps.setString(2, judul);
            ps.setString(3, jumlah);
            ps.setString(4, pengarang);
            ps.setString(5, penerbit);
            ps.setString(6, tahunterbit);
            ps.setString(7, kodebuku);

            int result = ps.executeUpdate();
            if(result == 1){
                JOptionPane.showMessageDialog(null, "Data berhasil diubah");
                databuku();
                kosong();
            } else {
                JOptionPane.showMessageDialog(null, "Gagal Mengubah");
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
         databuku();
    JOptionPane.showMessageDialog(null, "Berhasil dibersihkan!");  
    }//GEN-LAST:event_btnclearActionPerformed

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed
        // TODO add your handling code here:
       try {
        // Mengecek jumlah data buku yang sudah ada di tabel
        int count = getDataCount();
        
        // Jika jumlah data sudah mencapai 500, tidak izinkan menyimpan
        if (count >= 500) {
            JOptionPane.showMessageDialog(null, "Jumlah data buku sudah mencapai 500, tidak bisa menambah lagi.");
            return;  // Keluar dari fungsi dan tidak lanjutkan simpan
        }

        // Jika jumlah data belum mencapai 500, lanjutkan menyimpan data
        String sql = "INSERT INTO db_buku (Kode_buku, Judul_buku, Jumlah_buku, Pengarang, Penerbit, Tahun_terbit) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ps.setString(1, tkodebuku.getText());
        ps.setString(2, tjudul.getText());
        ps.setString(3, tjumlah.getText());
        ps.setString(4, tpengarang.getText());
        ps.setString(5, tpenerbit.getText());
        ps.setString(6, tterbit.getText());

        int result = ps.executeUpdate();
        if (result == 1){
            JOptionPane.showMessageDialog(null, "Data berhasil di Simpan");
            databuku(); // Refresh tabel buku
            kosong(); // Kosongkan form input
        } else {
            JOptionPane.showMessageDialog(null, "Gagal Menyimpan Data");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
    }//GEN-LAST:event_btnsaveActionPerformed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        // TODO add your handling code here:
        String selectedItem = (String) cpencarian.getSelectedItem();
        String caribuku = tcaribuku.getText();

        Object[] Baris = {"Kode_buku", "Judul_buku", "Jumlah_buku", "Pengarang", "Penerbit", "Tahun_terbit"};
        tabmode = new DefaultTableModel(null, Baris);
        Tablebuku.setModel(tabmode);

        String sql = "SELECT * FROM db_buku WHERE Kode_buku LIKE ? OR Judul_buku LIKE ?";

        try {
            PreparedStatement stat = koneksi.prepareStatement(sql);
            stat.setString(1, "%" + caribuku + "%");
            stat.setString(2, "%" + caribuku + "%");

            ResultSet hasil = stat.executeQuery();
            while(hasil.next()){
                String a = hasil.getString("Kode_buku");
                String b = hasil.getString("Judul_buku");
                String c = hasil.getString("Jumlah_buku");
                String d = hasil.getString("Pengarang");
                String e = hasil.getString("Penerbit");
                String f = hasil.getString("Tahun_terbit");

                String[] data={a, b, c, d, e, f};
                tabmode.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erorr : " + e.getMessage());
        }
    }//GEN-LAST:event_btncariActionPerformed

    private void tcaribukuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcaribukuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcaribukuActionPerformed

    private void mdendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdendaActionPerformed
        // TODO add your handling code here:
        dispose();
        
        Dendaperpus Dendaperpus= new Dendaperpus();
        Dendaperpus.setVisible(true);
    }//GEN-LAST:event_mdendaActionPerformed

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
        // TODO add your handling code here:
           try {
        String namaFile = "src/Laporan/LaporanBuku.jasper";
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bukuperpus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formlogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tablebuku;
    private javax.swing.JButton btncari;
    private javax.swing.JButton btnclear;
    private javax.swing.JButton btndelete;
    private javax.swing.JButton btnedit;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnsave;
    private javax.swing.JComboBox<String> cpencarian;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem jbackmenu;
    private javax.swing.JMenu jmenuback;
    private javax.swing.JMenu jreport;
    private javax.swing.JMenu jreport1;
    private javax.swing.JMenu jreport2;
    private javax.swing.JMenu jreport3;
    private javax.swing.JMenu jreport4;
    private javax.swing.JMenu jreport5;
    private javax.swing.JMenuItem manggotadata;
    private javax.swing.JMenuItem mbukudata;
    private javax.swing.JMenuItem mdenda;
    private javax.swing.JMenuItem mpegawaidata;
    private javax.swing.JMenuItem mpeminjamandata;
    private javax.swing.JMenuItem mpengembaliandata;
    private javax.swing.JTextField tcaribuku;
    private javax.swing.JTextField tjudul;
    private javax.swing.JTextField tjumlah;
    private javax.swing.JTextField tkodebuku;
    private javax.swing.JTextField tpenerbit;
    private javax.swing.JTextField tpengarang;
    private javax.swing.JTextField tterbit;
    // End of variables declaration//GEN-END:variables

    private void aktif(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  

}
