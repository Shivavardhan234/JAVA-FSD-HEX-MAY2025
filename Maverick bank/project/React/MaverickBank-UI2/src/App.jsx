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
import CustomerDashboard from './components/customer/CustomerDashboard';
import CioDashboard from './components/cio/CioDashboard';
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
        <Route path='/customer' element={<CustomerDashboard />}></Route>

        {/* CIO */}
        <Route path='/cio' element={<CioNavbar />}>
          <Route path='' element={<CustomerAccountsSideBar />}></Route>
          <Route path='employeeAccounts' element={<EmployeeAccountsSideBar />}></Route>
          <Route path='cioAccounts' element={<CioAccountsSideBar />}></Route>


          {/* Branch */}
          <Route path='cioBranch' element={<CioBranchSideBar />}>
            <Route path='' element={<FindBranch />}></Route>
            <Route path='add' element={<AddBranch/>}></Route>
            <Route path='category' element={<BranchByCategory/>}></Route>
            <Route path='branchDetails' element={<BranchDetails/>}></Route>
            
          </Route>


          <Route path="profile" element={<Profile />}></Route>
        </Route>





      </Routes>

    </BrowserRouter>

  )
}

export default App
