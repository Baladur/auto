import React from 'react'

class MainMenu extends React.Component {
    render() {
        return (
            <ul className="main-menu">
                <li id="menu-file" onClick={this.props.onFile}>
                    {this.props.fileCaption}
                    <ul className="sub-menu">
                        <li id="menu-file-create" onClick={this.props.onFileCreate}>
                            {this.props.fileCreateCaption}
                            <ul>
                                <li id="menu-file-create-project">{this.props.createProjectCaption}</li>
                                <li id="menu-file-create-test">{this.props.createTestCaption}</li>
                                <li id="menu-file-create-binding">{this.props.createBindingCaption}</li>
                                <li id="menu-file-create-element">{this.props.createElementCaption}</li>
                            </ul>
                        </li>
                        <li id="menu-file-open" onClick={this.props.onFileOpen}>
                            {this.props.fileOpenCaption}
                            <ul>
                                <li id="menu-file-open-project">{this.props.openProjectCaption}</li>
                                <li id="menu-file-open-test" onClick={this.props.onFileOpenTest}>{this.props.openTestCaption}</li>
                                <li id="menu-file-open-binding">{this.props.openBindingCaption}</li>
                                <li id="menu-file-open-element">{this.props.openElementCaption}</li>
                            </ul>
                        </li>
                        <li id="menu-file-save" onClick={this.props.onFileSave}>{this.props.saveCurrentCaption}</li>
                        <li id="menu-file-save-all" onClick={this.props.onFileSaveAll}>{this.props.saveAllCaption}</li>
                    </ul>
                </li>
                <li id="menu-view">{this.props.viewCaption}</li>
                <li id="menu-repo">{this.props.repoCaption}</li>
                <li id="menu-browser">{this.props.browserCaption}</li>
                <li id="menu-help">{this.props.helpCaption}</li>
            </ul>
        )
    }
}

export default MainMenu