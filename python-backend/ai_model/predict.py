# python-backend/ai_model/predict.py
import joblib
import os
import numpy as np

MODEL_FILE = os.path.join(os.path.dirname(__file__), 'model.pkl')

try:
    model = joblib.load(MODEL_FILE)
    print("✅ Model loaded successfully!")
except FileNotFoundError:
    print("❌ model.pkl not found. Train your model first.")
    exit(1)

def predict(input_data):
    """
    input_data: list of lists [[x1], [x2], ...]
    """
    np_input = np.array(input_data)
    predictions = model.predict(np_input)
    return predictions.tolist()

# ✅ Test prediction
if __name__ == "__main__":
    sample_input = [[5], [6]]
    print("Input:", sample_input)
    print("Prediction:", predict(sample_input))
