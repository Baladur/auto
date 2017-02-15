import React from 'react'

class MainInput extends React.Component {
    render() {
        return (
            <input id="mainInput" className="awesomplete" onInput={this.props.handleInput}/>
        )
    }
}

export default MainInput