import { useState } from "react";
import axios from "axios";
function LogIn() {

    let [username, setUsername] = useState("")
    let [password, setPassword] = useState("")
    let [msg, setMsg] = useState("")
    const processLogin = async () => {

        const encodedString = window.btoa(username + ':' + password)
        try {
            const response = await axios.get('http://localhost:8080/api/user/token/v1',
                {
                    headers: { "Authorization": "Basic " + encodedString }
                }
            )
            console.log(response)
            setMsg("LogIn success!!!");
        }
        catch (err) {
            setMsg("Invalid login credentials!!!");
        }

    }
    return (
        <div className="container">
            <div className="row">
                <br /><br /><br /><br />

            </div>
            <div className="row">
                <div className="col-md-4"></div>
                <div className="col-md-4">
                    <div className="card">
                        <div className="card-header">
                            <h6 className="display-6"> Log in</h6>
                        </div>
                        <div className="card-body">
                            {msg !== "" ? <div>
                                <div className="alert alert-info" >
                                    {msg}
                                </div>
                            </div> : ""}
                            <div>
                                <label >Username:</label>

                                <input type="text" className="form-control" onChange={($e) => { setUsername($e.target.value) }} />
                            </div>
                            <br />
                            <div>
                                <label >Password:</label>

                                <input type="text" className="form-control" onChange={($e) => { setPassword($e.target.value) }} />
                            </div>
                            <div>
                                <button className="btn btn-primary" onClick={() => processLogin()} style={{ marginTop: 6 }}>Sign in</button>
                            </div>
                        </div>
                        <div className="card-footer">
                            <label>Don't have a account? </label>
                            <a href="#">Sign Up</a>
                            <label>here</label>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    )
}

export default LogIn;