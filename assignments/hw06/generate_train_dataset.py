import numpy as np
from math import cos

x = []
for i in range(-4, 5):
    for j in range(-4, 5):
        x.append([i, j])

y = []
for point in x:
    cos_part = cos(point[0]/5)**2
    result = ((point[0]-1)**2 + (point[1] + 2)**2 - 5*point[0]*point[1] + 3)*cos_part
    y.append(result)

for i in range(len(y)):
    print("{} {} {}".format(x[i][0], x[i][1], y[i]))
