import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getUserDetails, logOutAction } from "../store/actions/UserAction";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";

function SignIn() {
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");
    let [message, setMessage] = useState("");
    let [successMessage,setSuccessMessage]= useState("");
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const userDet = useSelector(state => state.user.userDetails);
    //overlay
    const [showInactiveOverlay, setShowInactiveOverlay] = useState(false);






    const logIn = async () => {
        const encryptedCredentials = window.btoa(username + ':' + password);
        try {
            const basicAuthString = "Basic " + encryptedCredentials;
            const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                headers: { "Authorization": basicAuthString }
            });
            localStorage.setItem('token', tokenResponse.data);

            await getUserDetails(dispatch)();
            console.log(userDet);

            localStorage.setItem('password', password);
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

    const activateAccount = async () => {
        const token = localStorage.getItem('token');
        const baererAuthString = "Bearer " + token;


        await axios.put(
            `http://localhost:9090/api/user/update/activate`,
            {},
            { headers: { Authorization: baererAuthString } }
        );
        await getUserDetails(dispatch)();
        setShowInactiveOverlay(false);
        setSuccessMessage("Account activation successfull..!!! ");
            setMessage("");
            // Auto-clear the message after 3 seconds
            setTimeout(() => {
                setSuccessMessage("");
            }, 3000);
    }

    const cancelActivateAccount = () => {
        logOutAction(dispatch)();
        localStorage.clear();
        setShowInactiveOverlay(false);

    }




    useEffect(() => {
        if (userDet && userDet.user && userDet.user.role) {
            if (userDet.user.status === "DELETED") {
                setMessage("Invaid credentials..!!!");
                logOutAction(dispatch)();
                localStorage.clear();
            }
            if (userDet.user.status === "SUSPENDED") {
                setMessage("Your user account has been suspended. Please try contacting administrators..!!!");
                logOutAction(dispatch)();
                localStorage.clear();
            }
            if (userDet.user.status === "INACTIVE") {
                setShowInactiveOverlay(true);
            }
            if (userDet.user.status === "ACTIVE") {
                switch (userDet.user.role) {
                    case "CUSTOMER":
                        console.log("Customer dashboard");
                        navigate("/customer")
                        break;
                    case "ADMIN":
                        console.log("CIO dashboard");
                        navigate("/cio")
                        break;
                    case "LOAN_OFFICER":
                        console.log("LOAN_OFFICER dashboard");
                        break;
                    case "ACCOUNT_MANAGER":
                        console.log("ACCOUNT_MANAGER dashboard");
                        break;
                    case "REPORT_MANAGER":
                        console.log("REPORT_MANAGER dashboard");
                        break;
                    case "TRANSACTION_ANALYST":
                        console.log("TRANSACTION_ANALYST dashboard");
                        break;
                    case "JUNIOR_OPERATIONS_MANAGER":
                        console.log("JUNIOR_OPERATIONS_MANAGER dashboard");
                        break;
                    case "SENIOR_OPERATIONS_MANAGER":
                        console.log("SENIOR_OPERATIONS_MANAGER dashboard");
                        break;
                    default:
                        console.log("Improper role")
                        break;

                }
            }
        }
    }, [userDet]);

    //Making screen unresponsive when there is a overlay
    useEffect(() => {
        if (showInactiveOverlay) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'auto';
        }


    }, [showInactiveOverlay]);


    return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: '80vh' }}>
            <div className="card shadow p-4" style={{ width: '100%', maxWidth: '400px' }}>
                {/* Header */}
                <h3 className="text-center mb-4">Sign In</h3>

                {/* Username */}
                <div className="mb-3">
                    <label className="form-label">Username</label>
                    <div className="input-group">
                        <span className="input-group-text">
                            <i className="bi bi-person-fill"></i>
                        </span>
                        <input type="text" className="form-control" placeholder="Enter username" onChange={($e) => { setUsername($e.target.value) }} />
                    </div>
                </div>

                {/* Password */}
                <div className="mb-3">
                    <label className="form-label">Password</label>
                    <div className="input-group">
                        <span className="input-group-text">
                            <i className="bi bi-lock-fill"></i>
                        </span>
                        <input type="password" className="form-control" placeholder="Enter password" onChange={($e) => { setPassword($e.target.value) }} />
                    </div>
                </div>
                <div>
                    {message && (
                            <div className="alert alert-danger">{message}</div>
                        )}
                </div>

                {/* Sign In Button */}
                <div className="d-grid mb-3">
                    <button className="btn btn-primary" onClick={() => logIn()}>Sign In</button>
                </div>

                {/* Footer */}
                <div className="text-center">
                    <small>
                        Don't have an account?{' '}
                        <Link to="/signup" className="text-primary">
                            Sign Up here
                        </Link>
                    </small>
                </div>
            </div>
            {/*-------------------------------------------------- OVERLAY------------------------------------------------------------- */}
            {showInactiveOverlay && (
                <div
                    className="position-fixed start-0 w-100 bg-dark bg-opacity-50"
                    style={{
                        top: '56px',
                        height: 'calc(100vh - 56px)',
                        backdropFilter: 'blur(4px)',
                        zIndex: 1050
                    }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div className="bg-white border rounded-3 shadow-lg p-4" style={{ width: '90%', maxWidth: '500px' }}>
                            <h4 className="text-danger text-center mb-3 fw-bold">Account Inactive</h4>
                            <p className="text-center mb-4">
                                Your account is inactive. Do you want to activate your account?
                            </p>

                            <div className="d-flex justify-content-end gap-3">
                                <button className="btn btn-secondary" onClick={() => {cancelActivateAccount()}}>Back</button>
                                <button className="btn btn-success" onClick={() => {activateAccount()
                                    
                                }}>Activate</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
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

export default SignIn;