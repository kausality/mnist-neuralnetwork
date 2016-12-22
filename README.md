# MNIST Digit recognizer




### Description
This program implements neural networks to recognize digits in the [MNIST](http://yann.lecun.com/exdb/mnist/) database.

This project is inspired from Michael Nielsen's implementation in his [Online Book](http://neuralnetworksanddeeplearning.com/).


### Dependencies

The [Colt Library](https://dst.lbl.gov/software/colt/) is used to perform linear algebra operations.


### Comments

#### Sample run:
<i>
Finished loading Data

Epoch 1: 4767/10000<br/>
Epoch 2: 5879/10000<br/>
Epoch 3: 5958/10000<br/>
Epoch 4: 6886/10000<br/>
Epoch 5: 6651/10000<br/>
Epoch 6: 7127/10000<br/>
Epoch 7: 7223/10000<br/>
Epoch 8: 7475/10000<br/>
Epoch 9: 6950/10000<br/>
-------<br/>
Epoch 98: 8389/10000<br/>
Epoch 99: 8449/10000<br/>
Epoch 100: 8425/10000<br/>
</i>

<b>With Hyperparameters:</b> 

epochs -> 100<br/>
batch size -> 30<br/>
learning rate -> 0.5<br/>
hidden layer perceptorns -> 30<br/>


As you can see, it's still not approaching the ~95% success rates as in the book(I have tried with other hyperparameters too).  Further improvements can be done.
