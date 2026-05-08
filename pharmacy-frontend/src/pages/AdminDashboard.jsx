import { Link, Outlet, useLocation } from "react-router-dom";
import "./AdminDashboard.css";

const AdminDashboard = () => {
    const location = useLocation();

    return (
        <div className="admin-layout">
            <aside className="admin-sidebar">
                <div className="admin-sidebar-header">
                    <h2>Admin Panel</h2>
                </div>
                <nav className="admin-nav">
                    <Link 
                        to="/admin/medicines" 
                        className={`admin-nav-link ${location.pathname.includes('/medicines') ? 'active' : ''}`}
                    >
                        💊 Medicines
                    </Link>
                    <Link 
                        to="/admin/categories" 
                        className={`admin-nav-link ${location.pathname.includes('/categories') ? 'active' : ''}`}
                    >
                        📁 Categories
                    </Link>
                    <Link 
                        to="/admin/prescriptions" 
                        className={`admin-nav-link ${location.pathname.includes('/prescriptions') ? 'active' : ''}`}
                    >
                        📋 Prescriptions
                    </Link>
                    <Link 
                        to="/admin/orders" 
                        className={`admin-nav-link ${location.pathname.includes('/orders') ? 'active' : ''}`}
                    >
                        📦 Orders
                    </Link>
                </nav>
            </aside>
            <main className="admin-main-content">
                <Outlet />
            </main>
        </div>
    );
};

export default AdminDashboard;