import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import bankAccountReducer from "./reducers/BankAccountReducer";
import loanReducer from "./reducers/LoanReducer";





const store = configureStore({
    reducer:{
        user:UserReducer,
        bankAccount:bankAccountReducer,
        loanStore:loanReducer
    }
})


export default store;