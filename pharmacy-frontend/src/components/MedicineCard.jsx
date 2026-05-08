import "./MedicineCard.css";

const MedicineCard = ({ medicine, onAdd }) => {

    return (
        <div className="medicine-card">
            <div className="medicine-image">
                {medicine.imageUrl ? (
                    <img
                        src={medicine.imageUrl}
                        alt={medicine.name}
                    />
                ) : (
                    "💊"
                )}
            </div>

            <div className="medicine-content">
                <h2 className="medicine-title">
                    {medicine.name}
                </h2>

                <p className="medicine-description">
                    {medicine.description}
                </p>

                <div className="medicine-footer">
                    <span className="medicine-price">
                        ₹{medicine.price}
                    </span>

                    <button
                        onClick={() => onAdd(medicine.id)}
                        className="medicine-add-btn"
                    >
                        Add To Cart
                    </button>
                </div>
            </div>
        </div>
    );
};

export default MedicineCard;