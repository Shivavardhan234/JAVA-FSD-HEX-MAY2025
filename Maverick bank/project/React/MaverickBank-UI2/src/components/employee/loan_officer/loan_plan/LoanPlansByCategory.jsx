import { useEffect, useState } from "react";
import axios from "axios";

function LoanPlansByCategory() {
    const [loanPlans, setLoanPlans] = useState([]);
    const [selectedType, setSelectedType] = useState("ALL");
    const [message, setMessage] = useState("");

    const loanTypes = [
        "ALL",
        "BIKE_LOAN",
        "CAR_LOAN",
        "HOME_LOAN",
        "STUDENT_LOAN",
        "GOLD_LOAN",
        "PERSONAL_LOAN",
        "CROP_LOAN",
    ];

    useEffect(() => {
        fetchLoanPlans();
    }, [selectedType]);

    const fetchLoanPlans = async () => {
        try {
            const token = localStorage.getItem("token");
            const url =
                selectedType === "ALL"
                    ? "http://localhost:9090/api/loan-plan/get/all"
                    : `http://localhost:9090/api/loan-plan/get/by-loan-type/${selectedType}`;

            const res = await axios.get(url, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            setLoanPlans(res.data);
            setMessage(res.data.length === 0 ? "No loan plans found." : "");
        } catch (err) {
            setMessage("Failed to fetch loan plans.");
            setLoanPlans([]);
        }
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                <h6 className="mb-0">Loan Plans</h6>
                <select
                    className="form-select w-auto"
                    value={selectedType}
                    onChange={(e) => setSelectedType(e.target.value)}
                >
                    {loanTypes.map((type, idx) => (
                        <option key={idx} value={type}>
                            {type.replace("_", " ")}
                        </option>
                    ))}
                </select>
            </div>

            <div className="card-body">
                {message && <div className="alert alert-info">{message}</div>}

                {loanPlans.length > 0 && (
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
                            {loanPlans.map((plan, idx) => (
                                <tr key={idx}>
                                    <td>{plan.loanName}</td>
                                    <td>{plan.loanType}</td>
                                    <td>{plan.loanTerm} months</td>
                                    <td>₹{plan.principalAmount}</td>
                                    <td>{(plan.intrestRate * 100).toFixed(2)}%</td>
                                    <td>₹{plan.installmentAmount}</td>
                                    <td>{plan.repaymentFrequency} months</td>
                                    <td>{plan.gracePeriod} months</td>
                                    <td>{plan.penaltyRate}%</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
        </div>
    );
}

export default LoanPlansByCategory;
