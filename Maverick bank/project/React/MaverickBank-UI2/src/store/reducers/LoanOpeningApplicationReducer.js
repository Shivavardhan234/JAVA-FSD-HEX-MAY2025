
const initialState ={
    application:{}
}



const loanOpeningApplicationReducer=(state = initialState, action)=>{

    if(action.type === "GET_LOAN_OPENING_APPLICATION_BY_ID"){
        
        return{
            ...state,
            application:action.payload
        }
    }
    return state;

}

export default loanOpeningApplicationReducer;