import { useEffect, useState } from "react";
import {
    getCart,
    removeCartItem
} from "../services/cartService";
import "./Cart.css";

const Cart = () => {

    const [cart, setCart] = useState(null);

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {

        const response = await getCart();

        setCart(response.data);
    };

    const handleRemove = async (id) => {

        await removeCartItem(id);

        fetchCart();
    };

    if (!cart || !cart.items) return <p>Loading...</p>;

    return (
        <div className="cart-container">
            <div className="cart-wrapper">
                <div className="cart-header">
                    <h1>My Cart</h1>
                </div>
                {cart.items && cart.items.length === 0 ? (
                    <div className="empty-cart">
                        <p>Your cart is empty</p>
                    </div>
                ) : (
                    <>
                        <div className="cart-items">
                            {cart.items && cart.items.map((item) => (
                                <div key={item.itemId} className="cart-item">
                                    <div className="item-info">
                                        <h2 className="item-name">{item.medicineName}</h2>
                                        <p className="item-quantity">Quantity: {item.quantity}</p>
                                    </div>
                                    <div className="item-price">₹{item.totalPrice || item.price}</div>
                                    <button
                                        onClick={() => handleRemove(item.itemId)}
                                        className="remove-btn"
                                    >
                                        Remove
                                    </button>
                                </div>
                            ))}
                        </div>
                        <div className="cart-summary">
                            <div className="summary-row">
                                <span>Subtotal:</span>
                                <strong>₹{cart.totalAmount || 0}</strong>
                            </div>
                            <div className="total-amount">
                                Total: ₹{cart.totalAmount || 0}
                            </div>
                            <button className="checkout-btn">
                                Proceed to Checkout
                            </button>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default Cart;