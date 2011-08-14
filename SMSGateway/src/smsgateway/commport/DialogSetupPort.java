package smsgateway.commport;

import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import core.system.Startup;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import smsgateway.ATCommand;

public class DialogSetupPort extends javax.swing.JDialog {
    private static DialogSetupPort singleton = null;
    public static DialogSetupPort getSingleton() {
        if (singleton == null) {
            singleton = new DialogSetupPort(Startup.getInstance().getMainFrame(), true);
        }
        return singleton;
    }

    ManajerDataPort manSys = new ManajerDataPort();
    private String[] comPortNames;
    private String[] selectedConndata;
    private String titleMessage = "Konfirmasi";
    private String msgVoidConn = "Nama koneksi tidak boleh kosong";
    private String msgDupConn = "Nama koneksi sudah ada";

    private DialogSetupPort(Frame parent, boolean modal) {// ini adalah overloading
        super(parent, modal);
        try {
            UIManager.setLookAndFeel(Startup.getInstance().getLookAndFeel());
            initComponents();
            comPortNames = ATCommand.getSingleton().getPortName(); // -- dipanggil sekali sj si ATCommand
            portNameCombo.setModel(new javax.swing.DefaultComboBoxModel(comPortNames));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DialogSetupPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DialogSetupPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DialogSetupPort.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(DialogSetupPort.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showForm() {
        setCardLayout();
        setVisible(true);
    }

    public void showForm(String[] data) {
        selectedConndata = data;
        setCardLayout();
        setVisible(true);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        framePanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        addConnFirstPanel = new javax.swing.JPanel();
        connName = new javax.swing.JLabel();
        portNameLabel = new javax.swing.JLabel();
        connNameTField = new javax.swing.JTextField();
        portNameCombo = new javax.swing.JComboBox(new String[]{"test","Tetrert"});
        connName1 = new javax.swing.JLabel();
        optOutboxAll = new javax.swing.JRadioButton();
        optOutboxSpesifik = new javax.swing.JRadioButton();
        txtOutboxSpesifik = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        connName2 = new javax.swing.JLabel();
        cboSeriHP = new core.dataview.JCombBoxEx();
        addConnSecondPanel = new javax.swing.JPanel();
        bpsLabel = new javax.swing.JLabel();
        dataBitsLabel = new javax.swing.JLabel();
        parityLabel = new javax.swing.JLabel();
        stopBitsLabel = new javax.swing.JLabel();
        flowControlLabel = new javax.swing.JLabel();
        bpsCombo = new javax.swing.JComboBox();
        dataBitsCombo = new javax.swing.JComboBox();
        parityCombo = new javax.swing.JComboBox();
        stopBitsCombo = new javax.swing.JComboBox();
        flowControlCombo = new javax.swing.JComboBox();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Setting Koneksi");
        setFocusCycleRoot(false);
        setFocusable(false);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                onWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        framePanel.setOpaque(false);

        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.CardLayout());

        addConnFirstPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addConnFirstPanel.setOpaque(false);

        connName.setFont(new java.awt.Font("Arial", 0, 11));
        connName.setText("Nama koneksi");

        portNameLabel.setFont(new java.awt.Font("Arial", 0, 11));
        portNameLabel.setText("Pilih port");

        portNameCombo.setFont(new java.awt.Font("Arial Black", 0, 11));

        connName1.setFont(new java.awt.Font("Arial", 0, 11));
        connName1.setText("Format pesan outbox");

        buttonGroup1.add(optOutboxAll);
        optOutboxAll.setSelected(true);
        optOutboxAll.setText("All");
        optOutboxAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optOutboxAllActionPerformed(evt);
            }
        });

