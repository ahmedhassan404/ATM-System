# ATM System

This is a simple ATM (Automated Teller Machine) system implemented in Java. The system allows users to sign up, log in, and perform basic banking operations such as checking their balance, depositing money, and withdrawing money. The project also includes basic security features like password hashing and multi-factor authentication (MFA).

## Features

- **User Registration**: Users can sign up with a username, password, and initial balance.
- **User Login**: Secure login with password hashing.
- **Multi-Factor Authentication (MFA)**: An additional layer of security using a 6-digit code.
- **ATM Operations**:
  - Check balance.
  - Deposit money.
  - Withdraw money.
- **Admin Role**: Admins have access to additional operations (e.g., admin-specific tasks).

## Technologies Used

- **Java**: Core programming language.
- **MySQL**: Database for storing user information.
- **JDBC**: Java Database Connectivity for interacting with the database.
- **SHA-256**: Password hashing for secure storage.
- **OWASP Encoder**: For output encoding to prevent XSS attacks.

## Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or higher.
- **MySQL**: Database server.
- **MySQL Connector/J**: JDBC driver for MySQL.
- **Maven** (optional): For dependency management.

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ahmedhassan404/ATM-System.git
   cd ATM-System
