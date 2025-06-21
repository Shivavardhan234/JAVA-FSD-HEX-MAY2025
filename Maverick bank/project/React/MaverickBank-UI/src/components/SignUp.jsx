import { useState } from "react";
import "../css/SignUp.css"
import axios from "axios";
import { useNavigate } from "react-router-dom";

function SignUp() {
    let [name, setName] = useState();
    let [dob, setDob] = useState();
    let [gender, setGender] = useState("none");
    let [contactNumber, setContactNumber] = useState();
    let [email, setEmail] = useState();
    let [state, setState] = useState("Select state");
    let [district, setDistrict] = useState();
    let [street, setStreet] = useState();

    let [username, setUsername] = useState();
    let [password, setPassword] = useState();
    let [rePassword, setRepassword] = useState();

    let [msg, setMsg] = useState("");

    const navigate = useNavigate();

    const signUp = async () => {

        if (!name || !dob || !contactNumber || !email || !district || !street || !username || !password || !rePassword) {
            setMsg("All fields are required...!!!");
            return;
        }
        if (gender === "none") {
            setMsg("Choose valid gender...!!!");
            return;
        }
        if (state === "Select state") {
            setMsg("Choose valid state...!!!");
            return;
        }
        if (password !== rePassword) {
            setMsg("Passwords doesn't match...!!!");
            return;
        }

        const address = street + ", " + district + ", " + state;

        try {

            const response = await axios.post("http://localhost:9090/api/customer/signup", {
                'name': name,
                'dob': dob,
                'gender': gender,
                'contactNumber': contactNumber,
                'email': email,
                'address': address,
                'user': {
                    'username': username,
                    'password': password
                }

            });
            //console.log(response.data);


            // //logging the new user in
            // const encryptedCredentials = window.btoa(username + ':' + password)
            // const basicAuthString = "Basic " + encryptedCredentials;
            // const tokenResponse = await axios.get("http://localhost:9090/api/user/token/v1", {
            //     headers: { "Authorization": basicAuthString }
            // });
            // //console.log(tokenResponse.data);
            // //store it in the local storage to use from other components
            // localStorage.setItem('token', tokenResponse.data)
            // //make a baerer string
            // const baererAuthString = "Bearer " + tokenResponse.data;
            // const userDetailResponse = await axios.get("http://localhost:9090/api/user/get/details", {
            //     headers: { "Authorization": baererAuthString }
            // })

            console.log("Customer dashboard");
            //navigate("/customer")
            setMsg("Sign Up success. Now you can log in(^o^)")

        }
        catch (err) {
            console.log(err);
        }

    }



    return (
        <div>
            <nav className="navbar">
                <div className="logo">MAVERICK BANK</div>
            </nav>

            <div className="signup-container">
                <h2 className="signup-heading">Sign Up</h2>
                <div className="form-grid">


                    <div className="left-column">
                        <input type="text" name="name" placeholder="Full Name" className="input-field" onChange={($e) => { setName($e.target.value) }} />
                        <input type="date" name="dob" className="input-field" onChange={($e) => { setDob($e.target.value) }} />
                        <select name="gender" className="input-field" onChange={($e) => { setGender($e.target.value) }}>
                            <option value="none">Select Gender</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="TRANSGENDER">Transgender</option>
                        </select>
                        <input type="text" name="contactNumber" placeholder="Contact Number" className="input-field" onChange={($e) => { setContactNumber($e.target.value) }} />
                        <input type="email" name="email" placeholder="Email" className="input-field" onChange={($e) => { setEmail($e.target.value) }} />
                        <select name="state" className="input-field" onChange={($e) => { setState($e.target.value) }}>
                            {[
                                "Select state", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                                "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
                                "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
                                "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana",
                                "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
                            ].map((state) => (
                                <option key={state} value={state}>{state}</option>
                            ))}
                        </select>
                        <input type="text" name="district" placeholder="District" className="input-field" onChange={($e) => { setDistrict($e.target.value) }} />
                        <input type="text" name="address" placeholder="House No. & Street" className="input-field" onChange={($e) => { setStreet($e.target.value) }} />
                    </div>



                    <div className="right-column">
                        <input type="text" name="username" placeholder="Username" className="input-field" onChange={($e) => { setUsername($e.target.value) }} />
                        <input type="password" name="password" placeholder="Password" className="input-field" onChange={($e) => { setPassword($e.target.value) }} />
                        <input type="password" name="rePassword" placeholder="Re-enter Password" className="input-field" onChange={($e) => { setRepassword($e.target.value) }} />
                        {msg !== "" ? <div className="alert alert-info">{msg}</div> : ""}
                    </div>

                </div>

                <button className="btn signup signup-btn" onClick={() => signUp()}>Sign Up</button>

                <p className="signin-text">
                    Already have an account?{" "}
                    <span className="signin-link" onClick={() => navigate("/signIn")}>
                        Sign In here
                    </span>
                </p>
            </div>
        </div>
    )
}

export default SignUp;