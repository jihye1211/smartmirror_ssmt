package org.techtown.smartmirror;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;


    private LoginButton btn_facebook_login;
    private LoginCallBack mLoginCallback;
    private CallbackManager mCallbackManager;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentIp fragmentIp = new FragmentIp();

    BottomNavigationView bottomNavigationView;

    FragmentIp fragment_ip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);




        Intent intent = new Intent(this, Loading.class);
        startActivity(intent);

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.btn_facebook_login);
        loginButton.setPermissions(Arrays.asList("public_profile", "email"));
        /*loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));*/
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("result", object.toString());

                        setContentView(R.layout.login_home);

                        bottomNavigationView = findViewById(R.id.bottomNavigationView);

                        // 프래그먼트 생성
                        fragment_ip = new FragmentIp();
                        // 다른 메뉴 3개 더 만들어야함.


                        //제일 처음 띄워줄 뷰를 세팅한다. commit();까지 해줘야 함.
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragmentIp).commitAllowingStateLoss();
                        // 현재는 제작한 메뉴가 frgmentIp만 있어서 Ip로 설정함
                        // main 으로 바꾸기

                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                            { switch (menuItem.getItemId()){
                                //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                                case R.id.ipItem:{ getSupportFragmentManager().beginTransaction() .replace(R.id.frameLayout,fragmentIp).commitAllowingStateLoss();
                                    return true; }
                /*case R.id.tab2:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment2).commitAllowingStateLoss();
                return true; }
                case R.id.tab3:{ getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment3).commitAllowingStateLoss();
                return true; }*/
                                default: return false;
                            } } });
                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr", error.toString());
            }


        });


        /*// 새로 추가한 코드
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentIp).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());*/





    }

    /*// 새로 추가한 코드
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        private MenuItem item;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.ipItem:
                    transaction.replace(R.id.frameLayout, fragmentIp).commitAllowingStateLoss();

                    break;
*//*                case R.id.voiceItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

                    break;
                case R.id.mainItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

                    break;
                case R.id.mypageItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

                    break;
                case R.id.devItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();

                    break;*//*
            }
            return true;
        }

    }

*/


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        Profile.getCurrentProfile();

    }





}