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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements OrderAdapter.AdapterCallback {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 2345;
    private ArrayList<OrderModel> pointsPath = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private Location currentUserLocation;
    private double currentShortestDistance = 0;
    private List<Integer> currentPoints;
    private long startTime;
    private OrderAdapter adapter;
    private ListView list;
    private EditText invoiceInput;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        initList();
        initClickListeners();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        } else {
            getCurrentUserLocation();
        }
//mock data
        //    currentUserLocation.setLatitude(26.192662);
        //   currentUserLocation.setLongitude(50.198229);
        pointsPath.add(new OrderModel(6, new LatLng(26.157573, 50.185384)));

        pointsPath.add(new OrderModel(2, new LatLng(26.177473, 50.151384)));
        pointsPath.add(new OrderModel(3, new LatLng(26.19273, 50.155384)));
        pointsPath.add(new OrderModel(7, new LatLng(26.192473, 50.125384)));
        pointsPath.add(new OrderModel(8, new LatLng(26.157473, 50.155384)));
        pointsPath.add(new OrderModel(4, new LatLng(26.192662, 50.198229)));

        pointsPath.add(new OrderModel(5, new LatLng(26.195473, 50.155385)));
        pointsPath.add(new OrderModel(1, new LatLng(26.199473, 50.195384)));


    }

    private void initClickListeners() {
        findViewById(R.id.calculate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getCount() <= 1 || adapter.isSorted()) {
                    return;
                }
                if (currentUserLocation == null) {
                    Toast.makeText(MainActivity.this, "Please wait untill we find Your location", Toast.LENGTH_SHORT).show();
                    return;
                }

                getAllCombinations();

            }
        });
        invoiceInput = (EditText) findViewById(R.id.invoice_number);

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = invoiceInput.getText().toString();
                if (inputText.length() == 0) {
                    return;
                }
                String[] splitValues = inputText.split(",|\\.");

                if (splitValues != null) {
                    for (String item : splitValues) {
                        if (item.length() > 0) {
                            adapter.addElement(new OrderModel(Integer.valueOf(item), pointsPath.get(adapter.getCount()).getLocation()));
                        }
                    }
                }
                invoiceInput.setText("");
            }
        });
        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.updateDataset(new ArrayList<OrderModel>());
            }
        });
    }

    private void initList() {
        list = (ListView) findViewById(R.id.main_list);
        adapter = new OrderAdapter(this);
        list.setAdapter(adapter);
    }


    @Override
    public void onNavigateClicked(OrderModel model) {
        double destinationLatitude = model.getLocation().latitude;
        double destinationLongitude = model.getLocation().longitude;

        String url = "http://maps.google.com/maps?f=d&daddr=" + destinationLatitude + "," + destinationLongitude + "&dirflg=d&layer=t";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    public <T> Collection<List<T>> generatePermutationsNoRepetition(Set<T> availableNumbers) {
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


    private void getAllCombinations() {
        startTime = System.currentTimeMillis();
        for (OrderModel order : adapter.getDataset()) {
            Location location = new Location(String.valueOf(order.getInvoiceNumber()));
            location.setLatitude(order.getLocation().latitude);
            location.setLongitude(order.getLocation().longitude);
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

        for (int i = 0; i < currentPoints.size(); i++) {
            System.out.print(currentPoints.get(i) + ",  ");
        }
        System.out.println();
        System.out.println("##########################");
        List<OrderModel> dataset = adapter.getDataset();
        List<OrderModel> newDataset = new ArrayList<>(dataset.size());
        for (int point : currentPoints) {
            newDataset.add(dataset.get(point));
        }
        adapter.updateDataset(newDataset);
        Log.i("time", System.currentTimeMillis() - startTime + " " + currentShortestDistance / 1000);

    }


    @Override
    protected void onPause() {
        if (locationManager != null) {
            //Dummy android stuff
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
        super.onPause();
    }

    private void getDistanceFromPath() {
        // String url = "http://maps.googleapis.com/maps?f=d&daddr=" + destinationLatitude + "," + destinationLongitude + "&dirflg=d&layer=t";
        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/directions/json?");
        url.append("origin=" + currentUserLocation.getLatitude() + "," + currentUserLocation.getLongitude());
        url.append("&destination=" + currentUserLocation.getLatitude() + "," + currentUserLocation.getLongitude());
        url.append("&waypoints=optimize:true");
        for (int i = 0; i < adapter.getDataset().size() - 1; i++) {
            url.append("|" + adapter.getDataset().get(i).getLocation().latitude + "," + adapter.getDataset().get(i).getLocation().longitude);
        }

    }

    private void calculatePathDistance(List<Integer> path) {

        double countDistance = currentUserLocation.distanceTo(locations.get(path.get(0)));
        for (int i = 0; i < path.size() - 1; i++) {
            countDistance += locations.get(path.get(i)).distanceTo(locations.get(path.get(i + 1))); //meters
        }

        if (currentShortestDistance == 0 || currentShortestDistance > countDistance) {
         /*   System.out.println(counter + "Permutation is");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i) + ",  ");
            }
            System.out.println();
            System.out.println("##########################");*/
            currentShortestDistance = countDistance;
            currentPoints = path;
            Log.d("shortest distance", currentShortestDistance / 1000 + " ");
        }

    }


    public void getCurrentUserLocation() {
        Criteria myCriteria = new Criteria();
        myCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
        myCriteria.setPowerRequirement(Criteria.POWER_LOW);
        // let Android select the right location provider for you
        String myProvider = locationManager.getBestProvider(myCriteria, true);

        // finally require updates at -at least- the desired rate
        long minTimeMillis = 5000; // 600,000 milliseconds make 10 minutes

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
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
