import "./AdminDashboard.css";

const AdminDashboard = () => {
    return (
        <div className="admin-container">
            <div className="admin-wrapper">
                <div className="admin-header">
                    <h1>Admin Dashboard</h1>
                    <p>Manage medicines, prescriptions, inventory and orders.</p>
                </div>
                <div className="admin-grid">
                    <div className="admin-card">
                        <div className="admin-card-icon">💊</div>
                        <h2>Medicines</h2>
                        <p>Add, edit, and manage medicine inventory</p>
                        <button className="admin-card-btn">Manage</button>
                    </div>
                    <div className="admin-card">
                        <div className="admin-card-icon">📋</div>
                        <h2>Prescriptions</h2>
                        <p>Review and process prescription requests</p>
                        <button className="admin-card-btn">Review</button>
                    </div>
                    <div className="admin-card">
                        <div className="admin-card-icon">📦</div>
                        <h2>Orders</h2>
                        <p>Track and manage customer orders</p>
                        <button className="admin-card-btn">View Orders</button>
                    </div>
                    <div className="admin-card">
                        <div className="admin-card-icon">📊</div>
                        <h2>Reports</h2>
                        <p>View sales and inventory reports</p>
                        <button className="admin-card-btn">View Reports</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;