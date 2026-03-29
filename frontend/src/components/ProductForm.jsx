import { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { Modal, Button } from "react-bootstrap"; // ✅ popup modal

function ProductForm() {
  const [product, setProduct] = useState({
    id: 0,
    name: "",
    category: "",
    price: 0,
    quantity: 0,
    createdAt: ""
  });

  // ✅ popup state
  const [showPopup, setShowPopup] = useState(false);
  const [popupMsg, setPopupMsg] = useState("");

  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = !!id;

  useEffect(() => {
    if (!isEdit) return;

    axios
      .get(`http://localhost:8085/products/${id}`)
      .then((response) => setProduct(response.data))
      .catch((err) => console.log(err));
  }, [id, isEdit]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    const numericFields = ["id", "price", "quantity"];

    setProduct({
      ...product,
      [name]: numericFields.includes(name) ? Number(value) : value
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    const payload = {
      name: product.name,
      category: product.category,
      price: product.price,
      quantity: product.quantity,
    };

    const apiresponse = isEdit
      ? axios.put(
          `http://localhost:8085/products/${id}/stock`,
          null,
          { params: { quantity: product.quantity } }
        )
      : axios.post("http://localhost:8085/products", payload);

    apiresponse
      .then((resp) => {
        setProduct(resp.data);

        // ✅ set popup message based on mode
        setPopupMsg(isEdit ? "✅ Product updated successfully!" : "✅ Product added successfully!");
        setShowPopup(true); // ✅ open popup
      })
      .catch((err) => {
        console.log("API ERROR:", err);
        console.log("STATUS:", err?.response?.status);
        console.log("DATA:", err?.response?.data);

        setPopupMsg("❌ Something went wrong. Please try again.");
        setShowPopup(true);
      });
  };

  const handleClosePopup = () => {
    setShowPopup(false);
    navigate("/"); // ✅ navigate after closing popup
  };

  const createdAtText =
    product.createdAt ? new Date(product.createdAt).toLocaleString() : "";

  return (
    <>
      <div className="container-fluid mt-5 w-50">
        <h2>{isEdit ? "Edit" : "Add"} Product</h2>

        <form onSubmit={handleSubmit}>
          <div className="mb-3 text-start">
            <label className="form-label">Product Id</label>
            <input
              className="form-control"
              type="number"
              id="id"
              name="id"
              value={product.id}
              onChange={handleChange}
              min="0"
              disabled={true}
              required
            />
          </div>

          <div className="mb-3 text-start">
            <label className="form-label">Product Name</label>
            <input
              className="form-control"
              type="text"
              id="name"
              name="name"
              onChange={handleChange}
              value={product.name}
              disabled={isEdit}
              required
            />
          </div>

          <div className="mb-3 text-start">
            <label className="form-label">Category</label>
            <input
              className="form-control"
              type="text"
              id="category"
              name="category"
              onChange={handleChange}
              value={product.category}
              disabled={isEdit}
              required
            />
          </div>

          <div className="mb-3 text-start">
            <label className="form-label">Price</label>
            <input
              className="form-control"
              type="number"
              step="0.01"
              id="price"
              name="price"
              onChange={handleChange}
              value={product.price}
              min="0"
              required
            />
          </div>

          <div className="mb-3 text-start">
            <label className="form-label">Stock (Quantity)</label>
            <input
              className="form-control"
              type="number"
              id="quantity"
              name="quantity"
              value={product.quantity}
              onChange={handleChange}
              min="0"
              required
            />
          </div>

          {product.createdAt && (
            <div className="mb-3 text-start">
              <label className="form-label">Created At</label>
              <input
                className="form-control"
                type="text"
                value={createdAtText}
                readOnly
              />
            </div>
          )}

          <button type="submit" className="btn btn-primary">
            Submit
          </button>
        </form>
      </div>

      {/* ✅ POPUP MODAL */}
      <Modal show={showPopup} onHide={handleClosePopup} centered>
        <Modal.Header closeButton>
          <Modal.Title>Notification</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p className="mb-0">{popupMsg}</p>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="primary" onClick={handleClosePopup}>
            OK
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default ProductForm;