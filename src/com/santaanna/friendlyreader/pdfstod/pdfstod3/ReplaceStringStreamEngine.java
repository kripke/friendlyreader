package com.santaanna.friendlyreader.pdfstod.pdfstod3;

/*  ReplaceStringStreamEngine.java
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.LinkedList;
import java.util.Vector;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.geom.GeneralPath;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSStream;

import org.apache.pdfbox.exceptions.COSVisitorException;

import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;

import org.apache.pdfbox.pdmodel.common.PDStream;

import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFStreamEngine;

import org.apache.pdfbox.util.ResourceLoader;
import org.apache.pdfbox.util.TextPosition;


// AH* Klasser för att göra sammanfattningen:

import java.util.ArrayList;
import java.util.Collection;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SEArrayIndKlass;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SEArrayNumber;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SECosstr;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SETextBlock;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SEmening;
import com.santaanna.friendlyreader.pdfstod.CogsumApplet.SEmeningsdel;
import com.santaanna.friendlyreader.summarization.reader.Document;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.summarization.summarizer.CogSum;
import com.santaanna.friendlyreader.summarization.Settings;

/**
 * This is an example that will replace a string in a PDF with a new one.
 *
 * The example is taken from the pdf file format specification.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.3 $
 */

public class ReplaceStringStreamEngine extends PDFStreamEngine
{
    private static List tokenlist;
    private Graphics2D graphics;
    private Dimension pageSize;
    public PDDocument doc1 = null;
    private PDPage page = null;
    private PDPage page1 = null;
    private GeneralPath linePath = new GeneralPath();
    public static int stringlen = 0;
    public static int currstringlen = 0;
    public static byte[] bytestr;
    public static String strtp = ""; // AH* Samlade stringen från COSString.
    public static String helaTexten = ""; //AH* Totala texten!
    // public static Vector<String> meningarna;  // Används inte!

    public static float sumcharAlla = 0; //AH* Bredden för hela stringen.
    public float[] sumcharwidth = new float[1000]; // AH* array med char width för varje char.
    public static float tpx;
    public static float tpy;
    public static SECosstr SEC = null; // Textblocks string struktur.
    public static Vector<SETextBlock> TBVector; // TBVector per sida.
    public static Vector<Vector> PageVector = new Vector<Vector>(); // Vektor som innehåller siddata.
    // public Vector<SEmening> menvektor = new Vector<SEmening>(); // Anv temporärt.
    public int TBIndex = 0; // Aktuellt index till TBVector!
    public static SETextBlock setb = null;
    public static boolean ISArray = false;
    public static int arraysize = 0;
    public static int tbpagenr = 0;
    public static int mind = 0; // Index för aktuell mening.
    public static Vector<SEmening> meningsvektor = new Vector<SEmening>();// Vektor som lagrar meningarna.
    public static boolean skrivut = false;
    private String outputFile = "";
    private PDDocument doc = null;
    public static List ptokens = null; // Sparade tokens på en sida.
    public static Vector<List> pageTokens = new Vector<List>(); // Vektor med tokens på alla sidor.
    // Sammanfattningsvariabler:
    public static int cosenr = 0; // Nummer på aktuell COSString eller COSArray.
    public static boolean medisammanf = true;
    public static int mennr = 0; // Aktuellt meningsnummer.
    public static int mendelnr = 0;
    public static int mendelantal = 0; // Antal delar som en mening består av.

    public static int[] menisammanfattn =
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};

    public static int[] menisammanfattningenTest = // xxx
        {0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30};

    // AH* Data som används av sammanfattningen:
    public static ArrayList<Integer> menisammanfattningen;
    public static int sumslidval = 0;
    static ArrayList<String> sentences;
    static SummaryReader reader;
    static ArrayList<Integer> indexSummary;
    public static int valdsida = -1;
    static int tempsidnr;

    public static void resetXY()
    {
        tpx = 0;
        tpy = 0;
    }

    /*
     * SkrivUt(String)
     */

    public static void SkrivUt(String str)
    {
        // if (skrivut)
            System.out.println(str);
    }

        /*
     * SkrivUt(String)
     */

    public static void SkrivUt(int plats, String str)
    {
        switch (plats)
        {
            case 0 : System.out.println(str); // catch utskrifter.
            break;
            case 1 : // System.out.println(str);
            break;
            case 2 : // System.out.println(str);
            break;
            case 3 : // System.out.println(str);
            break;
            case 4 : // System.out.println(str); // Många grundutskrifter.
            break;
            case 5 : // System.out.println(str); // I TextPosition.
            break;
            case 6 : System.out.println(str);
            break;
            case 7 : System.out.println(str);
            break;
            default: System.out.println("Ingen match i Skrivut!");
            break;

        }
    }

    public static void SkrivUt2(int plats, String str)
    {
        switch (plats)
        {
            case 0 : System.out.print(str);
            break;
            case 1 : // System.out.println(str);
            break;
            case 2 : // System.out.println(str);
            break;
            case 3 : System.out.print(str);
            break;
            case 4 : // System.out.print(str); //Loop i TextPosition.
            break;
            case 5 : System.out.print(str);
            break;
            case 6 : System.out.print(str);
            break;
            case 7 : System.out.print(str);
            break;
            default: System.out.print("Ingen match i Skrivut2!");
            break;

        }
    }
        /**
     * encoding that text will be written in (or null).
     */
    protected String outputEncoding = "Unicode"; // "UTF-16"; // Samma som i PDFHighlighter.java

        /**
     * The normalizer is used to remove text ligatures/presentation forms
     * and to correct the direction of right to left text, such as Arabic and Hebrew.
     */
    // private TextNormalize normalize = null;

    /**
     * Constructor.
     */
    public ReplaceStringStreamEngine() throws IOException
    {
          super( ResourceLoader.loadProperties("Resources/MyPropertyFile.properties", true) );
          // this.outputEncoding = null;
          // normalize = new TextNormalize(this.outputEncoding);
          // AH Tillagd.
          SkrivUt(4, "Konstruktorn klar.");
    }

    /* baraBlanka kollar om varje tecken i en string är ett blanktecken.
     *
     */

    public static Boolean baraBlanka(String instr)
    {
        for (int b = 0; b < instr.length(); b++)
        {
            if (!isBlank( instr.charAt(b)))
                return false;
        }
        return true;
    }
    
    /*
     *  isBlank kollar om tecknet är blankt.
     */

    public static Boolean isBlank(char c)
    {
        if ((c == ' ')||(c =='\t')|| (c == '\n')||(c=='\r')||(c=='\f'))
            return true;
        else return false;
    }

    /*
     *  isSlutTecken kollar om tecknet är sluttecken.
     */

    public static Boolean isSlutTecken(char c)
    {
        if ((c == '.')||(c =='!')|| (c == '?')||(c=='…'))
            return true;
        else return false;
    }
    
    /*
     *  isVersal testa om 
     */

    public static Boolean isVersal(char c)
    {
        if ((c == 'Å')||(c =='Ä')|| (c == 'Ö')||
                ((c >= '0')&& (c <= '9'))||
                ((c >= 'A')&&(c <='Z')))
            return true;
        else return false;
    }

    /*
     * Hitta meningarna letar igenom helaTexten och sparar
     * separata meningar som den hittar.
     */

    public static Vector<SEmening> Hittameningarna(String helaTexten )
    {
        int antal = 0;
        final int vanligText = 0;
        final int slutTecken = 1;
        final int blanka = 2;
        final int versaler = 3;
        int state = vanligText;
        String tempmening = "";
        String tempstr = "";
        SEmening semening = new SEmening(); // AH* ändrad struktur.
        Vector<SEmening> meningsvektor = new Vector(); // AH* ändrad struktur.
        int meningsnummer = 0;
        int k;
        char tc;
        for ( k=0; k< helaTexten.length(); k++)
        {
            tc = helaTexten.charAt( k );
            switch ( state ) {
                case vanligText : // Alla tecken utom sluttecken.
                    // Vid sluttecken byt state.
                    if (isSlutTecken( tc ))
                    {
                        state = slutTecken;
                        tempmening += tc;
                    } else // Samma för alla efterkommande tecken.
                    {
                        tempmening += tc;
                    }
                    break;
                case slutTecken : // .,!? och ...
                    if (isSlutTecken( tc))
                    {
                        tempmening += tc;
                    } else if (isBlank( tc))
                    {
                        state = blanka;
                        tempstr += tc;
                    } else if (isVersal( tc ))
                    { // Ny mening. >> Kräver INTE blanka innan!
                        // Spara tidigare mening.
                        semening = new SEmening();
                        semening.helameningen = tempmening;
                        meningsvektor.add( semening );
                        meningsnummer++;
                        tempmening = "" + tc;
                        state = vanligText;
                    } else // inte sluttecken, blank eller versal.
                    { // => fortsatt mening!
                        tempmening += tc;
                        state = vanligText;
                    }
                    break;
                case blanka : // ' ', '\t','\n'
                    // Bara blanka efter sluttecken!
                    if (isBlank( tc ))
                    {
                        tempstr += tc;
                    } else if (isVersal( tc ))
                    { // Ny mening.
                        // Spara tidigare mening.
                        semening = new SEmening();
                        semening.helameningen = tempmening;
                        meningsvektor.add( semening );
                        meningsnummer++;
                        tempmening = tempstr + tc;
                        tempstr = "";
                        state = vanligText;
                    } else // Ingen ny mening!
                    {
                        tempmening += tempstr + tc;
                        tempstr = "";
                        state = vanligText;
                    }
                    break;

                case versaler : // A-Z, Å,Ä,Ö, 0-9.
                    // Start på ny mening hittad. INGET SKALL GÖRAS!
                    break;
                default: SkrivUt(4, "Inget alt vid meningsläsning.");
            }

        }
        // Efterbearbetning av resultatet!
        switch ( state ) {
                case vanligText : // Alla tecken utom sluttecken.
                    // Vid sluttecken byt state.
                    semening = new SEmening();
                    semening.helameningen = tempmening;
                    meningsvektor.add( semening );
                    meningsnummer++;
                    break;
                case slutTecken : // .,! och ?
                    semening = new SEmening();
                    semening.helameningen = tempmening;
                    meningsvektor.add( semening );
                    // meningsvektor.add( tempmening );
                    meningsnummer++;
                    break;
                case blanka : // ' ', '\t','\n'
                    semening = new SEmening();
                    semening.helameningen = tempmening;
                    meningsvektor.add( semening );
                    semening = new SEmening();
                    semening.helameningen = tempstr;
                    meningsvektor.add( semening );
                    // meningsvektor.add( tempmening );
                    // meningsvektor.add(tempstr);
                    meningsnummer += 2;
                    break;
                case versaler : // A-Z, Å,Ä,Ö, 0-9.
                    // Start på ny mening hittad. INGET SKALL GÖRAS!
                    break;
                default: SkrivUt(4, "Inget alt vid meningsläsning.");
            }
        // Testutskrift av alla meningarna.
        // for (k=0; k< meningsnummer; k++ )
        // {
        //  SkrivUt(4, "Mening: " + meningsvektor.get(k));
        // }
        return meningsvektor;
    }
    
/*            helaTexten
 public static Vector<String> meningarna
 *
 */


    /* OperatorCall testar att anropa en funktion med en operator och dess
     * argument. I nuläget så skrivs endast operationen och argumenten ut.
     */

    public String OperatorCall( PDFOperator op, List arguments )
    {
        String retstr = op.toString();
        for (int k=0; k < arguments.size(); k++)
        {
            retstr += " ," + arguments.get(k);
        }
        return retstr;
    }
    
    /*
     * setTokenList förmedlar TokenList från andra klasser till denna klass.
     */

    public static void setTokenList( List tokens )
    {
        tokenlist = tokens;
    }

     /*
     * getTokenList returnerar TokenList.
     */

    public static List getTokenList()
    {
        return tokenlist;
    }

    /*
     *  addToArray skall addera en SECosstr till ett COSArray textblock.
     */

    public void addToArray(SECosstr SEC )
    {
          setb = new SETextBlock();
          setb.Carray.add(SEC);
          setb.IsArray = true;
          TBVector.add( TBIndex, setb);
    }

    /*
     * addToArray med nummer som indata lägger till ett nummer till arrayen.
     * 
     */

    public void addToArray(SEArrayNumber SEAN )
    {
          setb = new SETextBlock();
          setb.Carray.add(SEAN);
          setb.IsArray = true;
          TBVector.add( TBIndex, setb);
    }

    /* Funktion genom vilken aktuella textdata kan extraheras.
     * process the decoded text
     */

    public void processTextPosition( TextPosition tp)
    {
         // if (fas == PDFStreamEngine.splitstrings)

         String tpstr = tp.getCharacter();
         SkrivUt(5, "TextPosition: " + tpstr + " len: " + tpstr.length());
         SkrivUt(5, "spaceWidth: " + tp.getWidthOfSpace() );
         SkrivUt(5, "Width: " + tp.getWidth()); // För TP, inte string!?
         int tempcurrstrlen = currstringlen;
         currstringlen += tpstr.length();
         strtp += tpstr;
         //AH* Data som sparas om string.
         byte[] bytedata = bytestr;
         PDFont strfont = tp.getFont();
         float strfontsizeIP = tp.getFontSizeInPt();
         SkrivUt(5, "strfontsizeIP: " + tp.getFontSizeInPt());
         float strfontsizeVal = tp.getFontSize();
         SkrivUt(5, "strfontsizeVal: " + tp.getFontSize());
         float[] charwidth = tp.getIndividualWidths(); // array av char bredder.
         float maxFontHeight = tp.getHeight(); // Gäller alla tecken i fonten?
         // Dvs hela string?
         SkrivUt(5, "maxFontHeight: " + tp.getHeight());
         /* AH* Fel i bortkommenterat.
         if (firstInText) //Testa om första tp i string.
         {
             tpx = tp.getX(); // FEL, bara vid första TP i string!
             tpy = tp.getY(); // FEL, bara vid första TP i string!
             firstInText = false;
         }
          *
          */
         SkrivUt(5, "X: " + tpx + " Y: " + tpy);
         // int sumcharlen = 0;
         float sumcharAlla = 0; // Bredden för hela string.
         // float[] sumcharwidth = new float[1000];
         for (int k = 0; k < tpstr.length(); k++) // charwidth.length; k++ )
         {
             SkrivUt2(4, " iw " + charwidth[ k ]);
             sumcharwidth[ tempcurrstrlen + k ] = charwidth[k];
         }
         SkrivUt(5, "");
         if (currstringlen < stringlen) // Inte avslutad än.
         {
            // Fortsätt bara samla delsträngar.
         } else if(currstringlen == stringlen)
         {
             SkrivUt(5, "TextPosition hela: " + strtp);
             for (int k = 0; k < stringlen; k++ )
             {
                 SkrivUt2(4, " iw " + sumcharwidth[ k ]);
                 sumcharAlla += sumcharwidth[ k ];
             }
             SkrivUt(5, "\nSumcharAlla: " + sumcharAlla);
             // Ta hand om resultat string...
             // Hanteras på annat håll: helaTexten += strtp;

             // Spara data till en Cosstring post.

             SEC = new SECosstr(
                0, // int mennrin
                0, //int CARRind
                strtp, // String cosstrin,
                bytestr, // bytedatain,
                strfont, // PDFont strfontin,
                strfontsizeIP, // float strfontsizeIPin,
                strfontsizeVal,// float strfontsizeValin,
                sumcharwidth, // float[] charwidthin,
                maxFontHeight,// float maxFontHeightin,
                sumcharAlla,// float sumcharAllain,
                tpx,// float xin,
                tpy // float yin
                );

             if (ISArray) // Lägg till till COSArrayen.
             {
                 addToArray( SEC );
             } else // Lägg till COSString till TB vektorn.
             {
                 setb = new SETextBlock();
                 setb.cstr = SEC;
                 setb.IsArray = false;
                 TBVector.add( TBIndex, setb);
             }

             strtp = "";
             currstringlen = 0;
             setSumcharAlla( 0 );

         } else // (currstringlen > stringlen) ny string eftersom längden överskrider!
         {
            SkrivUt(5, "För stor string i processTextPosition.");
            SkrivUt(5, "Text: " + strtp + "currlen: " + currstringlen);
             strtp = "";
             currstringlen = 0;
             setSumcharAlla( 0 );
         }
     }
    
     /*               new TextPosition(
                            page,
                            textMatrixStDisp,
                            textMatrixEndDisp,
                            totalVerticalDisplacementDisp,
                            individualWidths,
                            spaceWidthDisp,
                            characterBuffer.toString(),
                            font,
                            fontSizeText,
                            (int)(fontSizeText * textMatrix.getXScale()),
                            wordSpacingDisp ));
      *
      */

     public void setSumcharAlla( float val)
    {
        sumcharAlla = val;
    }

     public float getSumcharAlla()
    {
         return sumcharAlla;
    }
     
