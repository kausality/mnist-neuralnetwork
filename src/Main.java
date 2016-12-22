/**
 * Created by kaustubh on 11/11/16.
 */
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<List<Data>> data;
        Loader loader = new Loader();
        data = loader.loadAllData();
        List<Data> trainingData = data.get(0);
        List<Data> testData = data.get(1);
        System.out.println("Finished loading Data");
        int[] sizes = {784, 30, 10};
        Network n = new Network(sizes);
        n.SGD(trainingData, testData, 100, 30, 0.5);
    }
}
