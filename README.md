# login-project-kotlin

A simple but robust **user login system** written in Kotlin. This console-based project supports **username/password authentication**, **role-based access control**, **password hashing**, and stores all data in a local JSON file.

---

## 🔑 Features

- **User login** with username and password
- **Hashed passwords**
- **Role-based access** (`admin` and `user`)
- **Logging user actions** such as login, logout, and password changes
- **Admin panel**:
  - View all users
  - Add users
  - Remove users
  - Change users' passwords
- Prevents:
  - Duplicate usernames
  - Deleting the currently logged-in account
- Clean console interaction with basic input validation
- Readable and clean code structure

---

## 🚀 Getting Started

1. Clone the repository.
2. Open in IntelliJ IDEA or another Kotlin-compatible IDE.
3. Make sure `kotlinx.serialization` is set up.
4. Make sure that file at `data/users.json` with this content:

```json
[
  {
    "username": "admin",
    "password": "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918",
    "auth": "admin"
  }
]
```
5.	Run the app. Use admin as both username and password to log in.

⸻

## 📌 Notes
-	User actions like login, logout, and password changes are now logged to logs/log.txt for audit and tracking purposes.
- Logs include timestamps and usernames for better traceability.
-	All data is stored in a flat JSON file — no database required.
