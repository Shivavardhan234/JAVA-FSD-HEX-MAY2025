import { useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getAccountRequest } from "../../../../store/actions/AccountRequestAction";

function ViewAccountRequest() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const request = useSelector(state => state.accountRequestStore.accountRequest);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");



    const handleAccept = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(`http://localhost:9090/api/account-request/update/accept/${request.id}`, {}, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("Request accepted successfully.");
            getAccountRequest(dispatch)(request.id);
        } catch (err) {
            handleError(err);
        }
    };

    const handleReject = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(`http://localhost:9090/api/account-request/update/reject/${request.id}`, {}, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("Request rejected.");
            getAccountRequest(dispatch)(request.id);
        } catch (err) {
            handleError(err);
        }
    };

    const handleError = (err) => {
        if (err.response?.data) {
            const firstKey = Object.keys(err.response.data)[0];
            setError(err.response.data[firstKey]);
        } else {
            setError("Something went wrong.");
        }
    };

    return (
        <>
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Accounts</li>

                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(`/accountManager/bankAccountsSideBar/accountRequestsByCategory`)}
                            className="text-decoration-none text-primary"
                        >
                            Accounts Request By Category
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Account Request Details
                    </li>
                </ol>
            </nav>

            <div className="container mt-4 d-flex justify-content-center">
                <div className="card shadow w-100" style={{ maxWidth: "800px" }}>
                    <div className="card-header d-flex justify-content-between align-items-center">
                        <h5 className="mb-0">Find Account Request</h5>
                    </div>

                    <div className="card-body">
                        {error && <div className="alert alert-danger">{error}</div>}
                        {message && <div className="alert alert-success">{message}</div>}

                        {request && request.account && (
                            <>
                                <h6 className="text-secondary">Account Details</h6>
                                <p><strong>Account Number:</strong> {request.account.accountNumber}</p>
                                <p><strong>Account Name:</strong> {request.account.accountName}</p>
                                <p><strong>Status:</strong> {request.account.accountStatus}</p>
                                <p><strong>Branch:</strong> {request.account.branch.branchName}</p>
                                <p><strong>Type:</strong> {request.account.accountType.accountType}</p>

                                <hr />
                                <h6 className="text-secondary">Request Type</h6>
                                <p>{request.requestType}</p>
                                <h6 className="text-secondary">Purpose</h6>
                                <p>{request.purpose}</p>
                                <h6 className="text-secondary">Request Status</h6>
                                <p>{request.requestStatus}</p>

                            </>
                        )}
                    </div>

                    {request && request.requestStatus === "PENDING" && (
                        <div className="card-footer text-end d-flex gap-2 justify-content-end">
                            <button className="btn btn-success" onClick={handleAccept}>Accept</button>
                            <button className="btn btn-danger" onClick={handleReject}>Reject</button>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default ViewAccountRequest;
