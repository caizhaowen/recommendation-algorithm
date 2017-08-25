package com.czw.recommend;

/**
 * Created by caizhaowen on 17/8/24.
 */
import  java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EvaluateFun {

    private Map<Integer,Double> rank;

    private  int K = 9;




    //testUserToMovie是测试集合数据
    public double Evaluate (int msize, Recommendation run, Map<Integer,List<Integer>> testUserToMovie)
    {
        int hit = 0;
        int all1 = 0;
        int all2 = 0;
        int reCommend=0;
        Set<Integer> reCommendItem = new HashSet<>();

        for(Map.Entry<Integer,List<Integer>> tum : testUserToMovie.entrySet())
        {
            List<Integer> tu = tum.getValue();

            rank = run.Recommend(K,tum.getKey());

            int num = 0;

            if (tu != null) {
                for (int j = 0; j < tu.size(); j++) {
                    Integer key = tu.get(j);
                    if (rank.containsKey(key)) {
                        hit += 1;
                    }
                }
                all1 += tu.size();
                all2 += rank.size();

                for (Map.Entry<Integer,Double> urank : rank.entrySet())
                {
                    reCommendItem.add(urank.getKey());
                }
            }
        }
        reCommend = reCommendItem.size();
        System.out.println("hit = " + hit);
        System.out.println("all1 = " + all1);
        System.out.println("all2 = " + all2);
        System.out.println("recall = " + hit/(all1*1.0));
        System.out.println("precision = " + hit/(all2*1.0));
        System.out.println("coverage = " + reCommend/(msize * 1.0));
        return 0;
    }

    //计算物品的平均流行度来作为评测推荐的新颖度指标
    //对流行度取对数,是因为流行度成长尾分布,对数后,平均值更加稳定
    //usize用训练集合
    //userToMovie用训练集合即可
    public double Popularity(Map<Integer,List<Integer>> userToMovie,int usize,Recommendation run)
    {
        Map<Integer,Integer> moviePopularity = new HashMap<>();
        for(Map.Entry<Integer,List<Integer>> usm : userToMovie.entrySet())
        {
            List<Integer> movies = usm.getValue();
            for (int i = 0; i < movies.size(); i++)
            {
                Integer key = movies.get(i);
                if ( ! moviePopularity.containsKey(key))
                {
                    moviePopularity.put(key,0);
                }
                moviePopularity.put(key,moviePopularity.get(key)+1);
            }
        }
        double ret = 0;
        int n = 0;
        for (int j = 0 ;j < usize;j++)
        {
            rank = run.Recommend(K,j);

            for (Map.Entry<Integer,Double> weight : rank.entrySet())
            {
                Integer key = weight.getKey();
                if (moviePopularity.containsKey(key)) {
                    ret += Math.log(1 + moviePopularity.get(key));
                    n += 1;
                }
            }
        }
        return  ret / n ;

    }
}
