/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.summarization.striphtml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import moj.util.TagStripper;

/**
 *
 * @author christian
 */
public class StripHtml {
    public static void main(String[] args) throws MalformedURLException, IOException{

        // create a new tag stripper
        TagStripper ts = new TagStripper();

        // open an url connection to the specified adress
        URL url = new URL("http://sv.wikipedia.org/wiki/Henrik_VIII_av_England");
        InputStreamReader input = new InputStreamReader(url.openConnection().getInputStream());

        // use the tag stripper to strip most of the tags
        String result = ts.stripHTML(input);

        //print the result
        System.out.println(result);
    }
}
