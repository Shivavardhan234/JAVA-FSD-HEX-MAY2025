import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function AddEmployee() {
    const [name, setName] = useState("");
    const [dob, setDob] = useState("");
    const [gender, setGender] = useState("none");
    const [contactNumber, setContactNumber] = useState("");
    const [email, setEmail] = useState("");
    const [state, setState] = useState("Select state");
    const [district, setDistrict] = useState("");
    const [street, setStreet] = useState("");
    const [designation, setDesignation] = useState("");
    const [branchId, setBranchId] = useState("");

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [rePassword, setRePassword] = useState("");

    const [msg, setMsg] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const navigate = useNavigate();

    const addEmployee = async () => {
        setMsg("");
        if (!name || !dob || !contactNumber || !email || !district || !street || !username || !password || !rePassword || !designation || !branchId) {
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

        const address = street + ',' + district + ',' + state;

        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            const response = await axios.post(
                "http://localhost:9090/api/employee/add",
                {
                    "name": name,
                    "dob": dob,
                    "gender": gender,
                    "contactNumber": contactNumber,
                    "email": email,
                    "address": address,
                    "designation": designation,
                    "branch": {
                        "id": parseInt(branchId)
                    },
                    "user": {
                        "username": username,
                        "password": password
                    }
                },
                {
                    headers: {
                        Authorization: bearerToken
                    }
                }
            );

            setSuccessMessage("Employee added successfully ..!!");
            setTimeout(() => setSuccessMessage(""), 4000);
            // Clear all fields
            setName(""); setDob(""); setGender("none"); setContactNumber("");
            setEmail(""); setState("Select state"); setDistrict(""); setStreet("");
            setUsername(""); setPassword(""); setRePassword("");
            setDesignation(""); setBranchId("");
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
                        <span className="text-decoration-none text-muted">Employee Accounts</span>
                    </li>

                    <li className="breadcrumb-item active" aria-current="page">
                        Add Employee
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
                    <h5 className="mb-0">ADD EMPLOYEE</h5>
                </div>
                <div className="row">
                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Full Name</label>
                            <input type="text" className="form-control" value={name} onChange={(e) => setName(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Date of Birth</label>
                            <input type="date" className="form-control" value={dob} onChange={(e) => setDob(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Gender</label>
                            <select className="form-select" value={gender} onChange={(e) => setGender(e.target.value)}>
                                <option value="none">Select Gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="TRANSGENDER">Transgender</option>
                            </select>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Contact Number</label>
                            <input type="text" className="form-control" value={contactNumber} onChange={(e) => setContactNumber(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">State</label>
                            <select className="form-select" value={state} onChange={(e) => setState(e.target.value)}>
                                {["Select state", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"].map((s) => (
                                    <option key={s} value={s}>{s}</option>
                                ))}
                            </select>
                        </div>
                        <div className="mb-3">
                            <label className="form-label">District</label>
                            <input type="text" className="form-control" value={district} onChange={(e) => setDistrict(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">House No. & Street</label>
                            <input type="text" className="form-control" value={street} onChange={(e) => setStreet(e.target.value)} />
                        </div>
                    </div>

                    <div className="col-md-6">
                        <div className="mb-3">
                            <label className="form-label">Username</label>
                            <input type="text" className="form-control" value={username} onChange={(e) => setUsername(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Password</label>
                            <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Re-enter Password</label>
                            <input type="password" className="form-control" value={rePassword} onChange={(e) => setRePassword(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Designation</label>
                            <input type="text" className="form-control" value={designation} onChange={(e) => setDesignation(e.target.value)} />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Branch ID</label>
                            <input type="number" className="form-control" value={branchId} onChange={(e) => setBranchId(e.target.value)} />
                        </div>
                    </div>

                    {msg && (
                        <div className="alert alert-warning mt-3">{msg}</div>
                    )}
                </div>

                <div className="d-grid gap-2 mt-3">
                    <button className="btn btn-primary" onClick={addEmployee}>Add Employee</button>
                </div>
            </div>
        </div>
        </>
    );
}

export default AddEmployee;
