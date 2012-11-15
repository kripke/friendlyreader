package merge.pdfstod.GUI;

/* PDFReader 1.6 Nyaste versionen.
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.pdfbox.pdfviewer.ReaderBottomPanel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ExtensionFileFilter;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import merge.pdfstod.pdfstod3.PageWrapper;
import merge.pdfstod.pdfstod3.ReplaceStringStreamEngine;

/**
 * An application to read PDF documents.  This will provide Acrobat Reader like
 * funtionality.
 *
 * @author <a href="ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.5 $
 */
public class PDFReader extends javax.swing.JFrame
{
    private File currentDir=new File(".");
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem nextPageItem;
    private javax.swing.JMenuItem previousPageItem;
    public SummarizerPanelPDF sumPan;
    public JPanel documentPanel = new JPanel();
    public ReaderBottomPanel bottomStatusPanel = new ReaderBottomPanel();
    public CogSumSim cogsumsim; // AH* xxx
    public AdobeReaderTester adobereadertester = new AdobeReaderTester();

    public SummarizerPanelPDF getSummarizerPanel()
    {
        return sumPan;
    }

    public JPanel getDocumentPanel()
    {
        return documentPanel;
    }

    public void setCogSumSim(CogSumSim csi)
    {
        cogsumsim = csi;
    }

    public CogSumSim getCogSumSim()
    {
        return cogsumsim;
    }

    private static Boolean EasyReader; // AH* Visar om klassen används med EasyReader.

    private PDDocument document = null;
    public List pages= null;
    
    public int currentPage = 0;
    private int numberOfPages = 0;

    private String currentFilename = null;
    private ReplaceStringStreamEngine rsse = null;
    private static String pdfname = "";
    public static int sumslidval = 0;
    /**
     * Constructor.
     */
    public PDFReader()
    {
        EasyReader = false;
        SkrivUt(4, "EasyReader = false.");
        initComponents();
        //AH* Tillagt.
        try
        {
            rsse = new ReplaceStringStreamEngine();
        } catch (java.io.IOException jiioe)
        {
            SkrivUt(0, "Kan inte initiera ReplaceStringStreamEngine");
        }
    }

    /* Konstruktor för anrop från CogSumSim eller EasyReader.
     *
     */

    public PDFReader(Boolean easyreader)
    {
        EasyReader = easyreader;
        initComponents();
        //AH* Tillagt.
        try
        {
            rsse = new ReplaceStringStreamEngine();
        } catch (java.io.IOException jiioe)
        {
            SkrivUt(0, "Kan inte initiera ReplaceStringStreamEngine");
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()
    {
        SkrivUt(4, "initComponents");
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        printMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        nextPageItem = new javax.swing.JMenuItem();
        previousPageItem = new javax.swing.JMenuItem();

        if (EasyReader)
        {
            SkrivUt(4, "EasyReader True, sumPan.");
            sumPan = new SummarizerPanelPDF( this , false ); // AH* tillagd.
        } else
        {
            SkrivUt(4, "EasyReader False, sumPan.");
            sumPan = new SummarizerPanelPDF( this , true ); // AH* tillagd.
        }
        sumPan.setSize(100, 30); // AH* tillagd.

        setTitle("PDFBox - PDF Reader");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                exitApplication();
            }
        });


        JScrollPane documentScroller = new JScrollPane();
        documentScroller.setViewportView( documentPanel );

        getContentPane().add( sumPan, java.awt.BorderLayout.NORTH); // AH* SumPan.
        getContentPane().add( documentScroller, java.awt.BorderLayout.CENTER );
        getContentPane().add( bottomStatusPanel, java.awt.BorderLayout.SOUTH );

