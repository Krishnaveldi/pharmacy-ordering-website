import { useEffect, useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { getMedicines, getCategories } from "../services/medicineService";
import MedicineCard from "../components/MedicineCard";
import "./Home.css";

const Home = () => {
    const [medicines, setMedicines] = useState([]);
    const [categories, setCategories] = useState([]);
    const { user } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        fetchPublicData();
    }, []);

    const fetchPublicData = async () => {
        try {
            const medRes = await getMedicines(0, 8); // fetch top 8
            setMedicines(medRes.data.content || []);

            const catRes = await getCategories();
            setCategories(catRes.data || []);
        } catch (error) {
            console.error("Failed to load public data", error);
        }
    };

    const handleAddToCart = (medicineId) => {
        if (!user) {
            alert("Please login to buy medicines.");
            navigate("/login");
            return;
        }
        // If logged in, maybe redirect to medicines page or handle here
        navigate("/medicines");
    };

    return (
        <div className="home-container">
            <div className="hero-section">
                <div className="hero-content">
                    <h1>Your Health, Our Priority</h1>
                    <p>Order medicines online and get them delivered to your doorstep.</p>
                    {!user && (
                        <div className="hero-buttons">
                            <Link to="/register" className="hero-btn primary">Join Now</Link>
                            <Link to="/login" className="hero-btn secondary">Login</Link>
                        </div>
                    )}
                </div>
            </div>

            <div className="categories-section">
                <h2>Browse by Category</h2>
                <div className="categories-grid">
                    {categories.length > 0 ? (
                        categories.map(cat => (
                            <div key={cat.id} className="category-card">
                                <h3>{cat.name}</h3>
                                <p>{cat.description}</p>
                            </div>
                        ))
                    ) : (
                        <p>No categories available</p>
                    )}
                </div>
            </div>

            <div className="featured-medicines-section">
                <h2>Featured Medicines</h2>
                <div className="medicines-grid">
                    {medicines.length > 0 ? (
                        medicines.map(med => (
                            <MedicineCard 
                                key={med.id} 
                                medicine={med} 
                                onAdd={handleAddToCart} 
                            />
                        ))
                    ) : (
                        <p>No medicines available at the moment.</p>
                    )}
                </div>
                <div className="view-all-container">
                    <Link to="/medicines" className="view-all-btn">View All Medicines</Link>
                </div>
            </div>
        </div>
    );
};

export default Home;