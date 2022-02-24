import { createContext, FC, useContext, useReducer } from 'react'
import { ActionTypes, ApplicationProps, IApplication, IApplicationAction } from './application'

const initialState: IApplication = {}

const reducer = (state: IApplication, action: IApplicationAction) => {
    switch (action.type) {
        case ActionTypes.UPDATE_ABOUT_YOU:
            return {
                ...state,
                aboutYou: action.payload,
            }
        default:
            return state
    }
}

const ApplicationContext = createContext<ApplicationProps>({
    state: initialState,
    dispatch: () => {},
})

const useApplicationContext = () => useContext(ApplicationContext)

const ApplicationProvider: FC = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState)

    return <ApplicationContext.Provider value={{ state, dispatch }}>{children}</ApplicationContext.Provider>
}

export { useApplicationContext, ApplicationProvider }
