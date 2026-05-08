import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import { loginUser } from "../services/authService";
import "./Login.css";

const Login = () => {

    const [form, setForm] = useState({
        email: "",
        password: ""
    });

    const { login } = useContext(AuthContext);

    const navigate = useNavigate();

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await loginUser(form);

            login(
                response.data.token,
                response.data.role
            );

            if (response.data.role === "ADMIN") {
                navigate("/admin");
            } else {
                navigate("/medicines");
            }

        } catch (error) {
            alert(error.response.data.message);
        }
    };
    return (
        <div className="login-container">
            <div className="login-card">
                <h2>Login</h2>
                <form onSubmit={handleSubmit} className="login-form">

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

                <button className="login-btn">
                    Login
                </button>
            </form>
            <div className="login-footer">
                <p>Don't have an account? <a href="/register">Register here</a></p>
            </div>
            </div>
        </div>
    );
};

export default Login;