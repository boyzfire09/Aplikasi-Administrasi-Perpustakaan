/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugasperpus;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author firzi
 */
public class koneksi
{
 Connection koneksi=null;
 public static Connection koneksiDb()
 {
     try
     {
         Class.forName("com.mysql.jdbc.Driver");
         Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost/db_login","root","");
         return koneksi;
     }
     catch(Exception e)
     {
         JOptionPane.showMessageDialog(null, e);
         return null;
     }
 }

    public Connection koneksiDB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Connection connect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
