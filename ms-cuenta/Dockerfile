# Usa una imagen base de OpenJDK para Java 21
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR de tu aplicación al contenedor.
# Asume que el archivo JAR se llama 'ms-cuenta-0.0.1-SNAPSHOT.jar'
# y que está en la carpeta 'target' después de la construcción de Maven.
COPY target/ms-cuenta-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que usa tu microservicio (8082 para ms-cuenta)
EXPOSE 8082

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]