import React from 'react'

class LineNumber extends React.Component {
    render() {
        return (
            <div className="linenumber">{this.props.children}</div>
        )
    }
}

export default LineNumber