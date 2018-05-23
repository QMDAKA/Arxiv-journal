import numpy as np

import demjson
from sklearn.externals import joblib
from sklearn import svm, datasets, metrics
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import roc_curve, auc
from sklearn.model_selection import StratifiedKFold
import mysql.connector

# #############################################################################
# Data IO and generation
X_normal_measure = []
X_weighted_measure = []
y = []
cnx = mysql.connector.connect(user='root', password='root',
                              host='127.0.0.1',
                              database='journal')
cursor = cnx.cursor()
query = ("SELECT cn,aa,jc FROM candidate WHERE cn > 0")
cursor.execute(query)
for col in cursor:
    arr = np.asarray(col)
    X_normal_measure.append(arr)
query = ("SELECT wcn,waa,wjc FROM candidate WHERE cn > 0")
cursor.execute(query)
for col in cursor:
    arr = np.asarray(col)
    X_weighted_measure.append(arr)
query = ("SELECT label FROM candidate WHERE cn > 0")
cursor.execute(query)
for col in cursor:
    y.append(np.asarray(col[0]))
X_arr_normal_measure = np.array(X_normal_measure)
X__arr_weighted_measure = np.array(X_weighted_measure)
y_arr = np.array(y)
y0 = y_arr[np.where(y_arr == 0)]
quantity_label_0 = y0.size
quantity_label_1 = y_arr.size - quantity_label_0
cv = StratifiedKFold(n_splits=6)
classifier_normal = LogisticRegression(penalty='l2', class_weight='balanced', C=1)
classifier_weighted = LogisticRegression(penalty='l2', class_weight='balanced', C=1)
arr_hash_map = []
hashmap = {}
hashmap["total"] = len(X_normal_measure)
arr_fold = []
total_score = 0
acc_score = 0
for train, test in cv.split(X_arr_normal_measure, y_arr):
    fold = {}
    classifier_normal.fit(X_arr_normal_measure[train], y_arr[train])
    y_predict_test = classifier_normal.predict(X_arr_normal_measure[test])
    score = metrics.f1_score(y_arr[test], y_predict_test)
    acc = metrics.accuracy_score(y_arr[test], y_predict_test)
    total_score += score
    acc_score += acc
    tn, fp, fn, tp = metrics.confusion_matrix(y_arr[test], y_predict_test).ravel()
    fold["size"] = len(y_predict_test)
    fold["tp"] = tp
    fold["fp"] = fp
    fold["tn"] = tn
    fold["fn"] = fn
    fold["score"] = score
    arr_fold.append(fold)
hashmap["matrix"] = arr_fold
hashmap["f1_score"] = total_score/6
hashmap["acc_score"] = acc_score/6
hashmap["quantity_label_0"] = quantity_label_0
hashmap["quantity_label_1"] = quantity_label_1
arr_hash_map.append(hashmap)
arr_fold = []
total_score = 0
acc_score =0
for train, test in cv.split(X__arr_weighted_measure, y_arr):
    fold = {}
    classifier_weighted.fit(X__arr_weighted_measure[train], y_arr[train])
    y_predict_test = classifier_weighted.predict(X__arr_weighted_measure[test])
    score = metrics.f1_score(y_arr[test], y_predict_test)
    total_score += score
    acc_score += acc
    tn, fp, fn, tp = metrics.confusion_matrix(y_arr[test], y_predict_test).ravel()
    fold["size"] = len(y_predict_test)
    fold["tp"] = tp
    fold["fp"] = fp
    fold["tn"] = tn
    fold["fn"] = fn
    fold["score"] = score
    arr_fold.append(fold)
hashmap = {}
hashmap["total"] = len(X_normal_measure)
hashmap["matrix"] = arr_fold
hashmap["f1_score"] = total_score/6
hashmap["acc_score"] = acc_score/6
hashmap["quantity_label_0"] = quantity_label_0
hashmap["quantity_label_1"] = quantity_label_1
arr_hash_map.append(hashmap)
json = demjson.encode(arr_hash_map)
fo = open("result.json", "w+")
fo.write(json)
fo.close()
_ = joblib.dump(classifier_normal, 'classifier_normal.pkl')
_ = joblib.dump(classifier_weighted, 'classifier_weighted.pkl')
print(json)