import numpy as np
import matplotlib.pyplot as plt
import sys

"""
Plots the functions f(x,y) based on data in input file.
"""

graph_name = ""
if len(sys.argv) != 1:
    graph_name += "\n"
    graph_name += sys.argv[1]

step = 40 # defines how many points are generated in the range

# Collect the data from the input file
X = []
Y = []
Z = []
f = open("anfisFuncPlotData.txt", "r")
line = f.readline()
while(line != ""):
    rowX = []
    rowY = []
    rowZ = []
    for i in range(step):
        rowX.append(float(line.strip().split(" ")[0]))
        rowY.append(float(line.strip().split(" ")[1]))
        rowZ.append(float(line.strip().split(" ")[2]))
        line = f.readline()
    X.append(rowX)
    Y.append(rowY)
    Z.append(rowZ)

Z = np.array(Z)

# Plot surface
fig = plt.figure()
ax = plt.axes(projection="3d")

ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap="inferno", edgecolor="none")
ax.set_title("f(x, y)" + graph_name)

plt.show()
f.close()
