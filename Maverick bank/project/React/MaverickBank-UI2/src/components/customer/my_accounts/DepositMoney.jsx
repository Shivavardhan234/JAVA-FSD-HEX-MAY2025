import { Link, useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getBankAccount } from "../../../store/actions/BankAccountAction";

function DepositMoney() {

    const navigate = useNavigate();
    const dispatch = useDispatch();


    const [amount, setAmount] = useState("");
    const [medium, setMedium] = useState("UPI");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");   

    const account = useSelector(state => state.bankAccount.account);


    const handleSubmit = async () => {
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
            getBankAccount(dispatch)(account.id);
            setTimeout(() => {
                setMessage("");
            }, 3000);

        } catch (err) {
            handleError(err);
        }
    };



      const handleError = (err) => {
        console.log(err);
        if (err.response && err.response.data) {
            const errorData = err.response.data;
            const firstKey = Object.keys(errorData)[0];
            setMessage(errorData[firstKey]);
        } else {
            setMessage("Deposit unsuccessful. Please try again later...");
        }
        setTimeout(() => setMessage(""), 3000);
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

                    <Form >
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

                        <Button variant="primary" type="button" onClick={()=> handleSubmit()}>
                            Deposit
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

export default DepositMoney;
