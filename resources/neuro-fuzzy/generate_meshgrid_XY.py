import numpy as np

import matplotlib.pyplot as plt

"""
This file generates meshgrid with given range on x and y axes.
"""

def z_function(x, y):
    return ((x - 1)**2 + (y + 2)**2 -5*x*y + 3)*np.cos(x/5)**2

step = 40 # defines how many points are generated in the range

x = np.linspace(-4, 4, step)
y = np.linspace(-4, 4, step)

X, Y = np.meshgrid(x, y)

f = open("meshGridXY.txt", "w")
for i in range(X.shape[0]):
    for j in range(X.shape[1]):
        f.write("{:.4f} {:.4f} {:.6f}\n".format(X[i][j], Y[i][j],
                                                z_function(X[i][j], Y[i][j])))

f.close()
