
# 🏦 Online Banking System – Microservices Architecture

A full-stack, secure, scalable online banking system built with Spring Boot, Spring Security, OpenFeign, Eureka, and Spring Cloud Gateway.

---

## 📦 Microservices in This Project
- **User Microservice** – Registration, Login, Role-based access, Password Reset
- **Card Microservice** – Issue, Block, PIN setup, Upgrade Limit (Admin approval)
- **Transaction Microservice** – Deposit, Withdraw, NetBanking, Card Shopping
- **Loan Microservice** – Apply Loan, View Loans, Settle Loans (Admin only)
- **Insurance Microservice** – Buy/View Home & Car Insurance Plans
- **Notification Microservice** – Sends notifications for key events
- **Eureka Server** – Service registry
- **Spring Cloud Gateway** – Single entry point to access all services

---

## 🔐 Security Features
- JWT Authentication (Bearer Token)
- BCrypt password hashing
- Role-based authorization using `@PreAuthorize`
- All endpoints secured except `/login` and `/register`
- Stateless sessions (no CSRF, no Basic Auth)

---

## 🔄 Communication
- **OpenFeign** used for inter-service calls (e.g., Card ↔ Transaction)
- **Service Discovery** via **Eureka Server**
- **Spring Cloud Gateway** routes all client requests to appropriate services

---

## 📁 Tech Stack

| Layer | Tech |
|-------|------|
| Backend | Spring Boot 3.3.1 |
| Security | Spring Security 6.2, JWT |
| API Gateway | Spring Cloud Gateway |
| Service Discovery | Eureka |
| Communication | OpenFeign |
| Build Tool | Maven |
| Database | MySQL |
| Testing | Postman |

---

## 🚀 How to Run

### 1. Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```

### 2. Start All Microservices
Repeat for each:
```bash
cd user-service  # or card-service, loan-service etc.
mvn spring-boot:run
```

> 💡 Ensure ports don’t conflict. Each service runs on a different port (e.g., 8081, 8082…).

### 3. Start Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

---

## ✅ API Highlights

### 🔑 Auth
- `POST /register` – Register new user
- `POST /login` – Authenticate & get JWT

### 👤 User
- `GET /profile` – View logged-in user details
- `PUT /reset-password` – Reset password via email link

### 💳 Card
- `POST /issue` – Request new card
- `PUT /block` – Block a card
- `PUT /pin` – Set PIN
- `POST /upgrade-limit` – User request → Admin approve/reject

### 💰 Transaction
- `POST /deposit`
- `POST /withdraw`
- `POST /netbank-shop`
- `POST /card-shop` → Validates card via CardMS

### 🏠 Insurance
- `GET /plans`
- `POST /buy`
- `GET /my-insurances`

### 🏦 Loan
- `POST /apply`
- `GET /my-loans`
- `PUT /settle/{loanId}` → Only Admin

---

## 📸 Postman Collection
You can import the Postman collection from:
`/postman/OnlineBankingCollection.json`

---

## 🧪 Example Roles
```json
"roles": ["USER"]  // or ["ADMIN"]
```

---

## 🧠 Learning Outcomes
- Spring Security with JWT
- Microservices Architecture
- OpenFeign communication
- Eureka + Gateway integration
- Role-based access control
- Token-based password reset

---

## 🧑‍💻 Author
**Aakash Kumar**  
 Software Developer   


