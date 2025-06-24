import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import { FaUserCircle } from 'react-icons/fa';
import { getUserDetails, logOutAction } from '../../store/actions/UserAction';

function EmployeeProfile() {
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const employee = useSelector(state => state.user.userDetails);
    const [showEditOverlay, setShowEditOverlay] = useState(false);
    const [showPasswordOverlay, setShowPasswordOverlay] = useState(false);
    const [showDeactivateOverlay, setShowDeactivateOverlay] = useState(false);

    const [currentPassword, setCurrentPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [rePassword, setRePassword] = useState("");
    const [newUsername, setNewUsername] = useState("");
    const [successMessage, setSuccessMessage] = useState("");
    const [message, setMessage] = useState("");

    useEffect(() => {
        getUserDetails(dispatch)();
    }, [])


    useEffect(() => {
        if (employee?.user?.username) {
            setNewUsername(employee.user.username);
        }
    }, [employee]);

    const getStatusClass = (status) => {
        return status === "ACTIVE"
            ? "text-success fw-semibold"
            : status === "INACTIVE"
                ? "text-warning fw-semibold"
                : "text-danger fw-semibold";
    };

    const handleChangePassword = async () => {
        setMessage("");
        if (currentPassword === newPassword) {
            setMessage("New password cant be same as old password!!!");
            return;
        }
        if (newPassword !== rePassword) {
            setMessage("New passwords do not match!!!");
            return;
        }

        const token = localStorage.getItem('token');
        const baererAuthString = "Bearer " + token;

        try {
            await axios.put(
                `http://localhost:9090/api/user/update/password/${currentPassword}/${newPassword}`,
                {},
                { headers: { Authorization: baererAuthString } }
            );

            setShowPasswordOverlay(false);
            localStorage.setItem("password", newPassword);
            setCurrentPassword(newPassword);

            const encryptedCredentials = window.btoa(newUsername + ':' + newPassword);
            const basicAuthString = "Basic " + encryptedCredentials;
            const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                headers: { Authorization: basicAuthString }
            });

            localStorage.setItem('token', tokenResponse.data);
            getUserDetails(dispatch)();
            setSuccessMessage("Password changed successfully ");
            setMessage("");
            setTimeout(() => setSuccessMessage(""), 3000);
            setCurrentPassword("");
            setNewPassword("");
            setRePassword("");

        } catch (err) {
            console.error(err);
            if (err.response && err.response.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    const deactivateAccount = async () => {
        const token = localStorage.getItem('token');
        const baererAuthString = "Bearer " + token;

        try {
            await axios.put(
                `http://localhost:9090/api/user/update/deactivate/${currentPassword}`,
                {},
                { headers: { Authorization: baererAuthString } }
            );

            setShowDeactivateOverlay(false);
            localStorage.clear();
            logOutAction(dispatch)();
            navigate('/');
        } catch (err) {
            console.error(err);
            if (err.response && err.response.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    const updateEmployee = async () => {
        const token = localStorage.getItem("token");
        const baererAuthString = "Bearer " + token;

        try {
            if (employee.user.username !== newUsername) {
                await axios.put(`http://localhost:9090/api/user/update/username/${newUsername}`,
                    {},
                    { headers: { Authorization: baererAuthString } }
                );

                const password = localStorage.getItem('password');
                const encryptedCredentials = window.btoa(newUsername + ':' + password);
                const basicAuthString = "Basic " + encryptedCredentials;

                const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                    headers: { Authorization: basicAuthString }
                });

                localStorage.setItem("token", tokenResponse.data);
                getUserDetails(dispatch)();
            }

            setSuccessMessage("Profile updated successfully");
            setShowEditOverlay(false);
            setMessage("");
            setTimeout(() => setSuccessMessage(""), 3000);
            setNewUsername("");
        } catch (err) {
            console.error(err);
            if (err.response && err.response.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    return (
        <div className="container mt-4">
            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card border border-dark shadow rounded-3 bg-white">
                        <div className="card-header bg-secondary bg-opacity-10 fw-semibold"
                            style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}>
                            Employee Profile
                        </div>

                        <div className="card-body">
                            {employee ? (
                                <div className="d-flex">
                                    <div className="d-flex align-items-center justify-content-center me-4" style={{ minWidth: "200px" }}>
                                        <FaUserCircle size={160} className="text-secondary" />
                                    </div>

                                    <div className="w-100">
                                        <div className="bg-secondary text-white rounded p-2 mb-3">
                                            <h5 className="mb-0">Employee Information</h5>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-6 mb-2"><strong>Username:</strong> {employee?.user?.username}</div>
                                            <div className="col-md-6 mb-2"><strong>Contact Number:</strong> {employee.contactNumber}</div>
                                            <div className="col-md-6 mb-2"><strong>Full Name:</strong> {employee.name}</div>
                                            <div className="col-md-6 mb-2"><strong>Email:</strong> {employee.email}</div>
                                            <div className="col-md-6 mb-2"><strong>Gender:</strong> {employee.gender}</div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Account Status:</strong>{" "}
                                                <span className={getStatusClass(employee?.user?.status)}>{employee?.user?.status}</span>
                                            </div>
                                            <div className="col-md-6 mb-2"><strong>Date of Birth:</strong> {employee.dob}</div>
                                            <div className="col-md-6 mb-2"><strong>Designation:</strong> {employee.designation}</div>
                                            <div className="col-md-12 mb-2"><strong>Branch:</strong> {employee?.branch?.branchName}</div>
                                            <div className="col-md-12 mb-2">
                                                <button className="btn btn-outline-secondary btn-sm"
                                                    onClick={() => navigate("/accountManager/viewBranchForAccountManager", { state: { branch: employee.branch } })}>
                                                    View Branch
                                                </button>
                                            </div>
                                            <div className="col-md-12 mb-2"><strong>Address:</strong> {employee.address}</div>
                                        </div>

                                        <button className="btn btn-primary" onClick={() => setShowEditOverlay(true)}>Edit Username</button>{" "}
                                        <button className="btn btn-warning" onClick={() => {
                                            setMessage("");
                                            setCurrentPassword("");
                                            setNewPassword("");
                                            setRePassword("");
                                            setShowPasswordOverlay(true);
                                        }}>Change Password</button>{" "}
                                        <button className="btn btn-danger" onClick={() => {
                                            setMessage("");
                                            setCurrentPassword("");
                                            setShowDeactivateOverlay(true);
                                        }}>Deactivate Account</button>

                                        {successMessage && <div className="alert alert-success mt-3">{successMessage}</div>}
                                        {message && <div className="alert alert-danger mt-3">{message}</div>}
                                    </div>
                                </div>
                            ) : (
                                <div className="text-center text-muted fs-5 py-5">No employee data available</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>

            {/* -------------------- Edit Username Overlay -------------------- */}
            {showEditOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white border border-primary rounded-3 shadow-lg p-4" style={{ width: '90%', maxWidth: '500px' }}>
                            <h4 className="text-primary text-center mb-3 fw-bold">Edit Username</h4>
                            <div className="mb-3">
                                <label className="form-label">New Username</label>
                                <input
                                    type="text"
                                    className="form-control"
                                    value={newUsername}
                                    onChange={(e) => setNewUsername(e.target.value)}
                                    placeholder="Enter new username"
                                />
                            </div>
                            {message && (
                                <div className="text-danger mb-3 text-center">
                                    {message}
                                </div>
                            )}
                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => setShowEditOverlay(false)}>Cancel</button>
                                <button className="btn btn-primary" onClick={updateEmployee}>Save</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* -------------------- Change Password Overlay -------------------- */}
            {showPasswordOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white border border-warning rounded-3 shadow-lg p-4" style={{ width: '90%', maxWidth: '500px' }}>
                            <h4 className="text-warning text-center mb-3 fw-bold">Change Password</h4>

                            <div className="mb-2">
                                <label className="form-label">Current Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                    placeholder="Enter current password"
                                />
                            </div>
                            <div className="mb-2">
                                <label className="form-label">New Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    placeholder="Enter new password"
                                />
                            </div>
                            <div className="mb-3">
                                <label className="form-label">Re-enter New Password</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    value={rePassword}
                                    onChange={(e) => setRePassword(e.target.value)}
                                    placeholder="Re-enter new password"
                                />
                            </div>

                            {message && (
                                <div className="text-danger mb-3 text-center">
                                    {message}
                                </div>
                            )}

                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => setShowPasswordOverlay(false)}>Cancel</button>
                                <button className="btn btn-warning" onClick={handleChangePassword}>Change</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/* -------------------- Deactivate Account Overlay -------------------- */}
            {showDeactivateOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white border border-danger rounded-3 shadow-lg p-4" style={{ width: '90%', maxWidth: '500px' }}>
                            <h4 className="text-danger text-center mb-3 fw-bold">Deactivate Account</h4>
                            <p className="text-center mb-3">
                                Are you sure you want to deactivate your user account?
                            </p>

                            <div className="mb-3">
                                <label className="form-label">Enter Password to Confirm</label>
                                <input
                                    type="password"
                                    className="form-control"
                                    value={currentPassword}
                                    onChange={(e) => setCurrentPassword(e.target.value)}
                                    placeholder="Enter your current password"
                                />
                            </div>

                            {message && (
                                <div className="text-danger mb-3 text-center">
                                    {message}
                                </div>
                            )}

                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => setShowDeactivateOverlay(false)}>Cancel</button>
                                <button className="btn btn-danger" onClick={deactivateAccount}>Proceed</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

        </div>
    );
}

export default EmployeeProfile;
