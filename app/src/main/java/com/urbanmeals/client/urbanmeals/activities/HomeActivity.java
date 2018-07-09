package com.urbanmeals.client.urbanmeals.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.urbanmeals.client.urbanmeals.fragments.HomeSuggestionsFragment;
import com.urbanmeals.client.urbanmeals.fragments.HomeNearbyFragment;
import com.urbanmeals.client.urbanmeals.fragments.HomeProfileFragment;
import com.urbanmeals.client.urbanmeals.R;
import com.urbanmeals.client.urbanmeals.interfaces.LocationReadyListener;


public class HomeActivity extends AppCompatActivity {

    private static final int LOCATION_SERVICE_ON = 1134;
    private static final int LOCATION_REQUEST = 879;

    private LocationReadyListener listener;
    private HomeNearbyFragment nearbyFragment;
    private HomeProfileFragment profileFragment;
    private HomeSuggestionsFragment suggestionsFragment;
    private SearchView searchView;
    private ImageView umLogo;

    private int bugClickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bugClickCount = 2;

        BottomNavigationView navigationView = findViewById(R.id.NavigationBar);
        //searchButton = findViewById(R.id.Home_SearchButton);
        searchView = findViewById(R.id.Home_SearchBar);
        umLogo = findViewById(R.id.Home_Logo);


        //Setting the navigation bar as the center.
        navigationView.setSelectedItemId(R.id.Home_NavigationHome);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        profileFragment = new HomeProfileFragment();
        nearbyFragment = new HomeNearbyFragment();
        suggestionsFragment = new HomeSuggestionsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.Home_Fragment, nearbyFragment).commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.Home_NavigationSearch:{
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.Home_Fragment, suggestionsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    }
                    case R.id.Home_NavigationHome:{
                        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
                        transactions.replace(R.id.Home_Fragment, nearbyFragment);
                        transactions.addToBackStack(null);
                        transactions.commit();
                        break;
                    }
                    case R.id.Home_NavigationProfile:{
                        FragmentTransaction transactions = getSupportFragmentManager().beginTransaction();
                        transactions.replace(R.id.Home_Fragment, profileFragment);
                        transactions.addToBackStack(null);
                        transactions.commit();
                        break;
                    }
                }
                return true;
            }
        });

        umLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bugClickCount == 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.urbanmeals.client.urbanmeals")));
                    bugClickCount = 2;
                } else {
                    Toast.makeText(HomeActivity.this, String.format("%d clicks away", bugClickCount), Toast.LENGTH_SHORT).show();
                    bugClickCount--;
                }
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getBoolean("firstTime", false)) {
            showAlert();
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.WarningDialogueTheme);
        builder.setTitle("Thank You")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setMessage("Thanks for installing Urban Meals.\nAs this is our initial release, you may experience some stability issues. " +
                        "If you find any bugs or problem, feel free to report it through our Google Play Page by triple tapping our logo on top left corner.");
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Got the Permission For the location
                    SetUpLocationListener(listener);
                } else {
                    Toast.makeText(this, "App cannot work without Location. Please restart the app.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //After the Google GPS turning on dialogue is Successful
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(LOCATION_SERVICE_ON  == requestCode){
            SetUpLocationListener(listener);
        }
    }


    public void SetUpLocationListener(final LocationReadyListener listener){
        this.listener = listener;
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(250);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
                    locationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            listener.onResult(locationResult.getLocations().get(0));
                        }
                    }, null);
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException){
                    ResolvableApiException resolvable  = (ResolvableApiException)e;
                    try {
                        //This Prompt a Google location access Prompt
                        resolvable.startResolutionForResult(HomeActivity.this, LOCATION_SERVICE_ON);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
        } else {
            //Closes the app when back button is pressed on HomeScreen.
            //super.onBackPressed();
            finishAffinity();
        }
    }
}
