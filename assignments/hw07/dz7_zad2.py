import numpy as np
import matplotlib.pyplot as plt


X = []
y = []
with open("zad7-dataset.txt",'r') as dz7_dataset:
    for line in dz7_dataset:
        segments = line.strip().split("\t")
        X.append([float(segments[0]), float(segments[1])])
        y.append([int(segments[2]), int(segments[3]), int(segments[4])])

x1 = []
x2 = []
x3 = []
for i in range(len(X)):
    if y[i][0] == 1:
        x1.append(X[i])
    elif y[i][1] == 1:
        x2.append(X[i])
    else:
        x3.append(X[i])
        
xPlot = []
yPlot = []
for point in x1:
    xPlot.append(point[0])
    yPlot.append(point[1])
plt.scatter(xPlot, yPlot, label="A")

xPlot = []
yPlot = []
for point in x2:
    xPlot.append(point[0])
    yPlot.append(point[1])
plt.scatter(xPlot, yPlot, label="B")

xPlot = []
yPlot = []
for point in x3:
    xPlot.append(point[0])
    yPlot.append(point[1])
plt.scatter(xPlot, yPlot, label="C")

W1_type1 = [0.87, 0.62, 0.13, 0.37, 0.13, 0.63, 0.38, 0.87]
W2_type1 = [0.26, 0.74, 0.27, 0.74, 0.74, 0.26, 0.26, 0.73]

W1_test = [0.16, 0.64, 0.35, 0.43, 0.61, 1.34]
W2_test = [0.93, 0.19, 0.71, -1.76, 2.86, 0.33]
for i in range(len(W1_test)):
    plt.scatter(W1_test[i], W2_test[i])
    plt.text(W1_test[i], W2_test[i], i+1)

plt.legend(loc="best")
plt.show()
