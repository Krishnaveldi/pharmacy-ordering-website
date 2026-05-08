import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../services/authService";
import "./Register.css";

const Register = () => {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        fullName: "",
        email: "",
        password: "",
        phone: "",
        address: ""
    });

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await registerUser(form);

            alert("Registration successful");

            navigate("/login");

        } catch (error) {
            alert(error.response.data.message);
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <h2>Register</h2>
                <form onSubmit={handleSubmit} className="register-form">
                <input
                    type="text"
                    placeholder="Full Name"
                    onChange={(e) => setForm({
                        ...form,
                        fullName: e.target.value
                    })}
                />

                <input
                    type="email"
                    placeholder="Email"
                    onChange={(e) => setForm({
                        ...form,
                        email: e.target.value
                    })}
                />

                <input
                    type="password"
                    placeholder="Password"
                    onChange={(e) => setForm({
                        ...form,
                        password: e.target.value
                    })}
                />

                <input
                    type="text"
                    placeholder="Phone"
                    onChange={(e) => setForm({
                        ...form,
                        phone: e.target.value
                    })}
                />
                <textarea
                    placeholder="Address"
                    onChange={(e) => setForm({
                        ...form,
                        address: e.target.value
                    })}
                />

                <button className="register-btn">
                    Register
                </button>
            </form>
            <div className="register-footer">
                <p>Already have an account? <a href="/login">Login here</a></p>
            </div>
            </div>
        </div>
    );
};

export default Register;