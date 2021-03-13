import numpy as np
import matplotlib.pyplot as plt
import sys
import math

"""
Plots the function mse(epoch)
"""

# Collect the data from the input file
X = []
Y = []
f = open("mseByEpoch.txt", "r")
line = f.readline()
while(line != ""):
    X.append(float(line.strip().split(" ")[0]))
    Y.append(float(line.strip().split(" ")[1]))
    line = f.readline()

# Adjust y axis for plotting with logarithm scale
for i in range(len(Y)):
    Y[i] = math.log(Y[i])

# Plot graph
plt.plot(X, Y)
plt.ylabel("log\N{subscript one}\N{subscript zero}(MSE)")
plt.xlabel("epoch")
plt.title("Mean Squared Error for each Epoch")

plt.show()
f.close()
