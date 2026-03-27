import React, { useState, useEffect } from "react";
import { productService } from "../services/productService";
import UpdateStockModal from "../components/updateStockModal";

export default function LowStock({ onUpdateStock }) {
  const [threshold, setThreshold] = useState(5);
  const [inputVal, setInputVal] = useState("5");
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [updateTarget, setUpdateTarget] = useState(null);

  const fetch = async (t) => {
    setLoading(true);
    setError(null);
    try {
      const data = await productService.getLowStock(t);
      setProducts(data);
    } catch (err) {
      setError(err.message || "Failed to fetch low stock products");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetch(threshold); }, [threshold]);

  const handleApply = () => {
    const t = parseInt(inputVal);
    if (!isNaN(t) && t >= 0) setThreshold(t);
  };

  return (
    <div>
      <div className="d-flex align-items-center mb-4">
        <div>
          <h2 className="fw-bold mb-0 d-flex align-items-center gap-2">
            <i className="bi bi-exclamation-triangle-fill text-warning"></i>
            Low Stock Alert
          </h2>
          <p className="text-muted mb-0">Products with quantity below threshold</p>
        </div>
      </div>

      {/* Threshold control */}
      <div className="card border-0 shadow-sm mb-4" style={{ borderRadius: "14px" }}>
        <div className="card-body p-3">
          <div className="row g-3 align-items-center">
            <div className="col-auto">
              <label className="col-form-label fw-semibold">Threshold:</label>
            </div>
            <div className="col-auto">
              <input
                type="number"
                className="form-control"
                style={{ width: "100px" }}
                min="0"
                value={inputVal}
                onChange={(e) => setInputVal(e.target.value)}
              />
            </div>
            <div className="col-auto">
              <button className="btn btn-warning fw-semibold" onClick={handleApply}>
                <i className="bi bi-funnel me-1"></i> Apply
              </button>
            </div>
            <div className="col-auto ms-auto">
              {!loading && (
                <span className="badge bg-danger rounded-pill px-3 py-2 fs-6">
                  {products.length} item(s) below {threshold}
                </span>
              )}
            </div>
          </div>
        </div>
      </div>

      {error && (
        <div className="alert alert-danger d-flex align-items-center gap-2">
          <i className="bi bi-exclamation-circle-fill"></i> {error}
        </div>
      )}

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-warning" style={{ width: 48, height: 48 }}></div>
          <p className="text-muted mt-3">Checking stock levels…</p>
        </div>
      ) : products.length === 0 ? (
        <div className="card border-0 shadow-sm text-center py-5" style={{ borderRadius: "14px" }}>
          <i className="bi bi-check-circle-fill text-success fs-1 mb-3 d-block"></i>
          <h5 className="fw-bold">All Clear!</h5>
          <p className="text-muted">No products are below the threshold of {threshold}.</p>
        </div>
      ) : (
        <div className="row g-3">
          {products.map((p) => (
            <div className="col-sm-6 col-xl-4" key={p.id}>
              <div
                className="card border-0 shadow-sm h-100"
                style={{ borderRadius: "14px", borderLeft: "4px solid #dc3545" }}
              >
                <div className="card-body p-4">
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <div>
                      <h6 className="fw-bold mb-1">{p.name}</h6>
                      <span className="badge bg-secondary bg-opacity-15 text-secondary fw-normal">
                        {p.category || "Uncategorized"}
                      </span>
                    </div>
                    <span
                      className="badge bg-danger rounded-pill px-2 py-1 fs-6"
                      title="Current quantity"
                    >
                      {p.quantity}
                    </span>
                  </div>
                  <div className="d-flex justify-content-between align-items-center">
                    <span className="text-muted small">₹{parseFloat(p.price || 0).toFixed(2)}</span>
                    <button
                      className="btn btn-sm btn-outline-primary"
                      onClick={() => setUpdateTarget(p)}
                    >
                      <i className="bi bi-pencil-square me-1"></i> Restock
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      <UpdateStockModal
        show={!!updateTarget}
        product={updateTarget}
        onConfirm={async (id, qty) => {
          await onUpdateStock(id, qty);
          setUpdateTarget(null);
          fetch(threshold);
        }}
        onCancel={() => setUpdateTarget(null)}
      />
    </div>
  );
}