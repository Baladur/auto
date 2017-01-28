import React from 'react'

class Tab extends React.Component {
    render() {
        return (
            <div className="tab-item">
                {this.props.name}
                <span className="icon icon-cancel icon-close-tab" onClick={this.props.close} />
            </div>
        )
    }
}

export default Tab