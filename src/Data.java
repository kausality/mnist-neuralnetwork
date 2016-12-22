/**
 * Created by kaustubh on 17/11/16.
 */
public class Data {
    Matrix input; // The input image as 28 X 28 matrix
    Matrix result; // The value of the number in image as 10 x 1 vector

    Data(Matrix input, Matrix result){
        this.input = input;
        this.result = result;
    }
}
