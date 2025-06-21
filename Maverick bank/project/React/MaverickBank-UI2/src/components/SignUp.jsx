import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import axios from "axios";

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
        console.log("Sign up initiated")

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
            

            console.log("Customer dashboard");
            setMsg("Sign Up success. Now you can log in(^o^)")

        }
        catch (err) {
             console.error(err);
            if (err.response && err.response.data) {
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        
        
        }

    }

       

    





    return (
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "80vh" }}>
            <div className="card shadow p-4" style={{ width: "100%", maxWidth: "900px" }}>
                {/* Header */}
                <h3 className="text-center mb-4">Sign Up</h3>

                <div className="row">
                    {/* Left Column */}
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Full Name</label>
                            <input type="text" className="form-control" placeholder="Enter your name" onChange={($e)=>{setName($e.target.value)}}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Date of Birth</label>
                            <input type="date" className="form-control" onChange={($e)=>{setDob($e.target.value)}}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Gender</label>
                            <select className="form-select" onChange={($e)=>{setGender($e.target.value)}}>
                                <option value="none">Select Gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="TRANSGENDER">Transgender</option>
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Contact Number</label>
                            <input type="text" className="form-control" placeholder="Enter contact number" onChange={($e)=>{setContactNumber($e.target.value)}}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" className="form-control" placeholder="Enter email" onChange={($e)=>{setEmail($e.target.value)}}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">State</label>
                            <select className="form-select" onChange={($e)=>{setState($e.target.value)}}>
                                {[
                                    "Select state", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                                    "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala",
                                    "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
                                    "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
                                    "Uttarakhand", "West Bengal"
                                ].map((state) => (
                                    <option key={state} value={state}>{state}</option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">District</label>
                            <input type="text" className="form-control" placeholder="Enter district"onChange={($e)=>{setDistrict($e.target.value)}} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">House No. & Street</label>
                            <input type="text" className="form-control" placeholder="Enter address" onChange={($e)=>{setStreet($e.target.value)}}/>
                        </div>
                    </div>

                    {/* Right Column */}
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Username</label>
                            <input type="text" className="form-control" placeholder="Choose a username" onChange={($e)=>{setUsername($e.target.value)}}/>
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Password</label>
                            <input type="password" className="form-control" placeholder="Enter password" onChange={($e)=>{setPassword($e.target.value)}} />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Re-enter Password</label>
                            <input type="password" className="form-control" placeholder="Re-enter password" onChange={($e)=>{setRepassword($e.target.value)}}/>
                        </div>

                        {msg && (
                            <div className="alert alert-info">{msg}</div>
                        )}
                    </div>
                </div>

                {/* Sign Up Button */}
                <div className="d-grid gap-2 mt-3">
                    <button className="btn btn-primary" onClick={() => {signUp()}}>Sign Up</button>
                </div>

                {/* Footer */}
                <div className="text-center mt-3">
                    <small>
                        Already have an account?{" "}
                        <Link to="/signin" className="text-primary">Sign In here</Link>
                    </small>
                </div>
            </div>
        </div>
    );
}

export default SignUp;