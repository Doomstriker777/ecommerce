# 🌸 AB Mart - Anime E-Commerce Platform

[![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)](https://getbootstrap.com/)

**AB Mart** is a full-featured, modern e-commerce web application dedicated to anime merchandise, collectibles, apparel, accessories, home decor, and stationery. Built with a robust **Java Spring Boot** backend, connected to **MySQL**, and rendered server-side using **Thymeleaf** and **Bootstrap 5**.

---

## 🚀 Key Features

### 🛍️ Product Catalog & Shopping Experience
- **6 Curated Anime Categories:**
  - 🎎 *Anime Figures & Statues* (Naruto Sage Mode, Goku Ultra Instinct, Tanjiro Kamado, Levi Ackerman)
  - 👕 *Anime Apparel & Clothing* (Akatsuki Cloud Hoodie, Straw Hat Pirates Tee, U.A. High Jacket, Ryomen Sukuna Tee)
  - 💍 *Anime Accessories & Jewelry* (Leaf Village Headband, Wings of Freedom Necklace, Potara Earrings, Ace's Necklace, Tanjiro Hanafuda Earrings, Kaneki Ring)
  - 🏮 *Anime Home & Decor* (Naruto Neon Sign, Totoro Desk Lamp, Shenron Dragon Burner, Colossal Titan 3D Wall Art)
  - 🎒 *Anime Bags & Cases* (Deku Backpack, Sailor Moon Luna Cat Crossbody, Akatsuki Pencil Case, One Piece Messenger Bag)
  - 📓 *Anime Stationery & Art* (Holographic Sticker Pack, Ghibli Watercolor Art Prints, Death Note Notebook with Quill, JJK Washi Tape)
- **Advanced Filtering & Search:** Real-time text search, category selection, price range filtering, in-stock filtering, and sorting (price low-to-high, high-to-low, name A-Z, newest).
- **Product Details:** Rich image galleries, stock status badges, category tags, customer reviews rating breakdown, and instant add-to-cart.

### 🛒 Cart & Checkout
- **Add to Cart:** Session & user-persistent cart management.
- **Quantity Adjustments:** Dynamic update and instant total recalculations.
- **Seamless Checkout:** Multi-field shipping address capture, order review, payment method selection (Cash on Delivery / Card simulation), and instant order placement.

### 📦 Order Tracking & Wishlist
- **Order History:** Complete log of past orders with unique tracking codes (`ORD-XXXXX`).
- **Order Details:** Breakdown of purchased items, pricing, delivery address, and live status badges (`PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`).
- **Wishlist:** Heart products to save them for later and transfer them directly into the cart.

### 🔐 Authentication & Security
- **Role-Based Access Control:** `ROLE_USER` and `ROLE_ADMIN` using **Spring Security**.
- **Secure Password Hashing:** BCrypt encryption for user credentials.
- **Session Management:** Automatic redirect after login, persistent authenticated sessions, and secure logout.
- **User Profile:** Manage personal info, shipping address, and password updates.

### ⚡ Admin Dashboard
- **Store KPIs:** Live metrics for total revenue, active orders, customer count, and total product catalog size.
- **Product Management:** Add new anime items, edit existing listings, toggle featured status, and update stock quantities.
- **Order Management:** View all customer orders and update shipping / fulfillment statuses in real-time.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Backend Framework** | Spring Boot 3.5 (Spring MVC, Spring Data JPA, Spring Security) |
| **Language** | Java 21 / 27 |
| **Database** | MySQL 8.0 |
| **ORM / Persistence** | Hibernate / JPA |
| **Template Engine** | Thymeleaf + Spring Security Dialect |
| **Styling & Icons** | Bootstrap 5.3, FontAwesome 6, Google Fonts (Poppins) |
| **Build Tool** | Apache Maven |

---

## ⚙️ Getting Started

### Prerequisites
- **Java Development Kit (JDK 21 or higher)** installed and set in `PATH`
- **MySQL Server 8.0+** running locally on port `3306`
- **Git**

### 1. Clone the Repository
```bash
git clone https://github.com/Doomstriker777/ecommerce.git
cd ecommerce
```

### 2. Configure MySQL Database
Create the MySQL database:
```sql
CREATE DATABASE abmart_db;
```

Update your database credentials in `src/main/resources/application.properties` if needed:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/abmart_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build & Run
Run the application using the Maven wrapper:

**On Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**On Linux/macOS:**
```bash
./mvnw spring-boot:run
```

The application will start on **`http://localhost:8080`**.

> 💡 **Auto-Seeding:** On first boot, the database automatically populates all 6 categories, 26 anime products, and default user accounts!

---

## 👤 Default Accounts

| Role | Username | Password | Access Level |
|---|---|---|---|
| **Administrator** | `admin` | `admin123` | Full Admin Dashboard & Storefront |
| **Customer** | `user1` | `user123` | Storefront, Cart, Orders & Wishlist |

*(You can also register a new account anytime via the Sign Up page).*

---

## 📁 Project Structure

```
ecommerce/
├── src/
│   ├── main/
│   │   ├── java/com/example/ecommerce/
│   │   │   ├── config/          # Security & Web MVC configurations
│   │   │   ├── controller/      # Auth, Shop, Cart, Order, Admin controllers
│   │   │   ├── model/           # JPA Entities (Product, Order, User, Category...)
│   │   │   ├── repository/     # Spring Data JPA repositories
│   │   │   └── service/        # Business logic & Database Seeder
│   │   └── resources/
│   │       ├── static/          # CSS, JS, and authentic anime product images
│   │       ├── templates/       # Thymeleaf HTML views (shop, cart, checkout, admin...)
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## 📜 License
This project is open-source and available under the [MIT License](LICENSE).
