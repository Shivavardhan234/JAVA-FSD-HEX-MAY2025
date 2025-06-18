import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "../store/reducers/UserReducer"
import CourseReducer from "./reducers/CourseReducer";


const store= configureStore({
    reducer:{
        user:UserReducer,
        allCourses: CourseReducer
    }

})


export default store;