/*
 *   getHelaTexten hämtar HelaTexten från en sida.
 */     
    public String getHelaTexten(COSStream cosStream)
    {                
        String HTexten = "";
        String string = "";

        try
        {
                List tokens = cosStream.getStreamTokens();
                // List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                    // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                SkrivUt(2, "token-size: " + tokens.size()); // xxx
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            string = previous.getString();
                            // SkrivUt(2, "Hittat Tj. String: " + string); // xxx
                            HTexten += string;
                            // string = string.replaceFirst( strToFind, message );
                            // previous.reset();
                            // previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                         /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                           */
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    string = cosString.getString();
                                    // SkrivUt(2, "Hittat TJ. String: " + string); // xxx
                                    HTexten += string;
                                    // string = string.replaceFirst( strToFind, message );
                                    // cosString.reset();
                                    // cosString.append( string.getBytes() );
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                          */
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }
                //now that the tokens are updated we will replace the
                //page content stream.
                    /*
                PDStream updatedStream = new PDStream(doc);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                page.setContents( updatedStream );
                     *
                     */
        } catch (java.io.IOException ioe)
        {
            SkrivUt(4, "Exception i getHelaTexten.");
        }
        return HTexten;
    }

    /*
     * arraycopy kopierar en COSArray och returnerar innehållet.
     */

    public COSArray arraycopy(COSArray carr)
    {
        COSArray retcarr = new COSArray();
        for (int cind=0; cind< carr.size(); cind++)
        {
            retcarr.add(carr.get(cind));
        }
        return retcarr;
    }

