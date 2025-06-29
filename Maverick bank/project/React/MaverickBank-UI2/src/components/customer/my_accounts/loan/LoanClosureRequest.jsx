import {  useNavigate } from "react-router-dom";
import {  useState } from "react";
import axios from "axios";
import { Form, Button, Card } from "react-bootstrap";
import {  useSelector } from "react-redux";

function LoanClosureRequest() {
    const navigate = useNavigate();


    const [purpose, setPurpose] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");



    const loan =useSelector(state=> state.loanStore.loan);

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
                `http://localhost:9090/api/loan-closure/add/${loan.id}?purpose=${purpose}`,
                {},
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );

            setMessage("Your request has been submitted successfully.");
            setPurpose("");
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
                    <li className="breadcrumb-item text-muted">My Loans</li>
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() =>
                                navigate(`/customer/myAccounts/manageLoan`)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Loan
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Loan Closure request
                    </li>
                </ol>
            </nav>

            {/* Card */}
            <Card className="shadow">
                <Card.Header className="bg-danger text-white fw-semibold">
                    LOAN CLOSING REQUEST FORM
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

                    

                    {/* Feedback */}
                    {message && <p className="text-success mt-3">{message}</p>}
                    {error && <p className="text-danger mt-3">{error}</p>}
                </Card.Body>

                <Card.Footer className="text-end">
                    <Button variant="danger" onClick={()=>{handleSubmit()}}>
                        Submit Request
                    </Button>
                </Card.Footer>
            </Card>
        </div>
    );
}

export default LoanClosureRequest;
