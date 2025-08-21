FROM node:20-slim

RUN apt-get update && \
    apt-get install -y openjdk-17-jre-headless && \
    rm -rf /var/lib/apt/lists/*

RUN npm install -g firebase-tools@12

WORKDIR /app

VOLUME ["/app"]

EXPOSE 9199 4000

CMD ["firebase", "emulators:start", "--only", "storage", "--project", "imageflow", "--host", "0.0.0.0"]
