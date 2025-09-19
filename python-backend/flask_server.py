from flask import Flask, request, jsonify
import joblib
import numpy as np
import os

app = Flask(__name__)

# Load the trained AI model (ensure model is trained and saved as linear_model.pkl)
MODEL_PATH = os.path.join(os.path.dirname(__file__), 'ai_model', 'linear_model.pkl')
model = joblib.load(MODEL_PATH)

@app.route('/', methods=['GET'])
def index():
    return "✅ AI Flask Server is Running! Visit /predict (POST only)"

# GET fallback to avoid 405 Method Not Allowed
@app.route('/predict', methods=['GET'])
def predict_get():
    return jsonify({
        "message": "❌ Use POST method with JSON like {\"value\": 5} to get predictions"
    }), 405

# Actual POST endpoint for predictions
@app.route('/predict', methods=['POST'])
def predict_post():
    try:
        data = request.get_json()
        value = data.get('value')

        if value is None:
            return jsonify({'error': 'Missing \"value\" in JSON body'}), 400

        prediction = model.predict(np.array([[float(value)]])).tolist()[0]
        return jsonify({'prediction': prediction})

    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    # Host 0.0.0.0 allows access from Java or other devices
    app.run(host='0.0.0.0', port=5000)
