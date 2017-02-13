import React from 'react'
import Header from '../components/header.component'
// import TabGroup from '../components/tabgroup.component'
// import TabPlus from '../components/tabplus.component'
// import Tab from '../components/tab.component'
import { Tabs, Tab, TabList, TabPanel} from 'react-tabs'
import CodeLogic from '../containers/code.container'
import MainMenuContainer from './mainmenu.container'
import Content from './content.container'
import Sidebar from './sidebar.container'


class AppContainer extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            tabs: [{
                id: 1,
                name: 'Test1'
            }],
            tabIndex: 2,
            active: 1,
            codeFrames : [<CodeLogic key="1" id="1" projectName="kinopoisk"/>, <CodeLogic key="2" id="2" projectName="nothing"/>]
        }
        //Tabs.setUseDefaultStyles(false);
    }
    render() {

        return (
            <div>
                <Header title="Test editor"/>
                <MainMenuContainer
                    onFileOpenTest={this.openNewTab.bind(this)}/>
                <Content>
                    <Sidebar/>
                    <Tabs>
                        <TabList>
                            <Tab>Default tab</Tab>
                            <Tab>Default tab 2</Tab>
                        </TabList>
                        {
                            this.state.codeFrames.map(codeFrame =>
                            <TabPanel key={codeFrame.key}>
                                {codeFrame}
                            </TabPanel>)
                        }
                    </Tabs>
                </Content>
            </div>
        )
    }

    openNewTab() {
        this.setState({
            tabIndex : this.state.tabIndex+1
            })
        this.setState({
            tabs: this.state.tabs.concat([{
                key: this.state.tabIndex,
                id : this.state.tabIndex,
                name : 'Tab ' + this.state.tabIndex
            }])
        })
        this.state.codeFrames.every(codeFrame => codeFrame.visible = false)
        var newCode = <Code key={this.state.tabIndex} id={"code-" + this.state.tabIndex}/>
        newCode.visible = true
        this.setState({
            codeFrames: this.state.codeFrames.concat([newCode]),
            active: this.state.tabIndex
        })
        console.log(this.state)

    }

    close(event) {
        console.log(event)
        var _tabs = this.state.tabs
        var indexToRemove = this.state.tabs.indexOf(event.target.parentNode)
        if (this.state.tabs[indexToRemove] === this.state.tabs.find(item => item.id == 'code-' + this.state.active)) {
            this.setState({
                active: indexToRemove == 0 ? _tabs[_tabs.length-1].id : _tabs[indexToRemove-1].id
            })
        }
        _tabs.splice(indexToRemove, 1)
        this.setState({
            tabs: _tabs
        })
    }
}

export default AppContainer