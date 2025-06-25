import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Form, Table } from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { getBankAccount } from "../../../store/actions/BankAccountAction";

function MyTransactions() {
    const navigate = useNavigate();


    const [filterType, setFilterType] = useState("count");
    const [count, setCount] = useState(10);
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [transactions, setTransactions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");


    const dispatch = useDispatch();
    const accountId = localStorage.getItem("accountId");

    useEffect(() => {
        getBankAccount(dispatch)(accountId);
    }, [])

    const account = useSelector(state => state.bankAccount.account);

    useEffect(() => {
        if (filterType === "count") {
            fetchTransactionsByCount(count);
        }
    }, [filterType, count]);

    const fetchTransactionsByCount = async (count) => {
        try {
            setLoading(true);
            const token = localStorage.getItem("token");
            const headers = { Authorization: `Bearer ${token}` };

            const res = await axios.get(
                `http://localhost:9090/api/transaction/get/last-transactions/${account.accountNumber}/${count}`,
                { headers }
            );
            setTransactions(res.data);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch transactions.");
        } finally {
            setLoading(false);
        }
    };

    const fetchByDateRange = async () => {
        try {
            if (!startDate || !endDate) {
                alert("Please provide both start and end dates.");
                return;
            }

            setLoading(true);
            const token = localStorage.getItem("token");
            const headers = { Authorization: `Bearer ${token}` };

            const res = await axios.get(
                `http://localhost:9090/api/transaction/get/by-date-range/${startDate}/${endDate}?accountNumber=${account.accountNumber}`,
                { headers }
            );
            setTransactions(res.data);
        } catch (err) {
            console.error(err);
            setError("Failed to fetch transactions by date.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-3">

            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Accounts</li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`/customer/myAccounts/manageBankAccount/${account?.accountNumber}`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Bank Account
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        My Transactions
                    </li>
                </ol>
            </nav>

            {/* Outer Ash Box */}
            <div className="bg-light p-3" style={{ border: "none", boxShadow: "none", width: "100%" }}>
                <div className="card border-0 shadow-sm">
                    <div className="card-header bg-secondary-subtle d-flex justify-content-between align-items-center">
                        <div className="d-flex gap-3 align-items-center">
                            <Form.Select value={filterType} onChange={(e) => setFilterType(e.target.value)}>
                                <option value="count">With Count</option>
                                <option value="date">By Date Range</option>
                            </Form.Select>

                            {filterType === "count" && (
                                <Form.Select
                                    value={count}
                                    onChange={(e) => setCount(Number(e.target.value))}
                                    className="ms-2"
                                >
                                    <option value="10">10 transactions</option>
                                    <option value="20">20 transactions</option>
                                    <option value="30">30 transactions</option>
                                    <option value="50">50 transactions</option>
                                    <option value="100">100 transactions</option>
                                </Form.Select>
                            )}

                            {filterType === "date" && (
                                <div className="d-flex gap-2 align-items-center">
                                    <Form.Control
                                        type="date"
                                        value={startDate}
                                        onChange={(e) => setStartDate(e.target.value)}
                                    />
                                    <Form.Control
                                        type="date"
                                        value={endDate}
                                        onChange={(e) => setEndDate(e.target.value)}
                                    />
                                    <button className="btn btn-primary btn-sm" onClick={fetchByDateRange}>
                                        Get
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="card-body">
                        {loading && <div>Loading transactions...</div>}
                        {error && <div className="alert alert-danger">{error}</div>}

                        {!loading && transactions.length > 0 ? (
                            <Table striped bordered hover size="sm">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Date</th>
                                        <th>Time</th>
                                        <th>Type</th>
                                        <th>Amount</th>
                                        <th>From</th>
                                        <th>To</th>
                                        <th>Medium</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {transactions.map((tx, idx) => (
                                        <tr key={idx}>
                                            <td>{idx + 1}</td>
                                            <td>{tx.transactionDate}</td>
                                            <td>{tx.transactionTime}</td>
                                            <td>{tx.transactionType}</td>
                                            <td>{tx.transactionAmount}</td>
                                            <td>{tx.fromDetails}</td>
                                            <td>{tx.toDetails}</td>
                                            <td>{tx.toPaymentMedium}</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </Table>
                        ) : (
                            !loading && <div>No transactions found.</div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MyTransactions;
