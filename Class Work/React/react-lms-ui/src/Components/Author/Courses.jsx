import axios from "axios";
import { useEffect, useState } from "react";


function Courses() {


    let [courseList, setCourseList] = useState([])
    useEffect(() => {
        const getAllCourses = async () => {
            try {
                // console.log(localStorage.getItem('token'));
                const response = await axios.get("http://localhost:8080/api/course/get/by-author", {
                    headers: { "Authorization": "Bearer " + localStorage.getItem('token') }
                })
                // console.log(response);
                setCourseList(response.data);
            } catch (err) {
                console.log(err);
            }
        }
        getAllCourses();
    }, [])
    return (
        <div>
            <h1>
                Author courses list
            </h1>
            <hr />
            <div className="container-fluid">
                <div className="row">
                    {
                        courseList.map((c) => (
                            <div className="col-md-4 mb-2" key={c.id}>
                                <div className="card" style={{ height: 500 }} >
                                      
                                    <div className="card-header">
                                        Title: {c.title}
                                    </div>
                                    <div className="card-body"style={{alignContent:'center'}}>
                                    <img className="card-img-top" style={{ height: '14rem'}} src={`/Images/${c.imagePath}`} alt={c.title} />
                                    </div>
                                    <div >
                                        <br />
                                        <label style={{padding:15, paddingBottom:0}}>Description:{c.description}</label>
                                        <h6 className="card-title" style={{padding:15, paddingBottom:0}}>Credits: {c.credits}</h6>

                                        
                                    </div>
                                    <div className="card-footer">
                                        <a href="#" className="btn btn-primary">View Author</a> &nbsp;&nbsp;
                                    </div>

                                </div>
                            </div>
                        ))
                    }

                </div>
            </div>
        </div>
    );
}
export default Courses;