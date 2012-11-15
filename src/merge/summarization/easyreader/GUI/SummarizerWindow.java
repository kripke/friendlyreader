
/*
 * NewJPanel.java
 *
 * Created on 2010-nov-05, 16:25:29
 */

package merge.summarization.easyreader.GUI;

import java.util.Vector;
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
public class SummarizerWindow extends javax.swing.JPanel {
    private Summarizer summarizer;
    private SummaryReader reader;

    /** Creates new form NewJPanel */
    public SummarizerWindow() {
        initComponents();
        Settings s = new Settings();
        s.setPageRankIterations(50);
        s.setPageRankFactorD(.85f);

        s.setRandomDegree(4);
        s.setRemoveStopWords(false);
        s.setRemoveDocumentVector(true);
        s.setDimensionality(300);
        s.setLeftWindowSize(2);
        s.setRightWindowSize(2);
        s.setRandomSeed(222);

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

        startBtn.setText("Start");
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showValuesCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
                        .addComponent(percentageLabel))
                    .addComponent(summarySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summarySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startBtn)
                    .addComponent(showValuesCheckBox)
                    .addComponent(percentageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startBtnMouseClicked
        // TODO add your handling code here:

        outputWindow.setText("");

        Document d = new Document();
        d.read(this.inputWindow.getText());
        this.summarizer.setText(d);
        Summary s = this.summarizer.buildSummary();
        reader = new SummaryReader(s);
        outputWindow.setText(reader.retrieveSummary(30, false));
        this.percentageLabel.setText("30%");

        Vector<String> keyw = ((CogSum)this.summarizer).retrieveKeywords(30, true);
        this.keyWordList.setListData(keyw);
        

    }//GEN-LAST:event_startBtnMouseClicked


    
    private void summarySliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summarySliderMouseDragged

        float percent = summarySlider.getModel().getValue();
        this.percentageLabel.setText(percent + "%");

        this.outputWindow.setText(reader.retrieveSummary(percent,
                this.showValuesCheckBox.getModel().isSelected()));

//        Vector<String> keyw = ((CogSum)this.summarizer).retrieveKeywords((int) percent, true);
//        this.keyWordList.setListData(keyw);

    }//GEN-LAST:event_summarySliderMouseDragged

    private void showValuesCheckBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showValuesCheckBoxMouseClicked
        float percent = summarySlider.getModel().getValue();
        this.percentageLabel.setText(percent + "%");

//        this.outputWindow.setText(summarizer.retrieveSummary(percent,
//                this.showValuesCheckBox.getModel().isSelected()));

        Vector<String> keyw = ((CogSum)this.summarizer).retrieveKeywords((int) percent, true);
        this.keyWordList.setListData(keyw);
    }//GEN-LAST:event_showValuesCheckBoxMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea inputWindow;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList keyWordList;
    private javax.swing.JTextArea outputWindow;
    private javax.swing.JLabel percentageLabel;
    private javax.swing.JCheckBox showValuesCheckBox;
    private javax.swing.JButton startBtn;
    private javax.swing.JSlider summarySlider;
    // End of variables declaration//GEN-END:variables

}
