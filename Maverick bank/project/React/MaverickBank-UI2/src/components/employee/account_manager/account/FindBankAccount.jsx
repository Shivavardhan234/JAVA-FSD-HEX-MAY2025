import { useState } from "react";
import axios from "axios";

function FindBankAccount() {
    const [searchType, setSearchType] = useState("by-id");
    const [searchInput, setSearchInput] = useState("");
    const [account, setAccount] = useState(null);
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

    const handleSearch = async () => {
        setError("");
        setMessage("");
        setAccount(null);
        setAccountHolders([]);

        const token = localStorage.getItem("token");
        try {
            let res;
            if (searchType === "by-id") {
                res = await axios.get(`http://localhost:9090/api/account/get/by-id/${searchInput}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
            } else {
                res = await axios.get(`http://localhost:9090/api/account/get/by-account-number/${searchInput}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
            }

            setAccount(res.data);

            const holdersRes = await axios.get(
                `http://localhost:9090/api/customer-account/get/by-account-id/${res.data.id}`,
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
            setAccount(res.data);
            setMessage(`Account status updated to ${newStatus}.`);
        } catch (err) {
            handleError(err);
        }
    };

    return (
        <div className="container mt-4 d-flex justify-content-center">
            <div className="card shadow w-100" style={{ maxWidth: "900px" }}>
                <div className="card-header d-flex justify-content-between align-items-center">
                    <div>
                        <h5 className="mb-0">Find Bank Account</h5>
                    </div>
                    <div className="d-flex gap-2">
                        <select className="form-select" style={{ width: "180px" }} value={searchType} onChange={e => setSearchType(e.target.value)}>
                            <option value="by-id">By Account ID</option>
                            <option value="by-number">By Account Number</option>
                        </select>
                        <input
                            type="text"
                            className="form-control"
                            placeholder={searchType === "by-id" ? "Enter Account ID" : "Enter Account Number"}
                            value={searchInput}
                            onChange={(e) => setSearchInput(e.target.value)}
                        />
                        <button className="btn btn-primary" onClick={handleSearch}>
                            <i className="bi bi-search"></i>
                        </button>
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
    );
}

export default FindBankAccount;
