# Build front end
FROM node:22-alpine AS front-end-build
WORKDIR /front-end

# Install deps first for better caching
COPY front-end/package*.json ./
RUN npm ci

# Build
COPY front-end/ ./
RUN npm run build

RUN ls -la dist

# Build back end 
FROM maven:3.9-eclipse-temurin-17 AS back-end-build
WORKDIR /app

# Copy back end code
COPY back-end/ .

# Copy Angular dist into Spring static resources
RUN mkdir -p src/main/resources/static
COPY --from=front-end-build /front-end/dist/front-end/browser src/main/resources/static/

# Build the jar
RUN mvn -DskipTests clean package

# Runtime image
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Copy the built jar
COPY --from=back-end-build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

