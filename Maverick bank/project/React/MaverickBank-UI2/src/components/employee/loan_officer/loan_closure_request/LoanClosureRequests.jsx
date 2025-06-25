import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { Dropdown } from "react-bootstrap";
import { getLoanClosureRequest } from "../../../../store/actions/LoanClosureRequestAction";
import { useDispatch } from "react-redux";

function LoanClosureRequests() {
    const [requests, setRequests] = useState([]);
    const [filter, setFilter] = useState("ALL");
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        fetchRequests(filter);
    }, [filter]);

    const fetchRequests = async (filterType) => {
        setError("");
        const token = localStorage.getItem("token");
        let url = "http://localhost:9090/api/loan-closure";

        try {
            if (filterType === "ALL") {
                const res = await axios.get(`${url}/get/all`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setRequests(res.data);
            } else {
                const res = await axios.get(`${url}/get/by-status/${filterType}`, {
                    headers: { Authorization: `Bearer ${token}` },
                });
                setRequests(res.data);
            }
        } catch (err) {
            handleError(err);
        }
    };

    const handleError = (err) => {
        if (err.response && err.response.data) {
            const firstKey = Object.keys(err.response.data)[0];
            setError(err.response.data[firstKey]);
            setRequests([]);
        } else {
            setError("Something went wrong. Try again.");
        }
    };

    return (
        <div className="container mt-4">
            {/* Card */}
            <div className="card shadow">
                {/* Header with Dropdown */}
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Loan Closure Requests</h5>
                    <Dropdown onSelect={(key) => setFilter(key)}>
                        <Dropdown.Toggle variant="secondary" id="dropdown-basic">
                            {filter === "ALL" ? "All Requests" : `${filter.charAt(0)}${filter.slice(1).toLowerCase()} Requests`}
                        </Dropdown.Toggle>

                        <Dropdown.Menu>
                            <Dropdown.Item eventKey="ALL">All Requests</Dropdown.Item>
                            <Dropdown.Item eventKey="PENDING">Pending Requests</Dropdown.Item>
                            <Dropdown.Item eventKey="ACCEPTED">Accepted Requests</Dropdown.Item>
                            <Dropdown.Item eventKey="REJECTED">Rejected Requests</Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>

                {/* Error Message */}
                {error && <div className="alert alert-danger m-3">{error}</div>}

                {/* Table */}
                <div className="table-responsive">
                    <table className="table table-bordered m-0">
                        <thead className="table-light">
                            <tr>
                                <th>#</th>
                                <th>Loan ID</th>
                                <th>Purpose</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {requests.length === 0 ? (
                                <tr>
                                    <td colSpan="6" className="text-center">No requests found.</td>
                                </tr>
                            ) : (
                                requests.map((req, idx) => (
                                    <tr key={req.id}>
                                        <td>{idx + 1}</td>
                                        <td>{req.loan?.id}</td>
                                        <td>{req.purpose}</td>
                                        <td>{req.requestStatus}</td>
                                        <td>
                                            <button
                                                className="btn btn-outline-primary btn-sm"
                                                onClick={() => {getLoanClosureRequest(dispatch)(req.id); navigate(`/loanOfficer/loanManagementSidebar/viewLoanClosure`);}}
                                            >
                                                View Request
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default LoanClosureRequests;
