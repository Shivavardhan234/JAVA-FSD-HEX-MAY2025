import { useEffect, useState } from "react";
import {  useNavigate } from "react-router-dom";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getLoan } from "../../../../store/actions/LoanAction";

function ViewLoan() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const loan = useSelector(state => state.loanStore.loan);
    const [paymentHistory, setPaymentHistory] = useState([]);
    const [message, setMessage] = useState("");

    useEffect(() => {
        if (loan) {
            fetchPaymentHistory(loan.id);
        }
    }, [loan]);

    const fetchPaymentHistory = async (loanId) => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(
                `http://localhost:9090/api/loan-payment/get/by-loan-id/${loanId}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setPaymentHistory(res.data);

        } catch (err) {
            handleError(err);
            
        }
    };

    const handleCloseLoan = async () => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.put(
                `http://localhost:9090/api/loan/update/close/${loan.id}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setMessage("Loan closed successfully.");
            getLoan(dispatch)(loan.id);
        } catch (err) {
            handleError(err);
            
        }
    };

        const handleError = (err) => {

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

    

    return (
        <>
          {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Loans</li>

                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(`/loanOfficer/loanManagementSidebar/loansByCategory`)}
                            className="text-decoration-none text-primary"
                        >
                            Loans By Category
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        View Loan
                    </li>
                </ol>
            </nav>
            {message && <div className="alert alert-info">{message}</div>}

            {loan && loan.account && (
                <div className="card shadow-sm">
                    <div className="card-header bg-primary text-white">
                        <h5 className="mb-0">Loan Details</h5>
                    </div>
                    <div className="card-body">
                        {/* Account Details */}
                        <h6 className="text-secondary">Account Information</h6>
                        <p><strong>Account Number:</strong> {loan.account.accountNumber}</p>
                        <p><strong>Account Name:</strong> {loan.account.accountName}</p>
                        <p><strong>Branch:</strong> {loan.account.branch.branchName}</p>
                        <p><strong>IFSC:</strong> {loan.account.branch.ifsc}</p>
                        <p><strong>Account Type:</strong> {loan.account.accountType.accountType}</p>

                        <hr />

                        {/* Loan Plan Table */}
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
                                    <td>{loan.loanPlan.loanName}</td>
                                    <td>{loan.loanPlan.loanType}</td>
                                    <td>{loan.loanPlan.loanTerm} months</td>
                                    <td>₹{loan.loanPlan.principalAmount}</td>
                                    <td>{(loan.loanPlan.interestRate)}%</td>
                                    <td>₹{loan.loanPlan.installmentAmount}</td>
                                    <td>{loan.loanPlan.repaymentFrequency} months</td>
                                    <td>{loan.loanPlan.gracePeriod} months</td>
                                    <td>{loan.loanPlan.penaltyRate}%</td>
                                </tr>
                            </tbody>
                        </table>

                        <hr />

                        {/* Payment History */}
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
                                    <tr>
                                        <td colSpan="5" className="text-center">No payment records.</td>
                                    </tr>
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
                        {loan?.status === "ACTIVE" && loan?.cleared && (
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

export default ViewLoan;
