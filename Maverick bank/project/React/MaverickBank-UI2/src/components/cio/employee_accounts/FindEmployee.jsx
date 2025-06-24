import { FaUserCircle } from "react-icons/fa";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function FindEmployee() {
    const [searchType, setSearchType] = useState("by-id");
    const [searchValue, setSearchValue] = useState("");
    const [employee, setEmployee] = useState(null);
    const [message, setMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const navigate = useNavigate();

    const getStatusClass = (status) => {
        switch ((status || "").toUpperCase()) {
            case "ACTIVE": return "text-success";
            case "INACTIVE": return "text-primary";
            case "SUSPENDED": return "text-warning";
            case "DELETED": return "text-danger";
            default: return "text-secondary";
        }
    };

    const fetchEmployee = async () => {
        try {
            setMessage("");
            const token = localStorage.getItem("token");
            const bearer = "Bearer " + token;

            const url =
                searchType === "by-id"
                    ? `http://localhost:9090/api/employee/get/by-id/${searchValue}`
                    : `http://localhost:9090/api/employee/get/by-user-id/${searchValue}`;

            const res = await axios.get(url, { headers: { Authorization: bearer } });
            setEmployee(res.data);
        } catch (err) {
            console.error(err);
            if (err.response?.data) {
                const firstKey = Object.keys(err.response.data)[0];
                setMessage(err.response.data[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
            setEmployee(null);
        }
    };

    const updateStatus = async (newStatus) => {
        try {
            const token = localStorage.getItem("token");
            await axios.put(
                `http://localhost:9090/api/user/update/status/${employee.user.id}/${newStatus}`,
                {},
                { headers: { Authorization: "Bearer " + token } }
            );
            setSuccessMessage(`Employee ${newStatus.toLowerCase()}d successfully`);
            setTimeout(() => setSuccessMessage(""), 3000);
            fetchEmployee();
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
                        <span className="text-decoration-none text-muted">Employee Accounts</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                       Find Employee
                    </li>
                </ol>
            </nav>
            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card border border-dark shadow rounded-3 bg-white">
                        {/* Header */}
                        <div
                            className="card-header bg-secondary bg-opacity-10 fw-semibold"
                            style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}
                        >
                            <div className="d-flex justify-content-between align-items-center flex-wrap gap-3">
                                {/* Dropdown */}
                                <div className="input-group w-auto">
                                    <select
                                        className="form-select"
                                        value={searchType}
                                        onChange={(e) => setSearchType(e.target.value)}
                                    >
                                        <option value="by-id">Search by ID</option>
                                        <option value="by-user-id">Search by User ID</option>
                                    </select>
                                </div>

                                {/* Search box */}
                                <div className="input-group" style={{ maxWidth: "300px" }}>
                                    <input
                                        type="number"
                                        className="form-control"
                                        placeholder="Enter ID"
                                        value={searchValue}
                                        onChange={(e) => setSearchValue(e.target.value)}
                                    />
                                    <button className="btn btn-outline-secondary" onClick={fetchEmployee}>
                                        <i className="bi bi-search"></i>
                                    </button>
                                </div>
                            </div>
                        </div>

                        {/* Body */}
                        <div className="card-body">
                            {employee ? (
                                <div className="d-flex">
                                    {/* Icon */}
                                    <div
                                        className="d-flex align-items-center justify-content-center me-4"
                                        style={{ minWidth: "200px" }}
                                    >
                                        <FaUserCircle size={160} className="text-secondary" />
                                    </div>

                                    {/* Details */}
                                    <div className="w-100">
                                        <div className="bg-secondary text-white rounded p-2 mb-3">
                                            <h5 className="mb-0">Employee Information</h5>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-6 mb-2">
                                                <strong>Username:</strong> {employee.user.username}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Contact Number:</strong> {employee.contactNumber}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Full Name:</strong> {employee.name}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Email:</strong> {employee.email}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Gender:</strong> {employee.gender}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Account Status:</strong>{" "}
                                                <span className={getStatusClass(employee.user.status)}>
                                                    {employee.user.status}
                                                </span>
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Date of Birth:</strong> {employee.dob}
                                            </div>
                                            {/* Extra fields */}
                                            <div className="col-md-6 mb-2">
                                                <strong>Designation:</strong> {employee.designation}
                                            </div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Branch Name:</strong>{" "}
                                                {employee.branch?.branchName ?? "—"}
                                            </div>
                                            <div className="col-md-12 mb-2">
                                                <strong>Address:</strong> {employee.address}
                                            </div>
                                        </div>

                                        {/* Action Buttons */}
                                        {employee.user.status !== "DELETED" && (
                                            <div className="d-flex gap-3 mt-3 flex-wrap">
                                                {employee.user.status === "SUSPENDED" && (
                                                    <button
                                                        className="btn btn-success"
                                                        onClick={() => updateStatus("ACTIVE")}
                                                    >
                                                        Activate Account
                                                    </button>
                                                )}
                                                {(employee.user.status === "ACTIVE" ||
                                                    employee.user.status === "INACTIVE") && (
                                                        <button
                                                            className="btn btn-warning"
                                                            onClick={() => updateStatus("SUSPENDED")}
                                                        >
                                                            Suspend Account
                                                        </button>
                                                    )}
                                                <button
                                                    className="btn btn-danger"
                                                    onClick={() => updateStatus("DELETED")}
                                                >
                                                    Delete Account
                                                </button>

                                                {/* View branch button */}
                                                {employee.branch && (
                                                    <button
                                                        className="btn btn-outline-primary"
                                                        onClick={() =>
                                                            navigate("/cio/cioBranch/branchDetails", {
                                                                state: { branch: employee.branch }
                                                            })
                                                        }
                                                    >
                                                        View Branch
                                                    </button>
                                                )}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ) : (
                                <div className="text-center text-muted fs-5 py-5">
                                    No profile found
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default FindEmployee;
