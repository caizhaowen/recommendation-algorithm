package com.czw.recommend;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by caizhaowen on 17/8/22.
 */
public class Experiment {

    public  static  void run(double alpha,double beta,int steps, int topic ) throws FileNotFoundException {
        ReadData readData = new ReadData();
        readData.ReadFromRaw("/Users/caizhaowen/code/data/train.dat");
        Map<Integer,List<Integer>> movieToUser = readData.getMovieToUser();
        Map<Integer,List<Integer>> userToMovie = readData.getUserToMovie();
        int usize = readData.UserToMovieSize();
        int msize = readData.MovieToUserSize();

        ReadData readData1 = new ReadData();
        readData1.ReadFromRaw("/Users/caizhaowen/code/data/test.dat");

        Map<Integer, List<Integer>> testmovieToUser = readData1.getMovieToUser();
        Map<Integer,List<Integer>>  testuserToMovie = readData1.getUserToMovie();




        long startTime = System.currentTimeMillis();
        LFW lfw = new LFW();
        lfw.setUsize(usize);
        lfw.setMsize(msize);
        lfw.setAlpha(alpha);
        lfw.setBeta(beta);
        lfw.setSteps(steps);
        lfw.setTopic(topic);
        lfw.BuildUserItem(userToMovie,movieToUser);
        lfw.Predict(userToMovie);
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间1：" + (endTime - startTime) + "ms");

        long startTime2 = System.currentTimeMillis();
        EvaluateFun evaluate = new EvaluateFun();

        evaluate.Evaluate(msize,lfw,testuserToMovie);
        double pop = evaluate.Popularity(userToMovie,usize,lfw);
        System.out.println("popularity = " + pop);

        long endTime2 = System.currentTimeMillis();
        System.out.println("程序运行时间1：" + (endTime2 - startTime2) + "ms");



    }

    public static void main(String[] args) {
        try {
            run(0.02,0.01,15,100);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
