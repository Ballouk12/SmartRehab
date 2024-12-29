# Smart Rehab: Intelligent Rehabilitation Monitoring and Alert System

Smart Rehab is a platform designed to enhance physical rehabilitation by leveraging real-time pose detection, motion analysis, and tailored recommendations. By integrating open-source pose estimation algorithms and patient-specific thresholds, Smart Rehab empowers users and therapists with actionable insights to optimize recovery and prevent improper movements during rehabilitation exercises.

## Table of Contents

- [Software Architecture](#software-architecture)
- [Docker Image](#docker-image)
- [Frontend](#frontend)
- [Backend](#backend)
- [Video Demonstration](#video-demonstration)
- [Contributing](#contributing)

## Software Architecture

![Smart Rehab Architecture]()

The application architecture consists of:
- *Frontend*: Vite.js for the web interface and java for mobile clients.
- *Backend*: Spring Boot for core services .
- *Communication*: RESTful APIs for seamless integration between services.
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
      - VITE_API_URL=http://localhost:8083  # Modifié pour pointer vers localhost
    depends_on:
      - backend
    command: npm run dev -- --host  # Corrigé: --host au lieu de --hosts

volumes:
  mysql_data:

networks:
  app-network:
    driver: bridge
