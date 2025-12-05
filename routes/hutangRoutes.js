const express = require("express");
const router = express.Router();
const db = require("../config/db");

// =========================
//  ADD HUTANG
// =========================
router.post("/add", (req, res) => {
  const { nama, tanggal, jumlah } = req.body;

  if (!nama || !tanggal || jumlah == null) {
    return res.status(400).json({
      success: false,
      message: "Data tidak lengkap",
    });
  }

  const query = `
      INSERT INTO hutang (nama, tanggal, jumlah)
      VALUES (?, ?, ?)
  `;

  db.query(query, [nama, tanggal, jumlah], (err) => {
    if (err) {
      console.error("Error Insert Hutang:", err);
      return res.status(500).json({
        success: false,
        message: "Gagal menambahkan hutang",
      });
    }

    res.json({
      success: true,
      message: "Hutang berhasil ditambahkan!",
    });
  });
});

// =========================
//  GET ALL HUTANG
// =========================
router.get("/", (req, res) => {
  const query = `
      SELECT * FROM hutang
      ORDER BY id DESC
  `;

  db.query(query, (err, result) => {
    if (err) {
      console.error("Error Get Hutang:", err);
      return res.status(500).json({
        success: false,
        message: "Gagal mengambil data hutang",
      });
    }

    res.json({
      success: true,
      data: result,
    });
  });
});

// =========================
//  DELETE HUTANG
// =========================
router.delete("/delete/:id", (req, res) => {
  const { id } = req.params;

  const query = "DELETE FROM hutang WHERE id = ?";

  db.query(query, [id], (err) => {
    if (err) {
      console.error("Error Delete Hutang:", err);
      return res.status(500).json({
        success: false,
        message: "Gagal menghapus hutang",
      });
    }

    res.json({
      success: true,
      message: "Hutang berhasil dihapus",
    });
  });
});

// =========================
//  UPDATE HUTANG
// =========================
router.put("/update/:id", (req, res) => {
  const { id } = req.params;
  const { nama, tanggal, jumlah } = req.body;

  if (!nama || !tanggal || jumlah == null) {
    return res.status(400).json({
      success: false,
      message: "Data tidak lengkap",
    });
  }

  const query = `
      UPDATE hutang
      SET nama = ?, tanggal = ?, jumlah = ?
      WHERE id = ?
  `;

  db.query(query, [nama, tanggal, jumlah, id], (err, result) => {
    if (err) {
      console.error("Error Update Hutang:", err);
      return res.status(500).json({
        success: false,
        message: "Gagal memperbarui hutang",
      });
    }

    if (result.affectedRows === 0) {
      return res.status(404).json({
        success: false,
        message: "Data hutang tidak ditemukan",
      });
    }

    res.json({
      success: true,
      message: "Hutang berhasil diperbarui",
    });
  });
});

// =========================
//  TANDAI HUTANG SELESAI
// =========================
router.put("/selesai/:id", (req, res) => {
  const { id } = req.params;

  const query = `
      UPDATE hutang 
      SET status = 1
      WHERE id = ?
  `;

  db.query(query, [id], (err, result) => {
    if (err) {
      console.error("Error update selesai:", err);
      return res.status(500).json({
        success: false,
        message: "Gagal menandai hutang selesai"
      });
    }

    return res.json({
      success: true,
      message: "Hutang berhasil ditandai selesai"
    });
  });
});


// =========================
//  EXPORT ROUTER
// =========================
module.exports = router;
