import { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate, useOutletContext } from "react-router-dom";
import axios from "axios";
import { Button, Form } from "react-bootstrap";
import { getUserDetails } from "../../../store/actions/UserAction";

function MyAccountOpeningApplications() {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { isExpanded } = useOutletContext();

    const customer = useSelector(state => state.user.userDetails);
    const [applications, setApplications] = useState([]);
    const [filter, setFilter] = useState("ALL");
    const [message, setMessage] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const [applicationData, setApplicationData] = useState();
    const [accountHolders, setAccountHolders] = useState([]);
    const [showOverlay, setShowOverlay] = useState(false);

    const indexOfLast = currentPage * perPage;
    const indexOfFirst = indexOfLast - perPage;
    const currentApplications = applications.slice(indexOfFirst, indexOfLast);
    const totalPages = Math.ceil(applications.length / perPage);

    useEffect(() => { getUserDetails(dispatch)(); }, []);
    useEffect(() => {
        if (customer && customer.id) {
            fetchApplications();
        }
    }, [filter, customer]);

    const fetchApplications = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            let url = "";

            if (filter === "ALL") {
                url = `http://localhost:9090/api/customer-account-opening-application/get/by-customer-id/${customer.id}`;
            } else {
                url = `http://localhost:9090/api/customer-account-opening-application/get/by-customer-id-status/${customer.id}/${filter}`;
            }

            const response = await axios.get(url, {
                headers: { Authorization: bearerToken }
            });

            setApplications(response.data);
            setCurrentPage(1);
        } catch (err) {
            console.error(err);
            setMessage("Unable to fetch applications.");
        }
    };


    const handleViewApplication = async (applicationId) => {
        setShowOverlay(true); // 1. Show the overlay first
        setMessage("");        // (Optional) Clear old errors/messages
        setAccountHolders([]); // Reset old data

        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

            const response = await axios.get(
                `http://localhost:9090/api/customer-account-opening-application/get/by-application-id/${applicationId}`,
                {
                    headers: {
                        Authorization: bearerAuth,
                    },
                }
            );

            // This returns a list of CustomerAccountOpeningApplication
            const applications = response.data;

            if (applications.length === 0) {
                setMessage("No account holders found for this application.");
                return;
            }
            setApplicationData(applications[0].accountOpeningApplication);


            setAccountHolders(applications.map(item => item.accountHolder));
        } catch (err) {
            console.error(err);
            setMessage("Failed to fetch application details.");
        }
    };

    useEffect(() => {
                if (showOverlay) {
                    document.body.style.overflow = 'hidden';
                } else {
                    document.body.style.overflow = 'auto';
                }
            }, [showOverlay]);

    return (
        <div
            className="d-flex flex-column"
            style={{
                height: "calc(100vh - 56px)",
                marginLeft: "70px",
                padding: "20px",
            }}
        >
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Customer</li>
                    <li className="breadcrumb-item active">My Applications</li>
                </ol>
            </nav>

            <div className="card shadow-sm">
                {/* Header */}
                <div className="card-header bg-light d-flex justify-content-between align-items-center">
                    <Form.Select
                        style={{ width: "200px" }}
                        value={filter}
                        onChange={(e) => setFilter(e.target.value)}
                    >
                        <option value="ALL">All Applications</option>
                        <option value="PENDING">Pending</option>
                        <option value="ACCEPTED">Accepted</option>
                        <option value="REJECTED">Rejected</option>
                    </Form.Select>
                </div>

                {/* Body */}
                <div className="card-body overflow-auto" style={{ maxHeight: "70vh" }}>
                    {message && (
                        <div className="alert alert-danger" role="alert">
                            {message}
                        </div>
                    )}
                    {currentApplications.map((app, idx) => (
                        <div
                            key={idx}
                            className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white"
                        >
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">{app.accountOpeningApplication.accountType.accountType}</h5>
                            </div>
                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                {/* Left: icon and info */}
                                <div className="d-flex align-items-center">
                                    <i
                                        className="bi bi-file-earmark-text fs-3 me-3"
                                        style={{
                                            color:
                                                app.accountOpeningApplication.employeeApprovalStatus === "PENDING"
                                                    ? "#ffc107" // yellow
                                                    : app.accountOpeningApplication.employeeApprovalStatus === "ACCEPTED"
                                                        ? "#198754" // green
                                                        : "#dc3545" // red
                                        }}
                                    />
                                    <div>
                                        <p className="mb-1">
                                            <strong>Branch:</strong> {app.accountOpeningApplication.branch.branchName}
                                        </p>
                                        {app.accountOpeningApplication.accountName && (
                                            <p className="mb-0">
                                                <strong>Account Name:</strong> {app.accountOpeningApplication.accountName}
                                            </p>
                                        )}
                                    </div>
                                </div>
                                <div>
                                    <Button
                                        variant="outline-primary"
                                        size="sm"
                                        onClick={() => handleViewApplication(app.accountOpeningApplication.id)}
                                    >
                                        View Application
                                    </Button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Footer */}
                <div className="card-footer d-flex justify-content-between align-items-center bg-light">
                    <div className="d-flex align-items-center">
                        <span className="me-2">Items per page:</span>
                        <Form.Select
                            value={perPage}
                            onChange={(e) => {
                                setCurrentPage(1);
                                setPerPage(Number(e.target.value));
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
                            <li className={`page-item ${currentPage === 1 ? "disabled" : ""}`}>
                                <button className="page-link" onClick={() => setCurrentPage(currentPage - 1)}>&laquo;</button>
                            </li>
                            {[...Array(totalPages).keys()].map(i => (
                                <li key={i} className={`page-item ${currentPage === i + 1 ? "active" : ""}`}>
                                    <button className="page-link" onClick={() => setCurrentPage(i + 1)}>{i + 1}</button>
                                </li>
                            ))}
                            <li className={`page-item ${currentPage === totalPages ? "disabled" : ""}`}>
                                <button className="page-link" onClick={() => setCurrentPage(currentPage + 1)}>&raquo;</button>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
            {showOverlay && (
                <div
                    className="position-fixed bg-dark bg-opacity-50 d-flex justify-content-center align-items-start"
                    style={{
                        top: '56px',
                        left: isExpanded ? '240px' : '70px',
                        width: isExpanded ? 'calc(100% - 240px)' : 'calc(100% - 70px)',
                        height: 'calc(100vh - 56px)',
                        backdropFilter: 'blur(4px)',
                        zIndex: 1050,
                        transition: 'left 0.3s ease, width 0.3s ease',
                        overflowY: 'auto',
                        paddingTop: '30px',
                    }}
                >
                    {/* Inner Card Box */}
                    <div
                        className="card shadow-lg position-absolute top-50 start-50 translate-middle"
                        style={{ width: '90%', maxWidth: '800px', maxHeight: '85vh' }}
                    >
                        {/* Header */}
                        <div className="card-header" style={{ backgroundColor: '#f8f9fa' }}>
                            {/* ash/light gray background */}
                            <h5 className="mb-0">Account Opening Application</h5>
                        </div>

                        {/* Body */}
                        {/* Body */}
                        <div className="card-body overflow-auto" style={{ maxHeight: '60vh' }}>
                            {/* Nested Card for Application Details */}
                            <div className="card mb-3 border">
                                {/* Nested Card Header with ash bg */}
                                <div className="card-header bg-light text-dark border-bottom border-secondary">
                                    <h6 className="mb-0">Application Details</h6>
                                </div>

                                {/* Nested Card Body with borders */}
                                <div className="card-body">
                                    {/* Branch info */}
                                    <p>
                                        <strong>Branch Name:</strong>{' '}
                                        {applicationData?.branch?.branchName || 'N/A'}
                                    </p>
                                    <p>
                                        <strong>IFSC:</strong> {applicationData?.branch?.ifsc || 'N/A'}
                                    </p>

                                    {/* Account Type */}
                                    <p>
                                        <strong>Account Type:</strong>{' '}
                                        {applicationData?.accountType?.accountType || 'N/A'}
                                    </p>

                                    <hr />

                                    {/* Additional Account Info */}
                                    <p>
                                        <strong>Account Name:</strong> {applicationData?.accountName || 'N/A'}
                                    </p>
                                    <p>
                                        <strong>Purpose:</strong> {applicationData?.purpose || 'N/A'}
                                    </p>
                                    <p>
                                        <strong>Customer Approval Status:</strong> {applicationData?.customerApprovalStatus || 'N/A'}
                                    </p>
                                    <p>
                                        <strong>Employee Approval Status:</strong> {applicationData?.employeeApprovalStatus || 'N/A'}
                                    </p>
                                    <p>
                                        <strong>Application Date & Time:</strong>{' '}
                                        {applicationData?.applicationDateTime
                                            ? new Date(applicationData.applicationDateTime).toLocaleString()
                                            : 'N/A'}
                                    </p>
                                </div>
                            </div>

                            {/* Account Holders Section (outside nested card) */}
                            <h6>Account Holders</h6>
                            {accountHolders.length === 0 ? (
                                <p>No account holders found.</p>
                            ) : (
                                accountHolders.map((holder, idx) => (
                                    <div
                                        key={idx}
                                        className="mb-3 p-3 border rounded"
                                        style={{ backgroundColor: '#fefefe' }}
                                    >
                                        <h6>Holder {idx + 1}</h6>
                                        <p>
                                            <strong>Name:</strong> {holder.name || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Date of Birth:</strong> {holder.dob || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Gender:</strong> {holder.gender || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Contact:</strong> {holder.contactNumber || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Email:</strong> {holder.email || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Address:</strong> {holder.address || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>PAN Card Number:</strong> {holder.panCardNumber || 'N/A'}
                                        </p>
                                        <p>
                                            <strong>Aadhar Number:</strong> {holder.aadharNumber || 'N/A'}
                                        </p>
                                    </div>
                                ))
                            )}
                        </div>



                        {/* Footer */}
                        <div className="card-footer d-flex justify-content-end">
                            <Button variant="secondary" onClick={() => setShowOverlay(false)}>
                                Back
                            </Button>
                        </div>
                    </div>

                </div>
            )}
        </div>
    );
}

export default MyAccountOpeningApplications;
