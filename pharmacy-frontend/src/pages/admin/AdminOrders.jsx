import { useEffect, useState } from "react";
import { getAllOrdersAdmin } from "../../services/adminService";

const AdminOrders = () => {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const res = await getAllOrdersAdmin();
            setOrders(res.data || []);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <div className="admin-view-header">
                <h1>All Orders</h1>
            </div>

            <div className="admin-table-container">
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>User Email</th>
                            <th>Total Amount</th>
                            <th>Status</th>
                            <th>Delivery Address</th>
                        </tr>
                    </thead>
                    <tbody>
                        {orders.map(order => (
                            <tr key={order.id}>
                                <td>#{order.id}</td>
                                <td>{order.user?.email || "N/A"}</td>
                                <td>₹{order.totalAmount}</td>
                                <td>
                                    <span style={{ 
                                        padding: '4px 8px', 
                                        borderRadius: '12px', 
                                        background: '#dbeafe', 
                                        color: '#1e40af',
                                        fontSize: '0.85rem',
                                        fontWeight: '600'
                                    }}>
                                        {order.status}
                                    </span>
                                </td>
                                <td>{order.deliveryAddress}</td>
                            </tr>
                        ))}
                        {orders.length === 0 && (
                            <tr>
                                <td colSpan="5" style={{ textAlign: 'center' }}>No orders found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminOrders;
