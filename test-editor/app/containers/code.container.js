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
		this.stateId = { projectName: this.props.projectName, 
						name: this.props.name };
        this.state = stateManager.getState(this.stateId);
        this.context = this.state.context;
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
                                this.state.context.done && index+1 == this.state.currentLine && <Tick key="1"/>]}
                            </LineNumber>
                            )
                        }
                    </LineNumberColumn>
					<div className="written">
                    {
                        this.state.lines.map((line, index) =>

                                <Line lineId={line.id} 
									  key={index+1}
									  selectLine={this.selectLine.bind(this)}>
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
                                            getSuggestions={this.getSuggestions.bind(this)}
											onSuggestionSelected={this.onSuggestionSelected.bind(this)}
											pasteSuggestion={this.pasteSuggestion.bind(this)}/>]
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

    }

    handleKeyDown(event) {
        const keyCode = event.keyCode ? event.keyCode : event.which;
        //this.excludeWrongCharacters(event);

        switch (keyCode) {
            case EditorKeys.NEW_LINE : this.handleNewLineEvent(); break;
            case EditorKeys.DELETE : this.handleDeleteEvent(); break;
            //TODO: handle space
            default: this.handleKey(event); break;
        }

    }

    handleNewLineEvent() {
        if (this.state.context.done) {
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
        console.log(`handleDeleteEvent()`);
        if (this.refs.mainInput.state.value.trim().length > 0) {
            let value = this.refs.mainInput.state.value;
            value.toArray().splice(-1, 1);
            this.refs.mainInput.setState({
                value: value
            });
            return;
        }
        let lines = this.state.lines;
        let words = lines[this.state.currentLine-1].words;
        if (words.length > 0) {
            words.splice(-1, 1);
            lines[this.state.currentLine-1].words = words;
            this.setState({
                lines: lines
            });
            console.log(`deleted last word!`)
        }
    }

    /**
     * This method handles last input character.
     */
    handleKey(event) {
        let inputChar = String.fromCharCode(event.keyCode ? event.keyCode : event.which);
        if (inputChar >= 'A' && inputChar <= 'Z') {
            inputChar = inputChar.toLowerCase();
        }
        if (this.state.context.shouldExcludeCharacters) {
            if (this.state.context.allowedCharacters.indexOf(inputChar) < 0) {
                event.preventDefault();
                return;
            }
        } else {
            //handle cases when we are in expression or variable declaration
        }
        //mainInput contains old value yet, so pass old value + input character to context and try to move pointer
        if (this.state.context.currentHints.length == 1) {
            console.log("one hint");
            this.pasteSuggestion(this.state.context.currentHints[0]);
            console.log('current input: ' + this.state.context.input);
        }

        this.state.context.updateInput(this.refs.mainInput.state.value + inputChar);
        this.state.context.forward();
    }

    excludeWrongCharacters(event) {
        console.log(`excludeWrongCharacters()`);
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

	onSuggestionSelected(event, { suggestion, suggestionValue, suggestionIndex, sectionIndex, method }) {
		console.log(`onSuggestionSelected with suggestion [${suggestion.word}], method [${method}]`);
		this.pasteSuggestion(suggestion);
		console.log(`state updated:`);
		console.log(this.state);

	}

	selectLine(index) {
		this.setState({
			currentLine: index
		});
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
        console.log(`getSuggestions by value [${value}]`);
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

	pasteSuggestion(suggestion) {
		let line = this.state.lines[this.state.currentLine-1];
		line.words.push(suggestion.word + ' '); //check space requirement
        this.state.context.updateInput(suggestion.word);
		let lines = this.state.lines;
		lines[this.state.currentLine-1].words = line.words;
		this.setState({
			lines: lines
		});
		this.refs.mainInput.setState({
			value: ''
		});
		stateManager.putState(this.stateId, this.state);
	}
}

export default CodeLogic