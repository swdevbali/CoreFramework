package smsgateway.commport;

import core.system.Startup;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import smsgateway.ATCommand;

public class DaftarPort extends javax.swing.JDialog {
    private static DaftarPort singleton = null;
    public static DaftarPort getSingleton() {
        if (singleton == null) {
            singleton = new DaftarPort(Startup.getInstance().getMainFrame(), true, ATCommand.getSingleton());
        }
        return singleton;
    }   
    ManajerDataPort manData = new ManajerDataPort(); 
    ATCommand at;
    private String[] activedConnData;
    private String[][] allConnData;
    public String newConnName;
    private String msgConfDelete = "Anda yakin untuk menghapus data";
    private String msgActConn = "Modem sudah aktif";
    private String[] titleMessage = {"Informasi", "Konfirmasi"};
    private String msgInfoEdit = "Tidak bisa diubah modem sedang aktif";
    private String msgInfoDelete = "Tidak bisa dihapus modem sedang aktif";
    private String msgNotSlc = "Modem belum dipilih";
    private String msgCommit = "Koneksi modem berhasil";
    private String msgVirPort = "Koneksi gagal, port virtual tidak dikenal";
    private String msqAlreadyAct = "Koneksi gagal, port sedang digunakan aplikasi lain";
    private String msqDevNotFound = "Koneksi gagal, piranti modem tidak dikenal";
    public boolean activedConn = false;

    public void diskonek() {
        // TODO: piye?
        activedConn = false;
    }

    public boolean isActivedConn() {
        return activedConn;
    }

    public void setActivedConn(boolean activedConn) {
        this.activedConn = activedConn;
    }

