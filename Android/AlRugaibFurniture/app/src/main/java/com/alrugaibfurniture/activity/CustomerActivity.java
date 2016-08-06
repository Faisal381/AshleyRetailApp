package com.alrugaibfurniture.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.alrugaibfurniture.BuildConfig;
import com.alrugaibfurniture.R;
import com.alrugaibfurniture.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomerActivity extends Activity {

    public static final String EXTRA_NUMBER = "numberExtra";
    private static final int DEFAULT_ZOOM_LEVEL = 9;

    @Bind(R.id.input_email)
    EditText email;
    @Bind(R.id.input_mobile)
    EditText phone;
    @Bind(R.id.input_name)
    EditText name;

    @Bind(R.id.map_choosen)
    MapView mapChoosen;
    @Bind(R.id.map_first)
    MapView mapFirst;
    @Bind(R.id.map_second)
    MapView mapSecond;
    @Bind(R.id.map_third)
    MapView mapThird;

    private GoogleMap current;
    private GoogleMap first;
    private GoogleMap second;
    private GoogleMap third;
    private Marker firstMarker;
    private Marker secondMarker;
    private Marker thirdMarker;
    private Marker currentMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        email.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        name.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        phone.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        phone.setTransformationMethod(null);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_NUMBER)) {
            phone.setText(getIntent().getStringExtra(EXTRA_NUMBER));
        }
        initMaps(savedInstanceState);
    }

    private void initMaps(Bundle savedInstanceState) {
        mapChoosen.onCreate(savedInstanceState);
        mapChoosen.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                current = googleMap;
                current.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));

            }
        });
        mapFirst.onCreate(savedInstanceState);
        mapFirst.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                first = googleMap;
                first.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));
                first.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(firstMarker);
                        setBorderToChoosenMap(0);
                    }
                });
                first.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if (firstMarker != null) { //if marker exists (not null or whatever)
                            firstMarker.setPosition(latLng);
                        } else {
                            firstMarker = first.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("First address")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                        updateCurrentMarker(firstMarker);
                        setBorderToChoosenMap(0);
                    }
                });
            }
        });
        mapSecond.onCreate(savedInstanceState);
        mapSecond.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                second = googleMap;
                second.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));
                second.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(secondMarker);
                        setBorderToChoosenMap(1);
                    }
                });
                second.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if (secondMarker != null) { //if marker exists (not null or whatever)
                            secondMarker.setPosition(latLng);
                        } else {
                            secondMarker = second.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Second address")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                        updateCurrentMarker(secondMarker);
                        setBorderToChoosenMap(1);
                    }
                });
            }
        });
        mapThird.onCreate(savedInstanceState);
        mapThird.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                third = googleMap;
                third.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));
                third.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(thirdMarker);
                        setBorderToChoosenMap(2);
                    }
                });
                third.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        if (thirdMarker != null) { //if marker exists (not null or whatever)
                            thirdMarker.setPosition(latLng);
                        } else {
                            thirdMarker = third.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title("Third address")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                        updateCurrentMarker(thirdMarker);
                        setBorderToChoosenMap(2);
                    }
                });
            }
        });
    }

    private void updateCurrentMarker(Marker marker) {
        if (marker == null) {
            if (currentMarker != null) {
                currentMarker.remove();
                currentMarker = null;
            }
        } else if (currentMarker != null) {
            currentMarker.setPosition(marker.getPosition());
        } else {
            currentMarker = current.addMarker(new MarkerOptions()
                    .position(marker.getPosition())
                    .title(getString(R.string.delivery_address))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        if (currentMarker != null) {
            current.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), DEFAULT_ZOOM_LEVEL));
            // Zoom in, animating the camera.
            current.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            current.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_LEVEL), 2000, null);
        }
    }

    private void setBorderToChoosenMap(int which) {
        mapFirst.setBackground(which == 0 ? getDrawable(R.drawable.flag_background_border) : null);
        mapSecond.setBackground(which == 1 ? getDrawable(R.drawable.flag_background_border) : null);
        mapThird.setBackground(which == 2 ? getDrawable(R.drawable.flag_background_border) : null);
    }

    @OnClick(R.id.submit_button)
    void onSubmitClicked() {
        String validMsg = validInput();

        Log.e("onSubmitClicked", validMsg+ " ");
        if (validMsg == null) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.sure_to_submit));
            builder.append(name.getText().toString()+"\n");
            builder.append(phone.getText().toString()+"\n");
            builder.append(email.getText().toString()+"\n");
            builder.append("Address one"+"\n");

            new AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation)
                    .setMessage(builder.toString())
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent a = new Intent(CustomerActivity.this, SplashActivity.class);
                            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(a);
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.information_incomplete)
                    .setMessage(validMsg)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .show();
        }
    }


    private String validInput() {
        boolean isValid = true;
        StringBuilder builder = new StringBuilder();
        if (currentMarker == null) {
            builder.append(getString(R.string.choose_address) + "\n");
            isValid = false;
        }

        // builder.append("Please input correct:\n");
        if (name.getText().length() == 0) {
            builder.append(getString(R.string.fill)+" " +getString(R.string.full_name) + "\n");
            isValid = false;
        }
        if (email.getText().length() == 0 ) {
            builder.append(getString(R.string.fill)+" " +getString(R.string.hint_email_address) + "\n");
            isValid = false;
        }else if(!Util.isEmailValid(email.getText().toString())){
            builder.append(getString(R.string.correct)+" " +getString(R.string.hint_email_address) + "\n");
            isValid = false;
        }

        if (phone.getText().length() == 0) {
            builder.append(getString(R.string.fill)+" " +getString(R.string.hint_phone_number) + "\n");
            isValid = false;
        }else if(!Util.validCellPhone(phone.getText().toString())){
            builder.append(getString(R.string.correct)+" "+getString(R.string.hint_phone_number) + "\n");
            isValid = false;
        }

        if (!isValid) {
            return getString(R.string.do_following_actions) + builder.toString();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapChoosen.onResume();
        mapFirst.onResume();
        mapSecond.onResume();
        mapThird.onResume();
    }

    @Override
    protected void onPause() {
        mapChoosen.onPause();
        mapFirst.onPause();
        mapSecond.onPause();
        mapThird.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapChoosen.onDestroy();
        mapFirst.onDestroy();
        mapSecond.onDestroy();
        mapThird.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapChoosen.onSaveInstanceState(outState);
        mapFirst.onSaveInstanceState(outState);
        mapSecond.onSaveInstanceState(outState);
        mapThird.onSaveInstanceState(outState);
    }
}
