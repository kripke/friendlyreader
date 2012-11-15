package com.santaanna.friendlyreader.robke.utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.santaanna.friendlyreader.utils.Tuple;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.robke.utils.Speak;

/**
 * This class controls a JTextPane.
 *
 * @author Robin Keskisärkkä
 */
public class TextAssistant {

    private JTextPane textPane;
    private ArrayList<Tuple> sentencePositions = new ArrayList();
    private Highlighter highlighter;
    private SummaryReader sentences;
    private Thread talkThread;
    private double sumPercentage = 100;
    private boolean showSentenceIndexes = false;
    //Line spacing
    private float spacing = 0;
    private float spacingMin = 0;
    private float spacingMax = 0;
    //Text
    private int textSize = 30;
    private int textSizeMin = 14;
    private int textSizeMax = 18;
    private String fontName = "Calibri";
    //Color
    private Color bgColor = new Color(255, 255, 255);
    private Color textColor = new Color(0, 0, 0);
    private Color highlightColor = Color.YELLOW;
    private boolean highlight = false;

    /**
     * TextAssistant takes a JTextArea as an argument. The methods implemented
     * simplifies the process of keeping the text area up to date.
     *
     * @param textPane
     */
    public TextAssistant(JTextPane textPane) {
        this.textPane = textPane;
        highlighter = textPane.getHighlighter();
    }

    /**
     * Activate/deactivate visible sentence indexes. Currently selected sentence
     * remains the same.
     *
     * @param value
     */
    public void setShowSentenceIndexes(boolean value) {
        int index = getCurrentSentenceIndex();
        showSentenceIndexes = value;
        updateText();
        moveToSentence(index);
    }

    /**
     * Update text. Necessary to update some text properties.
     */
    public void updateText() {
        if (sentences != null) {
            setPercentage(sumPercentage);
        }
    }

    /**
     * Get the currenty selected sentence index based on caret position.
     *
     * @return index
     */
    public int getCurrentSentenceIndex() {
        //Find the index corresponding to the caret position
        int caretPosition = textPane.getCaretPosition();
        Tuple t;
        Iterator<Tuple> iterator = sentencePositions.iterator();
        int sentenceNumber = -1;
        while (iterator.hasNext()) {
            t = iterator.next();
            sentenceNumber += 1;
            if (caretPosition >= (Integer) t.T1 && caretPosition < (Integer) t.T2) {
                return sentenceNumber;
            }
        }
        return -1;
    }

    /**
     * Set percentage and update text.
     *
     * @param sentences
     */
    public void setPercentage(double percentage) {
        setText(sentences, percentage);
    }

    /**
     * Set text with a sepcified percentage. The text will be styled, and the
     * beginning and ending indexes of the separate sentences will be stored as
     * tuples in the ArrayList sentencePositions
     *
     * @param sentences
     */
    public void setText(SummaryReader sentences, double percentage) {

        sumPercentage = percentage;
        this.sentences = sentences;
        try {
            //Empty the document
            StyledDocument doc = textPane.getStyledDocument();
            doc.remove(0, doc.getLength());
            sentencePositions = new ArrayList();

            String sentence;
            
            ArrayList indexes = sentences.buildIndexListPercent(percentage);
            for (Object i : indexes) {
                sentence = sentences.getSummary().getSentences().get((Integer) i);
                doc.insertString(0, sentence, doc.getStyle("paragraphStyle"));
            }
        } catch (BadLocationException ex) {
            System.out.println("Failed to update text.");
        }

        // reset caret position
        if (!sentencePositions.isEmpty()) {
            Tuple t = sentencePositions.get(0);
            textPane.setCaretPosition(((Integer) t.T1 + (Integer) t.T2) / 2);
            highlightCurrentSentence();
        }
    }

    /**
     * Set background color
     *
     * @param color
     */
    public void setBackgroundColor(Color color) {
        bgColor = color;
        updateStyle();
    }

    /**
     * Set text color
     *
     * @param color
     */
    public void setTextColor(Color color) {
        textColor = color;
        updateStyle();
    }

