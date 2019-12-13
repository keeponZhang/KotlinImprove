package com.bennyhuo.协程理解;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bennyhuo.coroutines.lite.R;

/**
 * createBy keepon
 */
public class 理解suspend方法4Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);

    }

    static Object obj = new Object();
    static String mData = "";

    private static String async2Sync() {
        // 随便创建一个对象当成锁使用
        synchronized (obj) {
            requestDataAsync(new Callback() {
                @Override
                public void onSucces(String data) {
                    Log.e("TAG", "理解suspend方法4Activity onSucces:"+mData);
                    mData = data;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            obj.notifyAll(); // 通知所有的等待者
                        }
                    });
                }
            });
            Log.e("TAG", "理解suspend方法4Activity async2Sync:");
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return mData;
    }

    public static void requestDataAsync(final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("获取数据");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("返回的数据3");
                callback.onSucces("返回的数据3");
            }
        }.start();
    }

    public void test(View view) {
        String data = async2Sync();  // 数据是同步返回了, 但是线程也阻塞了

        Log.e("TAG", "理解suspend方法4Activity test:data  " + data);
        System.out.println();
    }

    interface Callback {
        void onSucces(String data);
    }
}
