
/*
 * NewJPanel.java
 *
 * Created on 2010-nov-05, 16:25:29
 */

package merge.pdfstod.GUI;

// import cogsum.CogSum;
// import cogsum.Summarizer;
/* import cogsum.Settings;
import easyreader.Document;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane; */
import javax.swing.*;
import java.io.BufferedReader;
import java.io.*;
import java.net.*;


/**
 *
 * @author Christian
 */
public class SummarizerPanelPDF extends javax.swing.JPanel {
    //private Summarizer summarizer;
    private PDFReader pdfreader = null;
    public static int sumslidval = 0;

    /** Creates new form NewJPanel */
    public SummarizerPanelPDF( PDFReader pdfr, Boolean showComp) {
        initComponents();
        // this.setSize(100, 30); //AH* tillagd.
        setPreferredSize(new java.awt.Dimension(500, 70));
        // summarizer = new CogSum(new Settings());
        if (!showComp) //AH* Nytillagt 120423
        {
            VäljMening.setVisible(false);
            ValdMening.setVisible(false);
            startBtn.setVisible(false);
            TFSlider.setVisible(false);
        }
        pdfreader = pdfr;
    }

    /*
     * SkrivUt används för att styra utskrifter.
     */

    private static void SkrivUt(int nr, String str)
    {
        if (true) System.out.println( str );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        StegaNed = new javax.swing.JButton();
        StegaUpp = new javax.swing.JButton();
        GaTillSida = new javax.swing.JButton();
        TFSidnummer = new javax.swing.JTextField();
        Sidantal = new javax.swing.JLabel();
        VäljMening = new javax.swing.JButton();
        ValdMening = new javax.swing.JTextField();
        startBtn = new javax.swing.JButton();
        TFSlider = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(500, 500));

        StegaNed.setText("< -");
        StegaNed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StegaNedActionPerformed(evt);
            }
        });

        StegaUpp.setText("+ >");
        StegaUpp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StegaUppActionPerformed(evt);
            }
        });

        GaTillSida.setText("Gå till sida");
        GaTillSida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GaTillSidaActionPerformed(evt);
            }
        });

        TFSidnummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TFSidnummerActionPerformed(evt);
            }
        });

        Sidantal.setText("Av 0");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StegaNed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StegaUpp)
                .addGap(18, 18, 18)
                .addComponent(GaTillSida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TFSidnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Sidantal, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(VäljMening)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ValdMening, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TFSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Sidantal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ValdMening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(VäljMening)
                        .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TFSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(GaTillSida, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TFSidnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(StegaUpp)
                        .addComponent(StegaNed)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(446, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startBtnMouseClicked
        // TODO add your handling code here:
        // outputWindow.setText(""); AH*
        SkrivUt(4, "Startknappen tryckt.");
        // sumslidval = summarySlider.getModel().getValue();
        sumslidval = str2int( TFSlider.getText() );
        if (sumslidval > 100) sumslidval = 100;
        SkrivUt(4, "Slider värde: " + sumslidval);
        pdfreader.UppdateraSammanfatta( sumslidval, pdfreader.currentPage, -1 );
        SkrivUt(4, "Efter Startknappen.");
        /*
        Document d = new Document();
        // AH* d.read(new StringBuffer(this.inputWindow.getText()));
        this.summarizer.setText(d);
        this.summarizer.buildSummary();
         *
         */

        // AH* outputWindow.setText(this.summarizer.retrieveSummary(30,false));
    }//GEN-LAST:event_startBtnMouseClicked


    
    private void TFSidnummerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFSidnummerActionPerformed
        // TODO add your handling code here:
        // AH* Kanske reagera vid ändring istället för vid button. Eller newline.
    }//GEN-LAST:event_TFSidnummerActionPerformed

    private void GaTillSidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GaTillSidaActionPerformed
        // TODO add your handling code here:
        String tgotosida = TFSidnummer.getText().toString();
        int gotosida = str2int(tgotosida);
        // SkrivUt(4, "GotoSida: " + gotosida);
        if (gotosida != 0) gotosida--;
        if (gotosida < sidnrantal)
        {
            int visasidnr = gotosida + 1;
            Sidantal.setText("" + visasidnr + " Av " + sidnrantal);
            pdfreader.currentPage = gotosida; // Spara även sidan man gått till.
            pdfreader.showPage( gotosida );
        }  else
        {
            SkrivUt(4, "För stort sidnummer");
            TFSidnummer.setText("0"); // Kanske borde gå till förra innan ändringen.
        }
    }//GEN-LAST:event_GaTillSidaActionPerformed

    private void StegaUppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StegaUppActionPerformed
        // TODO add your handling code here:
        pdfreader.nextPage(); // Samma som menyvalet.
    }//GEN-LAST:event_StegaUppActionPerformed

    private void StegaNedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StegaNedActionPerformed
        // TODO add your handling code here:
        pdfreader.previousPage(); // Samma som menyvalet.
    }//GEN-LAST:event_StegaNedActionPerformed

    private void VäljMeningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VäljMeningActionPerformed
        // TODO add your handling code here:
        int valdmennr = 0;
        valdmennr = str2int( ValdMening.getText() );
        SkrivUt(4, "VäljMening tryckt.");
        sumslidval = str2int( TFSlider.getText() );
        if (sumslidval > 100) sumslidval = 100;
        SkrivUt(4, "Slider värde: " + sumslidval);
        SkrivUt(4, "Vald mening: " + valdmennr);
        pdfreader.UppdateraSammanfatta( sumslidval, pdfreader.currentPage, valdmennr );
        SkrivUt(4, "Efter VäljMening.");
    }//GEN-LAST:event_VäljMeningActionPerformed

    private void TFSliderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TFSliderActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_TFSliderActionPerformed

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
                SkrivUt(4, "Ej siffra i sidfältet.");
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
            } else SkrivUt(4, "pekare null.");
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
    	SkrivUt(4, "getFileToString");
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
            } else SkrivUt(4, "pekare null.");
    	} catch (IOException ioe) {
    		SkrivUt(4, "Exception: XX Kunde inte läsa från fil i getFileToString.");
    		return "";
    	}
    	return retstr;
    }
    
    
        private void SaveToFile(String filnamn, String content, boolean append)
    { // Spara sammanfattningen till fil (senare).
    	//SkrivUt(4, content);
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
            SkrivUt(4, "Kunde inte skriva till fil.");
            return;
        }
    }

    private void SparaPDF( String content, String path)
    { // Spara PDF filen temporärt på disken.
    	//SkrivUt(4, content);
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
            SkrivUt(4, "Kunde inte skriva till fil.");
            return;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton GaTillSida;
    public javax.swing.JLabel Sidantal;
    private javax.swing.JButton StegaNed;
    private javax.swing.JButton StegaUpp;
    private javax.swing.JTextField TFSidnummer;
    private javax.swing.JTextField TFSlider;
    private javax.swing.JTextField ValdMening;
    private javax.swing.JButton VäljMening;
    private javax.swing.JPanel jPanel2;
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
