
const initialState ={
    report:{}
}



const regulatoryReportReducer=(state = initialState, action)=>{

    if(action.type === "GET_BY_ID"){
        
        return{
            ...state,
            report:action.payload
        }
    }
    return state;

}

export default regulatoryReportReducer;