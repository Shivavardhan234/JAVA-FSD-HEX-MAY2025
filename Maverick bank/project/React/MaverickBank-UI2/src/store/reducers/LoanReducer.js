
const loanReducerInitialState ={
    loan:{}
}



const loanReducer=(state = loanReducerInitialState, action)=>{

    if(action.type === "GET_LOAN_BY_ID"){
        
        return{
            ...state,
            loan:action.payload
        }
    }
    return state;

}

export default loanReducer;