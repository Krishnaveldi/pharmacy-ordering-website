import API from "../api/axios";

// Medicines
export const createMedicine = async (data) => {
    return await API.post("/admin/medicines", data);
};

export const updateMedicine = async (id, data) => {
    return await API.put(`/admin/medicines/${id}`, data);
};

export const deleteMedicine = async (id) => {
    return await API.delete(`/admin/medicines/${id}`);
};

// Categories
export const createCategory = async (data) => {
    return await API.post("/admin/categories", data);
};

// Orders
export const getAllOrdersAdmin = async () => {
    return await API.get("/admin/orders");
};

// Prescriptions
export const getAllPrescriptionsAdmin = async () => {
    return await API.get("/admin/prescriptions");
};

export const approvePrescriptionAdmin = async (id) => {
    return await API.put(`/admin/prescriptions/${id}/approve`);
};
