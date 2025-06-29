import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useNavigate} from "react-router-dom";
import { useDispatch } from "react-redux";
import { getEmployee } from "../../../store/actions/EmployeeAction";

function EmployeesByCategory() {
    const [employees, setEmployees] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [filter, setFilter] = useState("ALL");
    const [designation, setDesignation] = useState("LOAN_OFFICER");
    const [branchId, setBranchId] = useState("");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        fetchEmployees();
    }, [page]);
    

    useEffect(() => {
        fetchEmployees();
    }, [filter, designation]);

    const fetchEmployees = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            let url = "";

            switch (filter) {
                case "ALL":
                    url = `http://localhost:9090/api/employee/get/all?page=${page}&size=${size}`;
                    break;
                case "ACTIVE":
                case "INACTIVE":
                case "SUSPENDED":
                case "DELETED":
                    url = `http://localhost:9090/api/employee/get/by-status/${filter}?page=${page}&size=${size}`;
                    break;
                case "DESIGNATION":
                    url = `http://localhost:9090/api/employee/get/by-designation?designation=${designation}&page=${page}&size=${size}`;
                    break;
                case "BRANCH":
                    url = `http://localhost:9090/api/employee/get/by-branch-id/${branchId}?page=${page}&size=${size}`;
                    break;
                default:
                    return;
            }

            const response = await axios.get(url, {
                headers: { Authorization: bearerToken },
            });

            setEmployees(response.data);
        } catch (err) {
            console.error(err);
            setEmployees([]);
            if (err.response?.data) {
                const errorData = err.response.data;
                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };

    return (
        <div className="d-flex flex-column" style={{ height: "calc(100vh - 56px)", marginLeft: "70px", transition: "margin-left 0.3s ease", padding: "20px" }}>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">Employee Accounts</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                    Employees By Category
                    </li>
                </ol>
            </nav>
            <div className="card" style={{ height: "100%", boxShadow: "0 0 8px rgba(0,0,0,0.1)" }}>
                {/* Header */}
                <div className="card-header bg-light d-flex gap-3 align-items-center">
                    <Form.Select style={{ maxWidth: "250px" }} value={filter} onChange={(e) => setFilter(e.target.value)}>
                        <option value="ALL">All Employees</option>
                        <option value="ACTIVE">Active Employees</option>
                        <option value="INACTIVE">Inactive Employees</option>
                        <option value="SUSPENDED">Suspended Employees</option>
                        <option value="DELETED">Deleted Employees</option>
                        <option value="DESIGNATION">By Designation</option>
                        <option value="BRANCH">By Branch ID</option>
                    </Form.Select>

                    {filter === "DESIGNATION" && (
                        <Form.Select style={{ maxWidth: "250px" }} value={designation} onChange={(e) => setDesignation(e.target.value)}>
                            {[
                                "LOAN_OFFICER",
                                "ACCOUNT_MANAGER",
                                "REPORT_MANAGER",
                                "TRANSACTION_ANALYST",
                                "JUNIOR_OPERATIONS_MANAGER",
                                "SENIOR_OPERATIONS_MANAGER"
                            ].map((role) => (
                                <option key={role} value={role}>{role}</option>
                            ))}
                        </Form.Select>
                    )}

                    {filter === "BRANCH" && (
                        <div className="input-group" style={{ maxWidth: "300px" }}>
                            <Form.Control type="number" placeholder="Branch ID" value={branchId} onChange={(e) => setBranchId(e.target.value)} />
                            <Button variant="outline-secondary" onClick={fetchEmployees}><i className="bi bi-search"></i></Button>
                        </div>
                    )}
                </div>

                {/* Body */}
                <div className="card-body flex-grow-1 overflow-auto border-top border-bottom-0">
                    {employees.map((emp, idx) => (
                        <div key={idx} className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white">
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">Employee ID: {emp.id}</h5>
                            </div>

                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                <div>
                                    <p className="mb-1"><strong>Name:</strong> {emp.name}</p>
                                    <p className="mb-1"><strong>Phone:</strong> {emp.contactNumber}</p>
                                    <p className="mb-1"><strong>Designation:</strong> {emp.designation}</p>
                                    <p className="mb-0"><strong>Branch:</strong> {emp.branch?.branchName}</p>
                                </div>

                                <Button variant="outline-primary" size="sm" onClick={() =>{getEmployee(dispatch)(emp.id); navigate("../viewEmployee");}}>View Employee</Button>
                            </div>
                        </div>
                    ))}
                </div>

                {/* Footer */}
                <div className="card-footer bg-light d-flex justify-content-between align-items-center px-3 border-top-0">
                    <div className="d-flex align-items-center">
                        <span className="me-2">Items per page:</span>
                        <Form.Select
                            value={size}
                            onChange={(e) => {
                                setSize(Number(e.target.value));
                                setPage(0);
                            }}
                            style={{ width: "80px" }}
                        >
                            {[10, 15, 20, 30].map((num) => (
                                <option key={num} value={num}>{num}</option>
                            ))}
                        </Form.Select>
                    </div>

                    <nav>
                        <ul className="pagination mb-0">
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page - 1)}>&laquo;</button>
                            </li>
                            
                                <li key={page} className="page-item">
                                    <button className="page-link" >{page + 1}</button>
                                </li>
                            
                            <li className="page-item">
                                <button className="page-link" onClick={() => setPage(page + 1)}>&raquo;</button>
                            </li>
                        </ul>
                    </nav>

                    
                </div>
            </div>
        </div>
    );
}

export default EmployeesByCategory;