        buttonGroup1.add(optOutboxSpesifik);
        optOutboxSpesifik.setText("Spesifik");
        optOutboxSpesifik.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optOutboxSpesifikActionPerformed(evt);
            }
        });

        jLabel1.setText("Pisahkan tiap format dgn tanda koma");

        connName2.setFont(new java.awt.Font("Arial", 0, 11));
        connName2.setText("Seri HP");

        cboSeriHP.setSqlSelect("select IdSeriHP,seri from seriHP order by seri");

        javax.swing.GroupLayout addConnFirstPanelLayout = new javax.swing.GroupLayout(addConnFirstPanel);
        addConnFirstPanel.setLayout(addConnFirstPanelLayout);
        addConnFirstPanelLayout.setHorizontalGroup(
            addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConnFirstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addConnFirstPanelLayout.createSequentialGroup()
                        .addComponent(connName1)
                        .addContainerGap(176, Short.MAX_VALUE))
                    .addGroup(addConnFirstPanelLayout.createSequentialGroup()
                        .addComponent(optOutboxAll)
                        .addContainerGap(241, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addConnFirstPanelLayout.createSequentialGroup()
                        .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addConnFirstPanelLayout.createSequentialGroup()
                                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(connName)
                                    .addComponent(portNameLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(connNameTField, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addComponent(portNameCombo, 0, 141, Short.MAX_VALUE)
                                    .addComponent(cboSeriHP, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                                .addGap(34, 34, 34))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addConnFirstPanelLayout.createSequentialGroup()
                                .addComponent(optOutboxSpesifik)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(txtOutboxSpesifik, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))))
                        .addGap(68, 68, 68))
                    .addComponent(connName2)))
        );
        addConnFirstPanelLayout.setVerticalGroup(
            addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConnFirstPanelLayout.createSequentialGroup()
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(connName2)
                    .addComponent(cboSeriHP, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connName)
                    .addComponent(connNameTField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portNameLabel)
                    .addComponent(portNameCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(connName1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(optOutboxAll)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addConnFirstPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optOutboxSpesifik)
                    .addComponent(txtOutboxSpesifik, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        mainPanel.add(addConnFirstPanel, "card2");

        addConnSecondPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addConnSecondPanel.setOpaque(false);

        bpsLabel.setFont(new java.awt.Font("Arial", 0, 11));
        bpsLabel.setText("Bit per second");

        dataBitsLabel.setFont(new java.awt.Font("Arial", 0, 11));
        dataBitsLabel.setText("Data bits");

        parityLabel.setFont(new java.awt.Font("Arial", 0, 11));
        parityLabel.setText("Parity");

        stopBitsLabel.setFont(new java.awt.Font("Arial", 0, 11));
        stopBitsLabel.setText("Stop Bits");

        flowControlLabel.setFont(new java.awt.Font("Arial", 0, 11));
        flowControlLabel.setText("Flow Control");

        bpsCombo.setFont(new java.awt.Font("Arial Black", 0, 11));
        bpsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "110", "300", "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600" }));
        bpsCombo.setSelectedIndex(3);

        dataBitsCombo.setFont(new java.awt.Font("Arial Black", 0, 11));
        dataBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "6", "7", "8" }));
        dataBitsCombo.setSelectedIndex(3);

        parityCombo.setFont(new java.awt.Font("Arial Black", 0, 11));
        parityCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Even", "Odd", "None", "Mark", "Space" }));
        parityCombo.setSelectedIndex(2);

        stopBitsCombo.setFont(new java.awt.Font("Arial Black", 0, 11));
        stopBitsCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

        flowControlCombo.setFont(new java.awt.Font("Arial Black", 0, 11));
        flowControlCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Xon / Xoff", "hardware", "None" }));
        flowControlCombo.setSelectedIndex(1);

        javax.swing.GroupLayout addConnSecondPanelLayout = new javax.swing.GroupLayout(addConnSecondPanel);
        addConnSecondPanel.setLayout(addConnSecondPanelLayout);
        addConnSecondPanelLayout.setHorizontalGroup(
            addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConnSecondPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bpsLabel)
                    .addComponent(dataBitsLabel)
                    .addComponent(parityLabel)
                    .addComponent(stopBitsLabel)
                    .addComponent(flowControlLabel))
                .addGap(10, 10, 10)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(flowControlCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stopBitsCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(parityCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dataBitsCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bpsCombo, 0, 147, Short.MAX_VALUE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        addConnSecondPanelLayout.setVerticalGroup(
            addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addConnSecondPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bpsLabel)
                    .addComponent(bpsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataBitsLabel)
                    .addComponent(dataBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parityLabel)
                    .addComponent(parityCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stopBitsLabel)
                    .addComponent(stopBitsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(addConnSecondPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(flowControlLabel)
                    .addComponent(flowControlCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        mainPanel.add(addConnSecondPanel, "card3");

        backButton.setFont(new java.awt.Font("Arial Black", 0, 11));
        backButton.setText("< Back");
        backButton.setEnabled(false);
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBack(evt);
            }
        });

        nextButton.setFont(new java.awt.Font("Arial Black", 0, 11));
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onNext(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Arial Black", 0, 11));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onCancel(evt);
            }
        });

        javax.swing.GroupLayout framePanelLayout = new javax.swing.GroupLayout(framePanel);
        framePanel.setLayout(framePanelLayout);
        framePanelLayout.setHorizontalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(framePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePanelLayout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        framePanelLayout.setVerticalGroup(
            framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, framePanelLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(framePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(nextButton)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(framePanel, "card2");

        getAccessibleContext().setAccessibleParent(this);

        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width-dialogSize.width)/2,(screenSize.height-dialogSize.height)/2);
    }// </editor-fold>//GEN-END:initComponents
    private void onWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_onWindowActivated
        setDefaultValue();//GEN-LAST:event_onWindowActivated
    }

    private void setDefaultValue() {
        if (selectedConndata != null) {
            connNameTField.setText(selectedConndata[0]);
            portNameCombo.setSelectedItem(selectedConndata[1]);
            bpsCombo.setSelectedItem(selectedConndata[2]);
            dataBitsCombo.setSelectedItem(selectedConndata[3]);
            parityCombo.setSelectedItem(selectedConndata[4]);
            stopBitsCombo.setSelectedItem(selectedConndata[5]);
            flowControlCombo.setSelectedItem(selectedConndata[6]);
//            txtOutboxSpesifik.setText(selectedConndata[8]);
            if ("0".equals(selectedConndata[7])) {
                optOutboxAll.setSelected(true);
                txtOutboxSpesifik.setEnabled(false);
            } else {
                optOutboxSpesifik.setSelected(true);
                txtOutboxSpesifik.setEnabled(true);
            }
            cboSeriHP.setSelectedIndexToAutoId(selectedConndata[7]);
        } else {
            connNameTField.setText("");
            portNameCombo.setSelectedIndex(0);
            bpsCombo.setSelectedIndex(3);
            dataBitsCombo.setSelectedIndex(3);
            parityCombo.setSelectedIndex(2);
            stopBitsCombo.setSelectedIndex(0);
            flowControlCombo.setSelectedIndex(1);
            optOutboxAll.setEnabled(true);
        }
    }

    /******* menampilkan panekl pertama ********/
    private void setCardLayout() {
        mainPanel.add(addConnFirstPanel, "card2");
        mainPanel.add(addConnSecondPanel, "card3");
        backButton.setEnabled(false);
        nextButton.setText("Next >");
    }

    private void onBack(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBack
        setCardLayout();
    }//GEN-LAST:event_onBack

    private void onNext(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onNext
        String[] values = new String[11];
        values[0] = connNameTField.getText();
        values[1] = (String) portNameCombo.getSelectedItem();
        values[2] = (String) bpsCombo.getSelectedItem();
        values[3] = (String) dataBitsCombo.getSelectedItem();
        values[4] = (String) parityCombo.getSelectedItem();
        values[5] = (String) stopBitsCombo.getSelectedItem();
        values[6] = (String) flowControlCombo.getSelectedItem();
        if (selectedConndata == null) {// ini untuk insert

            if (connNameTField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, msgVoidConn, titleMessage, 2);
                connNameTField.requestFocus();
            } else {
                if (manSys.isAlreadyExistConn(connNameTField.getText())) {
                    JOptionPane.showMessageDialog(this, msgDupConn, titleMessage, 2);
                } else {
                    mainPanel.add(addConnSecondPanel, "card2");
                    mainPanel.add(addConnFirstPanel, "card3");
                    if (nextButton.getText().equals(" Done ")) {
                        if (optOutboxAll.isSelected()) {
                            values[7] = "0";
                        } else {
                            values[7] = "1";
                        }
                        values[8] = txtOutboxSpesifik.getText();

                        values[9] = cboSeriHP.getAutoIdFromSelectedIndex();
                        manSys.addConn(values);
                        dispose();
                    }
                    backButton.setEnabled(true);
                    nextButton.setText(" Done ");
                }
            }
        } else {// ini untuk edit

            values[7] = selectedConndata[0];
            mainPanel.add(addConnSecondPanel, "card2");
            mainPanel.add(addConnFirstPanel, "card3");
            if (nextButton.getText().equals(" Done ")) {
                if (optOutboxAll.isSelected()) {
                    values[8] = "0";
                } else {
                    values[8] = "1";
                }
                values[9] = txtOutboxSpesifik.getText();
                values[10] = cboSeriHP.getAutoIdFromSelectedIndex();
                manSys.editConn(values);
                dispose();
            }
            backButton.setEnabled(true);
            nextButton.setText(" Done ");
        }
    }//GEN-LAST:event_onNext

    private void onCancel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancel
        dispose();
    }//GEN-LAST:event_onCancel

    private void optOutboxAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optOutboxAllActionPerformed
        txtOutboxSpesifik.setEnabled(false);
    }//GEN-LAST:event_optOutboxAllActionPerformed

    private void optOutboxSpesifikActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optOutboxSpesifikActionPerformed
        txtOutboxSpesifik.setEnabled(true);
    }//GEN-LAST:event_optOutboxSpesifikActionPerformed

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("Dihapus");
        singleton = null;
        selectedConndata = null;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addConnFirstPanel;
    private javax.swing.JPanel addConnSecondPanel;
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox bpsCombo;
    private javax.swing.JLabel bpsLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelButton;
    private core.dataview.JCombBoxEx cboSeriHP;
    private javax.swing.JLabel connName;
    private javax.swing.JLabel connName1;
    private javax.swing.JLabel connName2;
    private javax.swing.JTextField connNameTField;
    private javax.swing.JComboBox dataBitsCombo;
    private javax.swing.JLabel dataBitsLabel;
    private javax.swing.JComboBox flowControlCombo;
    private javax.swing.JLabel flowControlLabel;
    private javax.swing.JPanel framePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JRadioButton optOutboxAll;
    private javax.swing.JRadioButton optOutboxSpesifik;
    private javax.swing.JComboBox parityCombo;
    private javax.swing.JLabel parityLabel;
    private javax.swing.JComboBox portNameCombo;
    private javax.swing.JLabel portNameLabel;
    private javax.swing.JComboBox stopBitsCombo;
    private javax.swing.JLabel stopBitsLabel;
    private javax.swing.JTextField txtOutboxSpesifik;
    // End of variables declaration//GEN-END:variables
}
