package niee.kr.park;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

/**
 * Created by niee on 2016. 9. 18..
 */
public class DaumMapActivity extends Activity implements MapView.MapViewEventListener {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daummap);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey("43ef3e3fef08b48b3f18a7a64abb4a44");

        RelativeLayout mapViewContainer = (RelativeLayout) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Intent intent = getIntent();
        boolean isOnePoint = intent.getBooleanExtra("isOnePoint", true);
        String lat = isOnePoint ? intent.getStringExtra("lat") : intent.getStringExtra("lat").split(",")[0];
        String lng = isOnePoint ? intent.getStringExtra("lng") : intent.getStringExtra("lng").split(",")[0];
        String name = isOnePoint ? intent.getStringExtra("name") : intent.getStringExtra("name").split(",")[0];

        MapPoint mapPoint =  MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat), Double.parseDouble(lng));

        // 중심점 변경
        mapView.setMapCenterPointAndZoomLevel( mapPoint, 4, false);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.

        mapView.addPOIItem(marker);
        mapView.selectPOIItem(marker,false);

        if(!isOnePoint){
            String lat2 = intent.getStringExtra("lat").split(",")[1];
            String lng2 = intent.getStringExtra("lng").split(",")[1];
            String name2 = intent.getStringExtra("name").split(",")[1];

            MapPoint mapPoint2 =  MapPoint.mapPointWithGeoCoord(Double.parseDouble(lat2), Double.parseDouble(lng2));
            mapView.setMapCenterPointAndZoomLevel( mapPoint, 7, false);

            MapPOIItem marker2 = new MapPOIItem();
            marker2.setItemName(name2);
            marker2.setTag(1);
            marker2.setMapPoint(mapPoint2);
            marker2.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.

            mapView.addPOIItem(marker2);
            mapView.selectPOIItem(marker2,false);
        }
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
}
