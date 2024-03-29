package com.bennyhuo.experimental.coroutines;

import javax.annotation.Nullable;

public class Result<T> {  //java没有可空和不可空一说

    private T value;
    private Throwable error;

    public T getValue() {
        return value;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    //RepoDetailActivity的 val (subscriptionResponse, error) 用到这个
    public T component1() {
        return value;
    }

    @Nullable
    public Throwable component2() {
        return error;
    }

    public static <T> Result<T> of(Throwable error){
        Result<T> result = new Result<>();
        result.error = error;
        return result;
    }

    public static <T> Result<T> of(T value){
        Result<T> result = new Result<>();
        result.value = value;
        return result;
    }
}
