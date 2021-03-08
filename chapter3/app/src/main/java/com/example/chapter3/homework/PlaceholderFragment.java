package com.example.chapter3.homework;


import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView animationView2;
    private RecyclerView myRecycler;
    private Context mContext;

    private List list = new ArrayList();

    private static final String ARG_SECTION_NUMBER = "tab_number";

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        View root = inflater.inflate(R.layout.fragment_placeholder, container, false);
        animationView2 = root.findViewById(R.id.animation_view2);
        myRecycler = root.findViewById(R.id.recycler_view);
        mContext = container.getContext();
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化数组
        for (int i = 1; i < 101; i++) {
            list.add(String.format("这里是第 %d 行", i));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 播放动画
        animationView2.playAnimation();

        // 展示 recycler view
        myRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        myRecycler.setAdapter(new MyAdapter(list));
        myRecycler.setVisibility(View.GONE); //前5s并不显示不渲染

        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(animationView2,
                "alpha", 1f, 0f);//淡出效果，alpha从1到0
        fadeOutAnimator.setDuration(1000);// 淡出1s
//        fadeOutAnimator.setRepeatCount(0); // 设置动画重复播放次数 = 重放次数+1

        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(myRecycler,
                "alpha", 0f, 1f);
        fadeInAnimator.setDuration(1000);
//        fadeInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//           // 如果fadeInAnimator = ValueAnimator.ofInt(0,255) ，也可以使用这个
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int curValue = (int)animation.getAnimatedValue();
//                myRecycler.setAlpha((float)curValue/255);
//                Log.d("now alpha:" + myRecycler.getAlpha()," curValue:"+curValue);
//            }
//        });

        // 丢到一个动画集合里，一起运行
        final AnimatorSet fadeInOut = new AnimatorSet();
        fadeInOut.playTogether(fadeInAnimator,fadeOutAnimator);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                myRecycler.setAlpha(0f); //先设置透明度为0再显示
                myRecycler.setVisibility(View.VISIBLE);
                fadeInOut.start();
            }
        }, 5000);

        //  如果不需要等5s设置透明度，可以用这个delay
        //  fadeInOut.setStartDelay(5000);
        //  fadeInOut.start();

    }

}
