package com.czw.recommend;

import java.util.*;

/**
 * Created by caizhaowen on 17/8/22.
 */
public class LFW implements Recommendation {
    private double bu;
    public void InitModel()
    {
        P = new double[usize][topic];
        Q = new double[msize][topic];
        Random rand = new Random();
        double f = 1.0/Math.sqrt(topic);

        for (int i = 0 ; i < usize; i++)
        {
            for (int j = 0 ;j < topic; j++)
            {
                P[i][j] = f * (rand.nextDouble() - 0.5);
            }
        }

        for (int i = 0 ; i < msize ;i++)
        {
            for (int j = 0 ; j < topic ; j++)
            {
                Q[i][j] = f * (rand.nextDouble() - 0.5);
            }
        }

        /*
        for (int i = 0 ; i < topic ;i++)
        {
            for (int j = 0 ;j < msize ;j++)
            {
                Q[i][j] = f * (rand.nextDouble() - 0.5);
            }
        }*/
        bu = 0;
    }

    public  void BuildUserItem(Map<Integer, List<Integer>> userToMovie,Map<Integer,List<Integer>> movieToUser)
    {
        Random rand = new Random();
        R = new double[usize][msize];
        for (int i = 0 ; i < usize ;i++)
        {
            for (int j = 0; j < msize ;j++)
            {
                R[i][j] = - 1.0;
            }
        }
        Sampler sp = new Sampler();
        sp.init_prob(movieToUser,msize);
        for (Map.Entry<Integer,List<Integer>> entry : userToMovie.entrySet())
        {
            Integer key = entry.getKey(); //user
            List<Integer> value = entry.getValue(); //movies
            int size = value.size();
            int count = 0;
            for (int i = 0; i < size ; i++)
            {
                int col = value.get(i);
                R[key][col] = 1.0 ;
                count += 1;
            }

            int count2 = 0;
            while (true) {
                int item = sp.sample();
                if (value.contains(item)){
                    continue;
                }
                R[key][item] = 0.0 ;
                //R[key][item] = -1.0;
                count2 ++ ;

                if (count2 /count == 10 )
                    break;
            }
        }


    }

    @Override
    public void Predict(Map<Integer, List<Integer>> traindata) {
        W = new double[usize][msize];

        InitModel();
        for (int step = 0 ; step < steps ;step++)
        {
            double loss = 0;
            double count = 0;
            for (int i = 0 ; i < usize ; i++)
            {
                for (int j = 0 ;j < msize ;j++)
                {
                    if (R[i][j] >= 0)
                    {
                        double label = R[i][j];
                        double pred = 0;
                        for (int k = 0; k < topic ; k++) {
                            pred += P[i][k] * Q[j][k];
                        }
                        pred += bu;
                        double error = label - pred;
                        loss += Math.pow(R[i][j]-pred, 2);
                        count++;
                        for(int k = 0 ; k < topic ;k++)
                        {
                            double t = P[i][k];
                            P[i][k] += alpha * (error * Q[j][k] - beta * P[i][k]);
                            Q[j][k] += alpha * (error * t - beta * Q[j][k]);
                        }
                    }
                }
            }
            //alpha *= 0.9;
            System.out.println("loss = " + loss/(count+1e-3));
            System.out.println("count = " + count);
            System.out.println("bu = " + bu);
        }
        for (int i = 0; i < usize;i++)
        {
            for (int j = 0 ; j < msize ;j++)
            {
                W[i][j] = 0;
                for (int k = 0; k < topic; k++)
                {
                    W[i][j] += P[i][k] * Q[j][k];
                }
            }
        }

    }

    @Override
    public Map<Integer, Double> Recommend(int K, int user) {
        Map<Integer,Double> movieWeight = new HashMap<>();
        double [] temp = Arrays.copyOf(W[user],W[user].length);
        for (int i = 0 ; i < temp.length ; i++)
        {
            if (R[user][i] > 0)
            {
                temp[i] = 0.0;
            }
        }
        //从大到小排序
        Arrays.sort(temp);
        for (int i = 0; i < temp.length/2 ; i++)
        {
            double tmp = temp[i];
            temp[i] = temp[temp.length - (i + 1)];
            temp[temp.length - (i+1)] = tmp;
        }
        double ktemp = temp[K - 1];

        for (int i = 0 ; i < W[user].length ; i++)
        {
            if (R[user][i] > 0)
                continue;
            if (W[user][i] - ktemp >= 0)
                movieWeight.put(i,W[user][i]);
        }
        return  movieWeight;
    }

    //用户的数量
    private int usize;
    //item的数量
    private int msize;
    public void setUsize(int usize) {
        this.usize = usize;
    }

    public void setMsize(int msize) {
        this.msize = msize;
    }

    private double[][] P;
    private double[][] Q;
    private double[][] R;
    private double[][] W;

    private int topic;
    private int steps;
    private double alpha;
    private double beta;

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
}
