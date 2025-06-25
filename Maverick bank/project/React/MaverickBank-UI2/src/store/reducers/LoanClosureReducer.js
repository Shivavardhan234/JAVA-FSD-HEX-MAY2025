
const loanClosureReducerInitialState ={
    loanClosureRequest:{}
}



const loanClosureReducer=(state = loanClosureReducerInitialState, action)=>{

    if(action.type === "GET_LOAN_CLOSURE_REQUEST_BY_ID"){
        
        return{
            ...state,
            loanClosureRequest:action.payload
        }
    }
    return state;

}

export default loanClosureReducer;