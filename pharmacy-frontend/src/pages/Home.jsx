import { Link } from "react-router-dom";
import "./Home.css";

const Home = () => {

    return (
        <div className="home-container">
            <div className="home-content">
                <h1>Online Pharmacy System</h1>
                <p>Browse medicines and healthcare products online.</p>
                <Link to="/medicines" className="home-btn">
                    Browse Medicines
                </Link>
            </div>
        </div>
    );
};

export default Home;