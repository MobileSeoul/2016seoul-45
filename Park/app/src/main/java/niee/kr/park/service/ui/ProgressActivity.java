package niee.kr.park.service.ui;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by niee on 2016. 9. 14..
 */
public class ProgressActivity {

    private ProgressDialog progressDialog;

    public ProgressActivity(Activity activity, int progressStyle){
        progressDialog = new ProgressDialog(activity);
        progressDialog.setProgressStyle(progressStyle);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void show(String message){
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void show(String title, String message){
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public boolean isShowing(){
        return progressDialog.isShowing();
    }

    public void hide(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
