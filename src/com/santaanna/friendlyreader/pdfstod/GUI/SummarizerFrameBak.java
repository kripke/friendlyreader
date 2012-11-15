
/*
 * NewJPanel.java
 *
 * Created on 2010-nov-05, 16:25:29
 */

package com.santaanna.friendlyreader.pdfstod.GUI;

import javax.swing.*;
import java.io.*;
import java.net.*;

/**
 *
 * @author Christian
 */
public class SummarizerFrameBak extends javax.swing.JFrame {
    //private Summarizer summarizer;
    private PDFReader pdfreader = null;
    public static int sumslidval = 0;
    private CogSumSim cogsumsim = null;

    /** Creates new form NewJPanel */
    public SummarizerFrameBak( CogSumSim css)// PDFReader pdfr, Boolean showComp)
    {
        initComponents();
        // this.setSize(100, 30); //AH* tillagd.
        setPreferredSize(new java.awt.Dimension(80, 300));
        // summarizer = new CogSum(new Settings());
        // if (!showComp) //AH* Nytillagt 120423
        // {
            VäljMening.setVisible(true);
            ValdMening.setVisible(true);
            startBtn.setVisible(true);
            TFSlider.setVisible(true);
        // }
        // pdfreader = pdfr;
        cogsumsim = css;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        VäljMening = new javax.swing.JButton();
        ValdMening = new javax.swing.JTextField();
        startBtn = new javax.swing.JButton();
        TFSlider = new javax.swing.JTextField();

        jLabel1.setText("Ange vald mening och eller sammanfattningsnivå i %.");

        VäljMening.setText("Meningsnr");
        VäljMening.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VäljMeningActionPerformed(evt);
            }
        });

        ValdMening.setText("0");

        startBtn.setText("Start");
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startBtnMouseClicked(evt);
            }
        });

        TFSlider.setText("100");
        TFSlider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFSliderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(VäljMening)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ValdMening, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TFSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(VäljMening)
                    .addComponent(ValdMening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TFSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(262, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void TFSliderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFSliderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TFSliderActionPerformed

    private void startBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startBtnMouseClicked
        // TODO add your handling code here:
        // outputWindow.setText(""); AH*
        System.out.println("Startknappen tryckt.");
        // sumslidval = summarySlider.getModel().getValue();
        sumslidval = str2int( TFSlider.getText() );
        if (sumslidval > 100) sumslidval = 100;
        System.out.println("Slider värde: " + sumslidval);
        pdfreader.UppdateraSammanfatta( sumslidval, pdfreader.currentPage, -1 );
        System.out.println("Efter Startknappen.");
        /*
        Document d = new Document();
        // AH* d.read(new StringBuffer(this.inputWindow.getText()));
        this.summarizer.setText(d);
        this.summarizer.buildSummary();
         *
         */

        // AH* outputWindow.setText(this.summarizer.retrieveSummary(30,false));
}//GEN-LAST:event_startBtnMouseClicked

    private void VäljMeningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VäljMeningActionPerformed
        // TODO add your handling code here:
        int valdmennr = 0;
        valdmennr = str2int( ValdMening.getText() );
        System.out.println("VäljMening tryckt.");
        sumslidval = str2int( TFSlider.getText() );
        if (sumslidval > 100) sumslidval = 100;
        System.out.println("Slider värde: " + sumslidval);
        System.out.println("Vald mening: " + valdmennr);
        pdfreader.UppdateraSammanfatta( sumslidval, pdfreader.currentPage, valdmennr );
        System.out.println("Efter VäljMening.");
}//GEN-LAST:event_VäljMeningActionPerformed


    
    // str2int omvandlar en string med en siffra till en int.

    private int str2int(String instr)
    {
        int k;
        int summa = 0;
        int nolla = (int)'0';
        for (k=0;k<instr.length(); k++)
        {
            if ((instr.charAt(k)>= '0')&&(instr.charAt(k)<='9'))
            {
                summa = summa*10 + (int)instr.charAt(k) - nolla;
            } else
            {
                System.out.println("Ej siffra i sidfältet.");
                return 0;
            }
        }
        return summa;
    }
        private String getURLFileToString(String filnamn)
    { // Hämta en fil från nätet med en URL adress

        String inpline = "";
        String retstr = "";
        try
        {
            URL urlfil = new java.net.URL(filnamn);
            URLConnection c = urlfil.openConnection();
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(c.getInputStream()));
            //jedp.read(in, null);
            if (in != null) //&& (pwu != null))
            {
            	inpline=in.readLine();//read(bwr, null);
            	while (inpline != null)
            	{
            		retstr = retstr + inpline + "\n";
            		inpline=in.readLine();
            	}
            	in.close();
            } else System.out.println("pekare null.");
        } catch (IOException e)
        {
            JOptionPane.showMessageDialog(null,
                        "IOException vid läsning av URL fil.");
            return "";
        }
        return retstr;
    }

    private String getFileToString(String filnamn)
    {
    	String retstr = "";
    	String inpline ="";
    	System.out.println("getFileToString");
    	try {
            BufferedReader bwr = new BufferedReader(
                new FileReader(filnamn));
            // PrintWriter pwu = new PrintWriter(bwu);
            if (bwr != null) //&& (pwu != null))
            {
            	inpline=bwr.readLine();//read(bwr, null);
            	while (inpline != null)
            	{
            		retstr = retstr + inpline + "\n";
            		inpline=bwr.readLine();
            	}
            	bwr.close();
            } else System.out.println("pekare null.");
    	} catch (IOException ioe) {
    		System.out.println("Exception: XX Kunde inte läsa från fil i getFileToString.");
    		return "";
    	}
    	return retstr;
    }
    
    
        private void SaveToFile(String filnamn, String content, boolean append)
    { // Spara sammanfattningen till fil (senare).
    	//System.out.println(content);
        try {
        	BufferedWriter bw = new BufferedWriter(
    				new FileWriter(filnamn, append));
        	/*;
        	if (append)
        		bw = new BufferedWriter(
        				new FileWriter(filnamn, true));
        	else
        		bw = new BufferedWriter(
        				new FileWriter(filnamn));
        	*/
            PrintWriter pw = new PrintWriter(bw);
            pw.print(content);
            pw.close();
        } catch (IOException ioe) {
            System.out.println("Kunde inte skriva till fil.");
            return;
        }
    }

    private void SparaPDF( String content, String path)
    { // Spara PDF filen temporärt på disken.
    	//System.out.println(content);
        try {
        	BufferedWriter bw = new BufferedWriter(
    				new FileWriter(path));
        	/*;
        	if (append)
        		bw = new BufferedWriter(
        				new FileWriter(filnamn, true));
        	else
        		bw = new BufferedWriter(
        				new FileWriter(filnamn));
        	*/
            PrintWriter pw = new PrintWriter(bw);
            pw.print(content);
            pw.close();
        } catch (IOException ioe) {
            System.out.println("Kunde inte skriva till fil.");
            return;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TFSlider;
    private javax.swing.JTextField ValdMening;
    private javax.swing.JButton VäljMening;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton startBtn;
    // End of variables declaration//GEN-END:variables

    // Egna deklarationer.
    private JFileChooser fileCh = new JFileChooser();
    private String filnamn;
    private BufferedReader infil = null;
    private String filcontent = "";
    private java.net.URL urladress;
    public int sidnrantal = 0;
}
