package com.infonuascape.osrshelper.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.adapters.SuggestionsAdapter;
import com.infonuascape.osrshelper.controllers.MainFragmentController;
import com.infonuascape.osrshelper.db.DBController;
import com.infonuascape.osrshelper.db.PreferencesController;
import com.infonuascape.osrshelper.fragments.CMLXPTrackerFragment;
import com.infonuascape.osrshelper.fragments.CmlTopFragment;
import com.infonuascape.osrshelper.fragments.CombatCalcFragment;
import com.infonuascape.osrshelper.fragments.DonationFragment;
import com.infonuascape.osrshelper.fragments.GrandExchangeSearchFragment;
import com.infonuascape.osrshelper.fragments.HighScoreFragment;
import com.infonuascape.osrshelper.fragments.NewsFeedFragment;
import com.infonuascape.osrshelper.fragments.NewsFragment;
import com.infonuascape.osrshelper.fragments.OSRSFragment;
import com.infonuascape.osrshelper.fragments.ProfileFragment;
import com.infonuascape.osrshelper.fragments.WebViewFragment;
import com.infonuascape.osrshelper.fragments.WorldMapFragment;
import com.infonuascape.osrshelper.models.Account;
import com.infonuascape.osrshelper.utils.Utils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener,
        SearchView.OnSuggestionListener, SearchView.OnQueryTextListener, FilterQueryProvider {
    private static final String TAG = "MainActivity";

    private SearchView searchView;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private SuggestionsAdapter suggestionsAdapter;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.lookup_user));
        View searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainFragmentController.init(this, navigationView);
        MainFragmentController.getInstance().showRootFragment(R.id.nav_home, NewsFeedFragment.newInstance());
    }

    public void refreshProfileAccount() {
        Log.i(TAG, "refreshProfileAccount");
        Account account = DBController.getProfileAccount(this);
        navigationView.getMenu().getItem(1).getSubMenu().setGroupEnabled(R.id.nav_group_profile, account != null);
        if(account != null) {
            ((ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_icon)).setImageResource(Utils.getAccountTypeResource(account.type));
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.profile_name)).setText(account.username);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfileAccount();
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
            fragment = CMLXPTrackerFragment.newInstance(account);
        } else if (id == R.id.nav_combat_lvl) {
            Account account = DBController.getProfileAccount(this);
            fragment = CombatCalcFragment.newInstance(account);
        } else if (id == R.id.nav_world_map) {
            fragment = WorldMapFragment.newInstance();
        } else if (id == R.id.nav_wiki) {
            fragment = WebViewFragment.newInstance("http://2007.runescape.wikia.com/wiki/2007scape_Wiki");
        } else if (id == R.id.nav_news) {
            fragment = NewsFragment.newInstance();
        } else if (id == R.id.nav_grand_exchange) {
            fragment = GrandExchangeSearchFragment.newInstance();
        } else if (id == R.id.nav_top_players) {
            fragment = CmlTopFragment.newInstance();
        } else if (id == R.id.nav_donate) {
            fragment = DonationFragment.newInstance();
        }

        if(fragment != null) {
            drawer.closeDrawer(GravityCompat.START);

            MainFragmentController.getInstance().showRootFragment(id, fragment);
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
        MainFragmentController.getInstance().showRootFragment(-1, ProfileFragment.newInstance(account.username));
        searchView.setQuery(null, false);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i(TAG, "onQueryTextSubmit: query=" + query);
        MainFragmentController.getInstance().showRootFragment(-1, ProfileFragment.newInstance(query));
        searchView.setQuery(null, false);
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Log.i(TAG, "onQueryTextChange: query=" + query);
        return false;
    }

    @Override
    public Cursor runQuery(CharSequence query) {
        Log.i(TAG, "runQuery: charSequence=" + query);

        Cursor c = DBController.searchAccountsByUsername(this, query);
        return c;
    }
}