        fileMenu.setText("Arkiv");
        openMenuItem.setText("Öppna");
        openMenuItem.setToolTipText("Öppna PDF fil");
        openMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openMenuItemActionPerformed(evt);
            }
        });

        fileMenu.add(openMenuItem);

        printMenuItem.setText( "Skriv Ut" );
        printMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                try
                {
                    if (document != null) 
                    {
                        document.print();
                    }
                }
                catch( PrinterException e )
                {
                    e.printStackTrace();
                }
            }
        });
        fileMenu.add( printMenuItem );

        exitMenuItem.setText("Avsluta");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitApplication();
            }
        });

        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Hjälp");
        contentsMenuItem.setText("Innehåll");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("Om...");
        helpMenu.add(aboutMenuItem);

        viewMenu.setText("Visa");
        nextPageItem.setText("Nästa sida");
        nextPageItem.setAccelerator(KeyStroke.getKeyStroke('+'));
        nextPageItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                nextPage();
            }
        });
        viewMenu.add(nextPageItem);

        previousPageItem.setText("Föregående sida");
        previousPageItem.setAccelerator(KeyStroke.getKeyStroke('-'));
        previousPageItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                previousPage();
            }
        });
        viewMenu.add(previousPageItem);

        menuBar.add(viewMenu);

        menuBar.add(helpMenu); // AH* Avkommenterad.

        setJMenuBar(menuBar);


        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-700)/2, (screenSize.height-600)/2, 700, 600);
        }

    /* PDFReaderMain anropar main i PDFReader klassen och lämnar
     * därmed över kontrollen till detta program om anropet sker
     * från EasyReader.
     */

    public void PDFReaderMain()
    {
        try
        {
            if (EasyReader)
            {
                SkrivUt(4, "Exekvera main då EasyReader.");
                PDFReader.main(null);
            }
        } catch (java.lang.Exception jle)
        {
            SkrivUt(0,"Exception: PDFReader.main kunde inte exekveras.");
        }
    }

    /* SkrivUt styr om utskrift skall göras.
     */

    public static void SkrivUt(int plats, String str)
    {
        switch (plats)
        {
            case 0 : System.out.println(str);
            break;
            case 1 : // System.out.println(str);
            break;
            case 2 : // System.out.println(str);
            break;
            case 3 : System.out.println(str);
            break;
            case 4 : // System.out.println(str);
            break;
            case 5 : System.out.println(str);
            break;
            case 6 : System.out.println(str);
            break;
            case 7 : System.out.println(str);
            break;
            default: System.out.println("Ingen match i Skrivut!");
            break;

        }
    }

    // AH* Denna är borttagen i senare version!
    private void updateTitle() {
        setTitle( "PDFBox - " + pdfname); // AH* currentFilename );
    }
    
    public void nextPage()
    {
        if (currentPage < numberOfPages-1) 
        {
            currentPage++;
            int currtemp = currentPage+1;
            sumPan.Sidantal.setText("" + currtemp + " Av " + numberOfPages);
            // updateTitle(); // AH* Borttagen i senare.
            showPage(currentPage);
        }
    }
    
    public void previousPage()
    {
        if (currentPage > 0 ) 
        {
             int currtemp = currentPage;
            currentPage--;
            sumPan.Sidantal.setText("" + currtemp + " Av " + numberOfPages);
            // updateTitle(); // AH* Borttagen i senare.
            showPage(currentPage);
        }
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(currentDir);

        ExtensionFileFilter pdfFilter = new ExtensionFileFilter(new String[] {"PDF"}, "PDF Files");
        chooser.setFileFilter(pdfFilter);
        int result = chooser.showOpenDialog(PDFReader.this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            String name = chooser.getSelectedFile().getPath();
            PrepareFile( name, false, false); // Andra argumentet anger om rsse skall användas!
            /*
             * Här är koden kapad för att även kunna användas från CogSumSim som
             * också anropar PrepareFile.
            currentDir = new File(name).getParentFile();

            // AH* tillagd hantering av RSSE: xxx Här kan vara fel!
            pdfname = name; // Spara namnet till senare!
            try
            {
                rsse = new ReplaceStringStreamEngine(); // AH* nyligen tillagd för
                // att resetta rsse vid inläsning av ny fil! ***** Kan vara felet!
                rsse.doIt(name, "AHPrepout.pdf", false, 100, -1);
                // AH* Vid open skall ingen sammanfattning gÃ¶ras!
            } catch (java.io.IOException jaiie)
            {
                SkrivUt(0, "IOException i openMenuItemAction.");
            } catch (org.apache.pdfbox.exceptions.COSVisitorException cov)
            {
                SkrivUt(0, "COSVisitorException i openMenuItemAction.");
            }
            try
            {
                setTitle( "PDFBox - " + name); // AH* + f.getAbsolutePath() ); Flyttad hit.
                // AH* Nyare: openPDFFile("AHPrepout.pdf", 0); // AH* Tidigare vÃ¤rde name.
                openPDFFile("AHPrepout.pdf"); // AH* Tidigare var 0 andra argumentet.
                // showPage( 0 );
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
             *
             */
        }
    }

    /* PrepareFile metoden har extraherats för att skapa ett API som EasyReader
     * kan anropa.
     */

    public void PrepareFile(String filename, Boolean useRSSE, Boolean adobereader)
    {
            currentDir = new File(filename).getParentFile();

            // AH* tillagd hantering av RSSE: xxx Här kan vara fel!
            pdfname = filename; // Spara namnet till senare!
            if (useRSSE)// Skall filen bearbetas av rsse?
            {
                try
                {
                    // SkrivUt(7, "useRSSE TRUE.");
                    rsse = new ReplaceStringStreamEngine(); // AH* nyligen tillagd för
                    // att resetta rsse vid inläsning av ny fil! ***** Kan vara felet!
                    rsse.doIt(filename, "AHPrepout.pdf", false, 100, -1);
                    // AH* Vid open skall ingen sammanfattning gÃ¶ras!
                } catch (java.io.IOException jaiie)
                {
                    SkrivUt(0, "IOException i openMenuItemAction.");
                } catch (org.apache.pdfbox.exceptions.COSVisitorException cov)
                {
                    SkrivUt(0, "COSVisitorException i openMenuItemAction.");
                }
                try
                {
                    setTitle( "PDFBox - " + filename); // AH* + f.getAbsolutePath() ); Flyttad hit.
                    // AH* Nyare: openPDFFile("AHPrepout.pdf", 0); // AH* Tidigare vÃ¤rde name.
                    if (!adobereader)
                        openPDFFile("AHPrepout.pdf"); // AH* Tidigare var 0 andra argumentet.
                    else
                    {
                        adobereadertester.executefile("AHPrepout.pdf");
                    // Kan också dölja PDFReader fönstret med
                    // viewer.setVisible(true);
                    }
                    //Linkopia7
                    showPage( 0 );
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
                else // Ingen bearbetning med rsse används.
            {
                try
                {
                    // SkrivUt(7, "useRSSE FALSE.");
                    setTitle( "PDFBox - " + filename); // AH* + f.getAbsolutePath() ); Flyttad hit.
                    // AH* Nyare: openPDFFile("AHPrepout.pdf", 0); // AH* Tidigare vÃ¤rde name.
                    if (!adobereader)
                        openPDFFile( filename ); // AH* Tidigare var 0 andra argumentet.
                    else
                    {
                        adobereadertester.executefile(filename);
                        // Kan också dölja PDFReader fönstret med
                        // viewer.setVisible(true);
                    }
                    // showPage( 0 );
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

    }

    // AH* Kan vara fel i nedanstående del! Anropas från panelen då startknappen anv.

    public void UppdateraSammanfatta( int sumslidval, int nysida, int valdmening)
    {
        int valdsida = 0; // Sida med den valda meningen.
        if ((pdfname!=null)&&(pdfname!=""))
        {
            try
            {   // Kanske återstÃ¤lla rsse innan?
                if (rsse == null) // AH* senaste tillägget 120420.
                    rsse = new ReplaceStringStreamEngine(); // AH* inlagd för att testa om
                // programmet blir ok!+ ************* Är detta OK???
                SkrivUt(4, "Före doIT i UppdateraSammanfatta.");
                if (sumslidval > 100) sumslidval = 100;
                rsse.doIt(pdfname, "AHPrepout.pdf", true, sumslidval, valdmening);

                SkrivUt(4, "Efter doIT i UppdateraSammanfatta.");
                // AH* Vid open skall ingen sammanfattning göras!
                // Ta reda på vad vald sida är!
                valdsida = ReplaceStringStreamEngine.valdsida;
            } catch (java.io.IOException jaiie)
            {
                SkrivUt(0, "IOException i openMenuItemAction.");
            } catch (org.apache.pdfbox.exceptions.COSVisitorException cov)
            {
                SkrivUt(0, "COSVisitorException i openMenuItemAction.");
            }
            try
            {
                // setTitle( "PDFBox - " + name); // AH* + f.getAbsolutePath() ); Flyttad hit.
                SkrivUt(4, "Före openPDFFile i UppdateraSammanfatta.");
                openPDFFile("AHPrepout.pdf"); // AH* Tidigare name. Sid argument borta.
                SkrivUt(4, "Efter openPDFFile i UppdateraSammanfatta.");
                SkrivUt(4, ">>>>> ValdSida: " + valdsida);
                if ((valdsida != -1)&& (valdmening != -1))
                    currentPage = valdsida; // Om en mening är vald, dess sida.
                else
                    currentPage = nysida;
                if (cogsumsim!=null)
                    cogsumsim.showPage(currentPage); //AH* nytillagt.
                showPage(currentPage);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void exitApplication()
    {
        try
        {
            if( document != null )
            {
                document.close();
            }
        }
        catch( IOException io )
        {
            //do nothing because we are closing the application
        }
        this.setVisible( false );
        this.dispose();
    }

    /**
     * @param args the command line arguments
     *
     * @throws Exception If anything goes wrong.
     */
    public static void main(String[] args) throws Exception
    {
        SkrivUt(4,"Först i main.");
        PDFReader viewer = new PDFReader();
        SkrivUt(4,"Efter PDFReader() i main.");
        if( args.length >0 )
        {
            viewer.openPDFFile( args[0] );
        }
       
        if (!EasyReader) // AH* ! borttaget
            viewer.setVisible(true); // Felande rad!
        SkrivUt(4,"Sist.");
    }

    private void openPDFFile(String file) throws Exception
    {
        if( document != null )
        {
            document.close();
            documentPanel.removeAll();
        }
        InputStream input = null;
        File f = new File( file );
        input = new FileInputStream(f);
        document = parseDocument( input );
        pages = document.getDocumentCatalog().getAllPages();
        numberOfPages = pages.size();
        //AH* Sidantal till GUI:
        sumPan.Sidantal.setText("" + 1 + " Av " + numberOfPages);
        sumPan.sidnrantal = numberOfPages;
        currentFilename = f.getAbsolutePath(); // AH* Borttagen i senare version.
        currentPage = 0;
        updateTitle();
        showPage(0);
    }
    
    public void showPage(int pageNumber)
    {
        int visadsida = pageNumber+1; //AH* nytillagt.
        try 
        {
            SkrivUt(4, "ShowPage i PDFReader.");
            currentPage = pageNumber; //AH* TIllagd för att hålla reda på akt sida.
            PageWrapper wrapper = new PageWrapper( this );
            wrapper.displayPage( (PDPage)pages.get(pageNumber) );
            // if (EasyReader) cogsumsim.setDocumentPanel( wrapper.getPanel()); //AH*
            // PageWrapper cswrapper = new PageWrapper(cogsumsim.easyreader); AH*
            // cogsumsim.wrapper.displayPage( (PDPage)pages.get(pageNumber) ); //AH*
            if (documentPanel.getComponentCount() > 0)
            {
                documentPanel.remove(0);
                if (EasyReader) // AH* tillagt.
                    cogsumsim.getDocumentPanel().remove(0);
            }
            documentPanel.add( wrapper.getPanel() );
            if (EasyReader)  // AH* tillagt.
            {
                cogsumsim.setDocumentPanel( documentPanel );
                // cogsumsim.getDocumentPanel().;
            }
            pack();
            sumPan.Sidantal.setText("" + visadsida + " Av " + numberOfPages); // AH* nytt.
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
    /**
     * This will parse a document.
     *
     * @param input The input stream for the document.
     *
     * @return The document.
     *
     * @throws IOException If there is an error parsing the document.
     */
    private static PDDocument parseDocument( InputStream input )throws IOException
    {
        PDDocument document = PDDocument.load( input );
        if( document.isEncrypted() )
        {
            try
            {
                document.decrypt( "" );
            }
            catch( org.apache.pdfbox.exceptions.InvalidPasswordException e )
            {
                System.err.println( "Error: The document is encrypted." );
            }
            catch( org.apache.pdfbox.exceptions.CryptographyException e )
            {
                e.printStackTrace();
            }
        }

        return document;
    }

    /**
     * Get the bottom status panel.
     *
     * @return The bottom status panel.
     */
    public ReaderBottomPanel getBottomStatusPanel()
    {
        return bottomStatusPanel;
    }
}
