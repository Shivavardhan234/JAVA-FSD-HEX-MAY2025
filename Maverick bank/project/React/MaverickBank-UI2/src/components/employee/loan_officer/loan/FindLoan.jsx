import { useState } from "react";
import axios from "axios";

function FindLoan() {
    const [loan, setLoan] = useState(null);
    const [searchId, setSearchId] = useState("");
    const [message, setMessage] = useState("");
    const [paymentHistory, setPaymentHistory] = useState([]);


    const handleError=(err)=>{
        
            console.log(err);
            // Axios errors have a response object
            if (err.response && err.response.data) {
                const errorData = err.response.data;

                
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
                setTimeout(() => {
                setMessage("");
            }, 3000);
            } else {
                setMessage("something went wrong. Please try again later..");
                setTimeout(() => {
                setMessage("");
            }, 3000);
            }
        
    }

    const handleSearch = async () => {
        if (!searchId) return;
        setMessage("");
        setLoan(null);
        setPaymentHistory([]);
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:9090/api/loan/get/by-id/${searchId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setLoan(res.data);

            // Fetch payment history
            const historyRes = await axios.get(`http://localhost:9090/api/loan-payment/get/by-loan-id/${searchId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setPaymentHistory(historyRes.data);
        } catch (err) {
            handleError(err);
        }
    };

    const handleCloseLoan = async () => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(`http://localhost:9090/api/loan/update/close/${loan.id}`, {}, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setMessage("Loan closed successfully.");
            handleSearch();
        } catch (err) {
            handleError(err);
        }
    };

    return (
        <>
            {/* Search Header */}
            <div className="card shadow-sm mb-3">
                <div className="card-header bg-light d-flex justify-content-between align-items-center">
                    <h6 className="mb-0 text-secondary">Find Loan</h6>
                    <div className="input-group" style={{ maxWidth: "300px" }}>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Enter Loan ID"
                            value={searchId}
                            onChange={(e) => setSearchId(e.target.value)}
                        />
                        <button className="btn btn-outline-primary" onClick={handleSearch}>
                            <i className="bi bi-search"></i>
                        </button>
                    </div>
                </div>
            </div>

            {message && <div className="alert alert-info">{message}</div>}

            {loan && (
                <div className="card shadow-sm">
                    <div className="card-header bg-primary text-white">
                        <h5 className="mb-0">Loan Details</h5>
                    </div>
                    <div className="card-body">
                        <h6 className="text-secondary">Account Information</h6>
                        <p><strong>Account Number:</strong> {loan?.account.accountNumber}</p>
                        <p><strong>Account Name:</strong> {loan?.account.accountName}</p>
                        <p><strong>Branch:</strong> {loan?.account.branch.branchName}</p>
                        <p><strong>IFSC:</strong> {loan?.account.branch.ifsc}</p>
                        <p><strong>Account Type:</strong> {loan?.account.accountType.accountType}</p>

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
                                    <td>{loan?.loanPlan.loanName}</td>
                                    <td>{loan?.loanPlan.loanType}</td>
                                    <td>{loan?.loanPlan.loanTerm} months</td>
                                    <td>₹{loan?.loanPlan.principalAmount}</td>
                                    <td>{(loan?.loanPlan.intrestRate * 100).toFixed(2)}%</td>
                                    <td>₹{loan?.loanPlan.installmentAmount}</td>
                                    <td>{loan?.loanPlan.repaymentFrequency} months</td>
                                    <td>{loan?.loanPlan.gracePeriod} months</td>
                                    <td>{loan?.loanPlan.penaltyRate}%</td>
                                </tr>
                            </tbody>
                        </table>

                        <hr />
                        <h6 className="text-secondary mb-3">Loan Payment History</h6>
                        <table className="table table-bordered">
                            <thead className="table-light">
                                <tr>
                                    <th>Due Date</th>
                                    <th>Payment Date</th>
                                    <th>Amount To Pay</th>
                                    <th>Amount Paid</th>
                                    <th>Penalty</th>
                                </tr>
                            </thead>
                            <tbody>
                                {paymentHistory?.length === 0 ? (
                                    <tr><td colSpan="5" className="text-center">No payment records.</td></tr>
                                ) : (
                                    paymentHistory?.map((p, idx) => (
                                        <tr key={idx}>
                                            <td>{p.dueDate}</td>
                                            <td>{p.paymentDate}</td>
                                            <td>₹{p.amountToBePaid}</td>
                                            <td>₹{p.amountPaid}</td>
                                            <td>₹{p.penalty}</td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    <div className="card-footer d-flex justify-content-end gap-2">
                        {loan?.status === "ACTIVE" && loan?.isCleared && (
                            <button className="btn btn-success" onClick={handleCloseLoan}>
                                Close Loan
                            </button>
                        )}
                    </div>
                </div>
            )}
        </>
    );
}

export default FindLoan;
