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
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import niee.kr.park.FullscreenActivity;
import niee.kr.park.service.common.APIKey;
import niee.kr.park.service.common.AppOption;
import niee.kr.park.service.ui.ProgressActivity;
import niee.kr.park.service.util.LatLngToXY;

/**
 * Created by niee on 2016. 9. 13..
 */
public class GetWeatherData extends AsyncTask<String,String,String>{

    private URL url;
    private InputStream inputStream;
    private HttpURLConnection httpURLConnection;
    private BufferedReader bufferedReader;
    private Map map;
    private Activity activity;
    private ProgressActivity progressActivity;

    public GetWeatherData(Activity activity){
        this.activity = activity;
        this.progressActivity = new ProgressActivity(activity, ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressActivity.show("지역 날씨정보를 가져오는 중입니다");
    }

    @Override
    protected void onPostExecute(String result) {
        ((FullscreenActivity)activity).setWeather(result);
        progressActivity.hide();
    }

    private String createUrl(String... params){
        //LONGITUDE":126.899664955153,"LATITUDE":37.5436300382788,
        if(params[0]!= null && params[1]!= null && !params[0].equals("") && !params[1].equals(""))
            map = LatLngToXY.transToXY(Float.parseFloat(params[0]),Float.parseFloat(params[1]));
        else
            map = LatLngToXY.transToXY(37.5436300382788f,126.899664955153f);

        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        String yyyy = "" + calendar.get(Calendar.YEAR);
        String mm = ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : "" + (calendar.get(Calendar.MONTH) + 1));
        String dd = ((calendar.get(Calendar.DATE) ) < 10 ? "0" + (calendar.get(Calendar.DATE)) : "" + (calendar.get(Calendar.DATE)));

        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mi = calendar.get(Calendar.MINUTE);
        if(mi < 40) hh -= 1;
        if(hh < 0){
            hh = 23;
            calendar.add(Calendar.DATE, -1);
            yyyy = "" + calendar.get(Calendar.YEAR);
            mm = ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : "" + (calendar.get(Calendar.MONTH) + 1));
            dd = ((calendar.get(Calendar.DATE) ) < 10 ? "0" + (calendar.get(Calendar.DATE)) : "" + (calendar.get(Calendar.DATE)));
        }

        String getHH = (hh < 10 ? "0" + hh : "" + hh);
        String sUrl = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?ServiceKey="+ APIKey.API_KEY_DATA_INFO+"&numOfRows=999&pageNo=1&nx="+map.get("nx")+"&ny="+map.get("ny")+"&base_date="+yyyy+mm+dd+"&base_time="+getHH+"00";
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
