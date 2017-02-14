import React from 'react'

class Code extends React.Component {
    render() {
        return (
            <div className="editor-layout">
                <div className="editor">{this.props.children}</div>
            </div>

        )
    }
}
/*<iframe id={this.props.id} src="test.html" style={{display: this.props.visible ? 'block' : 'none'}} width="100%" height="800px" frameBorder="0" className="editor-layout" scrolling="no"/>*/
export default Code