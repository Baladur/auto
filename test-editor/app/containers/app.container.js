import React from 'react'
import Header from '../components/header.component'
import TabGroup from '../components/tabgroup.component'
import TabPlus from '../components/tabplus.component'
import Tab from '../components/tab.component'
import Code from '../components/code.component'


class AppContainer extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            tabs : [],
            tabIndex : 0
        }
    }
    render() {
        return (
            <div>

                <Header/>
                <TabPlus
                    openNewTab={this.openNewTab.bind(this)} />
                <TabGroup>
                    {
                        this.state.tabs.map(item =>
                            <Tab
                                key={item.id}
                                name={item.name}
                                close={this.close.bind(this)}
                            />)
                    }
                </TabGroup>
                <Code width={screen.width}/>
            </div>
        )
    }

    openNewTab() {
        this.setState({
            tabIndex : this.state.tabIndex+1
            })
        this.setState({
            tabs: this.state.tabs.concat([{
                id : this.state.tabIndex,
                name : 'Tab '
            }])
        })
        console.log(this.state)

    }

    close(event) {
        console.log(event)
        var _tabs = this.state.tabs
        _tabs.splice(this.state.tabs.indexOf(event.target.parentNode), 1)
        this.setState({
            tabIndex : this.state.tabIndex-1,
            tabs: _tabs
        })
    }
}

export default AppContainer