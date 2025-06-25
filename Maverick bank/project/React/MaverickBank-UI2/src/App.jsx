
import './App.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import HomeScreen from './components/HomeScreen';
import SignIn from './components/SignIn';
import SignUp from './components/SignUp';
import HomeScreenNavBar from './components/HomeScreenNavBar';
import 'bootstrap-icons/font/bootstrap-icons.css';

import CioNavbar from './components/cio/CioNavbar';
import Profile from './components/cio/profile';
import CustomerAccountsSideBar from './components/cio/customer_accounts/CustomerAccountsSideBar';
import EmployeeAccountsSideBar from './components/cio/employee_accounts/EmployeeAccountsSideBar';
import CioAccountsSideBar from './components/cio/cio_accounts/CioAccountsSideBar';
import CioBranchSideBar from './components/cio/branch/CioBranchSideBar';
import FindBranch from './components/cio/branch/FindBranch';
import AddBranch from './components/cio/branch/AddBranch';
import BranchByCategory from './components/cio/branch/BranchByCategory';
import BranchDetails from './components/cio/branch/BranchDetails';
import CustomersByCategory from './components/cio/customer_accounts/CustomersByCategory.jsx';
import FindCustomer from './components/cio/customer_accounts/FindCustomer.jsx';
import AddCio from './components/cio/cio_accounts/AddCio.jsx';
import FindCio from './components/cio/cio_accounts/FindCio.jsx';
import CioByCategory from './components/cio/cio_accounts/CioByCategory.jsx';
import ViewCio from './components/cio/cio_accounts/ViewCio.jsx';
import AddEmployee from './components/cio/employee_accounts/AddEmployee.jsx';
import FindEmployee from './components/cio/employee_accounts/FindEmployee.jsx';
import EmployeesByCategory from './components/cio/employee_accounts/EmployeesByCategory.jsx';
import ViewEmployee from './components/cio/employee_accounts/ViewEmployee.jsx';
import CustomerNavBar from './components/customer/CustomerNavBar.jsx';
import ViewCustomer from './components/cio/customer_accounts/ViewCustomer.jsx';
import CustomerProfile from './components/customer/CustomerProfile.jsx';
import AccountOpeningApplicationManagementSidebar from './components/customer/account-opening-application-management/AccountOpeningApplicationManagementSidebar.jsx';
import FillAccountApplication from './components/customer/account-opening-application-management/FillAccountApplication.jsx';
import MyAccountOpeningApplications from './components/customer/account-opening-application-management/MyAccountOpeningApplications.jsx';
import JointAccountRequests from './components/customer/account-opening-application-management/JointAccountRequests.jsx';
import AccountManagerNavbar from './components/employee/account_manager/AccountManagerNavbar.jsx';
import EmployeeProfile from './components/employee/EmployeeProfile.jsx';
import ViewBranchForEmployee from './components/employee/account_manager/ViewBranchForEmployee.jsx';
import AccountsDashboardSidebar from './components/employee/account_manager/AccountsDashboardSideber.jsx';
import FindAccountOpeningApplication from './components/employee/account_manager/account_opening_application/FindAccountOpeningApplication.jsx';
import AccountOpeningApplicationByCategory from './components/employee/account_manager/account_opening_application/AccountOpeningApplicationByCategory.jsx';
import MyAccountsSidebar from './components/customer/my_accounts/MyAccountsSidebar.jsx';
import DefaultMyAccountScreen from './components/customer/my_accounts/DefaultMyAccountScreen.jsx';
import ManageBankAccount from './components/customer/my_accounts/ManageBankAccount.jsx';
import DepositMoney from './components/customer/my_accounts/DepositMoney.jsx';
import WithdrawMoney from './components/customer/my_accounts/WithdrawMoney.jsx';
import TransferMoney from './components/customer/my_accounts/TransferMoney.jsx';
import MyTransactions from './components/customer/my_accounts/MyTransactions.jsx';
import AccountStatement from './components/customer/my_accounts/AccountStatement.jsx';
import CloseOrSuspendAccount from './components/customer/my_accounts/CloseOrSuspendAccount.jsx';
import MyLoans from './components/customer/my_accounts/loan/MyLoans.jsx';
import ApplyForLoan from './components/customer/my_accounts/loan/ApplyForLoan.jsx';
import MyLoanApplications from './components/customer/my_accounts/loan/MyLoanApplications.jsx';
import LoanOfficerNavbar from './components/employee/loan_officer/LoanOfficerNavbar.jsx';
import LoanManagementSidebar from './components/employee/loan_officer/LoanManagementSidebar.jsx';
import LoanOpeningApplications from './components/employee/loan_officer/loan_opening_applications/LoanOpeningApplications.jsx';
import ViewLoanOpeningApplication from './components/employee/loan_officer/loan_opening_applications/ViewLoanOpeningApplication.jsx';
import FindLoanOpeningApplication from './components/employee/loan_officer/loan_opening_applications/FindLoanApplication.jsx';
import FindLoan from './components/employee/loan_officer/loan/FindLoan.jsx';
import LoansByCategory from './components/employee/loan_officer/loan/LoansByCategory.jsx';
import ViewLoan from './components/employee/loan_officer/loan/ViewLoan.jsx';
import FindLoanPlan from './components/employee/loan_officer/loan_plan/FindLoanPlan.jsx';
import LoanPlansByCategory from './components/employee/loan_officer/loan_plan/LoanPlansByCategory.jsx';
import AddLoanPlan from './components/employee/loan_officer/loan_plan/AddLoanPlan.jsx';
import ManageLoan from './components/customer/my_accounts/loan/ManageLoan.jsx';
import LoanClosureRequest from './components/customer/my_accounts/loan/LoanClosureRequest.jsx';
import PayInstallment from './components/customer/my_accounts/loan/PayInstallment.jsx';
import FindLoanClosureRequest from './components/employee/loan_officer/loan_closure_request/FindLoanClosureRequest.jsx';
import LoanClosureRequests from './components/employee/loan_officer/loan_closure_request/LoanClosureRequests.jsx';
import ViewLoanClosureRequest from './components/employee/loan_officer/loan_closure_request/ViewLoanClosureRequest.jsx';
import FindBankAccount from './components/employee/account_manager/account/FindBankAccount.jsx';
import AccountsByCategory from './components/employee/account_manager/account/AccountsByCategory.jsx';
import ViewAccount from './components/employee/account_manager/account/ViewAccount.jsx';
import FindAccountRequest from './components/employee/account_manager/account_request/FindAccountRequest.jsx';
import AccountRequestsByCategory from './components/employee/account_manager/account_request/AccountRequestsByCategory.jsx';
import ViewAccountRequest from './components/employee/account_manager/account_request/ViewAccountRequest.jsx';
import TransactionAnalystNavbar from './components/employee/transaction_analyst/TransactionAnalystNavbar.jsx';
import TransactionsSidebar from './components/employee/transaction_analyst/TransactionsSidebar.jsx';
import FindTransaction from './components/employee/transaction_analyst/FindTransaction.jsx';
import TransactionsByCategory from './components/employee/transaction_analyst/TransactionsByCategory.jsx';
import TransactionsForAccount from './components/employee/transaction_analyst/TransactionsForAccount.jsx';



