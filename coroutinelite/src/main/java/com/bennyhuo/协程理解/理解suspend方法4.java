package com.bennyhuo.协程理解;

/**
 * createBy keepon
 */
//如果上述功能改用协程, 将会是:

public class 理解suspend方法4 {
    static Object obj = new Object();
    static String mData = "";

    public static void main(String[] args) {
        String data = async2Sync();  // 数据是同步返回了, 但是线程也阻塞了

        System.out.println("data = " + data);
    }

    private static String async2Sync() {
        // 随便创建一个对象当成锁使用
        synchronized (obj) {
            requestDataAsync(new Callback() {
                @Override
                public void onSucces(String data) {
                    mData = data;
                    obj.notify(); // 通知所有的等待者
                }
            });

        }
        try {
            obj.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    interface Callback {
        void onSucces(String data);
    }
}






