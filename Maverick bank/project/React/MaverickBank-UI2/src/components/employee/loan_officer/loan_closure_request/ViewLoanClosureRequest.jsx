import { useEffect, useState } from "react";
import axios from "axios";
import { Table } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getLoanClosureRequest } from "../../../../store/actions/LoanClosureRequestAction";
import { useNavigate } from "react-router-dom";

function ViewLoanClosureRequest() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const loanRequest =useSelector(state=>state.loanClosureStore.loanClosureRequest);

    const [loanPayments, setLoanPayments] = useState([]);
    const [error, setError] = useState("");
    const [message, setMessage] = useState("");


   

    const handleError = (err) => {
        console.error(err);
        if (err.response && err.response.data) {
            const errorData = err.response.data;
            const firstKey = Object.keys(errorData)[0];
            setError(errorData[firstKey]);
        } else {
            setError("Something went wrong. Please try again.");
        }
    };


    useEffect(()=>{
        fetchPayments(loanRequest.loan.id);
    },[])

 
    const fetchPayments = async (loanId) => {
        try {
            const token = localStorage.getItem("token");
            const res = await axios.get(
                `http://localhost:9090/api/loan-payment/get/by-loan-id/${loanId}`,
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            setLoanPayments(res.data);
        } catch (err) {
            handleError(err);
        }
    };

    const handleAccept = async () => {
        setError("");
        setMessage("");
        try {
            const token = localStorage.getItem("token");
            const res = await axios.put(
                `http://localhost:9090/api/loan-closure/update/accept/${loanRequest.loan.id}`,
                {},
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            setMessage("Loan closure request accepted.");
            getLoanClosureRequest(dispatch)(loanRequest.id);
        } catch (err) {
            handleError(err);
        }
    };

    const handleReject = async () => {
        setError("");
        setMessage("");
        try {
            const token = localStorage.getItem("token");
            const res = await axios.put(
                `http://localhost:9090/api/loan-closure/update/reject/${loanRequest.loan.id}`,
                {},
                {
                    headers: { Authorization: `Bearer ${token}` },
                }
            );
            setMessage("Loan closure request rejected.");
            getLoanClosureRequest(dispatch)(loanRequest.id);
        } catch (err) {
            handleError(err);
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
                            Loans Closure Requests
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        View Loan Closure Request
                    </li>
                </ol>
            </nav>
        
        <div className="container mt-4 d-flex justify-content-center">
           
            <div className="card shadow" style={{ width: "70rem" }}>
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Loan Closure Request</h5>
                </div>

                <div className="card-body">
                    {error && <div className="alert alert-danger">{error}</div>}
                    {message && <div className="alert alert-success">{message}</div>}

                    {loanRequest && (
                        <>
                            <div className="mb-3">
                                <h6>Loan Details</h6>
                                <p><strong>Loan Name:</strong> {loanRequest.loan.loanPlan.loanName}</p>
                                <p><strong>Loan Type:</strong> {loanRequest.loan.loanPlan.loanType}</p>
                                <p><strong>Due Date:</strong> {loanRequest.loan.dueDate}</p>
                                <p><strong>Total Penalty:</strong> ₹{loanRequest.loan.totalPenalty}</p>
                                <p><strong>Loan Status:</strong> {loanRequest.loan.cleared? "CLEARED":"PENDING"}</p>
                                <p><strong>Active Status:</strong> {loanRequest.loan.status}</p>
                            </div>

                            <hr />

                            <h6>Loan Plan</h6>
                            <Table bordered>
                                <thead className="table-secondary">
                                    <tr>
                                        <th>Term (months)</th>
                                        <th>Principal</th>
                                        <th>Interest Rate (%)</th>
                                        <th>Installment</th>
                                        <th>Penalty Rate (%)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>{loanRequest.loan.loanPlan.loanTerm}</td>
                                        <td>₹{loanRequest.loan.loanPlan.principalAmount}</td>
                                        <td>{loanRequest.loan.loanPlan.interestRate}</td>
                                        <td>₹{loanRequest.loan.loanPlan.installmentAmount}</td>
                                        <td>{loanRequest.loan.loanPlan.penaltyRate}</td>
                                    </tr>
                                </tbody>
                            </Table>

                            <hr />

                            <h6>Loan Payments</h6>
                            <Table bordered responsive>
                                <thead className="table-light">
                                    <tr>
                                        <th>Due Date</th>
                                        <th>Payment Date</th>
                                        <th>To Be Paid</th>
                                        <th>Penalty</th>
                                        <th>Paid</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {loanPayments && loanPayments.map((p, index) => (
                                        <tr key={index}>
                                            <td>{p.dueDate}</td>
                                            <td>{p.paymentDate}</td>
                                            <td>₹{p.amountToBePaid}</td>
                                            <td>₹{p.penalty}</td>
                                            <td>₹{p.amountPaid}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        </>
                    )}
                </div>

                {loanRequest && loanRequest.requestStatus=="PENDING" && (
                    <div className="card-footer d-flex justify-content-end gap-3">
                        <button className="btn btn-success" onClick={()=>{handleAccept()}}>
                            Accept
                        </button>
                        <button className="btn btn-danger" onClick={()=>{handleReject()}}>
                            Reject
                        </button>
                    </div>
                )}
            </div>
        </div>
        </>
    );
}

export default ViewLoanClosureRequest;
