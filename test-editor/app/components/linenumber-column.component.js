import React from 'react'

class LineNumberColumn extends React.Component {
    render() {
        return (
            <div className="linenumber-column">{this.props.children}</div>
        )
    }
}

export default LineNumberColumn