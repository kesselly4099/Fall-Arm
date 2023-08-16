# from flask import Flask, jsonify, request
# import json
# import os

# app = Flask(__name__)

# DATA_FILE = 'data.json'

# @app.route('/endpoint', methods=['POST'])
# def receive_data():
#     data = request.get_json()
    
#     # Required fields
#     required_fields = [
#         'patient_name', 'patient_id', 'timestamp', 'latitude', 'longitude', 
#         'location_name', 'accelerometer_value', 'gyroscope_value'
#     ]
    
#     if not all([field in data for field in required_fields]):
#         missing_fields = [field for field in required_fields if field not in data]
#         return jsonify({'status': 'error', 'message': f'Missing fields: {", ".join(missing_fields)}'}), 400

#     # Save to JSON file
#     if os.path.exists(DATA_FILE):
#         with open(DATA_FILE, 'r') as file:
#             saved_data = json.load(file)
#     else:
#         saved_data = []

#     saved_data.append(data)

#     with open(DATA_FILE, 'w') as file:
#         json.dump(saved_data, file, indent=4)
    
#     return jsonify({'status': 'success', 'message': 'Data received and saved!'}), 200

# if __name__ == '__main__':
#     app.run(debug=True, host='0.0.0.0', port=5000)







from flask import Flask, appcontext_pushed, request, jsonify, render_template
import json
import smtplib
from email.mime.text import MIMEText
from flask_cors import CORS

# ... [rest of the existing code]




app = Flask(__name__)
CORS(app)

THRESHOLD = 10 
data_store = []# This is a placeholder; replace with actual threshold for detecting a fall

def send_email(subject, body, to_email):
    from_email = "kamarakesselly@outlook.com"
    from_password = "06413051KkMm@"
    to_email = "kamarakesselly@gmail.com"
    subject = "Fall Detect"
    
    msg = MIMEText(body)
    msg['From'] = from_email
    msg['To'] = to_email
    msg['Subject'] = subject
    
    server = smtplib.SMTP('smtp.outlook.com', 587)
    server.starttls()
    server.login(from_email, from_password)
    server.sendmail(from_email, to_email, msg.as_string())
    server.quit()

@app.route('/endpoint', methods=['POST'])
def receive_data():
    data = request.json
    with open('data.json', 'a') as f:
        json.dump(data, f)
        f.write('\n')

    if data.get('accelerometer_value', 0) > THRESHOLD or data.get('gyroscope_value', 0) > THRESHOLD:
        email_subject = "Fall Detected!"
        email_body = f"""
        Hi CareTakers,
        A fall was detected Please ,Patient Name: {data['patient_name']}
        Patient ID: {data['patient_id']}
        Location: {data['location_name']}
        Accelerometer Value: {data['accelerometer_value']}
        Gyroscope Value: {data['gyroscope_value']}
        Timestamp: {data['timestamp']}
        """
        send_email(email_subject, email_body, "recipient_email@gmail.com")

    return jsonify({"message": "Data received"}), 200


@app.route('/get_data', methods=['GET'])
def get_data():
    with open('data.json', 'r') as f:
        data_list = [json.loads(line) for line in f]
    return jsonify(data_list)


@app.route('/web', methods=['GET'])
def web_page():
    return render_template('index.html')



if __name__ == '__main__':
    app.run(debug=True)






