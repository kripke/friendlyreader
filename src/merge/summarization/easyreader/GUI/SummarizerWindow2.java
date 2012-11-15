
/*
 * NewJPanel.java
 *
 * Created on 2010-nov-05, 16:25:29
 */

package merge.summarization.easyreader.GUI;

import java.awt.Font;
import java.io.File;
import java.util.Vector;
import javax.swing.JFileChooser;
import merge.summarization.easyreader.reader.Document;
import merge.summarization.easyreader.reader.Summary;
import merge.summarization.easyreader.reader.SummaryReader;
import merge.summarization.easyreader.summarization.CogSum;
import merge.summarization.easyreader.summarization.Settings;
import merge.summarization.easyreader.summarization.Summarizer;

/**
 *
 * @author Christian
 */
public class SummarizerWindow2 extends javax.swing.JPanel {
    private Summarizer summarizer;
    private String summary;
    private String inFile;
    private SummaryReader reader;
    final JFileChooser fileChooser = new JFileChooser();

    /** Creates new form NewJPanel */
    public SummarizerWindow2() {

        initComponents();

        this.fwdBtn.setVisible(false);
        this.backBtn.setVisible(false);


        Settings s = new Settings();
        s.setPageRankIterations(50);
        s.setPageRankFactorD(.85f);

        s.setRandomDegree(4);
        s.setRemoveStopWords(false);
        s.setRemoveDocumentVector(true);
        s.setDimensionality(300);
        s.setLeftWindowSize(2);
        s.setRightWindowSize(2);
        s.setRandomSeed(1050);
        s.setUseOutsideSpace(false);

        s.setTrainingFile("brownut");

        //s.setTrainingFile("/home/christian/NetBeansProjects/SDM/large");
//        s.setLeftWindowSize(5);
//        s.setRightWindowSize(5);
        summarizer = new CogSum(s);
        this.percentageLabel.setText("%");

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        inputWindow = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputWindow = new javax.swing.JTextArea();
        summarySlider = new javax.swing.JSlider();
        startBtn = new javax.swing.JButton();
        showValuesCheckBox = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        keyWordList = new javax.swing.JList();
        percentageLabel = new javax.swing.JLabel();
        spacingSlider = new javax.swing.JSlider();
        sizeSlider = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();
        fwdBtn = new javax.swing.JButton();
        chooseFileBtn = new javax.swing.JButton();
        saveXmlBtn = new javax.swing.JButton();

        inputWindow.setColumns(20);
        inputWindow.setLineWrap(true);
        inputWindow.setRows(5);
        inputWindow.setText("Paste text here and press \"Start\"! Adjust summary length by dragging the slider.");
        inputWindow.setWrapStyleWord(true);
        jScrollPane1.setViewportView(inputWindow);

        outputWindow.setColumns(20);
        outputWindow.setEditable(false);
        outputWindow.setLineWrap(true);
        outputWindow.setRows(5);
        outputWindow.setText("The summary appears here.");
        outputWindow.setWrapStyleWord(true);
        jScrollPane2.setViewportView(outputWindow);

        summarySlider.setValue(30);
        summarySlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                summarySliderMouseDragged(evt);
            }
        });

        startBtn.setText("Sum");
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startBtnMouseClicked(evt);
            }
        });

        showValuesCheckBox.setText("Show values");
        showValuesCheckBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showValuesCheckBoxMouseClicked(evt);
            }
        });

        keyWordList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Keywords appear here." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(keyWordList);

        spacingSlider.setMajorTickSpacing(1);
        spacingSlider.setMaximum(5);
        spacingSlider.setMinimum(1);
        spacingSlider.setMinorTickSpacing(1);
        spacingSlider.setPaintTicks(true);
        spacingSlider.setSnapToTicks(true);
        spacingSlider.setValue(1);
        spacingSlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                spacingSliderMouseDragged(evt);
            }
        });

        sizeSlider.setMajorTickSpacing(4);
        sizeSlider.setMaximum(48);
        sizeSlider.setMinimum(10);
        sizeSlider.setMinorTickSpacing(4);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setSnapToTicks(true);
        sizeSlider.setValue(12);
        sizeSlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                sizeSliderMouseDragged(evt);
            }
        });

        jLabel1.setText("Spacing");

        jLabel2.setText("Size");

        backBtn.setText("<-");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        fwdBtn.setText("->");
        fwdBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fwdBtnActionPerformed(evt);
            }
        });

        chooseFileBtn.setText("File");
        chooseFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFileBtnActionPerformed(evt);
            }
        });

        saveXmlBtn.setText("Save xml");
        saveXmlBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveXmlBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(summarySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chooseFileBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sizeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                        .addComponent(saveXmlBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(spacingSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                .addComponent(percentageLabel))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(backBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fwdBtn)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showValuesCheckBox))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(summarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(startBtn)
                                .addComponent(fwdBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(percentageLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chooseFileBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(showValuesCheckBox)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spacingSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sizeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(saveXmlBtn))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startBtnMouseClicked
        // TODO add your handling code here:

        outputWindow.setText("");

        Document d = new Document();
        if(inFile != null)
            d.readFile(inFile);
        else
            d.read(this.inputWindow.getText());
        this.summarizer.setText(d);
        Summary summ = this.summarizer.buildSummary();
        reader = new SummaryReader(summ);
        
        summary = reader.retrieveSummary(30,true);

        outputWindow.setText(reader.retrieveSummary(30,
                this.showValuesCheckBox.getModel().isSelected()));
        this.percentageLabel.setText("30%");

        Vector<String> keyw = ((CogSum)this.summarizer).retrieveKeywords(30, false);
        this.keyWordList.setListData(keyw);
        
        //System.out.println(reader.getIndexList());
    }//GEN-LAST:event_startBtnMouseClicked

    private void updateOutput(){
        int numRows = this.spacingSlider.getValue();
        String text = this.outputWindow.getText();
        String n = "";
        for(int i = 0; i < numRows; i++){
            n += "\n";
        }
        text = text.replaceAll("\n+", n);
        this.outputWindow.setText(text);

        Font font = new Font("Arial", Font.PLAIN, this.sizeSlider.getValue());
        this.outputWindow.setFont(font);
    }

    
    private void summarySliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summarySliderMouseDragged

        float percent = summarySlider.getModel().getValue();
        this.percentageLabel.setText(percent + "%");

        //summary = summarizer.retrieveSummary(percent, true);

            this.outputWindow.setText(reader.retrieveSummary(percent,
                    this.showValuesCheckBox.getModel().isSelected()));

            this.updateOutput();
//
        Vector<String> keyw = ((CogSum)this.summarizer).
                retrieveKeywords((int) percent, false);
        this.keyWordList.setListData(keyw);

        this.outputWindow.setCaretPosition(0);

    }//GEN-LAST:event_summarySliderMouseDragged

    private void showValuesCheckBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showValuesCheckBoxMouseClicked
        float percent = summarySlider.getModel().getValue();
        this.percentageLabel.setText(percent + "%");

        this.outputWindow.setText(reader.retrieveSummary(percent,
                this.showValuesCheckBox.getModel().isSelected()));

