import { useState } from "react";
import { users } from "../Sample_Data/Users";
function UserTable() {

  let [userList, setUserList] = useState(users);

  return (


    <div>
      <table className="table table-dark table-striped">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Name</th>
            <th scope="col">Username</th>
            <th scope="col">Email</th>
            <th scope="col">City</th>
            <th scope="col">Company name</th>
          </tr>
        </thead>
        <tbody>
          {
            userList.map((u, index) => (
              <tr key={u.id}>
                <th scope="row">{index + 1}</th>
                <td>{u.name}</td>
                <td>{u.username}</td>
                <td>{u.email}</td>
                <td>{u.address.city}</td>
                <td>{u.company.name}</td>
              </tr>
            ))
          }

        </tbody>
      </table>

    </div>
  )


}
export default UserTable;