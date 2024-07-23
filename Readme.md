Fall-Arm

#Fall Detection System

A comprehensive system that utilizes sensors, a mobile app, and a backend server to detect falls 
and promptly notify caretakers or emergency contacts.

#Overview This project aims to monitor the elderly and people prone to falls.
Utilizing the power of accelerometer and gyroscope sensors, we can detect unusual
movements that might indicate a fall. Once detected, the system immediately notifies a 
designated emergency contact via email, providing them with the details of the detected incident.

#Features #Sensor Data Collection: Utilizes data from accelerometer and gyroscope
sensors to monitor movements. #Instant Notifications: Sends an email alert when a 
potential fall is detected. #Data Visualization: A web interface that displays 
recorded data, aiding in monitoring and potential diagnostics. 
#Search Functionality: Allows caretakers to search for a particular patient's data. 
#Data Storage: Efficiently stores received data in a .json format for easy retrieval. 
Technical Stack #Mobile Application: Developed using native Android with Volley for network 
requests. #Backend Server: Flask-based server which handles incoming data, processes it, 
and sends out email notifications. #Data Storage: Uses .json files for storing and retrieving 
data. #Web Interface: Basic HTML, CSS, and JavaScript interface for data visualization and interaction.
Installation & Setup ##Backend Server: #Set up a virtual environment. #Install required packages:
pip install -r requirements.txt #Run the server: python app.py Mobile Application: Import the project 
into Android Studio and run on a physical device/emulator.

#Contributors

#Yixuan Liang #KESSELLY kamara #Francis Etang

License This project is licensed under the MIT License - see the LICENSE.md file for details.

#To See The Full doc of the project click the link below
#https://docs.google.com/presentation/d/1pEPmXz_q0fO4EMxui1FdB8F8LC13jY3TTE_dDCxpRIg/edit?usp=sharing
