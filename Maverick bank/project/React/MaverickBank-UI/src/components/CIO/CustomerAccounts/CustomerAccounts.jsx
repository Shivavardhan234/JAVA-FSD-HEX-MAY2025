import { useEffect } from 'react';
import '../../../css/CustomerAccounts.css'
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { fetchAllCustomers } from '../../../store/actions/CustomerAction';




function CustomerAccounts() {

    const dispatch = useDispatch();
    const navigate = useNavigate();



    useEffect(() => {
        let token = localStorage.getItem('token')
        if (token == null || token == undefined || token == "")
            navigate("/");

        fetchAllCustomers(dispatch)();
    }, [dispatch])





    return (
        <div>
            <nav className="main-menu">
                <ul>
                    <li>
                        <Link to='./'>
                            <i className="fa fa-search fa-2x"></i>
                            <span className="nav-text">
                                Search Customer
                            </span>
                        </Link>
                    </li>

                    <li className="has-subnav">
                        <Link to='allCustomers'>
                            <i className="fa fa-user-friends fa-2x"></i>
                            <span className="nav-text">
                                All Customers
                            </span>
                        </Link>

                    </li>



                </ul>

                <ul className="profile">
                    <li>
                        <a href="#">
                            <i className="fa fa-user-circle fa-2x"></i>
                            <span className="nav-text">
                                Profile
                            </span>
                        </a>
                    </li>
                </ul>
            </nav>
            <div className="main-outlet-wrapper">
                <Outlet />
            </div>
        </div>

    )

}
export default CustomerAccounts;