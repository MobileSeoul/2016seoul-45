package niee.kr.park.service.event;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import niee.kr.park.FullscreenActivity;

/**
 * Created by niee on 2016. 9. 13..
 */
public class BackPressHandler {
    private long pressTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressHandler(Activity activity){
        this.activity = activity;
    }

    public void onBackPressed(){
        if(System.currentTimeMillis() > pressTime + 1000){
            pressTime = System.currentTimeMillis();
            showMessage();
            return;
        }else if(System.currentTimeMillis() <= pressTime + 1000){
            toast.cancel();
            Intent intent = new Intent(activity, FullscreenActivity.class);
            activity.startActivity(intent);

            activity.moveTaskToBack(true);
            activity.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private void showMessage() {
        toast = Toast.makeText(activity, "뒤로가기를 한번더 누르시면 종료됩니다",Toast.LENGTH_SHORT);
        toast.show();
    }
}
