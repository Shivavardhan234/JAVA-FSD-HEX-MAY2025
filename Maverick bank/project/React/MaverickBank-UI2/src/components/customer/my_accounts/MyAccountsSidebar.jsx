import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { getBankAccount } from '../../../store/actions/BankAccountAction';

function MyAccountsSidebar() {
    const [isExpanded, setIsExpanded] = useState(false);
    const [accounts, setAccounts] = useState([]);
    const [message, setMessage] = useState("");
    const [suspendedAccount, setSuspendedAccount] = useState(null);
    const [showSuspensionOverlay, setShowSuspensionOverlay] = useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const customer = useSelector(state => state.user.userDetails);

    

    

    useEffect(() => {
        fetchAccounts();
    }, []);




    const fetchAccounts = async () => {
        if (!customer || !customer.id) return;

        try {
            const token = localStorage.getItem('token');
            const bearerAuth = 'Bearer ' + token;

            const response = await axios.get(
                `http://localhost:9090/api/customer-account/get/by-customer-id/${customer.id}`,
                {
                    headers: {
                        Authorization: bearerAuth,
                    },
                }
            );

            const customerAccounts = response.data || [];
            const extractedAccounts = customerAccounts.map(ca => ca.account).filter(account => account.accountStatus !== 'CLOSED');

            setAccounts(extractedAccounts);
        } catch (err) {
            console.log(err);

            if (err.response && err.response.data) {
                const errorData = err.response.data;


                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
        }
    };




    const handleAccountClick = (account) => {
        if (account.accountStatus === "OPEN") {

            getBankAccount(dispatch)(account.id);

            navigate(`manageBankAccount/${account.accountNumber}`);
            return;
        }
        else {
            setSuspendedAccount(account);
            setShowSuspensionOverlay(true);
        }

    }
    useEffect(() => {
        if (showSuspensionOverlay) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = 'auto';
        }
    }, [showSuspensionOverlay]);



    const handleRequestOpening = async () => {
        try {
            const token = localStorage.getItem("token");
            const accountId = suspendedAccount?.id;

            if (!token || !accountId) {
                console.error("Token or accountId missing");
                return;
            }

            const headers = {
                Authorization: `Bearer ${token}`,
            };

            await axios.post(
                `http://localhost:9090/api/account-request/add/${accountId}/OPEN/reopen`,
                {},
                { headers }
            );


            alert("Request to reopen account sent successfully!");

        } catch (error) {
             console.log(err);

            if (err.response && err.response.data) {
                const errorData = err.response.data;


                const firstKey = Object.keys(errorData)[0];
                setMessage(errorData[firstKey]);
            } else {
                setMessage("Something went wrong. Try again.");
            }
            alert("Failed to send request.");
        } finally {
            setSuspendedAccount(null);
            setShowSuspensionOverlay(false);
        }
    };


    return (
        <div className="d-flex">
            {/* Sidebar */}
            <div
                className="d-flex flex-column flex-shrink-0 text-white bg-dark position-fixed"
                style={{
                    width: isExpanded ? '240px' : '70px',
                    height: 'calc(100vh - 56px)',
                    top: '56px',
                    left: 0,
                    zIndex: 1040,
                    transition: 'width 0.3s ease',
                }}
                onMouseEnter={() => setIsExpanded(true)}
                onMouseLeave={() => setIsExpanded(false)}
            >
                {/* Heading */}
                <div
                    className="d-flex align-items-center justify-content-center"
                    style={{
                        height: '60px',
                        borderBottom: '1px solid rgba(255,255,255,0.1)',
                        padding: '0 10px',
                    }}
                >
                    <span
                        className="text-uppercase fw-bold text-white"
                        style={{
                            whiteSpace: 'nowrap',
                            overflow: 'hidden',
                            opacity: isExpanded ? 1 : 0,
                            visibility: isExpanded ? 'visible' : 'hidden',
                            transition: 'opacity 0.3s ease',
                        }}
                    >
                        My Accounts
                    </span>
                </div>


                <ul className="nav nav-pills flex-column mb-auto px-2">
                    {/* Dynamically mapped account numbers */}
                    {accounts.map((account, idx) => (
                        <li key={idx} className="nav-item border-bottom border-white">
                            <div


                                className={`nav-link d-flex align-items-center px-3 py-2 ${isExpanded ? 'justify-content-start' : 'justify-content-center'}`}
                                style={{
                                    height: '48px',
                                    color: 'white',
                                    transition: 'background-color 0.2s ease, color 0.2s ease',
                                }}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.backgroundColor = '#ffffff';
                                    e.currentTarget.style.color = '#000000';
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.backgroundColor = '';
                                    e.currentTarget.style.color = '#ffffff';
                                }} onClick={() => { handleAccountClick(account); }}>
                                <i className="bi bi-bank fs-5 text-center" style={{ width: '24px' }}></i>
                                <span
                                    className="ms-2"
                                    style={{
                                        width: isExpanded ? 'auto' : '0px',
                                        opacity: isExpanded ? 1 : 0,
                                        visibility: isExpanded ? 'visible' : 'hidden',
                                        overflow: 'hidden',
                                        transition: 'opacity 0.3s ease, width 0.3s ease',
                                        whiteSpace: 'nowrap',
                                    }}
                                >
                                    <span className="fw-semibold">{account.accountNumber}</span><br />
                                    <span className="small text-white-50">{account.accountName || "Unnamed"}</span>
                                </span>
                            </div>
                        </li>
                    ))}
                </ul>
            </div>

            {/* Main Content */}
            <div
                className="flex-grow-1"
                style={{
                    marginLeft: isExpanded ? '240px' : '70px',
                    transition: 'margin-left 0.3s ease',
                    paddingTop: '56px',
                    width: '100%',
                }}
            >
                <Outlet context={{ isExpanded }} />
                {/* Overlay when account is suspended */}
                {showSuspensionOverlay && (
                    <div
                        className="position-absolute top-0 start-0 w-100 h-100 d-flex align-items-center justify-content-center"
                        style={{
                            backdropFilter: 'blur(5px)',
                            backgroundColor: 'rgba(0,0,0,0.3)',
                            zIndex: 1050,
                        }}
                    >
                        <div className="bg-light rounded shadow p-4" style={{ width: '90%', maxWidth: '400px' }}>
                            {/* Header */}
                            <div className="bg-secondary text-white p-2 rounded-top text-center fw-bold">
                                ACCOUNT SUSPENDED
                            </div>

                            {/* Body */}
                            <div className="p-3 text-center">
                                <p>This account is currently suspended. You are restricted from accessing it.</p>
                            </div>

                            {/* Footer */}
                            <div className="d-flex justify-content-around mt-3">
                                <button className="btn btn-warning" onClick={() => handleRequestOpening()}>
                                    Request for Opening
                                </button>
                                <button className="btn btn-secondary" onClick={() => setShowSuspensionOverlay(false)}>
                                    OK
                                </button>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );

}
export default MyAccountsSidebar;