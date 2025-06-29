import { useEffect, useState } from "react";
import axios from "axios";

function TransactionsForAccount() {
    const [accountNumber, setAccountNumber] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [type, setType] = useState("ALL");
    const [transactions, setTransactions] = useState([]);
    const [message, setMessage] = useState("");

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);



    useEffect(()=>{
        handleSearch();
    },[page,size]);

    const handleSearch = async () => {
        setMessage("");
        setTransactions([]);
        const token = localStorage.getItem("token");

        if (!accountNumber || !startDate || !endDate) {
            setMessage("Please enter all required fields.");
            return;
        }

        let url = "";

        if (type === "ALL") {
            url = `http://localhost:9090/api/transaction/get/by-date-range/${startDate}/${endDate}?accountNumber=${accountNumber}&page=${page}&size=${size}`;
        } else if (type === "CREDITS") {
            url = `http://localhost:9090/api/transaction/get/credits/${startDate}/${endDate}?accountNumber=${accountNumber}&page=${page}&size=${size}`;
        } else if (type === "DEBITS") {
            url = `http://localhost:9090/api/transaction/get/debits/${startDate}/${endDate}?accountNumber=${accountNumber}&page=${page}&size=${size}`;
        }

        try {
            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setTransactions(res.data);
        } catch (err) {
            setMessage("Failed to fetch transactions.");
        }
    };

    return (
        <><h2>Transactions for account </h2>
        <div className="card shadow-sm">
            
            <div className="card-header d-flex flex-wrap justify-content-between align-items-center gap-2">
                
                <div className="d-flex flex-wrap gap-2">
                    <select
                        className="form-select"
                        style={{ minWidth: "140px" }}
                        value={type}
                        onChange={(e) => setType(e.target.value)}
                    >
                        <option value="ALL">All</option>
                        <option value="CREDITS">Credits</option>
                        <option value="DEBITS">Debits</option>
                    </select>

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

                    <input
                        type="text"
                        className="form-control"
                        placeholder="Account Number"
                        value={accountNumber}
                        onChange={(e) => setAccountNumber(e.target.value)}
                    />

                    <button className="btn btn-primary" onClick={handleSearch}>
                        Search
                    </button>
                </div>
            </div>

            <div className="card-body">
                {message && (
                    <div className="alert alert-info" role="alert">
                        {message}
                    </div>
                )}

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
                                    <td colSpan="9" className="text-center">No transactions found.</td>
                                </tr>
                            ) : (
                                transactions.map((t, idx) => (
                                    <tr key={idx}>
                                        <th scope="row">{t.transactionId}</th>
                                        <td>{t.fromDetails}</td>
                                        <td>{t.fromPaymentMedium}</td>
                                        <td>{t.toDetails}</td>
                                        <td>{t.toPaymentMedium}</td>
                                        <td>₹{t.transactionAmount}</td>
                                        <td>{t.transactionDate}</td>
                                        <td>{t.transactionTime}</td>
                                        <td>{t.transactionType || "-"}</td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>



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
                            {[10, 20,50,100].map((num) => (
                                <option key={num} value={num}>
                                    {num}
                                </option>
                            ))}
                        </select>
                    </div>

                    <ul className="pagination mb-0">
                        <li className="page-item">
                            <button className="page-link" onClick={() => setPage(page - 1)}>
                                &laquo;
                            </button>
                        </li>
                        
                            <li key={page} className="page-item" >
                                <button className="page-link" >
                                    {page + 1}
                                </button>
                            </li>
                        <li className="page-item">
                            <button className="page-link" onClick={() => setPage(page + 1)}>
                                &raquo;
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        </>
    );
}

export default TransactionsForAccount;
