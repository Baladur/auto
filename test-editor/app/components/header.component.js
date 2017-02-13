import React from 'react'

class Header extends React.Component {
    render() {
        return (
            <header className="header draggable" >
                {this.props.title}
            </header>
        )
    }
}

export default Header