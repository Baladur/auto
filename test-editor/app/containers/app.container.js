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
            codeFrames : [<CodeLogic key="1" id="1" projectName="kinopoisk" name="Test1"/>, <CodeLogic key="2" id="2" projectName="kinopoisk" name="Test2"/>]
        }
        Tabs.setUseDefaultStyles(false);
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
                        <TabList className="tab-list" activeTabClassName="tab-selected">
							{this.state.codeFrames.map(codeFrame => <Tab className="tab-item">{codeFrame.props.name}</Tab>)}
                        </TabList>
                        {
                            this.state.codeFrames.map(codeFrame =>
                            <TabPanel className="editor-layout" key={codeFrame.key}>
                                {codeFrame}
                            </TabPanel>)
                        }
                    </Tabs>
                </Content>
            </div>
        )
    }

    openNewTab() {
		const size = this.state.codeFrames.length;
        this.setState({
			codeFrames: this.state.codeFrames.concat([<CodeLogic key={size+1} id={size+1} projectName="kinopoisk" name="TestN"/>])
		});
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