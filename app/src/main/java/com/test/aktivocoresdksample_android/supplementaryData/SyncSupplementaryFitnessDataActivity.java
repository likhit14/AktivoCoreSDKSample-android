package com.test.aktivocoresdksample_android.supplementaryData;

import android.Manifest;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.aktivolabs.aktivocore.data.models.googlefit.FitSupplementaryDataPointEnum;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.test.aktivocoresdksample_android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class SyncSupplementaryFitnessDataActivity extends AppCompatActivity {

    private final String TAG = SyncSupplementaryFitnessDataActivity.class.getSimpleName();
    private final Calendar startDateCalendar = Calendar.getInstance();
    private final Calendar endDateCalendar = Calendar.getInstance();
    private TextView startDateTV;
    private TextView endDateTV;
    private String startDate = "", endDate = "";
    private FitnessOptions fitnessOptions;
    private final int GOOGLE_FIT_SUPPLEMENTARY_DATA_PERMISSIONS_REQUEST_CODE = 40006;
    private AktivoManager aktivoManager;
    private CompositeDisposable compositeDisposable;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    DatePickerDialog.OnDateSetListener startDateListener = (view, year, monthOfYear, dayOfMonth) -> {
        startDateCalendar.set(Calendar.YEAR, year);
        startDateCalendar.set(Calendar.MONTH, monthOfYear);
        startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateStartDate();
    };

    DatePickerDialog.OnDateSetListener endDateListener = (view, year, monthOfYear, dayOfMonth) -> {
        endDateCalendar.set(Calendar.YEAR, year);
        endDateCalendar.set(Calendar.MONTH, monthOfYear);
        endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateEndDate();
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplementary_fitness_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aktivoManager = AktivoManager.getInstance(this);
        compositeDisposable = new CompositeDisposable();

        fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_HEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_WEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_NUTRITION_SUMMARY, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_HYDRATION, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_BODY_FAT_PERCENTAGE_SUMMARY, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                        .build();

        Button googleFitPermissionButton = findViewById(R.id.requestGoogleFitPermissionButton);
        googleFitPermissionButton.setOnClickListener(v -> requestGoogleFitPermission(fitnessOptions));

        Button physicalActivityPermissionButton = findViewById(R.id.requestPhysicalActivityPermissionButton);
        physicalActivityPermissionButton.setOnClickListener(v -> requestActivityRecognitionPermission());

        Button accessLocationPermissionButton = findViewById(R.id.requestAccessLocationPermissionButton);
        accessLocationPermissionButton.setOnClickListener(v -> requestAccessFineLocationPermission());

        startDateTV = findViewById(R.id.startDateTV);
        startDateTV.setOnClickListener(v -> new DatePickerDialog(
                SyncSupplementaryFitnessDataActivity.this,
                startDateListener,
                startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        endDateTV = findViewById(R.id.endDateTV);
        endDateTV.setOnClickListener(v -> new DatePickerDialog(
                SyncSupplementaryFitnessDataActivity.this,
                endDateListener,
                endDateCalendar.get(Calendar.YEAR),
                endDateCalendar.get(Calendar.MONTH),
                endDateCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        Button syncSupplementaryFitnessDataButton = findViewById(R.id.syncSupplementaryFitnessDataButton);
        syncSupplementaryFitnessDataButton.setOnClickListener(v -> syncSupplementaryFitnessData());

    }

    private void updateStartDate() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        startDate = sdf.format(startDateCalendar.getTime());
        startDateTV.setText("Start Date: " + sdf.format(startDateCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        endDate = sdf.format(endDateCalendar.getTime());
        endDateTV.setText("End Date: " + sdf.format(endDateCalendar.getTime()));
    }

    private void requestGoogleFitPermission(FitnessOptions fitnessOptions) {
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_SUPPLEMENTARY_DATA_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestActivityRecognitionPermission() {
        requestPermissionLauncher.launch(
                Manifest.permission.ACTIVITY_RECOGNITION);
    }

    private void requestAccessFineLocationPermission() {
        requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void syncSupplementaryFitnessData() {
        ArrayList<FitSupplementaryDataPointEnum> enumList = new ArrayList<>();
        enumList.add(FitSupplementaryDataPointEnum.TYPE_HEIGHT);
        enumList.add(FitSupplementaryDataPointEnum.TYPE_WEIGHT);
        enumList.add(FitSupplementaryDataPointEnum.AGGREGATE_CALORIES_EXPENDED);
        enumList.add(FitSupplementaryDataPointEnum.TYPE_BODY_FAT_PERCENTAGE);
        enumList.add(FitSupplementaryDataPointEnum.TYPE_NUTRITION);
        enumList.add(FitSupplementaryDataPointEnum.TYPE_HYDRATION);
        enumList.add(FitSupplementaryDataPointEnum.TYPE_DISTANCE_DELTA);
        compositeDisposable.add(
                aktivoManager
                        .syncSupplementaryFitnessData(
                                enumList,
                                startDate,
                                endDate
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.e(TAG, "Supplementary Data synced");
                                Toast.makeText(SyncSupplementaryFitnessDataActivity.this, "Supplementary Data Synced", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                e.printStackTrace();
                                Log.e(TAG, "Supplementary Data sync error");
                                Toast.makeText(SyncSupplementaryFitnessDataActivity.this, "Supplementary Data Sync error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }
}
