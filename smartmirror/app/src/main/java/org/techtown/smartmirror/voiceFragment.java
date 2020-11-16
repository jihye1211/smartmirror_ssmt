package org.techtown.smartmirror;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class voiceFragment extends Fragment {
    ViewGroup viewGroup;
    @Nullable


    /* 환경 변수 */
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.voice_fragment, container, false);


        return viewGroup;
        // 김정출 코드에서는 rootView로 변수명이 정의되어있음. rootView=>viewGroup


    }
}