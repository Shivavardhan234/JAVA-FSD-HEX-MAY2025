const initialState = {
    username: "",
    role: ""
}



/*
* here the reducer takes 2 parameters
* 1. State, which declares the object structure to be stored
* 1.1-> Initial state means a state from which it should start ,or the state which should stored as default
* 2. Action -> when api called from the api file, it will dispatch an object and that object is stored in the store
*/


const UserReducer = (state = initialState, action) => {
    if (action.type === "SET_USER_DETAILS") {
        let user = action.payload;
        return {
            ...state,
            username: user.username,
            role: user.role
        }
    }

    if (action.type === "DELETE_USER_DETAILS") {
        return {
            ...state,
            username: "",
            role: ""
        }
    }
    return state;
}
export default UserReducer;