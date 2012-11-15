package merge.gui.gui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import com.santaanna.friendlyreader.robke.utils.CharsetDetector;
import com.santaanna.friendlyreader.robke.utils.TextAssistant;
import com.santaanna.friendlyreader.pdfstod.pdfstod3.ReplaceStringStreamEngine;
import com.santaanna.friendlyreader.summarization.evaluation.coherence.PortableStanfordCoref;
import com.santaanna.friendlyreader.summarization.reader.Summary;
import com.santaanna.friendlyreader.summarization.reader.SummaryReader;
import com.santaanna.friendlyreader.summarization.summarizer.PortableCorefSum;
import com.santaanna.friendlyreader.summarization.Settings;
import com.santaanna.friendlyreader.summarization.util.DocumentUtils;
import com.santaanna.friendlyreader.robke.utils.Speak;
import com.santaanna.friendlyreader.robke.utils.SpeakEvent;
import com.santaanna.friendlyreader.robke.utils.SpeakEventListener;
import com.santaanna.friendlyreader.robke.utils.WordTool;

/**
 *
 * @author Robin Keskisärkkä
 */
public class OurNewFriend extends javax.swing.JFrame {
    //Set the horizontal distance between the popup and the selected text

    private int popupPadding = 3;
    private TextAssistant assistant;
    public boolean continueTalking = false;
    //WordTool laddas här just nu men kanske ska laddas på
    //serversidan? Annars kan man ju skicka WordTool-instansen
    //istället för att baka in synonymerna i wordklasserna?
    private WordTool wordTool = new WordTool();
    
    private ReplaceStringStreamEngine rsse;
    private String globalFilePath;

