import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
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
import FindAccountOpeningApplication from './components/employee/account_manager/FindAccountOpeningApplication.jsx';
import AccountOpeningApplicationByCategory from './components/employee/account_manager/AccountOpeningApplicationByCategory.jsx';
import MyAccountsSidebar from './components/customer/my_accounts/MyAccountsSidebar.jsx';
import DefaultMyAccountScreen from './components/customer/my_accounts/DefaultMyAccountScreen.jsx';
import ManageBankAccount from './components/customer/my_accounts/ManageBankAccount.jsx';
import DepositMoney from './components/customer/my_accounts/DepositMoney.jsx';
import WithdrawMoney from './components/customer/my_accounts/WithdrawMoney.jsx';
import TransferMoney from './components/customer/my_accounts/TransferMoney.jsx';
import MyTransactions from './components/customer/my_accounts/MyTransactions.jsx';
import AccountStatement from './components/customer/my_accounts/AccountStatement.jsx';



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


        {/* Customer */}
        <Route path='/customer' element={<CustomerNavBar />}>
          <Route index element={<CustomerProfile />}></Route>


          {/* My Accounts */}
          <Route path='myAccounts' element={<MyAccountsSidebar/>}>
          <Route path='defaultScreen' element={<DefaultMyAccountScreen/>}></Route>
          <Route path='manageBankAccount/:accountNumber' element={<ManageBankAccount/>}></Route>
          <Route path='deposit' element={<DepositMoney/>}></Route>
          <Route path='withdraw' element={<WithdrawMoney/>}></Route>
          <Route path='transferMoney' element={<TransferMoney/>}></Route>
          <Route path='myTransactions' element={<MyTransactions/>}></Route>
          <Route path='statement' element={<AccountStatement/>}></Route>

          </Route>

          {/* Account Opening Application Management */}
          <Route path='accountApplications' element={<AccountOpeningApplicationManagementSidebar />}>
            <Route path='fillAccountOpeningApplication' element={<FillAccountApplication />}></Route>
            <Route path = 'jointAccountRequest' element={<JointAccountRequests/>}></Route>
            <Route index element={<MyAccountOpeningApplications/>}></Route>
          </Route>
        </Route>

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


        
        {/* Account manager */}
        <Route path='/accountManager' element={<AccountManagerNavbar/>}>
        <Route path='accountManagerProfile' element={<EmployeeProfile/>}></Route>
        <Route path='viewBranchForAccountManager' element={<ViewBranchForEmployee/>}></Route>

        {/* Accounts Dashboard */}
        <Route path='bankAccountsSideBar' element={<AccountsDashboardSidebar/>}>
        <Route path='findAccountApplication' element={<FindAccountOpeningApplication/>}></Route>
        <Route path='accountApplicationByCategory' element={<AccountOpeningApplicationByCategory/>}></Route>
        </Route>
        </Route>





      </Routes>

    </BrowserRouter>

  )
}

export default App
