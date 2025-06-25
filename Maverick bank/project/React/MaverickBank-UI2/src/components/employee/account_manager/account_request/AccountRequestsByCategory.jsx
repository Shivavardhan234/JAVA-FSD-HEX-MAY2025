import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { getAccountRequest } from "../../../../store/actions/AccountRequestAction";

function AccountRequestsByCategory() {
  const [requests, setRequests] = useState([]);
  const [filter, setFilter] = useState("ALL");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    fetchRequests();
  }, [filter]);

  const fetchRequests = async () => {
    setError("");
    setRequests([]);

    const token = localStorage.getItem("token");
    let url = "http://localhost:9090/api/account-request/get/all";

    if (filter !== "ALL") {
      url = `http://localhost:9090/api/account-request/get/by-status/${filter}`;
    }

    try {
      const res = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setRequests(res.data);
    } catch (err) {
      if (err.response?.data) {
        const firstKey = Object.keys(err.response.data)[0];
        setError(err.response.data[firstKey]);
      } else {
        setError("Something went wrong.");
      }
    }
  };

  return (
    <div className="container mt-4">
      <div className="card shadow">
        <div className="card-header d-flex justify-content-between align-items-center">
          <h5 className="mb-0">Account Requests</h5>
          <select
            className="form-select w-auto"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
          >
            <option value="ALL">All Requests</option>
            <option value="PENDING">Pending Requests</option>
            <option value="ACCEPTED">Accepted Requests</option>
            <option value="REJECTED">Rejected Requests</option>
          </select>
        </div>

        <div className="card-body">
          {error && <div className="alert alert-danger">{error}</div>}

          {requests.length === 0 ? (
            <div className="text-muted">No requests found.</div>
          ) : (
            <table className="table table-bordered">
              <thead className="table-light">
                <tr>
                  <th style={{ width: "10%" }}>#</th>
                  <th>Account Info</th>
                  <th style={{ width: "15%" }}>Action</th>
                </tr>
              </thead>
              <tbody>
                {requests.map((req, idx) => (
                  <tr key={req.id}>
                    <td>{idx + 1}</td>
                    <td>
                      <div><strong>Account Number:</strong> {req.account.accountNumber}</div>
                      <div><strong>Name:</strong> {req.account.accountName}</div>
                      <div><strong>Type:</strong> {req.requestType}</div>
                      <div><strong>Status:</strong> {req.requestStatus}</div>
                    </td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary"
                        onClick={() =>{
                            getAccountRequest(dispatch)(req.id);
                          navigate(`/accountManager/bankAccountsSideBar/viewAccountRequest`);}}>
                        View Request
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}

export default AccountRequestsByCategory;
