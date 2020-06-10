library(rgdal) #负责读入shp文件
library(spdep) #负责计算莫兰指数

args<-commandArgs()
inputFile<-args[6]
inputfeild<-args[7]


rdata=readOGR(inputFile)
nb=poly2nb(rdata,queen=TRUE) #queen连接的权重矩阵
lw=nb2listw(nb) #转为莫兰指数计算所需格式
feild=rdata[[inputfeild,exact=FALSE]]
mc=moran.test(as.numeric(as.character(feild)),listw=lw) #计算莫兰指数


write.csv(mc[3],"./output/mc.txt")

# The spatial weighting matrix
png(file = "./output/neighborsMap.png", width = 7, height = 7, units = "in",res = 72)
par(mar = rep(0.1, 4))
plot(rdata, col = "grey95", border = "grey")
plot(lw, coordinates(rdata), add = TRUE, pch = 20, lwd = 2, cex = 2)
dev.off()





