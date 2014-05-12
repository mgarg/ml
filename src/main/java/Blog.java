import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Blog {

    List<String> colnames = new ArrayList<String>();
    List<String> rownames = new ArrayList<String>();
    List<List<Double>> data = new ArrayList<List<Double>>();

    void printclust(bicluster clust,int n)
    {
        for (int i =0;i<n;i++)
            System.out.print(" ");
        if (clust.id < 0)
            System.out.print("-");
        else
            System.out.println(rownames.get(clust.id));
        if(clust.left!=null) printclust(clust.left,n+1);
        if(clust.right!=null) printclust(clust.right,n+1);

    }
    List<Double> merge(List<Double> v1, List<Double> v2)
    {
        List<Double> newList = new ArrayList<Double>();
        for (int i=0;i<v1.size();i++)
            newList.add((v1.get(i)+v2.get(i))/2);
        return newList;
    }
    bicluster hcluster()
    {
        int currentclusterid = -1;
        List<bicluster> clust = new ArrayList<bicluster>();
        for (int i=0;i<data.size();i++)
            clust.add(new bicluster(data.get(i),i));

        while (clust.size()>1)
        {

            Pair lowestPair = new Pair(0,1);
            double closest = pearson(clust.get(0).vec,clust.get(1).vec);

            Map<Pair,Double> distances = new HashMap<Pair, Double>();

            for (int i= 0;i<clust.size()-1;i++)
            {
                for (int j =i+1;j<clust.size();j++)
                    distances.put(new Pair(i,j),pearson(clust.get(i).vec,clust.get(j).vec));
            }

            for (Map.Entry<Pair,Double> d: distances.entrySet())
            {
                if(d.getValue()<closest)
                {
                    closest = d.getValue();
                    lowestPair = d.getKey();
                }

            }

            List<Double> mergeVec = merge(data.get(lowestPair.a),data.get(lowestPair.b));
            bicluster newcluster = new bicluster(mergeVec,closest,clust.get(lowestPair.a),clust.get(lowestPair.b),currentclusterid);
            currentclusterid --;

            clust.remove(lowestPair.b);

            clust.remove(lowestPair.a);
            clust.add(newcluster);
        }

        return clust.get(0);
    }
    double sum(List<Double> ls)
    {
        double s = 0;
        for (double d: ls)
            s += d;
        return s;
    }

    List<Double> powerList(List<Double> ls)
    {
        List<Double> newList = new ArrayList<Double>();
        for (double d:ls)
            newList.add(Math.pow(d,2));
        return newList;
    }

    List<Double> productList(List<Double> v1,List<Double> v2)
    {
        List<Double> newList = new ArrayList<Double>();
        for (int i =0;i<v1.size();i++)
            newList.add(v1.get(i)*v2.get(i));
        return newList;
    }

    double pearson(List<Double> v1,List<Double> v2)
    {
        assert v1.size() > 0;
        assert v2.size()>0;

        double sum1 = sum(v1);
        double sum2 = sum(v2);

        double sum1Sq = sum(powerList(v1));
        double sum2Sq = sum(powerList(v2));

        double pSum = sum(productList(v1, v2));

        double num = pSum-(sum1*sum2/v1.size());
        double den = Math.sqrt((sum1Sq-Math.pow(sum1,2)/v1.size())*(sum2Sq-Math.pow(sum2,2)/v2.size()));
        return 1-num/den;

    }

    void readFile(String filename) throws Exception
    {

        BufferedReader in = new BufferedReader(new FileReader(filename));
        String line;
        line = in.readLine();
        String cols[] = line.split(",");
        for(String cs :cols)
                colnames.add(cs);

        while ((line = in.readLine()) != null)
        {
            String temp[] = line.split(",");
            List<Double> rowdata = new ArrayList<Double>();
            rownames.add(temp[0]);
            for(int i =1;i< temp.length;i++)
                rowdata.add(Double.parseDouble(temp[i]));
            data.add(rowdata);

        }

    }
    public static void main(String[] args)throws Exception
    {
        Blog blog = new Blog();
        blog.readFile("/home/mahak/IdeaProjects/collectiveIntelligence/src/blogdata.txt");
//        for(String cols:blog.colnames)
//            System.out.print(cols+" ");
//        for(String rows:blog.rownames)
//            System.out.print(rows+" ");
//        for(List<Double> ls: blog.data)
//        {
//            for (double d: ls)
//                System.out.print(d);
//        }
        blog.printclust(blog.hcluster(),0);

    }
}
