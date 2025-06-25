import { useEffect, useState } from "react";
import axios from "axios";
import { getBankAccount } from "../../../../store/actions/BankAccountAction";
import { useDispatch, useSelector } from "react-redux";

function FindLoanOpeningApplication() {
    const [searchId, setSearchId] = useState("");
    const [loanApplication, setLoanApplication] = useState(null);
    const [message, setMessage] = useState("");
    const dispatch = useDispatch();
    const account = useSelector((state) => state.bankAccount.account);

    const handleSearch = async () => {
        setMessage("");
        setLoanApplication(null);
        if (!searchId) {
            setMessage("Please enter an application ID.");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:9090/api/loan-opening-application/get/by-id/${searchId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setLoanApplication(res.data);
            getBankAccount(dispatch)(res.data.account.id);
        } catch (err) {
            setMessage("Application not found or access denied.");
        }
    };

    const handleAccept = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(
                `http://localhost:9090/api/loan-opening-application/update/accept/${loanApplication.id}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setMessage("Application accepted.");
            handleSearch(); // re-fetch
        } catch (err) {
            setMessage("Failed to accept.");
        }
    };

    const handleReject = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(
                `http://localhost:9090/api/loan-opening-application/update/reject/${loanApplication.id}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setMessage("Application rejected.");
            handleSearch(); // re-fetch
        } catch (err) {
            setMessage("Failed to reject.");
        }
    };

    return (
        <>
                       {/* Header with search */}
    

            {/* Message */}
            {message && <div className="alert alert-info">{message}</div>}

            
            
            
        <div className="card shadow-sm">
            {/* Header */}
            <div className="card-header bg-light d-flex justify-content-between align-items-center">
                <h6 className="mb-0 text-secondary">Find Loan Opening Application</h6>
                <div className="input-group" style={{ maxWidth: "300px" }}>
                    <input
                        type="number"
                        className="form-control"
                        placeholder="Enter ID"
                        value={searchId}
                        onChange={(e) => setSearchId(e.target.value)}
                    />
                    <button className="btn btn-outline-primary" onClick={handleSearch}>
                        <i className="bi bi-search"></i>
                    </button>
                </div>
           
                    </div>

                    <div className="card-body">
                        <h6 className="text-secondary">Account Information</h6>
                        <p><strong>Account Number:</strong> {account?.accountNumber}</p>
                        <p><strong>Account Name:</strong> {account?.accountName}</p>
                        <p><strong>Branch:</strong> {account?.branch?.branchName}</p>
                        <p><strong>IFSC:</strong> {account?.branch?.ifsc}</p>
                        <p><strong>Account Type:</strong> {account?.accountType?.accountType}</p>

                        <hr />

                        <h6 className="text-secondary mb-3">Loan Plan Details</h6>
                        <table className="table table-bordered">
                            <thead className="table-light">
                                <tr>
                                    <th>Loan Name</th>
                                    <th>Type</th>
                                    <th>Term</th>
                                    <th>Principal</th>
                                    <th>Interest</th>
                                    <th>Installment</th>
                                    <th>Repayment Freq</th>
                                    <th>Grace Period</th>
                                    <th>Penalty Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{loanApplication?.loanPlan?.loanName}</td>
                                    <td>{loanApplication?.loanPlan?.loanType}</td>
                                    <td>{loanApplication?.loanPlan?.loanTerm} months</td>
                                    <td>₹{loanApplication?.loanPlan?.principalAmount}</td>
                                    <td>{loanApplication?.loanPlan?.interestRate}%</td>
                                    <td>₹{loanApplication?.loanPlan?.installmentAmount}</td>
                                    <td>{loanApplication?.loanPlan?.repaymentFrequency} months</td>
                                    <td>{loanApplication?.loanPlan?.gracePeriod} months</td>
                                    <td>{loanApplication?.loanPlan?.penaltyRate}%</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    <div className="card-footer d-flex justify-content-end gap-2">
                        {loanApplication?.status === "PENDING" && (
                            <>
                                <button className="btn btn-success" onClick={handleAccept}>
                                    Accept
                                </button>
                                <button className="btn btn-danger" onClick={handleReject}>
                                    Reject
                                </button>
                            </>
                        )}
                    </div>
                </div>
            
        </>
    );
}

export default FindLoanOpeningApplication;
