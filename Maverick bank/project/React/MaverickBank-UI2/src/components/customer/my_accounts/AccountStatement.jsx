import { useLocation, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";
import axios from "axios";
import { useRef } from "react";
import html2pdf from "html2pdf.js";
import {  useSelector } from "react-redux";

function AccountStatement() {
    const navigate=useNavigate();

    const statementRef = useRef();//used to wrap around a component and convert it to pdf

    const [duration, setDuration] = useState("1");//in months
    const [transactions, setTransactions] = useState([]);
    const [generated, setGenerated] = useState(false);
    const [totals, setTotals] = useState({ inbound: 0, outbound: 0 });

    
    const generationDate = new Date().toLocaleDateString();//today

    const formatDate = (date) => {return date.toISOString().split("T")[0]};
    

    const account = useSelector(state => state.bankAccount.account);

    const handleGenerate = async () => {
        const token = localStorage.getItem("token");
        const headers = {
            Authorization: `Bearer ${token}`,
        };

        const endDate = new Date();
        const startDate = new Date();
        startDate.setMonth(endDate.getMonth() - parseInt(duration));

        

        try {
            const response = await axios.get(
                `http://localhost:9090/api/transaction/get/by-date-range/${formatDate(startDate)}/${formatDate(endDate)}?accountNumber=${account?.accountNumber}`,
                { headers }
            );

            const data = response.data || [];

            // Compute totals
            let inbound = 0;
            let outbound = 0;
            data.forEach(t => {
                if (t.transactionType === "CREDIT") {
                    inbound += t.transactionAmount;
                }
                else {
                    outbound += t.transactionAmount;
                }
            });

            setTotals({
                inbound: inbound.toFixed(2),
                outbound: outbound.toFixed(2),
            });

            setTransactions(data);
            setGenerated(true);
        } catch (error) {
            alert("Failed to generate account statement");
        }
    };

    const durationMap = {
        "1": "1 Month",
        "3": "3 Months",
        "6": "6 Months"
    };

    const downloadPDF = () => {
        const element = statementRef.current;
        const opt = {
            margin: 0.5,
            filename: `account-statement-${account?.accountNumber}.pdf`,
            image: { type: 'jpeg', quality: 0.98 },
            html2canvas: { scale: 2 },
            jsPDF: { unit: 'in', format: 'a4', orientation: 'portrait' }
        };

        html2pdf().set(opt).from(element).save();
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
                            onClick={() =>navigate(-1)
                            }
                            className="text-decoration-none text-primary"
                        >
                            Manage Bank Account
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Account Statement
                    </li>
                </ol>
            </nav>

            {/* Duration Selection */}
            {!generated && (
                <div className="mb-4">
                    <Form.Select
                        value={duration}
                        onChange={(e) => setDuration(e.target.value)}
                        className="w-auto mb-3"
                    >
                        <option value="1">Last 1 Month</option>
                        <option value="3">Last 3 Months</option>
                        <option value="6">Last 6 Months</option>
                    </Form.Select>
                    <Button onClick={handleGenerate}>Generate Account Statement</Button>
                </div>
            )}

            {/* Statement Content */}
            {generated && (
                <div ref={statementRef}>
                    <div
                        className="bg-white border p-4"
                        style={{ minHeight: "80vh", width: "100%", fontFamily: "serif" }}
                    >
                        <h1 className="text-center mb-4">Account Statement</h1>
                        {/* Account Info */}
                        <div className="mb-3">
                            <p><strong>Account Number:</strong> {account?.accountNumber}</p>
                            <p><strong>Account Name:</strong> {account?.accountName}</p>
                            <p><strong>Branch:</strong> {account?.branch?.branchName}</p>
                            <p><strong>IFSC:</strong> {account?.branch?.ifsc}</p>
                            <p><strong>Account Type:</strong> {account?.accountType?.accountType}</p>
                        </div>

                        <hr />
                        <div className="d-flex justify-content-between mb-4">
                            <p className="text-muted">
                                <strong>Generated On:</strong> {generationDate}
                            </p>
                            <p className="text-muted">
                                <strong>Duration:</strong> {durationMap[duration]}
                            </p>
                        </div>
                        <hr />

                        {/* Summary */}
                        <div className="mb-4">
                            <p><strong>Total Inbound Amount:</strong> ₹ {totals.inbound}</p>
                            <p><strong>Total Outbound Amount:</strong> ₹ {totals.outbound}</p>
                            <p><strong>Current Balance:</strong> ₹ {account?.balance?.toFixed(2)}</p>
                        </div>

                        {/* Transactions Table */}
                        <table className="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>From</th>
                                    <th>To</th>
                                    <th>Amount</th>
                                    <th>Type</th>
                                </tr>
                            </thead>
                            <tbody>
                                {transactions.map((tx, index) => (
                                    <tr key={index}>
                                        <td>{tx.transactionDate}</td>
                                        <td>{tx.transactionTime}</td>
                                        <td>{tx.fromDetails}</td>
                                        <td>{tx.toDetails}</td>
                                        <td>₹ {tx.transactionAmount.toFixed(2)}</td>
                                        <td>{tx.transactionType}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>

                        {/* Bank Stamp */}
                        <div className="d-flex justify-content-end mt-5">
                            <div className="text-end">
                                <p className="text-muted mb-0">Bank Stamp</p>
                                <h5 className="fw-bold text-uppercase">MAVERICK BANK</h5>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            <div>
                {generated && (
                    <div className="my-4 d-flex justify-content-end">
                        <Button variant="outline-primary" onClick={() => downloadPDF()}>
                            Download as PDF
                        </Button>
                    </div>


                )}
            </div>
        </div>
    );
}

export default AccountStatement;
