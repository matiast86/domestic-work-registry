# 🔐 Security Overview

## 🧠 Overview

Security in **Domestic Work Registry** ensures safe access, role-based permissions, and token-based activation flows.

---

## ⚙️ Components

| Component                                 | Description                                    |
| ----------------------------------------- | ---------------------------------------------- |
| `SecurityConfig`                          | Configures authentication and route protection |
| `UserDetailsService`                      | Loads users and roles from DB                  |
| `DaoAuthenticationProvider`               | Verifies credentials                           |
| `CustomSuccessHandler`                    | Redirects users post-login                     |
| `AuthGuard`, `AdminGuard`, `TeacherGuard` | Restrict endpoints by role                     |
| `UserService`                             | Manages account activation and password reset  |
| `EmailService`                            | Sends secure token-based links                 |

---

## 🧾 Authentication Flow

1. User logs in with email & password
2. Credentials validated by `DaoAuthenticationProvider`
3. Spring Security sets authenticated principal
4. `CustomSuccessHandler` redirects based on role

---

## 🪄 Token-Based Activation

**Registration**

- New users created inactive (`active=false`)
- Token generated & emailed
- On link click → activates user

**Password Reset**

- Token generated with expiry (`resetTokenExpiry`)
- Email sent with reset link
- On submission → password re-encoded, token cleared

---

## 🧩 Role-Based Access

| Role       | Access                          |
| ---------- | ------------------------------- |
| `ADMIN`    | Full system management          |
| `EMPLOYER` | Contract & job management       |
| `EMPLOYEE` | Personal contracts & attendance |

Example:

```java
@PreAuthorize("hasRole('EMPLOYER')")
public String createContract() { ... }
```

---

## 🧱 Session & Password Policies

- Password: 8–16 chars, must include upper, lower, digit, special char
- Tokens expire after set time
- Session invalidated on logout

---

## 🔮 Future Security Enhancements

- 2FA for admin users
- JWT-based REST authentication (for future API)
- Audit logging of login and payroll access events

---

**Related Docs:**
🔗 [Architecture Overview](architecture.md) · [Business Logic](business-logic.md)
