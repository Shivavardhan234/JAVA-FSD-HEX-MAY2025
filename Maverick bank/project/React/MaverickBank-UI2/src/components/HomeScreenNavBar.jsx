import { Link, Outlet } from "react-router-dom";

function HomeScreenNavBar() {
    return (
        <>

            <nav className="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
                <div className="container-fluid">
                    
                    <Link className="navbar-brand fw-bold" to="/">
                        MAVERICK BANK
                    </Link>

                    
                    <Link className="nav-link text-light ms-3" to="/">
                        Home <span className="visually-hidden">(current)</span>
                    </Link>

                    
                    <div className="ms-auto d-flex">
                        <Link to="/signin" className="btn btn-outline-light me-2">Sign In</Link>
                        <Link to="/signup" className="btn btn-light">Sign Up</Link>
                    </div>
                </div>
            </nav>

            
            <div style={{ paddingTop: '70px' }} className="container">
                <Outlet />
            </div>
        </>

    )
}

export default HomeScreenNavBar;