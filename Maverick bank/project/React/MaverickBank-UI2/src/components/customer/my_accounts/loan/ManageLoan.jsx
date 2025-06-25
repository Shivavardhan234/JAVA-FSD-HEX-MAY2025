import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, Outlet, Link } from 'react-router-dom';
import axios from 'axios';
import { getLoan } from '../../../../store/actions/LoanAction';
import { getBankAccount } from '../../../../store/actions/BankAccountAction';

function ManageLoan() {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const accountId = localStorage.getItem("accountId");
    const loan = useSelector(state => state.loanStore.loan);
    const [showPenalty, setShowPenalty] = useState(false);
    const [message, setMessage] = useState("");
    const loanId = localStorage.getItem("loanId");
    const account = useSelector(state => state.bankAccount.account);


    const [showPayments, setShowPayments] = useState(false);
    const [payments, setPayments] = useState([]);



    useEffect(() => {
            getBankAccount(dispatch)(accountId);
        }, [])



    const fetchPayments = async () => {
        try {
            const token = localStorage.getItem("token");
            const response = await axios.get(`http://localhost:9090/api/loan-payment/get/by-loan-id/${loanId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setPayments(response.data);
        } catch (err) {
            handleError(err);
        }
    };

    const handleError = (err) => {
        console.log(err);
        if (err.response && err.response.data) {
            const errorData = err.response.data;
            const firstKey = Object.keys(errorData)[0];
            setMessage(errorData[firstKey]);
        } else {
            setMessage("Something went wrong. Please try again later...");
        }
        setTimeout(() => setMessage(""), 3000);
    };

    useEffect(() => { getLoan(dispatch)(loanId) }, []);


    const handleLoanClosure = async () => {
        const purpose = prompt("Enter reason to request loan closure:");
        if (!purpose || !purpose.trim()) return;

        try {
            await axios.post(`http://localhost:8080/api/loan-closure/add/${loanId}/${purpose}`, {}, {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                }
            });
            setMessage("Loan closure request submitted successfully.");
            setTimeout(() => setMessage(""), 3000);
        } catch (err) {
            handleError(err);
        }
    };



    return (
        <div className="container-fluid px-0">
            <div className="bg-light rounded-3 w-100 mx-0 px-0" style={{ minHeight: '85vh' }}>

                {/* Breadcrumb */}
                <nav aria-label="breadcrumb" className="p-3">
                    <ol className="breadcrumb mb-0">
                        <li className="breadcrumb-item">
                            <span
                                role="button"
                                className="text-primary text-decoration-none"
                                onClick={() =>
                                    navigate(`/customer/myAccounts/manageBankAccount/${account.accountNumber}`)
                                }
                            >
                                Manage Bank Account
                            </span>
                        </li>
                        <li className="breadcrumb-item">
                            <span className="text-muted">My Loans</span>
                        </li>
                        <li className="breadcrumb-item active" aria-current="page">
                            Manage Loan
                        </li>
                    </ol>
                </nav>

                {message && (
                    <div className="alert alert-warning mx-3" role="alert">
                        {message}
                    </div>
                )}

                {/* Loan Details */}
                {loan && (
                    <div className="card mx-3">
                        <div className="card-header bg-dark text-white fw-semibold">
                            Loan Details
                        </div>
                        <div className="card-body d-flex align-items-stretch">
                            <div
                                className="d-flex align-items-center justify-content-center"
                                style={{ width: '200px', marginLeft: '8px' }}
                            >
                                <i className="bi bi-cash-coin text-secondary" style={{ fontSize: '7rem' }}></i>
                            </div>
                            <div>
                                <p><strong>Loan Name:</strong> {loan?.loanPlan?.loanName}</p>
                                <p><strong>Loan Type:</strong> {loan?.loanPlan?.loanType}</p>
                                <p><strong>Principal:</strong> ₹{loan?.loanPlan?.principalAmount}</p>
                                <p><strong>Term:</strong> {loan?.loanPlan?.loanTerm} months</p>
                                <p><strong>Installment Amount:</strong> ₹{loan?.loanPlan?.installmentAmount}</p>
                                <p><strong>Interest Rate:</strong> {loan?.loanPlan?.interestRate}%</p>
                                <p><strong>Due Date:</strong> {loan?.dueDate}</p>
                                <p><strong>Status:</strong> {loan?.cleared ? "Cleared" : "Active"}</p>
                                {showPenalty && (
                                    <div className="bg-danger bg-opacity-25 p-3 mt-3 rounded d-flex justify-content-between align-items-center">
                                        <span className="fw-semibold text-danger">
                                            Total Penalty: ₹{loan?.totalPenalty}
                                        </span>
                                        <i
                                            className="bi bi-x-circle text-danger fs-5"
                                            style={{ cursor: 'pointer' }}
                                            onClick={() => setShowPenalty(false)}
                                        ></i>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                )}

                {/* Action Buttons */}
                <div className="card shadow-sm mt-4 border-0 mx-3">
                    <div className="card-body">
                        <div className="row g-4 text-center">

                            {/* View Penalty */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-danger rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}
                                    onClick={() => setShowPenalty(true)}>
                                    <i className="bi bi-exclamation-circle fs-4"></i>
                                </button>
                                <p className="mt-2">View Penalty</p>
                            </div>

                            {/* Pay Installment */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <Link to="/customer/myAccounts/payInstallment">
                                    <button className="btn btn-outline-success rounded-circle d-flex justify-content-center align-items-center"
                                        style={{ width: '80px', height: '80px' }}>
                                        <i className="bi bi-currency-rupee fs-4"></i>
                                    </button>
                                </Link>
                                <p className="mt-2">Pay Installment</p>
                            </div>

                            {/* View Payment History */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}
                                    onClick={() => { fetchPayments(); setShowPayments(true); }}>
                                    <i className="bi bi-clock-history fs-4"></i>
                                </button>
                                <p className="mt-2">Payment History</p>
                            </div>

                            {/* Close Loan */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <Link to="/customer/myAccounts/loanClosure">
                                    <button className="btn btn-outline-warning rounded-circle d-flex justify-content-center align-items-center"
                                        style={{ width: '80px', height: '80px' }}>
                                        <i className="bi bi-x-circle fs-4"></i>
                                    </button>
                                </Link>
                                <p className="mt-2">Request Closure</p>
                            </div>

                        </div>
                    </div>
                    {showPayments && (
                        <div className="card mt-4 mx-3 shadow">
                            <div className="card-header bg-secondary text-white fw-semibold d-flex justify-content-between align-items-center">
                                <span>Loan Payment History</span>
                                <button className="btn btn-sm btn-light" onClick={() => { setShowPayments(false) }}>Close Payments</button>
                            </div>
                            <div className="card-body table-responsive">
                                {payments.length > 0 ? (
                                    <table className="table table-bordered table-striped mb-0">
                                        <thead className="table-dark">
                                            <tr>
                                                <th>Amount Paid</th>
                                                <th>Amount To Be Paid</th>
                                                <th>Due Date</th>
                                                <th>Payment Date</th>
                                                <th>Penalty</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {payments.map((p, index) => (
                                                <tr key={index}>
                                                    <td>₹{p.amountPaid}</td>
                                                    <td>₹{p.amountToBePaid}</td>
                                                    <td>{p.dueDate}</td>
                                                    <td>{p.paymentDate}</td>
                                                    <td>₹{p.penalty}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p className="text-muted">No payments found.</p>
                                )}
                            </div>
                        </div>
                    )}
                </div>

            </div>
            <Outlet />
        </div>
    );
}

export default ManageLoan;
