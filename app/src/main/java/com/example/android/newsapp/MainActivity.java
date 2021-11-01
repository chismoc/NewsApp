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
            if(null != inputStream){
//parse xml file using this method for required data
                try {
                    initXMLPullParser(inputStream);
                } catch (XmlPullParserException e) {
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
        private void initXMLPullParser(InputStream inputStream) throws XmlPullParserException {
       //Log.d(TAG,"initXMLPullParser: Initializing XML PullParser");
            XmlPullParser parser = Xml.newPullParser();
            //Distinguish different xml tags with same name
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
//feed parser with input
            parser.setInput(inputStream,null);
        }

    }
}