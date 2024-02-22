package com.example.recomap;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.recomap.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LatLng currLating = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mapInit();
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    private void mapInit() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CameraPosition.Builder builder = new CameraPosition.Builder();
//
        builder.target((new LatLng(35.681382,139.766084)));
        builder.zoom(15.5f);

        CameraPosition cameraPosition = builder.build();


        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        UiSettings ui = mMap.getUiSettings();
        ui.setCompassEnabled(false);
        ui.setRotateGesturesEnabled(false);
        ui.setRotateGesturesEnabled(false);
        ui.setTiltGesturesEnabled(false);
        ui.setZoomControlsEnabled(true);
        ui.setZoomGesturesEnabled(false);

        MarkerOptions op = new MarkerOptions();


markerSet();

    }


    private void markerSet() {
        ArrayList<ShopInfo> list = getShopInfoes();

        for (ShopInfo shop : list) {
            if (shop.getPosition() != null) { // Ensure position is not null
                MarkerOptions options = new MarkerOptions();
                options.position(shop.getPosition());
                options.title(shop.getName());
                options.snippet(shop.getInfomation());
                mMap.addMarker(options);
            }
        }
    }


//        options.position(new LatLng(35.696346,139.698336));
//        options.draggable(false);
//        options.title("ラーメンリロ");
//        options.snippet("子って小手のラーメン,完食はむずかし(7/14)");
//        mMap.addMarker(options);
//
//        options.position(new LatLng(35.695339,139.696587));
//        options.draggable(false);
//        options.title("ラーメン風来屋");
//        options.snippet("スープにとろみのある塩豚骨(7/14)");
//        mMap.addMarker(options);


    private ArrayList<ShopInfo> getShopInfoes() {
        ArrayList<ShopInfo> ary = new ArrayList<ShopInfo>();

        ShopInfo tmp1 = new ShopInfo();
        tmp1.setIntcategory(1);
        tmp1.setInfomation(String.valueOf(new LatLng(35.6963346,139.698336)));
        tmp1.setName("ラーメン二郎");
        tmp1.setInfomation("子って小手のラーメン。完食はむずかし(7/14)");
        ary.add(tmp1);


        ShopInfo tmp2 = new ShopInfo();
        tmp2.setIntcategory(2);
        tmp2.setInfomation(String.valueOf(new LatLng(35.695339,139.696587)));
        tmp2.setName("ラーメン二郎");
        tmp2.setInfomation("子って小手のラーメン。完食はむずかし(7/14)");
        ary.add(tmp2);



//        ShopInfo tmp3 = new ShopInfo();
//        tmp3.setIntcategory(3);
//        tmp3.setInfomation(String.valueOf(new LatLng(35.695339,139.696587)));
//        tmp3.setName("ラーメン");
//        tmp3.setInfomation("ラーメン");
//        ary.add(tmp3);


        return ary;
    }
}