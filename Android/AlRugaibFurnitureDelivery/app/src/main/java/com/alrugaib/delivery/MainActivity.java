package com.alrugaib.delivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alrugaib.delivery.communication.ApiHelper;
import com.alrugaib.delivery.model.Order;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Main Activity of application with routes and distance/time calculations
 */
public class MainActivity extends AppCompatActivity implements OrderAdapter.AdapterCallback {


    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 2345;

    private ArrayList<Location> locations = new ArrayList<>();
    private Location currentUserLocation;
    private double currentShortestDistance = 0;
    private List<Integer> currentPoints;
    private OrderAdapter adapter;
    private EditText invoiceInput;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView routeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        routeDetails = (TextView) findViewById(R.id.route_details);
        initList();
        initClickListeners();
        //Check permission on start
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            //Start update user locations
            getCurrentUserLocation();
        }


    }

    /**
     * Init onClick listeners and add action for them
     */
    private void initClickListeners() {
        findViewById(R.id.calculate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do not optimize if list is 0,1 element or is sorted already
                if (adapter.getCount() <= 1 || adapter.isSorted()) {
                    return;
                }
                routeDetails.setVisibility(View.GONE);
                // We need location of user
                if (currentUserLocation == null) {
                    Toast.makeText(MainActivity.this, "Please wait until we find Your location", Toast.LENGTH_SHORT).show();
                    return;
                }
                getAllCombinations();
            }
        });
        invoiceInput = (EditText) findViewById(R.id.invoice_number);

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeDetails.setVisibility(View.GONE);
                String inputText = invoiceInput.getText().toString();
                if (inputText.length() == 0) {
                    return;
                }
                String[] splitValues = inputText.split(",|\\.");

                if (splitValues != null) {
                    for (final String item : splitValues) {
                        getOrder(item);

                    }
                }
                invoiceInput.setText("");
            }
        });
        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                routeDetails.setVisibility(View.GONE);
                adapter.updateDataset(new ArrayList<Order>());
            }
        });
    }

    private void getOrder(final String invoiceNumber) {
        if (invoiceNumber.length() > 0) {
            ApiHelper.getInstance().getOrder(invoiceNumber, new Callback<Order>() {
                @Override
                public void onResponse(Response<Order> response, Retrofit retrofit) {
                    if (response != null && response.body() != null) {
                        adapter.addElement(response.body());
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.order_not_found)
                                + invoiceNumber, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(MainActivity.this, getString(R.string.order_not_found)
                            + invoiceNumber, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * init list with empty adapter list
     */
    private void initList() {
        ListView list = (ListView) findViewById(R.id.main_list);
        adapter = new OrderAdapter(this);
        list.setAdapter(adapter);
    }


    @Override
    public void onNavigateClicked(Order model) {
        double destinationLatitude = model.getDeliveryAddress().getLocation().latitude;
        double destinationLongitude = model.getDeliveryAddress().getLocation().longitude;

        String url = "http://maps.google.com/maps?f=d&daddr=" + destinationLatitude + "," + destinationLongitude + "&dirflg=d&layer=t";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public void onItemRemoved(int position) {

        routeDetails.setVisibility(View.GONE);
    }

    private <T> Collection<List<T>> generatePermutationsNoRepetition(Set<T> availableNumbers) {
        Collection<List<T>> permutations = new HashSet<>();

        for (T number : availableNumbers) {
            Set<T> numbers = new HashSet<>(availableNumbers);
            numbers.remove(number);

            if (!numbers.isEmpty()) {
                Collection<List<T>> childPermutations = generatePermutationsNoRepetition(numbers);
                for (List<T> childPermutation : childPermutations) {
                    List<T> permutation = new ArrayList<>();
                    permutation.add(number);
                    permutation.addAll(childPermutation);
                    permutations.add(permutation);
                    //  calculatePathDistance((List<Integer>) permutation);
                }
            } else {
                List<T> permutation = new ArrayList<>();
                permutation.add(number);
                permutations.add(permutation);
                // calculatePathDistance((List<Integer>) permutation);
            }
        }

        return permutations;
    }

    /**
     * Method to check all possibilities of linears geographic routes to find shortes one (Traveler Salesman)
     */
    private void getAllCombinations() {
        //Measure time of optimization
        long startTime = System.currentTimeMillis();
        for (Order order : adapter.getDataset()) {
            Location location = new Location(String.valueOf(order.getInvoiceNumber()));
            location.setLatitude(order.getDeliveryAddress().getLocation().latitude);
            location.setLongitude(order.getDeliveryAddress().getLocation().longitude);
            locations.add(location);
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < adapter.getDataset().size(); i++) {
            numbers.add(i);
        }

        Collection<List<Integer>> result = generatePermutationsNoRepetition(new HashSet<>(numbers));
        for (List<Integer> list : result) {
            calculatePathDistance(list);
        }

        //List from adapter
        List<Order> dataset = adapter.getDataset();
        //New lists with sorted values
        List<Order> newDataset = new ArrayList<>(dataset.size());
        for (int point : currentPoints) {
            newDataset.add(dataset.get(point));
        }
        adapter.updateDataset(newDataset);
        getDistanceFromPath(newDataset);
        Log.i("time of algorithm", System.currentTimeMillis() - startTime + " " + currentShortestDistance / 1000);

    }


    @Override
    protected void onPause() {
        if (locationManager != null) {
            //Dummy android stuff
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
        super.onPause();
    }


    /**
     * Method calling maps directions to measure the distance and duration via routes
     *
     * @param orderModels - sorted order list to get distance from it
     */
    private void getDistanceFromPath(List<Order> orderModels) {
        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/directions/json?");
        url.append("origin=" + currentUserLocation.getLatitude() + "," + currentUserLocation.getLongitude());
        url.append("&destination=" + orderModels.get(orderModels.size() - 1).getDeliveryAddress().getLocation().latitude
                + "," + orderModels.get(orderModels.size() - 1).getDeliveryAddress().getLocation().longitude);
        url.append("&waypoints=");//optimize:true
        //size()-1 because last point is set as destination
        for (int i = 0; i < orderModels.size() - 1; i++) {
            url.append("|" + orderModels.get(i).getDeliveryAddress().getLocation().latitude + ","
                    + orderModels.get(i).getDeliveryAddress().getLocation().longitude);
        }
        url.append("&sensor=false&units=metric&mode=driving");  //&key="+getString(R.string.google_map_key));
        Log.i("getDistanceFromPath " + orderModels.size(), url.toString());
        ApiHelper.getInstance().getDirections(url.toString(), new Callback<ResponseBody>() {

            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    //Parse response to get duration and distance of all "legs"
                    JSONObject responseObject = (JSONObject) new JSONTokener(response.body().string()).nextValue();
                    JSONArray routesArray = responseObject.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONArray legs;
                    JSONObject dist;
                    Integer distance = 0;
                    Integer time = 0;
                    if (route.has("legs")) {
                        legs = route.getJSONArray("legs");

                        int nlegs = legs.length();
                        for (int i = 0; i < nlegs; i++) {
                            JSONObject leg = legs.getJSONObject(i);
                            if (leg.has("distance")) {
                                dist = (JSONObject) leg.get("distance");// throws exception
                                distance += dist.getInt("value");
                            }
                            if (leg.has("duration")) {
                                dist = (JSONObject) leg.get("duration");// throws exception
                                time += dist.getInt("value");
                            }
                        }
                    }
                    if (distance > 0 && time > 0) {
                        showTimeAndDistance(distance, time);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure", "");
            }
        });


    }

    private void showTimeAndDistance(final Integer distance, final Integer time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * Show duration and distance of path by routes.
                 * if time < 60 then its seconds
                 * else if time > 60 then its minutes
                 * else time > 3600 then its hours
                 *
                 * if distance <1000 then its meters
                 * else if distance > 1000 then its kilometers
                 */
                routeDetails.setVisibility(View.VISIBLE);
                routeDetails.setText(getString(R.string.calculated_distance) + (distance > 1000 ? distance / 1000 + " km " : distance + " m ")
                        + " Duration " + (time > 60 ? (time > 3600 ? time / 3600 + " h " : time / 60 + " min ") : time + " sec "));
            }
        });
    }

    private void calculatePathDistance(List<Integer> path) {
        //calculate path linear
        double countDistance = currentUserLocation.distanceTo(locations.get(path.get(0)));
        for (int i = 0; i < path.size() - 1; i++) {
            countDistance += locations.get(path.get(i)).distanceTo(locations.get(path.get(i + 1))); //meters
        }
        // if its shortest then its current
        if (currentShortestDistance == 0 || currentShortestDistance > countDistance) {
            currentShortestDistance = countDistance;
            currentPoints = path;
            Log.d("shortest distance", currentShortestDistance / 1000 + " ");
        }

    }

    /**
     * We need user location to calculate distance and provide navigation
     */
    public void getCurrentUserLocation() {
        Criteria myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        myCriteria.setPowerRequirement(Criteria.POWER_LOW);
        // let Android select the right location provider for you
        String myProvider = locationManager.getBestProvider(myCriteria, true);

        // finally require updates at -at least- the desired rate
        long minTimeMillis = 5000; // 5sec update

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged " + location.toString());
                currentUserLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                Log.d(TAG, "onStatusChanged " + s);
            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d(TAG, "onProviderEnabled " + s);
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d(TAG, "onProviderDisabled " + s);
            }
        };
        locationManager.requestLocationUpdates(myProvider, minTimeMillis, 0, locationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentUserLocation();
                } else {
                    Toast.makeText(this, R.string.we_need_your_location, Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_ACCESS_COARSE_LOCATION);
                    }
                }

                break;
        }
    }
}
