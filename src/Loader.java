/**
 * Created by kaustubh on 29/11/16.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Loader {
    /* Load the image files separately as training and test set. */
    static String trainingFile = "sets/train-images";
    static String trainingLabel = "sets/train-labels";
    static String testFile = "sets/test-images";
    static String testLabel = "sets/test-labels";

    int readInt(InputStream in) throws IOException{
        // Data is stored in high endian format so make it low endian.
        int d;
        int[] b = new int[4];
        for(int i = 0; i < 4; i++)
            b[i] = in.read();
        d = b[3] | b[2] << 8 | b[1] << 16 | b[0] << 24;
        return d;
    }

    List<Data> readDataFiles(String imageFile, String labelFile) throws IOException{
        List<Data> dataList = new ArrayList<>();
        int[] imageData;
        int[] labelData;
        int totalRows, totalCols, totalImages, totalLabels;
        try(InputStream in = new FileInputStream(imageFile)){
            int magic = readInt(in);
            totalImages = readInt(in);
            totalRows = readInt(in);
            totalCols = readInt(in);
            //System.out.println("Magic number: " + magic);
            //System.out.println("Images: " + totalImages);
            //System.out.println("Rows: " + rows);
            //System.out.println("Cols: " + cols);
            imageData = new int[totalImages * totalRows * totalCols];
            for(int i = 0; i < totalImages * totalRows * totalCols; i++)
                imageData[i] = in.read();
        }

        try(InputStream in = new FileInputStream(labelFile)){
            int magic = readInt(in);
            totalLabels = readInt(in);
            //System.out.println("Magic number: " + magic);
            //System.out.println("Items: " + totalItems);
            labelData = new int[totalLabels];
            for(int i = 0; i < totalLabels; i++)
                labelData[i] = in.read();
        }
        if (totalImages != totalLabels) // file corrupted
            return null;
        int ic = 0; //image data index counter
        int lc = 0; //label data index counter
        while(ic < imageData.length && lc < labelData.length){
            Matrix input, result;
            input = new Matrix(totalRows * totalCols, 1);
            for(int i = 0; i < totalRows * totalCols; i++)
                input.set(i, 0, imageData[ic++]);
            result = new Matrix(10, 1);
            result.applyFunc(p -> 0.0);
            result.set(labelData[lc++], 0, 1.0);
            dataList.add(new Data(input, result));
        }
        return dataList;
    }

    List<Data> loadData(String imageFile, String labelFile){
        List<Data> dataList;
        try {
            dataList = readDataFiles(imageFile, labelFile);
        }catch(java.io.IOException e){
            System.out.println(e);
            dataList = null;
        }
        if(dataList == null)
            System.out.println("dataList null");
        return dataList;
    }

    List<List<Data>> loadAllData(){
        // Return training and test data in a list of Data list.
        List<Data> trainingData = loadData(trainingFile, trainingLabel);
        List<Data> testData = loadData(testFile, testLabel);
        List<List<Data>> data = new ArrayList<>();
        data.add(trainingData);
        data.add(testData);
        return data;
    }

}
