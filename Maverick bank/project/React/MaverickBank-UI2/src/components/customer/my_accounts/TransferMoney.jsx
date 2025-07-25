import {  useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { getBankAccount } from '../../../store/actions/BankAccountAction';

function TransferMoney() {
    const dispatch = useDispatch();
    const navigate = useNavigate();


    const [toAccountNumber, setToAccountNumber] = useState('');
    const [amount, setAmount] = useState('');
    const [pin, setPin] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const account = useSelector(state => state.bankAccount.account);

    


    const handleError = (err) => {
        {
            console.log(err);

            if (err.response && err.response.data) {
                const errorData = err.response.data;


                const firstKey = Object.keys(errorData)[0];
                setError(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    }


    const handleSubmit = async () => {
        setMessage('');
        setError('');

        try {
            const token = localStorage.getItem("token");
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            await axios.put(
                `http://localhost:9090/api/account/update/transfer/${account.id}/${amount}?pin=${pin}&toAccountNumber=${toAccountNumber}`,
                {},
                { headers }
            );

            setMessage("Money transferred successfully!");
            
            getBankAccount(dispatch)(account.id);
            setToAccountNumber('');
            setAmount('');
            setPin('');
        } catch (err) {
            setMessage('');
            handleError(err);
        }
    };

    return (
        <div className="card shadow-sm border-0 mx-3">
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
                        Transfer Money
                    </li>
                </ol>
            </nav>
            <div className="card-header bg-light">
                <h5 className="mb-0">Transfer Money</h5>
            </div>
            <div className="card-body">
                {message && <div className="alert alert-success">{message}</div>}
                {error && <div className="alert alert-danger">{error}</div>}

                <Form >
                    {/* From Account Number */}
                    <Form.Group className="mb-3">
                        <Form.Label>From Account Number</Form.Label>
                        <Form.Control type="text" value={account?.accountNumber} readOnly />
                    </Form.Group>

                    {/* To Account Number */}
                    <Form.Group className="mb-3">
                        <Form.Label>To Account Number</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter recipient account number"
                            value={toAccountNumber}
                            onChange={(e) => setToAccountNumber(e.target.value)}
                            required
                        />
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
                            required
                        />
                    </Form.Group>

                    {/* PIN */}
                    <Form.Group className="mb-4">
                        <Form.Label>Enter PIN</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Enter your 6-digit PIN"
                            value={pin}
                            onChange={(e) => setPin(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Button variant="primary" type="button" onClick={()=>handleSubmit()}>
                        Transfer
                    </Button>
                </Form>
            </div>
        </div>
    );
}

export default TransferMoney;
