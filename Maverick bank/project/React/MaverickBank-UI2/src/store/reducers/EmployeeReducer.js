
const initialState ={
    employee:{}
}



const employeeReducer=(state = initialState, action)=>{

    if(action.type === "GET_EMPLOYEE_BY_ID"){
        
        return{
            ...state,
            employee:action.payload
        }
    }
    return state;

}

export default employeeReducer;