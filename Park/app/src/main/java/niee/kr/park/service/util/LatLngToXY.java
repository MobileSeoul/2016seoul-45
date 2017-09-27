package niee.kr.park.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by niee on 2016. 9. 14..
 */
public class LatLngToXY {

    static final float RE = 6371.00877f; // 지구 반경(km)
    static final float GRID = 5.0f; // 격자 간격(km)
    static final float SLAT1 = 30.0f; // 투영 위도1(degree)
    static final float SLAT2 = 60.0f; // 투영 위도2(degree)
    static final float OLON = 126.0f; // 기준점 경도(degree)
    static final float OLAT = 38.0f; // 기준점 위도(degree)
    static final float XO = 43f; // 기준점 X좌표(GRID)
    static final float YO = 136f; // 기1준점 Y좌표(GRID)

    public static Map transToXY(float lat, float lng) {
        // LCC DFS 좌표변환 ( code : "toXY"(위경도->좌표, v1:위도, v2:경도), "toLL"(좌표->위경도,v1:x, v2:y) )
        float DEGRAD = ((float)Math.PI) / 180.0f;
        float RADDEG = 180.0f / ((float)Math.PI);

        float re = RE / GRID;
        float slat1 = SLAT1 * DEGRAD;
        float slat2 = SLAT2 * DEGRAD;
        float olon = OLON * DEGRAD;
        float olat = OLAT * DEGRAD;

        float sn = (float)Math.tan(((float)Math.PI) * 0.25 + slat2 * 0.5) / (float)Math.tan(((float)Math.PI) * 0.25 + slat1 * 0.5);
        sn = (float)Math.log((float)Math.cos(slat1) / (float)Math.cos(slat2)) / (float)Math.log(sn);
        float sf = (float)Math.tan((float)Math.PI * 0.25 + slat1 * 0.5);
        sf = (float)Math.pow(sf, sn) * (float)Math.cos(slat1) / sn;
        float ro = (float)Math.tan((float)Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / (float)Math.pow(ro, sn);
        Map rs = new HashMap();
        float ra = (float)Math.tan((float)Math.PI * 0.25 + (lat) * DEGRAD * 0.5);
        ra = re * sf / (float)Math.pow(ra, sn);
        float  theta = lng * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.put("nx",(int)((float)Math.floor(ra * Math.sin(theta) + XO + 0.5)));
        rs.put("ny",(int)((float)Math.floor(ro - ra * Math.cos(theta) + YO + 0.5)));
        return rs;
    }
}
