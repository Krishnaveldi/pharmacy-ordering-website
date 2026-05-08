import { useEffect, useState } from "react";
import { getOrders } from "../services/orderService";
import "./Orders.css";

const Orders = () => {

    const [orders, setOrders] = useState([]);

    useEffect(() => {
        fetchOrders();
    }, []);

    const fetchOrders = async () => {

        const response = await getOrders();

        setOrders(response.data);
    };

    return (
        <div className="orders-container">
            <div className="orders-wrapper">
                <div className="orders-header">
                    <h1>My Orders</h1>
                </div>
                <div className="orders-list">
                    {orders.length === 0 ? (
                        <div className="empty-orders">
                            <p>No orders yet</p>
                        </div>
                    ) : (
                        orders.map((order) => (
                            <div key={order.id} className="order-card">
                                <div className="order-header">
                                    <h2 className="order-id">Order #{order.id}</h2>
                                    <span className={`order-status status-${order.status?.toLowerCase()}`}>
                                        {order.status}
                                    </span>
                                </div>
                                <div className="order-details">
                                    <div className="detail-item">
                                        <div className="detail-label">Total Amount</div>
                                        <div className="order-total">₹{order.totalAmount}</div>
                                    </div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default Orders;