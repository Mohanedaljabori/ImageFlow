FROM node:20-slim

RUN apt-get update && \
    apt-get install -y openjdk-17-jre-headless curl && \
    rm -rf /var/lib/apt/lists/*

RUN npm install -g firebase-tools@12

WORKDIR /app

COPY firebase.json storage.rules ./

CMD ["sh", "-c", "firebase emulators:start --only storage --project ${FIREBASE_PROJECT_ID:-imageflow} --host 0.0.0.0 --port $PORT"]