function App() {

  return (
    <BrowserRouter>
      <Routes>

        {/* Home screen */}
        <Route path='/' element={<HomeScreenNavBar />}>
          <Route path='home' element={<HomeScreen />}></Route>
          <Route path='signin' element={<SignIn />}></Route>
          <Route path='signup' element={<SignUp />}></Route>
        </Route>

        {/* ============================================================ CUSTOMER ==================================================================== */}
        {/* Customer */}
        <Route path='/customer' element={<CustomerNavBar />}>
          <Route index element={<CustomerProfile />}></Route>




          {/* My Accounts */}
          <Route path='myAccounts' element={<MyAccountsSidebar />}>
            <Route path='defaultScreen' element={<DefaultMyAccountScreen />}></Route>
            <Route path='manageBankAccount/:accountNumber' element={<ManageBankAccount />}></Route>
            <Route path='deposit' element={<DepositMoney />}></Route>
            <Route path='withdraw' element={<WithdrawMoney />}></Route>
            <Route path='transferMoney' element={<TransferMoney />}></Route>
            <Route path='myTransactions' element={<MyTransactions />}></Route>
            <Route path='statement' element={<AccountStatement />}></Route>
            <Route path='request' element={<CloseOrSuspendAccount />}></Route>

            {/* My Loans */}
            <Route path='myLoans' element={<MyLoans />}></Route>
            <Route path='applyLoan' element={<ApplyForLoan />}></Route>
            <Route path='myLoanApplications' element={<MyLoanApplications />}></Route>
            <Route path='manageLoan' element={<ManageLoan />}></Route>
            <Route path='loanClosure' element={<LoanClosureRequest />}></Route>
            <Route path='payInstallment' element={<PayInstallment />}></Route>

          </Route>

          {/* Account Opening Application Management */}
          <Route path='accountApplications' element={<AccountOpeningApplicationManagementSidebar />}>
            <Route path='fillAccountOpeningApplication' element={<FillAccountApplication />}></Route>
            <Route path='jointAccountRequest' element={<JointAccountRequests />}></Route>
            <Route index element={<MyAccountOpeningApplications />}></Route>
          </Route>
        </Route>
        {/* ====================================================== CIO / ADMIN ======================================================================= */}
        {/* CIO */}
        <Route path='/cio' element={<CioNavbar />}>

          {/* Customer Accounts */}
          <Route path='' element={<CustomerAccountsSideBar />}>
            <Route index element={<FindCustomer />}></Route>
            <Route path='byCategory' element={<CustomersByCategory />}></Route>
            <Route path='customerProfile' element={<ViewCustomer />}></Route>
          </Route>

          {/* Employee accounts */}
          <Route path='employeeAccounts' element={<EmployeeAccountsSideBar />}>
            <Route index element={<FindEmployee />} />
            <Route path="category" element={<EmployeesByCategory />} />
            <Route path="viewEmployee" element={<ViewEmployee />} />
            <Route path="addEmployee" element={<AddEmployee />} />
          </Route>

          {/* Cio accounts */}
          <Route path='cioAccounts' element={<CioAccountsSideBar />}>
            <Route index element={<FindCio />}></Route>
            <Route path='category' element={<CioByCategory />}></Route>
            <Route path='viewCio' element={<ViewCio />}></Route>
            <Route path='addCio' element={<AddCio />}></Route>
          </Route>


          {/* Branch */}
          <Route path='cioBranch' element={<CioBranchSideBar />}>
            <Route index element={<FindBranch />}></Route>
            <Route path='addBranch' element={<AddBranch />}></Route>
            <Route path='category' element={<BranchByCategory />}></Route>
            <Route path='branchDetails' element={<BranchDetails />}></Route>

          </Route>


          <Route path="profile" element={<Profile />}></Route>
        </Route>

{/* =============================================================== EMP PROFILR ============================================================ */}

        

        {/* ====================================================== ACCOUNT MANAGER =================================================================== */}

        {/* Account manager */}
        <Route path='/accountManager' element={<AccountManagerNavbar />}>
          <Route path='employeeProfile' element={<EmployeeProfile />}></Route>
          <Route path='viewBranchForAccountManager' element={<ViewBranchForEmployee />}></Route>

          {/* Accounts Dashboard */}
          <Route path='bankAccountsSideBar' element={<AccountsDashboardSidebar />}>
            {/* Account opening application */}
            <Route path='findAccountApplication' element={<FindAccountOpeningApplication />}></Route>
            <Route path='accountApplicationByCategory' element={<AccountOpeningApplicationByCategory />}></Route>

            {/* Bank Account */}
            <Route path='findAccount' element={<FindBankAccount />}></Route>
            <Route path='accountsByCategory' element={<AccountsByCategory />}></Route>
            <Route path='viewAccount' element={<ViewAccount />}></Route>

            <Route path='findAccountRequest' element={<FindAccountRequest />}></Route>
            <Route path='accountRequestsByCategory' element={<AccountRequestsByCategory />}></Route>
            <Route path='viewAccountRequest' element={<ViewAccountRequest />}></Route>
          </Route>
        </Route>

        {/* ====================================================== LOAN OFFICER ====================================================================== */}
        <Route path='/loanOfficer' element={<LoanOfficerNavbar />}>
          <Route index element={<EmployeeProfile />}></Route>
          <Route path='employeeProfile' element={<EmployeeProfile />}></Route>
          {/* loan Management sidebar */}
          <Route path='loanManagementSidebar' element={<LoanManagementSidebar />}>

            {/* loans */}
            <Route path='findLoan' element={<FindLoan />}></Route>
            <Route path='loansByCategory' element={<LoansByCategory />}></Route>
            <Route path='viewLoan' element={<ViewLoan />}></Route>
            {/* loan Opening Applications */}
            <Route path='loanOpeningApplications' element={<LoanOpeningApplications />}></Route>
            <Route path='viewLoanOpeningApplication' element={<ViewLoanOpeningApplication />}></Route>
            <Route path='findLoanApplication' element={<FindLoanOpeningApplication />}></Route>

            {/* Loan Plans */}
            <Route path='findLoanPlan' element={<FindLoanPlan />}></Route>
            <Route path='loanPlansByCategory' element={<LoanPlansByCategory />}></Route>
            <Route path='addLoanPlan' element={<AddLoanPlan />}></Route>

            {/* Loan Closure Requests */}
            <Route path='findLoanClosureRequest' element={<FindLoanClosureRequest />}></Route>
            <Route path='loanClosureRequests' element={<LoanClosureRequests />}></Route>
            <Route path='viewLoanClosure' element={<ViewLoanClosureRequest />}></Route>
          </Route>
        </Route>

        {/* =================================================== TRANSACTION ANALYST ========================================================== */}
        <Route path='/transactionAnalyst' element={<TransactionAnalystNavbar/>}>
        <Route path='employeeProfile' element={<EmployeeProfile />}></Route>

        <Route path='transactionSideBar' element={<TransactionsSidebar/>}>
        <Route path='findTransaction' element={<FindTransaction/>}></Route>
        <Route path='transactionsByCategory' element={<TransactionsByCategory/>}></Route>
        <Route path='transactionsForAccount' element={<TransactionsForAccount/>}></Route>
        </Route>
        
        </Route>




      </Routes>

    </BrowserRouter>

  )
}

export default App
