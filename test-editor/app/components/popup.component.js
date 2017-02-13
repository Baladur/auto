import React from 'react'

class PopupWindow extends React.Component {
    render() {
        return (
            <div className="popup-window">
                {this.props.children}
            </div>
        )
    }
}

export default PopupWindow