    /**
     * Creates new form OurNewFriend
     */
    public OurNewFriend() {
        //Set up the GUI and listeners
        setTitle("Friendly Reader");
        initComponents();
        setupListeners();
        resizeScrollPane();

        //Add popup to GUI
        layeredPane.add(popup, JLayeredPane.POPUP_LAYER);
        popup.setVisible(false);

        //Create a text assistant and setup text properties
        assistant = new TextAssistant(textPane);
        assistant.setMargins(10, 20, 10, 20);
        assistant.setTextSize(16, 14, 30);
        assistant.setLineSpacing(0.1f, -0.2f, 1.5f);
        assistant.setBackgroundColor(Color.red);
        assistant.setTextColor(Color.black);

        //Synch assistant with GUI
        assistant.setShowSentenceIndexes(toggleShowIndexes.isSelected());
        assistant.setHighlight(toggleHighlight.isSelected());
        try {
            rsse = new ReplaceStringStreamEngine();
        } catch (IOException ex) {
            Logger.getLogger(OurNewFriend.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    /**
     * Setup listeners.
     */
    private void setupListeners() {
        textPane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showPopup();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                updatePopup();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                updatePopup();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                updatePopup();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                updatePopup();
            }
        });

        textPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                updatePopup();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_RIGHT) {
                    assistant.nextSentence();
                } else if (key == KeyEvent.VK_LEFT) {
                    assistant.previousSentence();
                }
                updatePopup();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updatePopup();
            }
        });

        summarySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                assistant.setPercentage(summarySlider.getValue());
                statusLabel.setText("Texten motsvarar "
                        + summarySlider.getValue() + "% av ursprungstexten.");
            }
        });

        scrollPane.getVerticalScrollBar().addAdjustmentListener(
                new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                updatePopup();
                            }
                        });
                    }
                });
    }

    /**
     * Resize the scroll panel to fit within its container.
     */
    private void resizeScrollPane() {
        scrollPane.setSize(bigPanel.getSize());
    }

    /**
     * Returns the visual location of a caret position, i, relative to the text
     * pane's scroll pane.
     *
     * @param i
     * @return Point
     */
    private Point getCorrectedCaretLocation(int i) {
        Point p;
        try {
            p = textPane.modelToView(i).getLocation();
        } catch (BadLocationException ex) {
            return null;
        }
        return new Point(
                (int) p.getX(),
                (int) p.getY() + textPane.getY());
    }

    /**
     * Displays the popup if a piece of text has been selected.
     */
    public void showPopup() {
        String selectedText = textPane.getSelectedText();
        if (selectedText == null || selectedText.equals("")) {
            popup.setVisible(false);
            return;
        }

        ArrayList<String> synonyms = wordTool.getSynonyms(selectedText, 5);
        if (synonyms == null || synonyms.size() < 2) {
            return;
        }

        //Setup the visual layout of the popup
        StyledDocument doc = new DefaultStyledDocument();

        Style heading = doc.addStyle("heading", null);
        StyleConstants.setFontSize(heading, 16);
        StyleConstants.setFontFamily(heading, "Calibri");
        StyleConstants.setBold(heading, true);

        Style normal = doc.addStyle("normal", null);
        StyleConstants.setFontSize(normal, 16);
        StyleConstants.setFontFamily(normal, "Calibri");
        int lines = 0;
        try {
            doc.insertString(0, synonyms.get(0).toLowerCase(), heading);
            for (int i = 1; i < synonyms.size(); i++) {
                doc.insertString(doc.getLength(), "\n  " + synonyms.get(i), normal);
                lines += 1;
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(OurNewFriend.class.getName()).log(Level.SEVERE, null, ex);
        }
        popupTextPane.setDocument(doc);
        popup.setVisible(true);

        FontMetrics fm1 = popupTextPane.getFontMetrics(doc.getFont(heading));
        FontMetrics fm2 = popupTextPane.getFontMetrics(doc.getFont(normal));
        popup.setSize(120, fm1.getHeight() + lines * fm2.getHeight() + 20);
        updatePopupLocation();
    }

    /**
     * Update the location of the popup.
     */
    private void updatePopupLocation() {
        //Position of popup is relative to the scroll and true panel
        int end = textPane.getSelectionEnd();
        int start = textPane.getSelectionStart();

        //Make sure end > start, oterwise swap places
        if (end < start) {
            int temp = start;
            start = end;
            end = temp;
        }
        Point p;
        p = getCorrectedCaretLocation(start);
        Point startPoint = new Point((int) p.getX(), (int) p.getY());
        p = getCorrectedCaretLocation(end);
        Point endPoint = new Point((int) p.getX(), (int) p.getY());

        //Assume that the box fits on the rigth side and hanging
        Point popupPoint = endPoint;
        int popupWidth = popup.getWidth();
        int popupHeight = popup.getHeight();
        int hAlign = 0; //-1=left, 0=right, -0.5=center
        int vAlign = 0; //0=down, -1=up, -0.5=center

        //Check if popup fits the right side
        if (popupPoint.getX() + popupWidth + popupPadding > scrollPane.getWidth()) {
            popupPoint = startPoint;
            hAlign = -1;
        }
        //Check if popup fits below
        if (popupPoint.getY() + popupHeight + popupPadding > scrollPane.getHeight()) {
            vAlign = -1;
        }

        popup.setLocation(new Point(
                (int) popupPoint.getX() + hAlign * popupWidth,
                (int) popupPoint.getY() + vAlign * (popupHeight - assistant.getHighlightHeight())));
    }

    /**
     * Updates the position of the popup, or hides it if no text has been
     * selected.
     */
    private void updatePopup() {
        if (textPane.getSelectionEnd() == textPane.getSelectionStart()) {
            popup.setVisible(false);
        }
        if (popup.isVisible()) {
            updatePopupLocation();
        }
        if (textPane.getSelectedText() == null) {
            assistant.highlightCurrentSentence();
        }
        textPane.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popup = new javax.swing.JScrollPane();
        popupTextPane = new javax.swing.JTextPane();
        toolPanel = new javax.swing.JPanel();
        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnAddTextSize = new javax.swing.JButton();
        btnSubtractTextSize = new javax.swing.JButton();
        btnSubtractLineSpace = new javax.swing.JButton();
        btnAddLineSpace = new javax.swing.JButton();
        btnTalk = new javax.swing.JButton();
        toggleHighlight = new javax.swing.JToggleButton();
        toggleShowIndexes = new javax.swing.JToggleButton();
        sliderPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        summarySlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        statusPanel = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        bigPanel = new javax.swing.JPanel();
        layeredPane = new javax.swing.JLayeredPane();
        scrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();

        popup.setFocusable(false);
        popup.setPreferredSize(new java.awt.Dimension(100, 100));
        popup.setRequestFocusEnabled(false);

        popupTextPane.setEditable(false);
        popupTextPane.setFocusable(false);
        popupTextPane.setRequestFocusEnabled(false);
        popup.setViewportView(popupTextPane);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        btnOpen.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/open.png"))); // NOI18N
        btnOpen.setToolTipText("Öppna en fil");
        btnOpen.setPreferredSize(new java.awt.Dimension(40, 40));
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/save_edited.png"))); // NOI18N
        btnSave.setToolTipText("Spara en fil (funktionen är ej tillgänglig)");
        btnSave.setEnabled(false);
        btnSave.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnAddTextSize.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnAddTextSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/glyphicons_236_zoom_in.png"))); // NOI18N
        btnAddTextSize.setToolTipText("Gör texten större");
        btnAddTextSize.setPreferredSize(new java.awt.Dimension(40, 40));
        btnAddTextSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTextSizeActionPerformed(evt);
            }
        });

        btnSubtractTextSize.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnSubtractTextSize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/glyphicons_237_zoom_out.png"))); // NOI18N
        btnSubtractTextSize.setToolTipText("Gör texten mindre");
        btnSubtractTextSize.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSubtractTextSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubtractTextSizeActionPerformed(evt);
            }
        });

        btnSubtractLineSpace.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnSubtractLineSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/linespace_smaller.png"))); // NOI18N
        btnSubtractLineSpace.setToolTipText("Mindre radavstånd");
        btnSubtractLineSpace.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSubtractLineSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubtractLineSpaceActionPerformed(evt);
            }
        });

        btnAddLineSpace.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnAddLineSpace.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/linespace_larger.png"))); // NOI18N
        btnAddLineSpace.setToolTipText("Större radavstånd");
        btnAddLineSpace.setPreferredSize(new java.awt.Dimension(40, 40));
        btnAddLineSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLineSpaceActionPerformed(evt);
            }
        });

        btnTalk.setFont(new java.awt.Font("Tahoma", 0, 10));
        btnTalk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/volume_up.png"))); // NOI18N
        btnTalk.setToolTipText("Aktivera uppläsning");
        btnTalk.setPreferredSize(new java.awt.Dimension(40, 40));
        btnTalk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTalkActionPerformed(evt);
            }
        });

        toggleHighlight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/sentence.png"))); // NOI18N
        toggleHighlight.setSelected(true);
        toggleHighlight.setToolTipText("Avaktivera meningsmarkör");
        toggleHighlight.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleHighlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleHighlightActionPerformed(evt);
            }
        });

        toggleShowIndexes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/glyphicons/numbers.png"))); // NOI18N
        toggleShowIndexes.setToolTipText("Aktivera meningsnummer");
        toggleShowIndexes.setPreferredSize(new java.awt.Dimension(40, 40));
        toggleShowIndexes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleShowIndexesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout toolPanelLayout = new javax.swing.GroupLayout(toolPanel);
        toolPanel.setLayout(toolPanelLayout);
        toolPanelLayout.setHorizontalGroup(
            toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubtractTextSize, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddLineSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubtractLineSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(toggleHighlight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toggleShowIndexes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 320, Short.MAX_VALUE)
                .addComponent(btnTalk, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        toolPanelLayout.setVerticalGroup(
            toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toolPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toolPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnTalk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSubtractLineSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddLineSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSubtractTextSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddTextSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toggleHighlight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toggleShowIndexes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 24));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("-");
        jLabel7.setFocusable(false);
        jLabel7.setPreferredSize(new java.awt.Dimension(20, 20));

        summarySlider.setMinimum(1);
        summarySlider.setOrientation(javax.swing.JSlider.VERTICAL);
        summarySlider.setValue(100);
        summarySlider.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 24));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("+");
        jLabel4.setFocusable(false);
        jLabel4.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout sliderPanelLayout = new javax.swing.GroupLayout(sliderPanel);
        sliderPanel.setLayout(sliderPanelLayout);
        sliderPanelLayout.setHorizontalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(summarySlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sliderPanelLayout.createSequentialGroup()
                        .addGroup(sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 17, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sliderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summarySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        statusLabel.setMaximumSize(new java.awt.Dimension(300, 14));
        statusLabel.setMinimumSize(new java.awt.Dimension(300, 14));
        statusLabel.setPreferredSize(new java.awt.Dimension(300, 14));

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(470, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrollPane.setBackground(new java.awt.Color(220, 220, 220));
        scrollPane.setOpaque(false);

        textPane.setEditable(false);
        textPane.setBackground(new java.awt.Color(220, 220, 220));
        textPane.setBorder(null);
        textPane.setFont(new java.awt.Font("Tahoma", 0, 16));
        scrollPane.setViewportView(textPane);

        scrollPane.setBounds(0, 0, 400, 280);
        layeredPane.add(scrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout bigPanelLayout = new javax.swing.GroupLayout(bigPanel);
        bigPanel.setLayout(bigPanelLayout);
        bigPanelLayout.setHorizontalGroup(
            bigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
        );
        bigPanelLayout.setVerticalGroup(
            bigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(layeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(toolPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sliderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This method allows a user to load a new file.
     *
     * @param evt
     */
    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        final JFileChooser fc = new JFileChooser();
        int x = this.getWidth() / 2 - fc.getWidth() / 2;
        int y = this.getHeight() / 2 - fc.getHeight() / 2;
        fc.setLocation(x, y);
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Välj en TXT- eller PDF-fil", "txt", "TXT", "pdf", "PDF");
        fc.setFileFilter(filter);
        fc.setCurrentDirectory(new java.io.File("."));
        fc.setDialogTitle("Öppna");

        int returnVal = fc.showOpenDialog(this);
        File document;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fc.getSelectedFile().getCanonicalPath();
                
                this.globalFilePath = filePath;
                
                String ending = filePath.split("\\.")[filePath.split("\\.").length - 1];
                System.out.println(ending);

                if (ending.equals("txt")) {
                    document = new File(filePath);
                    Reader reader = new InputStreamReader(new FileInputStream(document),
                            CharsetDetector.getCharset(document));

                    BufferedReader br = new BufferedReader(reader);
                    String text = "";
                    String line;
                    while ((line = br.readLine()) != null) {
                        text += line;
                    }

		Settings s = new Settings();
		s.setRemoveStopWords(false);

                PortableCorefSum cs = new PortableCorefSum(s);
                PortableStanfordCoref psf = new PortableStanfordCoref();
                String out = psf.process(text);
                DocumentUtils.save(out, "parse");
                cs.setCorefFile("parse");
                Summary sum = cs.buildSummary();
                
                SummaryReader read = new SummaryReader(sum);
                
                    summarySlider.setValue(100);
                    assistant.setText(read, 100);
                    //Move back to beginning of text
                    assistant.moveToSentence(0);
                    
                } else if (ending.equals("pdf")) {
//                    //For enabling saving of file a reference to the helper
//                    //must be kept in memory, otherwise the pdf will be need
//                    //to be re-read
//                    try {
//
//                        Collection<SEmening> test = this.rsse.doIt(
//                                this.globalFilePath,"AHprepout.pdf",
//                                true, summarySlider.getValue(),-1);
//
//                        StringBuilder text = new StringBuilder();
//                        for(SEmening sent : test)
//                            text.append(sent.helameningen);
//
//                        Sentences s = new Sentences(text.toString());
//                        summarySlider.setValue(100);
//                        assistant.setText(s, 100);
//                        assistant.moveToSentence(0);
//
//
//                    } catch (COSVisitorException ex) {
//                        Logger.getLogger(OurNewFriend.class.getName()).
//                                log(Level.SEVERE, null, ex);
//                    }
                    //Idén är här att få ut den rena texteen ur pdf-filen
                    //Jag skapade en hjälpklass som skulle fixa det men stötte
                    //på problem. Innifrån lyckades jag få ut den hela texten
                    //genom att använda:
                    //String text = "";
                    //for (SEmening s : sentenceVector) {
                    //    text += s.helameningen + " ";
                    //
                    //Helst ska det se ut så här:
//                    PdfHelper pdfHelper = new PdfHelper();
//                    String text = pdfHelper.getTextFromPdf(filePath);
                    

                    //Pass pure text to FriendlyReader
//                    Sentences s = new Sentences(text);
//                    summarySlider.setValue(100);
//                    assistant.setText(s, 100);
////                    //Move back to beginning of text
//                    assistant.moveToSentence(0);
                }
            } catch (IOException ex) {
                System.out.println("File not found");
                return;
            }
        }
    }//GEN-LAST:event_btnOpenActionPerformed

    /**
     * Save file. Not implemented.
     *
     * @param evt
     */
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

    /**
     * Make text larger.
     *
     * @param evt
     */
    private void btnAddTextSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTextSizeActionPerformed
        assistant.changeTextSize(2);
    }//GEN-LAST:event_btnAddTextSizeActionPerformed

    /**
     * Make text smaller.
     *
     * @param evt
     */
    private void btnSubtractTextSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubtractTextSizeActionPerformed
        assistant.changeTextSize(-2);
    }//GEN-LAST:event_btnSubtractTextSizeActionPerformed

    /**
     * Make line spacing smaller.
     *
     * @param evt
     */
    private void btnSubtractLineSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubtractLineSpaceActionPerformed
        assistant.changeLineSpacing(-0.1f);
    }//GEN-LAST:event_btnSubtractLineSpaceActionPerformed

    /**
     * Make line spaing bigger.
     *
     * @param evt
     */
    private void btnAddLineSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLineSpaceActionPerformed
        assistant.changeLineSpacing(0.1f);
    }//GEN-LAST:event_btnAddLineSpaceActionPerformed

    /**
     * Activate/stop speech function.
     *
     * @param evt
     */
    private void btnTalkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTalkActionPerformed
        continueTalking = !continueTalking;
        if (assistant.isTalking()) {
            Image img = new javax.swing.ImageIcon(getClass().getResource("/glyphicons/volume_up.png")).getImage();
            ImageIcon icon = new ImageIcon(img);
            btnTalk.setIcon(icon);
            btnTalk.setToolTipText("Aktivera uppläsning");
            //Kill the thread
            assistant.stopTalk();
            continueTalking = false;
        } else {
            continueTalking = true;
            Image img = new javax.swing.ImageIcon(getClass().getResource("/glyphicons/glyphicons_175_stop.png")).getImage();
            ImageIcon icon = new ImageIcon(img);
            btnTalk.setIcon(icon);
            btnTalk.setToolTipText("Stoppa uppläsning");
            startTalking(false);
        }
    }//GEN-LAST:event_btnTalkActionPerformed

    /**
     * Activates the autoamtic talk functionality. Passing jumNext=true jumps to
     * the next sentence before activating the talk functionality. If not
     * stopped manually this method will call itself and incrementally progress
     * through the text until the end is reached.
     *
     * @param jumpNext
     */
    private void startTalking(boolean jumpNext) {
        //If jumpNext is true then go to next sentence
        if (jumpNext) {
            int index = assistant.getCurrentSentenceIndex();
            assistant.nextSentence();
            //If end is reached, stop
            if (index == assistant.getCurrentSentenceIndex()) {
                continueTalking = false;
                Image img = new javax.swing.ImageIcon(getClass().getResource("/glyphicons/volume_up.png")).getImage();
                ImageIcon icon = new ImageIcon(img);
                btnTalk.setIcon(icon);
            }
            assistant.highlightCurrentSentence();
        }

        if (continueTalking) {
            //The GUI must know when the thread has stopped, i.e. when
            //a SpeakEvent has occured.
            Speak speaker = assistant.talk();
            if (speaker != null) {
                speaker.addSpeakEventListener(new SpeakEventListener() {
                    @Override
                    public void speakStopOccured(SpeakEvent e) {
                        if (continueTalking) {
                            startTalking(true);
                        }
                    }
                });
            }
        }
    }

    /**
     * Toggle sentence helper on/off
     *
     * @param evt
     */
    private void toggleHighlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleHighlightActionPerformed
        if (toggleHighlight.isSelected()) {
            toggleHighlight.setToolTipText("Avaktivera meningsmarkör");
        } else {
            toggleHighlight.setToolTipText("Aktivera meningsmarkör");
        }
        assistant.setHighlight(toggleHighlight.isSelected());
    }//GEN-LAST:event_toggleHighlightActionPerformed

    /**
     * Add index numbers to the sentences.
     *
     * @param evt
     */
    private void toggleShowIndexesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleShowIndexesActionPerformed
        if (toggleShowIndexes.isSelected()) {
            toggleShowIndexes.setToolTipText("Avaktivera meningsnummer");
        } else {
            toggleShowIndexes.setToolTipText("Aktivera meningsnummer");
        }
        assistant.setShowSentenceIndexes(toggleShowIndexes.isSelected());
    }//GEN-LAST:event_toggleShowIndexesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OurNewFriend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OurNewFriend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OurNewFriend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OurNewFriend.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OurNewFriend().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bigPanel;
    private javax.swing.JButton btnAddLineSpace;
    private javax.swing.JButton btnAddTextSize;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSubtractLineSpace;
    private javax.swing.JButton btnSubtractTextSize;
    private javax.swing.JButton btnTalk;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLayeredPane layeredPane;
    private javax.swing.JScrollPane popup;
    private javax.swing.JTextPane popupTextPane;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JSlider summarySlider;
    private javax.swing.JTextPane textPane;
    private javax.swing.JToggleButton toggleHighlight;
    private javax.swing.JToggleButton toggleShowIndexes;
    private javax.swing.JPanel toolPanel;
    // End of variables declaration//GEN-END:variables
}
