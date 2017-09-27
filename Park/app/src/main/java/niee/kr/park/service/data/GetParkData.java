package niee.kr.park.service.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import niee.kr.park.FullscreenActivity;
import niee.kr.park.service.common.APIKey;
import niee.kr.park.service.common.AppOption;
import niee.kr.park.service.ui.ProgressActivity;

/**
 * Created by niee on 2016. 9. 13..
 */
public class GetParkData extends AsyncTask<String,String,String>{

    private URL url;
    private InputStream inputStream;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private Activity activity;
    private ProgressActivity progressActivity;

    public GetParkData(Activity activity){
        this.activity = activity;
        this.progressActivity = new ProgressActivity(activity, ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressActivity.show("공원 리스트를 가져오는 중입니다");
    }

    @Override
    protected void onPostExecute(String result) {
        ((FullscreenActivity)activity).setParkList(result);
        progressActivity.hide();
    }

    @Override
    protected String doInBackground(String... strings) {
        String resultStr = "";
        String tempStr = "";
        try {
            url = new URL("http://openapi.seoul.go.kr:8088/"+ APIKey.API_KEY_PARK_INFO+"/json/SearchParkInfoService/1/100/");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(AppOption.HTTP_READ_TIME_OUT);
            httpURLConnection.setConnectTimeout(AppOption.HTTP_CONNECT_TIME_OUT);
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while((tempStr = bufferedReader.readLine()) != null){
                resultStr += tempStr;
            }
            Log.d("resultStr : ",resultStr);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultStr;
    }
}
