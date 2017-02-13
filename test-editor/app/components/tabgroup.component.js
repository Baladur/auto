import React from 'react'
import TabPlus from './tabplus.component'

class TabGroup extends React.Component {
    render() {
        return (
            <div id="tabs" className="tab-group" style={{border: 'none'}}>
                {this.props.children}
            </div>
        )
    }
}

export default TabGroup