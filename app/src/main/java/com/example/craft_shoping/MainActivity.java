package com.example.craft_shoping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.DataInput;

import static android.view.View.GONE;
import static com.example.craft_shoping.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    public static DrawerLayout drawerLayout;
    private static final int HOME_FRAGMENT = 0;
    private static final int ORDER_FRAGMENT = 1;
    private static final int REWARD_FRAGMENT = 2;
    private static final int CART_FRAGMENT = 3;
    private static final int WISHLIST_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    private int currentfragment = -1;
    private TextView actionbarTitle;
    private Fragment fragment = null;
    private FrameLayout frameLayout;
    private Dialog signInDailog;
    private FirebaseUser currentuser;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionbarTitle = findViewById(R.id.actionbar_title);
        setSupportActionBar(toolbar);

        //Navigation Code is  HERE

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        //Now here is Drawer Code
        drawerLayout = findViewById(R.id.drawer);
        frameLayout = findViewById(R.id.framelayout_for_navigationdrawer);

        if (showCart) {
            drawerLayout.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            loadFragment(new MyMallFragment(), HOME_FRAGMENT);
        }

        signInDailog = new Dialog(MainActivity.this);
        signInDailog.setContentView(R.layout.sign_in_dialog);
        signInDailog.setCancelable(true);
        signInDailog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button dailogSignInbtn = signInDailog.findViewById(R.id.cancel_btn);
        Button dailogSignUpbtn = signInDailog.findViewById(R.id.ok_btn);
        final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

        dailogSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDailog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dailogSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                SignInFragment.disableCloseBtn = true;
                signInDailog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);

            }
        });

        //navigation open and close from this one code

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (currentuser != null) {
                    switch (item.getItemId()) {
                        case R.id.nav_mymall:
                            actionbarTitle.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                            loadFragment(new MyMallFragment(), HOME_FRAGMENT);
                            break;
                        case R.id.nav_myorder:
                            gotoFragment("My Order", new MyOrderFragment(), ORDER_FRAGMENT);
                            break;
                        case R.id.nav_myrewards:
                            gotoFragment("My Reward", new MyRewardsFragment(), REWARD_FRAGMENT);
                            break;
                        case R.id.nav_mycart:
                            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                            break;
                        case R.id.nav_mywishlist:
                            gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
                            break;
                        case R.id.nav_myaccount:
                            gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
                            break;
                        case R.id.nav_signout:
                            FirebaseAuth.getInstance().signOut();
                            Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);
                            startActivity(registerIntent);
                            finish();
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    signInDailog.show();
                    return false;
                }
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentuser= FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentfragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (currentfragment == HOME_FRAGMENT) {
                currentfragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {
                    actionbarTitle.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    loadFragment(new MyMallFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_notification_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentuser == null) {
                signInDailog.show();
            } else {
                gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
            }
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                showCart = false;
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        actionbarTitle.setVisibility(GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        invalidateOptionsMenu();
        loadFragment(fragment, fragmentNo);

        if (fragmentNo == CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    private void loadFragment(Fragment fragment, int fragmentNo) {
        if (fragmentNo != currentfragment) {
            currentfragment = fragmentNo;
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.framelayout_for_navigationdrawer, fragment).commit();
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentTransaction.addToBackStack(null);
        }
    }

    /*
    private void setFragment(Fragment fragment,int fragmentNo)
    {
        currentFragment=fragmentNo;
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }*/
}