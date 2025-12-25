const express = require("express");
const router = express.Router();
const db = require("../config/db");
const multer = require("multer");
const path = require("path");

const storage = multer.diskStorage({
  destination: "uploads/",
  filename: (req, file, cb) => {
   
    const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);
    cb(null, uniqueSuffix + path.extname(file.originalname));
  },
});

const upload = multer({ storage });

router.post("/add", (req, res) => {
  const { nama, tanggal, jumlah } = req.body;
  if (!nama || !tanggal || jumlah == null) {
    return res.status(400).json({ success: false, message: "Data tidak lengkap" });
  }
  const query = "INSERT INTO hutang (nama, tanggal, jumlah) VALUES (?, ?, ?)";
  db.query(query, [nama, tanggal, jumlah], (err) => {
    if (err) return res.status(500).json({ success: false, message: "Gagal tambah hutang" });
    res.json({ success: true, message: "Hutang berhasil ditambahkan" });
  });
});

router.get("/", (req, res) => {
  const query = "SELECT * FROM hutang ORDER BY id DESC";
  db.query(query, (err, result) => {
    if (err) return res.status(500).json({ success: false, message: "Error ambil data" });
    res.json({ success: true, data: result });
  });
});

router.put("/update/:id", (req, res) => {
  const { id } = req.params;
  const { nama, tanggal, jumlah } = req.body;
  const query = "UPDATE hutang SET nama = ?, tanggal = ?, jumlah = ? WHERE id = ?";
  db.query(query, [nama, tanggal, jumlah, id], (err) => {
    if (err) return res.status(500).json({ success: false, message: "Gagal update" });
    res.json({ success: true, message: "Hutang diperbarui" });
  });
});

router.delete("/delete/:id", (req, res) => {
  const { id } = req.params;
  const query = "DELETE FROM hutang WHERE id = ?";
  db.query(query, [id], (err) => {
    if (err) return res.status(500).json({ success: false, message: "Gagal hapus" });
    res.json({ success: true, message: "Hutang dihapus" });
  });
});

router.post("/selesai/:id", (req, res) => {
  const { id } = req.params;
  const selectQuery = "SELECT * FROM hutang WHERE id = ?";
  
  db.query(selectQuery, [id], (err, result) => {
    if (err || result.length === 0) return res.status(404).json({ success: false, message: "Hutang tak ditemukan" });
    const h = result[0];
    const insertHistory = "INSERT INTO history_hutang (nama, tanggal, jumlah) VALUES (?, ?, ?)";
    
    db.query(insertHistory, [h.nama, h.tanggal, h.jumlah], (err2) => {
      if (err2) return res.status(500).json({ success: false, message: "Gagal history" });
      
      db.query("DELETE FROM hutang WHERE id = ?", [id], (err3) => {
        if (err3) return res.status(500).json({ success: false, message: "Gagal hapus lama" });
        res.json({ success: true, message: "Lunas! Masuk history." });
      });
    });
  });
});

router.get("/history", (req, res) => {
  const query = "SELECT * FROM history_hutang ORDER BY tanggal_selesai DESC";
  db.query(query, (err, result) => {
    if (err) return res.status(500).json({ success: false, message: "Gagal history" });
    res.json({ success: true, data: result });
  });
});

router.post("/upload-bukti", upload.single("image"), (req, res) => {
    const hutangId = req.body.hutang_id;
    
    if (!req.file || !hutangId) {
      return res.status(400).json({
        success: false, 
        message: "File gambar atau ID Hutang tidak ada!" 
      });
    }
  
    const imagePath = "uploads/" + req.file.filename;
  
    const query = `INSERT INTO gambarhutang (hutang_id, image_url) VALUES (?, ?)`;
  
    db.query(query, [hutangId, imagePath], (err, result) => {
      if (err) {
        console.error(err);
        return res.status(500).json({ success: false, message: "Gagal simpan ke database" });
      }
  
      res.json({
        success: true,
        message: "Gambar berhasil disimpan!",
        data: {
            id: result.insertId,
            image_url: imagePath
        }
      });
    });
});

router.get("/gambar/:id", (req, res) => {
    const id = req.params.id;
    const query = "SELECT * FROM gambarhutang WHERE hutang_id = ? ORDER BY created_at DESC";
    
    db.query(query, [id], (err, result) => {
        if (err) return res.status(500).json({ success: false, message: "Gagal ambil gambar" });
        
        res.json({
            success: true,
            data: result 
        });
    });
});

router.delete("/gambar/delete/:id", (req, res) => {
    const id = req.params.id;
    db.query("DELETE FROM gambarhutang WHERE id = ?", [id], (err) => {
        if (err) return res.status(500).json({ success: false, message: "Gagal hapus gambar" });
        res.json({ success: true, message: "Gambar berhasil dihapus" });
    });
});

module.exports = router;