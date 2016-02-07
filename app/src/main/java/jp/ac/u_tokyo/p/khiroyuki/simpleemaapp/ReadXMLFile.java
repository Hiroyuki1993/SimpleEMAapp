package jp.ac.u_tokyo.p.khiroyuki.simpleemaapp;

import android.content.Context;

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
    private String errMsg = "";
    private boolean pathEmpty = false;
    private boolean imperfect = false;
    private ArrayList<String> roots = new ArrayList<>();
    private ArrayList<HashMap> questions = new ArrayList<>();
    private ArrayList<String[]> items = new ArrayList<>();

    public ReadXMLFile(String path) throws XmlPullParserException, FileNotFoundException {
        if(path.isEmpty()){
            pathEmpty = true;
            return;
        }

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
                    HashMap<String,String> question = new HashMap<String,String>(){
                        {put("id","");}
                        {put("hq","");}
                        {put("type", "");}
                        {put("order","");}
                        {put("desc","");}
                        {put("min","");}
                        {put("max","");}
                    };
                    question.put("id", itemId);
                    for(e = myparser.getEventType();
                        e != XmlPullParser.END_TAG || !myparser.getName().equals("question");
                        e = myparser.next()){
                        if(e == XmlPullParser.END_DOCUMENT){
                            imperfect = true;
                            break;
                        }
                        String qTag = myparser.getName();
                        if(e == XmlPullParser.START_TAG){
                            for(String list:qList){
                                if(qTag.equals(list)){
                                    question.put(list,myparser.nextText());
                                }
                            }
                            if(qTag.equals("items")){
                                for(e = myparser.getEventType();
                                    e != XmlPullParser.END_TAG || !myparser.getName().equals("items");
                                    e = myparser.next()){
                                    if (e == XmlPullParser.END_DOCUMENT){
                                        imperfect = true;
                                        break;
                                    }
                                    if (e == XmlPullParser.START_TAG && myparser.getName().equals("item")){
                                        String[] item = new String[2];
                                        item[0] = itemId;
                                        item[1] = myparser.nextText();
                                        items.add(item.clone());
                                    }
                                }
                            }
                        }
                    }
                    questions.add(question);
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public String errMsg(Context c){
        if(pathEmpty){
            errMsg = c.getResources().getString(R.string.path_null_warn);
        } else if (imperfect){
            errMsg = c.getResources().getString(R.string.imperfectInq);
        } else if (roots.isEmpty()){
            errMsg = c.getResources().getString(R.string.root_item_err);
        } else {
            switch (QuestionValid()){
                case 0:break;
                case 1:
                    errMsg = c.getResources().getString(R.string.q_item_null);
                    break;
                case 2:
                    errMsg = c.getResources().getString(R.string.qIdErr);
                    break;
                case 3:
                    errMsg = c.getResources().getString(R.string.qParentErr);
                    break;
                case 4:
                    errMsg = c.getResources().getString(R.string.qTypeErr);
                    break;
                case 5:
                    errMsg = c.getResources().getString(R.string.qOrderErr);
                    break;
                case 6:
                    errMsg = c.getResources().getString(R.string.qItemErr);
                    break;
                case 7:
                    errMsg = c.getResources().getString(R.string.hq_err);
                    break;
                default:
                    break;
            }
        }
        return errMsg;
    }

    private int QuestionValid(){
        int errType = 0;
        if(questions.isEmpty()){
            errType = 1;
        } else {
            for(HashMap q:questions){
                if(!q.containsKey("id")){
                    errType=2;
                    break;
                } else if (q.get("id").toString().isEmpty()){
                    errType=3;
                    break;
                }
                if(!q.containsKey("hq")){
                    errType=7;
                    break;
                }
                if(!q.containsKey("parent")){
                    errType=3;
                    break;
                } else if (q.get("parent").toString().isEmpty()){
                    errType=3;
                    break;
                }
                if(!q.containsKey("type")){
                    errType=4;
                    break;
                } else if (q.get("type").toString().isEmpty()){
                    errType=4;
                    break;
                }
                if(!q.containsKey("order")){
                    errType=5;
                    break;
                } else if (q.get("order").toString().isEmpty()){
                    errType=5;
                    break;
                }
                /*if (q.get("type").toString().equals("radio")){
                    if(!items.containsKey(q.get("id"))){
                        errType=6;
                        break;
                    } else if (items.get(q.get("id")).isEmpty()){
                        errType=6;
                        break;
                    }
                }*/
            }
        }

        return errType;
    }

    public String[] returnRoots(){
        return roots.toArray(new String[roots.size()]);
    }
    public HashMap[] returnQuestions(){
        return questions.toArray(new HashMap[questions.size()]);
    }
    public ArrayList<String[]> returnItems() {
        return items;
    }
}
