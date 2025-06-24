import { useState } from 'react';
import axios from 'axios';
function AddBranch() {
    const [ifsc, setIfsc] = useState('');
    const [branchName, setBranchName] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [email, setEmail] = useState('');
    const [district, setDistrict] = useState('');
    const [street, setStreet] = useState('');
    const [state, setState] = useState('');

    let [message, setMessage] = useState('');
    let [successMessage, setSuccessMessage] = useState("");

    const handleAddBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const baererAuthString = "Bearer " + token;
            const address = street + ',' + district + ',' + state;
            const response = await axios.post(`http://localhost:9090/api/branch/add`, {

                'ifsc': ifsc,
                'branchName': branchName,
                'contactNumber': contactNumber,
                'email': email,
                'address': address

            },
                { headers: { Authorization: baererAuthString } });



            setSuccessMessage("Branch added successfully!!! ");

            setTimeout(() => {
                setSuccessMessage("");
            }, 3000);
            //reset fields to empty
            setIfsc('');
            setBranchName('');
            setContactNumber('');
            setEmail('');
            setStreet('');
            setDistrict('');
            setState('');

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

    return (
        <div className="container py-4">
             <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">BRANCH</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                       Add Branch
                    </li>
                </ol>
            </nav>
            <div className="card shadow-lg border-0" style={{ backgroundColor: "#f8f9fa" }}>
                {/* Header */}
                <div className="card-header bg-primary text-white fw-semibold fs-5">
                    Add Branch
                </div>

                {/* Body */}
                <div className="card-body">
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Branch Name</label>
                            <input
                                type="text"
                                className="form-control"
                                value={branchName}
                                onChange={(e) => setBranchName(e.target.value)}
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">IFSC</label>
                            <input
                                type="text"
                                className="form-control"
                                value={ifsc}
                                onChange={(e) => setIfsc(e.target.value)}
                            />
                        </div>

                        <div className="col-md-6 mb-3">
                            <label className="form-label">Contact Number</label>
                            <input
                                type="text"
                                className="form-control"
                                value={contactNumber}
                                onChange={(e) => setContactNumber(e.target.value)}
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Street</label>
                            <input
                                type="text"
                                className="form-control"
                                value={street}
                                onChange={(e) => setStreet(e.target.value)}
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">District</label>
                            <input
                                type="text"
                                className="form-control"
                                value={district}
                                onChange={(e) => setDistrict(e.target.value)}
                            />
                        </div>

                        <div className="col-md-6 mb-3">
                            <label className="form-label">State</label>
                            <select
                                className="form-select"
                                value={state}
                                onChange={(e) => setState(e.target.value)}
                            >
                                {[
                                    "Select state", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                                    "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala",
                                    "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
                                    "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
                                    "Uttarakhand", "West Bengal"
                                ].map((s) => (
                                    <option
                                        key={s}
                                        value={s === "Select state" ? "" : s}
                                        disabled={s === "Select state"}
                                    >
                                        {s}
                                    </option>
                                ))}
                            </select>
                        </div>

                    </div>
                </div>

                {/* Footer */}
                <div className="card-footer bg-light d-flex justify-content-end">
                    <button className="btn btn-success px-4" onClick={() => handleAddBranch()}>
                        Add
                    </button>
                </div>
            </div>
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

export default AddBranch;
