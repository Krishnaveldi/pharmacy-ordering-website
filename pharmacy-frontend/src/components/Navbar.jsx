import { Link, useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import "./Navbar.css";

const Navbar = () => {
    const { user, logout } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <nav className="navbar">
            <div className="navbar-container">
                <Link to="/" className="navbar-brand">
                    <span className="navbar-brand-icon">💊</span>
                    Pharmacy App
                </Link>

                <div className="navbar-menu">
                    <Link to="/" className="navbar-link">
                        Home
                    </Link>
                    <Link to="/medicines" className="navbar-link">
                        Medicines
                    </Link>
                    <Link to="/orders" className="navbar-link">
                        Orders
                    </Link>
                    <Link to="/upload-prescription" className="navbar-link">
                        Upload Rx
                    </Link>
                    <Link to="/cart" className="navbar-link">
                        🛒 Cart
                    </Link>

                    {user ? (
                        <div className="navbar-user">
                            <div className="navbar-user-info">
                                <span className="navbar-username">
                                    {user.role === "admin" ? "👨‍💼 Admin" : "👤 User"}
                                </span>
                            </div>
                            <button
                                onClick={handleLogout}
                                className="navbar-logout-btn"
                            >
                                Logout
                            </button>
                        </div>
                    ) : (
                        <>
                            <Link to="/login" className="navbar-link">
                                Login
                            </Link>
                            <Link to="/register" className="navbar-link">
                                Register
                            </Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;