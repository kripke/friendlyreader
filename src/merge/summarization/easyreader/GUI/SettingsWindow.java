
/*
 * CSAdvancedPanel.java
 *
 * Created on 2010-aug-31, 10:36:55
 */

package merge.summarization.easyreader.GUI;

import merge.summarization.easyreader.summarization.Settings;


/**
 *
 * @author Christian
 */
public class SettingsWindow extends javax.swing.JPanel {

    /** Creates new form CSAdvancedPanel */
    public SettingsWindow() {
        initComponents();


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDimensionality = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jRandomDegree = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jRightWindow = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLeftWindow = new javax.swing.JTextField();
        jSeed = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        weightingSchemeCombo = new javax.swing.JComboBox();
        jLabel28 = new javax.swing.JLabel();
        jRemoveStopWords1 = new javax.swing.JCheckBox();
        jSaveToDisk1 = new javax.swing.JCheckBox();
        choseSumCB = new javax.swing.JComboBox();
        jLabel30 = new javax.swing.JLabel();
        jIterations2 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jFactorD2 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        splitCheckBox = new javax.swing.JCheckBox();
        saveevalCheckBox = new javax.swing.JCheckBox();

        jDimensionality.setText("100");
        jDimensionality.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDimensionalityActionPerformed(evt);
            }
        });

        jLabel11.setText("Dimensionality");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel15.setText("General settings");

        jRandomDegree.setText("4");
        jRandomDegree.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRandomDegreeActionPerformed(evt);
            }
        });

        jLabel19.setText("Random Degree");

        jLabel25.setText("Right window size");

        jRightWindow.setText("2");
        jRightWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRightWindowActionPerformed(evt);
            }
        });

        jLabel26.setText("Left window size");

        jLeftWindow.setText("2");
        jLeftWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLeftWindowActionPerformed(evt);
            }
        });

        jSeed.setText("555");
        jSeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSeedActionPerformed(evt);
            }
        });

        jLabel27.setText("Seed");

        weightingSchemeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MangesWS", "ConstantWS" }));

        jLabel28.setText("Weighting scheme");

        jRemoveStopWords1.setSelected(true);
        jRemoveStopWords1.setText("Remove stopwords");

        jSaveToDisk1.setText("Save to disk");

        choseSumCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CogSum" }));

        jLabel30.setText("Summarizer");

        jIterations2.setText("50");

        jLabel31.setText("Iterations");

        jFactorD2.setText("0.85");

        jLabel32.setText("Factor-D");

        splitCheckBox.setText("\\n\\n split");

        saveevalCheckBox.setText("save eval");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(splitCheckBox)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(jDimensionality, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel11))
                            .add(layout.createSequentialGroup()
                                .add(jRandomDegree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel19))
                            .add(layout.createSequentialGroup()
                                .add(jRightWindow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel25))
                            .add(layout.createSequentialGroup()
                                .add(jLeftWindow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel26))
                            .add(layout.createSequentialGroup()
                                .add(jSeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel27))
                            .add(jLabel15)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                            .add(layout.createSequentialGroup()
                                .add(jRemoveStopWords1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                .add(8, 8, 8))
                            .add(layout.createSequentialGroup()
                                .add(choseSumCB, 0, 96, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel30)
                                .add(65, 65, 65))
                            .add(layout.createSequentialGroup()
                                .add(jIterations2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel31)
                                .add(141, 141, 141))
                            .add(layout.createSequentialGroup()
                                .add(jFactorD2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel32)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(weightingSchemeCombo, 0, 117, Short.MAX_VALUE)
                                    .add(jSaveToDisk1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(saveevalCheckBox)
                                    .add(jLabel28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(10, 10, 10)))
                        .add(41, 41, 41))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel15)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(jDimensionality, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRandomDegree, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel19))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRightWindow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel25))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLeftWindow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel26))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel27)
                    .add(jSeed, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(weightingSchemeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel28))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jSaveToDisk1)
                    .add(saveevalCheckBox))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRemoveStopWords1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(splitCheckBox)
                .add(7, 7, 7)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(choseSumCB, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel30))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jIterations2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel31))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jFactorD2)
                    .add(jLabel32))
                .add(141, 141, 141))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jSeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSeedActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jSeedActionPerformed

    private void jLeftWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLeftWindowActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jLeftWindowActionPerformed

    private void jRightWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRightWindowActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jRightWindowActionPerformed

    private void jRandomDegreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRandomDegreeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jRandomDegreeActionPerformed

    private void jDimensionalityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDimensionalityActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jDimensionalityActionPerformed


    public Settings getSettings(){
        Settings settings = new Settings();

        settings.setDimensionality(Integer.parseInt(jDimensionality.getText()));
        settings.setRightWindowSize(Integer.parseInt(jRightWindow.getText()));
        settings.setLeftWindowSize(Integer.parseInt(jLeftWindow.getText()));
        settings.setPageRankFactorD(Float.parseFloat(jFactorD2.getText()));
        settings.setPageRankIterations(Integer.parseInt(jIterations2.getText()));
        settings.setRandomDegree(Integer.parseInt(jRandomDegree.getText()));
        settings.setRandomSeed(Integer.parseInt(jSeed.getText()));
        settings.setWeightingScheme("easyreader.ri."+((String)weightingSchemeCombo.getModel().getSelectedItem()));
        settings.setRemoveStopWords(jRemoveStopWords1.isSelected());
        settings.setSaveToDisk(jSaveToDisk1.isSelected());
        settings.setSplit(splitCheckBox.getModel().isSelected());
        settings.setEval(saveevalCheckBox.getModel().isSelected());
        settings.setUsePageRank(true);

        return settings;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox choseSumCB;
    public javax.swing.JTextField jDimensionality;
    public javax.swing.JTextField jFactorD2;
    public javax.swing.JTextField jIterations2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    public javax.swing.JTextField jLeftWindow;
    public javax.swing.JTextField jRandomDegree;
    private javax.swing.JCheckBox jRemoveStopWords1;
    public javax.swing.JTextField jRightWindow;
    private javax.swing.JCheckBox jSaveToDisk1;
    public javax.swing.JTextField jSeed;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JCheckBox saveevalCheckBox;
    private javax.swing.JCheckBox splitCheckBox;
    private javax.swing.JComboBox weightingSchemeCombo;
    // End of variables declaration//GEN-END:variables

}
