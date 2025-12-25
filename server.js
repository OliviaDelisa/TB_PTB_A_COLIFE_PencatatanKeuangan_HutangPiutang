const express = require("express");
const cors = require("cors");
const path = require("path");

const app = express();
app.use(cors());
app.use(express.json());

// =======================
// STATIC FILE (UPLOADS)
// =======================
// INI WAJIB supaya gambar bisa diakses via URL
app.use("/uploads", express.static(path.join(__dirname, "uploads")));

// =======================
// ROUTES
// =======================
app.use("/api/user", require("./routes/userRoutes"));
app.use("/api/hutang", require("./routes/hutangRoutes"));
app.use("/api/struk", require("./routes/strukRoutes"));

// =======================
// SERVER START
// =======================
app.listen(3000, "0.0.0.0", () => {
  console.log("Server running on port 3000");
});
