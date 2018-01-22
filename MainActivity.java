package com.example.android.guess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> scientistsURL=new ArrayList<String>();
    ArrayList<String> scientistsName=new ArrayList<String>();
String [] answer = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    int choosenScientist=0;
    ImageView imageView;
    int locationOfCorrectName=-1;
    int himan=-1;
    public void nameChoosed(View view) throws ExecutionException, InterruptedException {
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectName)))
        {
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Wrong ! its correct answer is"+scientistsName.get(choosenScientist), Toast.LENGTH_SHORT).show();
        }

        try {
            createNewQuestion();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    public class imageDownload extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try
            {
                URL url=new URL(urls[0]);
                HttpsURLConnection connection=(HttpsURLConnection)url.openConnection();
                connection.connect();
                InputStream inputStream=connection.getInputStream();
                Bitmap myBitmap= BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
public class DownloadTask extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... urls) {
        String result= " ";
        URL url;
        HttpsURLConnection urlConnection=null;
        try{
            url=new URL(urls[0]);
            urlConnection=(HttpsURLConnection)url.openConnection();
            InputStream in=urlConnection.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            int data=reader.read();
            while(data!=-1)
            {
                char current =(char)data;
                result+=current;
                data=reader.read();
            }
                return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed";
    }

}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        String roughHtml = null;
        DownloadTask task = new DownloadTask();
        try {
            roughHtml = task.execute("https://thebestschools.org/features/50-influential-scientists-world-today/").get();

            //<img class="alignleft size-full wp-image-22696
//<h4 class="widget-title widgettitle

            String[] Html = roughHtml.split("<img class=\"alignleft size-full wp-image-22696\"");
            String[] exactHtml = Html[1].split("<h4 class=\"widget-title widgettitle\"");

            Pattern p = Pattern.compile("src=\"(.*?)\" alt");
            Matcher m = p.matcher(exactHtml[0]);
            while (m.find()) {
                //  Log.i("",m.group(1));
                scientistsURL.add(m.group(1));
            }
            p = Pattern.compile("alt=\"(.*?)\" width");
            m = p.matcher(exactHtml[0]);
            while (m.find()) {
                //Log.i("",m.group(1));
                scientistsName.add(m.group(1));
            }
            Random random = new Random();
            choosenScientist = random.nextInt(scientistsURL.size());

            imageDownload task1 = new imageDownload();
            Bitmap myImage;

            myImage = task1.execute(scientistsURL.get(choosenScientist)).get();

            imageView.setImageBitmap(myImage);

            locationOfCorrectName = random.nextInt(4);

            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectName) {
               answer[i]=scientistsName.get(choosenScientist);

                } else {
                    himan = random.nextInt(scientistsURL.size());
                    while (himan == choosenScientist) {
                        himan = random.nextInt(scientistsURL.size());
                    }
                   answer[i]=scientistsName.get(himan);
                }
                button0.setText(answer[3]);
                button1.setText(answer[2]);
                button2.setText(answer[1]);
                button3.setText(answer[0]);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
public void createNewQuestion() throws ExecutionException, InterruptedException {
        Random random=new Random();
        choosenScientist=random.nextInt(scientistsURL.size());

        imageDownload task1=new imageDownload();
        Bitmap myImage;

        myImage=task1.execute(scientistsURL.get(choosenScientist)).get();

        imageView.setImageBitmap(myImage);

        locationOfCorrectName=random.nextInt(4);

    for (int i = 0; i < 4; i++) {
        if (i == locationOfCorrectName) {
            answer[i]=scientistsName.get(choosenScientist);

        } else {
            himan = random.nextInt(scientistsURL.size());
            while (himan == choosenScientist) {
                himan = random.nextInt(scientistsURL.size());
            }
            answer[i]=scientistsName.get(himan);
        }
        button0.setText(answer[3]);
        button1.setText(answer[2]);
        button2.setText(answer[1]);
        button3.setText(answer[0]);
    }

    }
}
