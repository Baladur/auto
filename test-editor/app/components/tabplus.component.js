import React from 'react'

class TabPlus extends React.Component {
    render() {
        return (
            <div id="tabPlus" className="tab-item tab-item-fixed" onClick={this.props.openNewTab}>
                <span className="icon icon-plus"/>
            </div>
        )
    }
}

export default TabPlus