

/*
 * ColorCodedSummary.java
 *
 * Created on 2010-nov-11, 12:40:17
 */

package merge.summarization.easyreader.GUI;

//import GoogleAPI.GoogleAPITranslator;
//import TranslateApplication.TranslationEngine;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import merge.summarization.easyreader.reader.Summary;
//
/**
 *
 * @author Christian
 */
public class ColorCodedSummary extends javax.swing.JPanel {
    private LinkedHashMap<Integer, JTextArea> map = new LinkedHashMap();
    private  GridBagConstraints constraints;
    private boolean compareSentences = false;
    private Summary summary;
    private ArrayList<Integer> sumIndices = new ArrayList();

    /** Creates new form ColorCodedSummary */
    public ColorCodedSummary() {
        initComponents();
        
        constraints  = new GridBagConstraints();
        constraints.gridwidth = 1;
        constraints.gridheight = 0;
    }
    
    public void clearText(){
        this.sumIndices.clear();
        this.summaryPanel.removeAll();
        this.setLayout(new GridLayout(0,1));
        this.summaryPanel.setLayout(new BoxLayout(summaryPanel,BoxLayout.Y_AXIS));
    }

    
    public void addSentence(Integer sentence, Color c){

        JTextArea tf = (JTextArea) map.get(sentence);

        int temp = 0;
        if(tf == null){
            for(String sent : this.summary.getSentences())
            {
                tf = new JTextArea();
                tf.setText(sent);
                float f = (float) this.summary.getSentenceRanks()[temp];
                if(f < 0)
                    f= 0;
                if(f > 1)
                    f = 1;
//                Color c = new Color(1f,1f,1f);
                tf.setWrapStyleWord(true);
                tf.setLineWrap(true);
                tf.setFont(new Font("Arial",Font.BOLD,12));
//                tf.setBorder(BorderFactory.createLineBorder(Color.black));
                tf.setBackground(c);
                tf.setCaretPosition(0);


                map.put(temp, tf);
                temp++;
            }
        }

        JPanel tempPanel = new JPanel();
        tempPanel.setLayout(new BoxLayout(tempPanel,BoxLayout.X_AXIS));
        
        //add button
        JButton btn = new JButton("Translate");
//        btn.addActionListener(new TranslateListener(map.get(sentence)));
        btn.setAlignmentY(Component.TOP_ALIGNMENT);
        tempPanel.add(btn);

        // add text area
        ((JTextArea)map.get(sentence)).setAlignmentY(Component.TOP_ALIGNMENT);
        tempPanel.add((JTextArea)map.get(sentence));


        summaryPanel.add(tempPanel);
        map.get(sentence).setBackground(c);

        float[][] comps = summary.getSentenceComparisons();

        if(comps != null && summaryPanel.getComponentCount() == comps.length){
            for(int i = 0; i < comps.length; i++){
                TFListener listener = new TFListener(i);
                summaryPanel.getComponent(i).addMouseListener(listener);
            }
        }


        if(!sumIndices.contains(sentence))
            sumIndices.add(sentence);
        summaryPanel.validate();


    }

    class TranslateListener implements ActionListener{
        private final JTextArea sentence;
        private String translation=null;
//        private TranslationEngine e = null;
        private boolean translatedState = false;
        private final String originalSentence;

        TranslateListener(JTextArea sentence){
            this.sentence = sentence;
            this.originalSentence = sentence.getText();
//            e = new TranslationEngine();
//            e.changeTranslationServer(GoogleAPITranslator.class);
           
        }
        public void actionPerformed(ActionEvent e) {
//
//            if(translation == null)
//                translation = this.e.translateText(sentence.getText(), "Swedish", "English");
//
//
//             if(this.translatedState){
//                 this.sentence.setText(this.originalSentence);
//                 ((JButton)this.sentence.getParent().getComponent(0)).setText("Translate");
//             }
//             else{
//                this.sentence.setText(translation);
//                ((JButton)this.sentence.getParent().getComponent(0)).setText("Undo");
//             }
//
//             this.translatedState = !this.translatedState;

        }
        String getTranslation(){
            return translation;
        }

    }

