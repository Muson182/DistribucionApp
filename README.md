DistribucionApp
Descripción
DistribucionApp es una aplicación móvil creada para una empresa de distribución de alimentos. Su principal objetivo es calcular automáticamente los costos de despacho de acuerdo con reglas de negocio predefinidas y permitir el registro de usuarios mediante cuentas Gmail a través de Google Sign-In. Además, la aplicación registra la ubicación del usuario en tiempo real en Firebase y gestiona el control de temperatura del congelador para productos sensibles como carnes y mariscos congelados.

Características principales
Cálculo del costo de despacho:

Despacho gratuito para compras superiores a 50,000 pesos en un radio de 20 km.
Cobro de $150 por kilómetro para compras entre 25,000 y 49,999 pesos.
Cobro de $300 por kilómetro para compras menores a 25,000 pesos.
Autenticación mediante Google Sign-In:

Registro y autenticación de usuarios utilizando cuentas de Google.
Monitoreo y almacenamiento de ubicación:

Obtención y almacenamiento de la ubicación del usuario en Firebase Realtime Database en tiempo real.
Monitoreo de temperatura de congelador:

Alerta en caso de que la temperatura del congelador del camión de reparto exceda los límites establecidos.
Tecnologías utilizadas
Lenguaje: Kotlin
Interfaz de usuario: Jetpack Compose
Firebase: Firebase Authentication, Firebase Realtime Database
Google Sign-In: Autenticación con cuentas de Gmail
Control de versiones: Git y GitHub

---Instalación---
Requisitos previos
1.Android Studio instalado en tu sistema.
2.Tener una cuenta de Firebase y Google API configuradas para autenticar con Google Sign-In.
3.Un dispositivo virtual o físico con Android 5.0 (API 21) o superior.

---Clonar el repositorio---
git clone https://github.com/tu_usuario/distribucionapp.git
cd distribucionapp

Configurar Firebase
1.Crear un proyecto en Firebase Console.
2.Agregar una aplicación Android a Firebase y descargar el archivo google-services.json.
3.Colocar el archivo google-services.json en la carpeta app del proyecto.

Configurar Google Sign-In
1.En Firebase Console, habilita la autenticación con Google en la sección Authentication.
2.Obtén el client ID de OAuth 2.0 en las credenciales de la API de Google y agrégalo al archivo strings.xml:

4<resources>
    <string name="default_web_client_id">YOUR_CLIENT_ID</string>
</resources>

---Compilar y ejecutar---
1.Abre el proyecto en Android Studio.
2.Sincroniza las dependencias de Gradle.
3.Conecta un dispositivo Android o ejecuta un emulador.
4.Ejecuta la aplicación utilizando el botón Run o el atajo Shift + F10.

---Uso de la Aplicación---
1.Inicia sesión en la aplicación utilizando tu cuenta de Google.
2.Ingresa el total de la compra y la distancia en kilómetros para calcular el costo de despacho.
3.Almacena tu ubicación en Firebase con un solo clic.
4.Monitorea la temperatura del camión para asegurar la cadena de frío en productos congelados.
