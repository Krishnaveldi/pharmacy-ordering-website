import { Link } from "react-router-dom";
import "./NotFound.css";

const NotFound = () => {
    return (
        <div className="not-found-container">
            <div className="not-found-content">
                <div className="error-icon">🔍</div>
                <h1 className="error-code">404</h1>
                <h2 className="error-title">Page Not Found</h2>
                <p className="error-description">
                    Sorry, the page you're looking for doesn't exist or has been moved.
                </p>
                <Link to="/" className="home-btn">
                    Back to Home
                </Link>
                <div className="suggestions">
                    <Link to="/medicines" className="suggestion-link">
                        Browse Medicines
                    </Link>
                    <Link to="/cart" className="suggestion-link">
                        View Cart
                    </Link>
                    <Link to="/orders" className="suggestion-link">
                        My Orders
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default NotFound;
