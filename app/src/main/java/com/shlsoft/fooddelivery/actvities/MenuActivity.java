package com.shlsoft.fooddelivery.actvities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.shlsoft.fooddelivery.R;
import com.shlsoft.fooddelivery.app.BaseActivity;
import com.shlsoft.fooddelivery.common.Common;
import com.shlsoft.fooddelivery.fragment.CartFragment;
import com.shlsoft.fooddelivery.fragment.MenuFragment;
import com.shlsoft.fooddelivery.fragment.OrderFragment;

public class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    //Name of the current user
    private TextView tv_username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Default fragment and default title when the page is launched
        setTitle(getString(R.string.taomlar));
        loadFragment(new MenuFragment());

        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        setUserName();
    }

    private void setUserName() {
        //Set username in navigation drawer
        View headerView = navigationView.getHeaderView(0);
        tv_username = headerView.findViewById(R.id.tv_username);
        tv_username.setText(getString(R.string.buyurtmachi) + Common.current_user.getName());
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chiqish)
                .setCancelable(false)
                .setMessage(R.string.chiqishni_xohlaysizmi)
                .setPositiveButton(R.string.xa, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.yoq, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu:
                setTitle(getString(R.string.taomlar));
                loadFragment(new MenuFragment());
                break;
            case R.id.nav_cart:
                setTitle(getString(R.string.xarid_kartasi));
                loadFragment(new CartFragment());
                break;
            case R.id.nav_orders:
                setTitle(getString(R.string.buyurtmalar));
                loadFragment(new OrderFragment());
                break;
            case R.id.nav_sign_out:
                // TODO: 27.04.2020 implement sign out
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}
