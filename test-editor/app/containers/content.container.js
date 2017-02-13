import React from 'react'
import Sidebar from './sidebar.container'
import Code from '../components/code.component'

class Content extends React.Component {
    render() {
        return (
            <div className="content-container">
                {this.props.children}
            </div>
        )
    }
}

export default Content