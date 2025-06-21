import "../../css/CioDashboard.css";
import { useNavigate } from "react-router-dom";
import { Outlet } from "react-router-dom";

function CioDashboard() {
    const navigate = useNavigate();

    const logOut = () => {
        localStorage.clear();
        navigate("/signIn");
    };

    return (
        <div>
            <nav className="navbar">
                <div className="logo">MAVERICK BANK</div>
                <button className="logout-btn" onClick={logOut}>Log Out</button>
            </nav>

            <div className="cio-bar">
                <div className="cio-link-section"></div> {/* Empty Left Partition */}

                <div className="cio-link-section hoverable">
                    <a href="#" className="cio-link" onClick={()=>navigate('/cio/customerAccounts')}>Customer Accounts</a>
                </div>

                <div className="cio-link-section hoverable">
                    <a href="#" className="cio-link">Employee Accounts</a>
                </div>

                <div className="cio-link-section hoverable">
                    <a href="#" className="cio-link">CIO Accounts</a>
                </div>
                <div className="cio-link-section hoverable">
                    <a href="#" className="cio-link">Branch</a>
                </div>
                <div className="cio-link-section"></div> {/* Empty Right Partition */}
            </div>
            <div className="main-content">
                <Outlet />
            </div>
        </div>
    );
}

export default CioDashboard;
