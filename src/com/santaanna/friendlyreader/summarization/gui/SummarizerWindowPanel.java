
/*
 * NewJPanel.java
 *
 * Created on 2010-nov-05, 16:25:29
 */

package com.santaanna.friendlyreader.summarization.gui;

//import evaluation.GranskaReader;
//import evaluation.GranskaWriter;
//import evaluation.Ovix;
//import evaluation.Lix;
//import evaluation.NominalRatio;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.santaanna.friendlyreader.summarization.summarizer.Summarizer;

/**
 *
 * @author Christian
 */
public class SummarizerWindowPanel extends javax.swing.JPanel {

    private Summarizer summarizer;
    private double normalizedRank;
    private StringBuilder text = new StringBuilder();


    /** Creates new form NewJPanel */
    public SummarizerWindowPanel() {
        initComponents();
       // this.jScrollPane3.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        summarySlider = new javax.swing.JSlider();
        showValuesCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lixLabel = new javax.swing.JLabel();
        lixTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        nrLabel = new javax.swing.JLabel();
        nrTextField = new javax.swing.JTextField();
        ovixLabel = new javax.swing.JLabel();
        ovixTextField = new javax.swing.JTextField();

        summarySlider.setValue(30);
        summarySlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                summarySliderMouseDragged(evt);
            }
        });

        showValuesCheckBox.setText("Show values");

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, summarySlider, org.jdesktop.beansbinding.ELProperty.create("${value}%"), jLabel1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        lixLabel.setText("LIX:");

        jButton1.setText("Evaluate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        nrLabel.setText("NR:");

        ovixLabel.setText("OVIX:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(showValuesCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(summarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nrLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lixLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ovixLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ovixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lixLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nrTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nrLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ovixTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ovixLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(summarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addComponent(showValuesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    public void setSummarizer(Summarizer sum) throws UnsupportedEncodingException{
        this.summarizer = sum;
        setSummary();
    }

    private void updateEvaluation() throws UnsupportedEncodingException{
        
        /**
         * Tag
         */
//        GranskaWriter.write(text.toString(), "tagged.txt");
//        GranskaReader gr = new GranskaReader("tagged.txt");
//
//        Lix l = new Lix();
//        NominalRatio nk = new NominalRatio();
//        Ovix lv = new Ovix();
//
//        DecimalFormat twoDForm = new DecimalFormat("0.00");
//
//        this.lixTextField.setText(twoDForm.format(l.calc(gr)).replaceAll(",", "."));
//        this.nrTextField.setText(twoDForm.format(nk.calc(gr)).replaceAll(",", "."));
//        this.ovixTextField.setText(twoDForm.format(lv.calc(gr)).replaceAll(",", "."));

    }

    private void setSummary() throws UnsupportedEncodingException{
        
//        String[] arr =
//                this.summarizer.retrieveSummary(100, true).split("\n\n");

        double max=0;
        double min=0;
//        for(String sentence: arr){
//            System.out.println(sentence);
//            double f = Double.parseDouble(sentence.split("@value:")[1]);
//            if(f > max)
//                max = f;
//            if(f < min){
//                min = f;
//            }
//        }
//        max = 1;
//        min = 0;

        normalizedRank = 1 / ( max + -(min) );

//        outputWindow.clearText();
        
//        for(String sentence: arr){
//
//            if(this.showValuesCheckBox.getModel().isSelected()){
//                outputWindow.addSentence(sentence,
//                        normalizedRank);
//            }
//            else{
//                outputWindow.addSentence(sentence.split("@value:")[0],
//                        normalizedRank);
//            }
//        }

        this.updateEvaluation();
//        outputWindow.revalidate();
    }


    
    private void summarySliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summarySliderMouseDragged

        float percent = summarySlider.getModel().getValue();

        String s ="";// this.summarizer.retrieveSummary(percent,true);
        String[] arr = s.split("\n\n");


//        outputWindow.clearText();

//        this.text = new StringBuilder();
//        for(String sentence: arr){
//            this.text.append(sentence.split("@value:")[0]);
//            if(this.showValuesCheckBox.getModel().isSelected()){
//                outputWindow.addSentence(sentence,
//                        Double.parseDouble(sentence.split("@value:")[1])*normalizedRank);
//            }
//            else{
//                outputWindow.addSentence(sentence.split("@value:")[0],
//                        Double.parseDouble(sentence.split("@value:")[1])*normalizedRank);
//            }
//
//
//        }

//        outputWindow.revalidate();

    }//GEN-LAST:event_summarySliderMouseDragged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            this.updateEvaluation();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SummarizerWindowPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lixLabel;
    private javax.swing.JTextField lixTextField;
    private javax.swing.JLabel nrLabel;
    private javax.swing.JTextField nrTextField;
    private javax.swing.JLabel ovixLabel;
    private javax.swing.JTextField ovixTextField;
    private javax.swing.JCheckBox showValuesCheckBox;
    private javax.swing.JSlider summarySlider;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables



}
