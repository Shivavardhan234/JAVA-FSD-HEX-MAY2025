
import Concepts from "./Components/Concepts";
import Tasks from "./Components/Tasks";
import TodoTable from "./Components/TodoTable";
import UserTable from "./Components/Usertable";
import Posts from "./Components/Posts";
import CourseList from "./Components/CourseList";
import LogIn from "./Components/Login";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import AuthorDashboard from "./Components/Author/AuthorDashboard";
import Stats from "./Components/Author/Stats";
import Courses from "./Components/Author/Courses";
import Enrollments from "./Components/Author/Enrollments";
import Profile from "./Components/Author/Profile";
function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<LogIn />}> </Route>
        <Route path="/author" element={<AuthorDashboard />}>
          <Route index element={<Stats/>}></Route>
          <Route path="Courses" element={<Courses/>}></Route>
          <Route path="Enrollments" element={<Enrollments/>}></Route>
          <Route path="profile" element={<Profile/>}></Route>
        </Route>
        <Route path="/learner" element={<CourseList />}> </Route>

      </Routes>


    </BrowserRouter>
  );
}

export default App;
