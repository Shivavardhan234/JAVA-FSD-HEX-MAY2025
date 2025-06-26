import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Link } from "react-router-dom";
import { getUserDetails } from "../../store/actions/UserAction";
import { useDispatch, useSelector } from "react-redux";




function ViewBranchForEmployee() {
    const location = useLocation();
    const branch = useSelector(state => state.branchStore.branch);

    const dispatch = useDispatch();

    getUserDetails(dispatch)();
    const employee = useSelector(state => state.user.userDetails);
    const role = employee?.user?.role;

    const getProfilePath = () => {
        switch (role) {
            case "ACCOUNT_MANAGER":
                return "/accountManager/employeeProfile";
            case "LOAN_OFFICER":
                return "/loanOfficer/employeeProfile";
            case "REPORT_MANAGER":
                return "/reportManager/employeeProfile";
            case "TRANSACTION_ANALYST":
                return "/transactionAnalyst/employeeProfile";
            case "JUNIOR_OPERATIONS_MANAGER":
                return "/juniorOperationsManager/employeeProfile";
            case "SENIOR_OPERATIONS_MANAGER":
                return "/seniorOperationsManager/employeeProfile";
            default:
                 logOutAction(dispatch)();
                localStorage.clear();
                console.error("you are unauthorized...!!!")
                return "/";
        }
    };








    return (
        <>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <Link to={getProfilePath()} className="text-decoration-none">
                            Employee Profile
                        </Link>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        View Branch
                    </li>
                </ol>
            </nav>

            <div className="container-fluid position-relative">
                <div className="card shadow border-0 bg-light-subtle" style={{ borderRadius: "10px" }}>
                    {/* Header */}
                    <div className="card-header bg-secondary bg-opacity-10 fw-semibold"
                        style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}>
                        <h5 className="mb-0">Branch Details</h5>
                    </div>

                    {/* Body */}
                    <div className="card-body">
                        {branch ? (
                            <div className="row">
                                <div className="col-md-3 d-flex justify-content-center align-items-center">
                                    <i className={`bi bi-bank2 ${branch.status === "ACTIVE" ? "text-success" : "text-danger"}`}
                                        style={{ fontSize: "5rem" }}></i>
                                </div>
                                <div className="col-md-9">
                                    <div className="mb-2"><strong>Name:</strong> {branch.branchName}</div>
                                    <div className="mb-2"><strong>IFSC:</strong> {branch.ifsc}</div>
                                    <div className="mb-2"><strong>Contact:</strong> {branch.contactNumber}</div>
                                    <div className="mb-2"><strong>Email:</strong> {branch.email}</div>
                                    <div className="mb-2"><strong>Address:</strong> {branch.address}</div>
                                    <div className="mb-2"><strong>Status:</strong> {branch.status}</div>
                                </div>
                            </div>
                        ) : (
                            <p className="text-muted">No branch selected.</p>
                        )}
                    </div>


                </div>


            </div>
        </>
    );

}
export default ViewBranchForEmployee;