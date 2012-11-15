
package merge.summarization.easyreader.GUI;

import javax.swing.JFrame;

/**
 *
 * @author Kricke
 */
public class RunGUI {

    public static void main(String[] args){

        JFrame fr = new JFrame();
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        JFrame fr2 = new JFrame();
//        fr2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fr.add(new SummarizerWindow());
//        fr2.add(new SettingsWindow());

        fr.pack();
        fr.setVisible(true);

//        fr2.pack();
//        fr2.setVisible(true);
    }
}
