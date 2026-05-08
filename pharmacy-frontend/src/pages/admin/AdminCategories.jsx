import { useEffect, useState } from "react";
import { getCategories } from "../../services/medicineService";
import { createCategory } from "../../services/adminService";

const AdminCategories = () => {
    const [categories, setCategories] = useState([]);
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const res = await getCategories();
            setCategories(res.data || []);
        } catch (error) {
            console.error(error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await createCategory({ name, description });
            alert("Category created");
            setName("");
            setDescription("");
            fetchData();
        } catch (error) {
            alert("Failed to create");
        }
    };

    return (
        <div>
            <div className="admin-view-header">
                <h1>Categories</h1>
            </div>

            <div style={{ background: 'white', padding: '20px', borderRadius: '12px', marginBottom: '30px' }}>
                <h3>Add New Category</h3>
                <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
                    <input 
                        placeholder="Category Name" 
                        required 
                        value={name}
                        onChange={e => setName(e.target.value)} 
                        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
                    />
                    <input 
                        placeholder="Description" 
                        required 
                        value={description}
                        onChange={e => setDescription(e.target.value)} 
                        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc', flexGrow: 1 }}
                    />
                    <button type="submit" className="admin-add-btn">Add</button>
                </form>
            </div>

            <div className="admin-table-container">
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        {categories.map(cat => (
                            <tr key={cat.id}>
                                <td>{cat.id}</td>
                                <td>{cat.name}</td>
                                <td>{cat.description}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminCategories;
