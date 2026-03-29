import { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
 
const API_BASE = "http://localhost:8085";
 
function LowStockAlert() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
 
  useEffect(() => {
    axios
      .get(`${API_BASE}/products/low-stock`) // threshold defaults to 5 in backend
      .then((res) => {
        setProducts(res.data);
        setLoading(false);
      })
      .catch((err) => {
        setError("Failed to load low stock products");
        setLoading(false);
      });
  }, []);
 
  return (
    <div className="container mt-4">
      <h2>⚠️ Low Stock Alert (Stock &lt; 5)</h2>
 
      {loading && (
        <div className="alert alert-info mt-3">
          Loading low stock products...
        </div>
      )}
 
      {error && (
        <div className="alert alert-danger mt-3">
          {error}
        </div>
      )}
 
      {!loading && !error && products.length === 0 && (
        <div className="alert alert-success mt-3">
          ✅ No products are low in stock
        </div>
      )}
 
      {products.length > 0 && (
        <>
          <div className="alert alert-warning mt-3">
            ⚠️ {products.length} product(s) are low in stock!
          </div>
 
          <table className="table table-bordered table-striped mt-3">
            <thead className="table-dark">
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Quantity</th>
              </tr>
            </thead>
            <tbody>
              {products.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.name}</td>
                  <td>{p.category}</td>
                  <td>{p.price}</td>
                  <td>
                    <span className="badge bg-danger">
                      {p.quantity}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}
 
export default LowStockAlert;
 