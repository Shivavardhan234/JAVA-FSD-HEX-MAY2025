import { useState, useEffect } from "react";
import axios from "axios";

function TransactionsByCategory() {
    const [filter, setFilter] = useState("ALL");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [transactions, setTransactions] = useState([]);
    const [error, setError] = useState("");

    const fetchTransactions = async () => {
        setError("");
        setTransactions([]);
        const token = localStorage.getItem("token");
        let url = "http://localhost:9090/api/transaction/get/all";

        if (filter === "BY_DATE_RANGE" && startDate && endDate) {
            url = `http://localhost:9090/api/transaction/get/by-date-range/${startDate}/${endDate}`;
        }

        try {
            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setTransactions(res.data);
        } catch (err) {
            setError("Failed to fetch transactions.");
        }
    };

    useEffect(() => {
        if (filter === "ALL") {
            fetchTransactions();
        }
    }, [filter]);

    const handleSearch = () => {
        if (filter === "BY_DATE_RANGE" && startDate && endDate) {
            fetchTransactions();
        }
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center gap-2">
                    <select
                        className="form-select"
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    >
                        <option value="ALL">All Transactions</option>
                        <option value="BY_DATE_RANGE">By Date Range</option>
                    </select>

                    {filter === "BY_DATE_RANGE" && (
                        <>
                            <input
                                type="date"
                                className="form-control"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                            />
                            <input
                                type="date"
                                className="form-control"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                            />
                            <button className="btn btn-primary" onClick={handleSearch}>
                                Search
                            </button>
                        </>
                    )}
                </div>
            </div>

            <div className="card-body">
                {error && <div className="alert alert-danger">{error}</div>}

                <div className="table-responsive">
                    <table className="table table-hover table-dark">
                        <thead>
                            <tr>
                                <th>id</th>
                                <th>From</th>
                                <th>From Mode</th>
                                <th>To</th>
                                <th>To Mode</th>
                                <th>Amount</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {transactions.length === 0 ? (
                                <tr>
                                    <td colSpan="9" className="text-center">
                                        No transactions found.
                                    </td>
                                </tr>
                            ) : (
                                transactions.map((t, i) => (
                                    <tr key={i}>
                                        <th scope="row">{t.transactionId}</th>
                                        <td>{t.fromDetails}</td>
                                        <td>{t.fromPaymentMedium}</td>
                                        <td>{t.toDetails}</td>
                                        <td>{t.toPaymentMedium}</td>
                                        <td>₹{t.transactionAmount}</td>
                                        <td>{t.transactionDate}</td>
                                        <td>{t.transactionTime}</td>
                                        <td>{t.description || "-"}</td>
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

export default TransactionsByCategory;
