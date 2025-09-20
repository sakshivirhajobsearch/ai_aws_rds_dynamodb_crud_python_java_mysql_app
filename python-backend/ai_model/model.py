# python-backend/ai_model/model.py
from sklearn.linear_model import LinearRegression
import numpy as np
import joblib
import os

# ✅ Define model save path
MODEL_FILE = os.path.join(os.path.dirname(__file__), 'model.pkl')

# Dummy AI dataset
X = np.array([[1], [2], [3], [4]])
y = np.array([10, 20, 30, 40])

# Train model
model = LinearRegression()
model.fit(X, y)

# Save model
joblib.dump(model, MODEL_FILE)
print(f"✅ Model trained and saved at {MODEL_FILE}")
