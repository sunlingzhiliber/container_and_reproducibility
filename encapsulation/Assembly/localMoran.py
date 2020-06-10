import pygeoda
import os
from sys import argv
import matplotlib
import matplotlib.pyplot as plt
import geopandas


# 输入shp文件
dataPath = argv[1]
feild = argv[2]
randomSed = int(argv[3])
filter = float(argv[4])
cpu=int(argv[5])

data = pygeoda.open(dataPath)
queen_w = pygeoda.weights.queen(data)
fieldCol = data.GetIntegerCol(feild)
lisa = pygeoda.local_moran(queen_w, fieldCol)
lisa.SetPermutations(randomSed)
lisa.SetThreads(cpu)
lisa.Run()


lisa_colors = lisa.GetColors()
lisa_labels = lisa.GetLabels()

lisa_cluster = lisa.GetClusterIndicators()
list_p_value = lisa.GetPValues()

fig, ax = plt.subplots(figsize=(10, 10))

gdf = geopandas.read_file(dataPath)

gdf['LISA'] = lisa_cluster
gdf['LISA_PVAL'] = list_p_value
for ctype, data in gdf.groupby('LISA'):
    color = lisa_colors[ctype]
    lbl = lisa_labels[ctype]
    data.plot(color=color,
              ax=ax,
              label=lbl,
              edgecolor='black',
              linewidth=0.2)
    data[data['LISA_PVAL'] >= filter].plot(color=lisa_colors[0], ax=ax)


lisa_legend = [matplotlib.lines.Line2D(
    [0], [0], color=color, lw=2) for color in lisa_colors]
ax.legend(lisa_legend, lisa_labels, loc='lower left',
          fontsize=12, frameon=True)
ax.set(title='Local Moran Cluster Map of '+feild)
ax.set_axis_off()
plt.savefig('./output/localMoran.png')
plt.close()


