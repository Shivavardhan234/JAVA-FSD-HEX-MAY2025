import { useEffect, useState } from "react";
import axios from "axios";
import { Form, Button, Table, Alert } from "react-bootstrap";
import { BsPlusCircle } from "react-icons/bs";




function FillAccountApplication() {
    const [accountType, setAccountType] = useState("");
    const [accountTypeDetails, setAccountTypeDetails] = useState(null);
    const [accountHolders, setAccountHolders] = useState([{ name: "", dob: "", gender: "", contactNumber: "", email: "", address: "", panCardNumber: "", aadharNumber: "" }]);
    const [isJoint, setIsJoint] = useState(false);
    const [states] = useState(["Maharashtra", "Telangana", "Tamil Nadu", "Andhra Pradesh"]);
    const [selectedState, setSelectedState] = useState("");
    const [branches, setBranches] = useState([]);
    const [selectedBranch, setSelectedBranch] = useState("");
    const [message, setMessage] = useState("");
    const [accountName, setAccountName] = useState("");
    const [purpose, setPurpose] = useState("");
    let [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        if (accountType) {
            axios.get(`http://localhost:9090/api/account-type/get/by-name/${accountType}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") },
            })
                .then(res => {
                    setAccountTypeDetails(res.data);
                    setIsJoint(accountType.startsWith("JOINT"));
                    setAccountHolders([{ name: "", dob: "", gender: "", contactNumber: "", email: "", address: "", panCardNumber: "", aadharNumber: "" }]);
                })
                .catch(err => handleError(err));
        }
    }, [accountType]);

    useEffect(() => {
        if (selectedState) {
            axios.get(`http://localhost:9090/api/branch/get/active-by-state/${selectedState}`, {
                headers: { Authorization: "Bearer " + localStorage.getItem("token") },
            })
                .then(res => setBranches(res.data))
                .catch(err => handleError(err));
        }
    }, [selectedState]);

    const handleAddAccountHolder = () => {
        if (accountHolders.length < 4) {
            setAccountHolders([...accountHolders, { name: "", dob: "", gender: "", contactNumber: "", email: "", address: "", panCardNumber: "", aadharNumber: "" }]);
        } else {
            setMessage("Maximum 4 account holders allowed for joint accounts.");
        }
    };


    const addAccountOpeningApplication = async () => {
        setMessage(""); // Reset any previous messages

        const token = localStorage.getItem('token');
        const bearerAuth = "Bearer " + token;

        // Basic input validation (you can enhance this further)
        if (!accountType || !selectedBranch || accountHolders.length === 0) {
            setMessage("Please fill in all required fields before submitting.");
            return;
        }

        // Prepare the payload
        const payload = {
            'accountOpeningApplication': {
                'accountType': { 'accountType': accountType },
                'branch': { 'branchName': selectedBranch },
                'accountName': accountName,
                'purpose': purpose
            },
            'accountHolderList': accountHolders
        };

        try {
            const response = await axios.post(
                "http://localhost:9090/api/customer-account-opening-application/add",
                payload,
                {
                    headers: {
                        Authorization: bearerAuth
                    }
                }
            );

            console.log("Application submitted:", response.data);
            setSuccessMessage("Application submitted successfully!");
            setMessage("");
            setTimeout(() => setSuccessMessage(""), 3000);


            setAccountType("");
            setSelectedState("");
            setSelectedBranch("");
            setPurpose("");
            setAccountName("");
            const emptyAccountHolder = () => ({
                name: "",
                dob: "",
                gender: "",
                contactNumber: "",
                email: "",
                address: "",
                panCardNumber: "",
                aadharNumber: ""
            });
            setAccountHolders([emptyAccountHolder()]);
        } catch (err) {
            handleError(err);
        }
    };


    const handleError = (err) => {
        console.error(err);
        if (err.response && err.response.data) {
            const errorData = err.response.data;
            const firstKey = Object.keys(errorData)[0];
            setMessage(errorData[firstKey]);
        } else {
            setMessage("Something went wrong. Try again.");
        }
    }

    return (
        <div className="container mt-4 bg-light p-4 rounded-4 shadow">

            <div className="bg-secondary-subtle p-2 px-3 rounded-2 mb-4">
                <h3 className="m-0">Open New Bank Account</h3>
            </div>

            <Form.Group className="mb-3">
                <Form.Label>Select Account Type</Form.Label>
                <Form.Select value={accountType} onChange={e => setAccountType(e.target.value)}>
                    <option value="">-- Select Account Type --</option>
                    <option>SAVINGS</option>
                    <option>CURRENT</option>
                    <option>BUSINESS</option>
                    <option>FIXED</option>
                    <option>JOINT_SAVINGS</option>
                    <option>JOINT_CURRENT</option>
                    <option>JOINT_BUSINESS</option>
                    <option>JOINT_FIXED</option>
                </Form.Select>
            </Form.Group>

            {accountTypeDetails && (
                <Table striped bordered hover className="mt-3">
                    <thead>
                        <tr>
                            <th>Interest Rate (%)</th>
                            <th>Min Balance</th>
                            <th>Transaction Limit</th>
                            <th>Transaction Amount Limit</th>
                            <th>Withdraw Limit</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>{accountTypeDetails.interestRate}</td>
                            <td>{accountTypeDetails.minimumBalance}</td>
                            <td>{accountTypeDetails.transactionLimit}</td>
                            <td>{accountTypeDetails.transactionAmountLimit}</td>
                            <td>{accountTypeDetails.withdrawLimit}</td>
                        </tr>
                    </tbody>
                </Table>
            )}

            {accountHolders.map((holder, idx) => (
                <div className="position-relative border p-3 mt-3 rounded shadow-sm" key={idx}>
                    {/* Remove Button (X) in Top Right Corner */}
                    {accountHolders.length > 1 && (
                        <button
                            type="button"
                            className="btn-close position-absolute top-0 end-0 m-2"
                            aria-label="Remove"
                            onClick={() => {
                                const updated = [...accountHolders];
                                updated.splice(idx, 1);
                                setAccountHolders(updated);
                            }}
                        />
                    )}
                    <h5 className="mb-3">Account Holder {idx + 1}</h5>

                    {/* Name */}
                    <Form.Group className="mb-2">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            value={holder.name}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].name = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* DOB */}
                    <Form.Group className="mb-2">
                        <Form.Label>Date of Birth</Form.Label>
                        <Form.Control
                            type="date"
                            value={holder.dob}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].dob = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* Gender */}
                    <Form.Group className="mb-2">
                        <Form.Label>Gender</Form.Label>
                        <Form.Select
                            value={holder.gender}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].gender = e.target.value;
                                setAccountHolders(list);
                            }}
                        >
                            <option value="">-- Select Gender --</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="OTHER">Other</option>
                        </Form.Select>
                    </Form.Group>

                    {/* Contact Number */}
                    <Form.Group className="mb-2">
                        <Form.Label>Contact Number</Form.Label>
                        <Form.Control
                            type="text"
                            value={holder.contactNumber}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].contactNumber = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* Email */}
                    <Form.Group className="mb-2">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            value={holder.email}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].email = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* Address */}
                    <Form.Group className="mb-2">
                        <Form.Label>Address</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={2}
                            value={holder.address}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].address = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* PAN Card Number */}
                    <Form.Group className="mb-2">
                        <Form.Label>PAN Card Number</Form.Label>
                        <Form.Control
                            type="text"
                            value={holder.panCardNumber}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].panCardNumber = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>

                    {/* Aadhar Number */}
                    <Form.Group className="mb-2">
                        <Form.Label>Aadhar Number</Form.Label>
                        <Form.Control
                            type="text"
                            value={holder.aadharNumber}
                            onChange={e => {
                                const list = [...accountHolders];
                                list[idx].aadharNumber = e.target.value;
                                setAccountHolders(list);
                            }}
                        />
                    </Form.Group>
                </div>
            ))}
            <Form.Group className="mb-3">
                <Form.Label>Account Name</Form.Label>
                <Form.Control
                    type="text"
                    value={accountName}
                    onChange={(e) => setAccountName(e.target.value)}
                    placeholder="Enter account name"
                />
            </Form.Group>

            <Form.Group className="mb-3">
                <Form.Label>Purpose</Form.Label>
                <Form.Control
                    as="textarea"
                    rows={2}
                    value={purpose}
                    onChange={(e) => setPurpose(e.target.value)}
                    placeholder="Enter the purpose of opening the account"
                />
            </Form.Group>


            {isJoint && accountHolders.length < 4 && (
                <Button variant="outline-primary" className="mt-3" onClick={() => { handleAddAccountHolder() }}>
                    <BsPlusCircle className="me-2" /> Add Account Holder
                </Button>
            )}

            <Form.Group className="mt-4">
                <Form.Label>Select State</Form.Label>
                <Form.Select value={selectedState} onChange={e => setSelectedState(e.target.value)}>
                    <option value="">-- Select State --</option>
                    {states.map(state => <option key={state}>{state}</option>)}
                </Form.Select>
            </Form.Group>

            {branches.length > 0 && (
                <Form.Group className="mt-3">
                    <Form.Label>Select Branch</Form.Label>
                    <Form.Select value={selectedBranch} onChange={e => setSelectedBranch(e.target.value)}>
                        <option value="">-- Select Branch --</option>
                        {branches.map(branch => (
                            <option key={branch.id} value={branch.branchName}>{branch.branchName}-{branch.ifsc}</option>
                        ))}
                    </Form.Select>
                </Form.Group>
            )}

            {message && <Alert variant="warning" className="mt-3">{message}</Alert>}
            <div className="d-flex justify-content-center mt-4">
                <Button variant="primary" type="submit" onClick={() => { addAccountOpeningApplication() }}>
                    Submit Application
                </Button>
            </div>
            {successMessage && (
                <div className="d-flex justify-content-center position-fixed top-0 start-0 w-100 mt-3" style={{ zIndex: 9999 }}>
                    <div className="alert alert-success text-center" role="alert">
                        {successMessage}
                    </div>
                </div>
            )}
        </div>
    );

}
export default FillAccountApplication;