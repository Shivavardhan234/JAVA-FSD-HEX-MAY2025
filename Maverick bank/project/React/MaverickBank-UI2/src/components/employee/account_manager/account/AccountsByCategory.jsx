import { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { getBankAccount } from "../../../../store/actions/BankAccountAction";

function AccountsByCategory() {
    const navigate = useNavigate();
    const dispatch =useDispatch();

    const [filterType, setFilterType] = useState("ALL");
    const [filterValue, setFilterValue] = useState("");
    const [accounts, setAccounts] = useState([]);
    const [error, setError] = useState("");

    const handleError = (err) => {
        console.error(err);
        if (err.response?.data) {
            const key = Object.keys(err.response.data)[0];
            setError(err.response.data[key]);
        } else {
            setError("Something went wrong.");
        }
    };

    const fetchAccounts = async () => {
        setError("");
        setAccounts([]);

        const token = localStorage.getItem("token");
        let url = "http://localhost:9090/api/account/get/all";

        if (filterType === "OPEN" || filterType === "SUSPENDED" || filterType === "CLOSED") {
            url = `http://localhost:9090/api/account/get/by-account-status/${filterType}`;
        } else if (filterType === "TYPE") {
            url = `http://localhost:9090/api/account/get/by-account-type-id/${filterValue}`;
        } else if (filterType === "BRANCH") {
            url = `http://localhost:9090/api/account/get/by-branch-id/${filterValue}`;
        }

        try {
            const res = await axios.get(url, { headers: { Authorization: `Bearer ${token}` } });
            setAccounts(res.data);
        } catch (err) {
            handleError(err);
        }
    };

    useEffect(() => {
        if (filterType === "ALL" || filterType === "OPEN" || filterType === "SUSPENDED" || filterType === "CLOSED") {
            fetchAccounts();
        }
    }, [filterType]);

    const handleSearch = () => {
        if (filterType === "TYPE" || filterType === "BRANCH") {
            if (!filterValue.trim()) {
                setError("Please enter a valid ID.");
                return;
            }
            fetchAccounts();
        }
    };

    return (
        <div className="container mt-4">
            <div className="card shadow">
                <div className="card-header d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Accounts By Category</h5>
                    <div className="d-flex align-items-center gap-2">
                        <select
                            className="form-select"
                            style={{ width: "180px" }}
                            value={filterType}
                            onChange={(e) => {
                                setFilterType(e.target.value);
                                setFilterValue("");
                            }}
                        >
                            <option value="ALL">All</option>
                            <option value="OPEN">Open</option>
                            <option value="SUSPENDED">Suspended</option>
                            <option value="CLOSED">Closed</option>
                            <option value="TYPE">By Account Type ID</option>
                            <option value="BRANCH">By Branch ID</option>
                        </select>

                        {(filterType === "TYPE" || filterType === "BRANCH") && (
                            <>
                                <input
                                    type="text"
                                    className="form-control"
                                    style={{ width: "160px" }}
                                    placeholder={`${filterType === "TYPE" ? "Type ID" : "Branch ID"}`}
                                    value={filterValue}
                                    onChange={(e) => setFilterValue(e.target.value)}
                                />
                                <button className="btn btn-primary" onClick={handleSearch}>
                                    <i className="bi bi-search"></i>
                                </button>
                            </>
                        )}
                    </div>
                </div>

                <div className="card-body">
                    {error && <div className="alert alert-danger">{error}</div>}

                    {accounts.length === 0 ? (
                        <div className="text-center text-secondary">No accounts found.</div>
                    ) : (
                        <table className="table table-hover">
                            <thead className="table-light">
                                <tr>
                                    <th>#</th>
                                    <th>Account Info</th>
                                    <th>View Account</th>
                                </tr>
                            </thead>
                            <tbody>
                                {accounts.map((acc, idx) => (
                                    <tr key={acc.id}>
                                        <td>{idx + 1}</td>
                                        <td>
                                            <strong>{acc.accountNumber}</strong> — {acc.accountName} <br />
                                            <small>Status: {acc.accountStatus}, Type: {acc.accountType.accountType}</small>
                                        </td>
                                        <td>
                                            <button
                                                className="btn btn-outline-info btn-sm"
                                                onClick={() => {
                                                    getBankAccount(dispatch)(acc.id);
                                                    navigate(`../viewAccount`);
                                                }}
                                            >
                                                View
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    )}
                </div>
            </div>
        </div>
    );
}

export default AccountsByCategory;
