package com.bennyhuo.mvp.java.other;

/**
 * createBy	 keepon
 */
public class Test  implements   IMvpViewJava2{

    IPresenterJava2 P;
    public void init(){
        P = createPresenter();
        //java 中的属性都是public static final 类型的，所以不能像kotlin那种各持有对方的对象引用
//        P.V = this;
    }

    private IPresenterJava2 createPresenter() {
        return null;
    }



}
