import React from 'react'
import Code from '../components/code.component'
import LineNumberColumn from '../components/linenumber-column.component'
import LineNumber from '../components/linenumber.component'
import Line from '../components/line.component'
import Tick from '../components/tick.component'
import MainInput from '../components/maininput.component'
import ClassNames from 'classnames'
import EditorKeys from '../editor/keys'
import {stateManager} from '../editor/statemanager'

class CodeLogic extends React.Component {
    constructor(props) {
        super(props);
        this.state = stateManager.getState(this.props.projectName, this.props.name);
        this.context = this.state.context;
		this.awesomplete = {};
		this.input = {};
        console.log("initial state of codelogic:");
        console.log(this.state);
    }

    render() {
        return (
            <div tabIndex="0" onKeyDown={this.handleKeyDown.bind(this)} id={"code-" + this.props.id}>
                <Code>
                    <LineNumberColumn>
                        {
                            this.state.lines.map((line, index) =>
                            <LineNumber key={index+1}>
                                {[index+1,
                                this.state.done && index+1 == this.state.currentLine && <Tick key="1"/>]}
                            </LineNumber>
                            )
                        }
                    </LineNumberColumn>
					<div className="written">
                    {
                        this.state.lines.map((line, index) =>

                                <Line lineId={line.id} key={index+1}>
                                {
                                    [line.words.map(word =>
                                        <span className={ClassNames({
                                            'ordinary' : true
                                        })}>{word + ' '}</span>
                                    ),
                                    this.state.currentLine == index+1
                                    && <MainInput
                                            key="1"
                                            ref="mainInput"
                                            suggestions={this.state.context.currentHints}
                                            getSuggestions={this.getSuggestions.bind(this)}/>]
                                }
                                </Line>
                        )
                    }
					</div>
                </Code>
            </div>
        )
    }

    /*****HANDLERS*****/

    componentDidMount() {
        // this.input = document.getElementById('mainInput');
        // this.awesomplete = new Awesomplete(this.input, {
        //     autoFirst : true,
        //     minChars : "0",
        //     maxItems : 99999,
        //     filter : CodeLogic.filter,
        //     list : [],
        //     replace : this.replace
        // });
        // document.body.addEventListener("awesomplete-close", function(e){
        //     // The popup just closed.
        //     this.context.forward();
        //     this.awesomplete.list = this.context.currentHints;
        //     if (this.context.current != Context.END) {
        //         this.awesomplete.open();
        //     }
        // }, false);
        // this.mainInputWrapper = document.querySelector('div.awesomplete');
        // // this.newLine();
        // this.awesomplete.list = this.context.currentHints;
        // this.awesomplete.open();
    }

    // componentWillReceiveProps( nextProps ) {
    //     const { keydown: { event } } = nextProps;
    //     if ( event ) {
    //         this.setState( { key: event.which } );
    //         console.log(this.state.key);
    //     }
    // }

    handleKeyDown(event) {
        const keyCode = event.keyCode ? event.keyCode : event.which;
        //this.excludeWrongCharacters(event);
        switch (keyCode) {
            case EditorKeys.NEW_LINE : this.handleNewLineEvent(); break;
            case EditorKeys.DELETE : this.handleDeleteEvent(); break;
            default: console.log(keyCode); break;
        }
    }

    handleNewLineEvent() {
        if (this.state.done) {
            this.setState({
                lines: this.state.lines.concat([{
                    id: this.state.lineCount+1,
                    words: [""]
                }]),
                lineCount: this.state.lineCount+1,
                currentLine: this.state.currentLine+1,
                done: false
            });
        }
    }

    handleDeleteEvent() {

    }

    excludeWrongCharacters(event) {
        if (this.state.context.shouldExcludeCharacters) {
            let inputChar = String.fromCharCode(event.keyCode ? event.keyCode : event.which);
            if (inputChar >= 'A' && inputChar <= 'Z') {
                inputChar = inputChar.toLowerCase();
            }
            if (this.state.context.allowedCharacters.indexOf(inputChar) < 0// && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 ))
                || (this.getVisibleHints().count == 0)) {
                event.preventDefault();
            }

        }
    }

    /*****UTILITY FUNCTIONS*****/

    static filter(hint, input) {
        return hint.toLowerCase().indexOf(input.toLowerCase()) > -1;
    }

    replace(text) {
        if (this.input.value.length != 0) {
            this.input.value = text;
        }
    }

    getVisibleHints() {
        // let mc = 0;
        // let mh = "";
        // this.state.context.currentHints.forEach(hint => {
        //     console.log(`hint: ${hint}, input:`);
        //     console.log(this.state.lines[this.state.currentLine-1].words);
        //     if (CodeLogic.filter(hint, this.state.lines[this.state.currentLine-1].words[0])) {
        //         mc = mc + 1;
        //         mh = hint;
        //     }
        // });
        let hints = this.refs.mainInput.state.suggestions.map(s => s.word);
        return {
            count : hints.length,
            hint : hints[0]
        };
    }

    getSuggestions(value) {
        const inputValue = value.trim().toLowerCase();
        const inputLength = inputValue.length;
        let allHints = this.state.context.currentHints;
        console.log(`get suggestions by value ${value}`);
        console.log(`returning:`);
        console.log((inputLength === 0 ? allHints : allHints.filter(hint =>
            hint.word.toLowerCase().slice(0, inputLength) === inputValue)));
        return (inputLength === 0 ? allHints : allHints.filter(hint =>
            hint.word.toLowerCase().slice(0, inputLength) === inputValue));
    }

    static getInitialData() {
        return {
            localVariables: [],
            globalVariables: [],
            types: ['string', 'int', 'double', 'boolean'],
            methods: ['$timeout']
        };
    }

    getClassnameByWord(word) {
        return 'ordinary';
    }
}

export default  CodeLogic