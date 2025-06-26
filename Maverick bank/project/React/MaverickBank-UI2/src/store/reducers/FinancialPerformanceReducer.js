
const initialState ={
    report:{}
}



const financialPerformanceReportReducer=(state = initialState, action)=>{

    if(action.type === "GET_BY_ID"){
        
        return{
            ...state,
            report:action.payload
        }
    }
    return state;

}

export default financialPerformanceReportReducer;