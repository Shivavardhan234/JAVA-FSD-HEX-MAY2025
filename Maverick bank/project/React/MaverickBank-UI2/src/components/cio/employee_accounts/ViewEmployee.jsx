import { FaUserCircle } from "react-icons/fa";
import {  useNavigate } from "react-router-dom";
import { useState } from "react";
import axios from "axios";
import { useDispatch, useSelector } from "react-redux";
import { getEmployee } from "../../../store/actions/EmployeeAction";
import { getBranch } from "../../../store/actions/BranchAction";

function ViewEmployee() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const employee = useSelector(state=> state.employeeStore.employee);
    
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    

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
                `http://localhost:9090/api/user/update/status/${employee.user.id}/${newStatus}`,
                {},
                { headers }
            );

            setSuccessMessage(`Employee ${newStatus.toLowerCase()}d successfully`);
            setTimeout(() => setSuccessMessage(""), 3000);
            getEmployee(dispatch)(employee.id);
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
            {/* Breadcrumb */}
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span
                            role="button"
                            onClick={() => navigate("../category")}
                            className="text-decoration-none text-primary"
                            style={{ cursor: "pointer" }}
                        >
                            Employees by Category
                        </span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Employee Profile
                    </li>
                </ol>
            </nav>

            <div className="row justify-content-center">
                <div className="col-md-10">
                    <div className="card border border-dark shadow rounded-3 bg-white">
                        <div className="card-header bg-secondary bg-opacity-10 fw-semibold"
                            style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}>
                            Employee Profile
                        </div>

                        <div className="card-body">
                            {employee && employee?.user ? (
                                <div className="d-flex">
                                    {/* Icon */}
                                    <div className="d-flex align-items-center justify-content-center me-4" style={{ minWidth: "200px" }}>
                                        <FaUserCircle size={160} className="text-secondary" />
                                    </div>

                                    {/* Details */}
                                    <div className="w-100">
                                        <div className="bg-secondary text-white rounded p-2 mb-3">
                                            <h5 className="mb-0">Employee Information</h5>
                                        </div>

                                        <div className="row">
                                            <div className="col-md-6 mb-2"><strong>Username:</strong> {employee.user.username}</div>
                                            <div className="col-md-6 mb-2"><strong>Contact Number:</strong> {employee.contactNumber}</div>
                                            <div className="col-md-6 mb-2"><strong>Full Name:</strong> {employee.name}</div>
                                            <div className="col-md-6 mb-2"><strong>Email:</strong> {employee.email}</div>
                                            <div className="col-md-6 mb-2"><strong>Gender:</strong> {employee.gender}</div>
                                            <div className="col-md-6 mb-2">
                                                <strong>Account Status:</strong>{" "}
                                                <span className={getStatusClass(employee.user.status)}>{employee.user.status}</span>
                                            </div>
                                            <div className="col-md-6 mb-2"><strong>Date of Birth:</strong> {employee.dob}</div>
                                            <div className="col-md-6 mb-2"><strong>Designation:</strong> {employee.designation}</div>
                                            <div className="col-md-12 mb-2"><strong>Branch:</strong> {employee.branch?.branchName}</div>
                                            <div className="col-md-12 mb-2">
                                                <button
                                                    className="btn btn-outline-secondary btn-sm"
                                                    onClick={() =>{ getBranch(dispatch)(employee.branch.id); navigate("../employeeBranch");}}
                                                >
                                                    View Branch
                                                </button>
                                            </div>
                                            <div className="col-md-12 mb-2"><strong>Address:</strong> {employee.address}</div>
                                        </div>

                                        {/* Status Buttons */}
                                        {employee.user.status !== "DELETED" && (
                                            <div className="d-flex gap-3 mt-3">
                                                {employee.user.status === "SUSPENDED" && (
                                                    <button className="btn btn-success" onClick={() => updateStatus("ACTIVE")}>Activate Account</button>
                                                )}
                                                {(employee.user.status === "ACTIVE" || employee.user.status === "INACTIVE") && (
                                                    <button className="btn btn-warning" onClick={() => updateStatus("SUSPENDED")}>Suspend Account</button>
                                                )}
                                                <button className="btn btn-danger" onClick={() => updateStatus("DELETED")}>Delete Account</button>
                                            </div>
                                        )}

                                        {/* Messages */}
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
                                <div className="text-center text-muted fs-5 py-5">No employee data available</div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ViewEmployee;
