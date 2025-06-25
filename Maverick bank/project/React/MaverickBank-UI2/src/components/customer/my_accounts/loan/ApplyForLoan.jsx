import { useEffect, useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getBankAccount } from "../../../../store/actions/BankAccountAction";
import { useNavigate } from "react-router-dom";

function ApplyForLoan() {
    const dispatch = useDispatch();
    const accountId = localStorage.getItem("accountId");
    const token = localStorage.getItem("token");
    const navigate = useNavigate();

    const [loanPlans, setLoanPlans] = useState([]);
    const [selectedType, setSelectedType] = useState("");
    const [selectedLoan, setSelectedLoan] = useState(null);
    const [purpose, setPurpose] = useState("");

    useEffect(() => {
        if (accountId) getBankAccount(dispatch)(accountId);
    }, []);

    const account = useSelector(state => state.bankAccount.account);

    const fetchLoanPlansByType = (type) => {
        setSelectedType(type);
        setLoanPlans([]); // clear old data

        axios.get(`http://localhost:9090/api/loan-plan/get/by-loan-type/${type}`, {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(res => setLoanPlans(res.data))
            .catch(err => {
                console.error("Failed to fetch loan plans", err);
                setLoanPlans([]);
            });
    };

    const handleApply = () => {
        if (!selectedLoan || !purpose.trim()) {
            alert("Please select a loan and enter a purpose.");
            return;
        }

        axios.post(
            `http://localhost:9090/api/loan-opening-application/add/${account.id}/${selectedLoan.id}?purpose=${encodeURIComponent(purpose)}`,
            {},
            { headers: { Authorization: `Bearer ${token}` } }
        )
            .then(res => {
                alert("Loan application submitted successfully!");
                setSelectedLoan(null);
                setPurpose("");
            })
            .catch(err => {
                console.error("Failed to apply for loan", err);
                alert("Application failed.");
            });
    };

    const loanTypes = [
        "BIKE_LOAN", "CAR_LOAN", "CROP_LOAN", "GOLD_LOAN",
        "HOME_LOAN", "PERSONAL_LOAN", "STUDENT_LOAN"
    ];

    return (
        <div className="container mt-4">
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Accounts</li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`/customer/myAccounts/manageBankAccount/${account?.accountNumber}`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Bank Account
                        </span>
                    </li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`/customer/myAccounts/myLoans`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            My Loans
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Loan Application
                    </li>
                </ol>
            </nav>
            <div className="card shadow">
                <div className="card-header bg-dark text-white fw-bold">
                    Apply For Loan
                </div>

                <div className="card-body">
                    {!selectedLoan && (
                        <div className="card mb-4">
                            <div className="card-header">
                                Select Loan Type
                            </div>
                            <div className="card-body">
                                <select
                                    className="form-select mb-3"
                                    value={selectedType}
                                    onChange={(e) => fetchLoanPlansByType(e.target.value)}
                                >
                                    <option value="">-- Select Loan Type --</option>
                                    {loanTypes.map(type => (
                                        <option key={type} value={type}>{type.replaceAll("_", " ")}</option>
                                    ))}
                                </select>

                                {loanPlans.length > 0 && (
                                    <div className="table-responsive">
                                        <table className="table table-hover table-dark">
                                            <thead>
                                                <tr>
                                                    <th>#</th>
                                                    <th>Loan Name</th>
                                                    <th>Type</th>
                                                    <th>Term</th>
                                                    <th>Principal</th>
                                                    <th>Interest</th>
                                                    <th>Installment</th>
                                                    <th>Repayment Freq</th>
                                                    <th>Grace Period</th>
                                                    <th>Penalty rate</th>
                                                    <th>Select</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {loanPlans.map((plan, index) => (
                                                    <tr key={plan.id}>
                                                        <td>{index + 1}</td>
                                                        <td>{plan.loanName}</td>
                                                        <td>{plan.loanType}</td>
                                                        <td>{plan.loanTerm} months</td>
                                                        <td>₹{plan.principalAmount}</td>
                                                        <td>{(plan.intrestRate * 100).toFixed(2)}%</td>
                                                        <td>₹{plan.installmentAmount}</td>
                                                        <td>{plan.repaymentFrequency} months</td>
                                                        <td>{plan.gracePeriod} months</td>
                                                        <td>{plan.penaltyRate}%</td>
                                                        <td>
                                                            <button
                                                                className="btn btn-sm btn-success"
                                                                onClick={() => setSelectedLoan(plan)}
                                                            >
                                                                Select
                                                            </button>
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}

                    {selectedLoan && (
                        <div className="table-responsive mb-4">
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
                                        <th>Penalty rate</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>{selectedLoan.loanName}</td>
                                        <td>{selectedLoan.loanType}</td>
                                        <td>{selectedLoan.loanTerm} months</td>
                                        <td>₹{selectedLoan.principalAmount}</td>
                                        <td>{(selectedLoan.intrestRate * 100).toFixed(2)}%</td>
                                        <td>₹{selectedLoan.installmentAmount}</td>
                                        <td>{selectedLoan.repaymentFrequency} months</td>
                                        <td>{selectedLoan.gracePeriod} months</td>
                                        <td>{selectedLoan.penaltyRate}%</td>
                                        <td>
                                            <button
                                                className="btn btn-sm btn-warning"
                                                onClick={() => setSelectedLoan(null)}
                                            >
                                                Change Loan Plan
                                            </button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>

                            <div className="mb-3">
                                <label className="form-label">Purpose</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    value={purpose}
                                    onChange={(e) => setPurpose(e.target.value)}
                                    placeholder="Enter loan purpose"
                                />
                            </div>
                        </div>
                    )}
                </div>

                {selectedLoan && (
                    <div className="card-footer text-end">
                        <button className="btn btn-primary" onClick={handleApply}>
                            Apply Loan
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default ApplyForLoan;
