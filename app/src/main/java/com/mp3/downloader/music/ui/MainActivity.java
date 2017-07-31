package com.mp3.downloader.music.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mp3.downloader.music.R;
import com.mp3.downloader.music.db.DBService;
import com.mp3.downloader.music.db.DBServiceImpl;
import com.mp3.downloader.music.model.AudioLink;
import com.mp3.downloader.music.model.AudioManager;
import com.mp3.downloader.music.network.NetworkService;
import com.mp3.downloader.music.network.ResponseListener;
import com.mp3.downloader.music.utils.AndroidHelper;

import java.io.File;

import static com.mp3.downloader.music.network.NetworkService.getFullPath;
import static com.mp3.downloader.music.utils.AndroidHelper.showAlert;
import static com.mp3.downloader.music.utils.LogHelper.makeLogTag;

public class MainActivity extends AppCompatActivity implements AudioManager {


    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4, floatingActionButton5, floatingActionButton6;


    private static final String LOG_TAG = makeLogTag(MainActivity.class);

    private static final int GET_YOUTUBE_URL = 1;

    @BindView(R.id.my_toolbar)
    Toolbar toolbar;
    @BindView(R.id.list_audio)
    RecyclerView listAudio;

    private AudioAdapter adapter;
    private DBService dbService;
    private ProgressDialog pd;
    private NetworkService networkService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        ButterKnife.bind(this);
        AndroidThreeTen.init(this);

        pd = createProgressDialog();
        dbService = new DBServiceImpl(this);
        networkService = new NetworkService(this);

        setSupportActionBar(toolbar);
        initRecyclerView();

        checkPermissions();

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleSendText(intent);
                }
            }
        }





        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.social_floating_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_facebook);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_linkdin);
        floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_instagram);
        floatingActionButton6 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.floating_youtube);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                Intent facebookIntent = getOpenFacebookIntent(MainActivity.this);
                startActivity(facebookIntent);


            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                Intent linkdinIntent = getOpenLinkdinIntent(MainActivity.this);
                startActivity(linkdinIntent);
            }
        });


        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                Intent instagramIntent = getOpenInstagramIntent(MainActivity.this);
                startActivity(instagramIntent);
            }
        });
        floatingActionButton6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked
                Intent youtubeIntent = getOpenYouTubeIntent(MainActivity.this);
                startActivity(youtubeIntent);
            }
        });


    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/100003203346146")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/bwedoo.dewe")); //catches and opens a url to the desired page
        }
    }



    public static Intent getOpenLinkdinIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.playstore", 0); //Checks if Linkdin is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=pemikir%20versi%20baru")); //Trys to make intent with Linkdin's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=pemikir%20versi%20baru")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenGPlusIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.apps.plus", 0); //Checks if G+ is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/u/0/106173178497476203797")); //Trys to make intent with G+'s URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/u/0/106173178497476203797")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenInstagramIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.instagram.android", 0); //Checks if Instagram is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/pemikir_versi_baru/")); //Trys to make intent with Instagram's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/pemikir_versi_baru/")); //catches and opens a url to the desired page
        }
    }

    public static Intent getOpenYouTubeIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.google.android.youtube", 0); //Checks if YT is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCQ9wB7jxmr_Kqig_UZrFDBQ")); //Trys to make intent with YT's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/channel/UCQ9wB7jxmr_Kqig_UZrFDBQ")); //catches and opens a url to the desired page
        }
    }




    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AndroidHelper.requestMultiplePermissions(this);
        }
    }






    private void initRecyclerView() {
        listAudio.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listAudio.setLayoutManager(layoutManager);
        adapter = new AudioAdapter(this, this);
        listAudio.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            addAudio(sharedText);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void onBackPressed() {
        exit();//Pergi ke method exit
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do You Want to Exit..?")
                .setCancelable(false)//tidak bisa tekan tombol back
                //jika pilih yess
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                //jika pilih no
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent();
                intent.setClass(this, AddLinkActivity.class);
                startActivityForResult(intent, GET_YOUTUBE_URL);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_YOUTUBE_URL) {
            if (resultCode == RESULT_OK) {
                String url = data.getStringExtra("url");
                addAudio(url);
            }
        }
    }

    @Override
    public void addAudio(String url) {
        if (!pd.isShowing()) {
            pd.show();
        }
        networkService.parsePage(url, new ResponseListener<AudioLink>() {
            @Override
            public void onResponse(AudioLink response) {
                if (pd.isShowing()) {
                    pd.hide();
                }
                if (!dbService.isAlreadyAdded(response)) {
                    adapter.addItem(response);
                    long id = dbService.addLink(response);
                    Log.i(LOG_TAG, "Video " + response.getTitle() + " are added with id = " + id);
                } else {
                    Log.i(LOG_TAG, "This video are already added " + response.getTitle());
                    showAlert(MainActivity.this, "This video are already added " + response.getTitle());
                }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(LOG_TAG, "Failed to download page on this url " + url);
                Log.e(LOG_TAG, Log.getStackTraceString(t));
                showAlert(MainActivity.this, "Failed to download page on this url " + url);
            }
        });
    }

    @Override
    public void deleteAudio(AudioLink link) {
        dbService.deleteLink(link);
        dbService.deleteAudio(link.getAudio());
        File audio = new File(getFullPath(link));
        if (audio.exists()) {
            audio.delete();
            Log.i(LOG_TAG, "Deleted file " + link.getFileName());
        }
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Page downloading...");
        pd.setCancelable(false);
        return pd;
    }
}
