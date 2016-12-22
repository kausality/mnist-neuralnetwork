/**
 * Created by kaustubh on 9/11/16.
 */

import java.util.function.Function;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.linalg.Algebra;


public class Matrix {
    /* Wrapper for the Colt library.
       Makes doing Matrix operations more clear and less verbose.
    */
    private int rows;
    private int cols;
    private DoubleMatrix2D mat;

    Matrix(int r, int c){
        rows = r;
        cols = c;
        mat = new DenseDoubleMatrix2D(r, c);
    }

    Matrix(Matrix a){
        mat = new DenseDoubleMatrix2D(a.rows, a.cols);
        rows = a.rows;
        cols = a.cols;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++)
                mat.setQuick(i, j, a.get(i, j));
        }
    }

    private DoubleMatrix2D cloneInternalObject(){
        return mat.copy();
    }

    private DenseDoubleMatrix2D toInternalObject(Matrix matObj){
        return (DenseDoubleMatrix2D) matObj.cloneInternalObject();
    }

    private Matrix toMatrixObject(DoubleMatrix2D internalObj){
        int r = internalObj.rows();
        int c = internalObj.columns();
        Matrix matObj = new Matrix(r, c);
        for(int i = 0; i < r; i++)
            for(int j = 0; j < c; j++)
                matObj.set(i, j, internalObj.getQuick(i, j));
        return matObj;
    }

    void set(int r, int c, double v){
        mat.setQuick(r, c, v);
    }

    double get(int r, int c){
        return mat.getQuick(r, c);
    }

    int get_rows(){
        return rows;
    }

    int get_cols() {
        return cols;
    }

    void printMatrix(){
        for(int i=0; i<rows; i++){
            for(int j=0; j<cols; j++) {
                System.out.print(mat.get(i, j));
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    Matrix matrixAdd(Matrix matB){
        DoubleDoubleFunction plus = new DoubleDoubleFunction() {
            @Override
            public double apply(double v, double v1) {
                return v + v1;
            }
        };
        DoubleMatrix2D newMat = cloneInternalObject();
        newMat.assign(toInternalObject(matB), plus);
        return toMatrixObject(newMat);
    }

    Matrix matrixSub(Matrix matB){
        DoubleDoubleFunction sub = new DoubleDoubleFunction() {
            @Override
            public double apply(double v, double v1) {
                return v - v1;
            }
        };
        DoubleMatrix2D newMat = cloneInternalObject();
        newMat.assign(toInternalObject(matB), sub);
        return toMatrixObject(newMat);
    }

    Matrix matrixMult(Matrix matB){
        DoubleMatrix2D ret = new DenseDoubleMatrix2D(rows, matB.cols);
        mat.zMult(toInternalObject(matB), ret);
        return toMatrixObject(ret);
    }

    Matrix matrixTranspose(){
        DoubleMatrix2D ret = Algebra.DEFAULT.transpose(mat).copy();
        return toMatrixObject(ret);
    }

    Matrix schurProduct(Matrix matB){
        Matrix newMat = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                newMat.set(i, j, mat.get(i, j) * matB.get(i, j));
        return newMat;
    }

    Matrix applyFunc(Function<Double, Double> fn){
        DoubleMatrix2D newMat = cloneInternalObject();
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                double v = fn.apply(mat.getQuick(i, j));
                newMat.setQuick(i, j, v);
            }
        }
        return toMatrixObject(newMat);
    }
}