    /**
     * Set line spacing. The min and max value set the legal interval for the
     * line spacing.
     *
     * @param spacing
     * @param spacingMin
     * @param spacingMax
     */
    public void setLineSpacing(float spacing, float spacingMin, float spacingMax) {
        this.spacing = spacing;
        this.spacingMin = spacingMin;
        this.spacingMax = spacingMax;
        updateStyle();
    }

    /**
     * Add a value to the current line spacing. Decrease current line spacing by
     * passing a negative value.
     *
     * @param spacing
     */
    public void changeLineSpacing(float spacing) {
        this.spacing += spacing;
        if (this.spacing > spacingMax) {
            this.spacing = spacingMax;
        } else if (this.spacing < spacingMin) {
            this.spacing = spacingMin;
        }
        updateStyle();
    }

    /**
     * Set text size. The min and max value set the legal interval for the text
     * size.
     *
     * @param textSize
     * @param textSizeMin
     * @param textSizeMax
     */
    public void setTextSize(int textSize, int textSizeMin, int textSizeMax) {
        this.textSize = textSize;
        this.textSizeMin = textSizeMin;
        this.textSizeMax = textSizeMax;
        updateStyle();
    }

    /**
     * Add a value to the current tet size. Decrease current text size by
     * passing a negative value.
     *
     * @param size
     */
    public void changeTextSize(int size) {
        textSize += size;
        if (textSize > textSizeMax) {
            textSize = textSizeMax;
        } else if (textSize < textSizeMin) {
            textSize = textSizeMin;
        }
        updateStyle();
        updateText();
    }

    /**
     * Set margins around the document using an empty border
     *
     * @param top
     * @param right
     * @param bottom
     * @param left
     */
    public void setMargins(int top, int right, int bottom, int left) {
        textPane.setBorder(new EmptyBorder(top, left, bottom, right));
    }

