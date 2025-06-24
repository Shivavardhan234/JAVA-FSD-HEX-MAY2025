
import { FaUserCircle, FaSearch } from "react-icons/fa";
import { useEffect, useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { getUserDetails } from "../../../store/actions/UserAction";

function FindCio() {
    const [searchType, setSearchType] = useState("by-id");
    const [searchValue, setSearchValue] = useState("");
    const [cio, setCio] = useState(null);
    const [message, setMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const getStatusClass = (status) => {
        switch (status?.toUpperCase()) {
            case "ACTIVE": return "text-success";
            case "INACTIVE": return "text-primary";
            case "SUSPENDED": return "text-warning";
            case "DELETED": return "text-danger";
            default: return "text-secondary";
        }
    };


    useEffect(() => { getUserDetails(dispatch)(); }, []);

    const currentCio = useSelector(state => state.user.userDetails);


    const fetchCio = async () => {
        try {
            setMessage("");
            const token = localStorage.getItem('token');
            const baererAuthString = "Bearer " + token;

            let url = "";
            if (searchType === "by-id") {
                url = `http://localhost:9090/api/cio/get/by-id/${searchValue}`;
            } else {
                url = `http://localhost:9090/api/cio/get/by-user-id/${searchValue}`;
            }


            const res = await axios.get(url, { headers: { "Authorization": baererAuthString } });

            setCio(res.data);


        } catch (err) {
            console.error(err);
            if (err.response?.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
            setCio(null);
        }
    };

    useEffect(() => {
        if (cio && currentCio && cio.id === currentCio.id) {
            navigate('/cio/profile')
        }
    }, [cio, currentCio])

    const updateStatus = async (newStatus) => {
        try {
            const token = localStorage.getItem("token");
            const headers = { Authorization: "Bearer " + token };

            await axios.put(
                `http://localhost:9090/api/user/update/status/${cio.user.id}/${newStatus}`,
                {},
                { headers }
            );
            setSuccessMessage(`Cio ${newStatus.toLowerCase()}d successfully`);
            setTimeout(() => setSuccessMessage(""), 3000);
            fetchCustomer();
        } catch (err) {
            console.error(err);
            if (err.response?.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    return (
        <div className="container mt-4">
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">CIO</span>
                    </li>

                    <li className="breadcrumb-item active" aria-current="page">
                        Find CIO
                    </li>
                </ol>
            </nav>
            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card border border-dark shadow rounded-3 bg-white">

                        {/* Header */}
                        <div className="card-header bg-secondary bg-opacity-10 fw-semibold"
                            style={{ borderTopLeftRadius: '10px', borderTopRightRadius: '10px' }}>
                            <div className="d-flex justify-content-between align-items-center flex-wrap gap-3">
                                {/* Dropdown on left */}
                                <div className="input-group w-auto">
                                    <select className="form-select" value={searchType} onChange={(e) => setSearchType(e.target.value)}>
                                        <option value="by-id">Search by ID</option>
                                        <option value="by-user-id">Search by User ID</option>
                                    </select>
                                </div>

                                {/* Search input on right */}
                                <div className="input-group" style={{ maxWidth: '300px' }}>
                                    <input type="number" className="form-control" placeholder="Enter ID" value={searchValue} onChange={(e) => setSearchValue(e.target.value)} />
                                    <button className="btn btn-outline-secondary" onClick={() => fetchCio()}>
                                        <i className="bi bi-search"></i>
                                    </button>
                                </div>
                            </div>
                        </div>

                        {/* Body */}
                        <div className="card-body">
                            {cio ? (
                                <div className="d-flex">
                                    {/* Left: Icon */}
                                    <div className="d-flex align-items-center justify-content-center me-4" style={{ minWidth: '200px' }}>
                                        <FaUserCircle size={160} className="text-secondary" />
                                    </div>

                                    {/* Right: Details */}
                                    <div className="w-100">
                                        <div className="bg-secondary text-white rounded p-2 mb-3">
                                            <h5 className="mb-0">CIO Information</h5>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-6 mb-2"><strong>Username:</strong> {cio.user.username}</div>
                                            <div className="col-md-6 mb-2"><strong>Contact Number:</strong> {cio.contactNumber}</div>
                                            <div className="col-md-6 mb-2"><strong>Full Name:</strong> {cio.name}</div>
                                            <div className="col-md-6 mb-2"><strong>Email:</strong> {cio.email}</div>
                                            <div className="col-md-6 mb-2"><strong>Gender:</strong> {cio.gender}</div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Account Status:</strong>{" "}
                                                <span className={getStatusClass(cio.user.status)}>{cio.user.status}</span>
                                            </div>
                                            <div className="col-md-6 mb-2"><strong>Date of Birth:</strong> {cio.dob}</div>
                                            <div className="col-md-12 mb-2"><strong>Address:</strong> {cio.address}</div>
                                        </div>

                                        {/* Action Buttons */}
                                        {cio.user.status !== "DELETED" && (
                                            <div className="d-flex gap-3 mt-3">
                                                {cio.user.status === "SUSPENDED" && (
                                                    <button className="btn btn-success" onClick={() => updateStatus("ACTIVE")}>Activate Account</button>
                                                )}
                                                {(cio.user.status === "ACTIVE" || cio.user.status === "INACTIVE") && (
                                                    <button className="btn btn-warning" onClick={() => updateStatus("SUSPENDED")}>Suspend Account</button>
                                                )}
                                                <button className="btn btn-danger" onClick={() => updateStatus("DELETED")}>Delete Account</button>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ) : (
                                <div className="text-center text-muted fs-5 py-5">No profile found</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>

    );
}

export default FindCio;
