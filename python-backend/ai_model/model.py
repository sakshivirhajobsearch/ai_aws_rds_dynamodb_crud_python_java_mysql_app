# python-backend/ai_model/model.py
from sklearn.linear_model import LinearRegression
import numpy as np
import joblib

# Dummy AI model
X = np.array([[1], [2], [3], [4]])
y = np.array([10, 20, 30, 40])

model = LinearRegression()
model.fit(X, y)
joblib.dump(model, 'linear_model.pkl')
