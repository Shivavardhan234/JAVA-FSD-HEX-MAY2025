import { useState } from 'react';
import { Link, Outlet } from 'react-router-dom';


function CioAccountsSideBar(){
    
     const [isExpanded, setIsExpanded] = useState(false);

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
                        CIO Accounts
                    </span>
                </div>

                {/* Nav links */}
                <ul className="nav nav-pills flex-column mb-auto px-2">
                    {/* Find Employee */}
                    <li className="nav-item border-bottom border-white">
                        <Link
                            to="find"
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
                            }}
                        >
                            <i className="bi bi-search fs-5 text-center" style={{ width: '24px' }}></i>
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
                                Find CIO
                            </span>
                        </Link>
                    </li>

                    {/* all CIO's */}
                    <li className="nav-item border-bottom border-white">
                        <Link
                            to="by-category"
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
                            }}
                        >
                            <i className="bi bi-people-fill fs-5 text-center" style={{ width: '24px' }}></i>
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
                                All CIO's
                            </span>
                        </Link>
                    </li>
                </ul>
            </div>

            {/* Main Content */}
            <div
                className="flex-grow-1"
                style={{
                    marginLeft: isExpanded ? '240px' : '70px',
                    transition: 'margin-left 0.3s ease',
                    paddingTop: '56px',
                    padding: '20px',
                    width: '100%',
                }}
            >
                <Outlet />
            </div>
        </div>
    );

}
export default CioAccountsSideBar;