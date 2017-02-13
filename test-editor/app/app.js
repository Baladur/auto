import React from 'react'
import ReactDOM from 'react-dom'
import AppContainer from './containers/app.container'
import Sequence from './editor/sequence'

class App extends React.Component {
    render() {
        return (
            <div>
                <AppContainer/>
            </div>
        )
    }
}

ReactDOM.render(<App/>, document.getElementById('content'))