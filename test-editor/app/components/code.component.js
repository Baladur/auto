import React from 'react'

class Code extends React.Component {
    render() {
        return (
            <div className="editor-layout">
                <div id="elementsJsonScript"></div>
                <div className="editor">
                    <div id="linenumbers" ref="lineNumbers"></div>
                    <div id="written" ref="written">
                        <input id="mainInput" className="awesomplete" ref="mainInput" onInput={this.props.handleInput}></input>
                    </div>
                </div>
            </div>

        )
    }
}
/*<iframe id={this.props.id} src="test.html" style={{display: this.props.visible ? 'block' : 'none'}} width="100%" height="800px" frameBorder="0" className="editor-layout" scrolling="no"/>*/
export default Code