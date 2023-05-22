/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Anatasya
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import sun.applet.Main;
import koneksi.koneksi;


public class Sewa extends javax.swing.JFrame {

        koneksi k;
        Statement statement;
        ResultSet resultSet;
        String idJual, id_barang, ubah;
        int count, jml_awal;
        double jml_total;
    
    public Sewa() {
        initComponents();
        k = new koneksi();
        tampilInfo();
        tampilJual();
        kondisiAwal();
    }
    
    private void clear() {
        tfIdBarang.setText("");
        tfHarga.setText("");
        Hapus.setText("");
    }

    private void kondisiAwal () {
        btnTambah.setEnabled(false);
        //gridJual ("") ;
        tfIdBarang.setEnabled(false); 
        tfHarga.setEnabled(false);
        tfTrans.setEnabled(false);
        Tanggal.setEnabled(false);
        btnTrans.setEnabled(true);
        Hapus.setEnabled(false);
        lblTotal.setEnabled(false);
        btnHapus.setEnabled(false);
        idJual = "";
        lblTotal.setText("0");
        Total.setText("0");
        clear();
            }

        private void kondisiAktif() {
        btnTambah.setEnabled(true);
        tfIdBarang.setEnabled(true);
        tfHarga.setEnabled(true);
        tfTrans.setEnabled(true);
        Tanggal.setEnabled(true);
        btnTrans.setEnabled(false);
        Hapus.setEnabled(false);
        lblTotal.setEnabled(false);
        Total.setEnabled(false);
        btnHapus.setEnabled(true);
        clear();
        }
        
