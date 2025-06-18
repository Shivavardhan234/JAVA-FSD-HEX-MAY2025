import axios from "axios";
import "../css/SignIn.css"
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function SignIn() {
    let [username, setUsername] = useState();
    let [password, setPassword] = useState();

    const navigate = useNavigate();



    const logIn = async () => {
        const encryptedCredentials = window.btoa(username + ':' + password)
        try {
            const basicAuthString = "Basic " + encryptedCredentials;
            const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
                headers: { "Authorization": basicAuthString }
            });
            //console.log(tokenResponse.data);
            //store it in the local storage to use from other components
            localStorage.setItem('token', tokenResponse.data)
            //make a baerer string
            const baererAuthString = "Bearer " + tokenResponse.data;
            const userDetailResponse = await axios.get("http://localhost:9090/api/user/get/details", {
                headers: { "Authorization": baererAuthString }
            })

            const role = userDetailResponse.data.user.role;
            switch (role) {
                case "CUSTOMER":
                    console.log("Customer dashboard");
                    navigate("/customer")
                    break;
                case "ADMIN":
                    console.log("CIO dashboard");
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
        catch (err) {
            console.log(err);
            alert("Invalid credentials or server error. Please try again.");
        }

    }





    return (
        <div>

            <nav className="navbar">
                <div className="logo">MAVERICK BANK</div>
            </nav>


            <div className="login-container">
                <h2 className="login-heading">Sign In</h2>
                <input type="text" placeholder="Username" className="input-field" onChange={($e) => { setUsername($e.target.value) }} />
                <input type="password" placeholder="Password" className="input-field" onChange={($e) => { setPassword($e.target.value) }} />
                <button className="btn signup login-btn" onClick={() => logIn()}>Sign In</button>

                <p className="signup-text">
                    Don't have an account?{" "}
                    <span className="signup-link" onClick={() => navigate("/signUp")}>
                        Sign Up here
                    </span>
                </p>
            </div>
        </div>
    );
}

export default SignIn;