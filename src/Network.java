/**
 * Created by kaustubh on 14/11/16.
 */

import java.util.*;


public class Network {
    private int totalLayers; // Keep count of total layers
    Matrix[] biases; // An array of biases
    Matrix[] weights; // An array of weights

    Network(int[] sizes){
        totalLayers = sizes.length;
        biases = new Matrix[totalLayers - 1];
        weights = new Matrix[totalLayers - 1];
        for(int i = 0; i < totalLayers - 1; i++){
            Random rand = new Random();
            biases[i] = new Matrix(sizes[i + 1], 1);
            weights[i] = new Matrix(sizes[i + 1], sizes[i]);
            biases[i] = biases[i].applyFunc(p -> rand.nextGaussian());
            weights[i] = weights[i].applyFunc(p -> rand.nextGaussian());
        }
    }

    Matrix sigmoid(Matrix z){
        return z.applyFunc(p -> 1 / (1 + Math.pow(Math.E, -p)));
    }

    Matrix sigmoidPrime(Matrix z){
        Matrix x = sigmoid(z);
        x = x.applyFunc(p -> 1 - p);
        return sigmoid(z).schurProduct(x);
    }

    Matrix feedForward(Matrix inp){
        Matrix a = new Matrix(inp);
        for(int i = 0; i < totalLayers - 1; i++){
            a = sigmoid(weights[i].matrixMult(a).matrixAdd(biases[i]));
        }
        return a;
    }

    Matrix cost_derivative(Matrix output_activations, Matrix y){
        return output_activations.matrixSub(y);
    }

    List<Matrix[]> backprop(Data data){
        Matrix[] nabla_b = new Matrix[totalLayers - 1];
        Matrix[] nabla_w = new Matrix[totalLayers - 1];
        for(int j = 0; j < totalLayers - 1; j++){
            nabla_b[j] = new Matrix(biases[j].get_rows(), biases[j].get_cols());
            nabla_w[j] = new Matrix(weights[j].get_rows(), weights[j].get_cols());
        }
        Matrix activation = new Matrix(data.input);
        List<Matrix> activations = new ArrayList<>();
        activations.add(activation);
        List<Matrix> zVector = new ArrayList<>();
        //System.out.println("activation[0]:");
        //activation.printMatrix();
        for(int j = 0; j < totalLayers - 1; j++){
            Matrix z = weights[j].matrixMult(activation).matrixAdd(biases[j]);
            zVector.add(z);
            activation = sigmoid(z);
            activations.add(activation);
        }
        Matrix delta = cost_derivative(activations.get(activations.size() - 1), data.result).schurProduct(sigmoidPrime(zVector.get(zVector.size() - 1)));
        nabla_b[nabla_b.length - 1] = delta;
        nabla_w[nabla_w.length - 1] = delta.matrixMult(activations.get(activations.size() - 2).matrixTranspose());
        for(int j = nabla_b.length - 2; j >= 0; j--){
            Matrix z = zVector.get(j);
            Matrix sp = sigmoidPrime(z);
            delta = weights[j + 1].matrixTranspose().matrixMult(delta).schurProduct(sp);
            nabla_b[j] = delta;
            nabla_w[j] = delta.matrixMult(activations.get(j).matrixTranspose());
        }
        List<Matrix[]> ret = new ArrayList<>();
        ret.add(nabla_b);
        ret.add(nabla_w);
        //System.exit(2);
        return ret;
    }

    void update_mini_batch(List<Data> miniBatch, double eta){
        int size = miniBatch.size();
        Matrix[] nabla_b = new Matrix[totalLayers - 1];
        Matrix[] nabla_w = new Matrix[totalLayers - 1];
        for(int j = 0; j < totalLayers - 1; j++){
            nabla_b[j] = new Matrix(biases[j].get_rows(), biases[j].get_cols());
            nabla_w[j] = new Matrix(weights[j].get_rows(), weights[j].get_cols());
        }
        for(int i = 0; i < size; i++){
            List<Matrix[]> deltas = backprop(miniBatch.get(i));
            Matrix[] delta_nabla_b = deltas.get(0);
            Matrix[] delta_nabla_w = deltas.get(1);
            for(int j = 0; j < totalLayers - 1; j++){
                nabla_b[j] = nabla_b[j].matrixAdd(delta_nabla_b[j]);
                nabla_w[j] = nabla_w[j].matrixAdd(delta_nabla_w[j]);
            }
        }
        for(int j = 0; j < totalLayers - 1; j++){
            weights[j] = weights[j].matrixSub(nabla_w[j].applyFunc(p -> eta/size * p));
            biases[j] = biases[j].matrixSub(nabla_b[j].applyFunc(p -> eta/size * p));
        }
    }

    void SGD(List<Data> trainingData, List<Data> testData, int epochs, int miniBatchSize, double eta){
        int trainDataSize = trainingData.size();
        int testDataSize = testData.size();
        for(int i = 0; i < epochs; i++){
            Collections.shuffle(trainingData);
            for(int j = 0; j < trainDataSize - miniBatchSize; j+=miniBatchSize)
                update_mini_batch(trainingData.subList(j, j + miniBatchSize), eta);
            int correct = evaluate(testData);
            System.out.println("Epoch " + (i + 1) + ": " + correct + "/" + testDataSize);
        }
    }

    int evaluate(List<Data> data){
        int correct = 0;
        for(int i = 0; i < data.size(); i++) {
            Data d = data.get(i);
            Matrix output = feedForward(d.input);
            int maxResultRow = 0;
            int maxOutputRow = 0;
            for (int j = 0; j < d.result.get_rows(); j++) {
                if (d.result.get(j, 0) > d.result.get(maxResultRow, 0))
                    maxResultRow = j;
                if (output.get(j, 0) > output.get(maxOutputRow, 0))
                    maxOutputRow = j;
            }
            if (maxResultRow == maxOutputRow)
                correct += 1;
        }
        return correct;
    }
}
