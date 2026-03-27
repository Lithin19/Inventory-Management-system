import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Inventory() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    axios
      .get("http://localhost:8085/products")
      .then((response) => setProducts(response.data))
      .catch((err) => {
        console.error(err);
        setError("Failed to load products");
      });
  }, []);
  const handleEditButton=(id)=>{
    navigate(`/updateProduct/${id}`);
  }
  const handleDeleteButoon = (id)=>{
    const del = window.confirm("Do you really want to delete");
    if(del){
        axios
      .delete( `http://localhost:8085/products/${id}`)
      .then(() => setProducts(products.filter(prod=>prod.id!==id)))
      .catch((err) => console.error("Delete Failed",err));
    }
  }

  return (
    <>
      {error && <p className="text-danger">{error}</p>}

      <table className="table table-dark table-hover ">
        <thead>
          <tr>
            <th>PRODUCT ID</th>
            <th>NAME</th>
            <th>CATEGORY</th>
            <th>PRICE</th>
            <th>QUANTITY</th>
            <th>UPDATE</th>
            <th>DELETE</th>
          </tr>
        </thead>

        <tbody>
          {products.map((prod) => (
            <tr key={prod.id}>
              <td>{prod.id}</td>
              <td>{prod.name}</td>
              <td>{prod.category}</td>
              <td>{prod.price}</td>
              <td>{prod.quantity}</td>
              <td><button className="btn btn-secondary" onClick={()=>handleEditButton(prod.id)}>UPDATE QUANTITY</button></td>
              <td><button className="btn btn-danger" onClick={()=>handleDeleteButoon(prod.id)}>Delete</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
}

export default Inventory;