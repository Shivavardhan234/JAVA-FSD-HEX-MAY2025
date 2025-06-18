import axios from "axios"

export const fetchAllCourses=(dispatch)=>()=>{

axios.get("http://localhost:8080/api/course/get/all-by-pagenation").then(function(response){
    console.log(response.data);
    dispatch({
        'payload':response.data,
        'type':'FETCH_ALL_COURSES'
    })
})

}