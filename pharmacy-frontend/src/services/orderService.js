import API from "../api/axios";

export const placeOrder = async (data) => {

    return await API.post("/orders/place", data);
};

export const getOrders = async () => {

    return await API.get("/orders/my-orders");
};