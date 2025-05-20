# login-project-kotlin
A simple but robust **user login system** written in Kotlin. This console-based project supports **username/password authentication**, **admin-only features**, and stores all data in a local JSON file.

---

## 🔑 Features

- **User login** with username and password
- **Role-based access** (`admin` and `user`)
- **Admin panel**:
  - View all users
  - Add users
  - Remove users
- Prevents:
  - Duplicate usernames
  - Deleting the currently logged-in account
- Clean console interaction with basic input validation

---

## 🚀 Getting Started

1. Clone the repository.
2. Open in IntelliJ IDEA or another Kotlin-compatible IDE.
3. Make sure `kotlinx.serialization` is set up.
4. Create a file at `data/users.json` with this content:

```json
[
  {
    "username": "admin",
    "password": "admin",
    "auth": "admin"
  }
]
```
5.	Run the app. Use admin as both username and password to log in.

⸻

## 📌 Notes
-	Users with the user role can log in, but only admins access the control panel.
-	All data is stored in a flat JSON file — no database required.
