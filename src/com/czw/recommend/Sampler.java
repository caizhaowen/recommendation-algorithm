package com.czw.recommend;

/**
 * Created by caizhaowen on 17/8/22.
 */

import java.util.*;

public class Sampler {
    private  double [] weight;

    public  int sample()
    {
        Random rand = new Random();
        double tmp = rand.nextDouble();

        int low = 0 ;
        int high = weight.length -1;
        while (low <= high)
        {
            int middle = (low + high) / 2;
            if ( tmp == weight[middle])
                return  middle;
            if ((middle == 0) || (middle == (weight.length-1)))
                return  middle;
            if (tmp < weight[middle] && tmp > weight[middle-1])
                return middle;
            if (tmp > weight[middle] && tmp < weight[middle+1])
                return middle+1;
            if(tmp < weight[middle])
                high = middle - 1;
            else
                low = middle + 1;
        }
        return -1 ;
    }


    public void BuildPopularItem(Map<Integer, List<Integer>> movieToUser,int msize)
    {
        int bcount = 0;
        weight = new double[msize];

        for (Map.Entry<Integer,List<Integer>> entry : movieToUser.entrySet())
        {
            int key = entry.getKey();
            double value = entry.getValue().size()*1.0 ;
            weight[key] = value;
            bcount ++ ;
        }
    }

    public  void BuildPopularItemTest(int msize)
    {
        int bcount = 0 ;
        weight = new double[msize];
        for (int i=0 ; i <weight.length; i++)
        {
            weight[i] = 1 ;
            bcount ++ ;
        }
    }


    public void init_prob (Map<Integer,List<Integer>> movieToUser,int msize)
    {
        BuildPopularItem(movieToUser,msize);
        double sum = 0;
        for (int i = 0; i < weight.length ; i++)
        {
            sum += weight[i];
        }
        double tmp = 0;
        for (int i = 0 ; i < weight.length ; i++)
        {
            weight[i] = weight[i]/sum ;
            if (i > 0)
                weight[i] += weight[i-1];
        }
    }
}
