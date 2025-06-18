import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchAllCourses } from "../../store/actions/CourseAction";

function Stats() {
    const dispatch=useDispatch();

   


    let courses= useSelector(state => state.allCourses.courses);


    return (
        <div>
            <h1>
                Author Stats
            </h1>
              

                {console.log(courses)}

                {
                    Array.isArray(courses) && courses.map((c, index) => (
                        <div key={index}>
                            <li>{c.title}</li>
                        </div>
                    ))
                }


            

        </div>
    );
}
export default Stats;