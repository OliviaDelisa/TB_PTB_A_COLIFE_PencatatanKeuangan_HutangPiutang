const bcrypt = require("bcrypt");
const User = require("../models/userModel");


// =======================
// REGISTER USER
// =======================
exports.register = async (req, res) => {
  const { name, email, password } = req.body;

  // Cek input kosong
  if (!name || !email || !password) {
    return res.status(400).json({
      success: false,
      message: "Semua field wajib diisi"
    });
  }

  try {
    // Cek apakah email sudah terdaftar
    const existingUser = await User.findByEmail(email);
    if (existingUser) {
      return res.status(400).json({
        success: false,
        message: "Email sudah terdaftar"
      });
    }

    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);

    // Simpan user baru
    const result = await User.create({
      name,
      email,
      password: hashedPassword
    });

    return res.json({
      success: true,
      message: "Registrasi berhasil",
      userId: result.insertId
    });

  } catch (err) {
    console.error(err);
    return res.status(500).json({
      success: false,
      message: "Terjadi kesalahan server"
    });
  }
};


// =======================
// LOGIN USER
// =======================
exports.login = async (req, res) => {
  const { email, password } = req.body;

  // Cek input kosong
  if (!email || !password) {
    return res.status(400).json({
      success: false,
      message: "Email dan password wajib diisi"
    });
  }

  try {
    const user = await User.findByEmail(email);

    if (!user) {
      return res.status(400).json({
        success: false,
        message: "Email tidak ditemukan"
      });
    }

    // Cek password
    const isMatch = await bcrypt.compare(password, user.password);
    if (!isMatch) {
      return res.status(400).json({
        success: false,
        message: "Password salah"
      });
    }

    return res.json({
      success: true,
      message: "Login berhasil",
      userId: user.id,
      name: user.name
    });

  } catch (err) {
    console.error(err);
    return res.status(500).json({
      success: false,
      message: "Terjadi kesalahan server"
    });
  }
};


// =======================
// GET ALL USERS
// =======================
exports.getUsers = async (req, res) => {
  const users = await User.getAll();
  return res.json(users);
};
