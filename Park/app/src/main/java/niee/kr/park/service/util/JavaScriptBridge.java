package niee.kr.park.service.util;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.kakao.KakaoParameterException;

import java.util.HashMap;
import java.util.Map;

import niee.kr.park.FullscreenActivity;
import niee.kr.park.service.kakao.KaKaoLinkService;

/**
 * Created by niee on 2016. 9. 14..
 */
public class JavaScriptBridge {
    private Activity activity;
    private KaKaoLinkService kaKaoLinkService;

    public JavaScriptBridge(Activity activity){
        this.activity = activity;
        try {
            kaKaoLinkService = new KaKaoLinkService(activity);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getWeather(String lat, String lng){
        ((FullscreenActivity)activity).getWeather(lat, lng);
    }

    @JavascriptInterface
    public void getPM1025(String areaName){
        ((FullscreenActivity)activity).getPM1025(areaName);
    }

    @JavascriptInterface
    public void getParkList(){
        ((FullscreenActivity)activity).getParkList();
    }

    @JavascriptInterface
    public void getParkProgramList(String parkId){
        ((FullscreenActivity)activity).getParkProgramList(parkId);
    }

    @JavascriptInterface
    public void sendMessage(String lat, String lng, String img, String name, String addr) throws KakaoParameterException {
        kaKaoLinkService.sendMessage( lat, lng, img, name, addr);
    }

    @JavascriptInterface
    public void showMap(boolean isOnePoint, String lat, String lng, String name){
        ((FullscreenActivity)activity).showMap(isOnePoint, lat, lng, name);
    }

    @JavascriptInterface
    public void getTourist(String lat, String lng, String contentTypeId){
        ((FullscreenActivity)activity).getTourist(lat, lng, contentTypeId);
    }

    @JavascriptInterface
    public void getTourist(String lat, String lng, String contentTypeId, String areaCode, String sigunguCode){
        ((FullscreenActivity)activity).getTourist(lat, lng, contentTypeId, areaCode, sigunguCode);
    }

    @JavascriptInterface
    public void getDetailIntro( String contentId, String contentTypeId){
        ((FullscreenActivity)activity).getDetailIntro(contentId, contentTypeId);
    }

    @JavascriptInterface
    public void getDetailInfo( String contentId, String contentTypeId){
        ((FullscreenActivity)activity).getDetailInfo(contentId, contentTypeId);
    }

    @JavascriptInterface
    public void exitApplication(){
       ((FullscreenActivity)activity).exitApplication();
    }
}
