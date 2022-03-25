package kz.mussin.oiynshyq.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewParent;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.ar.sceneform.samples.gltf.R;

import kz.mussin.oiynshyq.main.fragments.AccountFragment;
import kz.mussin.oiynshyq.main.fragments.InfoFragment;
import kz.mussin.oiynshyq.main.fragments.MainFragment;
import kz.mussin.oiynshyq.main.objects.Toy;

public class MainActivity extends AppCompatActivity {
    private BubbleNavigationLinearView bubbleNavigation;
    private ViewParent viewPager;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment mainFragment = new MainFragment();
    private Fragment accountFragment = new AccountFragment();
    private Fragment infoFragment = new InfoFragment();
    private Fragment current = mainFragment;

    private Toy currentToy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBubble();
    }
    public void goToInfo(){
        fragmentManager.beginTransaction().hide(current).show(infoFragment).commit();
        current = infoFragment;
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