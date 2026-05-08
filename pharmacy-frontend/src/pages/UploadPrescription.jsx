import { useState } from "react";
import { uploadPrescription } from "../services/prescriptionService";
import "./UploadPrescription.css";

const UploadPrescription = () => {

    const [file, setFile] = useState(null);

    const handleUpload = async () => {

        if (!file) {
            alert("Please select file");
            return;
        }

        await uploadPrescription(file);

        alert("Prescription uploaded successfully");
    };

    return (
        <div className="upload-container">
            <div className="upload-card">
                <h1>Upload Prescription</h1>
                <p className="upload-description">
                    Upload a PDF copy of your prescription for verification
                </p>
                <div className="upload-form">
                    <div className="file-input-wrapper">
                        <input
                            type="file"
                            id="prescription-file"
                            className="file-input"
                            accept="application/pdf"
                            onChange={(e) => setFile(e.target.files[0])}
                        />
                        <label htmlFor="prescription-file" className="file-input-label">
                            <span className="file-input-icon">📄</span>
                            <span className="file-input-text">
                                <strong>Choose PDF file</strong>
                                <span>or drag and drop</span>
                            </span>
                        </label>
                        {file && <div className="file-name">📎 {file.name}</div>}
                    </div>
                    <button
                        onClick={handleUpload}
                        className="upload-btn"
                    >
                        <span>📤</span>
                        Upload Prescription
                    </button>
                </div>
                <div className="upload-info">
                    <strong>Requirements:</strong>
                    <ul>
                        <li>File must be in PDF format</li>
                        <li>Maximum file size: 5MB</li>
                        <li>Prescription must be valid and recent</li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default UploadPrescription;