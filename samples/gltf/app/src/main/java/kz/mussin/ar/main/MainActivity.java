package kz.mussin.ar.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.ar.sceneform.samples.gltf.R;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

import kz.mussin.ar.main.fragments.FavouriteFragment;
import kz.mussin.ar.main.fragments.InfoFragment;
import kz.mussin.ar.main.fragments.MainFragment;
import kz.mussin.ar.main.objects.Toy;
import kz.mussin.ar.sceneform.samples.gltf.GltfActivity;


public class MainActivity extends AppCompatActivity {
    private BubbleNavigationLinearView bubbleNavigation;
    private ViewParent viewPager;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment mainFragment = new MainFragment();
    private Fragment accountFragment = new FavouriteFragment();
    private Fragment infoFragment = new InfoFragment();
    private Fragment current = mainFragment;
    private SharedPreferences localStorage;
    private SharedPreferences.Editor localStorageEditor;
    private String LOCAL_STORAGE_NAME = "LocalStorage";
    private String FAVOURITE_TOYS = "favouriteToys";
    private  ArrayList<String> favouriteToys;
    private Toy currentToy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        initLocalStorage();
        initBubble();
    }
//    public void goToInfo(){
//        fragmentManager.beginTransaction().hide(current).show(infoFragment).commit();
//        current = infoFragment;
//    }
    public void goToAR(){

        startActivity(new Intent(MainActivity.this, GltfActivity.class)
                .putExtra("modelUrl",
                        currentToy.gltf));
    }
    private void initLocalStorage(){
        localStorage = getSharedPreferences(LOCAL_STORAGE_NAME, MODE_PRIVATE);
        localStorageEditor = localStorage.edit();
        favouriteToys = new ArrayList<>();
        String idsString = readLocal(FAVOURITE_TOYS);
        String[] ids = idsString.split(",");
        favouriteToys = new ArrayList<>();
        for (String id: ids) {
            Log.e("Id",id);
            favouriteToys.add(id);
        }
    }
    public void addToyToFavourite(String id){
        favouriteToys.add(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            writeToLocal(FAVOURITE_TOYS, String.join(",",favouriteToys));
        }
    }
    public ArrayList<String> getFavouriteToyIds(){
        return favouriteToys;
    }

    public boolean writeToLocal(String key, String value) {
        localStorageEditor.remove(key);
        localStorageEditor.putString(key, value);
        return localStorageEditor.commit();
    }
    public String readLocal(String key){
        return localStorage.getString(key,"");
    }
    private void initBubble(){
        fragmentManager.beginTransaction().add(R.id.main_container, infoFragment, "3").hide(infoFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, accountFragment, "2").hide(accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, mainFragment, "2").commit();
        bubbleNavigation = findViewById(R.id.equal_navigation_bar);
        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                switch (position){
                    case 0:
                        fragmentManager.beginTransaction().hide(current).show(mainFragment).commit();
                        current = mainFragment;
                        return;
                    case 1:
                        fragmentManager.beginTransaction().remove(accountFragment).commit();
                        accountFragment = new FavouriteFragment();
                        fragmentManager.beginTransaction().add(R.id.main_container, accountFragment, "2").hide(accountFragment).commit();
                        fragmentManager.beginTransaction().hide(current).show(accountFragment).commit();
                        current = accountFragment;
                        return;
                    default:
                        return;
                }
            }
        });
    }
    public Toy getCurrentToy(){
        return currentToy;
    }public void setCurrentToy(Toy toy){
        this.currentToy = toy;
    }
}