//        Vector<String> keyw = ((CogSum)this.summarizer).retrieveKeywords((int) percent, true);
//        this.keyWordList.setListData(keyw);
    }//GEN-LAST:event_showValuesCheckBoxMouseClicked

    private void spacingSliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_spacingSliderMouseDragged
        // TODO add your handling code here:
        int numRows = this.spacingSlider.getValue();
        String text = this.outputWindow.getText();
        String n = "";
        for(int i = 0; i < numRows; i++){
            n += "\n";
        }
        text = text.replaceAll("\n+", n);
        this.outputWindow.setText(text);
        this.outputWindow.setCaretPosition(0);

    }//GEN-LAST:event_spacingSliderMouseDragged

    private void sizeSliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sizeSliderMouseDragged
        // TODO add your handling code here:
        Font font = new Font("Arial", Font.PLAIN, this.sizeSlider.getValue());
        this.outputWindow.setFont(font);

    }//GEN-LAST:event_sizeSliderMouseDragged

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_backBtnActionPerformed

    private void fwdBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fwdBtnActionPerformed
        // TODO add your handling code here:
        int pos = this.outputWindow.getCaretPosition();

        String text = this.outputWindow.getText();

        int newPos = text.substring(pos).indexOf("\n");

        this.outputWindow.setCaretPosition(pos+newPos+1);
        System.out.println(pos+newPos+1);

    }//GEN-LAST:event_fwdBtnActionPerformed

    private void chooseFileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFileBtnActionPerformed
        // TODO add your handling code here:
        int retVal = fileChooser.showOpenDialog(this);

        if(retVal == JFileChooser.APPROVE_OPTION){
            inFile = fileChooser.getSelectedFile().toString();
            System.out.println(fileChooser.getSelectedFile());
        }
    }//GEN-LAST:event_chooseFileBtnActionPerformed

    private void saveXmlBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveXmlBtnActionPerformed
        // TODO add your handling code here:
        String fileName = "out.xml";
        while(new File(fileName).exists())
            fileName = "*"+fileName;
        reader.getSummary().save(fileName);
        System.out.println("Saved as "+fileName);
    }//GEN-LAST:event_saveXmlBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton chooseFileBtn;
    private javax.swing.JButton fwdBtn;
    private javax.swing.JTextArea inputWindow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList keyWordList;
    private javax.swing.JTextArea outputWindow;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JButton saveXmlBtn;
    private javax.swing.JCheckBox showValuesCheckBox;
    private javax.swing.JSlider sizeSlider;
    private javax.swing.JSlider spacingSlider;
    private javax.swing.JButton startBtn;
    private javax.swing.JSlider summarySlider;
    // End of variables declaration//GEN-END:variables

}