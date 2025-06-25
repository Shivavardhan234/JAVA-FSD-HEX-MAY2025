import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logOutAction } from '../../../store/actions/UserAction';
import { useEffect } from 'react';
import { getBranch } from '../../../store/actions/BranchAction';


function TransactionAnalystNavbar() {
    const navigate = useNavigate();
    const dispatch = useDispatch();

    



    const logOut = () => {
        logOutAction(dispatch)();
        localStorage.clear();
        navigate("/");
    };

    return (
        <>
            <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
                <div className="container-fluid">
                    {/* Logo */}
                    <Link className="navbar-brand fw-bold" to="/transactionAnalyst/transactionSidebar">
                        MAVERICK BANK
                    </Link>

                    {/* Navigation Links */}
                    <div className="collapse navbar-collapse">
                        <ul className="navbar-nav ms-3">
                            <li className="nav-item border-end border-white pe-3 me-3">
                                <Link to="transactionSidebar" className="nav-link">
                                    Transactions Dashboard
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to="employeeProfile" className="nav-link">
                                    Profile
                                </Link>
                            </li>
                        </ul>
                    </div>

                    {/* Right-side Log Out */}
                    <div className="ms-auto">
                        <button className="btn btn-outline-light" onClick={logOut}>
                            Log Out
                        </button>
                    </div>
                </div>
            </nav>

            {/* Space for child routes */}
            <div className="container" style={{ paddingTop: '70px' }}>
                <Outlet />
            </div>
        </>
    );

}
export default TransactionAnalystNavbar;