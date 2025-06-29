
const initialState ={
    branch:{}
}



const branchReducer=(state = initialState, action)=>{

    if(action.type === "GET_BRANCH_BY_ID"){
        
        return{
            ...state,
            branch:action.payload
        }
    }
    return state;

}

export default branchReducer;