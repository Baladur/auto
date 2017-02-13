import React from 'react'

class Line extends React.Component {
    render() {
        return (
            <div id={this.props.lineId}>
                {
                    this.props.children
                }
            </div>
        )
    }
}

export default Line