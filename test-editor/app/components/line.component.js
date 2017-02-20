import React from 'react'

class Line extends React.Component {
    render() {
        return (
            <div id={this.props.lineId} onClick={this.handleClick.bind(this)}>
                {
                    this.props.children
                }
            </div>
        )
    }
	
	handleClick(event) {
		this.props.selectLine(this.props.lineId);
	}
}

export default Line