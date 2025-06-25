import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import bankAccountReducer from "./reducers/BankAccountReducer";
import loanReducer from "./reducers/LoanReducer";
import loanClosureReducer from "./reducers/LoanClosureReducer";
import loanOpeningApplicationReducer from "./reducers/LoanOpeningApplicationReducer";
import accountRequestReducer from "./reducers/AccountRequestReducer";
import branchReducer from "./reducers/BranchReducer";



const store = configureStore({
    reducer:{
        user:UserReducer,
        bankAccount:bankAccountReducer,
        loanStore:loanReducer,
        loanClosureStore: loanClosureReducer,
        loanOpeningApplicationStore: loanOpeningApplicationReducer,
        accountRequestStore: accountRequestReducer,
        branchStore: branchReducer
    }
})


export default store;