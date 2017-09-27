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
public class GetDetailInfoData extends AsyncTask<String,String,String>{

    private URL url;
    private InputStream inputStream;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private Activity activity;
    private ProgressActivity progressActivity;

    public GetDetailInfoData(Activity activity){
        this.activity = activity;
        this.progressActivity = new ProgressActivity(activity, ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressActivity.show("반복 정보를 가져오는 중입니다");
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.indexOf("'") != -1)
            result = result.replaceAll("'","&#39;");
        ((FullscreenActivity)activity).setDetailInfo(result);
        progressActivity.hide();
    }

    private String createUrl(String... params){
//        LONGITUDE":126.899664955153,"LATITUDE":37.5436300382788,
//        관광지 : 12
//        문화시설 : 14
//        행사/공연/축제 : 15
//        여행코스 : 25
//        레포츠 : 28
//        숙박 : 32
//        쇼핑 : 38
//        음식점 : 39

        String contentId = params[0];
        String contentTypeId = params[1];
        String sUrl = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailInfo?ServiceKey="+ APIKey.API_KEY_DATA_INFO+"&contentId="+contentId+"&contentTypeId="+contentTypeId+"&MobileOS=AND&MobileApp="+APIKey.API_KEY_APP_NAME;

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
