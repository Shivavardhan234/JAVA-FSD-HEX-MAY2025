import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";

function AccountOpeningApplicationByCategory() {
    const [applications, setApplications] = useState([]);
    const [filter, setFilter] = useState("ALL");
    const [message, setMessage] = useState("");

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);


    const [applicationData, setApplicationData] = useState(null);
    const [accountHolders, setAccountHolders] = useState([]);
    const [showOverlay, setShowOverlay] = useState(false);


    const token = localStorage.getItem("token");
    const bearerAuth = "Bearer " + token;


 useEffect(() => {
        fetchApplications();
    }, [page,size]);


 useEffect(() => {
        fetchApplications();
    }, [filter]);



    const fetchApplications = async () => {
        try {
            const url =
                filter === "ALL"
                    ? `http://localhost:9090/api/account-opening-application/get/all?page=${page}&size=${size}`
                    : `http://localhost:9090/api/account-opening-application/get/by-employee-approval-status/${filter}?page=${page}&size=${size}`;
            const res = await axios.get(url, {
                headers: { Authorization: bearerAuth },
            });
            setApplications(res.data);
            setMessage("");
        } catch (err) {
            console.error(err);
            setApplications([]);
            setMessage("Failed to fetch applications.");
        }
    };

    const handleApprove = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

            const response = await axios.put(
                `http://localhost:9090/api/account-opening-application/update/approve/${applicationData.id}`,
                {}, // no body
                {
                    headers: {
                        Authorization: bearerAuth,
                    },
                }
            );

            alert("Application approved successfully!");
            setShowOverlay(false);
            fetchApplications();
        } catch (error) {
            console.error(error);
            alert("Failed to approve the application.");
        }
    };

    const handleReject = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

            const response = await axios.put(
                `http://localhost:9090/api/account-opening-application/update/reject/${applicationData.id}`,
                {}, // no body
                {
                    headers: {
                        Authorization: bearerAuth,
                    },
                }
            );

            alert("Application rejected successfully!");
            setShowOverlay(false);
            fetchApplications();
        } catch (error) {
            console.error(error);
            alert("Failed to reject the application.");
        }
    };

    const handleViewApplication = async (applicationId) => {
        setShowOverlay(true);
        setApplicationData(null);
        setAccountHolders([]);
        setMessage("");

        try {
            const appRes = await axios.get(
                `http://localhost:9090/api/account-opening-application/get/by-id/${applicationId}`,
                {
                    headers: { Authorization: bearerAuth },
                }
            );
            setApplicationData(appRes.data);

            const holdersRes = await axios.get(
                `http://localhost:9090/api/customer-account-opening-application/get/by-application-id/${applicationId}`,
                {
                    headers: { Authorization: bearerAuth },
                }
            );
            setAccountHolders(holdersRes.data.map(item => item.accountHolder));
        } catch (err) {
            console.error(err);
            setMessage("Failed to fetch application details.");
        }
    };


   

    return (
        <div className="d-flex flex-column" style={{ height: "calc(100vh - 56px)", marginLeft: "70px", padding: "20px" }}>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Account Manager</li>
                    <li className="breadcrumb-item active">Applications By Category</li>
                </ol>
            </nav>

            <div className="card shadow-sm">
                <div className="card-header bg-light d-flex justify-content-between align-items-center">
                    <Form.Select style={{ width: "200px" }} value={filter} onChange={(e) => setFilter(e.target.value)}>
                        <option value="ALL">All Applications</option>
                        <option value="PENDING">Pending</option>
                        <option value="ACCEPTED">Accepted</option>
                        <option value="REJECTED">Rejected</option>
                    </Form.Select>
                </div>

                <div className="card-body overflow-auto" style={{ maxHeight: "70vh" }}>
                    {message && <div className="alert alert-danger">{message}</div>}
                    {applications.map((app, idx) => (
                        <div key={idx} className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white">
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">{app.accountType.accountType}</h5>
                            </div>
                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                <div className="d-flex align-items-center">
                                    <i
                                        className="bi bi-file-earmark-text fs-3 me-3"
                                        style={{
                                            color:
                                                app.employeeApprovalStatus === "PENDING"
                                                    ? "#ffc107"
                                                    : app.employeeApprovalStatus === "ACCEPTED"
                                                        ? "#198754"
                                                        : "#dc3545",
                                        }}
                                    />
                                    <div>
                                        <p className="mb-1"><strong>Branch:</strong> {app.branch.branchName}</p>
                                        {app.accountName && (
                                            <p className="mb-0"><strong>Account Name:</strong> {app.accountName}</p>
                                        )}
                                    </div>
                                </div>
                                <Button variant="outline-primary" size="sm" onClick={() => handleViewApplication(app.id)}>
                                    View Application
                                </Button>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="card-footer d-flex justify-content-between align-items-center bg-light">
                    <div className="d-flex align-items-center">
                        <span className="me-2">Items per page:</span>
                        <Form.Select
                            value={size}
                            onChange={(e) => {
                                setSize(Number(e.target.value));
                                setPage(0);
                                
                            }}
                            style={{ width: "80px" }}
                        >
                            {[5, 10, 20].map(num => (
                                <option key={num} value={num}>{num}</option>
                            ))}
                        </Form.Select>
                    </div>
                    <nav>
                        <ul className="pagination mb-0">
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page - 1)}>&laquo;</button>
                            </li>
                            
                                <li key={page} className="page-item">
                                    <button className="page-link" >{page + 1}</button>
                                </li>
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page + 1)}>&raquo;</button>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>

            {showOverlay && applicationData && (
                <div
                    className="position-fixed bg-dark bg-opacity-50 d-flex justify-content-center align-items-start"
                    style={{
                        top: '56px',
                        left: '70px',
                        width: 'calc(100% - 70px)',
                        height: 'calc(100vh - 56px)',
                        backdropFilter: 'blur(4px)',
                        zIndex: 1050,
                        paddingTop: '30px',
                    }}
                >
                    <div className="card shadow-lg position-absolute top-50 start-50 translate-middle" style={{ width: '90%', maxWidth: '800px', maxHeight: '85vh' }}>
                        <div className="card-header bg-light"><h5 className="mb-0">Account Opening Application</h5></div>
                        <div className="card-body overflow-auto" style={{ maxHeight: '60vh' }}>
                            <div className="card mb-3 border">
                                <div className="card-header bg-light text-dark border-bottom border-secondary">
                                    <h6 className="mb-0">Application Details</h6>
                                </div>
                                <div className="card-body">
                                    <p><strong>Branch Name:</strong> {applicationData.branch?.branchName || 'N/A'}</p>
                                    <p><strong>IFSC:</strong> {applicationData.branch?.ifsc || 'N/A'}</p>
                                    <p><strong>Account Type:</strong> {applicationData.accountType?.accountType || 'N/A'}</p>
                                    <hr />
                                    <p><strong>Account Name:</strong> {applicationData.accountName || 'N/A'}</p>
                                    <p><strong>Purpose:</strong> {applicationData.purpose || 'N/A'}</p>
                                    <p><strong>Customer Approval Status:</strong> {applicationData.customerApprovalStatus}</p>
                                    <p><strong>Employee Approval Status:</strong> {applicationData.employeeApprovalStatus}</p>
                                    <p><strong>Application Date & Time:</strong> {new Date(applicationData.applicationDateTime).toLocaleString()}</p>
                                </div>
                            </div>
                            <h6>Account Holders</h6>
                            {accountHolders.length === 0 ? (
                                <p>No account holders found.</p>
                            ) : (
                                accountHolders.map((holder, idx) => (
                                    <div key={idx} className="mb-3 p-3 border rounded bg-light">
                                        <h6>Holder {idx + 1}</h6>
                                        <p><strong>Name:</strong> {holder.name}</p>
                                        <p><strong>Date of Birth:</strong> {holder.dob}</p>
                                        <p><strong>Gender:</strong> {holder.gender}</p>
                                        <p><strong>Contact:</strong> {holder.contactNumber}</p>
                                        <p><strong>Email:</strong> {holder.email}</p>
                                        <p><strong>Address:</strong> {holder.address}</p>
                                        <p><strong>PAN:</strong> {holder.panCardNumber}</p>
                                        <p><strong>Aadhar:</strong> {holder.aadharNumber}</p>
                                    </div>
                                ))
                            )}
                        </div>
                        <div className="card-footer d-flex justify-content-between">
                            <Button variant="secondary" onClick={() => setShowOverlay(false)}>
                                Back
                            </Button>

                            {applicationData?.employeeApprovalStatus === 'PENDING' &&
                                applicationData?.customerApprovalStatus === 'ACCEPTED' && (
                                    <div className="d-flex gap-2">
                                        <Button variant="success" onClick={handleApprove}>Approve</Button>
                                        <Button variant="danger" onClick={handleReject}>Reject</Button>
                                    </div>
                                )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AccountOpeningApplicationByCategory;
