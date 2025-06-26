import { useState } from "react";
import axios from "axios";

function FindReport() {
    const [reportType, setReportType] = useState("REGULATORY");
    const [reportId, setReportId] = useState("");
    const [reportData, setReportData] = useState(null);
    const [message, setMessage] = useState("");

    const handleSearch = async () => {
        if (!reportId) {
            setMessage("Please enter a Report ID.");
            setReportData(null);
            return;
        }

        const token = localStorage.getItem("token");
        let url = "";

        if (reportType === "REGULATORY") {
            url = `http://localhost:9090/api/regulatory-report/get/by-id/${reportId}`;
        } else {
            url = `http://localhost:9090/api/financial-performance-report/get/by-id/${reportId}`;
        }

        try {
            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setReportData(res.data);
            setMessage("");
        } catch (err) {
            setReportData(null);
            setMessage("Report not found or an error occurred.");
        }
    };

    return (
        <div className="card shadow-sm">
            {/* Header */}
            <div className="card-header d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Find Report</h5>
                <div className="d-flex gap-2 align-items-center">
                    <select
                        className="form-select"
                        style={{ width: "250px" }}
                        value={reportType}
                        onChange={(e) => setReportType(e.target.value)}
                    >
                        <option value="REGULATORY">Regulatory Report</option>
                        <option value="FINANCIAL">Financial Performance Report</option>
                    </select>

                    <input
                        type="number"
                        className="form-control"
                        style={{ width: "120px" }}
                        placeholder="Report ID"
                        value={reportId}
                        onChange={(e) => setReportId(e.target.value)}
                    />

                    <button className="btn btn-primary" onClick={handleSearch}>
                        Search
                    </button>
                </div>
            </div>

            {/* Body */}
            <div className="card-body">
                {message && <div className="alert alert-danger">{message}</div>}

                {reportData && (
                    <>
                        {reportData.totalActiveLoans ? (
                            <ul className="list-group list-group-flush">
                                <h6 className="text-secondary mb-3">Regulatory Report</h6>
                                <li className="list-group-item">Report Date: {reportData.reportDate}</li>
                                <li className="list-group-item">Total Customers: {reportData.totalCustomers}</li>
                                <li className="list-group-item">Total Accounts: {reportData.totalAccounts}</li>
                                <li className="list-group-item">Total Active Loans: {reportData.totalActiveLoans}</li>
                                <li className="list-group-item">KYC Compliant Accounts: {reportData.kycCompliantAccounts}</li>
                            </ul>
                        ) : (
                            <ul className="list-group list-group-flush">
                                <h6 className="text-secondary mb-3">Financial Performance Report</h6>
                                <li className="list-group-item">Report Date: {reportData.reportDate}</li>
                                <li className="list-group-item">Total Deposits: ₹{reportData.totalDeposits}</li>
                                <li className="list-group-item">Loans Issued: {reportData.totalNumberOfLoansIssued}</li>
                                <li className="list-group-item">Active Loans: {reportData.totalNumberOfActiveLoans}</li>
                                <li className="list-group-item">Loan Amount Issued: ₹{reportData.totalLoanAmountIssued}</li>
                                <li className="list-group-item">Loan Repayment: ₹{reportData.totalLoanRepayment}</li>
                                <li className="list-group-item">Total Transactions: {reportData.totalTransactions}</li>
                                <li className="list-group-item">Transaction Amount: ₹{reportData.totaltransactionAmount}</li>
                            </ul>
                        )}
                    </>
                )}
            </div>
        </div>
    );
}

export default FindReport;
