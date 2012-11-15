package com.santaanna.friendlyreader.utils.bingapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rybing
 */
public class HTTPConnection {

    /**
* Sends an HTTP GET request to a url
*
* @param endpoint - The URL of the server. (Example: " http://www.yahoo.com/search")
* @param requestParameters - all the request parameters 
     * (Example: "param1=val1&param2=val2"). 
     * Note: This method will add the question mark (?) to the request - 
     * DO NOT add it yourself
     * 
* @return - The response from the end point
     * 
*/
    
    public static String bingTranslateString(String translate, String from, 
            String to) {
        String translation = "";
        String webserver = "http://api.microsofttranslator.com/v2/Http.svc/Translate?";
        String myID = "417BC5DCED1017058BB12B3434B5B42D90D25BE6";
        String parameters = "appId=" + myID + "&text;=" + 
                translate + "&from;=" + from + "&to;=" + to;

        // Send a GET request to the servlet
        try {
            // The complete url
            String urlStr = webserver + parameters;
            System.out.println("URL: " + urlStr);

            //Create and open URL connection
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();

            // Get the response
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) response.append(line);

            rd.close();
            translation = response.toString();

        } catch (Exception e) {
            System.out.println("Translation failed");
            e.printStackTrace();
        }

        return translation;
    }   


    public static String getLanguages() throws MalformedURLException, IOException{
        String appIdParam = "417BC5DCED1017058BB12B3434B5B42D90D25BE6";
        String languagesnames = "http://api.microsofttranslator.com/V1/Http.svc/GetLanguages";
        String urlQuery = languagesnames+"?"+appIdParam;
        URL lurl = new URL(urlQuery);

        //Create and open URL connection
        URLConnection conn = null;

        try {
            conn = lurl.openConnection();
        } catch (IOException ex) {
            System.out.println("CANNOT CONNECT");
            Logger.getLogger(HTTPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get the response
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) response.append(line);
        
        rd.close();
        String lang = response.toString();

        return lang;
    }
    
}

