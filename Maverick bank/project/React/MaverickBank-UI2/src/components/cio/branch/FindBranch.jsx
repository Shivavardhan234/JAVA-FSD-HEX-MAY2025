import axios from "axios";
import { useEffect, useState } from "react";
import { Form, Button, InputGroup } from "react-bootstrap";
import { BsSearch } from "react-icons/bs";
import { useOutletContext } from "react-router-dom";

function FindBranch() {
    let [searchBy, setSearchBy] = useState("ID");
    let [query, setQuery] = useState("");
    let [branch, setBranch] = useState(null);
    const [branchName, setBranchName] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [email, setEmail] = useState('');
    const [address, setAddress] = useState('');
    const [ifsc, setIfsc] = useState('');
    const [message, setMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const [showEditOverlay, setShowEditOverlay] = useState(false);
    const { isExpanded } = useOutletContext();


    //----------------------------------------- when branch is changed update all variables/states ------------------------------------------
    useEffect(() => {
        if (branch !== null) {
            setBranchName(branch.branchName || '');
            setContactNumber(branch.contactNumber || '');
            setEmail(branch.email || '');
            setAddress(branch.address || '');
            setIfsc(branch.ifsc || '');
        }
    }, [branch]);
    //--------------------------------------------------------- Search operation -----------------------------------------------------------
    const handleSearch = async () => {
        try {
            if (searchBy === "ID") {
                if (query === "") {
                    setMessage("Enter appropriate Id...!!!")
                    return;
                }
                const token = localStorage.getItem('token');
                const baererAuthString = "Bearer " + token;

                const responseById = await axios.get(`http://localhost:9090/api/branch/get/by-id/${query}`,
                    { headers: { Authorization: baererAuthString } });

                setBranch(responseById.data)
                setMessage("");
                return;
            }
            else if (searchBy === "IFSC") {
                if (query === "") {
                    setMessage("Enter appropriate IFSC code...!!!")
                    return;
                }
                const token = localStorage.getItem('token');
                const baererAuthString = "Bearer " + token;

                const responseByIfsc = await axios.get(`http://localhost:9090/api/branch/get/by-ifsc?ifsc=${query}`,
                    { headers: { Authorization: baererAuthString } });

                setBranch(responseByIfsc.data)
                setMessage("");
                return;
            } else if (searchBy === "NAME") {
                if (query === "") {
                    setMessage("Enter appropriate Branch name...!!!")
                    return;
                }
                const token = localStorage.getItem('token');
                const baererAuthString = "Bearer " + token;

                const responseByName = await axios.get(`http://localhost:9090/api/branch/get/by-name?name=${query}`,
                    { headers: { Authorization: baererAuthString } });

                setBranch(responseByName.data)
                setMessage("");
                return;
            }
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
    //----------------------------------------- deactivate branch ---------------------------------------------------------------------------
    const deactivateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const baererAuthString = "Bearer " + token;
            const response = await axios.put(`http://localhost:9090/api/branch/deactivate/${branch.id}`, {},
                { headers: { Authorization: baererAuthString } });

            setBranch(response.data);
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

    }
    //----------------------------------------- activate branch ---------------------------------------------------------------------------
    const activateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const baererAuthString = "Bearer " + token;
            const response = await axios.put(`http://localhost:9090/api/branch/activate/${branch.id}`, {},
                { headers: { Authorization: baererAuthString } });

            setBranch(response.data);
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

    }

    //----------------------------------------------- update branch -----------------------------------------------------------------------
    const updateBranch = async () => {
        try {
            const token = localStorage.getItem('token');
            const baererAuthString = "Bearer " + token;
            const response = await axios.put(`http://localhost:9090/api/branch/update`, {
                'id': branch.id,
                'ifsc': ifsc,
                'branchName': branchName,
                'contactNumber': contactNumber,
                'email': email,
                'address': address

            },
                { headers: { Authorization: baererAuthString } });

            setBranch(response.data);
            setShowEditOverlay(false)
            setSuccessMessage("Branch updated successfully!!! ");
            setMessage("");
            // Auto-clear the message after 3 seconds
            setTimeout(() => {
                setSuccessMessage("");
            }, 3000);

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

    }


    //------------------------------------------ if overlay is changed ,change the responsiveness of screen-----------------------------

    useEffect(() => {
        if (showEditOverlay) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'auto';
        }
    }, [showEditOverlay]);

    //---------------------------------------------------- UI ---------------------------------------------------------------------------

    return (
        <div className="container-fluid position-relative">
            
             <nav aria-label="breadcrumb" className="mb-3">
                <ol className="breadcrumb">
                    <li className="breadcrumb-item">
                        <span className="text-decoration-none text-muted">BRANCH</span>
                    </li>
                   
                    <li className="breadcrumb-item active" aria-current="page">
                      Find Branch
                    </li>
                </ol>
            </nav>
            <div className="card shadow border-0 bg-light-subtle" style={{ borderRadius: "10px" }}>

                {/* Header */}
                <div
                    className="card-header d-flex justify-content-between align-items-center bg-secondary bg-opacity-10 fw-semibold"
                    style={{ borderTopLeftRadius: "10px", borderTopRightRadius: "10px" }}
                >
                    {/* Dropdown */}
                    <Form.Select
                        size="sm"
                        value={searchBy}
                        onChange={(e) => setSearchBy(e.target.value)}
                        style={{ maxWidth: "150px" }}
                    >
                        <option value="ID">By ID</option>
                        <option value="IFSC">By IFSC</option>
                        <option value="NAME">By Name</option>
                    </Form.Select>

                    {/* Input + Search */}
                    <InputGroup size="sm" style={{ maxWidth: "300px" }}>
                        <Form.Control
                            placeholder="Enter search value"
                            value={query}
                            onChange={(e) => setQuery(e.target.value)}
                        />
                        <Button variant="primary" onClick={() => { handleSearch() }}>
                            <BsSearch />
                        </Button>
                    </InputGroup>
                </div>

                {/* Body */}
                <div className="card-body">
                    {branch ? (
                        <div className="row">
                            {/* Left: Big Bank Icon */}
                            <div className="col-md-3 d-flex justify-content-center align-items-center">
                                <i
                                    className={`bi bi-bank2 ${branch.status === "ACTIVE" ? "text-success" : "text-danger"}`}
                                    style={{ fontSize: "5rem" }}
                                ></i>
                            </div>

                            {/* Right: Branch Details */}
                            <div className="col-md-9">
                                <div className="mb-2"><strong>ID:</strong> {branch.id}</div>
                                <div className="mb-2"><strong>IFSC:</strong> {branch.ifsc}</div>
                                <div className="mb-2"><strong>Name:</strong> {branch.branchName}</div>
                                <div className="mb-2"><strong>Contact:</strong> {branch.contactNumber}</div>
                                <div className="mb-2"><strong>Email:</strong> {branch.email}</div>
                                <div className="mb-2"><strong>Address:</strong> {branch.address}</div>
                                <div className="mb-2"><strong>Status:</strong> {branch.status}</div>

                            </div>
                        </div>
                    ) : (
                        <p className="text-muted">No branch selected.</p>
                    )}
                    {message && (
                        <div className="text-danger mt-2">
                            {message}
                        </div>
                    )}
                </div>



                {/* Footer */}
                {branch && (
                    <div className="card-footer d-flex justify-content-end gap-2">
                        <Button variant="secondary" size="sm" onClick={() => { setShowEditOverlay(true) }}>Edit Branch</Button>
                        {branch.status === "ACTIVE" ? (
                            <Button variant="danger" size="sm" onClick={() => { deactivateBranch() }}>Deactivate Branch</Button>
                        ) : (
                            <Button variant="success" size="sm" onClick={() => { activateBranch() }}>Activate Branch</Button>
                        )}
                    </div>
                )}
            </div>
            {showEditOverlay && (
                <div
                    className="position-fixed bg-dark bg-opacity-50"
                    style={{
                        top: '56px',
                        left: isExpanded ? '240px' : '70px',
                        width: isExpanded ? 'calc(100% - 240px)' : 'calc(100% - 70px)',
                        height: 'calc(100vh - 56px)',
                        backdropFilter: 'blur(4px)',
                        zIndex: 1050,
                        transition: 'left 0.3s ease, width 0.3s ease',
                    }}
                >
                    <div className="d-flex justify-content-center align-items-center h-100">
                        <div
                            className="bg-white p-4 rounded shadow-lg"
                            style={{
                                width: '90%',
                                maxWidth: '600px',
                                maxHeight: '90vh',
                                overflowY: 'auto',
                            }}
                        >
                            <div className="bg-secondary text-white rounded p-2 mb-3">
                                <h5 className="mb-0">Edit Branch</h5>
                            </div>

                            <div className="row">
                                <div className="col-md-6">
                                    <div className="mb-3">
                                        <label className="form-label">IFSC</label>
                                        <input type="text" className="form-control" value={ifsc} onChange={(e) => setIfsc(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Branch Name</label>
                                        <input type="text" className="form-control" value={branchName} onChange={(e) => setBranchName(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Contact Number</label>
                                        <input type="text" className="form-control" value={contactNumber} onChange={(e) => setContactNumber(e.target.value)} />
                                    </div>
                                </div>

                                <div className="col-md-6">
                                    <div className="mb-3">
                                        <label className="form-label">Email</label>
                                        <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Address</label>
                                        <input type="text" className="form-control" value={address} onChange={(e) => setAddress(e.target.value)} />
                                    </div>
                                </div>
                            </div>

                            {message && <div className="text-danger mt-2">{message}</div>}


                            <div className="d-flex justify-content-end gap-3 mt-3">
                                <button className="btn btn-secondary" onClick={() => setShowEditOverlay(false)}>Cancel</button>
                                <button className="btn btn-success" onClick={() => updateBranch()}>Save</button>
                            </div>
                        </div>
                    </div>

                </div>
            )}
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

export default FindBranch;
