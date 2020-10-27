package org.techtown.smartmirror;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentIp extends Fragment {
    ViewGroup viewGroup;
    @Nullable

    private EditText et_ip;
    private TextView tv_currentIP;
    private Button btn_setIP;
    private String current_ip; // 환경변수에 저장된 ip주소
    private String smartmirror_ip; // 스마트 미러의 할당받은 ip 주소

    /* 환경 변수 */
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_ip,container,false);
        tv_currentIP = (TextView) viewGroup.findViewById(R.id.tv_currentIP);
        et_ip = (EditText) viewGroup.findViewById(R.id.et_ip);
        btn_setIP = (Button) viewGroup.findViewById(R.id.btn_setIP);
        prefs = getActivity().getSharedPreferences("login", 0);
        editor = prefs.edit();
        current_ip = prefs.getString("SMARTMIRROR_IP", "0.0.0.0");
        tv_currentIP.setText(current_ip);
        btn_setIP.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                smartmirror_ip = et_ip.getText().toString();
                editor.putString("SMARTMIRROR_IP", smartmirror_ip);
                editor.commit();
                Toast.makeText(getActivity(), "IP가" + smartmirror_ip + "로 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return viewGroup;
        // 김정출 코드에서는 rootView로 변수명이 정의되어있음. rootView=>viewGroup



    }
}