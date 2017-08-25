package com.czw.recommend;

/**
 * Created by caizhaowen on 17/8/22.
 */


import java.util.*;

public interface Recommendation {
    void Predict(Map<Integer,List<Integer>> traindata);

    Map<Integer,Double> Recommend(int K,int user);




}
