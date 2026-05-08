import API from "../api/axios";

export const addToCart = async (data) => {

    return await API.post("/cart/add", data);
};

export const getCart = async () => {

    return await API.get("/cart");
};

export const removeCartItem = async (id) => {

    return await API.delete(`/cart/remove/${id}`);
};