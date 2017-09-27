package niee.kr.park;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import niee.kr.park.service.data.GetDetailInfoData;
import niee.kr.park.service.data.GetDetailIntroData;
import niee.kr.park.service.data.GetPM1025Data;
import niee.kr.park.service.data.GetParkData;
import niee.kr.park.service.data.GetParkProgramsData;
import niee.kr.park.service.data.GetTouristData;
import niee.kr.park.service.data.GetWeatherData;
import niee.kr.park.service.ui.ProgressActivity;
import niee.kr.park.service.util.JavaScriptBridge;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity {

    public static final int DAUM_MAP_INTENT_REQUEST_CODE = 100001;
    private WebView webView;
    private ProgressActivity progressActivity;
    private JavaScriptBridge javaScriptBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        if(!isNetWork()){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FullscreenActivity.this);
            alert_confirm.setMessage("인터넷에 연결 되어 있지 않습니다. 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 'YES'
                            finish();
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 'No'
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
        progressActivity = new ProgressActivity(this, ProgressDialog.STYLE_SPINNER);
        javaScriptBridge = new JavaScriptBridge(this);

        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(javaScriptBridge,"JavascriptBridge");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!progressActivity.isShowing())
                    progressActivity.show("데이터를 가져오는 중입니다.");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressActivity.hide();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressActivity.hide();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("tel:")){
                    Intent dial = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    //현재의 activity 에 대하여 startActivity 호출
                    startActivity(dial);
                    return true;
                }else if(!url.startsWith("https://parkminkyu.github.io")){
                    return super.shouldOverrideUrlLoading(view,url);
                }
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
                WebView newWebView = new WebView(FullscreenActivity.this);

                WebView.WebViewTransport transport = (WebView.WebViewTransport)resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();

                return true;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://parkminkyu.github.io/mplatform");
    }

    private Boolean isNetWork(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:goBack()");
            }
        });
    }

    public void getParkList() {
        new GetParkData(FullscreenActivity.this).execute("");
    }

    public void setParkList(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setParkList("+result+")");
            }
        });
    }

    public void getParkProgramList(final String parkId) {
        new GetParkProgramsData(FullscreenActivity.this).execute(parkId);
    }

    public void setParkProgramList(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setParkProgramList("+result+")");
            }
        });
    }

    public void getWeather(final String lat, final String lng){
        new GetWeatherData(FullscreenActivity.this).execute(lat,lng);
    }

    public void setWeather(final String result){
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setWeather('"+result+"')");
            }
        });
    }

    public void getTourist(String lat, String lng, String contentTypeId) {
        new GetTouristData(FullscreenActivity.this).execute(lat,lng, contentTypeId);
    }

    public void getTourist(String lat, String lng, String contentTypeId, String areaCode, String sigunguCode) {
        new GetTouristData(FullscreenActivity.this).execute(lat,lng, contentTypeId, areaCode, sigunguCode);
    }

    public void setTourist(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setTourist('"+result+"')");
            }
        });
    }

    public void getPM1025(String areaName) {
        new GetPM1025Data(this).execute(areaName);
    }

    public void setPM1025(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setPM1025('"+result+"')");
            }
        });
    }

    public void getDetailIntro(String contentId, String contentTypeId) {
        new GetDetailIntroData(FullscreenActivity.this).execute(contentId, contentTypeId);
    }

    public void getDetailInfo(String contentId, String contentTypeId) {
        new GetDetailInfoData(FullscreenActivity.this).execute(contentId, contentTypeId);
    }

    public void setDetailInfo(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setDetailInfo('"+result+"')");
            }
        });
    }

    public void setDetailIntro(final String result) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:setDetailIntro('"+result+"')");
            }
        });
    }

    public void showMap(final boolean isOnePoint, final String lat, final String lng, final String name){
        Intent intent = new Intent(getBaseContext(), DaumMapActivity.class);
        intent.putExtra("isOnePoint", isOnePoint);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        intent.putExtra("name",name);

        startActivityForResult(intent, DAUM_MAP_INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == DAUM_MAP_INTENT_REQUEST_CODE){
            if(requestCode == RESULT_OK){

            }
        }
    }

    public void exitApplication() {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FullscreenActivity.this);
        alert_confirm.setMessage("프로그램을 종료 하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'YES'
                        finish();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 'No'
                        return;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }
}