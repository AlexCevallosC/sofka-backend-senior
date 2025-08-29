# Navega a la carpeta de tu microservicio (ej. ms-cliente)

docker rmi $(docker images -f "dangling=true" -q)

cd ..
cd ms-cliente

# Obtén la versión del proyecto
APP_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "La versión del proyecto ms-cliente es: $APP_VERSION"

# Construye la imagen con la etiqueta de la versión
docker rmi alexandercevallos/ms-cliente:$APP_VERSION
docker build --no-cache -t alexandercevallos/ms-cliente:$APP_VERSION .

# Navega al directorio raíz del proyecto para el siguiente
cd ..

# Y repite para el otro microservicio
cd ms-cuenta

echo "La versión del proyecto ms-cuenta es: $APP_VERSION"
APP_VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)

docker rmi alexandercevallos/ms-cuenta:$APP_VERSION
docker build --no-cache -t alexandercevallos/ms-cuenta:$APP_VERSION .

docker images | grep -i ms-

# /Applications/Docker.app/Contents/Resources/cli-plugins/docker-compose up -d --build
# /Applications/Docker.app/Contents/Resources/cli-plugins/docker-compose down -v