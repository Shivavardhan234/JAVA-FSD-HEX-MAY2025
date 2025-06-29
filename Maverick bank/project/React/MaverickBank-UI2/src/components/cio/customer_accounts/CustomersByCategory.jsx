import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { getCustomer } from "../../../store/actions/CustomerAction";

function CustomersByCategory() {
    const [customers, setCustomers] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [filter, setFilter] = useState("ALL");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();
    const dispatch = useDispatch();

   

    useEffect(() => {
        fetchCustomers();
    }, [filter]);

    useEffect(() => {
        fetchCustomers();
    }, [page]);
    

    const fetchCustomers = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            let url = "";

            switch (filter) {
                case "ALL":
                    url = `http://localhost:9090/api/customer/get/all?page=${page}&size=${size}`;
                    break;
                default:
                    url = `http://localhost:9090/api/customer/get/by-status/${filter}?page=${page}&size=${size}`;
                    break;
            }

            const response = await axios.get(url, {
                headers: { Authorization: bearerToken },
            });

            setCustomers(response.data);
        } catch (err) {
            setCustomers([]);
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
        <div
            className="d-flex flex-column"
            style={{
                height: "calc(100vh - 56px)",
                marginLeft: "70px",
                transition: "margin-left 0.3s ease",
                padding: "20px",
            }}
        >
            <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">Customer Accounts</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                       Customers By Category
                    </li>
                </ol>
            </nav>
            <div className="card" style={{ height: "100%", boxShadow: "0 0 8px rgba(0,0,0,0.1)" }}>
                {/* Header */}
                <div className="card-header bg-light">
                    <div className="d-flex justify-content-start align-items-center">
                        <Form.Select
                            style={{ maxWidth: "250px" }}
                            value={filter}
                            onChange={(e) => setFilter(e.target.value)}
                        >
                            <option value="ALL">All Customers</option>
                            <option value="ACTIVE">Active Customers</option>
                            <option value="INACTIVE">Inactive Customers</option>
                            <option value="SUSPENDED">Suspended Customers</option>
                            <option value="DELETED">Deleted Customers</option>
                        </Form.Select>
                    </div>
                </div>

                {/* Body */}
                <div className="card-body flex-grow-1 overflow-auto border-top border-bottom-0">
                    {customers.map((customer, idx) => (
                        <div
                            key={idx}
                            className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white"
                        >
                            {/* Header with ash background */}
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">Customer ID: {customer.id}</h5>
                            </div>

                            {/* Body with details */}
                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                <div>
                                    <p className="mb-1">
                                        <strong>Name:</strong> {customer.name}
                                    </p>
                                    <p className="mb-0">
                                        <strong>Phone:</strong> {customer.contactNumber}
                                    </p>
                                </div>

                                <Button
                                    variant="outline-primary"
                                    size="sm"
                                    onClick={() => {getCustomer(dispatch)(customer.id);    navigate("../customerProfile");}}
                                >
                                    View Customer
                                </Button>
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
                            <li className="page-item ">
                                <button className="page-link" onClick={() => setPage(page - 1)}>&laquo;</button>
                            </li>
                            
                                <li key={page} className="page-item ">
                                    <button className="page-link" >{page + 1}</button>
                                </li>
                            <li className="page-item ">
                                <button className="page-link" onClick={() => setPage(page + 1)}>&raquo;</button>
                            </li>
                        </ul>
                    </nav>

                    
                </div>
            </div>
        </div>
    );
}

export default CustomersByCategory;
