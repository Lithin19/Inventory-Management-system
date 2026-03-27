import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from "./components/Navbar";
import Home from "./pages/HomePage";
import AddProduct from "./pages/AddProduct";
import Inventory from "./pages/Inventory";
import UpdateProduct from "./pages/UpdateProduct";

export function App(){
  return(
    <>
      
      <BrowserRouter>
      <NavBar/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/addProduct" element={<AddProduct/>}/>
        <Route path="/inventory" element={<Inventory/>}/>
        <Route path="/updateProduct/:id" element={<UpdateProduct/>}/>
      </Routes>
      </BrowserRouter>
    </>
  )
}