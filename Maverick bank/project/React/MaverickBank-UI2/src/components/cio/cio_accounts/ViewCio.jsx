import { FaUserCircle } from "react-icons/fa";
import {  Link, useNavigate } from "react-router-dom";
import {  useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getCio } from "../../../store/actions/CIOAction";

function ViewCio() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");


    const cio=useSelector(state=>state.cioStore.cio);


    const getStatusClass = (status) => {
        switch (status?.toUpperCase()) {
            case "ACTIVE": return "text-success";
            case "INACTIVE": return "text-primary";
            case "SUSPENDED": return "text-warning";
            case "DELETED": return "text-danger";
            default: return "text-secondary";
        }
    };



    const updateStatus = async (newStatus) => {
        try {
            const token = localStorage.getItem("token");
            const headers = { Authorization: "Bearer " + token };

            await axios.put(
                `http://localhost:9090/api/user/update/status/${cio.user.id}/${newStatus}`,
                {},
                { headers }
            );

            setSuccessMessage(`CIO ${newStatus.toLowerCase()}d successfully`);
            setTimeout(() => setSuccessMessage(""), 3000);

            getCio(dispatch)(cio.id);
        } catch (err) {
            console.error(err);
            if (err.response?.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setErrorMessage(err.response.data[firstKey]);
            } else {
                setErrorMessage("Something went wrong. Try again.");
            }
        }
    };

    return (
        <div className="container mt-4">
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <Link to="../category" className="text-decoration-none">
                            CIO's By Category
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        CIO Profile
                    </li>
                </ol>
            </nav>

            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card border border-dark shadow rounded-3 bg-white">
                        <div className="card-header bg-secondary bg-opacity-10 fw-semibold">
                            CIO Profile
                        </div>

                        <div className="card-body">
                            {cio ? (
                                <div className="d-flex">
                                    <div className="d-flex align-items-center justify-content-center me-4" style={{ minWidth: "200px" }}>
                                        <FaUserCircle size={160} className="text-secondary" />
                                    </div>

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

                                        {successMessage && (
                                            <div className="alert alert-success mt-3" role="alert">
                                                {successMessage}
                                            </div>
                                        )}
                                        {errorMessage && (
                                            <div className="alert alert-danger mt-3" role="alert">
                                                {errorMessage}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ) : (
                                <div className="text-center text-muted fs-5 py-5">No CIO data available</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ViewCio;
