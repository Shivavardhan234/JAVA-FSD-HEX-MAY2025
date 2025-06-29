import { useEffect, useState } from "react";
import { Form, InputGroup, Button } from "react-bootstrap";
import { BsSearch } from "react-icons/bs";
import axios from "axios";
import { FaUniversity } from "react-icons/fa";
import { Outlet, useNavigate, useOutletContext } from "react-router-dom";
import { useDispatch } from "react-redux";
import { getBranch } from "../../../store/actions/BranchAction";

function BranchByCategory() {
    const [branches, setBranches] = useState([]);
    const [page, setPage] = useState(0);
    const [branchesPerPage, setBranchesPerPage] = useState(10);
    const [filter, setFilter] = useState("ALL");
    const [stateQuery, setStateQuery] = useState("");
    const [message, setMessage] = useState("");


    const navigate = useNavigate();
    const dispatch = useDispatch();

    

    const { isExpanded } = useOutletContext();

    useEffect(()=>{
        fetchBranches();
    },[page])

    useEffect(() => {
        if (!filter.includes("BY_STATE")) {
            fetchBranches();
        }
        setPage(0);
    }, [filter]);

    const fetchBranches = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            let url = "";

            switch (filter) {
                case "ALL":
                    url = `http://localhost:9090/api/branch/get/all?page=${page}&size=${branchesPerPage}`;
                    break;
                case "ACTIVE":
                    url = `http://localhost:9090/api/branch/get/active?page=${page}&size=${branchesPerPage}`;
                    break;
                case "INACTIVE":
                    url = `http://localhost:9090/api/branch/get/inactive?page=${page}&size=${branchesPerPage}`;
                    break;
                case "BY_STATE":
                    url = `http://localhost:9090/api/branch/get/by-state?state=${stateQuery}&page=${page}&size=${branchesPerPage}`;
                    break;
                case "ACTIVE_BY_STATE":
                    url = `http://localhost:9090/api/branch/get/active-by-state?state=${stateQuery}&page=${page}&size=${branchesPerPage}`;
                    break;
                case "INACTIVE_BY_STATE":
                    url = `http://localhost:9090/api/branch/get/inactive-by-state?state=${stateQuery}&page=${page}&size=${branchesPerPage}`;
                    break;
                default:
                    return;
            }

            const response = await axios.get(url, {
                headers: { Authorization: bearerToken },
            });

            setBranches(response.data);
        } catch (err) {
            setBranches([]);
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

    const handleSearch = () => {
        if (filter.includes("BY_STATE") && stateQuery.trim() !== "") {
            fetchBranches();
        }
    };

    return (
        <>
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">BRANCH</span>
                    </li>
                    <li className="breadcrumb-item active" aria-current="page">
                        Branches by Category
                    </li>
                </ol>
            </nav>
            <div
                className="d-flex flex-column"
                style={{
                    height: "calc(100vh - 56px)",
                    marginLeft: "70px",
                    transition: "margin-left 0.3s ease",
                    padding: "20px",
                }}
            >

                <div className="card" style={{ height: "100%", boxShadow: "0 0 8px rgba(0,0,0,0.1)" }}>
                    {/* Header */}
                    <div
                        className="card-header border-bottom-0"
                        style={{ boxShadow: "none", backgroundColor: "#f8f9fa" }}
                    >
                        <div className="d-flex justify-content-between align-items-center">
                            <Form.Select
                                style={{ maxWidth: "250px" }}
                                value={filter}
                                onChange={(e) => setFilter(e.target.value)}
                            >
                                <option value="ALL">All Branches</option>
                                <option value="ACTIVE">Active Branches</option>
                                <option value="INACTIVE">Inactive Branches</option>
                                <option value="BY_STATE">Branches by State</option>
                                <option value="ACTIVE_BY_STATE">Active Branches by State</option>
                                <option value="INACTIVE_BY_STATE">Inactive Branches by State</option>
                            </Form.Select>

                            {filter.includes("BY_STATE") && (
                                <InputGroup style={{ maxWidth: "300px" }}>
                                    <Form.Control
                                        placeholder="Enter state name"
                                        value={stateQuery}
                                        onChange={(e) => setStateQuery(e.target.value)}
                                    />
                                    <Button variant="primary" onClick={()=>handleSearch()}>
                                        <BsSearch />
                                    </Button>
                                </InputGroup>
                            )}
                        </div>
                    </div>

                    {/* Body */}
                    <div
                        className="card-body flex-grow-1 overflow-auto border-top border-bottom-0"
                        style={{ boxShadow: "none" }}
                    >
                        {branches.map((branch, idx) => (
                            <div
                                key={idx}
                                className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white"
                            >
                                {/* Header with ash background */}
                                <div className="px-3 py-2 bg-light border-bottom">
                                    <h5 className="mb-0">{branch.branchName}</h5>
                                </div>

                                {/* Body with icon and details */}
                                <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                    {/* Left: icon and info */}
                                    <div className="d-flex align-items-center">
                                        <FaUniversity
                                            size={24}
                                            className="me-3"
                                            color={branch.status === "ACTIVE" ? "green" : "red"}
                                        />
                                        <div>
                                            <p className="mb-1">
                                                <strong>Branch ID:</strong> {branch.id}
                                            </p>
                                            <p className="mb-0">
                                                <strong>Address:</strong> {branch.address}
                                            </p>
                                        </div>
                                    </div>

                                    {/* Right: View button */}
                                    <div>
                                        <Button variant="outline-primary" size="sm" onClick={() => {getBranch(dispatch)(branch.id);   navigate("../branchDetails");}}>
                                            View Branch
                                        </Button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    {/* Footer */}
                    <div
                        className="card-footer bg-light d-flex justify-content-between align-items-center px-3 border-top-0"
                        style={{ boxShadow: "none" }}
                    >
                        {/* Items per page dropdown */}
                        <div className="d-flex align-items-center">
                            <span className="me-2">Items per page:</span>
                            <Form.Select
                                value={branchesPerPage}
                                onChange={(e) => {
                                    setBranchesPerPage(Number(e.target.value));
                                    setPage(0);
                                    
                                }}
                                style={{ width: "80px" }}
                            >
                                {[10, 15, 20, 30].map((num) => (
                                    <option key={num} value={num}>{num}</option>
                                ))}
                            </Form.Select>
                        </div>

                        {/* Pagination */}
                        <nav>
                            <ul className="pagination mb-0">
                                <li className="page-item">
                                    <button className="page-link" onClick={() => setPage(page - 1)}>&laquo;</button>
                                </li>
                                
                                    <li key={page} className="page-item">
                                        <button className="page-link">{page + 1}</button>
                                    </li>
                                
                                <li className="page-item">
                                    <button className="page-link" onClick={() => setPage(page + 1)}>&raquo;</button>
                                </li>
                            </ul>
                        </nav>

                    </div>
                </div>
                <Outlet context={{ isExpanded }} />
            </div>
        </>
    );
}

export default BranchByCategory;
