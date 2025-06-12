import axios from "axios";
import { useEffect, useState } from "react";

function CourseList(){

let[courseList,setCourseList]=useState([])
useEffect(()=>{
    const getAllCourses = async ()=>{
        const response= await axios.get("http://localhost:8080/api/course/get/all-by-pagenation")
        // console.log(response);
        setCourseList(response.data);
    }
    getAllCourses();
},[])
    return(
        <div className="container">
            <div className="row">
                <h1 className="display-5">ALL COURSES</h1>
                {
                    courseList.map((c)=>(
                         <div className="col-md-12 mb-2" key={c.id}>
                            <div className="card" >
                                <div className="card-header">
                                    Title: {c.title}
                                </div>
                                 <div className="card-body">
                                    <h5 className="card-title"> {c.credits}</h5>
                                    
                                    <a href="#" className="btn btn-primary">View Author</a> &nbsp;&nbsp;
                                </div>
                                 
                                
                            </div>
                        </div>
                    ))
                }

            </div>
        </div>
    )
}

export default CourseList;