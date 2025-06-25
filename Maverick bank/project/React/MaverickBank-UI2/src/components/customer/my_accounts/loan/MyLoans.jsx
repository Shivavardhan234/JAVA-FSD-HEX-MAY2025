import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Card, Button } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getBankAccount } from "../../../../store/actions/BankAccountAction";

function MyLoans() {

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const accountId = localStorage.getItem("accountId");
    const account = useSelector(state => state.bankAccount.account);
    const [loans, setLoans] = useState([]);


    
    useEffect(() => {
        getBankAccount(dispatch)(accountId);
    }, [])

    useEffect(() => {
        const token = localStorage.getItem("token");

        axios
            .get(`http://localhost:9090/api/loan/get/by-account-id-status/${account.id}/ACTIVE`, {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => {
                setLoans(res.data);
            })
            .catch((err) => {
                console.log(err);
                setLoans([]); 
            });
    }, [account.id]);


    const handleManageLoan = (loan)=>{
          if (localStorage.getItem("loanId")) {
                localStorage.removeItem("loanId");
            }
            localStorage.setItem("loanId", loan.id);

            navigate(`/customer/myAccounts/manageLoan`);
            return;
    }

    return (
        <div className="container mt-3">
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Accounts</li>
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
                    <li className="breadcrumb-item active" aria-current="page">
                        My Loans
                    </li>
                </ol>
            </nav>

            {/* My Loans Card */}
            <Card className="shadow mb-4">
                <Card.Header className="bg-dark text-white fw-semibold">
                    My Loans
                </Card.Header>
                <Card.Body style={{ minHeight: "200px" }}>
                    {loans.length === 0 ? (
                        <div className="text-center text-secondary fs-5 mt-5">NO LOANS</div>
                    ) : (
                        <table className="table table-hover table-dark">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">Loan Details</th>
                                    <th scope="col">Manage Loan</th>
                                </tr>
                            </thead>
                            <tbody>
                                {loans.map((loan, index) => (
                                    <tr key={loan.id}>
                                        <th scope="row">{index + 1}</th>
                                        <td>
                                            <h6>{loan.loanPlan.loanName}</h6>
                                            <p className="mb-1">
                                                <strong>Loan ID:</strong> {loan.id}
                                            </p>
                                            <p className="mb-0">
                                                <strong>Loan Type:</strong> {loan.loanPlan.loanType}
                                            </p>
                                        </td>
                                        <td>
                                            <button
                                                className="btn btn-outline-info"
                                                onClick={() => handleManageLoan(loan)}>
                                                Manage Loan
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </Card.Body>
            </Card>

            {/* Buttons Card */}
            <Card className="shadow-sm border-0">
                <Card.Body>
                    <div className="d-flex justify-content-start gap-4">
                        {/* Apply for New Loan */}
                        <div className="d-flex flex-column align-items-center">
                            <button
                                className="btn btn-outline-primary rounded-circle d-flex justify-content-center align-items-center"
                                style={{ width: "80px", height: "80px" }}
                                onClick={() =>
                                    navigate("/customer/myAccounts/applyLoan")
                                }
                            >
                                <i className="bi bi-plus-circle fs-4"></i>
                            </button>
                            <p className="mt-2">Apply for New Loan</p>
                        </div>

                        {/* Loan Opening Applications */}
                        <div className="d-flex flex-column align-items-center">
                            <button
                                className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                style={{ width: "80px", height: "80px" }}
                                onClick={() =>
                                    navigate("/customer/myAccounts/myLoanApplications")
                                }
                            >
                                <i className="bi bi-card-list fs-4"></i>
                            </button>
                            <p className="mt-2">Loan Applications</p>
                        </div>
                    </div>
                </Card.Body>
            </Card>
        </div>
    );
}

export default MyLoans;
