import { useState } from "react";
function Concepts(){
  let [count,setCount] = useState(0);
  let[name,setName]=useState("");
  let[showWelcome,setShowWelcome]=useState(false);
  let[iplTeams,setIplTeams]=useState(["CSK","MI","SRH"])

  
  
  return (
    
      <div>
        
      <h1 style={{color:'blue'}}>count = {count}</h1>
      <button onClick={()=>{setCount(count+1)}} style={{color:'green'}}>Increment</button>
      <button onClick={()=>{setCount(count-1)}} style={{color:'red'}}>Decrement</button>
      <br/>
      <br/>
      <hr/>
      <hr/>
      <label>Enter your name</label>

      <input type="text" onChange={($event)=>{setName($event.target.value)}}></input>
      <br/>
      <button onClick={()=>{setShowWelcome(true)}}>Submit</button>

      {showWelcome == true? <h2>Welcome {name}</h2>:""}

      <br/>
      <hr/>
        <br />
        <label>Array of ipl teams</label>
        <br/>
        {
        iplTeams.map((e)=>(
            <li key={e}>{e}<br/></li>
        ))
        }
     </div>
   
  )
}

export default Concepts;