/*
 *   splitMeningar splittrar strings vid meningsskifte.
 *   Detta sker sidvis.
 *   Resterande av helaTexten returneras till nästa sida.
 *   mindex är aktuellt index i meningsvektorn.
 *   reststr är resten av meningen som är kvar för bearbetning av nästa sida.
 */
    
    public String splitMeningar(String reststr, COSStream cosStream)
    {
        SkrivUt(4, "Först i splitmeningar.");
        // Använd meningsvektor för att hämta meningar.
        // Metoden räknar bara tecknen i string och mening och antar att de är synkade
        // i övrigt.
        // ShowText Tj = new ShowText(); // Tj
        // ShowTextGlyph TJ = new ShowTextGlyph(); // TJ
        int mindex = mind;
        PDFOperator Tj = PDFOperator.getOperator("Tj");
        PDFOperator TJ = PDFOperator.getOperator("TJ");
        String restretur = ""; // Resterande text på meningsrad. En arbetsstruktur.
        String men; // Aktuell meningsstruktur.
        if (reststr != "") men = reststr;
        else men = meningsvektor.get(mindex).helameningen;
        // Återskrivet Hit!
        int menlen = men.length(); // Längden på den aktuella meningen.
        int mvektlen = meningsvektor.size(); // Storleken på vektorn.
        byte[] bytestr ; // string representerad som byte[]
        byte[] tempbstr; // byte[] för temporär lagring.
        int strlen; // Längden på den aktuella stringen.
        COSString prevny = null;
        boolean firsttime = true;
        boolean kapad = false;
        List tokens = null;
        try
        {
                tokens = cosStream.getStreamTokens();
                // pageTokens.add(tokens); Fungerande hantering.
                SkrivUt(4, "Splitmeningar, data innan split.");
                listTokens( tokens ); // Data innan split.
                // List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                SkrivUt(4, ">>> Split-Token size: " + tokens.size());
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            SkrivUt(4, "Tj hittad.");
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
                            prevny = null;
                            bytestr = previous.getBytes();
                            strlen = string.length();
                            restretur = "";
                            SkrivUt(4, "Men: " + men);
                            SkrivUt(4, "string: "+ string);
                            if (strlen > menlen)
                            {
                                firsttime=true;
                                // Hela meningen är kortare än string.
                                // => Stega mening tills strlen < menlen.
                                while (strlen > menlen) // Tills strlen <= menlen.
                                { // Använder while eftersom string kan sträcka sig över flera meningar.
                                    // I detta fall skall string kapas eftersom
                                    // String innehåller mer än en mening.
                                    // Resterande delen skall sparas som en egen string.
                                    // Minska strlen:
                                    SkrivUt(4,"while strlen > menlen");
                                    SkrivUt(4, "Men1: " + men);
                                    SkrivUt(4, "string1: "+ string);
                                    byte[] prevbytestr = new byte[ menlen];
                                    for (int it=0; it < menlen; it++)
                                    {  // Ta fram den delstring som motsvarar slutet på meningen.
                                        prevbytestr[ it ] = bytestr[ it ];
                                    }
                                    // Lagra tillbaka prevbytestr till posten.
                                    prevny = new COSString( prevbytestr );
                                    // prevny.reset();
                                    // prevny.append( prevbytestr );
                                    // Spara tillbaka kapad string till tokens.
                                    if (firsttime)
                                    {
                                        tokens.set(j-1, prevny);
                                        firsttime = false;
                                    }  else
                                    {
                                        tokens.add(j+1, Tj);
                                        tokens.add(j+1, prevny);
                                        j=j+2;
                                    }
                                    // Lägg in 0 g KVAR!
                                    tempbstr = new byte[ strlen - menlen];
                                    // Kapa bort meningen från string.
                                    for (int it = menlen; it <strlen; it++)
                                    {
                                        tempbstr[it - menlen] = bytestr[it];
                                    }
                                    bytestr = tempbstr; // spara resterande string.
                                    string = string.substring(menlen, strlen);
                                    strlen = strlen - menlen; // Nya längden på nya string
                                    // Stega index f mening. Kolla vektorlängd.
                                    // Stega j så att nästa string läggs in rätt!

                                    mindex++;
                                    if (mindex >= mvektlen)
                                    {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                    }
                                    // Stega själva meningen.
                                    men = (meningsvektor.get(mindex)).helameningen;
                                    menlen = men.length();
                                    SkrivUt(4, "Men2: " + men);
                                    SkrivUt(4, "string2: "+ string);
                                }
                                // Testa om strlen <= menlen.

                                if (strlen == menlen)
                                {
                                    // I detta fall skall data sparas tillbaka!
                                    // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                    // spara string.
                                    SkrivUt(4, "strlen == menlen e while");
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);
                                    prevny = new COSString( bytestr );
                                    // prevny.reset();
                                    // prevny.append( bytestr );
                                    tokens.add(j+1, Tj); // Spara Tj operatorn. Kolla index.
                                    tokens.add(j+1, prevny);
                                    j = j+3;
                                    // Kolla vektorlängd innan ökning.
                                    mindex++;
                                    if (mindex >= mvektlen)
                                    {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                    }
                                    men = (meningsvektor.get(mindex)).helameningen;
                                    menlen = men.length();
                                    // menlen = (meningsvektor.get(mindex)).length();
                                    // Leta efter nästa text.
                                } else if (strlen < menlen)
                                {
                                    // I detta fall skall data sparas tillbaka!
                                    // Hela string får plats i meningen.
                                    // Stega string. Behåll mening. Kolla om slut på sidan.
                                    SkrivUt(4, "strlen < menlen e while");
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);
                                    prevny = new COSString( bytestr );
                                    // prevny.reset();
                                    // prevny.append( bytestr );
                                    tokens.add(j+1, Tj); // Spara Tj operatorn. Kolla index!
                                    tokens.add(j+1, prevny);
                                    j=j+3;
                                    men = men.substring(strlen, menlen);
                                    menlen = menlen - strlen;
                                    // Kapa mening. Kolla om slut på sidan.
                                    // Spara en ev rest om det är slut på sidan!
                                    restretur = men;
                                    SkrivUt(4, "restreturStr0: "+ restretur);
                                }

                            } else if (strlen == menlen)
                            {
                                // I detta fall skall INGA data sparas tillbaka!
                                // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                // Kolla vektorlängd innan ökning.
                                SkrivUt(4, "strlen == menlen loopen");
                                SkrivUt(4, "Men: " + men);
                                SkrivUt(4, "string: "+ string);
                                /* Sparande tillbaka till strukturen behövs inte här!
                                 * KOLLAS!
                                prevny = new COSString();
                                prevny.reset();
                                prevny.append( bytestr );
                                tokens.add(j, Tj); // Spara Tj operatorn. Kolla index.
                                tokens.add(j, prevny);
                                j = j+2;

                                 */
                                // Kolla vektorlängd innan ökning.
                                mindex++;
                                if (mindex >= mvektlen)
                                {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                }
                                men = (meningsvektor.get(mindex)).helameningen;
                                menlen = men.length();
                                // menlen = (meningsvektor.get(mindex)).length();
                                // Leta efter nästa text.
                            } else if (strlen < menlen)
                            {
                                // I detta fall skall INGA data sparas tillbaka!
                                // Hela string får plats i meningen.
                                // Stega string. Behåll mening. Kolla om slut på sidan.
                                SkrivUt(4, "strlen < menlen loopen");
                                SkrivUt(4, "Men: " + men);
                                SkrivUt(4, "string: "+ string);
                                /* Data skall inte  sparas tillbaka här!
                                prevny = new COSString();
                                prevny.reset();
                                prevny.append( bytestr );
                                tokens.add(j, Tj); // Spara Tj operatorn. Kolla index.
                                tokens.add(j, prevny);
                                j = j+2;
                                 *
                                 */
                                men = men.substring(strlen, menlen);
                                menlen = menlen - strlen;
                                // Kapa mening. Kolla om slut på sidan.
                                // AH* Här borde ett returvärde sättas om sist på sidan.
                                restretur = men;
                                SkrivUt(4, "restreturStr1: "+ restretur);
       
                            }

                            // Kolla kommenterad kod.
                            // HTexten += string;
                            // string = string.replaceFirst( strToFind, message );
                            // previous.reset();
                            // previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                         /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                           */
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            SkrivUt(4, "TJ hittad.");
                            COSArray aktarray = new COSArray(); // Arbetsstrukturen
                            COSArray temparray = new COSArray(); // Arbetsstrukturen
                            COSArray nextarray = new COSArray();
                            kapad = false;
                            restretur = "";
                            int aktind = 0;
                            // för den aktuella arrayen.
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            firsttime = true;
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
                                    bytestr = ((COSString)arrElement).getBytes();
                                    strlen = string.length();
                                    prevny = null;
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);
                                    if (strlen > menlen)
                                    {
                                        // Hela meningen är kortare än string.
                                        // => Stega mening tills strlen < menlen.
                                        // firsttime = true; // Första gången i while loopen.
                                        while (strlen > menlen) // Tills strlen <= menlen.
                                        { // Använder while eftersom string kan sträcka sig över flera meningar.
                                            // I detta fall skall string o array kapas eftersom
                                            // String innehåller mer än en mening.
                                            // Resterande delen skall sparas som en egen string.
                                            // Minska strlen:
                                            SkrivUt(4, "TJ while strlen > menlen");
                                            SkrivUt(4, "Men1: " + men);
                                            SkrivUt(4, "string1: "+ string);
                                            byte[] prevbytestr = new byte[ menlen];
                                            for (int it=0; it < menlen; it++)
                                            {  // Ta fram den delstring som motsvarar slutet på meningen.
                                                prevbytestr[ it ] = bytestr[ it ];
                                            }
                                            // Lagra tillbaka prevbytestr till posten.
                                            // Sparandet behöver modifieras här!
                                            prevny = new COSString( prevbytestr );
                                            aktarray.add(prevny); // array som avslutas.
                                            //prevny.reset();
                                            // prevny.append( prevbytestr );
                                            // Spara tillbaka token:
                                            // modifiera nedanstående: För korrekt sparande.
                                            // Kopiera en ev rest av array till nextarray.
                                            // Detta utförs alltid vid kapning.
                                            nextarray.clear(); // Rensa ev tidigare data.

                                            // nextarray.add(prevrest); Resten av nuvarande string.
                                            
                                            for (int n=k+1; n < previous.size(); n++ )
                                            {
                                                nextarray.add(previous.get(n));
                                            } // Om slut, så kan nextarray vara tom!
                                            if (firsttime) // Modifiera existerande COSArray.
                                            {
                                                SkrivUt(4, "Firsttime = true.");
                                                temparray = arraycopy( aktarray );
                                                tokens.set(j-1, temparray ); // OK nu eftersom rest är sparad.
                                                SkrivUt(4, "token.SET . " + (j-1) + " " + aktarray.toString());
                                                // tokens.add(j, TJ); Finns ju redan på plats!
                                                firsttime = false;
                                                
                                            } else // Skapa en ny array och lägg in med TJ.
                                            {
                                                SkrivUt(4, "Firsttime = false.");
                                                tokens.add(j+1, TJ);
                                                SkrivUt(4, "token.add1 . " + (j+1) + " " + TJ.toString());
                                                temparray = arraycopy( aktarray );;
                                                tokens.add(j+1, temparray);
                                                SkrivUt(4, "token.add1 . " + (j+1) + " " + temparray.toString());
                                                j = j+2;  // AH* Ändrad.
                                            }
                                            aktarray.clear(); // Rensa till ny array.
                                            aktind = 0;
                                            // Lägg in 0 g KVAR!
                                            // Stega index f mening. Kolla vektorlängd.
                                            // Stega j så att nästa string läggs in rätt!
                                            // >> Stega kod flyttad härifrån!

                                            tempbstr = new byte[ strlen - menlen];
                                            // Kapa bort meningen från string.
                                            for (int it = menlen; it <strlen; it++)
                                            {
                                                tempbstr[it - menlen] = bytestr[it];
                                            }
                                            bytestr = tempbstr; // spara resterande string.
                                            string = string.substring(menlen, strlen);
                                            strlen = strlen - menlen; // Nya längden på nya string

                                            mindex++;
                                            if (mindex >= mvektlen)
                                            { // Skall något mer utföras innan sparande?
                                                sparaTokens( tokens );
                                                mind = mindex;
                                                return ""; // Fler sidor.
                                            }// Fler sidor.
                                            // Stega själva meningen.
                                            men = (meningsvektor.get(mindex)).helameningen;
                                            menlen = men.length();
                                            SkrivUt(4, "Men2: " + men);
                                            SkrivUt(4, "string2: "+ string);
                                        }
                                        // Fortsätt efter while.
                                        // Testa om strlen <= menlen.

                                        if (strlen == menlen)
                                        {
                                            // Stega både string och men.
                                            // Kolla om array slut. else kapa array.
                                            // Kolla först om man är klar med sidan/ dokumentet.
                                            // spara string.
                                            SkrivUt(4, "TJ strlen == menlen e while");
                                            SkrivUt(4, "Men: " + men);
                                            SkrivUt(4, "string: "+ string);
                                            // Sparandet behöver modifieras här:
                                            prevny = new COSString( bytestr );
                                            aktarray.add( prevny );
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add2 ." + (j+1) + " " + TJ.toString());
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add2 ." + (j+1) + " " + temparray.toString());
                                            j = j+2;
                                            aktarray.clear();
                                            aktind = 0;
                                            // Kolla vektorlängd innan ökning.
                                            mindex++;
                                            if (mindex >= mvektlen)
                                            {
                                                sparaTokens( tokens );
                                                mind = mindex;
                                                return ""; // Fler sidor.
                                            }
                                            men = (meningsvektor.get(mindex)).helameningen;
                                            menlen = men.length();
                                            // menlen = (meningsvektor.get(mindex)).length();
                                            // Leta efter nästa text.
                                        } else if (strlen < menlen)
                                        {
                                            // Hela string får plats i meningen.
                                            // Dvs splittra inte här! Bygg sparade data.
                                            // Stega string. Behåll mening. Kolla om slut på sidan.
                                            SkrivUt(4, "TJ strlen < menlen e while");
                                            SkrivUt(4, "Men: " + men);
                                            SkrivUt(4, "string: "+ string);
                                            SkrivUt(4, ">> bytestr: " + bytestr.toString());
                                            // Sparandet behöver modifieras här!
                                            prevny = new COSString( bytestr );
                                            aktarray.add(prevny);
                                            aktind++;
                                            // Spara data här om det är sista i arrayen!
                                            if (firsttime == false)
                                                 if ((k >= previous.size()-1) ||
                                                    ((k >= previous.size()-2) &&
                                                    (! (previous.get( k+1 ) instanceof COSString))))
                                            {
                                                SkrivUt(4, "Spara i TJ strlen < menlen loopen.");
                                                // prevny = new COSString( bytestr );
                                                // aktarray.add(prevny);
                                                // aktind++;
                                                // prevny.reset();
                                                // prevny.append( bytestr );
                                                tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                                SkrivUt(4, "token.add4 ." + (j+1) + " "+ TJ.toString());
                                                temparray = arraycopy( aktarray );
                                                tokens.add(j+1, temparray);
                                                SkrivUt(4, "token.add4 ." + (j+1) + " "+ temparray.toString());
                                                j = j+3;
                                                aktarray.clear();
                                                aktind = 0;
                                            }

                                            // INTE spara data här!?
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            /* tokens.add(j+1, TJ); // Spara TJ operatorn. Kolla index!
                                            tokens.add(j+1, prevny);
                                            j=j+3;
                                             * 
                                             */
                                            men = men.substring(strlen, menlen);
                                            menlen = menlen - strlen;
                                            // Kapa mening. Kolla om slut på sidan.
                                            // AH* Här borde ett returvärde sättas om sist på sidan.
                                            restretur = men;
                                            SkrivUt(4, "restretur0: "+ restretur);
                                        }

                                    } else if (strlen == menlen)
                                    {
                                        // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                        // Om det är slut på arrayen också behöver ingen splittring göras!
                                        // Else splittra arrayen!
                                        // Kolla vektorlängd innan ökning.
                                        SkrivUt(4, "TJ strlen == menlen loopen");
                                        SkrivUt(4, "Men: " + men);
                                        SkrivUt(4, "string: "+ string);
                                        // Ansats: Skall inte spara tillbaka data här!
                                        // >> Data skall sparas bara om detta INTE är sista i arrayen!
                                        // Om det är sista i arrayen skall det INTE sparas! Då finns
                                        // den ju redan!
                                        // Borde testa om alla efterkommande != COSString!
                                        // Tidigare kod:
                                        // if (!((k >= previous.size()-1) ||
                                        //        ((k >= previous.size()-2) &&
                                        //       (! (previous.get( k+1 ) instanceof COSString)))))
                                        // Behöver inte vara i slutet av arrayen för att kapa här!
                                         // Skall spara data till array alltid här!?
                                        // Kan data redan vara sparade här?
                                        //if // Villkor för sparandet.
                                        //{
                                        SkrivUt(4, "Spara i TJ strlen== menlen loopen.");
                                        prevny = new COSString( bytestr );
                                        aktarray.add(prevny);
                                        // aktind++;
                                        // prevny.reset();
                                        // prevny.append( bytestr );
                                        if (firsttime) // Testa denna!
                                        {
                                            temparray = arraycopy( aktarray );
                                            tokens.set(j-1, temparray);
                                            SkrivUt(4, "token.SET3 ." + (j-1) + " "+ temparray.toString());
                                            firsttime = false;
                                        } else
                                        {
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add3 ." + (j+1) + " "+ TJ.toString());
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add3 ." + (j+1) + " "+ temparray.toString());
                                            j = j+2;
                                        }
                                        aktarray.clear();
                                        aktind = 0;
                                        //} else SkrivUt(4, "Sparar INTE data i TJ strlen == menlen loopen.");
                                        // Kolla vektorlängd innan ökning.
                                        mindex++;
                                        if (mindex >= mvektlen)
                                        {
                                            sparaTokens( tokens );
                                            mind = mindex;
                                            return ""; // Fler sidor.
                                        }
                                        men = (meningsvektor.get(mindex)).helameningen;
                                        menlen = men.length();
                                        // menlen = (meningsvektor.get(mindex)).length();
                                        // Leta efter nästa text.
                                    } else if (strlen < menlen)
                                    {
                                        // Hela string får plats i meningen.
                                        // Stega string. Behåll mening. Kolla om slut på sidan.
                                        // Behöver inte splittra arrayen här!
                                        // Kan behöva spara denna string till arbetsarrayen.
                                        SkrivUt(4, "TJ strlen < menlen loopen");
                                        SkrivUt(4, "Men: " + men);
                                        SkrivUt(4, "string: "+ string);
                                        SkrivUt(4, ">> bytestr: " + bytestr.toString());
                                        // Ansats: Spara inte tillbaka data här!
                                        prevny = new COSString( bytestr );
                                        aktarray.add(prevny);
                                        aktind++;
                                        // prevny.reset();
                                        // prevny.append( bytestr );
                                        /*
                                        tokens.add(j, TJ); // Spara Tj operatorn. Kolla index.
                                        tokens.add(j, prevny);
                                        j = j+2;
                                         *
                                         */
                                        // Från (while strlen < menlen.
                                        // Om firsttime == true så skall data INTE sparas eftersom den
                                        // redan är sparad.
                                        // Skall denna kod vara med?
                                        if (firsttime == false)
                                            if ((k >= previous.size()-1) ||
                                                ((k >= previous.size()-2) &&
                                                (! (previous.get( k+1 ) instanceof COSString))))
                                        {
                                            SkrivUt(4, "Spara i TJ strlen < menlen loopen.");
                                            // prevny = new COSString( bytestr );
                                            // aktarray.add(prevny);
                                            // aktind++;
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add5 ." + (j+1) + " "+ TJ.toString());
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add5 ." + (j+1) + " "+ temparray.toString());
                                            j = j+2;
                                            aktarray.clear();
                                            aktind = 0;
                                        }
                                        
                                        
    
                                        men = men.substring(strlen, menlen);
                                        menlen = menlen - strlen;
                                        // AH* Här borde ett returvärde sättas om sist på sidan.
                                        restretur = men;
                                        SkrivUt(4, "restretur0: "+ restretur);
                                        // Kapa mening. Kolla om slut på sidan.

                                    }
                                    // HTexten += string;
                                    // string = string.replaceFirst( strToFind, message );
                                    // cosString.reset();
                                    // cosString.append( string.getBytes() );
                                } else if ( arrElement instanceof COSNumber)
                                {
                                    // Testa om det finns nr.
                                    // Spara arrElement i aktarray!
                                    // Tester visar att det är COSNumber som hanteras!
                                    aktarray.add( (COSNumber)arrElement );
                                    aktind++;
                                    SkrivUt(4, "COSNumber i splitMeningar.");
                                } else if ( arrElement instanceof COSInteger)
                                {
                                    // Testa om det finns int.
                                    // Spara arrElement i aktarray!
                                    aktarray.add( (COSInteger)arrElement );
                                    aktind++;
                                    SkrivUt(4, "COSInteger i splitMeningar.");
                                } else if ( arrElement instanceof COSFloat)
                                {
                                    // Testa om det finns float.
                                    // Spara arrElement i aktarray!
                                    aktarray.add( (COSFloat)arrElement );
                                    aktind++;
                                    SkrivUt(4, "COSFloat i splitMeningar.");
                                } else 
                                {
                                    // Spara data till aktarray. Kolla vilken typ det är!
                                    SkrivUt(4, "Okänd typ arrel i split: " + arrElement.toString());
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                          */
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }
                //now that the tokens are updated we will replace the
                //page content stream.
                
                SkrivUt(4, ">>> Före spara tokens i splitMeningar.");
                
                PDStream updatedStream = new PDStream(doc1);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                page1.setContents( updatedStream );
                
                SkrivUt(4, ">>> Efter spara tokens i splitMeningar.");
                
        } catch (java.io.IOException ioe)
        {
            SkrivUt(0, "Exception i getHelaTexten.");
        }
        // Sparad skräpkod:
        // cosStream.setStreamTokens( tokens );
        // tokens = cosStream.getStreamTokens();
        mind = mindex;
        // Borde spara tokens här!?
        // SkrivUt(4, "restretur: " + restretur);
        // SkrivUt(4, "Splitmeningar, data efter split.");
        // listTokens( tokens ); // Data innan split.
        return restretur;
}

