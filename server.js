const express = require("express");
const cors = require("cors");

const app = express();
app.use(cors());
app.use(express.json());

// ROUTES
app.use("/api/user", require("./routes/userRoutes"));
app.use("/api/hutang", require("./routes/hutangRoutes"));

// SERVER START
app.listen(3000, "0.0.0.0", () => {
  console.log("Server running on port 3000");
});
