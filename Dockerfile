# Use OpenJDK 26 base image
FROM openjdk:26-jdk

# Set working directory inside container
WORKDIR /app

# Copy compiled JAR into container
COPY target/OOPProjectJavaFX-1.0-SNAPSHOT.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
