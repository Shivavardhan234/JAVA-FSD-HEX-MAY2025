import { useNavigate } from 'react-router-dom';
import '../css/HomeScreen.css';

function HomeScreen() {

    const navigate= useNavigate();

    return (
        <nav className="navbar">
            <div className="logo">MAVERICK BANK</div>
            <div className="nav-buttons">
                <button className="btn" onClick={()=>{navigate("/signIn")}}>Sign In</button>
                <button className="btn signup"onClick={()=>{navigate("/signUp")}}>Sign Up</button>
            </div>
        </nav>
    )

}
export default HomeScreen;