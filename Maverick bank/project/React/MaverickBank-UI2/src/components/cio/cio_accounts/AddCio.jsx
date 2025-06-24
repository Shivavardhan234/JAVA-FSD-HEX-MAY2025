import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AddCio() {
    const [name, setName] = useState("");
    const [dob, setDob] = useState("");
    const [gender, setGender] = useState("none");
    const [contactNumber, setContactNumber] = useState("");
    const [email, setEmail] = useState("");
    const [state, setState] = useState("Select state");
    const [district, setDistrict] = useState("");
    const [street, setStreet] = useState("");

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [rePassword, setRePassword] = useState("");

    const [msg, setMsg] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const navigate = useNavigate();

    const addCio = async () => {
        setMsg("");
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

        const address = `${street}, ${district}, ${state}`;

        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            const response = await axios.post(
                "http://localhost:9090/api/cio/add",
                {
                    'name': name,
                    'dob': dob,
                    'gender': gender,
                    'contactNumber': contactNumber,
                    'email': email,
                    'address': address,
                    'user': {
                        'username': username,
                        'password': password
                    },
                },
                {
                    headers: {
                        "Authorization": bearerToken,
                    },
                }
            );

            setSuccessMessage("CIO added successfully ✅");
            setName("");
            setDob("");
            setGender("none");
            setContactNumber("");
            setEmail("");
            setState("Select state");
            setDistrict("");
            setStreet("");
            setUsername("");
            setPassword("");
            setRePassword("");
            setTimeout(() => setSuccessMessage(""), 4000);
        } catch (err) {
            console.error(err);
            if (err.response && err.response.data) {
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMsg(errorData[firstKey]);
            } else {
                setMsg("Something went wrong. Try again.");
            }
        }
    };

    return (
         <>
        <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">CIO</span>
                    </li>

                    <li className="breadcrumb-item active" aria-current="page">
                        Add CIO
                    </li>
                </ol>
            </nav>
        <div className="d-flex justify-content-center align-items-center" style={{ minHeight: "80vh" }}>
            
             
            {successMessage && (
                <div className="d-flex justify-content-center position-fixed top-0 start-0 w-100 mt-3" style={{ zIndex: 9999 }}>
                    <div className="alert alert-success text-center" role="alert">
                        {successMessage}
                    </div>
                </div>
            )}

            <div className="card shadow p-4" style={{ width: "100%", maxWidth: "900px" }}>
                                        <div className="bg-secondary text-white rounded p-2 mb-3">
                                            <h5 className="mb-0">ADD CIO</h5>
                                        </div>
                <div className="row">
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Full Name</label>
                            <input type="text" className="form-control" onChange={(e) => setName(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Date of Birth</label>
                            <input type="date" className="form-control" onChange={(e) => setDob(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Gender</label>
                            <select className="form-select" onChange={(e) => setGender(e.target.value)}>
                                <option value="none">Select Gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="TRANSGENDER">Transgender</option>
                            </select>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Contact Number</label>
                            <input type="text" className="form-control" onChange={(e) => setContactNumber(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" className="form-control" onChange={(e) => setEmail(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">State</label>
                            <select className="form-select" onChange={(e) => setState(e.target.value)}>
                                {["Select state", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"].map((state) => (
                                    <option key={state} value={state}>{state}</option>
                                ))}
                            </select>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">District</label>
                            <input type="text" className="form-control" onChange={(e) => setDistrict(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">House No. & Street</label>
                            <input type="text" className="form-control" onChange={(e) => setStreet(e.target.value)} />
                        </div>
                    </div>

                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Username</label>
                            <input type="text" className="form-control" onChange={(e) => setUsername(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Password</label>
                            <input type="password" className="form-control" onChange={(e) => setPassword(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Re-enter Password</label>
                            <input type="password" className="form-control" onChange={(e) => setRePassword(e.target.value)} />
                        </div>
                        
                    </div>
                    {msg && (
                            <div className="alert alert-warning">{msg}</div>
                        )}
                </div>

                <div className="d-grid gap-2 mt-3">
                    <button className="btn btn-primary" onClick={()=>addCio()}>Add CIO</button>
                </div>
            </div>
        </div>
        </>
    );
}

export default AddCio;
