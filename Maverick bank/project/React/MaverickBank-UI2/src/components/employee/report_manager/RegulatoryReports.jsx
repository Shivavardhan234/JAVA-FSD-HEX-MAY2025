import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { getRegulatoryReport } from "../../../store/actions/RegulatoryReportAction";

function RegulatoryReports() {
    const [filterType, setFilterType] = useState("ALL");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [reports, setReports] = useState([]);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const token = localStorage.getItem("token");

    const fetchReports = async () => {
        setError("");
        let url = "http://localhost:9090/api/regulatory-report/get/all";

        if (filterType === "BY_DATE" && startDate && endDate) {
            url = `http://localhost:9090/api/regulatory-report/get/by-date-range/${startDate}/${endDate}`;
        }

        try {
            const res = await axios.get(url, {
                headers: { Authorization: `Bearer ${token}` }
            });
            setReports(res.data);
        } catch (err) {
            setError(err.response?.data?.message || "Failed to fetch reports.");
            setReports([]);
        }
    };

    useEffect(() => {
        if (filterType === "ALL") fetchReports();
    }, [filterType]);

    const handleSearch = () => {
        if (filterType === "BY_DATE" && startDate && endDate) {
            fetchReports();
        }
    };

    const viewReport = (report) => {
        getRegulatoryReport(dispatch)(report.id);
        navigate('../viewRegulatoryReport')
    };

    return (
        <> <nav aria-label="breadcrumb" className="mb-3">
            <ol className="breadcrumb">
                <li className="breadcrumb-item text-muted">Report </li>

                
                <li className="breadcrumb-item active" aria-current="page">
                    Regulatory Reports
                </li>
            </ol>
        </nav>
            <div className="card shadow-sm">

                {/* Header */}
                <div className="card-header d-flex flex-wrap justify-content-between align-items-center">
                    <select
                        className="form-select"
                        style={{ width: "200px" }}
                        value={filterType}
                        onChange={(e) => setFilterType(e.target.value)}
                    >
                        <option value="ALL">All Reports</option>
                        <option value="BY_DATE">By Date</option>
                    </select>

                    {filterType === "BY_DATE" && (
                        <div className="d-flex gap-2 align-items-center mt-2 mt-md-0">
                            <input
                                type="date"
                                className="form-control"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                            />
                            <input
                                type="date"
                                className="form-control"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                            />
                            <button className="btn btn-primary" onClick={handleSearch}>
                                Search
                            </button>
                        </div>
                    )}
                </div>

                {/* Body */}
                <div className="card-body">
                    {error && <div className="alert alert-danger">{error}</div>}

                    {reports.length === 0 ? (
                        <p className="text-muted">No reports found.</p>
                    ) : (
                        <div className="table-responsive">
                            <table className="table table-striped table-bordered table-hover">
                                <thead className="table-dark">
                                    <tr>
                                        <th scope="col">Report ID</th>
                                        <th scope="col">Report Date</th>
                                        <th scope="col">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {reports.map((report) => (
                                        <tr key={report.id}>
                                            <td>{report.id}</td>
                                            <td>{report.reportDate}</td>
                                            <td>
                                                <button
                                                    className="btn btn-sm btn-outline-primary"
                                                    onClick={() => viewReport(report)}
                                                >
                                                    View Report
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default RegulatoryReports;
