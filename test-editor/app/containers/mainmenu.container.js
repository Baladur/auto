import React from 'react'
import MainMenu from '../components/mainmenu.component'

class MainMenuContainer extends React.Component {
    constructor(props) {
        super(props)
        this.state = {

        }
    }

    render() {
        const mainMenu = require('electron').remote.getGlobal('mainMenuCaptions')//this.loadMainMenuCaptions()
        return (
            <div>
                <MainMenu
                    fileCaption={mainMenu.file}
                    viewCaption={mainMenu.view}
                    repoCaption={mainMenu.repo}
                    browserCaption={mainMenu.browser}
                    helpCaption={mainMenu.help}
                    fileCreateCaption={mainMenu.fileCreate}
                    fileOpenCaption={mainMenu.fileOpen}
                    saveCurrentCaption={mainMenu.saveCurrent}
                    saveAllCaption={mainMenu.saveAll}
                    createProjectCaption={mainMenu.createProject}
                    createTestCaption={mainMenu.createTest}
                    createBindingCaption={mainMenu.createBinding}
                    createElementCaption={mainMenu.createElement}
                    openProjectCaption={mainMenu.openProject}
                    openTestCaption={mainMenu.openTest}
                    openBindingCaption={mainMenu.openBinding}
                    openElementCaption={mainMenu.openElement}
                    onFileSave={this.handleFileSave}
                    onFileSaveAll={this.handleFileSaveAll}
                />
            </div>
        )
    }

    handleFileSave() {
        console.log("Saved file")
    }

    handleFileSaveAll() {
        console.log("Saved all files")
    }
}

export default MainMenuContainer