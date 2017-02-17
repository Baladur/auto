import React from 'react'
import Code from '../components/code.component'
import LineNumberColumn from '../components/linenumber-column.component'
import LineNumber from '../components/linenumber.component'
import Line from '../components/line.component'
import Tick from '../components/tick.component'
import MainInput from '../components/maininput.component'
import FileUtils from '../util/file-utils'
import Sequence from '../editor/sequence'
import Context from '../editor/context'
import Awesomplete from '../../public/js/awesomplete.min'
import ClassNames from 'classnames'
import {stateManager} from '../util/statemanager'
import keydown from 'react-keydown'

/*@keydown*/
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
            <div id={"code-" + this.props.id}>
                <Code handleInput={this.handleInput.bind(this)}>
                    <LineNumberColumn>
                        {
                            this.state.lines.map((line, index) =>
                            <LineNumber key={index+1}>
                                {[index+1,
                                this.state.done && index+1 == this.state.currentLine && <Tick/>]}
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
                                    this.state.currentLine == line.id && <MainInput key="1" ref="mainInput" handleInput={this.handleInput.bind(this)}/>]
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
        this.input = document.getElementById('mainInput');
        this.awesomplete = new Awesomplete(this.input, {
            autoFirst : true,
            minChars : "0",
            maxItems : 99999,
            filter : CodeLogic.filter,
            list : [],
            replace : this.replace
        });
        // document.body.addEventListener("awesomplete-close", function(e){
        //     // The popup just closed.
        //     this.context.forward();
        //     this.awesomplete.list = this.context.currentHints;
        //     if (this.context.current != Context.END) {
        //         this.awesomplete.open();
        //     }
        // }, false);
        this.mainInputWrapper = document.querySelector('div.awesomplete');
        // this.newLine();
        this.awesomplete.list = this.context.currentHints;
        this.awesomplete.open();
    }
	
	componentWillReceiveProps( nextProps ) {
		const { keydown: { event } } = nextProps;
		if ( event ) {
		  this.setState( { key: event.which } );
		  this.excludeWrongCharacters(event);
		}
  	}

    handleInput(event) {
        let lines = this.state.lines;
        lines[this.state.currentLine-1].words = [event.target.value];
        this.setState({
            lines: lines
        });
        console.log("input changed:");
        console.log(this.state.lines[this.state.currentLine-1].words);
        stateManager.putState(this.props.projectName, this.props.name, this.state);
    }

    excludeWrongCharacters(event) {
        if (this.state.context.shouldExcludeCharacters) {
            let inputChar = this.state.key;
            if (inputChar >= 'A' && inputChar <= 'Z') {
                inputChar = inputChar.toLowerCase();
            }
            if ((this.context.allowedCharacters.indexOf(inputChar) < 0 && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 ))
                || (this.getVisibleHints().count == 0 && this.awesomplete.filter == CodeLogic.filter)) {
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
        let mc = 0;
        let mh = "";
        this.context.currentHints.forEach(hint => {
            if (CodeLogic.filter(hint, this.input.value)) {
                mc = mc + 1;
                mh = hint;
            }
        });
        return {
            count : mc,
            hint : mh
        };
    }

    handleTickImg() {
        if (this.state.done) {
            this.tickImg.style.visibility = "visible";
        } else {
            this.tickImg.style.visibility = "hidden";
        }
}

    initTickImg() {
        if (this.tickImg == undefined) {
            this.tickImg = document.createElement('span');
            this.tickImg.setAttribute("class", "fa fa-check");
            //document.querySelector("div.awesomplete>div.awesomplete").appendChild(tickImg);
        }
        this.handleTickImg();
    }

    newLine() {
        this.setState({
            linesCount: this.state.linesCount + 1
        });
        //linenumber
        let lineNumber = document.createElement('div');
        lineNumber.setAttribute('class', 'linenumber');
        lineNumber.innerHTML = this.state.linesCount;
        lineNumber.appendChild(this.tickImg);
        this.refs.lineNumbers.getDOMNode().appendChild(lineNumber);
        //newline
        let div = document.createElement('div');
        div.setAttribute('id', this.linesCount);
        div.appendChild(this.mainInputWrapper);
        this.written.appendChild(div);
        //tickImg
        this.handleTickImg();
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

export default CodeLogic