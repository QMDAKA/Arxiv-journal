import numpy as np
import sys
import demjson
from sklearn.externals import joblib
from sklearn import svm, datasets, metrics
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_curve, auc
import mysql.connector

#confirm argument

# Data IO and generation
clf_by_normal = joblib.load('classifier_normal.pkl')
clf_by_weighted = joblib.load('classifier_weighted.pkl')
arr_normal = np.array([[sys.argv[1],sys.argv[2],sys.argv[3]]], dtype='float')
arr_weighted = np.array([[sys.argv[4],sys.argv[5],sys.argv[6]]], dtype='float')

predict_normal =  clf_by_normal.predict(arr_normal)
predict_weighted = clf_by_weighted.predict(arr_weighted)
hash = {}
hash["normal"] = predict_normal[0]
hash["weighted"] = predict_weighted[0]
print hash