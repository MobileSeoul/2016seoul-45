package niee.kr.park.service.kakao;

import android.app.Activity;

import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

/**
 * Created by niee on 2016. 9. 14..
 */
public class KaKaoLinkService {
    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private Activity activity;

    public KaKaoLinkService(Activity activity) throws KakaoParameterException {
        this.activity = activity;
        kakaoLink = KakaoLink.getKakaoLink(activity);
        kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
    }

    public void sendMessage(String lat, String lng, String img, String name, String addr) throws KakaoParameterException {

        kakaoTalkLinkMessageBuilder
                .addImage(img, 512, 512)
                .addText(name + "/\n" + addr)
                .addWebLink("위치 확인", "http://map.daum.net/link/map/"+name+","+lat+","+lng)
                .build();
        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(),activity);
        kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
    }
}
