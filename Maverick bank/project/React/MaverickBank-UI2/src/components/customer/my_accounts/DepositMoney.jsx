import { Link, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getBankAccount } from "../../../store/actions/BankAccountAction";

function DepositMoney() {

    const navigate = useNavigate();
    const accountId = localStorage.getItem("accountId");
    const dispatch = useDispatch();

    useEffect(() => {
        getBankAccount(dispatch)(accountId);
    }, [])

    const account = useSelector(state => state.bankAccount.account);

    const [amount, setAmount] = useState("");
    const [medium, setMedium] = useState("UPI");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setError("");

        if (!amount || !medium) {
            setError("Please fill all fields.");
            return;
        }

        try {
            const token = localStorage.getItem("token");

            const response = await axios.put(
                `http://localhost:9090/api/account/update/deposit/${account.id}/${amount}/${medium}`,
                {}, // empty body for PUT
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setMessage("Deposit successful! ");
            setTimeout(() => {
                setMessage("");
            }, 3000);

        } catch (err) {
            console.error(err);
            setError(err?.response?.data?.message || "Deposit failed.");
        }
    };

    return (
        <div className="container-fluid px-3">
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">My Accounts </li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate(`/customer/myAccounts/manageBankAccount/${account?.accountNumber}`)}
                            className="text-decoration-none text-primary"

                        >
                            Manage Bank Account
                        </span>
                    </li>

                    <li className="breadcrumb-item active" aria-current="page">
                        Deposit Money
                    </li>
                </ol>
            </nav>

            {/* Deposit Card */}
            <div className="card shadow-sm border-0">
                <div className="card-header bg-light">
                    <h5 className="mb-0">Deposit Money</h5>
                </div>
                <div className="card-body">
                    {message && <div className="alert alert-success">{message}</div>}
                    {error && <div className="alert alert-danger">{error}</div>}

                    <Form onSubmit={handleSubmit}>
                        {/* Account Number */}
                        <Form.Group className="mb-3">
                            <Form.Label>Account Number</Form.Label>
                            <Form.Control type="text" value={account?.accountNumber} readOnly />
                        </Form.Group>

                        {/* Amount */}
                        <Form.Group className="mb-3">
                            <Form.Label>Amount</Form.Label>
                            <Form.Control
                                type="number"
                                min="1"
                                placeholder="Enter amount"
                                value={amount}
                                onChange={(e) => setAmount(e.target.value)}
                            />
                        </Form.Group>

                        {/* Payment Medium */}
                        <Form.Group className="mb-4">
                            <Form.Label>Payment Medium</Form.Label>
                            <Form.Select value={medium} onChange={(e) => setMedium(e.target.value)}>
                                <option value="UPI">UPI</option>
                                <option value="WALLET">Wallet</option>
                            </Form.Select>
                        </Form.Group>

                        <Button variant="primary" type="submit">
                            Deposit
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

export default DepositMoney;
