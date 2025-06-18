import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import HomeScreen from './components/HomeScreen'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import SignIn from './components/SignIn'
import SignUp from './components/SignUp'
import CustomerDashboard from './components/Customer/CustomerDashboard'
import Profile from './components/Profile'

function App() {


  return (

    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeScreen />}></Route>
        <Route path="/signIn" element={<SignIn />}></Route>
        <Route path="/signUp" element={<SignUp />}></Route>

        <Route path="/customer" element={<CustomerDashboard />}>
          <Route index element={<Profile />}></Route>
        </Route>

        
      </Routes>
    </BrowserRouter>
  )
}

export default App