    private void tampilInfo(){
        Object header [] = {"ID Barang", "Nama Barang", "Harga", "Stok"};
        DefaultTableModel defaultTable = new DefaultTableModel(null, header); 
        gridTampil.setModel(defaultTable);

        int baris = gridTampil.getRowCount();
        for (int i = 0; i < baris; i++) {
        defaultTable.removeRow(i);
        }

        String query = "select id_brg, nm_brg, stok from barang where stok > 0"; 
        try {
        statement = k.con.createStatement(); 
        resultSet = statement.executeQuery (query);
        while (resultSet.next()){
        String idBarang = resultSet.getString(1); 
        String nmBarang = resultSet.getString (2); 
        String stok = resultSet.getString(3);
        String kolom [] = {idBarang, nmBarang, 
        stok}; 
        defaultTable.addRow(kolom);
        }
        } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
        Logger.getLogger (Sewa.class.getName()).log (Level.SEVERE, null, ex);
        }
    }
    
    private String idTransaksi () {
        try {
            Date date= new Date();
            SimpleDateFormat dateformat=new SimpleDateFormat ("yyyy-MM-dd");
            String tanggal=dateformat. format (date);
            Tanggal.setDate(date);

            SimpleDateFormat dateformat2=new SimpleDateFormat ("dd. MM. yyyy hh:mm:ss"); 
            String tanggal2=dateformat2. format (date);
            String queryTanggal = "select waktu, id_jual from penjualan"; 
            resultSet = statement.executeQuery (queryTanggal);
            idJual = "Trans." + tanggal2;
            String query = "insert into penjualan values ('" + idJual + "','" + tanggal + "','ADMIN',0,0, 'NOTHING')"; 
            statement.executeUpdate (query);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return idJual;
    }
    
    
    private void tampilJual () {
            Object header [] = {"ID Transaksi", "ID Barang", "Nama Barang", "Jumlah", "Sub Total"};
            DefaultTableModel defaultTable = new DefaultTableModel (null, header);
            gridJual.setModel (defaultTable);

            int baris = gridJual.getRowCount();
            for (int i = 0; i < baris; i++) {
            defaultTable.removeRow(i);

            String query="SELECT * from transaksi JOIN barang where " 
                    + "penjualan.id_barang = barang.id_brg";

        try{
            statement = k.con.createStatement();
            resultSet = statement.executeQuery (query);

        while (resultSet.next()) {
            String idTransaksi = resultSet.getString(1);//kolom id_t_jual_barang dari t_jual_barang 
            String idBarang = resultSet.getString (7); // kolom id_barang dari t_barang 
            String nmBarang = resultSet.getString (8);// kolom nm_barang dari t_barang 
            String jml = resultSet.getString (4); // kolom jumlah dari t_jual_barang 
            String subTotal = resultSet.getString (6); // kolom subTotal dari t_jual_barang
            String kolom [] = {idTransaksi, idBarang, nmBarang, jml, subTotal}; 
            defaultTable.addRow(kolom);
            }
        }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                    System.out.println("" + ex.getMessage());
                    Logger.getLogger(Sewa.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }
    }
    
    private void tambahTransaksi () {

        double subTotal, harga, jumlah;
        jumlah = Integer.parseInt(tfJumlah.getText()); 
        harga = Double.parseDouble (tfHarga.getText());
        subTotal =  10000 * jumlah;
        try {
        String query = "INSERT INTO t_jual_barang "
        + "VALUES ('" + idJual + "','"+idJual+"','"
        + "','" + tfJumlah.getText()
        + "','"+ tfHarga.getText()
        + "','" + subTotal + "')";
        statement =k.con.createStatement ();
        statement.executeUpdate (query);
        while (resultSet.next()) {
        } 
        } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
        System.out.println("" + e.getMessage());
        }
    }
    
    
    private void total_transaksi () {
        if (!id_barang.equals("")) {
        String total = "SELECT sum (sub_total) from transaksi"; 
        try {
            statement=k.con.createStatement();
            resultSet = statement.executeQuery (total);
            while (resultSet.next()) {
            lblTotal.setText (resultSet.getString(1));
            } 
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("" + ex.getMessage());
            Logger.getLogger (Sewa.class.getName()).log (Level.SEVERE, null, ex);
        }
        } else{
            lblTotal.setText("");
        }
    } 
    
        private void total_unit() {
        if (!id_barang.equals("")) {
        String total = "SELECT sum (jumlah) from transaksi"; 
        try {
            statement=k.con.createStatement();
            resultSet = statement.executeQuery (total);
            while (resultSet.next()) {
            lblTotal.setText (resultSet.getString(1));
            } 
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("" + ex.getMessage());
            Logger.getLogger (Sewa.class.getName()).log (Level.SEVERE, null, ex);
        }
        } else{
            lblTotal.setText("");
        }
    }  
      
    private void updateTotalTransaksi(){
        String query = "UPDATE penjualan SET "
                + "jumlah = '" + Total.getText() + "', "
                + "total = '" + Total + "'"
                + "WHERE id_jual = '" + idJual + "'";
        
        try{
            statement=k.con.createStatement();
            resultSet = statement.executeQuery (query);
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("" + ex.getMessage());
            Logger.getLogger (Sewa.class.getName()).log (Level.SEVERE, null, ex);
        }
    }
    
    private int cekStok(){
        int stok = 0;
        String total = "SELECT stok from barang where id_brg='" + id_barang + "'";
        try{
            statement=k.con.createStatement();
            resultSet = statement.executeQuery (total);
            
            while(resultSet.next()){
                stok = Integer.parseInt(resultSet.getString(1));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("" + ex.getMessage());
        }
        return stok;
    }
    
    private void UpdateStok(String ubah){
        int stokBaru = 0;
        int total;
        
        try{
            if(ubah.equals("tambah")){
                stokBaru = cekStok() - Integer.parseInt(tfJumlah.getText());
            }else if(ubah.equals("delete")){
                stokBaru = cekStok() + jml_awal;
                total = Integer.parseInt(Total.getText()) - jml_awal;
            }
            String query = "UPDATE barang set stok = '" + stokBaru + "' where id_brg= '" + id_barang + "'";
            statement.executeUpdate(query);
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.out.println("" + ex.getMessage());
        }
    }
    
    public void delete(String id){
        String query = "DELETE form transaksi where id_trans = '" + id + "'";
        try{
            statement=k.con.createStatement();
            resultSet = statement.executeQuery (query);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Data masih digunakan");
            System.out.println("" + ex.getMessage());
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
        lbdata = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tfTrans = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Tanggal = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        tfJumlah = new javax.swing.JTextField();
        tfIdBarang = new javax.swing.JTextField();
        tfHarga = new javax.swing.JTextField();
        Hapus = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        gridTampil = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        gridJual = new javax.swing.JTable();
        Total = new javax.swing.JTextField();
        btnKembali = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        btnTrans = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbdata.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbdata.setText("Data Transaksi");

        jLabel1.setText("Id Transaksi");

        tfTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTransActionPerformed(evt);
            }
        });

        jLabel2.setText("Tanggal");

        jLabel3.setText("Jumlah Barang");

        tfJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfJumlahActionPerformed(evt);
            }
        });
        tfJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfJumlahKeyPressed(evt);
            }
        });

        gridTampil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        gridTampil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gridTampilMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(gridTampil);

        gridJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        gridJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gridJualMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(gridJual);

        Total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalActionPerformed(evt);
            }
        });

        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotal.setText("0");

        btnTrans.setText("Transaksi Baru");
        btnTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransActionPerformed(evt);
            }
        });

        jLabel5.setText("ID Barang");

        jLabel6.setText("Harga");

        jLabel7.setText("Total Unit");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Data Jual");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Data Barang");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnKembali)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnHapus))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnTrans))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tfJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2)
                                            .addComponent(tfTrans, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(191, 191, 191)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(tfIdBarang)
                                                        .addComponent(tfHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(277, 277, 277)
                        .addComponent(lbdata)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbdata)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfTrans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfIdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(3, 3, 3)
                        .addComponent(tfHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTrans))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnKembali)
                    .addComponent(btnHapus))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tfTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTransActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTransActionPerformed

    private void gridTampilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gridTampilMouseClicked
        // TODO add your handling code here:
        id_barang = gridTampil.getValueAt(gridTampil.getSelectedRow(),0).toString();
        String harga = gridTampil.getValueAt(gridTampil.getSelectedRow(),2).toString();
        String nmBarang = gridTampil.getValueAt(gridTampil.getSelectedRow(), 1).toString();
        tfIdBarang.setText(id_barang);
        tfHarga.setText(harga);
    }//GEN-LAST:event_gridTampilMouseClicked

    private void gridJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gridJualMouseClicked
        // TODO add your handling code here:
        Hapus.setText(gridJual.getValueAt(gridJual.getSelectedRow(), 1).toString());
        jml_total = Double.valueOf(gridJual.getValueAt(gridJual.getSelectedRow(), 3).toString());
        jml_awal = Integer.valueOf(gridJual.getValueAt(gridJual.getSelectedRow(), 4).toString());
    }//GEN-LAST:event_gridJualMouseClicked

    private void tfJumlahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfJumlahKeyPressed
        // TODO add your handling code here:
        ubah = "tambah";
        if(evt.getKeyChar() == '\n'){
            if(Integer.parseInt(tfJumlah.getText()) <= cekStok()){
                idTransaksi();
                total_transaksi();
                UpdateStok(ubah);
                updateTotalTransaksi();
            }
        }
    }//GEN-LAST:event_tfJumlahKeyPressed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        String id;
        int xRow, xPilih, xCol;
        xRow = gridJual.getSelectedRow();
        xCol = gridJual.getSelectedColumn();
        id = gridJual.getValueAt (xRow, 0).toString();
        ubah="delete";

        Object[] pilihan = {"Tidak!","Iya, sangat yakin."};
        if (Hapus.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "Pilih data yang akan dihapus");
        } else{
        xPilih = JOptionPane.showOptionDialog(null,
        "Apakah benar anda ingin menghapus Data Transaksi ini: "+ "\n" + "\t\t"+"ID Transaksi :"+id+"\n"+
        "\t\t"+"Nama Barang :"+gridJual.getValueAt (xRow, 2).toString(), "Konfirmasi",
        JOptionPane. YES_NO_OPTION,
        JOptionPane. QUESTION_MESSAGE,
        null,
        pilihan,
        pilihan [0]);
        
        if(xPilih == 1){
            delete(id);
            UpdateStok(ubah);
            Hapus.setText("");
            tampilInfo();
            tampilJual();
            total_transaksi();
            total_unit();
            btnTrans.setVisible(true);
        }else {
            Hapus.setText("");
        }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        btnTrans.setEnabled(true);
        btnTambah.setEnabled(false);
        ubah = "tambah";
        if(Integer.parseInt(tfJumlah.getText()) <= cekStok()){
            tambahTransaksi();
            UpdateStok(ubah);
            tampilInfo();
            tampilJual();
            clear();
            total_transaksi();
            updateTotalTransaksi();
            total_unit();
        }else{
            JOptionPane.showMessageDialog(null, "Stok barang kurang");
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransActionPerformed
        // TODO add your handling code here:
        kondisiAktif();
        tfTrans.setText(idTransaksi());
    }//GEN-LAST:event_btnTransActionPerformed

    private void tfJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfJumlahActionPerformed

    private void TotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalActionPerformed

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
            java.util.logging.Logger.getLogger(Sewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sewa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sewa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Hapus;
    private com.toedter.calendar.JDateChooser Tanggal;
    private javax.swing.JTextField Total;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTrans;
    private javax.swing.JTable gridJual;
    private javax.swing.JTable gridTampil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbdata;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField tfHarga;
    private javax.swing.JTextField tfIdBarang;
    private javax.swing.JTextField tfJumlah;
    private javax.swing.JTextField tfTrans;
    // End of variables declaration//GEN-END:variables
}
