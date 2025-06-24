import { useEffect, useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { useOutletContext } from 'react-router-dom';
import { getUserDetails } from '../../../store/actions/UserAction';
import { useDispatch, useSelector } from 'react-redux';

function JointAccountRequests() {
    const dispatch = useDispatch();
    const { isExpanded } = useOutletContext();

    const [applications, setApplications] = useState([]);
    const [currentApplications, setCurrentApplications] = useState([]);
    const [customerAccountOpeningApplication, setCustomerAccountOpeningApplication] = useState();
    const [accountHolders, setAccountHolders] = useState([]);
    const [applicationData, setApplicationData] = useState(null);

    const [filter, setFilter] = useState("ALL");
    const [currentPage, setCurrentPage] = useState(1);
    const [perPage, setPerPage] = useState(5);
    const [message, setMessage] = useState("");
    const [showOverlay, setShowOverlay] = useState(false);

    const customer = useSelector(state => state.user.userDetails);

    useEffect(() => {
        getUserDetails(dispatch)();
    }, [dispatch]);

    useEffect(() => {
        if (customer && customer.id) {
            fetchApplications();
        }
    }, [customer]);

    useEffect(() => {
        applyFilter();
    }, [applications, filter, currentPage, perPage]);

    const fetchApplications = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

           
            const response = await axios.get("http://localhost:9090/api/customer-account-opening-application/get/customer-approval-pending", {
                headers: { Authorization: bearerAuth }
            });

            setApplications(response.data);
        } catch (err) {
            console.error(err);
            setMessage("Failed to load joint account requests.");
        }
    };

    const applyFilter = () => {
        const startIndex = (currentPage - 1) * perPage;
        const paginated = applications.slice(startIndex, startIndex + perPage);
        setCurrentApplications(paginated);
    };

    useEffect(() => {
            if (showOverlay) {
                document.body.style.overflow = 'hidden';
            } else {
                document.body.style.overflow = 'auto';
            }
        }, [showOverlay]);

    const handleViewApplication = async (applicationId) => {
        setShowOverlay(true);
        setMessage("");
        setAccountHolders([]);
        setApplicationData(null);

        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

            const response = await axios.get(
                `http://localhost:9090/api/customer-account-opening-application/get/by-application-id/${applicationId}`,
                { headers: { Authorization: bearerAuth } }
            );

            const apps = response.data;
            if (apps.length === 0) {
                setMessage("No application data found.");
                return;
            }

            setCustomerAccountOpeningApplication(apps.find(item => item.customer.id === customer.id));

            setAccountHolders(apps.map(item => item.accountHolder));
            setApplicationData(apps[0].accountOpeningApplication);
        } catch (err) {
            console.error(err);
            setMessage("Failed to fetch application details.");
        }
    };

    const handleApproveReject = async (status) => {
        try {
            const token = localStorage.getItem("token");
            const bearerAuth = "Bearer " + token;

            await axios.put(
                `http://localhost:9090/api/customer-account-opening-application/update/approval/${customerAccountOpeningApplication.id}/${status}`,
                {},
                { headers: { Authorization: bearerAuth } }
            );

            fetchApplications();
            setShowOverlay(false);
        } catch (err) {
           console.error(err);
            if (err.response && err.response.data) {
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    const totalPages = Math.ceil(applications.length / perPage);

    return (
        <div className="d-flex flex-column" style={{ height: "calc(100vh - 56px)", marginLeft: "70px", padding: "20px" }}>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item text-muted">Customer</li>
                    <li className="breadcrumb-item active">Joint Account Requests</li>
                </ol>
            </nav>

            <div className="card shadow-sm">
                {/* Header without filter dropdown */}
                <div className="card-header bg-light d-flex justify-content-between align-items-center">
                    <h5 className="mb-0">Joint Account Applications</h5>
                </div>

                {/* Body */}
                <div className="card-body overflow-auto" style={{ maxHeight: "70vh" }}>
                    {message && (
                        <div className="alert alert-danger" role="alert">{message}</div>
                    )}
                    {currentApplications && currentApplications.map((app, idx) => (
                        <div key={idx} className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white">
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">{app.accountOpeningApplication.accountType.accountType}</h5>
                            </div>
                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                <div className="d-flex align-items-center">
                                    <i className="bi bi-file-earmark-text fs-3 me-3"
                                        style={{
                                            color:
                                                app.accountOpeningApplication.employeeApprovalStatus === "PENDING" ? "#ffc107" :
                                                    app.accountOpeningApplication.employeeApprovalStatus === "ACCEPTED" ? "#198754" : "#dc3545"
                                        }}
                                    />
                                    <div>
                                        <p className="mb-1"><strong>Branch:</strong> {app.accountOpeningApplication.branch.branchName}</p>
                                        {app.accountOpeningApplication.accountName && (
                                            <p className="mb-0"><strong>Account Name:</strong> {app.accountOpeningApplication.accountName}</p>
                                        )}
                                    </div>
                                </div>
                                <div>
                                    <Button variant="outline-primary" size="sm" onClick={() => handleViewApplication(app.accountOpeningApplication.id)}>
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

            {/* Overlay for view application */}
            {showOverlay && (
                <div className="position-fixed bg-dark bg-opacity-50 d-flex justify-content-center align-items-start"
                    style={{
                        top: '56px',
                        left: isExpanded ? '240px' : '70px',
                        width: isExpanded ? 'calc(100% - 240px)' : 'calc(100% - 70px)',
                        height: 'calc(100vh - 56px)',
                        backdropFilter: 'blur(4px)',
                        zIndex: 1050,
                        paddingTop: '30px'
                    }}>
                    <div className="card shadow-lg position-absolute top-50 start-50 translate-middle"
                        style={{ width: '90%', maxWidth: '800px', maxHeight: '85vh' }}>

                        <div className="card-header bg-light text-dark border-bottom border-secondary">
                            <h5 className="mb-0">Account Opening Application</h5>
                        </div>

                        <div className="card-body overflow-auto" style={{ maxHeight: '60vh' }}>
                            <div className="card mb-3 border">
                                <div className="card-header bg-light text-dark border-bottom border-secondary">
                                    <h6 className="mb-0">Application Details</h6>
                                </div>
                                <div className="card-body">
                                    <p><strong>Branch Name:</strong> {applicationData?.branch?.branchName || 'N/A'}</p>
                                    <p><strong>IFSC:</strong> {applicationData?.branch?.ifsc || 'N/A'}</p>
                                    <p><strong>Account Type:</strong> {applicationData?.accountType?.accountType || 'N/A'}</p>
                                    <p><strong>Account Name:</strong> {applicationData?.accountName || 'N/A'}</p>
                                    <p><strong>Purpose:</strong> {applicationData?.purpose || 'N/A'}</p>
                                    <p><strong>Customer Approval:</strong> {applicationData?.customerApprovalStatus || 'N/A'}</p>
                                    <p><strong>Employee Approval:</strong> {applicationData?.employeeApprovalStatus || 'N/A'}</p>
                                    <p><strong>Applied On:</strong> {applicationData?.applicationDateTime ? new Date(applicationData.applicationDateTime).toLocaleString() : 'N/A'}</p>
                                </div>
                            </div>

                            <h6>Account Holders</h6>
                            {accountHolders.length === 0 ? (
                                <p>No account holders found.</p>
                            ) : (
                                accountHolders.map((holder, idx) => (
                                    <div key={idx} className="mb-3 p-3 border rounded bg-light-subtle">
                                        <h6>Holder {idx + 1}</h6>
                                        <p><strong>Name:</strong> {holder.name || 'N/A'}</p>
                                        <p><strong>DOB:</strong> {holder.dob || 'N/A'}</p>
                                        <p><strong>Gender:</strong> {holder.gender || 'N/A'}</p>
                                        <p><strong>Contact:</strong> {holder.contactNumber || 'N/A'}</p>
                                        <p><strong>Email:</strong> {holder.email || 'N/A'}</p>
                                        <p><strong>Address:</strong> {holder.address || 'N/A'}</p>
                                        <p><strong>PAN:</strong> {holder.panCardNumber || 'N/A'}</p>
                                        <p><strong>Aadhar:</strong> {holder.aadharNumber || 'N/A'}</p>
                                    </div>
                                ))
                            )}
                        </div>

                        <div className="card-footer d-flex justify-content-end gap-2">
                            <Button variant="success" onClick={() => handleApproveReject("ACCEPTED")}>
                                Accept
                            </Button>
                            <Button variant="danger" onClick={() => handleApproveReject("REJECTED")}>
                                Reject
                            </Button>
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

export default JointAccountRequests;
