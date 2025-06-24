import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import User from './components/user'
import AddUser from './components/AddUser'

function App() {
  const [count, setCount] = useState(0)

  return (
    <BrowserRouter>
    <Routes>
      <Route path='/' element={<User/>}></Route>
      <Route path='/add' element={<AddUser/>}></Route>
    </Routes>
    </BrowserRouter>

   
  )
}

export default App
