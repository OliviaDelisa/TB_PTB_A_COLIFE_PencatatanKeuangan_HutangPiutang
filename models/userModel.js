const db = require("../config/db");

const User = {

  create: (data) => {
    return new Promise((resolve, reject) => {
      db.query(
        "INSERT INTO users (name, email, password) VALUES (?, ?, ?)",
        [data.name, data.email, data.password],
        (err, result) => {
          if (err) reject(err);
          else resolve(result);
        }
      );
    });
  },

  findByEmail: (email) => {
    return new Promise((resolve, reject) => {
      db.query(
        "SELECT * FROM users WHERE email = ?",
        [email],
        (err, result) => {
          if (err) reject(err);
          else resolve(result[0]);
        }
      );
    });
  },

  getAll: () => {
    return new Promise((resolve, reject) => {
      db.query(
        "SELECT id, name, email FROM users",
        (err, result) => {
          if (err) reject(err);
          else resolve(result);
        }
      );
    });
  }
};

module.exports = User;
