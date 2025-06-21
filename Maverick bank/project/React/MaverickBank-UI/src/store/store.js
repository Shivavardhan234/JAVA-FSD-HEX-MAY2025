import { configureStore } from "@reduxjs/toolkit";
import CustomerReducer from "./reducers/CustomerReducer";

const store = configureStore({
    reducer:{
        customer:CustomerReducer
    }
})
export default store;