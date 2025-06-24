import { useEffect, useState } from "react";
import { Form, Button } from "react-bootstrap";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { getUserDetails } from "../../../store/actions/UserAction";

function CioByCategory() {
    const [cios, setCios] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [ciosPerPage, setCiosPerPage] = useState(10);
    const [filter, setFilter] = useState("ALL");
    const [message, setMessage] = useState("");

    const navigate = useNavigate();
    const dispatch = useDispatch();



    const currentCio = useSelector(state => state.user.userDetails);

    const indexOfLast = currentPage * ciosPerPage;
    const indexOfFirst = indexOfLast - ciosPerPage;
    const currentCios = cios.slice(indexOfFirst, indexOfLast);
    const totalPages = Math.ceil(cios.length / ciosPerPage);

    useEffect(() => {
        fetchCios();
    }, [filter]);
    useEffect(() => { getUserDetails(dispatch)(); }, []);

    

    const fetchCios = async () => {
        try {
            const token = localStorage.getItem("token");
            const bearerToken = "Bearer " + token;
            let url = "";

            switch (filter) {
                case "ALL":
                    url = "http://localhost:9090/api/cio/get/all";
                    break;
                default:
                    url = `http://localhost:9090/api/cio/get/by-status/${filter}`;
                    break;
            }

            const response = await axios.get(url, {
                headers: { Authorization: bearerToken },
            });

            setCios(response.data);
            setCurrentPage(1);
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

    const handleViewCio = (selectedCio) => {
    if (currentCio && selectedCio && selectedCio.id === currentCio.id) {
        navigate("/cio/profile");
    } else {
        navigate("/cio/cioAccounts/viewCio", { state: { cio: selectedCio } });
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
                        <span className="text-decoration-none text-muted">CIO</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                    CIO By Category
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
                            <option value="ALL">All CIOs</option>
                            <option value="ACTIVE">Active CIOs</option>
                            <option value="INACTIVE">Inactive CIOs</option>
                            <option value="SUSPENDED">Suspended CIOs</option>
                            <option value="DELETED">Deleted CIOs</option>
                        </Form.Select>
                    </div>
                </div>

                {/* Body */}
                <div className="card-body flex-grow-1 overflow-auto border-top border-bottom-0">
                    {currentCios.map((cio, idx) => (
                        <div
                            key={idx}
                            className="list-group-item p-0 mb-3 border rounded overflow-hidden bg-white"
                        >
                            <div className="px-3 py-2 bg-light border-bottom">
                                <h5 className="mb-0">CIO ID: {cio.id}</h5>
                            </div>

                            <div className="px-3 py-3 d-flex align-items-center justify-content-between">
                                <div>
                                    <p className="mb-1">
                                        <strong>Name:</strong> {cio.name}
                                    </p>
                                    <p className="mb-0">
                                        <strong>Phone:</strong> {cio.contactNumber}
                                    </p>
                                </div>

                                <Button
                                    variant="outline-primary"
                                    size="sm"
                                    onClick={() =>{handleViewCio(cio)}}
                                >
                                    View CIO
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
                            value={ciosPerPage}
                            onChange={(e) => {
                                setCurrentPage(1);
                                setCiosPerPage(Number(e.target.value));
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
                            <li className={`page-item ${currentPage === 1 ? "disabled" : ""}`}>
                                <button className="page-link" onClick={() => setCurrentPage(currentPage - 1)}>&laquo;</button>
                            </li>
                            {[...Array(totalPages).keys()].map(i => (
                                <li key={i} className={`page-item ${currentPage === i + 1 ? "active" : ""}`}>
                                    <button className="page-link" onClick={() => setCurrentPage(i + 1)}>{i + 1}</button>
                                </li>
                            ))}
                            <li className={`page-item ${currentPage === totalPages ? "disabled" : ""}`}>
                                <button className="page-link" onClick={() => setCurrentPage(currentPage + 1)}>&raquo;</button>
                            </li>
                        </ul>
                    </nav>

                    <div className="text-muted small">
                        Page {currentPage} of {totalPages}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CioByCategory;
