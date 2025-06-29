import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import { Form, Button, Card } from "react-bootstrap";
import {  useSelector } from "react-redux";

function CloseOrSuspendAccount() {
    const navigate = useNavigate();


    const [requestType, setRequestType] = useState("CLOSE");
    const [purpose, setPurpose] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");


    

    const account =useSelector(state=> state.bankAccount.account);

    const handleSubmit = async () => {
        setMessage("");
        setError("");

        if (!purpose.trim()) {
            setError("Please enter a purpose for the request.");
            return;
        }

        try {
            const token = localStorage.getItem("token");
            const response = await axios.post(
                `http://localhost:9090/api/account-request/add/${account.id}/${requestType}?purpose=${purpose}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setMessage("Your request has been submitted successfully.");
            setPurpose("");
            setRequestType("CLOSE");
        } catch (err) {
            
            handleError(err);
        }
    };


    
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

    return (
        <div className="container mt-4">
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
                        Account request
                    </li>
                </ol>
            </nav>

            {/* Card */}
            <Card className="shadow">
                <Card.Header className="bg-danger text-white fw-semibold">
                    ACCOUNT CLOSING / SUSPENSION REQUEST FORM
                </Card.Header>

                <Card.Body>
                    {/* Purpose */}
                    <Form.Group className="mb-3">
                        <Form.Label>Purpose</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={3}
                            placeholder="Enter purpose for closing or suspending the account"
                            value={purpose}
                            onChange={(e) => setPurpose(e.target.value)}
                        />
                    </Form.Group>

                    {/* Request Type */}
                    <Form.Group>
                        <Form.Label>Request Type</Form.Label>
                        <Form.Select
                            value={requestType}
                            onChange={(e) => setRequestType(e.target.value)}
                        >
                            <option value="CLOSE">Close Account</option>
                            <option value="SUSPEND">Suspend Account</option>
                        </Form.Select>
                    </Form.Group>

                    {/* Feedback */}
                    {message && <p className="text-success mt-3">{message}</p>}
                    {error && <p className="text-danger mt-3">{error}</p>}
                </Card.Body>

                <Card.Footer className="text-end">
                    <Button variant="danger" onClick={handleSubmit}>
                        Submit Request
                    </Button>
                </Card.Footer>
            </Card>
        </div>
    );
}

export default CloseOrSuspendAccount;
