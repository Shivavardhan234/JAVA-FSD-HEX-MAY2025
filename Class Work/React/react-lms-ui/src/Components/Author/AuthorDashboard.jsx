import { useEffect, useRef, useState } from 'react';
import '../CSS/Author.css';
import { Link, Outlet, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { deleteUserDetails } from '../../store/actions/UserAction'
import { fetchAllCourses } from '../../store/actions/CourseAction';
function AuthorDashboard(){
const navigate=useNavigate();
const dispatch =useDispatch();




   useEffect(() => {
        let token = localStorage.getItem('token');
        if (token == null || token == undefined || token == "")
            navigate("/");

        fetchAllCourses(dispatch)();
    }, [dispatch]);




const logOut=()=>{
    deleteUserDetails(dispatch);
    localStorage.clear();
    navigate("/");

}




    return(
        <div className='container'>
        <div className="area"></div><nav className="main-menu">
            <ul>
                <li>
                    <Link to="/author/">
                        <i className="fa fa-home fa-2x"></i>
                        <span className="nav-text">
                           Home
                        </span>
                    
                    </Link>
                </li>
                <li className="has-subnav">
                    <Link to="/author/Courses">
                        <i className="fa fa-book fa-2x"></i>
                        <span className="nav-text">
                            Courses
                        </span>
                    </Link>
                    
                </li>
                <li className="has-subnav">
                    <Link to="/author/Enrollments">
                       <i className="fa fa-book-reader fa-2x"></i>
                        <span className="nav-text">
                            Enrollments
                        </span>
                    </Link>
                    
                </li>
                <li className="has-subnav">
                    <Link to="/author/Profile">
                       <i className="fa fa-user-circle fa-2x"></i>
                        <span className="nav-text">
                            Profile
                        </span>
                    </Link>
                   
                </li>
               
            </ul>

            <ul className="logout">
                <li>
                   <a onClick={()=>logOut()} href="#">
                         <i className="fa fa-power-off fa-2x"></i>
                        <span className="nav-text">
                            Logout
                        </span>
                    </a>
                </li>  
            </ul>
        </nav>
        <main className="content-area">
               
                <Outlet />
            </main>
        </div>

    );

}

export default AuthorDashboard;