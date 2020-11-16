package org.techtown.smartmirror;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
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

    // 안드로이드 소켓 설정
    Socket socket = null;
    TextView recieveText;
    Button connectBtn;
    EditText editTextAddress, editTextPort, messageText;

    BottomNavigationView bottomNavigationView;

    FragmentIp fragment_ip;
    FragmentDev fragment_dev;
    FragmentHome fragment_home;
    FragmentMain fragment_main;
    voiceFragment voiceFragment;



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

                        FloatingActionButton fab = findViewById(R.id.fab);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                                // ip 소캣
                                /*VoiceTask voiceTask = new VoiceTask();
                                voiceTask.execute();*/
                            }
                        });

                        bottomNavigationView = findViewById(R.id.bottomNavigationView);

                        // 프래그먼트 생성
                        fragment_ip = new FragmentIp();
                        fragment_dev = new FragmentDev();
                        fragment_home = new FragmentHome();
                        fragment_main = new FragmentMain();
                        voiceFragment = new voiceFragment();
                        // 다른 메뉴 3개 더 만들어야함.


                        //제일 처음 띄워줄 뷰를 세팅한다. commit();까지 해줘야 함.
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_main).commitAllowingStateLoss();
                        // 현재는 제작한 메뉴가 frgmentIp만 있어서 Ip로 설정함
                        // main 으로 바꾸기

                        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    //menu_bottom.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                                    case R.id.mainItem: {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_main).commitAllowingStateLoss();
                                        return true;
                                    }
                                    case R.id.ipItem: {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentIp).commitAllowingStateLoss();
                                        return true;
                                    }
                                    case R.id.devItem: {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_dev).commitAllowingStateLoss();
                                        return true;
                                    }
                                    case R.id.mypageItem: {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment_home).commitAllowingStateLoss();
                                        return true;
                                    }
                                    case R.id.voiceItem: {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, voiceFragment).commitAllowingStateLoss();
                                        return true;
                                    }

                                    default:
                                        return false;
                                }
                            }
                        });
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
/*
        // connect 설정
        connectBtn = (Button) findViewById(R.id.btn_setIP);
        recieveText = (TextView) findViewById(R.id.et_ip);
        // textViewReciev 에서 기존 ip xml 아이디인 et_ip로 수정함
        // connect 버튼 클릭(send)
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask(editTextAddress.getText().toString(), Integer.parseInt(editTextPort.getText().toString()), messageText.getText().toString());
                myClientTask.execute();
                //messageText.setText("");
            }
        });*/



        /*// 새로 추가한 코드
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentIp).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());*/


    }

    /*public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";
        String myMessage = "";

        //constructor
        MyClientTask(String addr, int port, String message){
            dstAddress = addr;
            dstPort = port;
            myMessage = message;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            myMessage = myMessage.toString();
            try {
                socket = new Socket(dstAddress, dstPort);
                //송신
                OutputStream out = socket.getOutputStream();
                out.write(myMessage.getBytes());

                //수신
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();
                *//*
                 * notice:
                 * inputStream.read() will block if no data return
                 *//*
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response += byteArrayOutputStream.toString("UTF-8");
                }
                response = "서버의 응답: " + response;

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            recieveText.setText(response);
            super.onPostExecute(result);
        }
    }

    public class VoiceTask extends AsyncTask<String, Integer, String> {
        String str = null;

        @Override
        protected String doInBackground(String... strings) {
            try {
                getVoice();
            } catch (Exception e) {

            }
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

            } catch (Exception e) {
                Log.d("OnActivityResult", "getImageURL exception");
            }
        }
    }*/

    private void getVoice() {
        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        String language = "ko-KR";

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        startActivityForResult(intent, 2);

    }

    public class NotiListenService extends NotificationListenerService {


        private String smartmirror_ip; // 스마트 미러의 할당받은 ip 주소


        /* 환경 변수 */

        private SharedPreferences prefs;


        public NotiListenService() {

        }


        @Override

        public void onNotificationPosted(StatusBarNotification sbn) {

            //super.onNotificationPosted(sbn);

            /**
             * 보안 / 알림 액세스 / 알림 설정해야함!

             */

            Notification noti = sbn.getNotification();

            Bundle bundle = noti.extras;


            prefs = getSharedPreferences("login", 0);

            smartmirror_ip = prefs.getString("SMARTMIRROR_IP", "0.0.0.0");

            Log.d("smartmirror IP : ", smartmirror_ip);


            /**
             * strPacakge : 패캐지 이름
             * title : 이름, 사용자, 전화번호 등...
             * text : 내용 */

            String strPackage = sbn.getPackageName();

            String title = bundle.getString(Notification.EXTRA_TITLE);

            CharSequence text = bundle.getCharSequence(Notification.EXTRA_TEXT);


            if (title == null) title = "";

            if (text == null) text = "";


            Log.d("[noti] get :", "package : " + strPackage + " // title : " + title + " // text : " + text);


            /* 페이스북, 구글 메일, 카카오톡, 전화, 문자 패키지만 라즈베리파이 알림으로 전송 */


            if (strPackage.equals("com.facebook.orca") || strPackage.equals("com.facebook.katana") || strPackage.equals("com.android.mms")

                    || strPackage.equals("com.kakao.talk") || strPackage.equals("com.google.android.gm") || strPackage.equals("com.lge.ltecall")) {


                /** 라즈베리 파이2 전송하는 부분 */

                try {

                    String enPackage = URLEncoder.encode(strPackage, "UTF-8");

                    String enTitle = URLEncoder.encode(title, "UTF-8");

                    String enText = URLEncoder.encode(text.toString(), "UTF-8");

                    /* 라즈베리 파이 IP : 9090 (port) */

                    String urlRasp = "http://" + smartmirror_ip + ":9090/noti.do?package=" + enPackage + "&title=" + enTitle + "&text=" + enText;

                    Log.d("smartmirror url : ", urlRasp);

                    URL url = new URL(urlRasp);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


                    String response = null;


                    while (true) {

                        response = br.readLine();

                        if (response == null) break;

                        Log.d("[noti] response :", response);

                    }


                    urlConnection.disconnect();


                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        Profile.getCurrentProfile();

    }
}


    