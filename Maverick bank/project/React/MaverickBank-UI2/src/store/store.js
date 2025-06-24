import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import bankAccountReducer from "./reducers/BankAccountReducer";





const store = configureStore({
    reducer:{
        user:UserReducer,
        bankAccount:bankAccountReducer
    }
})


export default store;