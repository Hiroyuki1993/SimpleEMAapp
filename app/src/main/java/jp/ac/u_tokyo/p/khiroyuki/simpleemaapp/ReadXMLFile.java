package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.app.Activity;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReadXMLFile {
    public ReadXMLFile(String path) throws XmlPullParserException, FileNotFoundException {
        InputStream stream = new BufferedInputStream(new FileInputStream(path));
        ArrayList<String> roots = new ArrayList<String>();
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setInput(stream, "utf-8");
            int counter = 0;
            for(int e = myparser.getEventType(); e != XmlPullParser.END_DOCUMENT; e = myparser.next()){
                if(e == XmlPullParser.START_TAG && myparser.getName().equals("root")){
                    roots.add(myparser.nextText());
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("roots",roots.toString());
    }
}
