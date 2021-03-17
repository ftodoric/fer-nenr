import numpy as np

import matplotlib.pyplot as plt

def z_function(x, y):
    return ((x - 1)**2 + (y + 2)**2 -5*x*y + 3)*np.cos(x/5)**2

step = 1000

x = np.linspace(-4, 4, step)
y = np.linspace(-4, 4, step)

X, Y = np.meshgrid(x, y)
Z = z_function(X, Y)
    
fig = plt.figure()
ax = plt.axes(projection="3d")

ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap="inferno", edgecolor="none")
ax.set_title("f(x,y)")

plt.show()
