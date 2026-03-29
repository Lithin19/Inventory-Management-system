import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const API_BASE = "http://localhost:8085";

function Home() {
  const navigate = useNavigate();

  // ✅ low stock count (only show alert card if > 0)
  const [lowStockCount, setLowStockCount] = useState(0);

  useEffect(() => {
    axios
      .get(`${API_BASE}/products/low-stock`) // backend defaults threshold to 5
      .then((res) => {
        setLowStockCount(Array.isArray(res.data) ? res.data.length : 0);
      })
      .catch(() => {
        setLowStockCount(0);
      });
  }, []);

  const heroStyle = {
    height: "60vh",
    background: "linear-gradient(135deg, #313169, #5a5abf)",
    color: "white",
    textShadow: "1px 1px 4px rgba(0,0,0,0.4)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexDirection: "column",
  };

  const cardStyle = {
    borderRadius: "12px",
    transition: "transform 0.2s ease, boxShadow 0.2s ease",
    cursor: "pointer",
  };

  const footerStyle = {
    backgroundColor: "#313169",
    color: "white",
    textAlign: "center",
    padding: "15px",
    marginTop: "30px",
  };

  // optional: reuse hover handlers
  const onEnter = (e) => {
    e.currentTarget.style.transform = "translateY(-5px)";
    e.currentTarget.style.boxShadow = "0 6px 20px rgba(0,0,0,0.15)";
  };

  const onLeave = (e) => {
    e.currentTarget.style.transform = "";
    e.currentTarget.style.boxShadow = "";
  };

  return (
    <div>
      {/* Hero Section */}
      <header style={heroStyle}>
        <h1 className="display-4 fw-bold">Welcome</h1>
        <p className="lead">Simple &amp; Modern</p>
      </header>

      <section className="container my-5">
        <div className="row text-center">
          {/* Add Products */}
          <div className="col-md-6 mb-6">
            <div
              className="card shadow-sm h-100"
              style={cardStyle}
              onClick={() => navigate("/addProduct")}
              onMouseEnter={onEnter}
              onMouseLeave={onLeave}
            >
              <div className="card-body">
                <h5 className="card-title text-success">Add Products</h5>
                <p className="card-text">Update the Stock from here.</p>
              </div>
            </div>
          </div>

          {/* Available Inventory */}
          <div className="col-md-6 mb-6">
            <div
              className="card shadow-sm h-100"
              style={cardStyle}
              onClick={() => navigate("/inventory")}
              onMouseEnter={onEnter}
              onMouseLeave={onLeave}
            >
              <div className="card-body">
                <h5 className="card-title text-primary">Available Inventory</h5>
                <p className="card-text">Manage Inventory efficiently.</p>
              </div>
            </div>
          </div>

          {/* ✅ Low Stock Alert Card (ONLY show if lowStockCount > 0) */}
          {lowStockCount > 0 && (
            <div className="d-flex flex-row justify-content-center">
            <div className="col-md-4 mt-5 mb-4">
              <div
                className="card shadow-sm h-100 border border-danger"
                style={cardStyle}
                onClick={() => navigate("/low-stock")}
                onMouseEnter={onEnter}
                onMouseLeave={onLeave}
              >
                <div className="card-body">
                  <h5 className="card-title text-danger">
                    ⚠ Stock Running Out!
                  </h5>
                  <p className="card-text">
                    {lowStockCount} product(s) have stock running out
                  </p>
                  <span className="badge bg-danger">View Products</span>
                </div>
              </div>
            </div>
            </div>
          )}
        </div>
      </section>

      <footer style={footerStyle}>
        <p>© 2026 My Portal</p>
      </footer>
    </div>
  );
}

export default Home;