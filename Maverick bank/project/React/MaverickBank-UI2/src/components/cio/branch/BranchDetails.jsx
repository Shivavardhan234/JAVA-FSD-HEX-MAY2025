import { useEffect, useState } from "react";
import { Button } from "react-bootstrap";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { useOutletContext } from "react-router-dom";
import { Link } from "react-router-dom";

function BranchDetails() {
    const location = useLocation();
    const [branch, setBranch] = useState(location.state?.branch || null);
    const [branchName, setBranchName] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [ifsc, setIfsc] = useState('');
    const [message, setMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [showEditOverlay, setShowEditOverlay] = useState(false);
    const { isExpanded } = useOutletContext();

    useEffect(() => {
        if (branch) {
            setBranchName(branch.branchName || '');
            setContactNumber(branch.contactNumber || '');
            setEmail(branch.email || '');
            setAddress(branch.address || '');
            setIfsc(branch.ifsc || '');
        }
    }, [branch]);

    const deactivateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const res = await axios.put(`http://localhost:9090/api/branch/deactivate/${branch.id}`, {}, {
                headers: { Authorization: "Bearer " + token }
            });
            setBranch(res.data);
        } catch (err) {
            handleError(err);
        }
    };

    const activateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const res = await axios.put(`http://localhost:9090/api/branch/activate/${branch.id}`, {}, {
                headers: { Authorization: "Bearer " + token }
            });
            setBranch(res.data);
        } catch (err) {
            handleError(err);
        }
    };

    const updateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const res = await axios.put(`http://localhost:9090/api/branch/update`, {
                id: branch.id,
                ifsc, branchName, contactNumber, email, address
            }, {
                headers: { Authorization: "Bearer " + token }
            });
            setBranch(res.data);
            setShowEditOverlay(false);
            setSuccessMessage("Branch updated successfully!");
            setTimeout(() => setSuccessMessage(""), 3000);
        } catch (err) {
            handleError(err);
        }
    };

    const handleError = (err) => {
        console.error(err);
        if (err.response?.data) {
            const firstKey = Object.keys(err.response.data)[0];
            setMessage(err.response.data[firstKey]);
        } else {
            setMessage("Something went wrong. Try again.");
        }
    };

    useEffect(() => {
        document.body.style.overflow = showEditOverlay ? 'hidden' : 'auto';
    }, [showEditOverlay]);

    return (
        <>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <Link to="/cio/cioBranch/category" className="text-decoration-none">
                            Branches by Category
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        View Branch
                    </li>
                </ol>
            </nav>

            <div className="container-fluid position-relative">
                <div className="card shadow border-0 bg-light-subtle" style={{ borderRadius: "10px" }}>
                    {/* Header */}
                    <div className="card-header bg-secondary bg-opacity-10 fw-semibold"
                        style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}>
                        <h5 className="mb-0">Branch Details</h5>
                    </div>

                    {/* Body */}
                    <div className="card-body">
                        {branch ? (
                            <div className="row">
                                <div className="col-md-3 d-flex justify-content-center align-items-center">
                                    <i className={`bi bi-bank2 ${branch.status === "ACTIVE" ? "text-success" : "text-danger"}`}
                                        style={{ fontSize: "5rem" }}></i>
                                </div>
                                <div className="col-md-9">
                                    <div className="mb-2"><strong>ID:</strong> {branch.id}</div>
                                    <div className="mb-2"><strong>IFSC:</strong> {branch.ifsc}</div>
                                    <div className="mb-2"><strong>Name:</strong> {branch.branchName}</div>
                                    <div className="mb-2"><strong>Contact:</strong> {branch.contactNumber}</div>
                                    <div className="mb-2"><strong>Email:</strong> {branch.email}</div>
                                    <div className="mb-2"><strong>Address:</strong> {branch.address}</div>
                                    <div className="mb-2"><strong>Status:</strong> {branch.status}</div>
                                </div>
                            </div>
                        ) : (
                            <p className="text-muted">No branch selected.</p>
                        )}
                        {message && <div className="text-danger mt-2">{message}</div>}
                    </div>

                    {/* Footer */}
                    {branch && (
                        <div className="card-footer d-flex justify-content-end gap-2">
                            <Button variant="secondary" size="sm" onClick={() => setShowEditOverlay(true)}>Edit Branch</Button>
                            {branch.status === "ACTIVE" ? (
                                <Button variant="danger" size="sm" onClick={deactivateBranch}>Deactivate</Button>
                            ) : (
                                <Button variant="success" size="sm" onClick={activateBranch}>Activate</Button>
                            )}
                        </div>
                    )}
                </div>

                {/* Edit Overlay */}
                {showEditOverlay && (
                    <div className="position-fixed bg-dark bg-opacity-50"
                        style={{
                            top: '56px',
                            left: isExpanded ? '240px' : '70px',
                            width: isExpanded ? 'calc(100% - 240px)' : 'calc(100% - 70px)',
                            height: 'calc(100vh - 56px)',
                            backdropFilter: 'blur(4px)',
                            zIndex: 1050,
                            transition: 'left 0.3s ease, width 0.3s ease',
                        }}>
                        <div className="d-flex justify-content-center align-items-center h-100">
                            <div className="bg-white p-4 rounded shadow-lg" style={{ width: '90%', maxWidth: '600px', maxHeight: '90vh', overflowY: 'auto' }}>
                                <div className="bg-secondary text-white rounded p-2 mb-3">
                                    <h5 className="mb-0">Edit Branch</h5>
                                </div>
                                <div className="row">
                                    <div className="col-md-6">
                                        <div className="mb-3"><label className="form-label">IFSC</label>
                                            <input type="text" className="form-control" value={ifsc} onChange={(e) => setIfsc(e.target.value)} /></div>
                                        <div className="mb-3"><label className="form-label">Branch Name</label>
                                            <input type="text" className="form-control" value={branchName} onChange={(e) => setBranchName(e.target.value)} /></div>
                                        <div className="mb-3"><label className="form-label">Contact Number</label>
                                            <input type="text" className="form-control" value={contactNumber} onChange={(e) => setContactNumber(e.target.value)} /></div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="mb-3"><label className="form-label">Email</label>
                                            <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} /></div>
                                        <div className="mb-3"><label className="form-label">Address</label>
                                            <input type="text" className="form-control" value={address} onChange={(e) => setAddress(e.target.value)} /></div>
                                    </div>
                                </div>
                                {message && <div className="text-danger mt-2">{message}</div>}
                                <div className="d-flex justify-content-end gap-3 mt-3">
                                    <button className="btn btn-secondary" onClick={() => setShowEditOverlay(false)}>Cancel</button>
                                    <button className="btn btn-success" onClick={updateBranch}>Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
                

                {successMessage && (
                    <div className="d-flex justify-content-center position-fixed top-0 start-0 w-100 mt-3" style={{ zIndex: 9999 }}>
                        <div className="alert alert-success text-center" role="alert">{successMessage}</div>
                    </div>
                )}
            </div>
        </>
    );
}

export default BranchDetails;
