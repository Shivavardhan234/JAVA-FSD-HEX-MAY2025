import React, { useEffect, useState } from 'react';
import {  useNavigate } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { getBankAccount } from '../../../store/actions/BankAccountAction';

function WithdrawMoney() {
    const navigate = useNavigate();
     const accountId = localStorage.getItem("accountId");
    const dispatch = useDispatch();

    useEffect(() => {
        getBankAccount(dispatch)(accountId);
    }, [])

    const account = useSelector(state => state.bankAccount.account);

    const [amount, setAmount] = useState('');
    const [medium, setMedium] = useState('UPI');
    const [pin, setPin] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleError=(err)=>{
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

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem("token");
            const headers = {
                Authorization: `Bearer ${token}`,
            };

            await axios.put(
                `http://localhost:9090/api/account/update/withdraw/${account?.id}/${amount}/${medium}/${pin}`,
                {},
                { headers }
            );

            setMessage('Withdrawal successful!');
            setError('');
            setAmount('');
            setPin('');
        } catch (err) {
             setMessage('');
            handleError(err);
            
           
        }
    };

    return (
        <div className="container mt-4">
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
                        Withdraw Money
                    </li>
                </ol>
            </nav>
            {/* Withdraw Card */}
            <div className="card shadow-sm border-0">
                <div className="card-header bg-light">
                    <h5 className="mb-0">Withdraw Money</h5>
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
                        <Form.Group className="mb-3">
                            <Form.Label>Payment Medium</Form.Label>
                            <Form.Select value={medium} onChange={(e) => setMedium(e.target.value)}>
                                <option value="UPI">UPI</option>
                                <option value="WALLET">Wallet</option>
                            </Form.Select>
                        </Form.Group>

                        {/* PIN */}
                        <Form.Group className="mb-4">
                            <Form.Label>PIN</Form.Label>
                            <Form.Control
                                type="password"
                                placeholder="Enter PIN"
                                value={pin}
                                onChange={(e) => setPin(e.target.value)}
                            />
                        </Form.Group>

                        <Button variant="danger" type="submit">
                            Withdraw
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

export default WithdrawMoney;
