import axios from "axios";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function User() {
    let [userList, setUserList] = useState([]);
    let [token, setToken] = useState("c730e0b4729e66b64a8ff2c7c105413ef09940d738ab31f9628ce783dfb440de");
    let [updateForm, setUpdateForm] = useState(false);
    let [userToBeUpdated, setUserToBeUpdated] = useState(null);
    const bearerAuthString = "Bearer " + token;

    //Used for update operation
    let [name, setName] = useState("");
    let [email, setEmail] = useState("");
    let [gender, setGender] = useState("");
    let [status, setStatus] = useState("");




    useEffect(() => {
        getUserList();


    }, [])

    const getUserList = async () => {

        const response = await axios.get("https://gorest.co.in/public/v2/users");
        setUserList(response.data);



    }

    const deleteUser = async (id) => {

        const response = await axios.delete(`https://gorest.co.in/public/v2/users/${id}`, {
            headers: { "Authorization": bearerAuthString }
        });
        console.log(response.data);
        const temp = userList.filter(u => u.id !== id);
        setUserList(temp);
    }


    //------------------------------ update user-----------------------------------------------------------------------------
    const updateUser = async () => {
        if (name === "" || email === "" || gender === "" || status === "") {
            alert("enter all fields");
            return;
        }



        const updateResponse = await axios.put(`https://gorest.co.in/public/v2/users/${userToBeUpdated?.id}`, {
            "id": userToBeUpdated.id,
            "name": name,
            "email": email,
            "gender": gender,
            "status": status

        }, {
            headers: { "Authorization": bearerAuthString }
        });
        console.log(updateResponse.data);
        const temp = userList.filter(u => u.id !== userToBeUpdated.id);
        setUserList([...temp,updateResponse.data])
        alert('Update Successful...!!!')
        cancelUpdate();
    }
//----------- When ever the user to be updated changed ,also change the states-------------------
    useEffect(() => {
        if (userToBeUpdated) {
            setName(userToBeUpdated?.name);
            setEmail(userToBeUpdated?.email);
            setGender(userToBeUpdated?.gender);
            setStatus(userToBeUpdated?.status);

        }
    }, [userToBeUpdated])



    const cancelUpdate = () => {
        setName("");
        setEmail("");
        setGender("");
        setStatus("");
        setUserToBeUpdated(null);
        setUpdateForm(false);

    }


    return (
        <div>
            <div className="card">
                <div className="card-header">
                    <h1>Users List</h1>
                </div>
                <div className="card-body">
                    <table className="table table-hover table-dark">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Name</th>
                                <th scope="col">Email</th>
                                <th scope="col">Gender</th>
                                <th scope="col">Status</th>
                                <th scope="col">Update</th>
                                <th scope="col">Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            {userList && userList.map((u, index) => (
                                <tr key={u.id}>
                                    <th scope="row">{index + 1}</th>
                                    <td>{u.name}</td>
                                    <td>{u.email}</td>
                                    <td>{u.gender}</td>
                                    <td>{u.status}</td>
                                    <td><button type="button" className="btn btn-warning" onClick={() => { setUserToBeUpdated(u); setUpdateForm(true); }}>Update</button></td>
                                    <td><button type="button" className="btn btn-danger" onClick={() => { deleteUser(u.id) }}>Delete</button></td>
                                </tr>

                            ))
                            }


                        </tbody>
                    </table>

                    {/* for some space */}
                    <br /><br /><br /><br />
                    {/* Update form only shown when update button is clicked */}
                    {updateForm && <div className="card">
                        <div className="card-header">
                            <h1>Update User {userToBeUpdated?.id}</h1>
                        </div>
                        <div className="card-body">
                            {/* Input fields */}
                            <div className="input-group mb-3">
                                <span className="input-group-text" id="inputGroup-sizing-default">Name:</span>
                                <input type="text" className="form-control" value={name} aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" onChange={($e) => { setName($e.target.value) }} />
                            </div>
                            <div className="input-group mb-3">
                                <span className="input-group-text" id="inputGroup-sizing-default">Email:</span>
                                <input type="text" className="form-control" value={email} aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" onChange={($e) => { setEmail($e.target.value) }} />
                            </div>
                            <select value={gender} onChange={($e) => { setGender($e.target.value) }}>
                                <option value="">Select Gender</option>
                                <option value="male">Male</option>
                                <option value="female">Female</option>

                            </select>
                            <select value={status} onChange={($e) => { setStatus($e.target.value) }}>
                                <option value="">Select status</option>
                                <option value="active">active</option>
                                <option value="inactive">inactive</option>

                            </select>
                        </div>
                        <div className="card-footer">
                            <button type="button" className="btn btn-success me-2" onClick={() => { updateUser() }}>Save Updated User</button>
                            <button type="button" className="btn btn-danger ms-2" onClick={() => { cancelUpdate() }}>Cancel</button>
                        </div>
                    </div>

                    }


                </div>


                <br /><br /><br />


                {/* By clicking go to add user component */}
                <div className="card-footer">
                    <Link to="/add">
                        <button type="button" className="btn btn-success">Add User</button>
                    </Link>
                </div>
            </div>
        </div>
    )

}
export default User;