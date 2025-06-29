import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { getLoan } from "../../../../store/actions/LoanAction";

function PayInstallment() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    
    const [penalty, setPenalty] = useState(0);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    
    const loan = useSelector(state => state.loanStore.loan);

    const handleError = (err) => {
        console.log(err);
        if (err.response && err.response.data) {
            const errorData = err.response.data;
            const firstKey = Object.keys(errorData)[0];
            setError(errorData[firstKey]);
        } else {
            setError("Something went wrong. Please try again later...");
        }
        setTimeout(() => setError(""), 3000);
    };

    

    useEffect(() => {
       
        if (loan?.id) {fetchPenalty()};
    }, [loan]);


     const fetchPenalty = async () => {
            try {
                const token = localStorage.getItem("token");
                const response = await axios.get(`http://localhost:9090/api/loan-payment/get/penalty/${loan.id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setPenalty(response.data);
            } catch (err) {
                handleError(err);
            }
        };

    const handlePay = async () => {
        try {
            const token = localStorage.getItem("token");
            const total = parseFloat(loan.loanPlan.installmentAmount) + parseFloat(penalty);

            await axios.post(`http://localhost:9090/api/loan-payment/add/${loan.id}/${total}`, {}, {
                headers: { Authorization: `Bearer ${token}` }
            });

            setMessage("Installment paid successfully.");
            setTimeout(() => {
                setMessage("");
            }, 2500);
            getLoan(dispatch)(loan.id);
        } catch (err) {
            handleError(err);
        }
    };

    let totalPay;

    if (loan?.loanPlan?.installmentAmount && penalty !== null && penalty !== undefined) {
        totalPay = (parseFloat(loan.loanPlan.installmentAmount) + parseFloat(penalty));
    } else {
        totalPay = "Loading...";
    }


    return (
        <div className="container mt-4">
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Loans</li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`/customer/myAccounts/manageLoan`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Loan
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Pay Installment
                    </li>
                </ol>
            </nav>

            {/* Message Alerts */}
            {message && <div className="alert alert-success">{message}</div>}
            {error && <div className="alert alert-danger">{error}</div>}

            {/* Bootstrap 5 Card */}
            {loan && loan.loanPlan && (
                loan.cleared ? (
                    <div className="alert alert-info text-center mx-auto mt-4" style={{ width: '22rem' }}>
                        This loan is already cleared.
                    </div>
                ) : (
                    <>
                        {/* Bootstrap 5 Card */}
                        <div className="card mx-auto shadow" style={{ width: '22rem' }}>
                            <div className="card-header bg-success text-white fw-semibold">
                                Pay Installment
                            </div>
                            <ul className="list-group list-group-flush">
                                <li className="list-group-item"><strong>Loan ID:</strong> {loan.id}</li>
                                <li className="list-group-item"><strong>Loan Name:</strong> {loan.loanPlan.loanName}</li>
                                <li className="list-group-item"><strong>Loan Type:</strong> {loan.loanPlan.loanType}</li>
                                <li className="list-group-item"><strong>Due Date:</strong> {loan.dueDate}</li>
                                <li className="list-group-item"><strong>Installment:</strong> ₹{loan.loanPlan.installmentAmount}</li>
                                <li className="list-group-item"><strong>Penalty:</strong> ₹{penalty}</li>
                                <li className="list-group-item text-success"><strong>Total To Pay:</strong> ₹{totalPay}</li>
                            </ul>
                        </div>

                        {/* Pay Button */}
                        <div className="text-center mt-4">
                            <button
                                className="btn btn-outline-success rounded-circle d-flex justify-content-center align-items-center mx-auto"
                                style={{ width: '80px', height: '80px' }}
                                onClick={()=>handlePay()}
                            >
                                <i className="bi bi-currency-rupee fs-4"></i>
                            </button>
                            <p className="mt-2">Pay Installment</p>
                        </div>
                    </>
                )
            )}

        </div>
    );
}

export default PayInstallment;
