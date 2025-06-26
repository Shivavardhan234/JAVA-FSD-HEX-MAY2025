
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

function ViewFinancialPerformanceReport() {
    const reportData = useSelector(state => state.financialPerformanceStore.report);
    const navigate = useNavigate();





    return (
        <> <nav aria-label="breadcrumb" className="mb-3">
            <ol className="breadcrumb">
                <li className="breadcrumb-item text-muted">Report</li>

                <li className="breadcrumb-item">
                    <span
                        role="button"
                        onClick={() => navigate(-1)}
                        className="text-decoration-none text-primary"
                    >
                        Financial Performance Reports
                    </span>
                </li>
                <li className="breadcrumb-item active" aria-current="page">
                    View  Financial Performance Report
                </li>
            </ol>
        </nav>
        <div className="card shadow-sm">

            
            {/* Header */}
            <div className="card-header d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Financial Performance Report</h5>

            </div>

            {/* Body */}
            <div className="card-body">

                {reportData && (


                    <ul className="list-group list-group-flush">
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
            </div>
        </div>
        </>
    );
}

export default ViewFinancialPerformanceReport;
