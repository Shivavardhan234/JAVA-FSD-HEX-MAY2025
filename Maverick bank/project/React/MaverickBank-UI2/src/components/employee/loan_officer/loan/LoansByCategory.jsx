import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { getLoan } from "../../../../store/actions/LoanAction";
import { useDispatch } from "react-redux";

function LoansByCategory() {
    const [filter, setFilter] = useState("ALL");
    const [loans, setLoans] = useState([]);
    const [message, setMessage] = useState("");
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        fetchLoans();
    }, [filter]);

    useEffect(() => {
        fetchLoans();
    }, [page,size]);

    const fetchLoans = async () => {
        try {
            const token = localStorage.getItem("token");
            let url =
                filter === "ALL"
                    ? `http://localhost:9090/api/loan/get/all?page=${page}&size=${size}`
                    : `http://localhost:9090/api/loan/get/by-status/${filter}?page=${page}&size=${size}`;

            const res = await axios.get(url, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            setLoans(res.data);
            setMessage("");
        } catch (err) {
            setLoans([]);
            handleError(err);
        }
    };


    const handleError = (err) => {

        console.log(err);
        // Axios errors have a response object
        if (err.response && err.response.data) {
            const errorData = err.response.data;


            const firstKey = Object.keys(errorData)[0];
            setMessage(errorData[firstKey]);
            setTimeout(() => {
                setMessage("");
            }, 3000);
        } else {
            setMessage("something went wrong. Please try again later..");
            setTimeout(() => {
                setMessage("");
            }, 3000);
        }

    }



    return (
        <div className="d-flex flex-column" style={{ marginLeft: "70px", padding: "20px" }}>
            <div className="card shadow-sm">
                {/* Header */}
                <div className="card-header d-flex justify-content-between align-items-center bg-light">
                    <select
                        className="form-select w-auto"
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    >
                        <option value="ALL">All Loans</option>
                        <option value="ACTIVE">Active Loans</option>
                        <option value="CLOSED">Closed Loans</option>
                    </select>
                </div>

                {/* Body */}
                <div className="card-body">
                    {message && (
                        <div className="alert alert-danger" role="alert">
                            {message}
                        </div>
                    )}

                    {loans.length === 0 ? (
                        <p className="text-muted text-center">No loans found.</p>
                    ) : (
                        loans.map((loan, idx) => (
                            <div key={idx} className="list-group-item mb-3 p-3 border rounded bg-light">
                                <h5 className="mb-2 text-dark">Loan ID: {loan.id}</h5>
                                <p className="mb-1"><strong>Status:</strong> {loan.status}</p>
                                <p className="mb-1"><strong>Start Date:</strong> {loan.loanStartDate}</p>
                                <p className="mb-1"><strong>End Date:</strong> {loan.loanEndDate}</p>

                                {/* Right aligned button */}
                                <div className="d-flex justify-content-end mt-3">
                                    <button
                                        className="btn btn-outline-primary btn-sm"
                                        onClick={() => {
                                            getLoan(dispatch)(loan.id);
                                            navigate("../viewLoan");}}
                                    >
                                        View Loan
                                    </button>
                                </div>
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
                                setSize(e.target.value);
                                setPage(0);
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
                            <button className="page-link" onClick={() => setPage(page-1)}>
                                &laquo;
                            </button>
                        </li>
                        
                            <li key={page} className="page-item">
                                <button className="page-link" >
                                    {page + 1}
                                </button>
                            </li>
                        <li className="page-item">
                            <button className="page-link" onClick={() => setPage(page+1)}>
                                &raquo;
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default LoansByCategory;
