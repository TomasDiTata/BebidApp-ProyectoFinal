# 🍺 BebidApp - Sistema de Gestión y Venta de Bebidas

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005F0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)

## 📋 Descripción
BebidApp es una aplicación web Full Stack desarrollada como Proyecto Final. Simula un **E-commerce** completo para una distribuidora de bebidas, permitiendo la gestión integral de productos, usuarios y ventas.

El sistema implementa una arquitectura MVC robusta, seguridad basada en roles y persistencia de datos relacional.

---

## 🚀 Funcionalidades Principales

### 👤 Cliente
* **Catálogo Público:** Visualización de productos con barra de búsqueda inteligente.
* **Carrito de Compras:** Agregar/Quitar productos y cálculo automático de totales.
* **Checkout:** Proceso de pago simulado (Efectivo, Tarjeta, Transferencia) con validaciones en tiempo real.
* **Historial:** Visualización de pedidos anteriores y detalle de productos comprados.
* **Seguridad:** Registro de cuenta y Login encriptado.

### 📦 Vendedor
* **Gestión de Inventario:** Alta, Baja (Lógica) y Modificación de productos.
* **Control de Stock:** Actualización rápida de cantidades y precios.
* **Acceso:** Panel exclusivo de administración de productos.

### 🛡️ Administrador
* **Gestión de Usuarios:** Visualización de todos los usuarios registrados.
* **Roles:** Capacidad para ascender usuarios a Vendedores o Administradores.
* **Control Total:** Acceso a todas las funcionalidades del sistema.

---

## 🛠️ Tecnologías Utilizadas

* **Backend:** Java 21, Spring Boot 3.2.0 (Security, JPA, MVC).
* **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap 5.3, JavaScript.
* **Base de Datos:** MySQL 8.0.
* **Herramientas:** Maven, Lombok, NetBeans/IntelliJ.

---

## ⚙️ Instalación y Ejecución

Sigue estos pasos para correr el proyecto en tu entorno local:

### 1. Base de Datos
1. Abre tu gestor SQL (MySQL Workbench / XAMPP).
2. Crea la base de datos ejecutando el script incluido en la raíz del proyecto:
   - Archivo: `Bebidapp-Script-SQL.sql`
3. Verifica que la base de datos `bebidapp` se haya creado correctamente con sus tablas.

### 2. Configuración
1. Abre el archivo `src/main/resources/application.properties`.
2. Configura tus credenciales de base de datos si son diferentes a las predeterminadas:
   ```properties
   spring.datasource.username=tu_usuario (ej: root)
   spring.datasource.password=tu_clave
