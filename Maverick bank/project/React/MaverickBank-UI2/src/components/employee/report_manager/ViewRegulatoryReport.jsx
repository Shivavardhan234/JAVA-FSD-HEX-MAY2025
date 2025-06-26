
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

function ViewRegulatoryReport() {
    const reportData = useSelector(state => state.regulatoryReportStore.report);
    const navigate = useNavigate();





    return (
        <>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Report</li>

                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(-1)}
                            className="text-decoration-none text-primary"
                        >
                            Regulatory Reports
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        View Regulatory Report
                    </li>
                </ol>
            </nav>
            <div className="card shadow-sm">
                {/* Header */}
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">View Regulatory Report</h5>

                </div>

                {/* Body */}
                <div className="card-body">

                    {reportData && (


                        <ul className="list-group list-group-flush">
                            <h6 className="text-secondary mb-3">Regulatory Report</h6>
                            <li className="list-group-item">Report Date: {reportData.reportDate}</li>
                            <li className="list-group-item">Total Customers: {reportData.totalCustomers}</li>
                            <li className="list-group-item">Total Accounts: {reportData.totalAccounts}</li>
                            <li className="list-group-item">Total Active Loans: {reportData.totalActiveLoans}</li>
                            <li className="list-group-item">KYC Compliant Accounts: {reportData.kycCompliantAccounts}</li>
                        </ul>


                    )}
                </div>
            </div>
        </>
    );
}

export default ViewRegulatoryReport;
