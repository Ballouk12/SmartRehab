# Smart Rehab: Intelligent Rehabilitation

Smart Rehab is a platform designed to enhance physical rehabilitation by leveraging real-time pose detection, motion analysis, and tailored recommendations. By integrating open-source pose estimation algorithms and patient-specific thresholds, Smart Rehab empowers users and therapists with actionable insights to optimize recovery and prevent improper movements during rehabilitation exercises.

## Table of Contents

- [Software Architecture](#software-architecture)
- [Docker Image](#docker-image)
- [Frontend](#frontend)
- [Backend](#backend)
- [Video Demonstration](#video-demonstration)
- [Contributing](#contributing)


# Software Architecture
![archit](https://github.com/user-attachments/assets/d6ca1f9a-50a8-4110-9927-1663bf4510f7)

The application architecture consists of:
- *Frontend*: Vite.js for the web interface and java for mobile clients.
- *Backend*: Spring Boot for core services .
- *Communication*: RESTful APIs 
## Docker Image
```yaml
# version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_DATABASE: heath_db
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    networks:
      - app-network
    volumes:
      - mysql_data:/var/lib/mysql

  backend:
    build: ./MobileFinalBackend
    ports:
      - "8083:8083"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/heath_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql
    networks:
      - app-network

  frontend:
    build: ./frontendApp
    ports:
      - "5173:5173"
    networks:
      - app-network
    environment:
      - VITE_API_URL=http://localhost:8083  
    depends_on:
      - backend
    command: npm run dev -- --host  

volumes:
  mysql_data:

networks:
  app-network:
    driver: bridge
```
## Frontend

### Technologies Used
- vite.js (Web)
- java (Android)
- Tailwind CSS
- JavaScript

## Backend

### Technologies Used
- Spring Boot
- MySQL

### Backend Setup:

1. Clone the Project:
   - Clone the repository by running the following command:
     bash
     git clone https://github.com/Ballouk12/SmartRehab.git
     cd MobileFinalBackend
     

2. Install Backend Dependencies:
   - Open a terminal in the backend project folder.
   - Run the following command to install dependencies:
     bash
     mvn clean install
     

3. Configure Application Properties:
   - Update the application.properties file with your MySQL credentials.

4. Run Backend:
   - Start your XAMPP Apache and MySQL servers.
   - Run the Spring Boot application. The database and entities will be created automatically.
   - Verify that the backend is running at [http://localhost:8083](http://localhost:8083).
       

### Web Frontend Setup:

1. Install Dependencies:
   - Navigate to the web-client directory:
     bash
     cd frontendApp
     
   - Install the necessary dependencies by running:
     bash
     npm install
     

2. Run Frontend:
   - Start the development server by running:
     bash
     npm run dev
     

3. Access the Web Interface:
   - Open your browser and navigate to [http://localhost:5173](http://localhost:5173) to access the web interface.


### Mobile Frontend Setup:

1. Android:
   - Open the Android project in Android Studio.
   - Update the API endpoint in the configuration files.
   - Build and run the application on your device or emulator.


## Video Demonstration For mobile App
Here' an illustrative video of the android mobile app:

<div align="center">


https://github.com/user-attachments/assets/478a010e-7cc5-40b0-9841-9ae2776b2361



</div>

## Video Demonstration For Web APP
Here' an illustrative video of the android Web app:

<div align="center">


https://github.com/user-attachments/assets/53bad9c3-9233-4fba-9c1d-1b88a4eff929



</div>

# Contributing
 
We gladly welcome contributions from everyone and value your support in enhancing this project! If you're interested in contributing, please adhere to the following guidelines

## Contributors
                
- Mohamed Lachgar ([Researchgate](https://www.researchgate.net/profile/Mohamed-Lachgar))
- Ballouk Mohamed(https://github.com/Ballouk12)
- Harati Ayoube (https://github.com/ayoub-aav)