/*
 *   byggStrukturer bygger upp både menings och TB strukturer.
 *   Detta sker sidvis.
 *   Resterande av helaTexten returneras till nästa sida.
 *   mindex är aktuellt index i meningsvektorn.
 *   reststr är resten av meningen som är kvar för bearbetning av nästa sida.
 */

    public String byggStrukturer(String reststr, COSStream cosStream, int pageind)
    {
        SkrivUt(4, "Först i byggstrukturer.");
        // Använd meningsvektor för att hämta meningar.
        // Metoden räknar bara tecknen i string och mening och antar att de är synkade
        // i övrigt.
        // ShowText Tj = new ShowText(); // Tj
        // ShowTextGlyph TJ = new ShowTextGlyph(); // TJ
        int mindex = mind;
        PDFOperator Tj = PDFOperator.getOperator("Tj");
        PDFOperator TJ = PDFOperator.getOperator("TJ");
        String restretur = ""; // Resterande text på meningsrad. En arbetsstruktur.
        String men; // Aktuell meningsstruktur.
        if (reststr != "") men = reststr;
        else men = meningsvektor.get(mindex).helameningen;
        // Återskrivet Hit!
        int menlen = men.length(); // Längden på den aktuella meningen.
        int mvektlen = meningsvektor.size(); // Storleken på vektorn.
        SEmeningsdel semendel = new SEmeningsdel();
        SEmening semen = new SEmening();
        byte[] bytestr ; // string representerad som byte[]
        byte[] tempbstr; // byte[] för temporär lagring.
        int strlen; // Längden på den aktuella stringen.
        COSString prevny = null;
        boolean firsttime = true;
        boolean kapad = false;
        setb = new SETextBlock();
        SECosstr secostr; // Aktuell COSString struktur.
        Vector<SEArrayIndKlass> newCarray = null; // Aktuell COSArray.
        SEArrayNumber seaik = null; // Aktuellt nummer i COSArray
        List tokens = null;

        try
        {
                tokens = cosStream.getStreamTokens(); // AH* Detta skrivs över i nästa steg.
                // tokens = pageTokens.get( pageind ); Fungerade tidigare.
                SkrivUt(4, "Först i byggstrukturer, listToken.");
                listTokens( tokens ); // Data innan byggstrukturer.
                // List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                SkrivUt(4, ">>> Split-Token size: " + tokens.size());
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            SkrivUt(4, "Tj hittad.");
                            setb = new SETextBlock();
                            setb.IsArray = false;
                            setb.pagenr = tbpagenr;
                            setb.index = j;

                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
                            secostr = new SECosstr( mindex, 0, string );

                            setb.cstr = secostr;
                            TBVector.add(setb);

                            semendel = new SEmeningsdel();
                            semendel.pagenr = tbpagenr;
                            semendel.tb = TBVector.size();
                            semendel.isarr = false;
                            semendel.arrind = 0;
                            semendel.deltext = string;
                            semen = meningsvektor.get(mindex);
                            semen.allaDelar.add(semendel);
                            // Kolla om detta räcker eller om det också skall
                            // tilldelas till meningsvektor igen!

                            prevny = null;
                            bytestr = previous.getBytes();
                            strlen = string.length();
                            restretur = "";
                            SkrivUt(4, "Men: " + men);
                            SkrivUt(4, "string: "+ string);
                            if (strlen > menlen)
                            {
                                SkrivUt(4, "**** strlen > menlen i byggstrukturer.");
                                firsttime=true;
                                // Hela meningen är kortare än string.
                                // => Stega mening tills strlen < menlen.
                                while (strlen > menlen) // Tills strlen <= menlen.
                                { // Använder while eftersom string kan sträcka sig över flera meningar.
                                    // I detta fall skall string kapas eftersom
                                    // String innehåller mer än en mening.
                                    // Resterande delen skall sparas som en egen string.
                                    // Minska strlen:
                                    SkrivUt(4, "while strlen > menlen");
                                    SkrivUt(4, "Men1: " + men);
                                    SkrivUt(4, "string1: "+ string);
                                    byte[] prevbytestr = new byte[ menlen];
                                    for (int it=0; it < menlen; it++)
                                    {  // Ta fram den delstring som motsvarar slutet på meningen.
                                        prevbytestr[ it ] = bytestr[ it ];
                                    }
                                    // Lagra tillbaka prevbytestr till posten.
                                    prevny = new COSString( prevbytestr );
                                    // prevny.reset();
                                    // prevny.append( prevbytestr );
                                    // Spara tillbaka kapad string till tokens.
                                    if (firsttime)
                                    {
                                        tokens.set(j-1, prevny);
                                        firsttime = false;
                                    }  else
                                    {
                                        tokens.add(j+1, Tj);
                                        tokens.add(j+1, prevny);
                                        j=j+2;
                                    }
                                    // Lägg in 0 g KVAR!
                                    tempbstr = new byte[ strlen - menlen];
                                    // Kapa bort meningen från string.
                                    for (int it = menlen; it <strlen; it++)
                                    {
                                        tempbstr[it - menlen] = bytestr[it];
                                    }
                                    bytestr = tempbstr; // spara resterande string.
                                    string = string.substring(menlen, strlen);
                                    strlen = strlen - menlen; // Nya längden på nya string
                                    // Stega index f mening. Kolla vektorlängd.
                                    // Stega j så att nästa string läggs in rätt!

                                    mindex++;
                                    if (mindex >= mvektlen)
                                    {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                    }
                                    // Stega själva meningen.
                                    men = (meningsvektor.get(mindex)).helameningen;
                                    menlen = men.length();
                                    SkrivUt(4, "Men2: " + men);
                                    SkrivUt(4, "string2: "+ string);
                                }
                                // Testa om strlen <= menlen.

                                if (strlen == menlen)
                                {
                                    // I detta fall skall data sparas tillbaka!
                                    // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                    // spara string.
                                    SkrivUt(4, "strlen == menlen e while");
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);
                                    prevny = new COSString( bytestr );
                                    // prevny.reset();
                                    // prevny.append( bytestr );
                                    tokens.add(j+1, Tj); // Spara Tj operatorn. Kolla index.
                                    tokens.add(j+1, prevny);
                                    j = j+3;
                                    // Kolla vektorlängd innan ökning.
                                    mindex++;
                                    if (mindex >= mvektlen)
                                    {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                    }
                                    men = (meningsvektor.get(mindex)).helameningen;
                                    menlen = men.length();
                                    // menlen = (meningsvektor.get(mindex)).length();
                                    // Leta efter nästa text.
                                } else if (strlen < menlen)
                                {
                                    // I detta fall skall data sparas tillbaka!
                                    // Hela string får plats i meningen.
                                    // Stega string. Behåll mening. Kolla om slut på sidan.
                                    SkrivUt(4, "strlen < menlen e while");
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);
                                    prevny = new COSString( bytestr );
                                    // prevny.reset();
                                    // prevny.append( bytestr );
                                    tokens.add(j+1, Tj); // Spara Tj operatorn. Kolla index!
                                    tokens.add(j+1, prevny);
                                    j=j+3;
                                    men = men.substring(strlen, menlen);
                                    menlen = menlen - strlen;
                                    // Kapa mening. Kolla om slut på sidan.
                                    // Spara en ev rest om det är slut på sidan!
                                    restretur = men;
                                    SkrivUt(4, "restreturStr0: "+ restretur);
                                }

                            } else if (strlen == menlen)
                            {
                                // I detta fall skall INGA data sparas tillbaka!
                                // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                // Kolla vektorlängd innan ökning.
                                SkrivUt(4, "strlen == menlen loopen");
                                SkrivUt(4, "Men: " + men);
                                SkrivUt(4, "string: "+ string);
                                /* Sparande tillbaka till strukturen behövs inte här!
                                 * KOLLAS!
                                prevny = new COSString();
                                prevny.reset();
                                prevny.append( bytestr );
                                tokens.add(j, Tj); // Spara Tj operatorn. Kolla index.
                                tokens.add(j, prevny);
                                j = j+2;

                                 */
                                // Kolla vektorlängd innan ökning.
                                mindex++;
                                if (mindex >= mvektlen)
                                {
                                        sparaTokens( tokens );
                                        mind = mindex;
                                        return ""; // Fler sidor.
                                }
                                men = (meningsvektor.get(mindex)).helameningen;
                                menlen = men.length();
                                // menlen = (meningsvektor.get(mindex)).length();
                                // Leta efter nästa text.
                            } else if (strlen < menlen)
                            {
                                // I detta fall skall INGA data sparas tillbaka!
                                // Hela string får plats i meningen.
                                // Stega string. Behåll mening. Kolla om slut på sidan.
                                SkrivUt(4, "strlen < menlen loopen");
                                SkrivUt(4, "Men: " + men);
                                SkrivUt(4, "string: "+ string);
                                /* Data skall inte  sparas tillbaka här!
                                prevny = new COSString();
                                prevny.reset();
                                prevny.append( bytestr );
                                tokens.add(j, Tj); // Spara Tj operatorn. Kolla index.
                                tokens.add(j, prevny);
                                j = j+2;
                                 *
                                 */
                                men = men.substring(strlen, menlen);
                                menlen = menlen - strlen;
                                // Kapa mening. Kolla om slut på sidan.
                                // AH* Här borde ett returvärde sättas om sist på sidan.
                                restretur = men;
                                SkrivUt(4, "restreturStr1: "+ restretur);

                            }

                            // Kolla kommenterad kod.
                            // HTexten += string;
                            // string = string.replaceFirst( strToFind, message );
                            // previous.reset();
                            // previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                         /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                           */
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            SkrivUt(4, "TJ hittad.");
                            setb = new SETextBlock();
                            setb.IsArray = true;
                            setb.pagenr = tbpagenr;
                            setb.index = j;
                            TBVector.add(setb);
                            newCarray = new Vector<SEArrayIndKlass>(); // COSArray
                            COSArray aktarray = new COSArray(); // Arbetsstrukturen
                            COSArray temparray = new COSArray(); // Arbetsstrukturen
                            COSArray nextarray = new COSArray();
                            kapad = false;
                            restretur = "";
                            int aktind = 0;
                            // för den aktuella arrayen.
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            firsttime = true;
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
                                    bytestr = ((COSString)arrElement).getBytes();
                                    strlen = string.length();
                                    prevny = null;
                                    // Datastrukturer byggs upp:
                                    secostr = new SECosstr( mindex, k, string);
                                    newCarray.add(secostr);
                                    setb.Carray = newCarray; // COSArray data.
                                    SkrivUt(4, "Men: " + men);
                                    SkrivUt(4, "string: "+ string);

                                    semendel = new SEmeningsdel();
                                    semendel.pagenr = tbpagenr;
                                    semendel.tb = TBVector.size();
                                    semendel.isarr = true;
                                    semendel.arrind = k;
                                    semendel.deltext = string;
                                    semen = meningsvektor.get(mindex);
                                    semen.allaDelar.add(semendel);
                                    // Kolla om detta räcker eller om det också skall
                                    // tilldelas till meningsvektor igen!

                                    if (strlen > menlen)
                                    {
                                        SkrivUt(4, "**** strlen > menlen i byggstrukturer.");
                                        // Hela meningen är kortare än string.
                                        // => Stega mening tills strlen < menlen.
                                        // firsttime = true; // Första gången i while loopen.
                                        while (strlen > menlen) // Tills strlen <= menlen.
                                        { // Använder while eftersom string kan sträcka sig över flera meningar.
                                            // I detta fall skall string o array kapas eftersom
                                            // String innehåller mer än en mening.
                                            // Resterande delen skall sparas som en egen string.
                                            // Minska strlen:
                                            SkrivUt(4, "TJ while strlen > menlen");
                                            SkrivUt(4, "Men1: " + men);
                                            SkrivUt(4, "string1: "+ string);
                                            byte[] prevbytestr = new byte[ menlen];
                                            for (int it=0; it < menlen; it++)
                                            {  // Ta fram den delstring som motsvarar slutet på meningen.
                                                prevbytestr[ it ] = bytestr[ it ];
                                            }
                                            // Lagra tillbaka prevbytestr till posten.
                                            // Sparandet behöver modifieras här!
                                            prevny = new COSString( prevbytestr );
                                            aktarray.add(prevny); // array som avslutas.
                                            //prevny.reset();
                                            // prevny.append( prevbytestr );
                                            // Spara tillbaka token:
                                            // modifiera nedanstående: För korrekt sparande.
                                            // Kopiera en ev rest av array till nextarray.
                                            // Detta utförs alltid vid kapning.
                                            nextarray.clear(); // Rensa ev tidigare data.

                                            // nextarray.add(prevrest); Resten av nuvarande string.

                                            for (int n=k+1; n < previous.size(); n++ )
                                            {
                                                nextarray.add(previous.get(n));
                                            } // Om slut, så kan nextarray vara tom!
                                            if (firsttime) // Modifiera existerande COSArray.
                                            {
                                                SkrivUt(4, "Firsttime = true.");
                                                temparray = arraycopy( aktarray );
                                                tokens.set(j-1, temparray ); // OK nu eftersom rest är sparad.
                                                SkrivUt(4, "token.SET . " + (j-1) + " " + aktarray.toString());
                                                // tokens.add(j, TJ); Finns ju redan på plats!
                                                firsttime = false;

                                            } else // Skapa en ny array och lägg in med TJ.
                                            {
                                                SkrivUt(4, "Firsttime = false.");
                                                tokens.add(j+1, TJ);
                                                SkrivUt(4, "token.add1 . " + (j+1) + " " + TJ.toString());
                                                temparray = arraycopy( aktarray );;
                                                tokens.add(j+1, temparray);
                                                SkrivUt(4, "token.add1 . " + (j+1) + " " + temparray.toString());
                                                j = j+2;  // AH* Ändrad.
                                            }
                                            aktarray.clear(); // Rensa till ny array.
                                            aktind = 0;
                                            // Lägg in 0 g KVAR!
                                            // Stega index f mening. Kolla vektorlängd.
                                            // Stega j så att nästa string läggs in rätt!
                                            // >> Stega kod flyttad härifrån!

                                            tempbstr = new byte[ strlen - menlen];
                                            // Kapa bort meningen från string.
                                            for (int it = menlen; it <strlen; it++)
                                            {
                                                tempbstr[it - menlen] = bytestr[it];
                                            }
                                            bytestr = tempbstr; // spara resterande string.
                                            string = string.substring(menlen, strlen);
                                            strlen = strlen - menlen; // Nya längden på nya string

                                            mindex++;
                                            if (mindex >= mvektlen)
                                            { // Skall något mer utföras innan sparande?
                                                sparaTokens( tokens );
                                                mind = mindex;
                                                return ""; // Fler sidor.
                                            }// Fler sidor.
                                            // Stega själva meningen.
                                            men = (meningsvektor.get(mindex)).helameningen;
                                            menlen = men.length();
                                            SkrivUt(4, "Men2: " + men);
                                            SkrivUt(4, "string2: "+ string);
                                        }
                                        // Fortsätt efter while.
                                        // Testa om strlen <= menlen.

                                        if (strlen == menlen)
                                        {
                                            // Stega både string och men.
                                            // Kolla om array slut. else kapa array.
                                            // Kolla först om man är klar med sidan/ dokumentet.
                                            // spara string.
                                            SkrivUt(4, "TJ strlen == menlen e while");
                                            SkrivUt(4, "Men: " + men);
                                            SkrivUt(4, "string: "+ string);
                                            // Sparandet behöver modifieras här:
                                            prevny = new COSString( bytestr );
                                            aktarray.add( prevny );
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add2 ." + (j+1) + " " + TJ.toString());
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add2 ." + (j+1) + " " + temparray.toString());
                                            j = j+2;
                                            aktarray.clear();
                                            aktind = 0;
                                            // Kolla vektorlängd innan ökning.
                                            mindex++;
                                            if (mindex >= mvektlen)
                                            {
                                                sparaTokens( tokens );
                                                mind = mindex;
                                                return ""; // Fler sidor.
                                            }
                                            men = (meningsvektor.get(mindex)).helameningen;
                                            menlen = men.length();
                                            // menlen = (meningsvektor.get(mindex)).length();
                                            // Leta efter nästa text.
                                        } else if (strlen < menlen)
                                        {
                                            // Hela string får plats i meningen.
                                            // Dvs splittra inte här! Bygg sparade data.
                                            // Stega string. Behåll mening. Kolla om slut på sidan.
                                            SkrivUt(4, "TJ strlen < menlen e while");
                                            SkrivUt(4, "Men: " + men);
                                            SkrivUt(4, "string: "+ string);
                                            SkrivUt(4, ">> bytestr: " + bytestr.toString());
                                            // Sparandet behöver modifieras här!
                                            prevny = new COSString( bytestr );
                                            aktarray.add(prevny);
                                            aktind++;
                                            // Spara data här om det är sista i arrayen!
                                            if (firsttime == false)
                                                 if ((k >= previous.size()-1) ||
                                                    ((k >= previous.size()-2) &&
                                                    (! (previous.get( k+1 ) instanceof COSString))))
                                            {
                                                SkrivUt(4, "Spara i TJ strlen < menlen loopen.");
                                                // prevny = new COSString( bytestr );
                                                // aktarray.add(prevny);
                                                // aktind++;
                                                // prevny.reset();
                                                // prevny.append( bytestr );
                                                tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                                SkrivUt(4, "token.add4 ." + (j+1) + " "+ TJ.toString());
                                                temparray = arraycopy( aktarray );
                                                tokens.add(j+1, temparray);
                                                SkrivUt(4, "token.add4 ." + (j+1) + " "+ temparray.toString());
                                                j = j+3;
                                                aktarray.clear();
                                                aktind = 0;
                                            }

                                            // INTE spara data här!?
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            /* tokens.add(j+1, TJ); // Spara TJ operatorn. Kolla index!
                                            tokens.add(j+1, prevny);
                                            j=j+3;
                                             *
                                             */
                                            men = men.substring(strlen, menlen);
                                            menlen = menlen - strlen;
                                            // Kapa mening. Kolla om slut på sidan.
                                            // AH* Här borde ett returvärde sättas om sist på sidan.
                                            restretur = men;
                                            SkrivUt(4, "restretur0: "+ restretur);
                                        }

                                    } else if (strlen == menlen)
                                    {
                                        // Stega båda. Kolla först om man är klar med sidan/ dokumentet.
                                        // Om det är slut på arrayen också behöver ingen splittring göras!
                                        // Else splittra arrayen!
                                        // Kolla vektorlängd innan ökning.
                                        SkrivUt(4, "TJ strlen == menlen loopen");
                                        SkrivUt(4, "Men: " + men);
                                        SkrivUt(4, "string: "+ string);
                                        // Ansats: Skall inte spara tillbaka data här!
                                        // >> Data skall sparas bara om detta INTE är sista i arrayen!
                                        // Om det är sista i arrayen skall det INTE sparas! Då finns
                                        // den ju redan!
                                        // Borde testa om alla efterkommande != COSString!
                                        // Tidigare kod:
                                        // if (!((k >= previous.size()-1) ||
                                        //        ((k >= previous.size()-2) &&
                                        //       (! (previous.get( k+1 ) instanceof COSString)))))
                                        // Behöver inte vara i slutet av arrayen för att kapa här!
                                         // Skall spara data till array alltid här!?
                                        // Kan data redan vara sparade här?
                                        //if // Villkor för sparandet.
                                        //{
                                        SkrivUt(4, "Spara i TJ strlen== menlen loopen.");
                                        prevny = new COSString( bytestr );
                                        aktarray.add(prevny);
                                        // aktind++;
                                        // prevny.reset();
                                        // prevny.append( bytestr );
                                        if (firsttime) // Testa denna!
                                        {
                                            temparray = arraycopy( aktarray );
                                            tokens.set(j-1, temparray);
                                            SkrivUt(4, "token.SET3 ." + (j-1) + " "+ temparray.toString());
                                            firsttime = false;
                                        } else
                                        {
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add3 ." + (j+1) + " "+ TJ.toString());
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add3 ." + (j+1) + " "+ temparray.toString());
                                            j = j+2;
                                        }
                                        aktarray.clear();
                                        aktind = 0;
                                        //} else SkrivUt(4, "Sparar INTE data i TJ strlen == menlen loopen.");
                                        // Kolla vektorlängd innan ökning.
                                        mindex++;
                                        if (mindex >= mvektlen)
                                        {
                                            sparaTokens( tokens );
                                            mind = mindex;
                                            return ""; // Fler sidor.
                                        }
                                        men = (meningsvektor.get(mindex)).helameningen;
                                        menlen = men.length();
                                        // menlen = (meningsvektor.get(mindex)).length();
                                        // Leta efter nästa text.
                                    } else if (strlen < menlen)
                                    {
                                        // Hela string får plats i meningen.
                                        // Stega string. Behåll mening. Kolla om slut på sidan.
                                        // Behöver inte splittra arrayen här!
                                        // Kan behöva spara denna string till arbetsarrayen.
                                        SkrivUt(4, "TJ strlen < menlen loopen");
                                        SkrivUt(4, "Men: " + men);
                                        SkrivUt(4, "string: "+ string);
                                        SkrivUt(4, ">> bytestr: " + bytestr.toString());
                                        // Ansats: Spara inte tillbaka data här!
                                        prevny = new COSString( bytestr );
                                        aktarray.add(prevny);
                                        aktind++;
                                        // prevny.reset();
                                        // prevny.append( bytestr );
                                        /*
                                        tokens.add(j, TJ); // Spara Tj operatorn. Kolla index.
                                        tokens.add(j, prevny);
                                        j = j+2;
                                         *
                                         */
                                        // Från (while strlen < menlen.
                                        // Om firsttime == true så skall data INTE sparas eftersom den
                                        // redan är sparad.
                                        // Skall denna kod vara med?
                                        if (firsttime == false)
                                            if ((k >= previous.size()-1) ||
                                                ((k >= previous.size()-2) &&
                                                (! (previous.get( k+1 ) instanceof COSString))))
                                        {
                                            SkrivUt(4, "Spara i TJ strlen < menlen loopen.");
                                            // prevny = new COSString( bytestr );
                                            // aktarray.add(prevny);
                                            // aktind++;
                                            // prevny.reset();
                                            // prevny.append( bytestr );
                                            tokens.add(j+1, TJ); // Spara Tj operatorn. Kolla index.
                                            SkrivUt(4, "token.add5 ." + (j+1) + " "+ TJ.toString());
                                            temparray = arraycopy( aktarray );
                                            tokens.add(j+1, temparray);
                                            SkrivUt(4, "token.add5 ." + (j+1) + " "+ temparray.toString());
                                            j = j+2;
                                            aktarray.clear();
                                            aktind = 0;
                                        }



                                        men = men.substring(strlen, menlen);
                                        menlen = menlen - strlen;
                                        // AH* Här borde ett returvärde sättas om sist på sidan.
                                        restretur = men;
                                        SkrivUt(4, "restretur0: "+ restretur);
                                        // Kapa mening. Kolla om slut på sidan.

                                    }
                                    // HTexten += string;
                                    // string = string.replaceFirst( strToFind, message );
                                    // cosString.reset();
                                    // cosString.append( string.getBytes() );
                                } else if ( arrElement instanceof COSNumber)
                                {
                                    // Testa om det finns nr.
                                    // Spara arrElement i aktarray!
                                    // Tester visar att det är COSNumber som hanteras!
                                    aktarray.add( (COSNumber)arrElement );
                                    aktind++;
                                    int temp = ((COSNumber)arrElement).intValue();
                                    seaik = new SEArrayNumber( temp );
                                    newCarray.add(seaik);
                                    SkrivUt(4, "COSNumber i splitMeningar.");
                                } else if ( arrElement instanceof COSInteger)
                                {
                                    // seaik = ((COSInteger)arrElement)
                                    // Testa om det finns int.
                                    // Spara arrElement i aktarray!
                                    aktarray.add( (COSInteger)arrElement );
                                    aktind++;
                                    int temp = ((COSNumber)arrElement).intValue();
                                    seaik = new SEArrayNumber( temp );
                                    newCarray.add(seaik);
                                    SkrivUt(4, "COSInteger i splitMeningar.");
                                } else if ( arrElement instanceof COSFloat)
                                {
                                    // Testa om det finns float.
                                    // Spara arrElement i aktarray!
                                    aktarray.add( (COSFloat)arrElement );
                                    aktind++;
                                    SkrivUt(4, "COSFloat i splitMeningar.");
                                } else
                                {
                                    // Spara data till aktarray. Kolla vilken typ det är!
                                    SkrivUt(4, "Okänd typ arrel i split: " + arrElement.toString());
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                          */
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }
                //now that the tokens are updated we will replace the
                //page content stream.
                
                SkrivUt(4, ">>> Före spara tokens i byggStrukturer.");
                
                PDStream updatedStream = new PDStream(doc1);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                page1.setContents( updatedStream );

                SkrivUt(4, ">>> Efter spara tokens i byggStrukturer.");
                
        } catch (java.io.IOException ioe)
        {
            SkrivUt(4, "Exception i getHelaTexten.");
        }
        // Sparad skräpkod:
        // cosStream.setStreamTokens( tokens );
        // tokens = cosStream.getStreamTokens();
        mind = mindex;
        // Borde spara tokens här!?
        SkrivUt(4, "restretur: " + restretur);
        return restretur;

}

    /* grayInsert skall lägga till gray operatorer och float argument före varje TJ och Tj.
     * Detta sker sidvis.
    *   Resterande av helaTexten returneras till nästa sida.
    *   mindex är aktuellt index i meningsvektorn.
    *   reststr är resten av meningen som är kvar för bearbetning av nästa sida.
    */

    public String grayInsert(String reststr, COSStream cosStream, int pageind)
    {
        SkrivUt(4, "Först i grayInsert.");
        // Använd meningsvektor för att hämta meningar.
        // Metoden räknar bara tecknen i string och mening och antar att de är synkade
        // i övrigt.

        // ShowText Tj = new ShowText(); // Tj
        // ShowTextGlyph TJ = new ShowTextGlyph(); // TJ
        int mindex = mind;
        PDFOperator Tj = PDFOperator.getOperator("Tj");
        PDFOperator TJ = PDFOperator.getOperator("TJ");
        PDFOperator g = PDFOperator.getOperator("g");
        COSFloat gval = null;
        try
        {
            gval = new COSFloat("0.0");
        } catch (IOException ioec)
        {
            SkrivUt(0,"Fel på gval!");
        }
        String restretur = ""; // Resterande text på meningsrad. En arbetsstruktur.
        String men; // Aktuell meningsstruktur.
        if (reststr != "") men = reststr;
        else men = meningsvektor.get(mindex).helameningen;
        // Återskrivet Hit!
        int menlen = men.length(); // Längden på den aktuella meningen.
        int mvektlen = meningsvektor.size(); // Storleken på vektorn.
        byte[] bytestr ; // string representerad som byte[]
        byte[] tempbstr; // byte[] för temporär lagring.
        int strlen; // Längden på den aktuella stringen.
        COSString prevny = null;
        boolean firsttime = true;
        boolean kapad = false;
        List tokens = null;
        try
        {
                SkrivUt(4, "Först i grayInsert.");
                tokens = cosStream.getStreamTokens(); // AH* Hämtar tokens från strukturen istället.
                // Dessa data skrivs över här:
                // tokens = pageTokens.get( pageind ); Fungerade tidigare.
                listTokens( tokens ); // Data innan gray.
                // List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                SkrivUt(4, ">>> AddGray-Token size: " + tokens.size());
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            SkrivUt(4, "Tj hittad.");
                            // Lägg till gray operator och argument!
                            tokens.add(j-1, g);
                            tokens.add(j-1, gval);
                            j= j+2;
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
                            prevny = null;
                            bytestr = previous.getBytes();
                            strlen = string.length();
                            //SkrivUt(4, "Men: " + men);
                            //SkrivUt(4, "string: "+ string);

                            // Kolla kommenterad kod.
                            // HTexten += string;
                            // string = string.replaceFirst( strToFind, message );
                            // previous.reset();
                            // previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                         /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                           */
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            SkrivUt(4, "TJ hittad.");
                            // Lägg till gray operator och argument!
                            tokens.add(j-1, g);
                            tokens.add(j-1, gval);
                            j = j+2;
                            restretur = "";
                            int aktind = 0;
                            // för den aktuella arrayen.
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            firsttime = true;
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
                                    bytestr = ((COSString)arrElement).getBytes();
                                    strlen = string.length();
                                    prevny = null;
                                    // SkrivUt(4, "Men: " + men);
                                    // SkrivUt(4, "string: "+ string);
     
                                    // HTexten += string;
                                    // string = string.replaceFirst( strToFind, message );
                                    // cosString.reset();
                                    // cosString.append( string.getBytes() );
                                } else if ( arrElement instanceof COSNumber)
                                {
                                    // Testa om det finns nr.
                                    // Spara arrElement i aktarray!
                                    // Tester visar att det är COSNumber som hanteras!
                                    // aktarray.add( (COSNumber)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSNumber i splitMeningar.");
                                } else if ( arrElement instanceof COSInteger)
                                {
                                    // Testa om det finns int.
                                    // Spara arrElement i aktarray!
                                    // aktarray.add( (COSInteger)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSInteger i splitMeningar.");
                                } else if ( arrElement instanceof COSFloat)
                                {
                                    // Testa om det finns float.
                                    // Spara arrElement i aktarray!
                                    // aktarray.add( (COSFloat)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSFloat i splitMeningar.");
                                } else
                                {
                                    // Spara data till aktarray. Kolla vilken typ det är!
                                    SkrivUt(4, "Okänd typ arrel i addGrey: " + arrElement.toString());
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                          */
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }
                //now that the tokens are updated we will replace the
                //page content stream.
                
                SkrivUt(4, ">>> Före spara tokens i grayInsert.");
                
                PDStream updatedStream = new PDStream(doc1);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                page1.setContents( updatedStream );
                
                SkrivUt(4, ">>> Efter spara tokens i grayInsert.");
                
        } catch (java.io.IOException ioe)
        {
            SkrivUt(4, "Exception i addGrey .");
        }
        // Sparad skräpkod:
        // cosStream.setStreamTokens( tokens );
        // tokens = cosStream.getStreamTokens();
        mind = mindex;
        // Borde spara tokens här!?
        SkrivUt(4, "ListTokens efter grayInsert.");
        listTokens( tokens );
        return restretur;

}

    /* highlight skall ändra de float argument till gray operatorer före varje TJ och Tj.
     * Detta sker sidvis.
    *   Resterande av helaTexten returneras till nästa sida.
    *   mindex är aktuellt index i meningsvektorn.
    *   reststr är resten av meningen som är kvar för bearbetning av nästa sida.
    */

    public String highlight(String reststr, COSStream cosStream, 
            int pageind, boolean DoHighlight, int valdmening)
    {
        SkrivUt(1, "Först i highlight.");
        // Använd meningsvektor för att hämta meningar.
        // Metoden räknar bara tecknen i string och mening och antar att de är synkade
        // i övrigt.

        // ShowText Tj = new ShowText(); // Tj
        // ShowTextGlyph TJ = new ShowTextGlyph(); // TJ
        int mindex = mind;
        PDFOperator Tj = PDFOperator.getOperator("Tj");
        PDFOperator TJ = PDFOperator.getOperator("TJ");
        PDFOperator g = PDFOperator.getOperator("g");
        PDFOperator G = PDFOperator.getOperator("G");
        COSFloat gval = null;
        COSFloat g2val = null;
        COSFloat etta = null;
        COSFloat nolla = null;
        COSInteger två = null;
        COSFloat red = null;
        COSFloat green = null;
        PDFOperator rg = PDFOperator.getOperator("rg");
        PDFOperator RG = PDFOperator.getOperator("RG");
        PDFOperator Tr = PDFOperator.getOperator("Tr");

        try
        {
            gval = new COSFloat("0.0");
            g2val = new COSFloat("0.6"); // AH* Här ställs sammanfattningsgraden!
            etta = new COSFloat("1.0");
            nolla = new COSFloat("0.0");
            två = new COSInteger("2");
            red = new COSFloat("0.753");
            green = new COSFloat("1.0");
        } catch (IOException ioec)
        {
            SkrivUt(0, "Fel på gval!");
        }
        String restretur = ""; // Resterande text på meningsrad. En arbetsstruktur.
        String men; // Aktuell meningsstruktur.
        if (reststr == null ? "" != null : !reststr.equals("")) men = reststr;
        else men = meningsvektor.get(mindex).helameningen;
        // Återskrivet Hit!
        int menlen = men.length(); // Längden på den aktuella meningen.
        int mvektlen = meningsvektor.size(); // Storleken på vektorn.
        byte[] bytestr ; // string representerad som byte[]
        byte[] tempbstr; // byte[] för temporär lagring.
        int strlen; // Längden på den aktuella stringen.
        COSString prevny = null;
        boolean firsttime = true;
        boolean kapad = false;
        List tokens = null;
        int tempmennr = 0;
        String teststr = "";
        try
        {
                SkrivUt( 1, "Andra i highlight.");
                tokens = cosStream.getStreamTokens();
                pageTokens.add( pageind, tokens );
                // tokens =  Fungerade tidigare.
                // listTokens( tokens ); // Data innan split.
                // List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                SkrivUt(1, ">>> AddGray-Token size: " + tokens.size());
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    // SkrivUt(1, "Tokennr: " + j);
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            SkrivUt(1, "Tj hittad.");
                            // Lägg till gray operator och argument!
                            // tokens.add(j-1, g);
                            // Här skall sammanfattningen göras!
                            tempmennr = mennr;
                            if (DoHighlight && !medisammanfattningen( false, pageind, valdmening )) // Ej Array.
                            {
                                tokens.set(j-3, g2val);
                                SkrivUt(2, "EJ Highlight men:" + tempmennr);
                            } else SkrivUt(2, "Highlight men:" + tempmennr);
                            mindex = mennr;
                            if (mennr >= meningsvektor.size())
                                return "";
                            SkrivUt(2, "mennr: " + tempmennr + " valdmening: " + valdmening);
                            SkrivUt(2, "Meningen: " + meningsvektor.get(tempmennr).helameningen);
                            if ( tempmennr == valdmening ){
                                // Kolla först om det är en tom text => Inte highlight!
                                SkrivUt(2, "Mennr == valdmening.");
                                /* Senaste bortkommenterade.
                                if (valdsida == -1) //
                                { // Tilldela valdsida bara om den inte tilldelats tidigare.
                                    // Skall också kolla om det bara är blanka i meningsdelen 
                                    // på denna sida.
                                    teststr = meningsvektor.get(tempmennr).allaDelar.get(mendelnr).deltext;
                                    SkrivUt(3, "xxxxxxxxx teststr: " + teststr);
                                    if (!baraBlanka( teststr ))
                                    {
                                        valdsida = pageind; // Detta är den valda sidan.
                                        SkrivUt(3, "*************************** valdsida: " + valdsida);
                                    } else SkrivUt(3, "bara blanka i deltext till meniong: " + tempmennr);
                                } */
                                SkrivUt(3, ">>>>>>> valdmening:" + valdmening + " mennr: " + mennr);
                                tokens.add(j-1, rg);
                                tokens.add(j-1, nolla);
                                // tokens.add(j-1, green);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, red);
                                j += 4;
                                /*
                                tokens.add(j-1, RG);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, red);
                                tokens.add(j-1, rg);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, red);
                                 * 
                                 */

                                // Addera kod efter Tj:
                                /* tokens.add(j+9, G);
                                tokens.add(j+9, nolla);
                                tokens.add(j+9, g);
                                tokens.add(j+9, nolla);
                                j+= 13;
                                 * 
                                 */

                                // Kanske behöver sätta fler variabler.
                            }
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            restretur = "";
                            int aktind = 0;
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
                            prevny = null;
                            bytestr = previous.getBytes();
                            strlen = string.length();
                            //SkrivUt(4, "Men: " + men);
                            //SkrivUt(4, "string: "+ string);

                            // Kolla kommenterad kod.
                            // HTexten += string;
                            // string = string.replaceFirst( strToFind, message );
                            // previous.reset();
                            // previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                         /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                           */
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            SkrivUt(1, "TJ hittad.");
                            // Lägg till gray operator och argument!
                            // tokens.add(j-1, g);
                            // Här skall sammanfattningen göras!
                            tempmennr = mennr;
                            if (DoHighlight && !medisammanfattningen( true, pageind, valdmening )) // Array.
                            {
                                tokens.set(j-3, g2val);
                                SkrivUt(2, "EJ Highlight men:" + tempmennr);
                            } else
                                SkrivUt(2, "Highlight men:" + tempmennr);
                            mindex = mennr;
                            if (mennr >= meningsvektor.size())
                                return "";
                            SkrivUt(2, "mennr: " + tempmennr + " valdmening: " + valdmening);
                            SkrivUt(2, "Meningen: " + meningsvektor.get(tempmennr).helameningen);
                            if ( tempmennr == valdmening ) // .
                            { //valdmening
                                // Kolla först om det är en tom text => Inte highlight!
                                SkrivUt(2, "Mennr == valdmening.");
                                /* if (valdsida == -1) //
                                { // Tilldela valdsida bara om den inte tilldelats tidigare.
                                    // Skall också kolla om det bara är blanka i meningsdelen
                                    // på denna sida.
                                    teststr = meningsvektor.get(tempmennr).allaDelar.get(mendelnr).deltext;
                                    SkrivUt(3, "xxxxxxxxx teststr: " + teststr);
                                    if (!baraBlanka( teststr ))
                                    {
                                        valdsida = pageind; // Detta är den valda sidan.
                                        SkrivUt(3, "*************************** valdsida: " + valdsida);
                                    } else SkrivUt(3, "bara blanka i deltext till mening: " + tempmennr);
                                }
                                 *
                                 */
                                SkrivUt(3, ">>>>>>> valdmening:" + valdmening + " mennr: " + mennr);

                                //tokens.add(j-2, RG);
                                //tokens.add(j-2, nolla);
                                //tokens.add(j-2, nolla);
                                // tokens.add(j-2, red);

                                tokens.add(j-1, rg);
                                tokens.add(j-1, nolla);
                                // tokens.add(j-1, green);
                                tokens.add(j-1, nolla);
                                tokens.add(j-1, red);

                                // Addera kod efter Tj:
                                // tokens.add(j+9, G);
                                // tokens.add(j+9, nolla);
                                // tokens.add(j+9, g);
                                // tokens.add(j+9, nolla);
                                // j+= 13;
                                j += 4;
                                // Kanske behöver sätta fler variabler.
                            }
                            restretur = "";
                            int aktind = 0;
                            // för den aktuella arrayen.
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            firsttime = true;
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
                                    bytestr = ((COSString)arrElement).getBytes();
                                    strlen = string.length();
                                    prevny = null;
                                    // SkrivUt(4, "Men: " + men);
                                    // SkrivUt(4, "string: "+ string);

                                    // HTexten += string;
                                    // string = string.replaceFirst( strToFind, message );
                                    // cosString.reset();
                                    // cosString.append( string.getBytes() );
                                } else if ( arrElement instanceof COSNumber)
                                {
                                    // Testa om det finns nr.
                                    // Spara arrElement i aktarray!
                                    // Tester visar att det är COSNumber som hanteras!
                                    // aktarray.add( (COSNumber)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSNumber i splitMeningar.");
                                } else if ( arrElement instanceof COSInteger)
                                {
                                    // Testa om det finns int.
                                    // Spara arrElement i aktarray!
                                    // aktarray.add( (COSInteger)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSInteger i splitMeningar.");
                                } else if ( arrElement instanceof COSFloat)
                                {
                                    // Testa om det finns float.
                                    // Spara arrElement i aktarray!
                                    // aktarray.add( (COSFloat)arrElement );
                                    // aktind++;
                                    // SkrivUt(4, "COSFloat i splitMeningar.");
                                } else
                                {
                                    // Spara data till aktarray. Kolla vilken typ det är!
                                    SkrivUt(4, "Okänd typ arrel i addGrey: " + arrElement.toString());
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                          */
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }
                //now that the tokens are updated we will replace the
                //page content stream.
                
                SkrivUt(3, ">>> Före spara tokens i highlight.");
                PDStream updatedStream = new PDStream(doc1);
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( tokens );
                // if (updatedStream == null) SkrivUt(4, "updatedStream == null");
                if (page1 == null) SkrivUt(4, "page == null");
                page1.setContents( updatedStream );

                SkrivUt(3, ">>> Efter spara tokens i highlight.");

                 
        } catch (java.io.IOException ioe)
        {
            SkrivUt(0, "Exception i Highlight .");
        }
        // Sparad skräpkod:
        // cosStream.setStreamTokens( tokens );
        // tokens = cosStream.getStreamTokens();
        mind = mindex;
        // Borde spara tokens här!?
        // SkrivUt(4, "restretur: " + restretur);
        listTokens( tokens );
        return restretur;

}
    /*
     *  medisammanfattningen returnerar true om aktuell COS element skall
     *  vara med i sammanfattningen och false annars.
     *  Parametern meningsnummer (mennr) är inte inparameter eftersom
     *  den tilldelas i metoden.
     */

    public boolean medisammanfattningen(boolean isArray, int pageind, int valdmening )
    {
        // Om större än antalet meningars delar. => Visa inget(?)
        // För aktuellt meningsnr: Om mendelnr > mendelantal så stega
        // Mening!
        // if (true) return true;
        boolean isSameArray = false;
        int arrind = 0;
        int tbnr = 0; // Nummer på textblocket.
        SkrivUt(1, ">> medisammanfattningen anropad.");
        boolean medisam = false;
        String teststr = "";
        if ( mennr >= meningsvektor.size())
        {
            return false;
        }
        SEmening semen = meningsvektor.get( mennr );
        if (semen == null)
        {
            SkrivUt(4, "semen == null i medisammanfattningen.");
            return false;
        }
        mendelantal = semen.allaDelar.size(); // Antalet beståndsdelar för aktuell mening.
        SkrivUt(1, "mendelantal: " + mendelantal);
        SkrivUt(1, "mendelnr: " + mendelnr);
        SkrivUt(1, "mendel: " + semen.allaDelar.get(mendelnr).deltext);
        tbnr = semen.allaDelar.get( mendelnr ).tb;
        SkrivUt(1, "tbnr: " + tbnr);
        medisam = Testmedisammanfattningen( mennr );
        if (mendelnr + 1 >= mendelantal)
        { // Stega mening.
            SkrivUt(4, "mendelnr > mendelantal.");
            mendelnr = 0;
            mennr++;
            return medisam;
        } else
        { // Här skall man ta hand om arrayer också!
            SkrivUt(4, "mendelnr <= mendelantal.");
            isSameArray = true;
            arrind = semen.allaDelar.get( mendelnr ).arrind; // Vilket array ind?
            if (semen.allaDelar.get(mendelnr).isarr) // Är det en array
            {
                while (isSameArray)
                {
                    // Testa om mendelnr > mendelantal. => isSameArray = false
                    if (mendelnr + 1 >= mendelantal)
                    {
                        SkrivUt(2, "Slut på nuvarande arrayen i medisammanfattningen.");
                        isSameArray = false;
                        // Lägg till koll om slutkod här!
                        mennr++;
                        mendelnr = 0;
                    } else
                    {
                        // Testa om nästa mendel är en del av samma array
                        // Skall stega hela arrayen!
                        mendelnr++;
                        if ((semen.allaDelar.get( mendelnr ).isarr) &&
                                (semen.allaDelar.get(mendelnr).tb == tbnr))
                        // Inom samma textblock!
                        {
                            SkrivUt(2, "medisammanf arrind1 : " +
                                    semen.allaDelar.get(mendelnr).arrind +
                                    "tbnr: " + semen.allaDelar.get(mendelnr).tb);
                            arrind = semen.allaDelar.get(mendelnr).arrind;
                            // Testa om valdsida skall tilldelas:
                            if ((valdsida == -1)&&(mennr == valdmening)) //
                            { // Tilldela valdsida bara om den inte tilldelats tidigare.
                                // Tilldela endast om aktuell del inte är bara blanka tecken.
                                teststr = meningsvektor.get(mennr).allaDelar.get(mendelnr).deltext;
                                SkrivUt(3, "xxxxxxxxx teststr: " + teststr);
                                SkrivUt(3, "xxxxx mennr: " + mennr);
                                SkrivUt(3, "xxxxx valdmening: " + valdmening);
                                if (!baraBlanka( teststr ))
                                {
                                    valdsida = pageind; // Detta är den valda sidan.
                                    SkrivUt(3, "*************************** valdsida: " + valdsida);
                                } else SkrivUt(3, "bara blanka i deltext till mening: " + mennr);
                            }
                        } else
                        { // Inte samma array!
                            SkrivUt(2, "medisammanf arrind2 : " +
                                    semen.allaDelar.get(mendelnr).arrind + 
                                    "tbnr: " + semen.allaDelar.get(mendelnr).tb);
                            SkrivUt(2, "Ej samma array");
                            isSameArray = false; // Bör avkommenteras när ovanst fungerar!
                        }
                    }
                }
            } else
            { // Denna del är otestad eftersom dokumenten oftast inte innehåller bara COSString!

                // Testa om valdsida skall tilldelas:
                if ((valdsida == -1)&&(mennr == valdmening)) //
                { // Tilldela valdsida bara om den inte tilldelats tidigare.
                    // Tilldela endast om aktuell del inte är bara blanka tecken.
                    teststr = meningsvektor.get(mennr).allaDelar.get(mendelnr).deltext;
                    SkrivUt(3, "xxxxxxxxx teststr: " + teststr);
                    SkrivUt(3, "xxxxx mennr: " + mennr);
                    SkrivUt(3, "xxxxx valdmening: " + valdmening);
                    if (!baraBlanka( teststr ))
                    {
                        valdsida = pageind; // Detta är den valda sidan.
                        SkrivUt(3, "*************************** valdsida: " + valdsida);
                    } else SkrivUt(3, "bara blanka i deltext till mening: " + mennr);
                }
                
                mendelnr++; // Kolla om stega mening också!!!
            }
            return medisam;
        }
    }



    /*
     * Testmedisammanfattningen testar om ett givet meningsnummer är
     * med i samnanfattade meningarna. Kan effektiviseras!
     */

    public boolean Testmedisammanfattningen(int testmen)
    {
        // if (true) return true;
        SkrivUt(4, "Testmedisammanfattningen anropad testmen: "+ testmen);
        for (int k=0; k < menisammanfattningen.size(); k++)
        {
            if (testmen == menisammanfattningen.get( k ))
            {
                SkrivUt(1, "testmedisam ind: " + testmen);
                return true;
            } //else if (testmen > menisammanfattningen.get( k ))
              //  return false;
        }
        SkrivUt(1, "testmedisam EJ ind: " + testmen);
        return false;
    }
    

    /* ListTokens visar COSString, COSArray och PDFOperatorer i token listan.
     * 
     */

    public void listTokens(List intokens)
    {
        Object obj;
        SkrivUt(4, "listTokens loop:");
        for (int k= 0; k< intokens.size(); k++)
        {
            obj = intokens.get(k);
            SkrivUt(4, "Ind: "+ k + " tokentyp: " + obj.toString());
            /*
            if (obj instanceof COSString)
                SkrivUt(4, "Ind: "+ k + " tokentyp: " + obj.toString());
            if (obj instanceof COSArray)
            {
                SkrivUt(4, "Ind: "+ k + " tokentyp: " + obj.toString());
            }
            if (obj instanceof PDFOperator)
                SkrivUt(4, "Ind: "+ k + " tokentyp: " + obj.toString());
            if ((obj.equals(PDFOperator.getOperator("Tj"))) || (obj.equals(PDFOperator.getOperator("Tj"))))
                SkrivUt(4, "tokentyp: " + obj.toString());
             *
             */
        }
    }

    /*
     *  SparaTokens sparar tillbaka en ändrad tokenlista till sidstrukturen.
     *  Observera att det är en gång per sida nu! Dvs borde inte spara till
     *  filen förrän alla filers data är klara!
     */

    public void listTextBlocks()
    {
        SkrivUt(4, "listTextBlocks:");
        for (int p = 0; p < PageVector.size(); p++)
        {
            TBVector = PageVector.get( p );
            SkrivUt(4, "Sida:" + p);
            for (int ind = 0; ind < TBVector.size(); ind++)
            {
                printTBPost(TBVector.get(ind), ind);
            }
        }
    }

    /* printTBPost skriver ut data om ett textblock.
     *
     */

    public void printTBPost( SETextBlock setb1, int ind1)
    {
        // SkrivUt(4, "COSString. Nr: "+ ind1);
        SEArrayNumber sean;
        
        if (setb1.IsArray == false)
        {
            SECosstr secosstr = setb1.cstr;
            SkrivUt2(4, "COSString. Nr: "+ ind1 + " mennr: "+ secosstr.meningsnummer);
            SkrivUt2(4, "String: " + secosstr.cosstr);
            SkrivUt(4, "");
        } else // isArray == true
        {
            Vector<SEArrayIndKlass> cosarr = setb1.Carray;
            int arrel;
            for (arrel=0; arrel < cosarr.size(); arrel++)
            {
                if (cosarr.get(arrel) instanceof SECosstr)
                {
                    SECosstr secosstr = (SECosstr)cosarr.get(arrel); // setb1.cstr;
                    SkrivUt2(4, "COSArray. Nr: "+ ind1 + " mennr: "+ secosstr.meningsnummer);
                    SkrivUt2(4, "String: " + secosstr.cosstr);
                    SkrivUt(4, "");
                } else // COSInt.
                {
                    sean = (SEArrayNumber)cosarr.get(arrel); // setb1.cstr;
                    SkrivUt(4, "COSArray. Nr: "+ ind1 + " valnr: "+ sean.CArrayInt);
                }
            }
        }
    }

    public void listMeningar()
    {
        SkrivUt(4, "listMeningar");
        int size = meningsvektor.size();
        int delarsize = 0;
        int td;
        String outdata = "";
        Vector<SEmeningsdel> semendelarr = null;
        SEmeningsdel semendel = null;
        for (int k = 0; k <size; k++)
        {
            SkrivUt(4, "Hela men: " + meningsvektor.get(k).helameningen);
            semendelarr = meningsvektor.get(k).allaDelar;
            delarsize = semendelarr.size();
            SkrivUt(4, "delarsize: " + delarsize);
            for (td = 0; td < delarsize; td++)
            {
                semendel = semendelarr.get(td);
                outdata = "page: " + semendel.pagenr + " tb: " + semendel.tb;
                outdata += " isarr: " + semendel.isarr + " arrind: " + semendel.arrind;
                outdata += " text: " + semendel.deltext;
                SkrivUt(4,  outdata );
            }


        }
    }


    public void sparaTokens( List intokens )
    {
        listTokens( intokens );
        // if (true) return;
        try
        {
            SkrivUt(4, ">>> Före spara tokens i sparaTokens.");
            SkrivUt(4, "Tok len: " + intokens.size());
            if (intokens == null) SkrivUt(4, "Token == null.");
            if (doc == null)
            {
                SkrivUt(4, "doc == null");
                doc = new PDDocument();
            }
            if (page1 == null) SkrivUt(4, "page1 == null");
            PDStream updatedStream = new PDStream(doc);
            // SkrivUt(4, "1.");
            OutputStream out = updatedStream.createOutputStream();
            // SkrivUt(4, "2.");
            ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
            // SkrivUt(4, "3.");
            tokenWriter.writeTokens( intokens ); // Denna rad felar => Sparande felar.
            // SkrivUt(4, "4.");
            page1.setContents( updatedStream );
            SkrivUt(4, ">>> Efter spara tokens i sparaTokens.");
            /*
            doc.save( outputFile ); // Skriv tillbaka resultatet till fil! Tas bort senare!
            if( doc != null )
            {
                doc.close();
            }
             *
             */
             
            
        } catch (java.io.IOException ioe)
        {
            SkrivUt(0,"IOException i sparaTokens.");
        } /* catch (org.apache.pdfbox.exceptions.COSVisitorException cve)
        { // Denna exception skall bara vara aktiverad vid save och close!
            SkrivUt(4, "COSVisitorException i sparaTokens.");
        } */
        // finally
        // {

        //}
    }
    
    /*
     * buildSummaryTextWithNumbers skapar en string som innehåller sammanfattningen.
     */

    static public String buildSummaryTextWithNumbers() {
        StringBuilder sb = new StringBuilder();
        for(int index : indexSummary) {
            sb.append(index);
            // sb.append(": ");
            // sb.append(sentences.get(index));
            sb.append("\n");
        }
        return sb.toString();
    }