    /**
     * Highlight current sentence. Current sentence is determined by finding the
     * interval of the caret.
     */
    public void highlightCurrentSentence() {
        highlighter.removeAllHighlights();
        if (highlight == false) {
            return;
        }

        //Find the current sentence based on caret position
        //If on the last caret position move back one step
        int caretPosition = textPane.getCaretPosition();
        if (caretPosition == textPane.getDocument().getLength()) {
            caretPosition--;
        }

        //Iterate through the sentence postitions
        Tuple t;
        Iterator<Tuple> iterator = sentencePositions.iterator();
        while (iterator.hasNext()) {
            t = iterator.next();
            //Test if caret position is in the interval
            if ((Integer) t.T1 <= caretPosition && (Integer) t.T2 > caretPosition) {
                try {
                    highlighter.addHighlight((Integer) t.T1, (Integer) t.T2 - 1,
                            new DefaultHighlighter.DefaultHighlightPainter(highlightColor));
                } catch (BadLocationException ex) {
                    Logger.getLogger(TextAssistant.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Stops speech if currently active.
     */
    public void stopTalk() {
        if (talkThread != null & talkThread.isAlive()) {
            try {
                talkThread.interrupt();
            } catch (Exception e) {
                //Do nothing
            }
        }
    }

    /**
     * Activates speech for a text string.
     *
     * @param text
     * @return speaker
     */
    public Speak talk(String text) {
        System.out.println("Try to say: text = " + text);
        if (text.trim().length() == 0) {
            return null;
        }
        Speak speaker = new Speak(text);
        talkThread = new Thread(speaker);
        talkThread.start();

        return speaker;
    }

    /**
     * Activates speech for currently active highlight or current sentence.
     *
     * @return speaker
     */
    public Speak talk() {
        String text = "";
        if (textPane.getSelectedText() != null) {
            text = textPane.getSelectedText();
            if (text.length() > 100) {
                text = text.substring(0, 100);
            }
        } else {
            //Find the current sentence based on caret position
            //Iterate through the sentence postitions
            int caretPosition = textPane.getCaretPosition();
            Tuple t;
            Iterator<Tuple> iterator = sentencePositions.iterator();
            while (iterator.hasNext()) {
                t = iterator.next();
                //Test if caret position is in the interval
                if ((Integer) t.T1 <= caretPosition && (Integer) t.T2 > caretPosition) {
                    try {
                        text = textPane.getStyledDocument().getText((Integer) t.T1, (Integer) t.T2 - (Integer) t.T1);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TextAssistant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return talk(text);
    }

    /**
     * Returns true if talkThread is currently active.
     *
     * @return isTalking
     */
    public boolean isTalking() {
        return (talkThread != null && talkThread.isAlive());
    }

    /**
     * Activate/deactivate highlight.
     *
     * @param value
     */
    public void setHighlight(boolean value) {
        highlight = value;
        highlightCurrentSentence();
    }

    /**
     * Returns the distance from the caret top to font baseline (approximately).
     *
     * @return height
     */
    public int getHighlightHeight() {
        StyledDocument doc = textPane.getStyledDocument();
        Style paragraphStyle = doc.getStyle("paragraphStyle");
        FontMetrics fm = textPane.getFontMetrics(doc.getFont(paragraphStyle));
        return fm.getHeight() + 6;
    }

    /**
     * Update document styles. This method is called automatically when a style
     * property has been changed. Updating the text must be done separately.
     */
    public void updateStyle() {
        StyledDocument doc = textPane.getStyledDocument();

        Style paragraphStyle = doc.addStyle("paragraphStyle", null);
        StyleConstants.setLineSpacing(paragraphStyle, spacing);
        StyleConstants.setFontFamily(paragraphStyle, fontName);
        StyleConstants.setFontSize(paragraphStyle, textSize);
        StyleConstants.setForeground(paragraphStyle, textColor);

        doc.setParagraphAttributes(0, doc.getLength(), paragraphStyle, true);
    }

    /**
     * Move to a specific sentence number. Has no effect if sentence does not
     * exist.
     *
     * @param i
     */
    public void moveToSentence(int i) {
        //Find the correct caret position to move to by iterating through the 
        //intervals
        Tuple t;
        Iterator<Tuple> iterator = sentencePositions.iterator();
        int sentenceNumber = -1;
        while (iterator.hasNext()) {
            t = iterator.next();
            sentenceNumber += 1;
            if (sentenceNumber == i) {
                int caretPosition = (Integer) ((Integer) t.T1 + (Integer) t.T2) / 2;
                textPane.setCaretPosition(caretPosition);
            }
        }
    }

    /**
     * Select next sentence.
     */
    public void nextSentence() {
        //Find the current sentence based on caret position
        //Iterate through the sentence postitions
        int caretPosition = textPane.getCaretPosition();
        Tuple t;
        Iterator<Tuple> iterator = sentencePositions.iterator();
        while (iterator.hasNext()) {
            t = iterator.next();
            //Test if caret position is in the interval
            if ((Integer) t.T1 <= caretPosition && (Integer) t.T2 > caretPosition) {
                //Move the caret to center of the cext sentence
                if (iterator.hasNext()) {
                    t = iterator.next();
                    caretPosition = (Integer) ((Integer) t.T1 + (Integer) t.T2) / 2;
                    textPane.setCaretPosition(caretPosition);
                    return;
                }
            }
        }
    }

    /**
     * Select previous senence.
     */
    public void previousSentence() {
        //Find the current sentence based on caret position
        //Iterate through the sentence postitions
        int caretPosition = textPane.getCaretPosition();
        Tuple t1 = null, t2;
        Iterator<Tuple> iterator = sentencePositions.iterator();
        if (iterator.hasNext()) {
            t1 = iterator.next();
        }
        while (iterator.hasNext()) {
            t2 = iterator.next();
            //Test if caret position is in the interval
            if ((Integer) t2.T1 <= caretPosition && (Integer) t2.T2 > caretPosition) {
                if (t1 != null) {
                    //Move the caret to center of the previous sentence
                    caretPosition = (Integer) ((Integer) t1.T1 + (Integer) t1.T2) / 2;
                    textPane.setCaretPosition(caretPosition);
                }
            }
            t1 = t2;
        }
    }
}
