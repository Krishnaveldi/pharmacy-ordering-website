import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Medicines from "./pages/Medicines";
import Cart from "./pages/Cart";
import Orders from "./pages/Orders";
import UploadPrescription from "./pages/UploadPrescription";

// Admin imports
import AdminDashboard from "./pages/AdminDashboard";
import AdminMedicines from "./pages/admin/AdminMedicines";
import AdminCategories from "./pages/admin/AdminCategories";
import AdminPrescriptions from "./pages/admin/AdminPrescriptions";
import AdminOrders from "./pages/admin/AdminOrders";

function App() {
    return (
        <BrowserRouter>
            <Navbar />
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/medicines" element={<Medicines />} />

                {/* User Protected Routes */}
                <Route path="/cart" element={<ProtectedRoute><Cart /></ProtectedRoute>} />
                <Route path="/orders" element={<ProtectedRoute><Orders /></ProtectedRoute>} />
                <Route path="/upload-prescription" element={<ProtectedRoute><UploadPrescription /></ProtectedRoute>} />

                {/* Admin Routes */}
                <Route path="/admin" element={<AdminRoute><AdminDashboard /></AdminRoute>}>
                    <Route index element={<Navigate to="medicines" replace />} />
                    <Route path="medicines" element={<AdminMedicines />} />
                    <Route path="categories" element={<AdminCategories />} />
                    <Route path="prescriptions" element={<AdminPrescriptions />} />
                    <Route path="orders" element={<AdminOrders />} />
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;