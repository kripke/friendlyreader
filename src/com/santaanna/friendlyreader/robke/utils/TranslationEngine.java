/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//KANSKE INTE BEHÖVS, REFERERA TILL BING DIREKT?



package com.santaanna.friendlyreader.robke.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rybing
 */
public class TranslationEngine {
    private Class translateClass; 

    public TranslationEngine() {
        translateClass = BingAPITranslator.class;
        invokeTranslatorMethod("setKey", null);
    }
    
    // General method for finding and invoking a static method (method_name) 
    // with the given arguments (args). Return a general Object-type. 
    private Object invokeTranslatorMethod(String method_name, Object[] args) {
        //Extract argument types
        Class[] argsTypes = null;
        if(args!=null){
            argsTypes = new Class[args.length];
            for(int i = 0; i<args.length; i++) {
                argsTypes[i] = args[i].getClass();
            }
        }
        //Identify and run appropriate method
        try {
            Method m = translateClass.getMethod(method_name, argsTypes);
            return m.invoke(null, args);
        } 
        //Many catches...
        catch (IllegalAccessException ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "FAIL";
    }
    
    public void Speak(String text, String dia){
        //Speak refers to a static method within the BingAPI
        try {
            Object[] o = {text, dia};
            invokeTranslatorMethod("Speak", o);        
        } catch (Exception ex) {
            Logger.getLogger(TranslationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