    private DaftarPort(java.awt.Frame parent, boolean modal, ATCommand at) {
        super(parent, modal);
        try {
            UIManager.setLookAndFeel(Startup.getInstance().getLookAndFeel());
            this.at = at;
            initComponents();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DaftarPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DaftarPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DaftarPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(DaftarPort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showForm() {
        setVisible(true);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        remConnButton = new javax.swing.JButton();
        editConnButton = new javax.swing.JButton();
        addConnButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstConnectionName = new javax.swing.JList();
        doneButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Koneksi");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                onWindowActivated(evt);
            }
        });

        mainPanel.setOpaque(false);

        cancelButton.setMnemonic('t');
        cancelButton.setText("Tutup");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setOpaque(false);

        remConnButton.setText("Hapus");
        remConnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemove(evt);
            }
        });

        editConnButton.setText("Ubah");
        editConnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEdit(evt);
            }
        });

        addConnButton.setText("Tambah");
        addConnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdd(evt);
            }
        });

        lstConnectionName.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstConnectionName.setSelectedIndex(0);
        jScrollPane1.setViewportView(lstConnectionName);

        doneButton.setMnemonic('p');
        doneButton.setText("Konek");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDone(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(remConnButton, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(editConnButton, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(addConnButton, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(doneButton, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addConnButton, editConnButton, remConnButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addConnButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editConnButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remConnButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(doneButton)))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleParent(this);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents
    private void onDone(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDone
        if (lstConnectionName.getSelectedIndex() != -1) {
            String selectedList = ((String) lstConnectionName.getSelectedValue()).replace("<html><body style=color:#FF0000;"
                    + "font-weight:bold'>", "").replace("</body></html>", "");
            String oldAct = at.actConn;
            ATCommand.getSingleton().setThreadOn(false);
            if (at.actConn == null || !at.actConn.equals(selectedList)) {
                at.closePort();
                at.installModem(manData.getSelectedConnData(selectedList));
                if (at.INIT_STATUS != 0) { // gagal;
                    at.actConn = oldAct;
                    if (at.INIT_STATUS == 1) {
                        JOptionPane.showMessageDialog(this, msgVirPort,
                                titleMessage[0], 2);
                    } else if (at.INIT_STATUS == 2) {
                        JOptionPane.showMessageDialog(this, msqAlreadyAct,
                                titleMessage[0], 2);
                    } else {
                        JOptionPane.showMessageDialog(this, msqDevNotFound,
                                titleMessage[0], 2);
                    }
                } else {
                    ATCommand.getSingleton().setThreadOn(true);
                    manData.setActivedConn(at.actConn);
                    activedConn = true;
                    dispose();
                    JOptionPane.showMessageDialog(this, msgCommit,
                            titleMessage[0], 2);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, msgActConn,
                    titleMessage[0], 2);
        }
    }//GEN-LAST:event_onDone

    private void onWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowActivated
        try {
            allConnData = manData.doViewConnData();
            activedConnData = manData.getActivedConnData();
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
        setListValue();
        setEnabledButton();
    }//GEN-LAST:event_onWindowActivated

    private void setListValue() {
        final String[] connNames = new String[allConnData.length];
        for (int i = 0; i < connNames.length; i++) {
            if (allConnData[i][0].equals(at.actConn)) {
                connNames[i] = "<html><body style=color:#FF0000;font-weight:bold'>" + allConnData[i][0] + "</body></html>";
            } else {
                connNames[i] = allConnData[i][0];
            }
        }
        lstConnectionName.setModel(new javax.swing.AbstractListModel() {

            String[] strings = connNames;

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
    }

    private void setEnabledButton() {
        if (allConnData.length == 0) {//jika kosong
            remConnButton.setEnabled(false);
            doneButton.setEnabled(false);
            editConnButton.setEnabled(false);
        } else {
            remConnButton.setEnabled(true);
            doneButton.setEnabled(true);
            editConnButton.setEnabled(true);
        }
    }

    private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
        dispose();
    }//GEN-LAST:event_onCancel

    private void onAdd(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAdd
        DialogSetupPort.getSingleton().showForm();
    }//GEN-LAST:event_onAdd

    private void onEdit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEdit
        if (lstConnectionName.getSelectedIndex() > -1) {
            String selectedList = ((String) lstConnectionName.getSelectedValue()).replace("<html><body style=color:#FF0000;"
                    + "font-weight:bold'>", "").replace("</body></html>", "");
            try {
                if (!at.actConn.equals(selectedList)) {
                    DialogSetupPort.getSingleton().showForm(allConnData[lstConnectionName.getSelectedIndex()]);
                } else {
                    JOptionPane.showMessageDialog(this, msgInfoEdit, titleMessage[0], 2);
                }
            } catch (NullPointerException ex) {
                DialogSetupPort.getSingleton().showForm(allConnData[lstConnectionName.getSelectedIndex()]);
            }
        } else {
            JOptionPane.showMessageDialog(this, msgNotSlc, titleMessage[0], 2);
        }
    }//GEN-LAST:event_onEdit

    private void onRemove(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemove
        if (lstConnectionName.getSelectedIndex() > -1) {
            String selectedList = ((String) lstConnectionName.getSelectedValue()).replace("<html><body style=color:#FF0000;"
                    + "font-weight:bold'>", "").replace("</body></html>", "");
            try {
                if (!at.actConn.equals(selectedList)) {
                    int confirm = JOptionPane.showConfirmDialog(this, msgConfDelete, titleMessage[1], JOptionPane.YES_NO_OPTION, 3);
                    if (confirm == JOptionPane.YES_OPTION) {
                        manData.deleteConn(selectedList);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, msgInfoDelete, titleMessage[0], 2);
                }
            } catch (NullPointerException ex) {
                int confirm = JOptionPane.showConfirmDialog(this, msgConfDelete, titleMessage[1], JOptionPane.YES_NO_OPTION, 3);
                if (confirm == JOptionPane.YES_OPTION) {
                    manData.deleteConn(selectedList);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, msgNotSlc, titleMessage[0], 2);
        }
    }//GEN-LAST:event_onRemove
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addConnButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton doneButton;
    private javax.swing.JButton editConnButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstConnectionName;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton remConnButton;
    // End of variables declaration//GEN-END:variables
}
