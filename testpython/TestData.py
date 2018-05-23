import numpy as np

import demjson
from sklearn.externals import joblib
from sklearn import svm, datasets, metrics
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_curve, auc
import mysql.connector

# Data IO and generation
clf_by_normal = joblib.load('classifier_normal.pkl')
clf_by_weighted = joblib.load('classifier_weighted.pkl')

X = []
y = []
obj = []
cnx = mysql.connector.connect(user='root', password='root',
                              host='127.0.0.1',
                              database='journal')
cursor = cnx.cursor()
query = ("SELECT cn,aa,jc FROM candidate WHERE cn > 0 LIMIT 10000")
cursor.execute(query)
for col in cursor:
    arr = np.asarray(col)
    X.append(arr)
query = ("SELECT label FROM candidate WHERE cn > 0 LIMIT 10000")
cursor.execute(query)
for col in cursor:
    y.append(np.asarray(col[0]))
query = ("SELECT author_id1,author_id2,predict_by_normal_measure,predict_by_weighted_measure FROM candidate WHERE cn > 0 LIMIT 10000")
cursor.execute(query)
for col in cursor:
    obj.append(col)
X_arr = np.array(X)
y_arr = np.array(y)
y_predict_by_normal = clf_by_normal.predict(X_arr)
y_predict_by_weighted = clf_by_weighted.predict(X_arr)
arr = []
for i in range(0, len(obj)):
    hash = {}
    hash["author1"] = obj[i][0]
    hash["author2"] = obj[i][1]
    hash["normal"] = y_predict_by_normal[i]
    hash["weighted"] = y_predict_by_weighted[i]
    arr.append(hash)
json = demjson.encode(arr)
fo = open("result.json", "w+")
fo.write(json)
fo.close()
print (json)
