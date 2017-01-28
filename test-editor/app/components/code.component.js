import React from 'react'

class Code extends React.Component {
    render() {
        return (
            <div className="window-content">


                <iframe src="test.html" style={{border:'none', width: '100%', height: '1000px'}} width={this.props.width} />
            </div>
        )
    }
}

export default Code