import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import bankAccountReducer from "./reducers/BankAccountReducer";
import loanReducer from "./reducers/LoanReducer";
import loanClosureReducer from "./reducers/LoanClosureReducer";
import loanOpeningApplicationReducer from "./reducers/LoanOpeningApplicationReducer";
import accountRequestReducer from "./reducers/AccountRequestReducer";
import branchReducer from "./reducers/BranchReducer";
import financialPerformanceReportReducer from "./reducers/FinancialPerformanceReducer";
import regulatoryReportReducer from "./reducers/RegulatoryReportReducer";
import employeeReducer from "./reducers/EmployeeReducer";
import customerReducer from "./reducers/CustomerReducer";
import cioReducer from "./reducers/CIOReducer";


const store = configureStore({
    reducer:{
        user:UserReducer,
        bankAccount:bankAccountReducer,
        loanStore:loanReducer,
        loanClosureStore: loanClosureReducer,
        loanOpeningApplicationStore: loanOpeningApplicationReducer,
        accountRequestStore: accountRequestReducer,
        branchStore: branchReducer,
        financialPerformanceStore: financialPerformanceReportReducer,
        regulatoryReportStore: regulatoryReportReducer,
        customerStore: customerReducer,
        employeeStore: employeeReducer,
        cioStore: cioReducer

    }
})


export default store;