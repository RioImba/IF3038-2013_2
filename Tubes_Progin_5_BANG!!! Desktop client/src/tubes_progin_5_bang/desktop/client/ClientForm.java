package tubes_progin_5_bang.desktop.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ClientForm extends javax.swing.JFrame {
    String username;
    Socket echoSocket = null;
    String serverHostname = "127.0.0.1";
    Object[][] data; //awalnya ga d final
    
    int jumlah_task = 0; 

    boolean[] statuslist;
    
        public void setUsername(String user){
        this.username = user;
        jLabel1.setText("Welcome to BANG!!!, " + username);
    }
    
    public void setEchoSocket (Socket echoSocket) {
        this.echoSocket = echoSocket;
        System.out.println("Echo Socket : " + echoSocket);
        updateTable();
    }
    
    public void updateTable() {
        try {
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            
            System.out.println("get task list : username = " + username);
            out.println("getlist;"+username);
            
            String response = in.readLine();
            
            String[] task_list = response.split("@");
            jumlah_task = task_list.length;
            
            String[] columnNames = new String[6];
            columnNames[0] = "Nama Tugas";
            columnNames[1] = "Deadline";
            columnNames[2] = "Daftar Assignee";
            columnNames[3] = "Tag";
            columnNames[4] = "Status";
            columnNames[5] = "Kategori";
            
            //banyaknya row tergantung dr kiriman server, assign smua nilai ke data[][]
            data = new Object[jumlah_task][6];
            statuslist = new boolean[jumlah_task];
//            for (int i = 0; i < jumlah_task; i++) {
//                //data[i][0] = new String();
//                data[i][4] = new Boolean(false);
//                
//            }
            
            for (int j = 0; j < task_list.length; j++) {
                System.out.println(task_list[j]);
                String[] task = task_list[j].split(";");

                System.out.println("Task id : " + task[1]);
                System.out.println("Task name : " + task[2]);
                String[] assignee = task[3].split("#");
                System.out.println("Assignee : ");
                for (int i = 0; i < assignee.length; i++) {
                    System.out.println("  - " + assignee[i]);
                }

                String[] tag = task[4].split("#");
                System.out.println("Tag : ");
                for (int i = 0; i < tag.length; i++) {
                    System.out.println("  - " + tag[i]);
                }

                System.out.println("Category name : " + task[5]);
                System.out.println("Status : " + task[6]);
                System.out.println("Deadline : " + task[7]);
                System.out.println("");
                
                data[j][0] = task[2];
                data[j][1] = task[7]; //deadline
                data[j][2] = task[3].replaceAll("#",","); //assignee
                data[j][3] = task[4].replaceAll("#",","); //tag
                if ("1".equals(task[6])) //status
                    data[j][4] = new Boolean(true);
                else 
                    data[j][4] = new Boolean(false);
                statuslist[j] = (boolean) data[j][4];
                data[j][5] = task[5]; //kategori
            } 
            
            jTable1.setModel(new DefaultTableModel(data, columnNames) {
                Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false, false, false, false, true, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTable1);

        }
        catch (IOException e) {
            System.out.println("Can't connect to server");
        }
    }
    
    public ClientForm() {
        initComponents();

//        String[] columnNames = new String[6];
//        columnNames[0] = "Nama Tugas";
//        columnNames[1] = "Deadline";
//        columnNames[2] = "Daftar Assignee";
//        columnNames[3] = "Tag";
//        columnNames[4] = "Status";
//        columnNames[5] = "Kategori";
        
//        //banyaknya row tergantung dr kiriman server, assign smua nilai ke data[][]
//        data = new Object[4][6];
//        statuslist = new boolean[4];
//        for (int i = 0; i < 4; i++) {
//            data[i][4] = new Boolean(false);
//            statuslist[i] = false;
//        }
//        
//        jTable1.setModel(new DefaultTableModel(data, columnNames) {
//            Class[] types = new Class[]{
//                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
//            };
//            boolean[] canEdit = new boolean[]{
//                false, false, false, false, true, false
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//
//            public boolean isCellEditable(int rowIndex, int columnIndex) {
//                return canEdit[columnIndex];
//            }
//        });
//        jScrollPane1.setViewportView(jTable1);

//        TableModel model = jTable1.getModel();
//        int row_count = model.getRowCount();
//        final boolean[] check_list = new boolean[row_count]; //awalny ga d final
//        for(int i = 0; i < row_count; i++){
//            check_list[i] = (boolean) data[i][4];
//            System.out.println("check list = "+check_list[i]);
//        }

        //selama program masi jalan, maka:
        jTable1.getModel().addTableModelListener(new TableModelListener(){

            @Override
            public void tableChanged(TableModelEvent e) {
                System.out.println("value Changed");
                TableModel model = jTable1.getModel();
                //model.setValueAt("Sharon", 0, 0);
                String[] timestamp_container = new String[model.getRowCount()];

                boolean[] check_list = new boolean[model.getRowCount()]; //awalny ga d final
                for (int i = 0; i < model.getRowCount(); i++) {
                    check_list[i] = statuslist[i];
                }

                for (int i = 0; i < model.getRowCount(); i++) {
                    statuslist[i] = (boolean) model.getValueAt(i, 4);
                    if (check_list[i] != (boolean) model.getValueAt(i, 4)) {
                        timestamp_container[i] = getTimestamp();
                    }
                }

                try {
                    String log_container = readFromFile("LogFile.txt");
                    writeLogFile(jTable1, timestamp_container, log_container);
                } catch (IOException ex) {
                    Logger.getLogger(ClientForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));
        jPanel1.setForeground(new java.awt.Color(255, 102, 0));

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setText("Welcome to BANG!!!");

        jButton1.setText("Sync Now");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Nama Tugas", "Deadline", "Daftar Assignee", "Tag", "Status", "Kategori"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setText("Log Out");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        updateTable();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        Date current = new Date();
        String timestamp = sdf.format(current);
        return timestamp;
    }

    public String readFromFile(String file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                    line = br.readLine();
                }
                String log_container = sb.toString();
                return log_container;
            } finally {
                br.close();
            }
        }catch(FileNotFoundException ex){
            System.out.println("File not found");
            return "";
        }catch(IOException ex){
            System.out.println("Cant read from file");
            return "";
        }
    }

//    public void writeToBeUpdated(javax.swing.JTable Table, String[] timestamp) throws IOException {
//        //error krn belom smua bs msk.. cmn yg trakhir aj yg ktulis
//        TableModel model = Table.getModel();
//        FileWriter fw = new FileWriter("ToBeUpdated.txt");
//        PrintWriter pw = new PrintWriter(fw);
//        for (int i = 0; i < model.getRowCount(); i++) {
//            if (timestamp[i] != null) {
//                pw.print("ID Task");
//                pw.print(";");
//                pw.print(model.getValueAt(i, 4));
//                System.out.println("Value at row " + i + " = " + model.getValueAt(i, 4));
//                pw.print(";");
//                pw.print(timestamp[i]);
//                pw.print(";");
//                pw.println();
//                pw.flush();
//            }
//        }
//        pw.close();
//        fw.close();
//    }

    public void writeLogFile(javax.swing.JTable Table, String[] timestamp, String log_container) throws IOException {
        //error krn belom smua bs msk.. cmn yg trakhir aj yg ktulis
        TableModel model = Table.getModel();
        File F = new File("LogFile.txt");
        FileWriter fw = new FileWriter(F.getAbsolutePath());
        PrintWriter pw = new PrintWriter(fw);
        pw.print(log_container);
        for (int i = 0; i < model.getRowCount(); i++) {
            if(timestamp[i] != null){
                pw.print("ID Task");
                pw.print(";");
                pw.print(model.getValueAt(i, 4));
                System.out.println("Value at row " + i + " = " + model.getValueAt(i, 4));
                pw.print(";");
                pw.print(timestamp[i]);
                pw.print(";");
                pw.println();
                pw.flush();
            }
        }
        pw.close();
        fw.close();
    }

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
            java.util.logging.Logger.getLogger(ClientForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new ClientForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
