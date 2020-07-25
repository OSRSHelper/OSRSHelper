package com.infonuascape.osrshelper.activities;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.SuggestionsAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.fragments.CmlTopFragment;
import com.infonuascape.osrshelper.fragments.DataPointsFragment;
import com.infonuascape.osrshelper.fragments.GrandExchangeDetailFragment;
import com.infonuascape.osrshelper.fragments.GrandExchangeSearchFragment;
import com.infonuascape.osrshelper.fragments.HighScoreFragment;
import com.infonuascape.osrshelper.fragments.NewsFeedFragment;
import com.infonuascape.osrshelper.fragments.NewsFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.fragments.WebViewFragment;
import com.infonuascape.osrshelper.fragments.WorldMapFragment;
import com.infonuascape.osrshelper.fragments.XPTrackerFragment;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Logger;
import com.infonuascape.osrshelper.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,
        SearchView.OnSuggestionListener, SearchView.OnQueryTextListener, FilterQueryProvider, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String EXTRA_FRAGMENT_TO_OPEN = "EXTRA_FRAGMENT_TO_OPEN";
    private static final String EXTRA_FRAGMENT_TO_OPEN_BUNDLE = "EXTRA_FRAGMENT_TO_OPEN_BUNDLE";
    public static final int REQUEST_CODE_SET_PROFILE = 9001;

    public static Intent getGrandExchangeDetailIntent(Context context, String name, String itemId) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(GrandExchangeDetailFragment.EXTRA_ITEM_NAME, name);
        i.putExtra(GrandExchangeDetailFragment.EXTRA_ITEM_ID, itemId);
        i.putExtra(EXTRA_FRAGMENT_TO_OPEN, GrandExchangeDetailFragment.class.getSimpleName());
        return i;
    }

    public static Intent getNewsIntent(Context context, String url) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra(EXTRA_FRAGMENT_TO_OPEN_BUNDLE, WebViewFragment.getBundle(url, true));
        i.putExtra(EXTRA_FRAGMENT_TO_OPEN, WebViewFragment.class.getSimpleName());
        return i;
    }

    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private SuggestionsAdapter suggestionsAdapter;
    public boolean isResumed;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        isResumed = true;
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.lookup_user));
        View searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        if(searchAutoComplete instanceof SearchView.SearchAutoComplete) {
            ((SearchView.SearchAutoComplete) searchAutoComplete).setThreshold(0);
        }

        suggestionsAdapter = new SuggestionsAdapter(this, DBController.searchAccountsByUsername(this, null));
        searchView.setSuggestionsAdapter(suggestionsAdapter);
        suggestionsAdapter.setFilterQueryProvider(this);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(this);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(this);

        MainFragmentController.init(this, navigationView);
        MainFragmentController.getInstance().showRootFragment(R.id.nav_home, NewsFeedFragment.newInstance());

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        final String fragmentToOpen = intent.getStringExtra(EXTRA_FRAGMENT_TO_OPEN);
        if(fragmentToOpen != null) {
            final Bundle bundle = intent.getBundleExtra(EXTRA_FRAGMENT_TO_OPEN_BUNDLE);
            if(TextUtils.equals(GrandExchangeDetailFragment.class.getSimpleName(), fragmentToOpen)) {
                final String name = intent.getStringExtra(GrandExchangeDetailFragment.EXTRA_ITEM_NAME);
                final String itemId = intent.getStringExtra(GrandExchangeDetailFragment.EXTRA_ITEM_ID);
                OSRSFragment fragment = GrandExchangeDetailFragment.newInstance(name, itemId);
                MainFragmentController.getInstance().showFragment(fragment);
            } else if(TextUtils.equals(WebViewFragment.class.getSimpleName(), fragmentToOpen)) {
                OSRSFragment fragment = WebViewFragment.newInstance(bundle);
                MainFragmentController.getInstance().showFragment(fragment);
            }

            intent.removeExtra(EXTRA_FRAGMENT_TO_OPEN);
        }
    }

    public void refreshProfileAccount() {
        Logger.add(TAG, ": refreshProfileAccount");
        Account account = DBController.getProfileAccount(this);
        SubMenu profileSubMenu = navigationView.getMenu().getItem(1).getSubMenu();

        //If there's no data_points set, hide the options
        for (int i = 0; i < profileSubMenu.size(); i++) {
            if (profileSubMenu.getItem(i).getItemId() != R.id.nav_switch_profile) {
                profileSubMenu.getItem(i).setVisible(account != null);
            }
        }
        if(account != null) {
            ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_icon)).setImageResource(Utils.getAccountTypeResource(account.type));
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.profile_name)).setText(account.getDisplayName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfileAccount();
        isResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(!MainFragmentController.getInstance().onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SET_PROFILE) {
            MainFragmentController.getInstance().setSelectedMenuItem(-1);

            if (resultCode == RESULT_OK) {
                refreshProfileAccount();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final boolean isShowVirtualLevels = PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);
        menu.getItem(0).setTitle(isShowVirtualLevels ? R.string.hide_virtual_levels : R.string.show_virtual_levels);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_show_virtual_levels) {
            final boolean isShowVirtualLevels = PreferencesController.getBooleanPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, false);
            PreferencesController.setPreference(this, PreferencesController.USER_PREF_SHOW_VIRTUAL_LEVELS, !isShowVirtualLevels);
            OSRSFragment fragment = MainFragmentController.getInstance().getCurrentFragment();
            if(fragment != null) {
                fragment.refreshDataOnPreferencesChanged();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        OSRSFragment fragment = null;

        if (id == R.id.nav_home) {
            fragment = NewsFeedFragment.newInstance();
        } else if (id == R.id.nav_hiscores) {
            Account account = DBController.getProfileAccount(this);
            fragment = HighScoreFragment.newInstance(account);
        } else if (id == R.id.nav_cml_tracker) {
            Account account = DBController.getProfileAccount(this);
            fragment = XPTrackerFragment.newInstance(account);
        } else if (id == R.id.nav_data_points) {
            Account account = DBController.getProfileAccount(this);
            fragment = DataPointsFragment.newInstance(account);
        } else if (id == R.id.nav_world_map) {
            fragment = WorldMapFragment.newInstance();
        } else if (id == R.id.nav_wiki) {
            fragment = WebViewFragment.newInstance("https://oldschool.runescape.wiki/");
        } else if (id == R.id.nav_news) {
            fragment = NewsFragment.newInstance();
        } else if (id == R.id.nav_grand_exchange) {
            fragment = GrandExchangeSearchFragment.newInstance();
        } else if (id == R.id.nav_top_players) {
            fragment = CmlTopFragment.newInstance();
        } else if (id == R.id.nav_switch_profile) {
            UsernameActivity.showForProfileForResult(this, REQUEST_CODE_SET_PROFILE);
            return true;
        }

        if(fragment != null) {
            MainFragmentController.getInstance().showRootFragment(id, fragment);
            drawer.closeDrawer(GravityCompat.START);
            drawer.closeDrawers();
        }

        return true;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {}

    @Override
    public void onDrawerOpened(View drawerView) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
    }

    @Override
    public void onDrawerClosed(View drawerView) {}

    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Account account = DBController.createAccountFromCursor((Cursor) suggestionsAdapter.getItem(position));
        if(account != null) {
            MainFragmentController.getInstance().showRootFragment(account.isProfile ? R.id.nav_hiscores : -1, HighScoreFragment.newInstance(account));
            searchView.setQuery(null, false);
            searchView.clearFocus();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Logger.add(TAG, ": onQueryTextSubmit: query=" + query);
        MainFragmentController.getInstance().showRootFragment(-1, HighScoreFragment.newInstance(query));
        searchView.setQuery(null, false);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Logger.add(TAG, ": onQueryTextChange: query=" + query);
        return false;
    }

    @Override
    public Cursor runQuery(CharSequence query) {
        Logger.add(TAG, ": runQuery: charSequence=" + query);

        Cursor c = DBController.searchAccountsByUsername(this, query);
        return c;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.nav_header_container) {
            Account account = DBController.getProfileAccount(this);
            if(account != null) {
                MainFragmentController.getInstance().showRootFragment(R.id.nav_hiscores, HighScoreFragment.newInstance(account));
                drawer.closeDrawer(GravityCompat.START);
                drawer.closeDrawers();
            }
        }
    }
}
