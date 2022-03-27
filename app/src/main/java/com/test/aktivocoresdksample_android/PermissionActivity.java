package com.test.aktivocoresdksample_android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.aktivolabs.aktivocore.managers.AktivoManager;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PermissionActivity extends AppCompatActivity {

    private final String TAG = PermissionActivity.class.getSimpleName();
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 40001;
    private final int GOOGLE_FIT_ACTIVITY_PERMISSIONS_REQUEST_CODE = 40002;
    private final int GOOGLE_FIT_SLEEP_PERMISSIONS_REQUEST_CODE = 40003;
    private final int PHYSICAL_ACTIVITY_PERMISSIONS_REQUEST_CODE = 40004;

    private AktivoManager aktivoManager;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        aktivoManager = AktivoManager.getInstance(this);

        Button requestAllPermissionButton = findViewById(R.id.requestAllPermissionButton);
        requestAllPermissionButton.setOnClickListener(v -> {

        });

        Button requestAllGoogleFitPermissionButton = findViewById(R.id.requestAllGoogleFitPermissionButton);
        requestAllGoogleFitPermissionButton.setOnClickListener(v -> {
            checkAllGoogleFitPermission();
        });

        Button requestGoogleFitActivityPermissionButton = findViewById(R.id.requestGoogleFitActivityPermissionButton);
        requestGoogleFitActivityPermissionButton.setOnClickListener(v -> {
            checkGoogleFitActivityPermission();
        });

        Button requestGoogleFitSleepPermissionButton = findViewById(R.id.requestGoogleFitSleepPermissionButton);
        requestGoogleFitSleepPermissionButton.setOnClickListener(v -> {
            checkGoogleFitSleepPermission();
        });

        Button requestPhysicalActivityPermissionButton = findViewById(R.id.requestPhysicalActivityPermissionButton);
        requestPhysicalActivityPermissionButton.setOnClickListener(v -> {
            checkPhysicalActivityPermission();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GOOGLE_FIT_PERMISSIONS_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "All Google fit permissions granted");
                } else {
                    Log.e(TAG, "All Google fit permissions denied");
                }
                break;
            case GOOGLE_FIT_ACTIVITY_PERMISSIONS_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "Google fit Activity permissions granted");
                } else {
                    Log.e(TAG, "Google fit Activity permissions denied");
                }
                break;
            case GOOGLE_FIT_SLEEP_PERMISSIONS_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Log.e(TAG, "Google fit Sleep permissions granted");
                } else {
                    Log.e(TAG, "Google fit Sleep permissions denied");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GOOGLE_FIT_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "All Google fit permissions granted");
                } else {
                    Log.e(TAG, "All Google fit permissions denied");
                }
                break;
            case GOOGLE_FIT_ACTIVITY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Google fit Activity permissions granted");
                } else {
                    Log.e(TAG, "Google fit Activity permissions denied");
                }
                break;
            case GOOGLE_FIT_SLEEP_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Google fit Sleep permissions granted");
                } else {
                    Log.e(TAG, "Google fit Sleep permissions denied");
                }
                break;
            case PHYSICAL_ACTIVITY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "Physical Activity permissions granted");
                } else {
                    Log.e(TAG, "Physical Activity permissions denied");
                }
                break;
        }
    }

    private void requestAllPermissions() {

    }

    private void checkAllGoogleFitPermission() {
        Log.e(TAG, "Checking All Google fit permissions");
        aktivoManager
                .isGoogleFitPermissionGranted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            Log.e(TAG, "All Google fit permissions granted");
                        } else {
                            Log.e(TAG, "All Google fit permissions not granted");
                            requestAllGoogleFitPermission();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void requestAllGoogleFitPermission() {
        Log.e(TAG, "Requesting All Google fit permissions");
        aktivoManager
                .requestGoogleFitPermissions(this, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Permission requested");
                    }
                });
    }

    private void checkGoogleFitActivityPermission() {
        Log.e(TAG, "Checking Google fit Activity permissions");
        aktivoManager
                .isGoogleFitActivityPermissionGranted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            Log.e(TAG, "Google fit Activity permissions granted");
                        } else {
                            Log.e(TAG, "Google fit Activity permissions not granted");
                            requestGoogleFitActivityPermission();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void requestGoogleFitActivityPermission() {
        Log.e(TAG, "Requesting Google fit Activity permissions");
        aktivoManager
                .requestGoogleFitActivityPermission(this, GOOGLE_FIT_ACTIVITY_PERMISSIONS_REQUEST_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Permission requested");
                    }
                });
    }

    private void checkGoogleFitSleepPermission() {
        Log.e(TAG, "Checking Google fit Sleep permissions");
        aktivoManager
                .isGoogleFitSleepPermissionGranted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            Log.e(TAG, "Google fit Sleep permissions granted");
                        } else {
                            Log.e(TAG, "Google fit Sleep permissions not granted");
                            requestGoogleFitSleepPermission();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void requestGoogleFitSleepPermission() {
        Log.e(TAG, "Requesting Google fit Sleep permissions");
        aktivoManager
                .requestGoogleFitSleepPermission(this, GOOGLE_FIT_SLEEP_PERMISSIONS_REQUEST_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Permission requested");
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkPhysicalActivityPermission() {
        Log.e(TAG, "Checking Physical Activity permissions");
        aktivoManager
                .isActivityRecognitionPermissionGranted(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Boolean aBoolean) {
                        if (aBoolean) {
                            Log.e(TAG, "Physical Activity permissions granted");
                        } else {
                            Log.e(TAG, "Physical Activity permissions not granted");
                            requestPhysicalActivityPermission();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestPhysicalActivityPermission() {
        Log.e(TAG, "Requesting Physical Activity permissions");
        aktivoManager
                .requestActivityRecognitionPermission(this, PHYSICAL_ACTIVITY_PERMISSIONS_REQUEST_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Permission requested");
                    }
                });
    }
}
