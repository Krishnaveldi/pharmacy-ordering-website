import { useEffect, useState } from "react";
import MedicineCard from "../components/MedicineCard";
import {
    getMedicines,
    searchMedicines
} from "../services/medicineService";
import { addToCart } from "../services/cartService";
import "./Medicines.css";

const Medicines = () => {

    const [medicines, setMedicines] = useState([]);
    const [keyword, setKeyword] = useState("");

    useEffect(() => {
        fetchMedicines();
    }, []);

    const fetchMedicines = async () => {

        const response = await getMedicines();

        setMedicines(response.data.content);
    };

    const handleSearch = async () => {

        const response = await searchMedicines(keyword);

        setMedicines(response.data.content);
    };

    const handleAddToCart = async (medicineId) => {

        const token = localStorage.getItem("token");
        if (!token) {
            alert("Please login first");
            return;
        }

        await addToCart({
            medicineId,
            quantity: 1
        });

        alert("Added to cart");
    };

    return (
        <div className="medicines-container">
            <div className="medicines-wrapper">
                <div className="medicines-header">
                    <h1>Browse Medicines</h1>
                </div>
                <div className="search-section">
                    <input
                        type="text"
                        placeholder="Search medicines"
                        className="search-input"
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                    <button
                        onClick={handleSearch}
                        className="search-btn"
                    >
                        Search
                    </button>
                </div>
                <div className="medicines-grid">
                    {medicines.map((medicine) => (
                        <MedicineCard
                            key={medicine.id}
                            medicine={medicine}
                            onAdd={handleAddToCart}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Medicines;