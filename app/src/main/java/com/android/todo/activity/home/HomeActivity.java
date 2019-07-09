package com.android.todo.activity.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.todo.R;
import com.android.todo.activity.authentication.LoginActivity;
import com.android.todo.activity.home.fragment.FinishedNotesFragment;
import com.android.todo.activity.home.fragment.UpcomingNotesFragment;
import com.android.todo.activity.home.presenter.HomeDelegate;
import com.android.todo.activity.home.presenter.HomePresenter;
import com.android.todo.activity.todo.NoteActivity;
import com.android.todo.model.dto.NoteDTO;
import com.android.todo.model.dto.UserDTO;
import com.android.todo.model.dto.UserNotesDTO;
import com.android.todo.model.sqlite.NotesDataBaseManager;
import com.android.todo.network.NetworkRouter;
import com.android.todo.utils.SharedPreferencesUtility;
import com.android.todo.utils.Utility;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeDelegate {

    private static final String TAG = "HomeActivity";
    private ArrayList<NoteDTO> localNotes = new ArrayList<>();
    private NotesDataBaseManager notesDataBaseManager;
    private ArrayList<UserDTO> localUsers = new ArrayList<>();
    private HomePresenter homePresenter;

    public FinishedNotesFragment finishedNoteFragment;

    private List<NoteDTO> notesLocal = new ArrayList<>();
    private List<NoteDTO> notesRemote = new ArrayList<>();
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager = findViewById(R.id.view_pager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

//        homePresenter = new HomePresenter(this, this);


        // SQLite data configuration
        notesDataBaseManager = new NotesDataBaseManager(this);
        notesDataBaseManager.open();


        // Toolbar configuration
        setSupportActionBar(toolbar);

        // FloatingActionButton configuration
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NoteActivity.class);
                intent.putExtra("actionType", 1);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        // DrawerLayout configuration
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // NavigationView configuration
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        ImageView profileImageView = header.findViewById(R.id.profile_image_view);
        TextView usernameTextView = header.findViewById(R.id.username_text_view);
        TextView emailTextView = header.findViewById(R.id.email_text_view);

//        Uri photoUrl = mUser.getPhotoUrl();
//        if (photoUrl != null) {
//            Glide.with(profileImageView.getContext())
//                    .load(photoUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(profileImageView);
//        }
//
//        if (mUser != null) {
//            usernameTextView.setText(mUser.getDisplayName());
//            emailTextView.setText(mUser.getEmail());
//        }

        // TabLayout configuration
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
        if (!Utility.isNetworkAvailable(this)) {
            showConnectivityAlert();

            localNotes = notesDataBaseManager.fetchAll(SharedPreferencesUtility.getUserId(this));
            if (localNotes.size() != 0) {
                setupViewPagerInOfflineMode(viewPager);
            } else {
                setupViewPagerInOnlineMode(viewPager);
                Toast.makeText(this, "you don't have any notes yet!", Toast.LENGTH_LONG).show();
            }
        } else {
            // check network availability
            AndroidNetworking.get(NetworkRouter.buildURLRequest("getNotes"))
                    .addHeaders("Content-Type", "application/json")
                    .addQueryParameter("userId", "1")
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            System.out.println(response);
                            NoteDTO[] navigationArray = new Gson().fromJson(response.toString(), NoteDTO[].class);

                            for (int i = 0; i < navigationArray.length; i++) {
                                notesRemote.add(navigationArray[i]);
                            }

                            compareData();
                            setupViewPagerInOnlineMode(viewPager);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        }
    }

    private void showConnectivityAlert() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.network_alert_title)
                .setMessage(R.string.network_alert_description)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.sign_out_menu:
                signOut();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            Toast.makeText(HomeActivity.this, "Comming soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_notes) {
//            Intent intent = new Intent(this, HistoryTripsMapActivty.class);
//            startActivity(intent);
        } else if (id == R.id.nav_sign_out) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        redirectLoginScreen();
    }

    private void redirectLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        SharedPreferencesUtility.setLoggedIn(this, false);
        startActivity(intent);
        finish();
    }

    private void setupViewPagerInOnlineMode(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpcomingNotesFragment(), "Upcoming");
        finishedNoteFragment=new FinishedNotesFragment();
        adapter.addFragment(finishedNoteFragment, "Finished");
        viewPager.setAdapter(adapter);
    }

    private void setupViewPagerInOfflineMode(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        UpcomingNotesFragment upcomingNotesFragment = new UpcomingNotesFragment();
        upcomingNotesFragment.getData(notesDataBaseManager.fetchUpcomingNotes(1));
        adapter.addFragment(upcomingNotesFragment, "Upcoming");


        finishedNoteFragment = new FinishedNotesFragment();
        finishedNoteFragment.getData(notesDataBaseManager.fetchFinishedNotes(1));
        adapter.addFragment(finishedNoteFragment, "Finished");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void fetchedData(List<NoteDTO> notes) {
        Toast.makeText(this, String.valueOf(notes.size()), Toast.LENGTH_LONG).show();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public void compareData() {
        // fetching data from SQLite DB
        notesLocal = notesDataBaseManager.fetchAll(1);

        if (notesLocal.size() > notesRemote.size()) {

            // delete data on the server
            AndroidNetworking.delete(NetworkRouter.buildURLRequest("deleteAllNotes"))
                    .addQueryParameter("userId", "1")
                    .addHeaders("Content-Type", "application/json")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                        }

                        @Override
                        public void onError(ANError anError) {
                            System.out.println(anError.getLocalizedMessage());
                        }
                    });

            // add note on server
            UserNotesDTO userNotesDTO = new UserNotesDTO();
            userNotesDTO.setUserId(SharedPreferencesUtility.getUserId(this));
            userNotesDTO.setNotes(notesLocal);
            System.out.println(new Gson().toJson(userNotesDTO));

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(new Gson().toJson(userNotesDTO));
                System.out.println(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // save all local data on the server
            AndroidNetworking.post(NetworkRouter.buildURLRequest("uploadNotes"))
                    .addJSONObjectBody(jsonObject)
                    .addHeaders("Content-Type", "application/json")
                    .setTag("test")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println(response.toString());
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            System.out.println(error);
                        }
                    });

            // TODO: FETCH NOTES
            AndroidNetworking.get(NetworkRouter.buildURLRequest("getNotes"))
                    .addHeaders("Content-Type", "application/json")
                    .addQueryParameter("userId", "1")
                    .setTag("test")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            System.out.println(response);
                            NoteDTO[] navigationArray = new Gson().fromJson(response.toString(), NoteDTO[].class);
                            List<NoteDTO> notes = new ArrayList<>();

                            for (int i = 0; i < navigationArray.length; i++) {
                                notes.add(navigationArray[i]);
                            }

                            // Update SQLite
                            int userId = SharedPreferencesUtility.getUserId(HomeActivity.this);
                            notesDataBaseManager.deleteAll(userId);
                            notesDataBaseManager.insertAll(notes, userId);
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } else if (notesLocal.size() < notesRemote.size()) {
            // delete all local data
            notesDataBaseManager.deleteAll(SharedPreferencesUtility.getUserId(this));
            // save all remote data locally
            notesDataBaseManager.insertAll(notesRemote, SharedPreferencesUtility.getUserId(this));
        }
    }
}