import pickle
import numpy as np

def load_model():
    try:
        with open('ai_model/model.pkl', 'rb') as f:
            model = pickle.load(f)
            print("✅ AI model loaded successfully.")
            return model
    except FileNotFoundError:
        print("❌ model.pkl not found. Train your model first.")
        return None

def predict(sample_input):
    model = load_model()
    if model is None:
        return "Model not loaded."

    try:
        prediction = model.predict(np.array(sample_input).reshape(1, -1))
        print("🔮 Prediction:", prediction[0])
        return prediction[0]
    except Exception as e:
        print("❌ Error during prediction:", e)
        return str(e)

if __name__ == '__main__':
    # 🔧 Sample input — must match your model's input shape
    input_data = [5.1, 3.5, 1.4, 0.2]  # Example: Iris data (4 features)

    # Predict
    predict(input_data)
