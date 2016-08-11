package com.alrugaibfurniture.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.alrugaibfurniture.BuildConfig;
import com.alrugaibfurniture.R;
import com.alrugaibfurniture.communication.ApiHelper;
import com.alrugaibfurniture.model.Address;
import com.alrugaibfurniture.model.CustomerProfile;
import com.alrugaibfurniture.model.OrderRequest;
import com.alrugaibfurniture.util.Logger;
import com.alrugaibfurniture.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.ResponseBody;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class CustomerActivity extends Activity {

    public static final String EXTRA_FROM_LOGIN = "extra_user";
    private static final int DEFAULT_ZOOM_LEVEL = 9;

    @Bind(R.id.input_email)
    EditText email;
    @Bind(R.id.input_mobile)
    EditText phone;
    @Bind(R.id.input_name)
    EditText name;

    @Bind(R.id.first_address)
    EditText firstName;
    @Bind(R.id.second_address)
    EditText secondName;
    @Bind(R.id.third_address)
    EditText thirdName;

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
    private CustomerProfile currentUser;
    private int currentAddressNumber = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        email.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        name.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        phone.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        phone.setTransformationMethod(null);
        if (getIntent() != null && getIntent().hasExtra(EXTRA_FROM_LOGIN)) {
            currentUser = (CustomerProfile) getIntent().getSerializableExtra(EXTRA_FROM_LOGIN);
            initViewWithCurrent();
        }
        initMaps(savedInstanceState);
    }

    private void initViewWithCurrent() {
        if (currentUser.getEmail() != null) {
            email.setText(currentUser.getEmail());
        }
        if (currentUser.getFullName() != null) {
            name.setText(currentUser.getFullName());
        }
        if (currentUser.getPhoneNumber() != null) {
            phone.setText(currentUser.getPhoneNumber());
        }
    }

    /**
     * Init maps and listeners
     *
     * @param savedInstanceState
     */
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
                Address address = getAddress(0);
                if (address != null) {
                    firstMarker = first.addMarker(new MarkerOptions()
                            .position(address.getLocation())
                            .title("First address")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    firstName.setText(address.getName());
                }
                first.moveCamera(CameraUpdateFactory.newLatLngZoom(firstMarker != null ?
                        firstMarker.getPosition() :
                        new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));
                first.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(firstMarker, 0);
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
                        updateCurrentMarker(firstMarker, 0);
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
                Address address = getAddress(1);
                if (address != null) {
                    secondMarker = second.addMarker(new MarkerOptions()
                            .position(address.getLocation())
                            .title("Second address")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    secondName.setText(address.getName());
                }
                second.moveCamera(CameraUpdateFactory.newLatLngZoom(secondMarker != null ?
                        secondMarker.getPosition() :
                        new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));

                second.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(secondMarker, 1);
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
                        updateCurrentMarker(secondMarker, 1);
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
                Address address = getAddress(2);
                if (address != null) {
                    thirdMarker = third.addMarker(new MarkerOptions()
                            .position(address.getLocation())
                            .title("Third address")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    thirdName.setText(address.getName());
                }
                third.moveCamera(CameraUpdateFactory.newLatLngZoom(thirdMarker != null ?
                        thirdMarker.getPosition() :
                        new LatLng(BuildConfig.MAP_HEIGHT, BuildConfig.MAP_WIDTH), DEFAULT_ZOOM_LEVEL));
                third.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        updateCurrentMarker(thirdMarker, 2);
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
                        updateCurrentMarker(thirdMarker, 2);
                        setBorderToChoosenMap(2);
                    }
                });
            }
        });
    }

    @Nullable
    private Address getAddress(int which) {
        if (currentUser.getDeliveryAddresses() == null) {
            return null;
        }
        if (which < currentUser.getDeliveryAddresses().size()) {
            return currentUser.getDeliveryAddresses().get(which);
        }
        return null;

    }

    private void updateCurrentMarker(Marker marker, int which) {
        currentAddressNumber = which;
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
        final String validMsg = validInput();

        Logger.logD("onSubmitClicked", validMsg + " ");
        if (validMsg == null) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.sure_to_submit));
            builder.append(name.getText().toString() + "\n");
            builder.append(phone.getText().toString() + "\n");
            builder.append(email.getText().toString() + "\n");
            builder.append(currentAddressNumber == 0 ? firstName.getText().toString()
                    : (currentAddressNumber == 1 ? secondName.getText().toString() :
                    thirdName.getText().toString()) + "\n");

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            edittext.setHint(R.string.invoice_number);
            edittext.setSingleLine();
            alert.setMessage(builder.toString());
            alert.setTitle(R.string.confirmation);

            alert.setView(edittext);

            alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = edittext.getText().toString();
                    if (value.length() > 0) {
                        makeOrder(value);
                    }
                }
            });

            alert.setNegativeButton(R.string.cancel, null);
            alert.show();
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

    private void makeOrder(String value) {
        updateCurrentUser();

        OrderRequest order = new OrderRequest();
        order.setCustomerProfile(currentUser);
        order.setDeliveryAddress(new Address(currentAddressNumber, currentAddressNumber == 0 ?
                firstName.getText().toString()
                : (currentAddressNumber == 1 ? secondName.getText().toString() :
                thirdName.getText().toString()),
                currentMarker.getPosition().latitude, currentMarker.getPosition().longitude));
        order.setDeviceId(Settings.Secure.getString(CustomerActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID));
        order.setDeliveryAddressNumber(currentAddressNumber);
        order.setInvoiceNumber(value);
        ApiHelper.getInstance().makeOrder(order, new Callback<OrderRequest>() {
            @Override
            public void onResponse(Response<OrderRequest> response, Retrofit retrofit) {
                Toast.makeText(CustomerActivity.this, R.string.order_done, Toast.LENGTH_LONG).show();
                Intent a = new Intent(CustomerActivity.this, SplashActivity.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(a);
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(CustomerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCurrentUser() {
        currentUser.setEmail(email.getText().toString());
        currentUser.setFullName(name.getText().toString());
        currentUser.setPhoneNumber(phone.getText().toString());
        //update current user
        ArrayList<Address> addresses = new ArrayList<>(3);
        if (firstMarker != null) {
            addresses.add(new Address(0, "First address",
                    firstMarker.getPosition().latitude, firstMarker.getPosition().longitude));
        }
        if (secondMarker != null) {
            addresses.add(new Address(1, "Second address",
                    secondMarker.getPosition().latitude, secondMarker.getPosition().longitude));
        }
        if (thirdMarker != null) {
            addresses.add(new Address(2, "Third address",
                    thirdMarker.getPosition().latitude, thirdMarker.getPosition().longitude));
        }
        currentUser.setContactAddresses(addresses);
    }

    /**
     * Method validating input values when submitting
     *
     * @return
     */
    private String validInput() {
        boolean isValid = true;
        StringBuilder builder = new StringBuilder();
        if (currentMarker == null) {
            builder.append(getString(R.string.choose_address) + "\n");
            isValid = false;
        }

        // builder.append("Please input correct:\n");
        if (name.getText().length() == 0) {
            builder.append(getString(R.string.fill) + " " + getString(R.string.full_name) + "\n");
            isValid = false;
        }
        if (email.getText().length() == 0) {
            builder.append(getString(R.string.fill) + " " + getString(R.string.hint_email_address) + "\n");
            isValid = false;
        } else if (!Util.isEmailValid(email.getText().toString())) {
            builder.append(getString(R.string.correct) + " " + getString(R.string.hint_email_address) + "\n");
            isValid = false;
        }

        if (phone.getText().length() == 0) {
            builder.append(getString(R.string.fill) + " " + getString(R.string.hint_phone_number) + "\n");
            isValid = false;
        } else if (!Util.validCellPhone(phone.getText().toString())) {
            builder.append(getString(R.string.correct) + " " + getString(R.string.hint_phone_number) + "\n");
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
