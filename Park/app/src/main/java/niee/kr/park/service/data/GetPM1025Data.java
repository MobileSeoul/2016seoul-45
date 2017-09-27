package niee.kr.park.service.data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import niee.kr.park.FullscreenActivity;
import niee.kr.park.service.common.APIKey;
import niee.kr.park.service.common.AppOption;
import niee.kr.park.service.ui.ProgressActivity;

/**
 * Created by niee on 2016. 9. 13..
 */
public class GetPM1025Data extends AsyncTask<String,String,String>{

    private URL url;
    private InputStream inputStream;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private Activity activity;
    private ProgressActivity progressActivity;

    public GetPM1025Data(Activity activity){
        this.activity = activity;
        this.progressActivity = new ProgressActivity(activity, ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressActivity.show("지역 미세먼지 정보를 가져오는 중입니다");
    }

    @Override
    protected void onPostExecute(String result) {
        ((FullscreenActivity)activity).setPM1025(result);
        progressActivity.hide();
    }

    private String createUrl(String... params){
        String areaName = params[0];
        String sUrl = null;
        try {
            sUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="+ URLEncoder.encode(areaName, "UTF-8")+"&dataTerm=month&pageNo=1&numOfRows=1&ServiceKey="+ APIKey.API_KEY_DATA_INFO;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sUrl;
    }

    @Override
    protected String doInBackground(String... strings) {
        String resultStr = "";
        String tempStr = "";
        try {
            url = new URL(createUrl(strings));
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
