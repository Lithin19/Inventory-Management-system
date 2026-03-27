import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";

function Home() {
  const navigate = useNavigate();

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

  return (
    <div>
      {/* Hero Section */}
      <header style={heroStyle}>
        <h1 className="display-4 fw-bold">Welcome</h1>
        <p className="lead">Simple & Modern</p>
      </header>

      <section className="container my-5">
        <div className="row text-center">
          <div className="col-md-6 mb-4">
            <div
              className="card shadow-sm h-100"
              style={cardStyle}
              onClick={() => navigate("/addProduct")}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = "translateY(-5px)";
                e.currentTarget.style.boxShadow =
                  "0 6px 20px rgba(0,0,0,0.15)";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = "";
                e.currentTarget.style.boxShadow = "";
              }}
            >
              <div className="card-body">
                <h5 className="card-title text-success">Add Products</h5>
                <p className="card-text">Update the Stock from here.</p>
              </div>
            </div>
          </div>
          <div className="col-md-6 mb-4">
            <div
              className="card shadow-sm h-100"
              style={cardStyle}
              onClick={() => navigate("/inventory")}
              onMouseEnter={(e) => {
                e.currentTarget.style.transform = "translateY(-5px)";
                e.currentTarget.style.boxShadow =
                  "0 6px 20px rgba(0,0,0,0.15)";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.transform = "";
                e.currentTarget.style.boxShadow = "";
              }}
            >
              <div className="card-body">
                <h5 className="card-title text-primary">Available Inventory</h5>
                <p className="card-text">Manage Inventory efficiently.</p>
              </div>
            </div>
          </div>
        </div>
      </section>
      <footer style={footerStyle}>
        <p>© 2026 My Portal</p>
      </footer>
    </div>
  );
}

export default Home;