    public void findPasteSentences(float thresh){
        System.out.println(sumIndices.size());

        ArrayList<Integer> newList = new ArrayList();
        newList.addAll(sumIndices);

        for(Integer i : sumIndices){
            float[] da = this.summary.getSentenceComparisons()[i];
            for(int j = 0; j < da.length; j++){
                Float f = this.summary.getSentenceComparisons()[i][j];
                if(f > thresh){
                    System.out.println("bbb");
                    newList.add(j);
                }
            }
        }

        Collections.sort(newList);
        this.clearText();

        for(Integer i: newList)
            addSentence(i, new Color(1f,1f,1f));




    }

    public void buildGlobalCoherence(float thresh){

        float[][] edges = summary.getSentenceComparisons();


        for(int i=0; i < edges.length; i++){
            ((JTextArea)this.summaryPanel.getComponent(i)).
                    setText(((JTextArea)this.summaryPanel.
                    getComponent(i)).getText().replaceAll("\n\n", ""));

            if(i < edges.length -1){
                float f = Double.valueOf(edges[i][i + 1]).floatValue();
                if(f < thresh)
                ((JTextArea)this.summaryPanel.getComponent(i)).
                        setText(((JTextArea)this.summaryPanel.
                        getComponent(i)).getText() + "\n\n");
            }
        }

    }

    public void resetColors(){
        Iterator it = this.map.keySet().iterator();
        int i = 0;
        while(it.hasNext()){
            Integer index = (Integer) it.next();
                        float f = (float) this.summary.
                                getSentenceRanks()[index]-.3f;
            if(f < 0)
                f = 0;
            if(f > 1)
                f = 1;

            this.summaryPanel.getComponent(i).setBackground(new Color(f,1-f,0f));

            i++;
        }
    }

    void setCompareSentences(boolean b) {
        this.compareSentences = b;
    }

    void setSummary(Summary summary) {
        this.summary = summary;
    }

    void clearColors() {
        Iterator it = this.map.keySet().iterator();
        int i = 0;
        while(it.hasNext()){
            this.summaryPanel.getComponents()[i].setBackground(Color.WHITE);
            i++;
        }
    }


    public int getNumSentences() {
        return this.summaryPanel.getComponentCount();
    }

    class TFListener implements MouseListener{
        private final int counter;

        TFListener(int counter){
            this.counter = counter;
        }

        public void mouseClicked(MouseEvent e) {

            float[][] comps = summary.getSentenceComparisons();
            if(compareSentences){
                for(int i = 0; i < comps.length; i++){
                    float f = Double.valueOf(comps[i][counter]).floatValue();
                    if(f>1)f=1;
                    if(f<0)f=0;
                    summaryPanel.getComponent(i).setBackground(new Color(f,f,f));
                }
            }

        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        summaryPanel = new javax.swing.JPanel();

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        summaryPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout summaryPanelLayout = new javax.swing.GroupLayout(summaryPanel);
        summaryPanel.setLayout(summaryPanelLayout);
        summaryPanelLayout.setHorizontalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 252, Short.MAX_VALUE)
        );
        summaryPanelLayout.setVerticalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 11, Short.MAX_VALUE)
                    .addComponent(summaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 12, Short.MAX_VALUE)
                    .addComponent(summaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 12, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel summaryPanel;
    // End of variables declaration//GEN-END:variables

    void setNewSize(Rectangle b) {
        for(Component c : summaryPanel.getComponents()){
            JPanel p = (JPanel) c;
            JButton btn = (JButton)p.getComponents()[0];
            Rectangle newB = new Rectangle(b.x, b.y, b.width - btn.getBounds().width ,b.height);
            Component tempComp = p.getComponents()[1];
            tempComp.setBounds(newB);

        }
    }

}
