package com.example.bt3_rss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Customadapter customadapter;
    ArrayList<Docbao> mangdocbao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        mangdocbao = new ArrayList<Docbao>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Đưa dữ liêu lấy được lên listview
                //new Readdata().execute("https://www.petfoodindustry.com/rss/topic/296-vitamins");
                new Readdata().execute("https://vnexpress.net/rss/khoa-hoc.rss");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                        /** XỬ LÍ SỰ KIỆN CLICK VÀO TỪNG MỤC ITEMS **/
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("link",mangdocbao.get(position).link);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /** TRONG RSS LẤY CÁC THÔNG TIN QUAN TRỌNG **/
    class Readdata extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            // Nhờ có phương thức XMLDOMParser ta có thể tương tác với XML
            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);
            NodeList nodeList = document.getElementsByTagName("item");
            NodeList nodeListdescription = document.getElementsByTagName("description");
            String hinhanh = "";
            String title = "";
            String link = "";

            // Duyệt toàn bộ danh sách XML
            for (int i = 0 ; i < nodeList.getLength() ; i++){
                String cdata = nodeListdescription.item(i + 1).getTextContent(); // Bỏ description đầu tiên
                Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
                Matcher matcher = p.matcher(cdata);
                if (matcher.find()){
                    hinhanh = matcher.group(1);
                }
                Element element = (Element) nodeList.item(i);
                title = parser.getValue(element,"title");
                link = parser.getValue(element,"link");
                mangdocbao.add(new Docbao(title,link,hinhanh));
            }
            customadapter = new Customadapter(MainActivity.this,android.R.layout.simple_list_item_1,mangdocbao);
            listView.setAdapter(customadapter);
            super.onPostExecute(s);
        }
    }

    /** ĐỌC NỘI DUNG CỦA MỘT URL INTERNET **/
    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // Tạo đối tượng URL
            URL url = new URL(theUrl);

            // Tạo một đối tượng để kết nối với URL
            URLConnection urlConnection = url.openConnection();

            // Đặt URLConnection vào trong bufferedReader (bộ đệm)
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // Đọc dữ liệu từ URLConection bằng bufferedReader (bộ đệm)
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
}