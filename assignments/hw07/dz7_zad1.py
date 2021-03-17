import numpy as np
import matplotlib.pyplot as plt

x = np.linspace(-8, 10, 1000)
w = 2
s = [0.25, 1, 4]

y = 1/(1 + np.abs(x - 2)/np.abs(4))
plt.plot(x, y, label="s = 4")

y = 1/(1 + np.abs(x - 2)/np.abs(1))
plt.plot(x, y, label="s = 1")

y = 1/(1 + np.abs(x - 2)/np.abs(0.25))
plt.plot(x, y, label="s = 0.25")

plt.legend(loc="best")
plt.show()
