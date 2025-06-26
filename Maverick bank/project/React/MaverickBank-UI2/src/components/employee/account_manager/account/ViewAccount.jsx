import { useEffect, useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

function ViewAccount() {
    const navigate = useNavigate();
    const account = useSelector(state => state.bankAccount.account);
    const [accountHolders, setAccountHolders] = useState([]);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");

    const handleError = (err) => {
        console.error(err);
        if (err.response?.data) {
            const key = Object.keys(err.response.data)[0];
            setError(err.response.data[key]);
        } else {
            setError("Something went wrong.");
        }
    };


    useEffect(()=>{
        getAccountHolders();
    },[]);

    const getAccountHolders = async () => {
        setError("");
        setMessage("");
        setAccountHolders([]);

        const token = localStorage.getItem("token");
        try {
           

            const holdersRes = await axios.get(
                `http://localhost:9090/api/customer-account/get/by-account-id/${account.id}`,
                { headers: { Authorization: `Bearer ${token}` } }
            );

            setAccountHolders(holdersRes.data.map(item => item.accountHolder));
        } catch (err) {
            handleError(err);
        }
    };

    const toggleStatus = async () => {
        if (!account) return;
        if(account.accountStatus==="CLOSED"){
            return;
        }
        const token = localStorage.getItem("token");
        const newStatus = account.accountStatus === "OPEN" ? "SUSPENDED" : "OPEN";

        try {
            const res = await axios.put(
                `http://localhost:9090/api/account/update/status/${account.id}/${newStatus}`,
                {},
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setMessage(`Account status updated to ${newStatus}.`);
        } catch (err) {
            handleError(err);
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
                            onClick={() => navigate(-1)}
                            className="text-decoration-none text-primary"
                        >
                            Accounts By Category
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Account Details
                    </li>
                </ol>
            </nav>
        
        <div className="container mt-4 d-flex justify-content-center">
            
            <div className="card shadow w-100" style={{ maxWidth: "900px" }}>
                <div className="card-header d-flex justify-content-between align-items-center">
                    <div>
                        <h5 className="mb-0">Bank Account Details</h5>
                    </div>
                    
                </div>

                <div className="card-body">
                    {error && <div className="alert alert-danger">{error}</div>}
                    {message && <div className="alert alert-success">{message}</div>}

                    {account && (
                        <>
                            <div className="mb-3">
                                <h6>Account Details</h6>
                                <p><strong>ID:</strong> {account.id}</p>
                                <p><strong>Account Number:</strong> {account.accountNumber}</p>
                                <p><strong>Account Name:</strong> {account.accountName}</p>
                                <p><strong>Status:</strong> {account.accountStatus}</p>
                                <p><strong>Branch:</strong> {account.branch.branchName} ({account.branch.ifsc})</p>
                                <p><strong>Account Type:</strong> {account.accountType.accountType}</p>
                            </div>

                            <hr />

                            <div>
                                <h6>Account Holder(s)</h6>
                                {accountHolders.length === 0 ? (
                                    <p>No holders found.</p>
                                ) : (
                                    accountHolders.map((holder, idx) => (
                                        <div key={idx} className="border rounded p-2 mb-2">
                                            <p><strong>Name:</strong> {holder.name}</p>
                                            <p><strong>Dob:</strong> {holder.dob}</p>
                                            <p><strong>Gender:</strong> {holder.gender}</p>
                                            <p><strong>Contact Number:</strong> {holder.contactNumber}</p>
                                            <p><strong>Email:</strong> {holder.email}</p>
                                            <p><strong>Address:</strong> {holder.address}</p>
                                            <p><strong>Aadhar card:</strong> {holder.aadharNumber}</p>
                                            <p><strong>PAN:</strong> {holder.panCardNumber}</p>

                                        </div>
                                    ))
                                )}
                            </div>
                        </>
                    )}
                </div>

                {account && (
                    <div className="card-footer text-end">
                        {account.accountStatus === "OPEN" && (
                            <button className="btn btn-danger me-2" onClick={toggleStatus}>
                                Suspend Account
                            </button>
                        )}
                        {account.accountStatus === "SUSPENDED" && (
                            <button className="btn btn-success" onClick={toggleStatus}>
                                Activate Account
                            </button>
                        )}
                    </div>
                )}
            </div>
        </div>
        </>
    );
}

export default ViewAccount;
