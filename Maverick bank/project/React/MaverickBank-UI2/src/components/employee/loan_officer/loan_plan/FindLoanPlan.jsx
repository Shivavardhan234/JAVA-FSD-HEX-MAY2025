import { useState } from "react";
import axios from "axios";

function FindLoanPlan() {
    const [loanPlanId, setLoanPlanId] = useState("");
    const [loanPlan, setLoanPlan] = useState(null);
    const [message, setMessage] = useState("");

    const handleSearch = async () => {
        if (!loanPlanId) {
            setMessage("Please enter a Loan Plan ID.");
            setLoanPlan(null);
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(`http://localhost:9090/api/loan-plan/get/by-id/${loanPlanId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });
            setLoanPlan(res.data);
            setMessage("");
        } catch (err) {
            setLoanPlan(null);
            setMessage("Loan Plan not found.");
        }
    };

    return (
        <>
            <div className="card shadow-sm">
                <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <h6 className="mb-0">Find Loan Plan</h6>
                    <div className="input-group" style={{ maxWidth: "300px" }}>
                        <input
                            type="number"
                            className="form-control"
                            placeholder="Enter ID"
                            value={loanPlanId}
                            onChange={(e) => setLoanPlanId(e.target.value)}
                        />
                        <button className="btn btn-outline-light" onClick={handleSearch}>
                            <i className="bi bi-search"></i>
                        </button>
                    </div>
                </div>

                <div className="card-body">
                    {message && <div className="alert alert-info">{message}</div>}

                    {loanPlan && (
                        <>
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
                                        <td>{loanPlan.loanName}</td>
                                        <td>{loanPlan.loanType}</td>
                                        <td>{loanPlan.loanTerm} months</td>
                                        <td>₹{loanPlan.principalAmount}</td>
                                        <td>{(loanPlan.intrestRate * 100).toFixed(2)}%</td>
                                        <td>₹{loanPlan.installmentAmount}</td>
                                        <td>{loanPlan.repaymentFrequency} months</td>
                                        <td>{loanPlan.gracePeriod} months</td>
                                        <td>{loanPlan.penaltyRate}%</td>
                                    </tr>
                                </tbody>
                            </table>
                        </>
                    )}
                </div>
            </div>
        </>
    );
}

export default FindLoanPlan;
