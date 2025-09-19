from flask import Flask, request, jsonify
from ai_model.predict import predict

app = Flask(__name__)

# ✅ Health Check Endpoint
@app.route('/', methods=['GET'])
def home():
    return "✅ AI + AWS RDS/DynamoDB Flask Server is running."

# ✅ Prediction Endpoint
@app.route('/predict', methods=['POST'])
def predict_route():
    try:
        data = request.get_json()
        
        # Validate JSON
        if not data or 'input' not in data:
            return jsonify({'error': '❌ Invalid input. JSON must contain "input" key.'}), 400

        sample_input = data['input']

        # Ensure input is list or compatible
        if not isinstance(sample_input, list):
            return jsonify({'error': '❌ "input" must be a list of values.'}), 400

        prediction = predict(sample_input)

        return jsonify({
            'input': sample_input,
            'prediction': prediction
        })

    except Exception as e:
        return jsonify({'error': f'❌ Server Error: {str(e)}'}), 500

# ✅ Server Run
if __name__ == '__main__':
    print("🚀 Starting Flask server at http://localhost:5000")
    app.run(debug=True, port=5000)
