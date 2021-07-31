package com.example.craft_shoping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static com.example.craft_shoping.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity {

    private static final int HOME_FRAGMENT = 0;
    private static final int ORDER_FRAGMENT = 1;
    private static final int REWARD_FRAGMENT = 2;
    private static final int CART_FRAGMENT = 3;
    private static final int WISHLIST_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static DrawerLayout drawerLayout;
    public static Boolean showCart = false;
    public static Activity mainActivity;
    public static boolean resetMainActivity = false;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private int currentfragment = -1;
    private TextView actionbarTitle;
    private Fragment fragment = null;
    private FrameLayout frameLayout;
    private Dialog signInDailog;
    private FirebaseUser currentuser;
    private TextView badgecount;
    private Window window;
    private CircleImageView profileView;
    private TextView fullname, email;
    private ImageView addProfileIcon;
//    private AppBarLayout.LayoutParams params;
//    private int scrollFlags;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        actionbarTitle = findViewById(R.id.actionbar_title);
        drawerLayout = findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

//        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
//        scrollFlags=params.getScrollFlags();
        //Navigation Code is  HERE

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        //Now here is Drawer Code
        frameLayout = findViewById(R.id.framelayout_for_navigationdrawer);

        profileView = navigationView.getHeaderView(0).findViewById(R.id.current_order_pic);
        fullname = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        email = navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        addProfileIcon = navigationView.getHeaderView(0).findViewById(R.id.add_profile_icon);

        if (showCart) {
            mainActivity = this;
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
                            DBqueries.clearData();
                            Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
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
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser == null) {
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(false);
        } else {
            if (DBqueries.email == null) {
                FirebaseFirestore.getInstance().collection("USERS").document(currentuser.getUid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DBqueries.fullname = task.getResult().getString("full_name");
                            DBqueries.email = task.getResult().getString("email");
                            DBqueries.profile = task.getResult().getString("profile");
                            fullname.setText(DBqueries.fullname);
                            email.setText(DBqueries.email);
                            if (DBqueries.profile.equals("")) {
                                addProfileIcon.setVisibility(View.VISIBLE);
                            } else {
                                addProfileIcon.setVisibility(View.INVISIBLE);
                                Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.profile)).into(profileView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                fullname.setText(DBqueries.fullname);
                email.setText(DBqueries.email);
                if (DBqueries.profile.equals("")) {
                    profileView.setImageResource(R.mipmap.profile);
                    addProfileIcon.setVisibility(View.VISIBLE);
                } else {
                    addProfileIcon.setVisibility(View.INVISIBLE);
                    Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.mipmap.profile)).into(profileView);
                }
            }
            navigationView.getMenu().getItem(navigationView.getMenu().size() - 1).setEnabled(true);
        }
        if (resetMainActivity) {
            resetMainActivity = false;
            actionbarTitle.setVisibility(View.VISIBLE);
            loadFragment(new MyMallFragment(), HOME_FRAGMENT);
            navigationView.getMenu().getItem(0).setChecked(true);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DBqueries.checkNotification(true,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentfragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);

            MenuItem cartItem = menu.findItem(R.id.main_cart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.ic_baseline_shopping_cart_white_24);
            badgecount = cartItem.getActionView().findViewById(R.id.badge_count);
            if (currentuser != null) {
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this), false, badgecount, new TextView(MainActivity.this));
                } else {
                    badgecount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartList.size() < 99) {
                        badgecount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgecount.setText("99+");
                    }
                }
            }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentuser == null) {
                        signInDailog.show();
                    } else {
                        gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
                    }
                }
            });

            MenuItem notifyItem = menu.findItem(R.id.main_notification_icon);
            notifyItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notifyItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.drawable.ic_baseline_notifications_24);
            TextView notifyCount = notifyItem.getActionView().findViewById(R.id.badge_count);
            if (currentuser != null){
                DBqueries.checkNotification(false,notifyCount);
            }
            notifyItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent=new Intent(MainActivity.this,NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });
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
                    mainActivity = null;
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
            Intent searchIntent=new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        } else if (id == R.id.main_notification_icon) {
            Intent notificationIntent=new Intent(this,NotificationActivity.class);
            startActivity(notificationIntent);
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
                mainActivity = null;
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