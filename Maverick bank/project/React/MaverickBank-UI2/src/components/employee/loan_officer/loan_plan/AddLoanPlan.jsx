import { useState } from "react";
import axios from "axios";

function AddLoanPlan() {

    const [loanName, setLoanName] = useState("");
    const [loanType, setLoanType] = useState("BIKE_LOAN");
    const [loanTerm, setLoanTerm] = useState("");
    const [principalAmount, setPrincipalAmount] = useState("");
    const [intrestRate, setIntrestRate] = useState("");
    const [installmentAmount, setInstallmentAmount] = useState("");
    const [repaymentFrequency, setRepaymentFrequency] = useState("");
    const [gracePeriod, setGracePeriod] = useState("");
    const [penaltyRate, setPenaltyRate] = useState("");
    const [prePaymentPenalty, setPrePaymentPenalty] = useState("");


    const [message, setMessage] = useState("");

    const loanTypes = [
        "BIKE_LOAN",
        "CAR_LOAN",
        "HOME_LOAN",
        "STUDENT_LOAN",
        "GOLD_LOAN",
        "PERSONAL_LOAN",
        "CROP_LOAN",
    ];



    const handleSubmit = async () => {
        if (
        loanName === "" ||
        loanTerm === "" ||
        principalAmount === "" ||
        intrestRate === "" ||
        installmentAmount === "" ||
        repaymentFrequency === "" ||
        gracePeriod === "" ||
        penaltyRate === "" ||
        prePaymentPenalty === ""
    ) {
            setMessage("Please fill in all the fields.");
            setTimeout(() => setMessage(""), 3000); 
            return;
        }
        setMessage("");

        try {
            const loanPlan = {
                'loanName': loanName,
                'loanType': loanType,
                'loanTerm': loanTerm,
                'principalAmount': principalAmount,
                'interestRate': intrestRate,
                'installmentAmount': installmentAmount,
                'repaymentFrequency': repaymentFrequency,
                'gracePeriod': gracePeriod,
                'penaltyRate': penaltyRate,
                'prePaymentPenalty': prePaymentPenalty,
            };

            const token = localStorage.getItem("token");
            await axios.post(
                "http://localhost:9090/api/loan-plan/add",
                loanPlan,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setMessage("Loan plan added successfully.");
            setLoanName("");
            setLoanType("BIKE_LOAN");
            setLoanTerm("");
            setPrincipalAmount("");
            setIntrestRate("");
            setInstallmentAmount("");
            setRepaymentFrequency("");
            setGracePeriod("");
            setPenaltyRate("");
            setPrePaymentPenalty("");

        } catch (err) {
            setMessage("Failed to add loan plan.");
        }
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header bg-primary text-white">
                <h5 className="mb-0">Add New Loan Plan</h5>
            </div>
            <div className="card-body">
                {message && <div className="alert alert-info">{message}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Loan Name</label>
                        <input
                            type="text"
                            className="form-control"
                            name="loanName"
                            value={loanName}
                            onChange={($e) => { setLoanName($e.target.value) }}
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Loan Type</label>
                        <select
                            className="form-select"
                            name="loanType"
                            value={loanType}
                            onChange={($e) => { setLoanType($e.target.value) }}
                        >
                            {loanTypes.map((type, idx) => (
                                <option key={idx} value={type}>
                                    {type.replace("_", " ")}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="row">
                        <div className="mb-3 col-md-6">
                            <label className="form-label">Loan Term (months)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="loanTerm"
                                value={loanTerm}
                                onChange={($e) => { setLoanTerm($e.target.value) }}
                                required
                            />
                        </div>
                        <div className="mb-3 col-md-6">
                            <label className="form-label">Principal Amount (₹)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="principalAmount"
                                value={principalAmount}
                                onChange={($e) => { setPrincipalAmount($e.target.value) }}
                                required
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="mb-3 col-md-6">
                            <label className="form-label">Interest Rate (7.00 for 7%)</label>
                            <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                name="intrestRate"
                                value={intrestRate}
                                onChange={($e) => { setIntrestRate($e.target.value) }}
                                required
                            />
                        </div>
                        <div className="mb-3 col-md-6">
                            <label className="form-label">Installment Amount (₹)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="installmentAmount"
                                value={installmentAmount}
                                onChange={($e) => { setInstallmentAmount($e.target.value) }}
                                required
                            />
                        </div>
                    </div>

                    <div className="row">
                        <div className="mb-3 col-md-4">
                            <label className="form-label">Repayment Frequency (months)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="repaymentFrequency"
                                value={repaymentFrequency}
                                onChange={($e) => { setRepaymentFrequency($e.target.value) }}
                                required
                            />
                        </div>
                        <div className="mb-3 col-md-4">
                            <label className="form-label">Grace Period (months)</label>
                            <input
                                type="number"
                                className="form-control"
                                name="gracePeriod"
                                value={gracePeriod}
                                onChange={($e) => { setGracePeriod($e.target.value) }}
                                required
                            />
                        </div>
                        <div className="mb-3 col-md-4">
                            <label className="form-label">Penalty Rate (%)</label>
                            <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                name="penaltyRate"
                                value={penaltyRate}
                                onChange={($e) => { setPenaltyRate($e.target.value) }}
                                required
                            />
                        </div>
                        <div className="mb-3 col-md-4">
                            <label className="form-label">Pre-Payment Penalty (₹)</label>
                            <input
                                type="number"
                                step="0.01"
                                className="form-control"
                                name="PrePaymentPenalty"
                                value={prePaymentPenalty}
                                onChange={($e) => { setPrePaymentPenalty($e.target.value) }}
                                required
                            />
                        </div>
                    </div>

                    <button type="button" className="btn btn-primary mt-2" onClick={() => { handleSubmit() }}>
                        Add Loan Plan
                    </button>
                </form>
            </div>
        </div>
    );
}

export default AddLoanPlan;
