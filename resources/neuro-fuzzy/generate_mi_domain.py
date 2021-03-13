import numpy as np

import matplotlib.pyplot as plt

"""
This file generates meshgrid with given range on x and y axes.
"""

step = 100 # defines how many points are generated in the range

x = np.linspace(-4, 4, step)

f = open("miFuncDomain.txt", "w")
for i in range(len(x)):
    f.write("{:.4f}\n".format(x[i]))

f.close()
