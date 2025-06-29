import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";

function MyLoanApplications() {
    const [filter, setFilter] = useState("ALL");
    const [applications, setApplications] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    const account = useSelector(state => state.bankAccount.account);
    const accountId=account?.id;

    useEffect(() => {
        fetchApplications();
    }, [filter, accountId]);

    useEffect(() => {
        fetchApplications();
    }, [page,size]);


      const fetchApplications = async () => {
            try {
                let url =
                    filter === "ALL"
                        ? `http://localhost:9090/api/loan-opening-application/get/by-account-id/${accountId}?page=${page}&size=${size}`
                        : `http://localhost:9090/api/loan-opening-application/get/by-account-id-status/${accountId}/${filter}?page=${page}&size=${size}`;

                const token = localStorage.getItem("token");
                const res = await axios.get(url, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setApplications(res.data);
                setMessage("");
            } catch (err) {
                setApplications([]);
                setMessage("Unable to fetch loan applications.");
            }
        };



    return (
        <div
            className="d-flex flex-column"
            style={{ height: "calc(100vh - 56px)", marginLeft: "70px", padding: "20px" }}
        >
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Accounts</li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`../manageBankAccount/${accountId}`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Bank Account
                        </span>
                    </li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(`../myLoans`)}
                            className="text-decoration-none text-primary"
                        >
                            My Loans
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        My Loan Applications
                    </li>
                </ol>
            </nav>

            <div className="card shadow-sm">
                {/* Header */}
                <div className="card-header d-flex justify-content-between align-items-center bg-light">
                    <select
                        className="form-select w-auto"
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    >
                        <option value="ALL">All Applications</option>
                        <option value="PENDING">Pending</option>
                        <option value="ACCEPTED">Accepted</option>
                        <option value="REJECTED">Rejected</option>
                    </select>
                </div>

                {/* Body */}
                <div className="card-body">
                    {message && (
                        <div className="alert alert-danger" role="alert">
                            {message}
                        </div>
                    )}

                    {applications.length === 0 ? (
                        <p className="text-muted text-center">No applications found.</p>
                    ) : (
                        applications.map((app, idx) => (
                            <div
                                key={idx}
                                className="list-group-item mb-3 p-3 border rounded bg-light"
                            >
                                <h5 className="mb-1 text-dark">{app.loanPlan.loanName}</h5>
                                <p className="mb-1">
                                    <strong>Loan Type:</strong> {app.loanPlan.loanType}
                                </p>
                                <p className="mb-1">
                                    <strong>Purpose:</strong> {app.purpose}
                                </p>
                                <p className="mb-1">
                                    <strong>Status:</strong>{" "}
                                    <span
                                        className={
                                            app.status === "PENDING"
                                                ? "text-warning"
                                                : app.status === "ACCEPTED"
                                                ? "text-success"
                                                : "text-danger"
                                        }
                                    >
                                        {app.status}
                                    </span>
                                </p>
                                
                            </div>
                        ))
                    )}
                </div>

                {/* Footer */}
                
                    <div className="card-footer d-flex justify-content-between align-items-center bg-light">
                        <div className="d-flex align-items-center">
                            <span className="me-2">Items per page:</span>
                            <select
                                className="form-select w-auto"
                                value={size}
                                onChange={(e) => {
                                    setPage(0);
                                    setSize(Number(e.target.value));
                                }}
                            >
                                {[5, 10, 20].map((num) => (
                                    <option key={num} value={num}>
                                        {num}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <ul className="pagination mb-0">
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page - 1)} >
                                    &laquo;
                                </button>
                            </li>
                            
                                <li key={page} className="page-item" >
                                    <button className="page-link"  >
                                        {page + 1}
                                    </button>
                                </li>
                           
                            <li className="page-item" >
                                <button className="page-link" onClick={() => setPage(page + 1)} >
                                    &raquo;
                                </button>
                            </li>
                        </ul>
                    </div>
               
            </div>
        </div>
    );
}

export default MyLoanApplications;
