import java.util.ArrayList;
import java.util.List;

public class bicluster {
    bicluster left, right;
    List<Double> vec = new ArrayList<Double>();
    double distance;
    int id;

    bicluster(List<Double> vec,double distance, bicluster left, bicluster right,int id)
    {
        this.vec = vec;
        this.distance = distance;
        this.left = left;
        this.right = right;
        this.id = id;
    }
    bicluster(List<Double> vec,int id)
    {
        this.vec = vec;
        this.id = id;
        left = null;
        right = null;
        distance = 0;
    }
}
