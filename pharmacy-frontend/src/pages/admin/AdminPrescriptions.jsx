import { useEffect, useState } from "react";
import { getAllPrescriptionsAdmin, approvePrescriptionAdmin } from "../../services/adminService";

const AdminPrescriptions = () => {
    const [prescriptions, setPrescriptions] = useState([]);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const res = await getAllPrescriptionsAdmin();
            setPrescriptions(res.data || []);
        } catch (error) {
            console.error(error);
        }
    };

    const handleApprove = async (id) => {
        try {
            await approvePrescriptionAdmin(id);
            alert("Approved!");
            fetchData();
        } catch (error) {
            alert("Failed to approve");
        }
    };

    return (
        <div>
            <div className="admin-view-header">
                <h1>Prescriptions</h1>
            </div>

            <div className="admin-table-container">
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>User Email</th>
                            <th>File URL</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {prescriptions.map(p => (
                            <tr key={p.id}>
                                <td>{p.id}</td>
                                <td>{p.user?.email}</td>
                                <td>
                                    <a href={p.fileUrl} target="_blank" rel="noreferrer" style={{ color: '#3b82f6' }}>
                                        View File
                                    </a>
                                </td>
                                <td>
                                    <span style={{ 
                                        padding: '4px 8px', 
                                        borderRadius: '12px', 
                                        background: p.status === 'APPROVED' ? '#d1fae5' : '#fef3c7', 
                                        color: p.status === 'APPROVED' ? '#065f46' : '#92400e',
                                        fontSize: '0.85rem',
                                        fontWeight: '600'
                                    }}>
                                        {p.status}
                                    </span>
                                </td>
                                <td>
                                    {p.status === 'PENDING' && (
                                        <button className="approve" onClick={() => handleApprove(p.id)}>Approve</button>
                                    )}
                                </td>
                            </tr>
                        ))}
                        {prescriptions.length === 0 && (
                            <tr>
                                <td colSpan="5" style={{ textAlign: 'center' }}>No prescriptions found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default AdminPrescriptions;
