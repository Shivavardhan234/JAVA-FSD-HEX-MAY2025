import { useState } from 'react';
import axios from 'axios';
function FindAccountOpeningApplication() {
    const [applicationId, setApplicationId] = useState("");
    const [applicationData, setApplicationData] = useState(null);
    const [accountHolders, setAccountHolders] = useState([]);
    const [message, setMessage] = useState("");

    const handleSearch = async () => {
        setApplicationData(null);
        setAccountHolders([]);
        setMessage("");

        const token = localStorage.getItem("token");
        const bearerAuth = `Bearer ${token}`;

        try {
            const response = await axios.get(`http://localhost:9090/api/account-opening-application/get/by-id/${applicationId}`, {
                headers: { Authorization: bearerAuth },
            });
            setApplicationData(response.data);

            const holderRes = await axios.get(`http://localhost:9090/api/customer-account-opening-application/get/by-application-id/${applicationId}`, {
                headers: { Authorization: bearerAuth },
            });
            setAccountHolders(holderRes.data.map(item => item.accountHolder));
        } catch (err) {
            console.error(err);
            setMessage("Failed to fetch application details.");
        }
    };

    const handleApprove = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = `Bearer ${token}`;
            await axios.put(`http://localhost:9090/api/account-opening-application/update/approve/${applicationId}`, {}, {
                headers: { Authorization: bearerAuth },
            });
            alert("Application approved successfully");
            handleSearch();
        } catch (err) {
            console.error(err);
            alert("Failed to approve application");
        }
    };

    const handleReject = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = `Bearer ${token}`;
            await axios.put(`http://localhost:9090/api/account-opening-application/update/reject/${applicationId}`, {}, {
                headers: { Authorization: bearerAuth },
            });
            alert("Application rejected successfully");
            handleSearch();
        } catch (err) {
            console.error(err);
            alert("Failed to reject application");
        }
    };

    return (
        <div className="container py-4">
            <div className="card shadow">
                {/* Header */}
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Find Application</h5>
                    <div className="input-group" style={{ maxWidth: "300px" }}>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Enter Application ID"
                            value={applicationId}
                            onChange={(e) => setApplicationId(e.target.value)}
                        />
                        <button className="btn btn-outline-secondary" onClick={handleSearch}>
                            <i className="bi bi-search"></i>
                        </button>
                    </div>
                </div>

                {/* Body */}
                <div className="card-body overflow-auto" style={{ maxHeight: '70vh' }}>
                    {message && <p className="text-danger">{message}</p>}

                    {applicationData && (
                        <>
                            {/* Application Details */}
                            <div className="card mb-3 border">
                                <div className="card-header bg-light text-dark border-bottom border-secondary">
                                    <h6 className="mb-0">Application Details</h6>
                                </div>
                                <div className="card-body">
                                    <p><strong>Branch Name:</strong> {applicationData.branch?.branchName || 'N/A'}</p>
                                    <p><strong>IFSC:</strong> {applicationData.branch?.ifsc || 'N/A'}</p>
                                    <p><strong>Account Type:</strong> {applicationData.accountType?.accountType || 'N/A'}</p>
                                    <hr />
                                    <p><strong>Account Name:</strong> {applicationData.accountName || 'N/A'}</p>
                                    <p><strong>Purpose:</strong> {applicationData.purpose || 'N/A'}</p>
                                    <p><strong>Customer Approval Status:</strong> {applicationData.customerApprovalStatus || 'N/A'}</p>
                                    <p><strong>Employee Approval Status:</strong> {applicationData.employeeApprovalStatus || 'N/A'}</p>
                                    <p><strong>Application Date & Time:</strong> {applicationData.applicationDateTime ? new Date(applicationData.applicationDateTime).toLocaleString() : 'N/A'}</p>
                                </div>
                            </div>

                            {/* Account Holders */}
                            <h6>Account Holders</h6>
                            {accountHolders.length === 0 ? (
                                <p>No account holders found.</p>
                            ) : (
                                accountHolders.map((holder, idx) => (
                                    <div key={idx} className="mb-3 p-3 border rounded bg-light">
                                        <h6>Holder {idx + 1}</h6>
                                        <p><strong>Name:</strong> {holder.name || 'N/A'}</p>
                                        <p><strong>Date of Birth:</strong> {holder.dob || 'N/A'}</p>
                                        <p><strong>Gender:</strong> {holder.gender || 'N/A'}</p>
                                        <p><strong>Contact:</strong> {holder.contactNumber || 'N/A'}</p>
                                        <p><strong>Email:</strong> {holder.email || 'N/A'}</p>
                                        <p><strong>Address:</strong> {holder.address || 'N/A'}</p>
                                        <p><strong>PAN Card Number:</strong> {holder.panCardNumber || 'N/A'}</p>
                                        <p><strong>Aadhar Number:</strong> {holder.aadharNumber || 'N/A'}</p>
                                    </div>
                                ))
                            )}
                        </>
                    )}
                </div>


                {/* Footer */}
                {applicationData?.employeeApprovalStatus === 'PENDING' &&applicationData?.customerApprovalStatus === 'ACCEPTED'&& (
                    <div className="card-footer d-flex justify-content-end gap-3">
                        <button className="btn btn-success" onClick={handleApprove}>Approve</button>
                        <button className="btn btn-danger" onClick={handleReject}>Reject</button>
                    </div>
                )}
            </div>
        </div>
    );

}
export default FindAccountOpeningApplication;