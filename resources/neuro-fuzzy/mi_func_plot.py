import numpy as np
import matplotlib.pyplot as plt
import sys

"""
Plots the function u(x) based on data in input file.
"""

file_name = sys.argv[1]

# Collect the data from the input file
X = []
Y = []
f = open(file_name + ".txt", "r")
line = f.readline()
while(line != ""):
    X.append(float(line.strip().split(" ")[0]))
    Y.append(float(line.strip().split(" ")[1]))
    line = f.readline()

# Plot graph
plt.ylim(0, 1)
plt.plot(X, Y)
plt.title("\N{greek small letter mu}(" + file_name[2] +
          "\N{latin subscript small letter i})\ni = " + file_name[3])

plt.show()
f.close()
