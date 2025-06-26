import {  useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import {  useNavigate } from "react-router-dom";
import { getLoanOpeningApplication } from "../../../../store/actions/LoanOpeningApplicationAction";

function ViewLoanOpeningApplication() {
    const navigate = useNavigate();
    const loanApplication = useSelector(state => state.loanOpeningApplicationStore.application);
    const selectedLoan = loanApplication?.loanPlan;
    const [message, setMessage] = useState("");
    const dispatch = useDispatch();

  

   



    const account = loanApplication?.account;


 


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
            setMessage("Application accepted successfully.");
            getLoanOpeningApplication(dispatch)(loanApplication.id);
        } catch (err) {
            setMessage("Failed to accept application.");
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
            getLoanOpeningApplication(dispatch)(loanApplication.id);
        } catch (err) {
            setMessage("Failed to reject application.");
        }
    };

    return (
        <>
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Loans</li>

                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(-1)}
                            className="text-decoration-none text-primary"
                        >
                            Loan Opening Applications
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Manage Loan Opening Application
                    </li>
                </ol>
            </nav>
            <div className="card shadow-sm">


                <div className="card-header bg-primary text-white">
                    <h5 className="mb-0">Loan Opening Application</h5>
                </div>

                <div className="card-body">

                    <>
                        {message && (
                            <div className="alert alert-info" role="alert">
                                {message}
                            </div>
                        )}

                        {/* Account Details */}
                        <h6 className="text-secondary">Account Information</h6>
                        <p><strong>Account Number:</strong> {account?.accountNumber}</p>
                        <p><strong>Account Name:</strong> {account?.accountName}</p>
                        <p><strong>Branch:</strong> {account?.branch?.branchName}</p>
                        <p><strong>IFSC:</strong> {account?.branch?.ifsc}</p>
                        <p><strong>Account Type:</strong> {account?.accountType?.accountType}</p>

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
                                    <td>{selectedLoan.loanName}</td>
                                    <td>{selectedLoan.loanType}</td>
                                    <td>{selectedLoan.loanTerm} months</td>
                                    <td>₹{selectedLoan.principalAmount}</td>
                                    <td>{selectedLoan.interestRate}%</td>
                                    <td>₹{selectedLoan.installmentAmount}</td>
                                    <td>{selectedLoan.repaymentFrequency} months</td>
                                    <td>{selectedLoan.gracePeriod} months</td>
                                    <td>{selectedLoan.penaltyRate}%</td>

                                </tr>
                            </tbody>
                        </table>
                    </>

                </div>

                <div className="card-footer d-flex justify-content-end gap-2">
                    {loanApplication.status === "PENDING" && (
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

export default ViewLoanOpeningApplication;