/*
 *     Sammanfatta returnerar data för sammanfattning.
 *     Skall i framtiden innehålla cogsums summering.
 *     Borde troligen inte vara en vanlig array!
 */

    public ArrayList<Integer> sammanfatta(String HelaTexten, int isumslidval)
    {
        SkrivUt(2, "Först i sammanfatta1. sumslidval: " + isumslidval);
        ArrayList<Integer> al = null;
        if (true)
        { // AH* kod från main i smallproject.
                   // Skapa sammanfattningen.
            SkrivUt(2, "Sammanfatta, HelaTexten: "+ HelaTexten); // AH* testutskrift.
            Settings s = new Settings();
            s.setRemoveStopWords(false);
            s.setUseOutsideSpace(false);
            s.setRemoveDocumentVector(true);
            s.setDimensionality(300);
            s.setPageRankIterations(20);
            CogSum cs = new CogSum(s);
            // easyreader.Document d = new easyreader.Document();
            // Fungerande?: Tar bort felet nedan.
            Document d = new Document();
            //d.readFile("resources/historia.txt");
            Vector<SEmening> vec = Hittameningarna(HelaTexten);
            ArrayList<String> sents = new ArrayList();
            for(SEmening sem : vec){
                sents.add(sem.helameningen);
            }
            d.read( sents );
            cs.setText(d);
            // Fel om d är easyreader.Document! Vill ha Reader.Doc..
            //Summary sum = cs.buildSummary();
            Summary sum = cs.buildSummary(); // Hittar inte metoden!
            System.out.println(sum.getOrderedValues());
            // reader = new SummaryReader(sum); 
            reader = new SummaryReader(sum);
            SkrivUt(2, "isumslidval: " + isumslidval); // AH*
//            try
//            {
                sentences = sum.getSentences();
                // sentences = null; //AH* Onödigt slösa med minne.
                //Get indexes for a 70% summary
                indexSummary = reader.buildIndexListPercent(isumslidval);
                if (indexSummary == null)
                    SkrivUt(2, "indexSummary == null.");
                else
                { /*
                    for (int n= 0; n < indexSummary.size(); n++)
                    {
                        SkrivUt(2, "IS = " + indexSummary.get(n));
                    }
                   * 
                   */
                }

//            } 
            
//            catch (java.lang.NullPointerException jln)
//            {
//                SkrivUt(2, "Null i sammanfatta.");
//                if (indexSummary == null) // AH* Tillagt.
//                {
//                    SkrivUt(2, "indexSummary == null");
//                    // indexSummary null dvs initiera för test.
//                    al = new ArrayList<Integer>();
//                    for (int n= 0; n < meningsvektor.size(); n += 2)
//                    {
//                        al.add( n );
//                        SkrivUt(2, "n1 = " + n);
//                    }
//                    indexSummary = al;
//                } else
//                    SkrivUt(2, "indexSummary != null");
//            }
            SkrivUt(2, "\n" + buildSummaryTextWithNumbers());
            SkrivUt(2, "Sist i sammanfatta. Summary");

            return indexSummary;
        }
        
        else // Skapa testdata med varannan med i sammanfattningen.
        {
            al = new ArrayList<Integer>();
            for (int n= 0; n < meningsvektor.size(); n += 2)
            {
                al.add( n );
                // SkrivUt(2, "n2 = " + n);
            }
            indexSummary = al;
            SkrivUt(2, buildSummaryTextWithNumbers());
            SkrivUt(2, "Sist i sammanfatta. Default");
            return al;
           // return null; // menisammanfattningenTest;
        }
    }

    /**
     * Sammanfatta PDF dokumentet baserat på ..
     *
     * @param inputFile The PDF to open.
     * @param outputFile The PDF to write to.
     * @param strToFind The string to find in the PDF document.
     * @param message The message to write in the file.
     *
     * @throws IOException If there is an error writing the data.
     * @throws COSVisitorException If there is an error writing the PDF.
     */
    public Collection<SEmening> doIt( String inputFile, String outputFile1, boolean DoHighlight,
            int sumslidval, int valdmening)
        throws IOException, COSVisitorException
    {
        // the document
        // doc = null; // Output dokumentet (? Kollas!)
        PDFOperator gop = PDFOperator.getOperator("g");
        COSFloat cfloat5 = new COSFloat("0.25");
        COSFloat cfloat1 = new COSFloat("0.75");
        Boolean gray1 = true;
        outputFile = outputFile1;
        String meningsrest="";
        String sidtext = "";
        Boolean filesaved = false;

        try
        {
            helaTexten = "";
            SkrivUt(3,"Före DoIt doc1 load");
            doc1 = PDDocument.load( inputFile ); // Indokumentet.
            SkrivUt(3,"Efter DoIt doc1 load");
            List pages = doc1.getDocumentCatalog().getAllPages();
            // SkrivUt(2, "Antal sidor: " + pages.size());
            for( int i=0; i<pages.size(); i++ )
            { // Första fasen skall samla hela texten samt ev ändra relativa till absoluta.
                // TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                // PageVector.add(i, TBVector); // Lägg till TB vektorn för denna sida.
                SkrivUt(4, "Ny sida helaTexten: " + i);
                PDPage page = (PDPage)pages.get( i );
                PDStream contents = page.getContents();
                //AH Kod från PageDrawer:
                if ( contents != null)
                {
                    PDResources resources = page.findResources();
                    SkrivUt(4, "Före getHelaTexten.");
                    // Fas = relativ2absolut är inte implementerad än!
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    sidtext = getHelaTexten( page.getContents().getStream() ); //getTextFromPDF, Robin
                    helaTexten += sidtext;
                    // Hämta hela texten från dokumentet.
                    // SkrivUt(2, "Hela texten per sida0: " + sidtext);
                    // helaTexten = ""; // Skall inte nollställas nu!
                    // helaTexten = ""; // AH* >> Nollställ INTE, - för alla sidor.
                    SkrivUt(4, "Efter getHelaTexten.");
                    // cosStream.getStreamToken
                    /*
                    // PDStream nycont = new PDStream( getTokenList());
                    PDFStreamEngine.fas = PDFStreamEngine.splitstrings;
                    processStream( page, resources, page.getContents().getStream());
                    SkrivUt(4, "Hela texten2: " + helaTexten);
                    SkrivUt(4, "Efter andra processStream.");
                    meningsvektor = Hittameningarna( helaTexten );
                    helaTexten = ""; // AH* >> Nollställ???
                     *
                     */
                }
            } // Extrahera meningarna från hela texten:
            // SkrivUt(2, "Hela texten1: " + helaTexten);
            meningsvektor = Hittameningarna( helaTexten ); // Splittra texten i meningar.
            SkrivUt(1, "Meningsvektor.Size: " + meningsvektor.size());
            SEmening semen = null;
            for (int n = 0; n < meningsvektor.size(); n++ )
            {
                // Lägg till mening till meningsvektor.
                // semen = new SEmening();
                // semen.helameningen = menvektor.get( n );
                // meningsvektor.add(n, semen); // Huvudstrukturen för meningar.
                SkrivUt(1, "Mening: " + meningsvektor.get( n ).helameningen);
            }
            /* for (int n = 0; n < meningsvektor.size(); n++ )
            {
                SkrivUt(4, "Mening: " + meningsvektor.get( n ).helameningen);
            } */
            
            // String helaTextTemp = helaTexten;

            // Vid nästa bearbetning skall string och array splittras vid
            // meningsgränser.
            mind = 0;
            meningsrest = meningsvektor.get(mind).helameningen; // Kvarvarande text på aktuella sidan
            for( int i=0; i<pages.size(); i++ )
            {
                // Borde flytta på denna om de inte skall användas!
                // TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                // PageVector.add(i, TBVector); // Totalstruktur.
                // Lägg till TB vektorn för denna sida.
                SkrivUt(4, "Ny sida Split: " + i);
                // PDPage Ändrad till lokal variabel!
                page1 = (PDPage)pages.get( i );
                PDStream contents = page1.getContents();
                //AH Kod från PageDrawer:
                SkrivUt(4, "Innan contents test.");
                if ( contents != null)
                {
                    PDResources resources = page1.findResources();
                    SkrivUt(3, "Före splitMeningar 1.");
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    SkrivUt(4, "Före splitMeningar 2.");
                    meningsrest = splitMeningar( meningsrest, page1.getContents().getStream());
                    SkrivUt(4, "*** meningsrest: "+ meningsrest);
                    SkrivUt(4, "3, Efter splitMeningar.");
                }
            }

            // Kolla denna kod!!!
            // saveAndClose( outputFile, doc1 ); // AH****
            // doc1 = PDDocument.load( inputFile ); // Indokumentet.
            // SkrivUt(3,"Efter DoIt doc1 load");
            // pages = doc1.getDocumentCatalog().getAllPages(); Nyinlagd. Kvar???


            // Här skall g operatorer läggas till för varje TJ och Tj!
            mind = 0; // Behövs denna här?
            meningsrest = meningsvektor.get( mind ).helameningen; // Kvarvarande text på aktuella sidan.

            for( int i=0; i<pages.size(); i++ )
            {
                // Borde flytta på denna om de inte skall användas!
                // TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                // PageVector.add(i, TBVector); // Totalstruktur.
                // Lägg till TB vektorn för denna sida.
                SkrivUt(4, "Ny sida Gray: " + i);
                // PDPage Ändrad till lokal variabel!
                page1 = (PDPage)pages.get( i );
                PDStream contents = page1.getContents();
                //AH Kod från PageDrawer:
                SkrivUt(4, "Innan contents test.");
                if ( contents != null)
                {
                    PDResources resources = page1.findResources();
                    SkrivUt(3, "Före GrayInsert 1.");
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    // SkrivUt(4, "Före splitMeningar 2.");
                    meningsrest = grayInsert( meningsrest, page1.getContents().getStream(), i);
                    //SkrivUt(4, "*** meningsrest: "+ meningsrest);
                    SkrivUt(3, "Efter grayInsert av sida.");
                }
            }
            SkrivUt(3, "Efter hela grayInsert.");

            // Bygg TB och meningsstrukturer.
            //
            mind = 0;
            meningsrest = meningsvektor.get( mind ).helameningen; // Kvarvarande text på aktuella sidan.

            for( int i=0; i<pages.size(); i++ )
            {
                TBIndex = 0; // Index i textblocks strukturen.
                // Här används och byggs datastrukturerna!
                TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                PageVector.add(i, TBVector); // Totalstruktur.
                tbpagenr = i;
                // Lägg till TB vektorn för denna sida.
                SkrivUt(4, "Ny sida Split: " + i);
                // PDPage Ändrad till lokal variabel!
                page1 = (PDPage)pages.get( i );
                PDStream contents = page1.getContents();
                //AH Kod från PageDrawer:
                SkrivUt(4, "Innan contents test.");
                if ( contents != null)
                {
                    PDResources resources = page1.findResources();
                    SkrivUt(3, "Före byggStrukturer 1.");
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    SkrivUt(4, "Före byggStrukturer 2.");
                    meningsrest = byggStrukturer( meningsrest, page1.getContents().getStream(), i);
                    SkrivUt(4, "*** meningsrest: "+ meningsrest);
                    SkrivUt(3, "Efter byggStrukturer.");
                }
            }

            // Skriv ut innehållet i TB strukturen:
            listTextBlocks();

            // Skriv ut meningarna:
            listMeningar();
             //*/
            // Sista passet skall samla in TP fontmetrics och spara dem till TB strukturer.

            SkrivUt(4, "För långt.");
            mind = 0; // Behövs denna här?

            for( int i=0; i<pages.size(); i++ )
            { // Första fasen skall samla hela texten samt ev ändra relativa till absoluta.
                // TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                // PageVector.add(i, TBVector); // Lägg till TB vektorn för denna sida.
                tempsidnr = i;
                SkrivUt(4, "Ny sida A: " + i);
                PDPage page = (PDPage)pages.get( i );
                PDStream contents = page.getContents();
                //AH Kod från PageDrawer:
                if ( contents != null)
                {
                    PDResources resources = page.findResources();
                    SkrivUt(4, "Före processStream.");
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    // SkrivUt(4, "Hela texten2FÖRE: " + helaTexten);
                    // processStream( page, resources, page.getContents().getStream()); // Här anropas sidhanteringen!
                    // SkrivUt(2, "Hela texten2: " + helaTexten);
                    // helaTexten = ""; // AH* >> Nollställ INTE, - för alla sidor.
                    SkrivUt(4, "Efter processStream. före nya");
                    // cosStream.getStreamToken

                    /*
                    // PDStream nycont = new PDStream( getTokenList());
                    PDFStreamEngine.fas = PDFStreamEngine.splitstrings;
                    processStream( page, resources, page.getContents().getStream());
                    SkrivUt(4, "Hela texten2: " + helaTexten);
                    SkrivUt(4, "Efter andra processStream.");
                    meningsvektor = Hittameningarna( helaTexten );
                    helaTexten = ""; // AH* >> Nollställ???
                     *
                     */
                }
            }
            SkrivUt(3, "Efter processStream.");


            //if (DoHighlight)
            //{
                // Här skall texten förmedlas till EasyReader och resultatlista med
                // meningar som skall highlightas skall returneras!
                if ((DoHighlight)&& !(helaTexten.equals("")))
                {
                    SkrivUt(2, "Före sammanfatta. helaTexten = \"\"");
                    menisammanfattningen = sammanfatta( helaTexten, sumslidval );
                    System.out.println(menisammanfattningen);
                }
                else 
                    menisammanfattningen = null;
                // Här skall g operatorernas argument modifieras för de som skall vara
                // med i sammanfattningen.
                mind = 0; // Behövs denna här?
                meningsrest = meningsvektor.get( mind ).helameningen; // Kvarvarande text på aktuella sidan.
                cosenr = 0; // index för COSString eller COSArray.
                mennr = 0; // index för aktuell mening.
                mendelnr = 0;
                mendelantal = 0; // Antal delar som meningen består av.
                valdsida = -1; // valda sidan inte känd än.
                for( int i=0; i<pages.size(); i++ )
                {
                    // Borde flytta på denna om de inte skall användas!
                    // TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                    // PageVector.add(i, TBVector); // Totalstruktur.
                    // Lägg till TB vektorn för denna sida.
                    SkrivUt(4, "Ny sida highlight: " + i);
                    // PDPage Ändrad till lokal variabel!
                    page1 = (PDPage)pages.get( i );
                    PDStream contents = page1.getContents();
                    //AH Kod från PageDrawer:
                    SkrivUt(4, "Innan contents test.");
                    if ( contents != null)
                    {
                        PDResources resources = page1.findResources();
                        SkrivUt(1, "Före highlight 1. Sida: " + i);
                        // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                        setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                        // SkrivUt(4, "Före splitMeningar 2.");
                        // if (DoHighlight) 
                        meningsrest = highlight( meningsrest, 
                                page1.getContents().getStream(), i, DoHighlight, valdmening);
                        //SkrivUt(4, "*** meningsrest: "+ meningsrest);
                        SkrivUt(1, "Efter highlight av sida:" + i);
                    }
                }
                SkrivUt(3, "Efter hela highlight.");
            /*} else // Spara data till pageTokens för sparande till fil efter.
            {
                
            }*/

            // Dags att hämta fontmetrics och spara till fil. Är det samma som ovan?
            // Skall inte göras f.n!

            for( int i=0; i<pages.size(); i++ )
            {
                TBVector = new Vector<SETextBlock>(); // TB vektorn för denna sida.
                PageVector.add(i, TBVector); // Lägg till TB vektorn för denna sida.
                SkrivUt(4, "Ny sida X: " + i);
                // PDPage Ändrad, inte lokal längre!
                page = (PDPage)pages.get( i );
                PDStream contents = page.getContents();
                //AH Kod från PageDrawer:
                if ( contents != null)
                {
                    PDResources resources = page.findResources();
                    SkrivUt(4, "Före processStream.");
                    // PDFStreamEngine.fas = PDFStreamEngine.rel2abs;
                    setSumcharAlla( 0 ); // Nollställ teckenräknaren för strings
                    // AH* Nästa rad används för att hämta ut fontmetrics.
                    /* processStream( page, resources, page.getContents().getStream()); // Här anropas sidhanteringen!
                    SkrivUt(4, "Hela texten1: " + helaTexten);
                    // helaTexten = ""; // Skall inte nollställas nu!
                    SkrivUt(4, "Efter processStream. före nya");
                    // cosStream.getStreamToken
                     *
                     */

                    // PDStream nycont = new PDStream( getTokenList());
                    // PDFStreamEngine.fas = PDFStreamEngine.splitstrings;
                    // Nedanstående har anropats ovan.
                    // processStream( page, resources, page.getContents().getStream());
                    // SkrivUt(4, "Hela texten3: " + helaTexten);
                    SkrivUt(4, "Efter andra processStream.");
                }
                SkrivUt(3,"Efter hela andra processStream.");
                /*
                PDFStreamParser parser = new PDFStreamParser(contents.getStream());
                parser.parse();
                 *
                 */
                // SkrivUt(4, "Egna loopen Sida: " + i);
               /* List tokens = getTokenList();// AH* parser.getTokens(); Tidigare hämtning av lista.
                    // Dvs hämta INTE tokens från den parsade filen. Använd tidigare data.
                LinkedList arguments = new LinkedList(); // AH* argumenten till operatorn.
                for( int j=0; j<tokens.size(); j++ )
                {
                    Object next = tokens.get( j );
                    if( next instanceof PDFOperator )
                    {
                        PDFOperator op = (PDFOperator)next;
                        //Tj and TJ are the two operators that display
                        //strings in a PDF
                        //AH:
                        //SkrivUt(4, "ArgumentList length: " + arguments.size());
                        //>> AH* SkrivUt(4, "Operator anrop:" + OperatorCall( op, arguments ));
                        // AH: Här borde man göra ett anrop till StreamEngine!
                        arguments = new LinkedList(); // Måste nollställa argumenten
                        // efter varje operator.
                        if( op.getOperation().equals( "Tj" ) )
                        {
                            //Tj takes one operator and that is the string
                            //to display so lets update that operator
                            COSString previous = (COSString)tokens.get( j-1 );
                            String string = previous.getString();
                            string = string.replaceFirst( strToFind, message );
                            previous.reset();
                            previous.append( string.getBytes() );
                            // AH* Testa tillägg av kod.
                            
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;
                             
                            
                        }
                        else if( op.getOperation().equals( "TJ" ) )
                        {
                            COSArray previous = (COSArray)tokens.get( j-1 );
                            for( int k=0; k<previous.size(); k++ )
                            {
                                Object arrElement = previous.getObject( k );
                                if( arrElement instanceof COSString )
                                {
                                    COSString cosString = (COSString)arrElement;
                                    String string = cosString.getString();
                                    string = string.replaceFirst( strToFind, message );
                                    cosString.reset();
                                    cosString.append( string.getBytes() );
                                }
                            }
                            // AH: Tillagd kod!
                            /*
                            tokens.add(j-1, gop);
                            if (gray1)
                            {
                                tokens.add(j-1,cfloat1 );
                                gray1 = false;
                            } else
                            {
                                tokens.add(j-1, cfloat5);
                                gray1 = true;
                            }
                            j = j+2;

                             
                        }
                    } else // Inte PDFOperator, samla argument!
                    {
                        if (next instanceof COSBase)
                        {
                             arguments.add( next);
                             //SkrivUt(4, "COSBase " + next.toString());
                        } else
                        {
                            SkrivUt(4, "next inte rätt typ!");
                        }
                    }
                }

                */
                //now that the tokens are updated we will replace the
                //page content stream.
                // Uppdatera data till filen!
                SkrivUt(3, ">>> Före spara tokens i DoIt.");
                PDStream updatedStream = new PDStream(doc1);
                SkrivUt(3, ">>> Efter updated stream i DoIt.");
                OutputStream out = updatedStream.createOutputStream();
                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
                tokenWriter.writeTokens( pageTokens.get( i ) );
                page.setContents( updatedStream );
                SkrivUt(3, ">>> Efter spara tokens i DoIt.");
            }
            /*
            if (!filesaved)
            {
                doc1.save( outputFile );
                filesaved = true;
            }
            doc1.close();
            SkrivUt(3, "doc1 closed 1.");
             *
             */
        }
        finally
        {
            saveAndClose( outputFile, doc1 );
            /*
            SkrivUt(2, "Finally.");
            if( doc1 != null )
            {
                if (!filesaved)
                {
                    doc1.save( outputFile );
                    filesaved = true;
                }
                doc1.close();
                SkrivUt(3, "doc1 closed 2.");
            }
             *
             */
        }
        
        return meningsvektor;
    }

    /* saveAndClose: Spara och stäng fil vid avslutning.
     *
     */

    private void saveAndClose( String filnamn, PDDocument utfil)
    {
        try
        {
            if (utfil != null)
            {
                SkrivUt(3, "saveAndClose");
                utfil.save(filnamn);
                utfil.close();
            }
        } catch (java.io.IOException jioio)
        {
            SkrivUt(7, "IO Fel i saveAndClose.");
        } catch (COSVisitorException cosv)
        {
            SkrivUt(7, "CosVis Fel i saveAndClose.");
        }
    }

    public static String testdata()
    {
        String men = "Alla bra saker. Är flera! Slut? Sist.Ord.ord!";
        men += " gemen?Ny del !Nyare ?nyast!7733INUTI ?     THE      END.";
        men += "  not new...Pig!!Bird!?!? Fish?? fish!?!    Aha.!?b";
        return men;
    }

    /**
     * This will open a PDF and replace a string if it finds it.
     * <br />
     * see usage() for commandline
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args)
    {
        // Test av meningsextrahering:

        // Hittameningarna( testdata() );
        //return;
        ReplaceStringStreamEngine app = null;
        try
        {
            app = new ReplaceStringStreamEngine();
        } catch (IOException ioe)
        {
            SkrivUt(4, "Konstruktorn fungerade inte!");
        }
        try
        {
            if (args.length== 0)
            {
                app.doIt( "AHDocWordPDF2.pdf", "AHPrepout.pdf", true, 80, -1); // AHPrep1.pdf
            } else if (args.length==1)
            {
                app.doIt(args[0], "AHPrepout.pdf", false, 100, 0);
            } else
            {
                SkrivUt(4, "Felaktiga inparametrar till main.");
            }
            SkrivUt(4, "Slut på programmet."); // yyy
            // AHDocWordREDTestPDF.pdf
            // AHDocWordPDF2.pdf
            // Tj: AHDoc2Test11.pdf
            // TJ: AHDoc2Mini.pdf
            // TJ: AHDoc2Mini2.pdf
            // TJ: AHDoc2Mini3Arrayer.pdf
            // TJ: AHDoc2MiniArrayerMod3.pdf
            // TJ: AHDoc2TestNow.pdf - finns fel i.
            // Två sidor: AHDoc1TvåSidor.pdf
            // sltc-08
            // Hassel040517
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This will print out a message telling how to use this example.
     */
    private void usage()
    {
        System.err.println( "usage: " + this.getClass().getName() +
            " <input-file> <output-file> <search-string> <Message>" );
    }
}
