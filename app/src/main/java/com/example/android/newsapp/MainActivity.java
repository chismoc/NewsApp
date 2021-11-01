package com.example.android.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.example.android.newsapp.databinding.ActivityMainBinding;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);
    }

    private class GetNews extends AsyncTask<Void, Void, Void> {
        // private static final String TAG = ;

        //Implement background method to fetch news
        @Override
        protected Void doInBackground(Void... voids) {
            //Receive tream of data in xml using getInpuStream method

            InputStream inputStream = getInputStream();
            //check if inputStream is null or not
            if (null != inputStream) {
//parse xml file using this method for required data
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        private InputStream getInputStream() {
            //incase something goes wrong ie no internet connection
            try {
                URL url = new URL("https://www.autosport.com/rss/feed/f1");
                //connect to url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Get data from internet
                connection.setRequestMethod("GET");
                //expecting input from connection
                connection.setDoInput(true);
                //if connection get data
                return connection.getInputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //if somehting elsee happens
            return null;
        }

        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException, IOException {
            //Log.d(TAG,"initXMLPullParser: Initializing XML PullParser");
            XmlPullParser parser = Xml.newPullParser();
            //Distinguish different xml tags with same name
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//feed parser with input
            parser.setInput(inputStream, null);
            //move parser to next element
            parser.next();
            //move inside opening Rss tag
            parser.require(XmlPullParser.START_TAG, null, "rss");
            //check if its not end tag and loop inside the rss from one item to the next
            while (parser.next() != XmlPullParser.END_TAG) {
                //if not inside a start Tag continue to the next element
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                //check if inside channel element
                parser.require(XmlPullParser.START_TAG, null, "channel");
                //check if its not START_TAG jump to next element
                while (parser.next() != XmlPullParser.END_TAG) {
                    //if not inside a start Tag continue to the next element
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    //look for item element inside the channel element
                    //return name of current tag
                    if (parser.getName().equals("item")) {
                        //check if inside opening item tag
                        parser.require(XmlPullParser.START_TAG, null, "item");

                        //String for required data
                        String title = "";
                        String description = "";
                        String link = "";
                        String date = "";
                        //check if its not START_TAG jump to next element
                        while (parser.next() != XmlPullParser.END_TAG) {
                            //if not inside a start Tag continue to the next element
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            String tagName = parser.getName();
                            if (tagName.equals("title")) {
                                title = getContent(parser,"title");

                            } else if (tagName.equals("description")) {
                                description = getContent(parser,"description");

                            } else if (tagName.equals("link")) {
                                link = getContent(parser,"link");

                            } else if (tagName.equals("pubdate")) {
                                date = getContent(parser,"pubdate");

                            } else {//TODO skip tag
                            }
                        }

                    } else {
                        //TODO:Skip Tag
                    }
                }
            }

        }

        //method to get content
        private String getContent(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
            String content = "";
            //check if inside opening tag with that tag name
            //check if inside channel element
            parser.require(XmlPullParser.START_TAG, null, tagName);
            //check for text and get that text
            if (parser.next() == XmlPullParser.TEXT) {
//return the content of tag
                content = parser.getText();
                //move parser to nest element
                parser.next();
            }
            return content;
        }
    }
}