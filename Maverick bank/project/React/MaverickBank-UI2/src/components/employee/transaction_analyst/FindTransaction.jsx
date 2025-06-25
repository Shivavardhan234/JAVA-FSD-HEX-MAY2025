import { useState } from "react";
import axios from "axios";

function FindTransaction() {
    const [transactionId, setTransactionId] = useState("");
    const [transaction, setTransaction] = useState(null);
    const [message, setMessage] = useState("");

    const handleSearch = async () => {
        setMessage("");
        setTransaction(null);

        if (!transactionId.trim()) {
            setMessage("Please enter a transaction ID.");
            return;
        }

        const token = localStorage.getItem("token");

        try {
            const res = await axios.get(`http://localhost:9090/api/transaction/get/by-id/${transactionId}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            setTransaction(res.data);
        } catch (err) {
            if (err.response && err.response.data) {
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Failed to fetch transaction.");
            }
        }
    };

    return (
        <div className="card shadow-sm">
            <div className="card-header d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Find Transaction</h5>

                <div className="d-flex">
                    <input
                        type="text"
                        placeholder="Enter Transaction ID"
                        value={transactionId}
                        onChange={(e) => setTransactionId(e.target.value)}
                        className="form-control me-2"
                        style={{ width: "250px" }}
                    />
                    <button className="btn btn-primary" onClick={handleSearch}>
                        <i className="bi bi-search"></i>
                    </button>
                </div>
            </div>

            <div className="card-body">
                {message && <div className="alert alert-info">{message}</div>}

                {transaction && (
                    <div className="table-responsive">
                        <table className="table table-hover table-dark">
                            <thead>
                                <tr>
                                    <th scope="col">#</th>
                                    <th scope="col">From</th>
                                    <th scope="col">From Medium</th>
                                    <th scope="col">To</th>
                                    <th scope="col">To Medium</th>
                                    <th scope="col">Amount</th>
                                    <th scope="col">Date</th>
                                    <th scope="col">Time</th>
                                    <th scope="col">Type</th>
                                    <th scope="col">Description</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th scope="row">{transaction.transactionId}</th>
                                    <td>{transaction.fromDetails}</td>
                                    <td>{transaction.fromPaymentMedium}</td>
                                    <td>{transaction.toDetails}</td>
                                    <td>{transaction.toPaymentMedium}</td>
                                    <td>₹{transaction.transactionAmount}</td>
                                    <td>{transaction.transactionDate}</td>
                                    <td>{transaction.transactionTime}</td>
                                    <td>{transaction.transactionType || "—"}</td>
                                    <td>{transaction.description || "—"}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}

export default FindTransaction;
