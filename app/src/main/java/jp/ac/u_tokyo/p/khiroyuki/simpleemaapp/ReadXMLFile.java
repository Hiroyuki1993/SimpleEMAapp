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
import java.util.HashMap;

public class ReadXMLFile {

    private static String qList[] = {"parent", "hq", "type", "order", "desc", "min", "max"};

    private ArrayList<String> roots = new ArrayList<>();
    private ArrayList<HashMap> questions = new ArrayList<>();
    private ArrayList<ArrayList<String>> items = new ArrayList<>();

    public ReadXMLFile(String path) throws XmlPullParserException, FileNotFoundException {
        InputStream stream = new BufferedInputStream(new FileInputStream(path));

        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setInput(stream, "utf-8");
            for(int e = myparser.getEventType(); e != XmlPullParser.END_DOCUMENT; e = myparser.next()){
                if(e == XmlPullParser.START_TAG && myparser.getName().equals("root")){
                    roots.add(myparser.nextText());
                }
                if(e == XmlPullParser.START_TAG && myparser.getName().equals("question")){
                    String itemId = myparser.getAttributeValue(null,"id");
                    HashMap<String,String> question = new HashMap<>();
                    ArrayList<String> item = new ArrayList<>();
                    for(e = myparser.getEventType();
                        e != XmlPullParser.END_TAG || !myparser.getName().equals("question");
                        e = myparser.next()){
                        String qTag = myparser.getName();
                        if(e == XmlPullParser.START_TAG){
                            for(String list:qList){
                                if(qTag.equals(list)){
                                    question.put(list,myparser.nextText());
                                }
                            }
                            if(qTag.equals("items")){
                                item.add(itemId);
                                question.put("id",itemId);
                                for(e = myparser.getEventType();
                                    e != XmlPullParser.END_TAG || !myparser.getName().equals("items");
                                    e = myparser.next()){
                                    String iTag = myparser.getName();
                                    if (e == XmlPullParser.START_TAG && myparser.getName().equals("item")){
                                        item.add(myparser.nextText());
                                    }
                                }
                                items.add(item);
                            }
                        }
                    }
                    questions.add(question);
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("roots",roots.toString());
        Log.d("questions",questions.toString());
        Log.d("items",items.toString());
    }
}
