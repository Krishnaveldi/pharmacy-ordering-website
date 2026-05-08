import { useEffect, useState } from "react";
import { getMedicines, getCategories } from "../../services/medicineService";
import { createMedicine, deleteMedicine } from "../../services/adminService";

const AdminMedicines = () => {
    const [medicines, setMedicines] = useState([]);
    const [categories, setCategories] = useState([]);
    const [showForm, setShowForm] = useState(false);
    
    // Form state
    const [form, setForm] = useState({
        name: "", description: "", manufacturer: "", price: "", 
        stockQuantity: "", dosage: "", requiresPrescription: false, 
        expiryDate: "", manufactureDate: "", imageUrl: "", categoryId: ""
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const medRes = await getMedicines(0, 100);
            setMedicines(medRes.data.content || []);
            
            const catRes = await getCategories();
            setCategories(catRes.data || []);
        } catch (error) {
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure?")) {
            await deleteMedicine(id);
            fetchData();
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createMedicine(form);
            alert("Medicine created!");
            setShowForm(false);
            fetchData();
        } catch (error) {
            alert(error.response?.data?.message || "Failed to create");
        }
    };

    return (
        <div>
            <div className="admin-view-header">
                <h1>Medicines</h1>
                <button className="admin-add-btn" onClick={() => setShowForm(!showForm)}>
                    {showForm ? "Cancel" : "+ Add Medicine"}
                </button>
            </div>

            {showForm && (
                <div style={{ background: 'white', padding: '20px', borderRadius: '12px', marginBottom: '30px' }}>
                    <form onSubmit={handleSubmit} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '15px' }}>
                        <input placeholder="Name" required onChange={e => setForm({...form, name: e.target.value})} />
                        <input placeholder="Description" required onChange={e => setForm({...form, description: e.target.value})} />
                        <input placeholder="Manufacturer" required onChange={e => setForm({...form, manufacturer: e.target.value})} />
                        <input placeholder="Price" type="number" required onChange={e => setForm({...form, price: e.target.value})} />
                        <input placeholder="Stock Quantity" type="number" required onChange={e => setForm({...form, stockQuantity: e.target.value})} />
                        <input placeholder="Dosage" required onChange={e => setForm({...form, dosage: e.target.value})} />
                        
                        <label>
                            Requires Prescription:
                            <input type="checkbox" onChange={e => setForm({...form, requiresPrescription: e.target.checked})} />
                        </label>

                        <div>
                            <label>Mfg Date</label>
                            <input type="date" required onChange={e => setForm({...form, manufactureDate: e.target.value})} />
                        </div>
                        <div>
                            <label>Exp Date</label>
                            <input type="date" required onChange={e => setForm({...form, expiryDate: e.target.value})} />
                        </div>
                        
                        <input placeholder="Image URL (optional)" onChange={e => setForm({...form, imageUrl: e.target.value})} />
                        
                        <select required onChange={e => setForm({...form, categoryId: e.target.value})}>
                            <option value="">Select Category</option>
                            {categories.map(c => (
                                <option key={c.id} value={c.id}>{c.name}</option>
                            ))}
                        </select>
                        
                        <button type="submit" className="admin-add-btn" style={{ gridColumn: 'span 2' }}>Save Medicine</button>
                    </form>
                </div>
            )}

            <div className="admin-table-container">
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Req. Rx</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {medicines.map(med => (
                            <tr key={med.id}>
                                <td>{med.id}</td>
                                <td>{med.name}</td>
                                <td>₹{med.price}</td>
                                <td>{med.stockQuantity}</td>
                                <td>{med.requiresPrescription ? "Yes" : "No"}</td>
                                <td>
                                    <button className="delete" onClick={() => handleDelete(med.id)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminMedicines;
