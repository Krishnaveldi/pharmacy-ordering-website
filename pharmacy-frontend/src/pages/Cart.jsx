import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getCart, removeCartItem } from "../services/cartService";
import { placeOrder } from "../services/orderService";
import "./Cart.css";

const Cart = () => {
    const [cart, setCart] = useState(null);
    const [checkoutMode, setCheckoutMode] = useState(false);
    const [address, setAddress] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            const response = await getCart();
            setCart(response.data);
        } catch (error) {
            console.error(error);
        }
    };

    const handleRemove = async (id) => {
        await removeCartItem(id);
        fetchCart();
    };

    const handleCheckout = async () => {
        if (!address) {
            alert("Please provide a delivery address");
            return;
        }

        try {
            await placeOrder({ deliveryAddress: address });
            alert("Order placed successfully!");
            navigate("/orders");
        } catch (error) {
            alert(error.response?.data?.message || "Failed to place order. Do you have a required prescription?");
        }
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
                                <div key={item.id} className="cart-item">
                                    <div className="item-info">
                                        <h2 className="item-name">{item.medicine?.name}</h2>
                                        <p className="item-quantity">Quantity: {item.quantity}</p>
                                    </div>
                                    <div className="item-price">₹{item.price * item.quantity}</div>
                                    <button
                                        onClick={() => handleRemove(item.id)}
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
                                <strong>₹{cart.items.reduce((total, item) => total + (item.price * item.quantity), 0)}</strong>
                            </div>
                            <div className="total-amount">
                                Total: ₹{cart.items.reduce((total, item) => total + (item.price * item.quantity), 0)}
                            </div>
                            
                            {checkoutMode ? (
                                <div className="checkout-form">
                                    <textarea 
                                        placeholder="Enter full delivery address..."
                                        value={address}
                                        onChange={(e) => setAddress(e.target.value)}
                                        rows={3}
                                        className="address-input"
                                    />
                                    <button onClick={handleCheckout} className="checkout-btn confirm">
                                        Confirm Order
                                    </button>
                                    <button onClick={() => setCheckoutMode(false)} className="checkout-btn cancel">
                                        Cancel
                                    </button>
                                </div>
                            ) : (
                                <button onClick={() => setCheckoutMode(true)} className="checkout-btn">
                                    Proceed to Checkout
                                </button>
                            )}
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default Cart;