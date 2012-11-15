/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.pdfstod.CogsumApplet;

import java.util.Vector;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Allan
 * Cstr representerar ett COSString objekt.
 */

public class SECosstr extends SEArrayIndKlass { // Lagra matriser också?
    public int meningsnummer; // Meningen som klassen lagrar.
    public int Carrayind; // Eventuellt index i COSArray. - Lagras inte på annan plats!
    public String cosstr = ""; // Själva texten.
    byte[] bytedata; // Data i COSString.
    PDFont strfont = null; // Font för texten.
    float strfontsizeIP = 0; // fontsize i punkt enheter.
    float strfontsizeVal = 0; // fontsize värde.
    float[] charwidth = null; // bredden för de individuella characters.
    float maxFontHeight = 0; // Högsta värdet på höjden för fonten.
    float sumcharAlla = 0; // Bredden på hela texten.
    float x = 0;
    float y = 0;

    public SECosstr(
            int mennrin,
            int CARRind,
            String cosstrin)
    {
            meningsnummer = mennrin;
            Carrayind = CARRind;
            cosstr = cosstrin;
    }

    public SECosstr( // Konstruktor som initierar alla variablerna!
            int mennrin,
            int CARRind,
            String cosstrin,
            byte[] bytedatain,
            PDFont strfontin,
            float strfontsizeIPin,
            float strfontsizeValin,
            float[] charwidthin,
            float maxFontHeightin,
            float sumcharAllain,
            float xin,
            float yin)

    {
            meningsnummer = mennrin;
            Carrayind = CARRind;
            cosstr = cosstrin;
            bytedata = bytedatain;
            strfont = strfontin;
            strfontsizeIP = strfontsizeIPin;
            strfontsizeVal = strfontsizeValin;
            charwidth = charwidthin; // bredden för de individuella characters.
            maxFontHeight = maxFontHeightin; // Högsta värdet på höjden för fonten.
            sumcharAlla = sumcharAllain;
            x = xin;
            y = yin;
    }

        public SECosstr( // Konstruktor som initierar alla variablerna!
            byte[] bytedatain,
            PDFont strfontin,
            float strfontsizeIPin,
            float strfontsizeValin,
            float[] charwidthin,
            float maxFontHeightin,
            float sumcharAllain,
            float xin,
            float yin)
        {
            // meningsnummer = mennrin;
            // Carrayind = CARRind;
            // cosstr = cosstrin;
            bytedata = bytedatain;
            strfont = strfontin;
            strfontsizeIP = strfontsizeIPin;
            strfontsizeVal = strfontsizeValin;
            charwidth = charwidthin; // bredden för de individuella characters.
            maxFontHeight = maxFontHeightin; // Högsta värdet på höjden för fonten.
            sumcharAlla = sumcharAllain;
            x = xin;
            y = yin;
    }

}

/*
 *     public void processTextPosition( TextPosition tp)
    {
         String tpstr = tp.getCharacter();
         System.out.println("TextPosition: " + tpstr + " " + tpstr.length());
         System.out.println("spaceWidth: " + tp.getWidthOfSpace() );
         System.out.println("Width: " + tp.getWidth());
         int tempcurrstrlen = currstringlen;
         currstringlen += tpstr.length();
         strtp += tpstr;
         //AH* Data som sparas om string.
         PDFont strfont = tp.getFont();
         float strfontsizeIP = tp.getFontSizeInPt();
         System.out.println("strfontsizeIP: " + tp.getFontSizeInPt());
         float strfontsizeVal = tp.getFontSize();
         System.out.println("strfontsizeVal: " + tp.getFontSize());
         float[] charwidth = tp.getIndividualWidths(); // array av char bredder.
         float maxFontHeight = tp.getHeight();
         System.out.println("maxFontHeight: " + tp.getHeight());
         // int sumcharlen = 0;
         float sumcharAlla = 0; // Bredden för hela string.
         // float[] sumcharwidth = new float[1000];
         for (int k = 0; k < tpstr.length(); k++) // charwidth.length; k++ )
         {
             System.out.print(" iw " + charwidth[ k ]);
             sumcharwidth[ tempcurrstrlen + k ] = charwidth[k];
         }
         System.out.println("");
         if (currstringlen < stringlen) // Inte avslutad än.
         {
            // Fortsätt bara samla delsträngar.
         } else if(currstringlen == stringlen)
         {
             System.out.println("TextPosition hela: " + strtp);
             for (int k = 0; k < stringlen; k++ )
             {
                 System.out.print(" iw " + sumcharwidth[ k ]);
                 sumcharAlla += sumcharwidth[ k ];
             }
             System.out.println("\nSumcharAlla: " + sumcharAlla);
             // Ta hand om resultat string...
             helaTexten += strtp;
             strtp = "";
             currstringlen = 0;
             setSumcharAlla( 0 );

         } else // (currstringlen > stringlen) ny string eftersom längden
 överskrider!
         {
            System.out.println("För stor string i processTextPosition.");
            System.out.println("Text: " + strtp + "currlen: " + currstringlen);
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