import { FaUserCircle } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getUserDetails, logOutAction } from "../../store/actions/UserAction";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function Profile() {
    //-------------------------------------------------------- DECLARING STATES --------------------------------------------------------------
    let [username, setUsername] = useState("");
    let [newUsername, setNewUsername] = useState("")
    let [fullName, setFullName] = useState("");
    let [dob, setDob] = useState("");
    let [gender, setGender] = useState("");
    let [contact, setContact] = useState("");
    let [email, setEmail] = useState("");
    let [address, setAddress] = useState("");
    let [message, setMessage] = useState("");
    let [activeStatus, setActiveStatus] = useState("");
    let [successMessage, setSuccessMessage] = useState("")
    const dispatch = useDispatch();
    const navigate = useNavigate();

    //these are for password reset
    let [currentPassword, setCurrentPassword] = useState("");
    let [newPassword, setNewPassword] = useState("");
    let [rePassword, setRePassword] = useState("");

    //overlays, makes screen blur and inactive
    const [showPasswordOverlay, setShowPasswordOverlay] = useState(false);
    const [showEditOverlay, setShowEditOverlay] = useState(false);
    const [showDeactivateOverlay, setShowDeactivateOverlay] = useState(false);


    //--------------------------------------------------------- GET CIO DETAILS FROM REDUX STORE --------------------------------------------
    let cio = useSelector(state => state.user.userDetails);

    //------ if refreshed get the details again --------------------------------
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            getUserDetails(dispatch)(); // dispatch again on refresh
        } else {
            navigate("/"); // or redirect to login if no token
        }
    }, []);

    //--------------------------------------------------------- IF CIO IS NOT NULL DISPLAY THE VALUES ----------------------------------------
    useEffect(() => {
        if (cio && cio.user) {
            setUsername(cio.user.username || "");
            setNewUsername(cio.user.username || "");
            setFullName(cio.name || "");
            setDob(cio.dob || "");
            setGender(cio.gender || "");
            setContact(cio.contactNumber || "");
            setEmail(cio.email || "");
            setAddress(cio.address || "");
            setActiveStatus(cio.user.status || "");
        }

    }, [cio])


    const getStatusClass = (status) => {
        switch (status?.toUpperCase()) {
            case "ACTIVE":
                return "text-success"; // green
            case "INACTIVE":
                return "text-primary"; // blue
            case "SUSPENDED":
                return "text-warning"; // yellow
            case "CLOSED":
                return "text-danger"; // red
            default:
                return "text-secondary"; // fallback
        }
    };

    //----------------------------------------------------- CHANGE PASSWORD ------------------------------------------------------------------
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
            const response = await axios.put(
                `http://localhost:9090/api/user/update/password/${currentPassword}/${newPassword}`,
                {},
                { headers: { Authorization: baererAuthString } }
            );


            setShowPasswordOverlay(false);
            localStorage.removeItem('password');
            localStorage.setItem('password', newPassword);


            setCurrentPassword(newPassword);

            //----------------------- again creating a new token with new credentials -------------------------------
            const encryptedCredentials = window.btoa(username + ':' + newPassword);

            const basicAuthString = "Basic " + encryptedCredentials;
            const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                headers: { "Authorization": basicAuthString }
            });
            localStorage.removeItem('token');
            localStorage.setItem('token', tokenResponse.data);

            getUserDetails(dispatch)();
            setSuccessMessage("Password changed successfully ");
            setMessage("");
            // Auto-clear the message after 3 seconds
            setTimeout(() => {
                setSuccessMessage("");
            }, 3000);

        } catch (err) {
            console.log(err);
            // Axios errors have a response object
            if (err.response && err.response.data) {
                const errorData = err.response.data;

                // Since you return a map like {"Some Exception": "message"}
                // extract the first value
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };
    //------------------------------------------------------ UPDATE CIO ----------------------------------------------------------------------
    const updateCio = async () => {
        //step 1- validate the data
        if (
            username.trim() === "" ||
            newUsername.trim() === "" ||
            fullName.trim() === "" ||
            dob.trim() === "" ||
            gender.trim() === "" ||
            contact.trim() === "" ||
            email.trim() === "" ||
            address.trim() === ""
        ) {
            setMessage("All fields are required.");
            return;
        }
        //get the token
        const token = localStorage.getItem('token');
        const baererAuthString = "Bearer " + token;

        try {
            //if username is changed update username
            if (username !== newUsername) {
                const usernameResponse = await axios.put(`http://localhost:9090/api/user/update/username/${newUsername}`,
                    {},
                    { headers: { Authorization: baererAuthString } }
                );
                const password = localStorage.getItem('password')
                //After updation of username token will be changed so generate new token
                const encryptedCredentials = window.btoa(newUsername + ':' + password);

                const basicAuthString = "Basic " + encryptedCredentials;
                const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                    headers: { "Authorization": basicAuthString }
                });
                //store token in local storage for future uses
                localStorage.removeItem('token');
                localStorage.setItem('token', tokenResponse.data);
                //update the details



            }
            const updatedToken = localStorage.getItem('token');
            const baererAuthStringUpdated = "Bearer " + updatedToken;
            //update cio personal profile
            const cioUpdateResponse = await axios.put(`http://localhost:9090/api/cio/update`,
                {
                    'name': fullName,
                    'dob': dob,
                    'gender': gender,
                    'contactNumber': contact,
                    'email': email,
                    'address': address
                },
                { headers: { Authorization: baererAuthStringUpdated } }
            );

            //update the details
            getUserDetails(dispatch)();


            setShowEditOverlay(false);
            // If everything is filled, clear the message and proceed
            setSuccessMessage("Profile updated successfully!!! ");
            setMessage("");
            // Auto-clear the message after 3 seconds
            setTimeout(() => {
                setSuccessMessage("");
            }, 3000);

        } catch (err) {
            console.log(err);
            // Axios errors have a response object
            if (err.response && err.response.data) {
                const errorData = err.response.data;

                // Since you return a map like {"Some Exception": "message"}
                // extract the first value
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }

    }

    //-------------------------------------------------- DEACTIVATE ACCOUNT -----------------------------------------------------------------

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
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };



    //makes background in the blur not responsive
    useEffect(() => {
        if (showEditOverlay || showDeactivateOverlay || showPasswordOverlay) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'auto';
        }
    }, [showEditOverlay, showDeactivateOverlay, showPasswordOverlay]);



    //---------------------------------------------------------- UI --------------------------------------------------------------------------
    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card shadow-sm rounded-3 bg-light">
                        <div className="card-body d-flex">
                            {/* Left: User Icon */}
                            <div className="d-flex align-items-center justify-content-center me-4" style={{ minWidth: '200px' }}>
                                <FaUserCircle size={160} className="text-secondary" />
                            </div>

                            {/* Right: Details */}
                            <div className="w-100">
                                {/* Ash background heading */}
                                <div className="bg-secondary text-white rounded p-2 mb-3">
                                    <h5 className="mb-0">Profile Information</h5>
                                </div>

                                <div className="row">
                                    <div className="col-md-6 mb-2"><strong>Username:</strong> {username}</div>
                                    <div className="col-md-6 mb-2"><strong>Contact Number:</strong> {contact}</div>
                                    <div className="col-md-6 mb-2"><strong>Full Name:</strong> {fullName}</div>
                                    <div className="col-md-6 mb-2"><strong>Email:</strong> {email}</div>
                                    <div className="col-md-6 mb-2"><strong>Gender:</strong> {gender}</div>
                                    <div className="col-md-6 mb-2"><strong >Account Status:</strong> <span className={getStatusClass(activeStatus)}>{activeStatus}</span></div>
                                    <div className="col-md-6 mb-2"><strong>Date of Birth:</strong> {dob}</div>
                                    <div className="col-md-12 mb-2"><strong>Address:</strong> {address}</div>
                                </div>

                                {/* Buttons */}
                                <div className="d-flex gap-3">
                                    <button className="btn btn-primary" onClick={() => setShowEditOverlay(true)}>Edit Details</button>
                                    <button className="btn btn-warning" onClick={() => setShowPasswordOverlay(true)} >Change Password</button>
                                    <button className="btn btn-danger" onClick={() => setShowDeactivateOverlay(true)}>Deactivate Account</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {/* -------------------------------------------------------------- OVERLAYS ------------------------------------------------ */}
            {showEditOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}
                >
                    <div
                        className="d-flex justify-content-center align-items-center h-100"
                    >
                        <div className="bg-white p-4 rounded shadow-lg" style={{ width: '90%', maxWidth: '600px' }}>
                            <div className="bg-secondary text-white rounded p-2 mb-3">
                                <h5 className="mb-0">Edit Profile</h5>
                            </div>
                            <div className="row">
                                {/* Left Column */}
                                <div className="col-md-6">
                                    <div className="mb-3">
                                        <label className="form-label">Username</label>
                                        <input type="text" className="form-control" value={newUsername} onChange={(e) => { setNewUsername(e.target.value) }} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Full Name</label>
                                        <input type="text" className="form-control" value={fullName} onChange={(e) => setFullName(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Gender</label>
                                        <select className="form-select" value={gender} onChange={(e) => setGender(e.target.value)}>
                                            <option value="">Select</option>
                                            <option value="MALE">Male</option>
                                            <option value="FEMALE">Female</option>
                                            <option value="TRANSGENDER">Transgender</option>
                                        </select>
                                    </div>
                                </div>

                                {/* Right Column */}
                                <div className="col-md-6">
                                    <div className="mb-3">
                                        <label className="form-label">Date of Birth</label>
                                        <input type="date" className="form-control" value={dob} onChange={(e) => setDob(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Contact Number</label>
                                        <input type="text" className="form-control" value={contact} onChange={(e) => setContact(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Email</label>
                                        <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                                    </div>
                                </div>

                                {/* Full width address */}
                                <div className="col-12">
                                    <div className="mb-3">
                                        <label className="form-label">Address</label>
                                        <input type="text" className="form-control" value={address} onChange={(e) => setAddress(e.target.value)} />
                                    </div>
                                </div>
                                {message && (
                                    <div className="text-danger mt-2">
                                        {message}
                                    </div>
                                )}

                            </div>

                            {/* Buttons */}
                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => setShowEditOverlay(false)}>Cancel</button>
                                <button className="btn btn-success" onClick={() => updateCio()}>Save</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            {/*--------------------------------------------------------- DEACTIVATE OVERLAY ---------------------------------------------- */}
            {showDeactivateOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}>
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white border border-danger rounded-3 shadow-lg p-4" style={{ width: '90%', maxWidth: '500px' }}>
                            <h4 className="text-danger text-center mb-3 fw-bold">Deactivate Account</h4>
                            <p className="text-center mb-3">
                                Are you sure you want to deactivate your user account?
                            </p>

                            {/* Password input field */}
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

                            {/* Error message (if any) */}
                            {message && (
                                <div className="text-danger mb-3 text-center">
                                    {message}
                                </div>
                            )}

                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => setShowDeactivateOverlay(false)}>Cancel</button>
                                <button className="btn btn-danger" onClick={() => deactivateAccount()}>Proceed</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            {/*------------------------------------------------------- CHANGE PASSWORD OVERLAY ------------------------------------------ */}
            {showPasswordOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{ top: '56px', height: 'calc(100vh - 56px)', backdropFilter: 'blur(4px)', zIndex: '1050' }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white rounded-3 shadow-lg" style={{ width: '90%', maxWidth: '500px' }}>

                            {/* Header */}
                            <div className="bg-light border-bottom px-4 py-3 rounded-top">
                                <h5 className="mb-0 fw-bold">Change Password</h5>
                            </div>

                            {/* Body */}
                            <div className="p-4">
                                <div className="mb-3">
                                    <label className="form-label">Enter Current Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={currentPassword}
                                        onChange={(e) => setCurrentPassword(e.target.value)}
                                    />
                                </div>
                                <div className="mb-3">
                                    <label className="form-label">Enter New Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={newPassword}
                                        onChange={(e) => setNewPassword(e.target.value)}
                                    />
                                </div>
                                <div className="mb-4">
                                    <label className="form-label">Re-enter New Password</label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        value={rePassword}
                                        onChange={(e) => setRePassword(e.target.value)}
                                    />
                                    {message && (
                                        <div className="text-danger mt-2">
                                            {message}
                                        </div>
                                    )}

                                </div>

                                {/* Buttons */}
                                <div className="d-flex justify-content-end gap-3">
                                    <button
                                        className="btn btn-secondary"
                                        onClick={() => setShowPasswordOverlay(false)}
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        className="btn btn-primary"
                                        onClick={() => handleChangePassword()}
                                    >
                                        Change Password
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
            {/* --------------------------------------Success message alert ---------------------------------- */}
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
export default Profile;