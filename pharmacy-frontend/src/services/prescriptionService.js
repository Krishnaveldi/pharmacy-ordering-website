import API from "../api/axios";

export const uploadPrescription = async (file) => {

    const formData = new FormData();

    formData.append("file", file);

    return await API.post(
        "/prescriptions/upload",
        formData,
        {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        }
    );
};