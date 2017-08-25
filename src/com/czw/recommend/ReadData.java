package com.czw.recommend;

/**
 * Created by caizhaowen on 17/8/23.
 */
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;


public class ReadData {
    public Map<Integer, List<Integer>> getMovieToUser() {
        return movieToUser;
    }

    public Map<Integer, List<Integer>> getUserToMovie() {
        return userToMovie;
    }


    public  void ReadFromRaw(String filename) throws FileNotFoundException
    {
        movieToUser = new HashMap<>();
        userToMovie = new HashMap<>();

        Scanner in = new Scanner(new File(filename));

        String line = null;
        List<Integer> users = null;
        List<Integer> movies = null;
        mbigsize = 0;
        Integer temp = -1 ;
        ubigsize = 0;


        while (in.hasNextLine())
        {
            line = in.nextLine();
            String [] lines = line.split("::");
            Integer uid = Integer.parseInt(lines[0])-1;
            Integer mid = Integer.parseInt(lines[1])-1;

            if( ! temp.equals(Integer.parseInt(lines[0])) )
            {
                movies = new ArrayList<>();
                movies.add(mid);
                userToMovie.put(uid , movies);
            }
            else
            {
                movies.add(mid);
            }
            temp = Integer.parseInt(lines[0]);
            if(temp >= ubigsize )
                ubigsize=temp;


            if ( movieToUser.containsKey(mid) )
            {
                movieToUser.get(mid).add(uid);
                //users.add(uid); 错误,该users已经无法确定是属于哪个mid的了.
            }
            else
            {
                users=new ArrayList<>();
                users.add(uid);
                movieToUser.put(mid , users);
            }
            if (mid >= mbigsize )
                mbigsize = mid+1;
        }
    }

    public int MovieToUserSize()
    {
        return mbigsize;
    }

    public int UserToMovieSize()
    {
        //return UserToMovie.size();
        return ubigsize;
    }
    public double[][] getR() {
        return R;
    }

    private Map<Integer,List<Integer>> movieToUser;
    private  Integer mbigsize;

    private Map<Integer,List<Integer>> userToMovie;
    private  Integer ubigsize;

    private double [][] R;


}
