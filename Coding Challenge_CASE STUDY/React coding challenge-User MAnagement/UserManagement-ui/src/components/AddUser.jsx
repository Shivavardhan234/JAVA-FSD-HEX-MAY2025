import axios from "axios";
import { useState } from "react";
import { Link } from "react-router-dom";

function AddUser() {

    let [userId, setUserId] = useState("");
    let [name, setName] = useState("");
    let [email, setEmail] = useState("");
    let [gender, setGender] = useState("");
    let [status, setStatus] = useState("");
    let [token, setToken] = useState("c730e0b4729e66b64a8ff2c7c105413ef09940d738ab31f9628ce783dfb440de");




    const addUser = async () => {
        if(userId === "" ||name === "" || email==="" || gender===""||status===""){
            alert("enter all fields");
            return;
        }


        const bearerAuthString = "Bearer " + token;
        const response = await axios.post("https://gorest.co.in/public/v2/users", {
            "id": userId,
            "name": name,
            "email": email,
            "gender": gender,
            "status": status

        }, {
            headers: { "Authorization": bearerAuthString }
        });

        alert('user added successfully...!!!')

    }

    return (
        <div>
            <div className="card">
                <div className="card-header">
                    <h1>Add User</h1>

                </div>
                <div className="card-body">
                    <div className="input-group mb-3">
                        <span className="input-group-text" id="inputGroup-sizing-default">Enter User id:</span>
                        <input type="text" className="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" onChange={($e) => { setUserId($e.target.value) }} />
                    </div>
                    <div className="input-group mb-3">
                        <span className="input-group-text" id="inputGroup-sizing-default">Enter User name:</span>
                        <input type="text" className="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" onChange={($e) => { setName($e.target.value) }} />
                    </div>
                    <div className="input-group mb-3">
                        <span className="input-group-text" id="inputGroup-sizing-default">Enter User email:</span>
                        <input type="text" className="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" onChange={($e) => { setEmail($e.target.value) }} />
                    </div>
                    <select onChange={($e) => { setGender($e.target.value) }}>
                        <option value="">Select Gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>

                    </select>
                    <select onChange={($e) => { setStatus($e.target.value) }}>
                        <option value="">Select status</option>
                        <option value="active">active</option>
                        <option value="inactive">inactive</option>

                    </select>

                </div>
                <div className="catd-footer">
                    <button type="button" className="btn btn-success me-2" onClick={()=>{addUser()}}>Add User</button>

                    <Link to="/">
                        <button type="button" className="btn btn-primary">Back</button>
                    </Link>
                </div>
            </div>

        </div>
    )

}

export default AddUser;