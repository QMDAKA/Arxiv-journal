import numpy as np
import demjson
import json
from sklearn.externals import joblib
from sklearn import svm, datasets, metrics
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_curve, auc
import mysql.connector
import re

input_file  = file("/home/quangminh/IdeaProjects/Journal2/candidates.json", "r")
data = json.loads(input_file.read().decode("utf-8-sig"))
# Data IO and generation
clf_by_normal = joblib.load('classifier_normal.pkl')
clf_by_weighted = joblib.load('classifier_weighted.pkl')
arr = []
for i in range(0, len(data)):
    hash = {}
    hash['authorId1'] = data[i]['authorId1']
    hash['authorId2'] = data[i]['authorId2']
    arr_normal = np.array([[data[i]['cn'],data[i]['aa'],data[i]['jc']]], dtype='float')
    arr_weighted = np.array([[data[i]['wcn'],data[i]['waa'],data[i]['wjc']]], dtype='float')
    predict_normal =  clf_by_normal.predict(arr_normal)
    predict_weighted = clf_by_weighted.predict(arr_weighted)
    hash['cn'] = data[i]['cn']
    hash['aa'] = data[i]['aa']
    hash['jc'] = data[i]['jc']
    hash['wcn'] = data[i]['wcn']
    hash['wjc'] = data[i]['waa']
    hash['waa'] = data[i]['wjc']
    hash['normal_predict'] = predict_normal[0]
    hash['weighted_predict'] = predict_weighted[0]
    arr.append(hash)
print (demjson.encode(arr))
# json = demjson.encode(arr)
# fo = open("/home/quangminh/IdeaProjects/Journal (copy)/result_predict.json", "w+")
# fo.write(json)
# fo.close()
# print(json)

