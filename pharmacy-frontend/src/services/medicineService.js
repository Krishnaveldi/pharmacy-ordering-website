import API from "../api/axios";

export const getMedicines = async (page = 0, size = 10) => {

    return await API.get(`/medicines?page=${page}&size=${size}`);
};

export const searchMedicines = async (keyword) => {

    return await API.get(`/medicines/search?keyword=${keyword}`);
};

export const getCategories = async () => {

    return await API.get("/categories");
};