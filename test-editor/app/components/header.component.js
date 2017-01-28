import React from 'react'

class Header extends React.Component {
    render() {
        return (
            <header className="toolbar toolbar-header draggable" >
                <h1 className="title">Test editor</h1>
            </header>
        )
    }
}

export default Header