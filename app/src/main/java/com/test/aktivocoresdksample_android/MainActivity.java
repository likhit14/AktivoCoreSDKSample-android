package com.test.aktivocoresdksample_android;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import com.aktivolabs.aktivocore.data.local.AktivoCoreDatabase;
import com.aktivolabs.aktivocore.data.models.Stats;
import com.aktivolabs.aktivocore.data.models.User;
import com.aktivolabs.aktivocore.data.models.badges.AktivoBadgeType;
import com.aktivolabs.aktivocore.data.models.badges.BadgeSummary;
import com.aktivolabs.aktivocore.data.models.badges.DailyBadge;
import com.aktivolabs.aktivocore.data.models.badges.HistoryBadgeType;
import com.aktivolabs.aktivocore.data.models.challenge.Challenge;
import com.aktivolabs.aktivocore.data.models.challenge.Enroll;
import com.aktivolabs.aktivocore.data.models.feed.Feed;
import com.aktivolabs.aktivocore.data.models.feed.FeedNotificationMetadata;
import com.aktivolabs.aktivocore.data.models.feed.enums.FeedStatusEnum;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTracker;
import com.aktivolabs.aktivocore.data.models.fitnesstrackers.FitnessTrackerAuthUrlInfo;
import com.aktivolabs.aktivocore.data.models.heartrate.HeartRateStats;
import com.aktivolabs.aktivocore.data.models.queries.BadgeByDateQuery;
import com.aktivolabs.aktivocore.data.models.queries.BadgeHistoryQuery;
import com.aktivolabs.aktivocore.data.models.queries.BadgeSummaryQuery;
import com.aktivolabs.aktivocore.data.models.queries.ChallengeEnrollQuery;
import com.aktivolabs.aktivocore.data.models.queries.ChallengeListQuery;
import com.aktivolabs.aktivocore.data.models.queries.ChallengeQuery;
import com.aktivolabs.aktivocore.data.models.queries.HeartRateQuery;
import com.aktivolabs.aktivocore.data.models.queries.MarkNotificationReadQuery;
import com.aktivolabs.aktivocore.data.models.queries.NotificationsListQuery;
import com.aktivolabs.aktivocore.data.models.queries.Query;
import com.aktivolabs.aktivocore.data.models.queries.RiseGameDailyPointsQuery;
import com.aktivolabs.aktivocore.data.models.queries.RiseGameRewardsQuery;
import com.aktivolabs.aktivocore.data.models.queries.RiseGameWeeklyPointsQuery;
import com.aktivolabs.aktivocore.data.models.queries.ScoreLatestQuery;
import com.aktivolabs.aktivocore.data.models.queries.ScoreQuery;
import com.aktivolabs.aktivocore.data.models.queries.SleepQuery;
import com.aktivolabs.aktivocore.data.models.queries.StepsQuery;
import com.aktivolabs.aktivocore.data.models.queries.UnreadNotificationCountQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.DeleteFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.FeedDetailsQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.FeedLikeDisLikeQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.FeedPostCommentQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.HideFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.PostImageFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.PostLinkFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.PostTextFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.PostVideoFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.ReportUserQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.SocialFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.UnFollowUserQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.UpdateFeedLinkQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.UpdateImageFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.UpdateTextFeedQuery;
import com.aktivolabs.aktivocore.data.models.queries.socialfeeed.UpdateVideoFeedQuery;
import com.aktivolabs.aktivocore.data.models.reminder.Reminder;
import com.aktivolabs.aktivocore.data.models.reminder.ReminderAt;
import com.aktivolabs.aktivocore.data.models.reminder.ReminderFrequency;
import com.aktivolabs.aktivocore.data.models.risegame.RiseGame;
import com.aktivolabs.aktivocore.data.models.risegame.dailypoints.RGPDailyPoints;
import com.aktivolabs.aktivocore.data.models.risegame.rewards.RiseGameReward;
import com.aktivolabs.aktivocore.data.models.risegame.weeklypoints.RGPWeeklyPoints;
import com.aktivolabs.aktivocore.data.models.schemas.feed.response.FeedEmbeddedResponse;
import com.aktivolabs.aktivocore.data.models.score.ScoreElement;
import com.aktivolabs.aktivocore.data.models.score.ScoreStats;
import com.aktivolabs.aktivocore.data.models.sleep.SleepStats;
import com.aktivolabs.aktivocore.data.models.steps.StepStats;
import com.aktivolabs.aktivocore.data.models.userprofile.UserProfile;
import com.aktivolabs.aktivocore.data.models.userprofile.height.Height;
import com.aktivolabs.aktivocore.data.models.userprofile.height.HeightCm;
import com.aktivolabs.aktivocore.data.models.userprofile.height.HeightFtIn;
import com.aktivolabs.aktivocore.data.models.userprofile.weight.Weight;
import com.aktivolabs.aktivocore.data.models.userprofile.weight.WeightKg;
import com.aktivolabs.aktivocore.data.models.userprofile.weight.WeightLbs;
import com.aktivolabs.aktivocore.data.repositories.LocalRepository;
import com.aktivolabs.aktivocore.factories.AktivoReminder;
import com.aktivolabs.aktivocore.managers.AktivoManager;
import com.aktivolabs.aktivoelk.data.models.ElkLog;
import com.aktivolabs.aktivoelk.managers.ElkManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.aktivocoresdksample_android.supplementaryData.SyncSupplementaryFitnessDataActivity;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;

public class MainActivity extends AppCompatActivity {

    public static final int FEED_API_GET_COMMENTS = 1;
    public static final int FEED_API_POST_COMMENT = 2;
    public static final int FEED_API_LIKE_UNLIKE = 3;
    public static final int FEED_API_HIDE = 4;
    public static final int FEED_API_UNFOLLOW_USER = 5;
    public static final int FEED_API_REPORT_USER = 6;
    public static final int FEED_API_DELETE = 7;

    public static final int UPLOAD_IMAGE_FILE = 1;
    public static final int UPLOAD_VIDEO_FILE = 2;
    public static final int UPDATE_FEED_IMAGE_FILE = 3;
    public static final int UPDATE_FEED_VIDEO_FILE = 4;

    public static final int ENROL_TO_CHALLENGE = 1;
    public static final int GET_CHALLENGE = 2;

    public static final String START_DATE = "2021-01-01";
    private final String TAG = MainActivity.class.getSimpleName();
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 40001;
    private final int ACTIVITY_RECOGNITION_REQUEST_CODE = 40002;
    private static final int REQUEST_PERMISSION_READ_STORAGE = 1002;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private String todaysDate = format.format(new Date());

    protected final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setLenient().create();
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private FitnessOptions fitnessOptions;
    private AktivoManager aktivoManager;
    private AktivoCoreDatabase db;
    private CompositeDisposable compositeDisposable;
    private LocalRepository localRepository;
    private String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        aktivoManager = AktivoManager.getInstance(this);
//        aktivoManager.setLog(new ErrorLogEntity(
//                getCurrentDate_yyyy_MM_dd_T_HH_mm_ss_sss_Z(),
//                "", "", "", "", "", ""
//        ));
        localRepository = new LocalRepository(this);
        compositeDisposable = new CompositeDisposable();

        fitnessOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                        .build();

        Button permissionButton = findViewById(R.id.requestPermissionButton);

        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PermissionActivity.class));
            }
        });

        Button authenticateButton = findViewById(R.id.authenticateUserButton);
        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });

        Button bypassAuthenticateUserButton = findViewById(R.id.bypassAuthenticateUserButton);
        bypassAuthenticateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byPassAuthenticateUserUsingToken();
            }
        });

        Button syncFitnessDataButton = findViewById(R.id.syncFitnessData);
        syncFitnessDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncFitnessData();
            }
        });

        Button syncSupplementaryFitnessDataButton = findViewById(R.id.syncSupplementaryFitnessData);
        syncSupplementaryFitnessDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //syncSupplementaryFitnessData();
                startActivity(new Intent(MainActivity.this, SyncSupplementaryFitnessDataActivity.class));
            }
        });

        Button clearDatabaseButton = findViewById(R.id.clearDatabaseButton);
        clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //aktivoManager.new ClearDatabaseTask().execute();
            }
        });

        Button getScoreStatsButton = findViewById(R.id.getScoreStatsButton);
        getScoreStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScoreStats();
            }
        });

        Button getSleepStatsButton = findViewById(R.id.getSleepStatsButton);
        getSleepStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSleepStats();
            }
        });

        Button getStepsStatsButton = findViewById(R.id.getStepsStatsButton);
        getStepsStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStepsStats();
            }
        });

        Button getHeartRateStatsButton = findViewById(R.id.getHeartRateStatsButton);
        getHeartRateStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHeartRateStats();
            }
        });

        Button getScoreStatsLatestButton = findViewById(R.id.getScoreStatsLatestButton);
        getScoreStatsLatestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScoreStatsLatest();
            }
        });

        Button getUserProfileButton = findViewById(R.id.getUserProfileButton);
        getUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserProfile();
            }
        });

        Button updateUserProfileButton = findViewById(R.id.updateUserProfileButton);
        updateUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });

        Button batchQueryButton = findViewById(R.id.getBatchQueryStatsButton);
        batchQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBatchStats();
            }
        });
        Button badgeByDateBtn = findViewById(R.id.badgeByDateBtn);
        badgeByDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBadgesByDate();
            }
        });
        Button badgeSummaryBtn = findViewById(R.id.badgeSummaryBtn);
        badgeSummaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBadgesSummary();
            }
        });
        Button badgeHistoryBtn = findViewById(R.id.badgeHistoryBtn);
        badgeHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBadgesHistory();
            }
        });
        Button aktivoBadgeTypesBtn = findViewById(R.id.aktivoBadgeTypesBtn);
        aktivoBadgeTypesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAktivoBadgeTypes();
            }
        });
        Button getFitnessTrackersBtn = findViewById(R.id.getFitnessTrackersBtn);
        getFitnessTrackersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFitnessTrackers();
            }
        });
        Button authUrlFitbitBtn = findViewById(R.id.authUrlFitbitBtn);
        authUrlFitbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFitnessPlatformAuthUrlForFitbit();
            }
        });
        Button authUrlGarminBtn = findViewById(R.id.authUrlGarminBtn);
        authUrlGarminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFitnessPlatformAuthUrlForGarmin();
            }
        });
        Button disconnectFitbitBtn = findViewById(R.id.disconnectFitbitBtn);
        disconnectFitbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectFitbit();
            }
        });
        Button disconnectGarminBtn = findViewById(R.id.disconnectGarminBtn);
        disconnectGarminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectGarmin();
            }
        });

        //Invalidate user
        Button invalidateUserButton = findViewById(R.id.invalidateUserButton);
        invalidateUserButton.setOnClickListener(v -> invalidateUser());

        Button getRiseGameBtn = findViewById(R.id.getRiseGameBtn);
        getRiseGameBtn.setOnClickListener(v -> getRiseGame());

        Button getRiseGameRewardsBtn = findViewById(R.id.getRiseGameRewardsBtn);
        getRiseGameRewardsBtn.setOnClickListener(v -> getRiseGameRewards());

        Button getDailyRiseGamePointsBtn = findViewById(R.id.getDailyRiseGamePointsBtn);
        getDailyRiseGamePointsBtn.setOnClickListener(v -> getDailyRiseGamePoints());

        Button getWeeklyRiseGamePointsBtn = findViewById(R.id.getWeeklyRiseGamePointsBtn);
        getWeeklyRiseGamePointsBtn.setOnClickListener(v -> getWeeklyRiseGamePoints());

        Button getOngoingChallengesBtn = findViewById(R.id.getOngoingChallengesBtn);
        getOngoingChallengesBtn.setOnClickListener(v -> getOngoingChallenges());

        Button getOverChallengesBtn = findViewById(R.id.getOverChallengesBtn);
        getOverChallengesBtn.setOnClickListener(v -> getOverChallenges());

        Button getChallengeBtn = findViewById(R.id.getChallengeBtn);
        getChallengeBtn.setOnClickListener(v -> enterChallengeId(GET_CHALLENGE));

        Button enrollToChallengeBtn = findViewById(R.id.enrollToChallengeBtn);
        enrollToChallengeBtn.setOnClickListener(v -> enterChallengeId(ENROL_TO_CHALLENGE));

        // Feed
        Button getFeedsBtn = findViewById(R.id.getFeedsBtn);
        getFeedsBtn.setOnClickListener(v -> getFeeds());

        Button getCommentsLikeBtn = findViewById(R.id.getCommentsLikeBtn);
        getCommentsLikeBtn.setOnClickListener(v -> enterFeedId(FEED_API_GET_COMMENTS));

        Button postCommentBtn = findViewById(R.id.postCommentBtn);
        postCommentBtn.setOnClickListener(v -> enterFeedId(FEED_API_POST_COMMENT));

        Button likeUnlikeBtn = findViewById(R.id.likeUnlikeBtn);
        likeUnlikeBtn.setOnClickListener(v -> enterFeedId(FEED_API_LIKE_UNLIKE));

        Button hideFeedBtn = findViewById(R.id.hideFeedBtn);
        hideFeedBtn.setOnClickListener(v -> enterFeedId(FEED_API_HIDE));

        Button unFollowUserBtn = findViewById(R.id.unFollowUserBtn);
        unFollowUserBtn.setOnClickListener(v -> enterFeedId(FEED_API_UNFOLLOW_USER));

        Button reportUserBtn = findViewById(R.id.reportUserBtn);
        reportUserBtn.setOnClickListener(v -> enterFeedId(FEED_API_REPORT_USER));

        Button deleteFeedBtn = findViewById(R.id.deleteFeedBtn);
        deleteFeedBtn.setOnClickListener(v -> enterFeedId(FEED_API_DELETE));

        Button getNotificationBtn = findViewById(R.id.getNotificationBtn);
        getNotificationBtn.setOnClickListener(v -> getFeedNotifications());

        Button getUnreadNotificationCountBtn = findViewById(R.id.getUnreadNotificationCountBtn);
        getUnreadNotificationCountBtn.setOnClickListener(v -> getUnreadNotificationCount());

        Button markNotificationAsReadBtn = findViewById(R.id.markNotificationAsReadBtn);
        markNotificationAsReadBtn.setOnClickListener(v -> enterNotificationId());

        // create Feed
        Button postImageFeedBtn = findViewById(R.id.postImageFeedBtn);
        postImageFeedBtn.setOnClickListener(v -> enterFileName(UPLOAD_IMAGE_FILE));

        Button postVideoFeedBtn = findViewById(R.id.postVideoFeedBtn);
        postVideoFeedBtn.setOnClickListener(v -> enterFileName(UPLOAD_VIDEO_FILE));

        Button postTextFeedBtn = findViewById(R.id.postTextFeedBtn);
        postTextFeedBtn.setOnClickListener(v -> postTextFeed());

        Button postLinkFeedBtn = findViewById(R.id.postLinkFeedBtn);
        postLinkFeedBtn.setOnClickListener(v -> postLinkFeed());

        //Update Feed
        Button updateImageFeedBtn = findViewById(R.id.updateImageFeedBtn);
        updateImageFeedBtn.setOnClickListener(v -> enterFileName(UPDATE_FEED_IMAGE_FILE));

        Button updateVideoFeedBtn = findViewById(R.id.updateVideoFeedBtn);
        updateVideoFeedBtn.setOnClickListener(v -> enterFileName(UPDATE_FEED_VIDEO_FILE));

        Button updateTextFeedBtn = findViewById(R.id.updateTextFeedBtn);
        updateTextFeedBtn.setOnClickListener(v -> updateTextFeed());

        Button updateLinkFeedBtn = findViewById(R.id.updateLinkFeedBtn);
        updateLinkFeedBtn.setOnClickListener(v -> updateLinkFeed());

        Button notificationTitleBtn = findViewById(R.id.notificationTitleBtn);
        notificationTitleBtn.setOnClickListener(v -> setNotificationTitle());

        Button notificationDescBtn = findViewById(R.id.notificationDescBtn);
        notificationDescBtn.setOnClickListener(v -> setNotificationDescription());

        Button notificationIconBtn = findViewById(R.id.notificationIconBtn);
        notificationIconBtn.setOnClickListener(v -> setNotificationIcon());

        Button notificationChannelNameBtn = findViewById(R.id.notificationChannelNameBtn);
        notificationChannelNameBtn.setOnClickListener(v -> updateNotificationChannelName());

        Button notificationChannelIdBtn = findViewById(R.id.notificationChannelIdBtn);
        notificationChannelIdBtn.setOnClickListener(v -> updateNotificationChannelId());

        Button callPersonalizeBtn = findViewById(R.id.callPersonalizeBtn);
        callPersonalizeBtn.setOnClickListener(v -> callPersonalizeAPI());

        // Permission Check
        checkStoragePermission();
    }

    private void updateNotificationChannelId() {
        aktivoManager.setSleepNotificationChannelId("Channel Id goes here");
    }

    private void updateNotificationChannelName() {
        aktivoManager.setSleepNotificationChannelName("Channel name goes here");
    }

    private void setNotificationIcon() {
        aktivoManager.setSleepNotificationIcon(R.mipmap.ic_launcher);
    }

    private void setNotificationDescription() {
        aktivoManager.setSleepNotificationDescription("Sleep notification desc goes here");
    }

    private void setNotificationTitle() {
        aktivoManager.setSleepNotificationTitle("Sleep notification title goes here");
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            if (isPermissionNotGranted(permissions)) {
                requestPermissions(permissions, REQUEST_PERMISSION_READ_STORAGE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isPermissionNotGranted(String[] permissions) {
        boolean flag = false;

        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                flag = true;
                break;
            }
        }

        return flag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_STORAGE) {
            Log.e(TAG, "read & write permission granted");
        }
        if (requestCode == ACTIVITY_RECOGNITION_REQUEST_CODE) {
            Log.e(TAG, "activity recognition permission granted");
        }
        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            Log.e(TAG, "google fit permission permission granted");
        }
    }

    // update feed
    private void updateLinkFeed() {
        UpdateFeedLinkQuery updateFeedLinkQuery = new UpdateFeedLinkQuery(
                "socialId",
                "updated text",
                "updateLinkImageUrl",
                "updateLinkTitle",
                "updateLinkHost",
                "updateLinkDescription",
                "updateLink"
        );


        aktivoManager.updateFeedLink(updateFeedLinkQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "updated link feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in update link feed", e);
                    }
                });
    }

    private void updateTextFeed() {
        UpdateTextFeedQuery updateTextFeedQuery = new UpdateTextFeedQuery(
                "socialId",
                "updated text"
        );

        aktivoManager.updateFeedText(updateTextFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "updated text feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in update text feed done", e);
                    }
                });
    }

    private void updateVideoFeed(String fileName, String feedId) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/video.mp4");
        File file = new File(fileName);

        UpdateVideoFeedQuery updateVideoFeedQuery = new UpdateVideoFeedQuery(
                feedId,
                "updated video text",
                file
        );

        aktivoManager.updateVideoFeed(updateVideoFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "updated image feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in update image feed done", e);
                    }
                });
    }

    private void updateImageFeed(String fileName, String feedId) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/e-tax-123.png");
        File file = new File(fileName);

        UpdateImageFeedQuery updateImageFeedQuery = new UpdateImageFeedQuery(
                feedId,
                "updated image text",
                file
        );
        aktivoManager.updateImageFeed(updateImageFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "updated image feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in updated image feed", e);
                    }
                });
    }

    // create feed
    private void postLinkFeed() {
        PostLinkFeedQuery postLinkFeedQuery = new PostLinkFeedQuery(
                localRepository.getUserId(),
                "write something",
                "https://cdn2.iconfinder.com/data/icons/pittogrammi/142/46-512.png",
                "linkTitle",
                "Host for the link",
                "linkDescription",
                "https://www.outlookindia.com/website/story/india-news-covid-19-these-states-require-a-negative-rt-pcr-test-for-entry/379281"
        );
        aktivoManager.postLinkFeed(postLinkFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "posting link done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in posting link done");
                    }
                });
    }

    private void postTextFeed() {
        PostTextFeedQuery postTextFeedQuery = new PostTextFeedQuery(localRepository.getUserId(),
                "write something");

        aktivoManager.postTextFeed(postTextFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "post text done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in post text", e);
                    }
                });
    }

    private void postVideoFeed(String fileName) {
        File file = new File(fileName);
        PostVideoFeedQuery postVideoFeedQuery = new PostVideoFeedQuery(
                localRepository.getUserId(),
                "sample video",
                file
        );

        aktivoManager.postVideoFeed(postVideoFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "posted video feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in posting video feed", e);
                    }
                });
    }

    private void enterFileName(int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog dialog = builder.create();
        final View dialogView = inflater.inflate(R.layout.layout_username_token, null);
        final TextView userDetailsTV = dialogView.findViewById(R.id.userDetailsTV);
        final Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        final EditText userNameET = dialogView.findViewById(R.id.userNameET);
        final EditText tokenET = dialogView.findViewById(R.id.accessTokenET);
        final CheckBox selectorCB = dialogView.findViewById(R.id.selectorCB);

        selectorCB.setVisibility(View.GONE);
        userNameET.setHint("File name");
        if (type == UPDATE_FEED_IMAGE_FILE || type == UPDATE_FEED_VIDEO_FILE) {
            tokenET.setVisibility(View.VISIBLE);
            tokenET.setHint(R.string.feed_id);
        } else {
            tokenET.setVisibility(View.GONE);
        }
        userDetailsTV.setText(R.string.upload_image_video);

        submitBtn.setOnClickListener(v -> {
            String fileName = userNameET.getText().toString().trim();
            String feedId = tokenET.getText().toString().trim();
            if (fileName.isEmpty())
                return;
            switch (type) {
                case UPLOAD_IMAGE_FILE:
                    postImageFeed(fileName);
                    break;
                case UPLOAD_VIDEO_FILE:
                    postVideoFeed(fileName);
                    break;
                case UPDATE_FEED_IMAGE_FILE:
                    if (feedId.isEmpty())
                        return;
                    updateImageFeed(fileName, feedId);
                    break;
                case UPDATE_FEED_VIDEO_FILE:
                    if (feedId.isEmpty())
                        return;
                    updateVideoFeed(fileName, feedId);
                    break;
            }
            dialog.dismiss();
        });

        dialog.setView(dialogView);
        dialog.show();
    }

    private void postImageFeed(String fileName) {
        //File file = new File(Environment.getExternalStorageDirectory(), "/Aktivo/20210115_144203496.png");
        File file = new File(fileName);
        PostImageFeedQuery postImageFeedQuery = new PostImageFeedQuery(
                localRepository.getUserId(),
                "sample text",
                file
        );

        aktivoManager.postImageFeed(postImageFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "post image feed done");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in posting image feed", e);
                    }
                });
    }

    private void markNotificationAsRead(String notificationId) {
        //String notificationID = "60b50e68b034d80012ab9887";
        MarkNotificationReadQuery markNotificationReadQuery =
                new MarkNotificationReadQuery(
                        localRepository.getUserId(),
                        notificationId,
                        true
                );
        aktivoManager.markNotificationAsRead(markNotificationReadQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "marked notification as read successfully");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e(TAG, "error in marked notification as read", e);
                    }
                });
    }

    private void getUnreadNotificationCount() {
        UnreadNotificationCountQuery unreadNotificationCountQuery =
                new UnreadNotificationCountQuery(
                        localRepository.getUserId()
                );
        aktivoManager.getUnreadNotificationCount(unreadNotificationCountQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer unreadCount) {
                        Log.e(TAG, "getUnreadNotificationCount " + unreadCount);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getUnreadNotificationCount, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getFeedNotifications() {
        int offset = 0;
        int pageSize = 10;
        boolean isRead = false;
        NotificationsListQuery notificationsListQuery =
                new NotificationsListQuery(
                        localRepository.getUserId(),
                        offset,
                        pageSize,
                        isRead
                );
        aktivoManager.getFeedNotifications(notificationsListQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<FeedNotificationMetadata>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<FeedNotificationMetadata> feedNotificationList) {
                        Log.e(TAG, "getFeedNotifications " + feedNotificationList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFeedNotifications, error: " + e.getMessage(), e);
                    }
                });
    }

    private void deleteFeed(String feedId) {
        //String feedId = "606a9a8b023dc6001212639e";
        aktivoManager.deleteFeed(
                new DeleteFeedQuery(feedId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "deleteFeed completed successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in deleteFeed", e);
                    }
                });
    }

    private void reportUser(String feedAuthorMemberId) {
        //String feedAuthorMemberId = "5f9c00f0e278dc001204260f";
        String reportMessage = "Inappropriate comment";
        aktivoManager.reportUser(
                new ReportUserQuery(localRepository.getUserId(),
                        feedAuthorMemberId,
                        ReportUserQuery.ReportCategory.Spam,
                        reportMessage
                ))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "reportUser completed successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in reportUser", e);
                    }
                });
    }

    private void unFollowUser(String feedAuthorMemberId) {
        //String feedAuthorMemberId = "5f9c00f0e278dc001204260f";
        aktivoManager.unFollowUser(
                new UnFollowUserQuery(localRepository.getUserId(), feedAuthorMemberId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "unFollowUser completed successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in unFollowUser", e);
                    }
                });
    }

    private void hideFeed(String feedId) {
        //String feedId = "6064c6df023dc60012125755";
        aktivoManager.hideFeed(
                new HideFeedQuery(localRepository.getUserId(), feedId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Hide feed successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in Hide feed", e);
                    }
                });
    }

    private void likeUnlikeFeed(String feedId, FeedStatusEnum feedStatusEnum) {
        //String feedId = "603f213a80bd9900142c2ac3";
        aktivoManager.likeUnlikeAFeed(
                new FeedLikeDisLikeQuery(localRepository.getUserId(), feedId, feedStatusEnum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "Like/Unlike added successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in Like/Unlike a feed", e);
                    }
                });
    }

    private void postComment(String feedId) {
        aktivoManager.postCommentToFeed(
                new FeedPostCommentQuery(localRepository.getUserId(), feedId, "test 1"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "comment added successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error in posting a comment", e);
                    }
                });
    }

    private void enterNotificationId() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog dialog = builder.create();
        final View dialogView = inflater.inflate(R.layout.layout_username_token, null);
        final TextView userDetailsTV = dialogView.findViewById(R.id.userDetailsTV);
        final Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        final EditText userNameET = dialogView.findViewById(R.id.userNameET);
        final EditText tokenET = dialogView.findViewById(R.id.accessTokenET);
        final CheckBox selectorCB = dialogView.findViewById(R.id.selectorCB);

        selectorCB.setVisibility(View.GONE);
        userNameET.setHint(R.string.notification_id);
        tokenET.setVisibility(View.GONE);
        userDetailsTV.setText(R.string.notification_details);

        submitBtn.setOnClickListener(v -> {
            String notificationId = userNameET.getText().toString().trim();
            markNotificationAsRead(notificationId);
            dialog.dismiss();
        });

        dialog.setView(dialogView);
        dialog.show();
    }

    private void enterFeedId(final int feedApiCode) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog dialog = builder.create();
        final View dialogView = inflater.inflate(R.layout.layout_username_token, null);
        final TextView userDetailsTV = dialogView.findViewById(R.id.userDetailsTV);
        final Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        final EditText userNameET = dialogView.findViewById(R.id.userNameET);
        final EditText tokenET = dialogView.findViewById(R.id.accessTokenET);
        final CheckBox selectorCB = dialogView.findViewById(R.id.selectorCB);
        if (feedApiCode == FEED_API_LIKE_UNLIKE) {
            selectorCB.setVisibility(View.VISIBLE);
        } else {
            selectorCB.setVisibility(View.GONE);
        }

        if (feedApiCode == FEED_API_UNFOLLOW_USER || feedApiCode == FEED_API_REPORT_USER) {
            userNameET.setHint(R.string.feed_author_id);
        } else {
            userNameET.setHint(R.string.feed_id);
        }

        userDetailsTV.setText(R.string.feed_details);
        tokenET.setVisibility(View.GONE);

        submitBtn.setOnClickListener(v -> {
            String feedId = userNameET.getText().toString().trim();
            switch (feedApiCode) {
                case FEED_API_GET_COMMENTS:
                    getCommentsLikeList(feedId);
                    break;

                case FEED_API_POST_COMMENT:
                    postComment(feedId);
                    break;

                case FEED_API_LIKE_UNLIKE:
                    FeedStatusEnum statusEnum = FeedStatusEnum.Like;
                    if (!selectorCB.isChecked())
                        statusEnum = FeedStatusEnum.UnLike;

                    likeUnlikeFeed(feedId, statusEnum);
                    break;

                case FEED_API_HIDE:
                    hideFeed(feedId);
                    break;

                case FEED_API_UNFOLLOW_USER:
                    unFollowUser(feedId);
                    break;

                case FEED_API_REPORT_USER:
                    reportUser(feedId);
                    break;

                case FEED_API_DELETE:
                    deleteFeed(feedId);
                    break;

                default:
                    Log.w(TAG, "error in feed api code");
            }
            dialog.dismiss();
        });
        dialog.setView(dialogView);
        dialog.show();
    }

    private void getCommentsLikeList(String feedId) {
        FeedDetailsQuery feedDetailsQuery = new FeedDetailsQuery(feedId);
        aktivoManager.getCommentLikesList(feedDetailsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FeedEmbeddedResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(FeedEmbeddedResponse feedembeddedResponse) {
                        Log.e(TAG, "getCommentsLikeList " + feedembeddedResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getCommentsLikeList, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getFeeds() {
        int pageNumber = 1;
        SocialFeedQuery socialFeedQuery = new SocialFeedQuery(localRepository.getUserId(), pageNumber);
        aktivoManager.getFeeds(socialFeedQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Feed>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Feed> feedList) {
                        Log.e(TAG, "getFeeds " + feedList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFeeds, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getWeeklyRiseGamePoints() {
        RiseGameWeeklyPointsQuery riseGameWeeklyPointsQuery = new RiseGameWeeklyPointsQuery(gameId,
                START_DATE, todaysDate);
        aktivoManager.getWeeklyRiseGamePoints(riseGameWeeklyPointsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RGPWeeklyPoints>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<RGPWeeklyPoints> rgpWeeklyPointsList) {
                        Log.e(TAG, "getWeeklyRiseGamePoints " + rgpWeeklyPointsList.toString());
                        Toast.makeText(MainActivity.this, "getWeeklyRiseGamePoints: " + rgpWeeklyPointsList.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getWeeklyRiseGamePoints, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getDailyRiseGamePoints() {
        RiseGameDailyPointsQuery riseGameDailyPointsQuery = new RiseGameDailyPointsQuery(gameId,
                START_DATE, todaysDate);
        aktivoManager.getDailyRiseGamePoints(riseGameDailyPointsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RGPDailyPoints>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<RGPDailyPoints> rgpDailyPointsList) {
                        Log.e(TAG, "getDailyRiseGamePoints " + rgpDailyPointsList.toString());
                        Toast.makeText(MainActivity.this, "getDailyRiseGamePoints: " + rgpDailyPointsList.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getDailyRiseGamePoints, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getRiseGameRewards() {
        RiseGameRewardsQuery riseGameRewardsQuery = new RiseGameRewardsQuery(gameId);
        aktivoManager.getRiseGameRewards(riseGameRewardsQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RiseGameReward>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(RiseGameReward riseGameReward) {
                        Log.e(TAG, "riseGameReward " + riseGameReward.toString());
                        Toast.makeText(MainActivity.this, "riseGameReward: " + riseGameReward.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "riseGameReward, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getRiseGame() {
        aktivoManager.getRiseGame()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RiseGame>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(RiseGame riseGame) {
                        if (riseGame != null && riseGame.getRiseGameAccount() != null) {
                            gameId = riseGame.getRiseGameAccount().getGameId();
                            Log.e(TAG, "getRiseGame " + riseGame.toString());
                            Log.e(TAG, "getRiseGame: ID " + riseGame.getRiseGameAccount().getGameId());
                            Toast.makeText(MainActivity.this, "getRiseGame: " + riseGame.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getRiseGame, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getOngoingChallenges() {
        aktivoManager.getOngoingChallenges(new ChallengeListQuery(localRepository.getUserId(), true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Challenge>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Challenge> challenges) {
                        Log.e(TAG, "ongoing challenges fetched successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "getOngoingChallenges error:", e);
                    }
                });
    }

    private void getOverChallenges() {
        aktivoManager.getOverChallenges(new ChallengeListQuery(localRepository.getUserId(), true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Challenge>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Challenge> challenges) {
                        Log.e(TAG, "over challenges fetched successfully");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "getOverChallenges error:", e);
                    }
                });
    }

    private void getChallenge(String challengeId) {
        aktivoManager.getChallenge(new ChallengeQuery(localRepository.getUserId(), challengeId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Challenge>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Challenge challenge) {
                        Log.e(TAG, "getChallenge:" + challenge.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "getChallenge error: " + e.getMessage(), e);
                    }
                });
    }

    private void enterChallengeId(int type) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog dialog = builder.create();
        final View dialogView = inflater.inflate(R.layout.layout_username_token, null);
        final TextView userDetailsTV = dialogView.findViewById(R.id.userDetailsTV);
        final Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        final EditText userNameET = dialogView.findViewById(R.id.userNameET);
        final EditText tokenET = dialogView.findViewById(R.id.accessTokenET);
        final CheckBox selectorCB = dialogView.findViewById(R.id.selectorCB);

        selectorCB.setVisibility(View.GONE);
        userNameET.setHint(R.string.challenge_id);
        tokenET.setVisibility(View.GONE);
        userDetailsTV.setText(R.string.challenge_details);

        submitBtn.setOnClickListener(v -> {
            String challengeId = userNameET.getText().toString().trim();
            if (type == ENROL_TO_CHALLENGE)
                enrollToChallenge(challengeId);
            else
                getChallenge(challengeId);

            dialog.dismiss();
        });

        dialog.setView(dialogView);
        dialog.show();
    }

    private void enrollToChallenge(String challengeId) {
        LocalRepository localRepository = new LocalRepository(this);
        //String challengeId = "606b07058aee1e0012fa6e8f";
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("enroll", true);
        aktivoManager.enrollToChallenge(new ChallengeEnrollQuery(localRepository.getUserId(), challengeId, bodyMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Enroll>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Enroll enroll) {
                        Log.e(TAG, "enrolToChallenge: " + enroll.getMessage());
                        Toast.makeText(MainActivity.this, "enrolToChallenge: " + enroll.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "enrolToChallenge error: " + e.getMessage(), e);
                    }
                });
    }

    private void updateUserProfile() {
        LocalDate dateOfBirth = LocalDate.parse("1982/11/13", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalTime bedTime = LocalTime.parse("05:00");
        LocalTime wakeTime = LocalTime.parse("10:00");
        UserProfile.Gender gender = UserProfile.Gender.Male;
        Height heightCms = new Height(new HeightCm(250));
        Height heightFtInch = new Height(new HeightFtIn(6, 2));
        Weight weightKgs = new Weight(new WeightKg(1451));
        Weight weightLbs = new Weight(new WeightLbs(1451));
        UserProfile userProfile = new UserProfile(dateOfBirth, gender, bedTime, wakeTime, heightFtInch, weightLbs);

        aktivoManager.updateUserProfile(userProfile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.e(TAG, "response for update profile: " + aBoolean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "response for update profile: " + e.getMessage());
                    }
                });
    }

    private void getUserProfile() {
        aktivoManager.getUserProfile().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<UserProfile>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(UserProfile userProfile) {
                        Log.e(TAG, "successfully get the user profile, dob: "
                                + userProfile.getDateOfBirth()
                                + " gender: " + userProfile.getGender()
                                + " bedTime: " + userProfile.getBedTime()
                                + " wakeTime: " + userProfile.getWakeTime()
                                + " heightUnit: " + userProfile.getHeight().getHeightUnit()
                                + " weightUnit: " + userProfile.getWeight().getWeightUnit()
                                + " heightFt: " + userProfile.getHeight().getHeightFtIn().getHeightInFt()
                                + " heightIn: " + userProfile.getHeight().getHeightFtIn().getHeightInInch()
                                + " heightCms: " + userProfile.getHeight().getHeightCm().getHeightInCm()
                                + " weightUnit: " + userProfile.getWeight().getWeightUnit()
                                + " weightKg: " + userProfile.getWeight().getWeightKg().getWeightInKg()
                                + " weightLbs: " + userProfile.getWeight().getWeightLbs().getWeightInLbs()
                        );

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkPermissions() {
        aktivoManager.isPermissionGranted().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onNext(Boolean aBoolean) {
                if (!aBoolean) {
                    compositeDisposable.add(
                            aktivoManager.requestGoogleFitPermissions(MainActivity.this, GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            Log.e(TAG, "Google fit permission requested");
                                        }

                                        @Override
                                        public void onError(@androidx.annotation.NonNull Throwable e) {

                                        }
                                    })
                    );
                } else {
                    compositeDisposable.add(
                            aktivoManager.requestActivityRecognitionPermission(MainActivity.this, ACTIVITY_RECOGNITION_REQUEST_CODE)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableCompletableObserver() {

                                        @Override
                                        public void onComplete() {
                                            Log.e(TAG, "Activity recognition permission requested");
                                        }

                                        @Override
                                        public void onError(@androidx.annotation.NonNull Throwable e) {

                                        }
                                    })
                    );
                    requestAccessFineLocationPermission();
                    Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void authenticateUser() {
        aktivoManager
                .authenticateUser(new User("5ec50e8bc9eda80012c5a71d"), this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        fireAktivoReminder();
                        Toast.makeText(MainActivity.this, "User Authenticated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Auth error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        /*LocalRepository localRepository = new LocalRepository(this);
        localRepository.putUserId("61b1b32887d98ccf6a302939");
        localRepository.putAccessToken("0ed603eac0edcdc2b5b29fed8a37addf01a7cb06");*/
    }

    private void authenticateUser(String userId) {
        aktivoManager
                .authenticateUser(new User(userId), this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        fireAktivoReminder();
                        Toast.makeText(MainActivity.this, "User Authenticated", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Auth error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        /*LocalRepository localRepository = new LocalRepository(this);
        localRepository.putUserId("61b1b32887d98ccf6a302939");
        localRepository.putAccessToken("0ed603eac0edcdc2b5b29fed8a37addf01a7cb06");*/
    }

    private void byPassAuthenticateUserUsingToken() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final AlertDialog dialog = builder.create();
        final View dialogView = inflater.inflate(R.layout.layout_username_token, null);
        final Button submitBtn = dialogView.findViewById(R.id.submitBtn);
        final EditText userNameET = dialogView.findViewById(R.id.userNameET);
        final EditText tokenET = dialogView.findViewById(R.id.accessTokenET);
        final CheckBox selectorCB = dialogView.findViewById(R.id.selectorCB);
        selectorCB.setVisibility(View.GONE);

        final LocalRepository localRepository = new LocalRepository(this);
        userNameET.setText(localRepository.getUserId());
        tokenET.setText(localRepository.getAccessToken());
        submitBtn.setOnClickListener(v -> {
            String username = userNameET.getText().toString().trim();
            String token = tokenET.getText().toString().trim();
            authenticateUser(username);
            Toast.makeText(MainActivity.this, "User details updated", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
        dialog.setView(dialogView);
        dialog.show();
    }

    private void syncFitnessData() {
        compositeDisposable.add(
                aktivoManager
                        .syncFitnessData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.e(TAG, "Data synced");
                                Toast.makeText(MainActivity.this, "Data Synced", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                Log.e(TAG, "Data sync error");
                                Toast.makeText(MainActivity.this, "Data Sync error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        })
        );
    }

    private void getScoreStats() {
        Log.e("MainActivity", "todays Date: " + todaysDate);
        aktivoManager.query(new ScoreQuery(START_DATE, todaysDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<LocalDate, ScoreStats>, Map<LocalDate, ScoreStats>>() {
                    @Override
                    public Map<LocalDate, ScoreStats> apply(Map<LocalDate, ScoreStats> localDateScoreStatsMap) throws Exception {
                        return localDateScoreStatsMap;
                    }
                }).subscribe(new SingleObserver<Map<LocalDate, ScoreStats>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Map<LocalDate, ScoreStats> localDateScoreStatsMap) {
                logBulkData();
                Set<LocalDate> keySet = localDateScoreStatsMap.keySet();
                for (LocalDate localDate : keySet) {
                    Log.e(TAG, "Score stats: " + "Date: " + localDate + " Score: " + localDateScoreStatsMap.get(localDate).getScore());
                }
            }

            @Override
            public void onError(Throwable e) {
                logBulkData();
                Log.e(TAG, "Error in getScoreStats: " + e.getMessage());
            }
        });
    }

    private void getSleepStats() {
        aktivoManager.query(new SleepQuery(START_DATE, todaysDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<LocalDate, SleepStats>, Map<LocalDate, SleepStats>>() {
                    @Override
                    public Map<LocalDate, SleepStats> apply(Map<LocalDate, SleepStats> localDateSleepStatsMap) throws Exception {
                        return localDateSleepStatsMap;
                    }
                }).subscribe(new SingleObserver<Map<LocalDate, SleepStats>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Map<LocalDate, SleepStats> localDateSleepStatsMap) {
                Set<LocalDate> keySet = localDateSleepStatsMap.keySet();
                for (LocalDate localDate : keySet) {
                    Log.e(TAG, "Sleep stats: " + "Date: " + localDate + " Sleep: " + localDateSleepStatsMap.get(localDate).getValue());
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void getStepsStats() {
        aktivoManager.query(new StepsQuery(START_DATE, todaysDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<LocalDate, StepStats>, Map<LocalDate, StepStats>>() {
                    @Override
                    public Map<LocalDate, StepStats> apply(Map<LocalDate, StepStats> localDateStepStatsMap) throws Exception {
                        return localDateStepStatsMap;
                    }
                }).subscribe(new SingleObserver<Map<LocalDate, StepStats>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Map<LocalDate, StepStats> localDateStepStatsMap) {
                Set<LocalDate> keySet = localDateStepStatsMap.keySet();
                for (LocalDate localDate : keySet) {
                    Log.e(TAG, "Steps stats: " + "Date: " + localDate + " Steps: " + localDateStepStatsMap.get(localDate).getValue());
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void getHeartRateStats() {
        aktivoManager.query(new HeartRateQuery(START_DATE, todaysDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<LocalDate, HeartRateStats>, Map<LocalDate, HeartRateStats>>() {
                    @Override
                    public Map<LocalDate, HeartRateStats> apply(Map<LocalDate, HeartRateStats> localDateHeartRateStatsMap) throws Exception {
                        return localDateHeartRateStatsMap;
                    }
                })
                .subscribe(new SingleObserver<Map<LocalDate, HeartRateStats>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Map<LocalDate, HeartRateStats> localDateHeartRateStatsMap) {
                        Set<LocalDate> keySet = localDateHeartRateStatsMap.keySet();
                        for (LocalDate localDate : keySet) {
                            Log.e(TAG, "Heart rate stats: " + "Date: " + localDate + " Heart rate: " + localDateHeartRateStatsMap.get(localDate).getValue());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getScoreStatsLatest() {
        Log.e("MainActivity", "todays Date: " + todaysDate);
        aktivoManager.query(new ScoreLatestQuery(todaysDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Map<LocalDate, ScoreStats>, Map<LocalDate, ScoreStats>>() {
                    @Override
                    public Map<LocalDate, ScoreStats> apply(Map<LocalDate, ScoreStats> localDateScoreStatsMap) throws Exception {
                        return localDateScoreStatsMap;
                    }
                }).subscribe(new SingleObserver<Map<LocalDate, ScoreStats>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Map<LocalDate, ScoreStats> localDateScoreStatsMap) {
                logBulkData();
                Set<LocalDate> keySet = localDateScoreStatsMap.keySet();
                for (LocalDate localDate : keySet) {
                    Log.e(TAG, "Score stats: " + "Date: " + localDate + " Score: " + localDateScoreStatsMap.get(localDate).getScore());
                }
            }

            @Override
            public void onError(Throwable e) {
                logBulkData();
                Log.e(TAG, "Error in getScoreStats: " + e.getMessage());
            }
        });
    }

    private void getBatchStats() {
        List<Query> queryList = new ArrayList<>();
        queryList.add(new ScoreQuery(START_DATE, todaysDate));
        queryList.add(new StepsQuery(START_DATE, todaysDate));
        queryList.add(new SleepQuery(START_DATE, todaysDate));
        queryList.add(new HeartRateQuery(START_DATE, todaysDate));

        compositeDisposable.add(
                aktivoManager
                        .query(queryList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<Map<LocalDate, Stats>>, List<Map<LocalDate, Stats>>>() {
                            @Override
                            public List<Map<LocalDate, Stats>> apply(List<Map<LocalDate, Stats>> maps) throws Exception {
                                return maps;
                            }
                        }).subscribeWith(new DisposableSingleObserver<List<Map<LocalDate, Stats>>>() {
                    @Override
                    public void onSuccess(List<Map<LocalDate, Stats>> mapList) {
                        Log.e(TAG, "Map size: " + mapList.size());
                        Log.e(TAG, "Map: " + mapList);
                        for (int i = 0; i < mapList.size(); i++) {
                            Set<LocalDate> keySet = mapList.get(i).keySet();
                            if (queryList.get(i) instanceof ScoreQuery) {
                                for (LocalDate object : keySet) {
                                    Log.e(TAG, "Date: " + object + " value: " + ((ScoreStats) mapList.get(i).get(object)).getScore());
                                }
                            } else if (queryList.get(i) instanceof StepsQuery) {
                                for (LocalDate object : keySet) {
                                    Log.e(TAG, "Date: " + object + " value: " + ((StepStats) mapList.get(i).get(object)).getValue());
                                }
                            } else if (queryList.get(i) instanceof SleepQuery) {
                                for (LocalDate object : keySet) {
                                    Log.e(TAG, "Date: " + object + " value: " + ((SleepStats) mapList.get(i).get(object)).getValue());
                                }
                            } else if (queryList.get(i) instanceof HeartRateQuery) {
                                for (LocalDate object : keySet) {
                                    Log.e(TAG, "Date: " + object + " value: " + ((HeartRateStats) mapList.get(i).get(object)).getValue());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Home stats error: " + e.getLocalizedMessage() + "---" + e.getMessage()); //TODO remove after fixing infinite loader
                    }
                })
        );
    }

    private void requestGoogleFitPermission(FitnessOptions fitnessOptions) {
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions);
    }

    private void getBadgesByDate() {
        BadgeByDateQuery badgeByDateQuery = new BadgeByDateQuery(todaysDate);
        aktivoManager.queryBadgeByDate(badgeByDateQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DailyBadge>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(DailyBadge dailyBadge) {
                        if (dailyBadge.getBadgeType() != null) {
                            Log.e(TAG, "BadgesByDate, getRefDate: " + dailyBadge.getRefDate()
                                    + " dailyBadge: type: " + dailyBadge.getBadgeType().getBadgeTypeEnum().toString()
                                    + " dailyBadge: type: " + dailyBadge.getBadgeType().getTitle()
                            );
                        } else {
                            Log.e(TAG, "BadgesByDate, getRefDate: " + dailyBadge.getRefDate());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "BadgesByDate, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getBadgesSummary() {
        BadgeSummaryQuery badgeSummaryQuery = new BadgeSummaryQuery(START_DATE, todaysDate);
        aktivoManager.queryBadgeSummary(badgeSummaryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BadgeSummary>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(BadgeSummary badgeSummary) {
                        Log.e(TAG, "BadgesSummary, getAllTimeTotal: "
                                + badgeSummary.getAllTimeTotal()
                                + " periodTotal: " + badgeSummary.getPeriodTotal()
                        );

                        if (badgeSummary.getLastEarnedBadge() != null) {
                            Log.e(TAG, "getLastEarnedBadge() " + badgeSummary.getLastEarnedBadge().getRefDate());
                            if (badgeSummary.getLastEarnedBadge().getLastEarnedBadgeType() != null) {
                                Log.e(TAG, "getLastEarnedBadge(): type"
                                        + badgeSummary.getLastEarnedBadge().getLastEarnedBadgeType().getBadgeTypeEnum().toString()
                                        + " title: " + badgeSummary.getLastEarnedBadge().getLastEarnedBadgeType().getTitle()
                                );
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getBadgesSummary, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getBadgesHistory() {
        BadgeHistoryQuery badgeHistoryQuery = new BadgeHistoryQuery(START_DATE, todaysDate);
        aktivoManager.queryBadgeHistory(badgeHistoryQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Map<LocalDate, HistoryBadgeType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Map<LocalDate, HistoryBadgeType> localDateHistoryBadgeTypeMap) {
                        for (Map.Entry<LocalDate, HistoryBadgeType> entry : localDateHistoryBadgeTypeMap.entrySet()) {
                            Log.e(TAG, "BadgesHistory, refDate: " + entry.getKey()
                                    + "badgeTypeEnum: " + entry.getValue().getBadgeTypeEnum().toString()
                                    + "title: " + entry.getValue().getTitle()
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getBadgesHistory, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getAktivoBadgeTypes() {
        aktivoManager.queryAllBadges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AktivoBadgeType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<AktivoBadgeType> aktivoBadgeTypes) {
                        for (AktivoBadgeType aktivoBadgeType : aktivoBadgeTypes) {
                            Log.e(TAG, "AktivoBadgeTypes, title: " + aktivoBadgeType.getTitle());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getAktivoBadgeTypes, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getFitnessTrackers() {
        aktivoManager.getFitnessTrackers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<FitnessTracker>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<FitnessTracker> fitnessTrackers) {
                        for (FitnessTracker fitnessTracker : fitnessTrackers) {
                            Log.e(TAG, "getFitnessTrackers, fitnessTracker: state: "
                                    + fitnessTracker.getFitnessTrackerConnectionState()
                                    + " platform: " + Objects.requireNonNull(fitnessTracker.getPlatform()).name()
                            );
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFitnessTrackers, error: " + e.getMessage(), e);
                    }
                });
    }

    private void getFitnessPlatformAuthUrlForFitbit() {
        aktivoManager.getFitnessPlatformAuthUrl(FitnessTracker.Platform.FITBIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FitnessTrackerAuthUrlInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(FitnessTrackerAuthUrlInfo fitnessTrackerAuthUrlInfo) {
                        Log.e(TAG, "getFitnessPlatformAuthUrlForFitbit, fitnessTrackerAuthUrlInfo: "
                                + fitnessTrackerAuthUrlInfo.getAuthUrl());
                        /*Intent i = new Intent(MainActivity.this, WebActivity.class);
                        i.putExtra("url", fitnessTrackerAuthUrlInfo.getAuthUrl());
                        startActivity(i);*/

                        CustomTabsIntent.Builder customTabIntent = new CustomTabsIntent.Builder();
                        openCustomTab(customTabIntent.build(), Uri.parse(fitnessTrackerAuthUrlInfo.getAuthUrl()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFitnessPlatformAuthUrlForFitbit, error: " + e.getMessage(), e);
                    }
                });
    }

    private void openCustomTab(CustomTabsIntent customTabsIntent, Uri uri) {
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.launchUrl(this, uri);
    }

    private void getFitnessPlatformAuthUrlForGarmin() {
        aktivoManager.getFitnessPlatformAuthUrl(FitnessTracker.Platform.GARMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FitnessTrackerAuthUrlInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(FitnessTrackerAuthUrlInfo fitnessTrackerAuthUrlInfo) {
                        Log.e(TAG, "getFitnessPlatformAuthUrlForGarmin, fitnessTrackerAuthUrlInfo: "
                                + fitnessTrackerAuthUrlInfo.getAuthUrl());
                        /*Intent i = new Intent(MainActivity.this, WebActivity.class);
                        i.putExtra("url", fitnessTrackerAuthUrlInfo.getAuthUrl());
                        startActivity(i);*/

                        CustomTabsIntent.Builder customTabIntent = new CustomTabsIntent.Builder();
                        openCustomTab(customTabIntent.build(), Uri.parse(fitnessTrackerAuthUrlInfo.getAuthUrl()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getFitnessPlatformAuthUrlForGarmin, error: " + e.getMessage(), e);
                    }
                });
    }

    private void disconnectFitbit() {
        aktivoManager.disconnectFitnessPlatform(FitnessTracker.Platform.FITBIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.e(TAG, "disconnectFitbit, state " + aBoolean);
                        Toast.makeText(MainActivity.this, "Disconnected fitbit successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "disconnectFitbit, error: " + e.getMessage(), e);
                    }
                });
    }

    private void disconnectGarmin() {
        aktivoManager.disconnectFitnessPlatform(FitnessTracker.Platform.GARMIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Log.e(TAG, "disconnectGarmin, state " + aBoolean);
                        Toast.makeText(MainActivity.this, "Disconnected garmin successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "disconnectGarmin, error: " + e.getMessage(), e);
                    }
                });
    }

    private void logBulkData() {
        if (localRepository.getElkBaseUrl().isEmpty())
            return;

        ElkManager elkManager = new ElkManager(this,
                localRepository.getElkBaseUrl(),
                localRepository.getElkApiKey());
        List<ElkLog> elkLogList = new ArrayList<>();

        elkLogList.add(new ElkLog("android_client", "_doc", "android", "1.0.68", "staging", "debug", "5d64f5d26d89460015ebab72", "Log from android 001", "2020-06-26T04:54:07.913Z"));
        elkLogList.add(new ElkLog("android_client", "_doc", "android", "1.0.68", "staging", "debug", "5d64f5d26d89460015ebab72", "Log from android 002", "2020-06-26T04:54:07.913Z"));

        elkManager.logBulkData(elkLogList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "Bulk upload successful", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void callPersonalizeAPI() {

    }

    private void invalidateUser() {
        aktivoManager
                .invalidateUser(new User(localRepository.getUserId()), this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this, "User invalidated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void getAktivoScoreInsightText() {
        aktivoManager.getAktivoScoreInsightText(
                "Lifestyle Score",
                new ScoreStats(
                        84,
                        new ScoreElement(
                                1,
                                ""
                        ),
                        new ScoreElement(
                                1,
                                ""
                        ),
                        new ScoreElement(
                                1,
                                ""
                        ),
                        "2021-07-15"
                ),
                new SleepStats(100, "", "2021-07-15"),
                new StepStats(100, "2021-07-15"),
                true
        )
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NotNull String s) {
                        Log.e(TAG, "Insight text: " + s);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                });
    }

    private void fireAktivoReminder() {
        ArrayList<Reminder> reminderList = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();
        days.add("TUE");
        reminderList.add(
                new Reminder(
                        "abcd",
                        "xyz",
                        "Title",
                        "Description",
                        new ReminderAt(
                                "20:03",
                                new ReminderFrequency(
                                        "everyDay",
                                        days
                                )
                        )
                )
        );
        reminderList.add(
                new Reminder(
                        "abcd2",
                        "xyz2",
                        "Title",
                        "Description",
                        new ReminderAt(
                                "13:36",
                                new ReminderFrequency(
                                        "weekdays",
                                        days
                                )
                        )
                )
        );
        AktivoReminder aktivoReminder = new AktivoReminder(
                this,
                "notification_channel_id",
                "notification_channel_name",
                "com.aktivolabs.sample.MainActivity"
        );
        aktivoReminder.cancelAllReminders();
        aktivoReminder.setReminderList(reminderList);
    }

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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void requestActivityRecognitionPermission() {
        requestPermissionLauncher.launch(
                Manifest.permission.ACTIVITY_RECOGNITION);
    }

    private void requestAccessFineLocationPermission() {
        requestPermissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
