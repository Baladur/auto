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

class CodeLogic extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            elementsJson: FileUtils.loadElementsJson(this.props.projectName),
            lines: [{
                id: 1,
                words: ['fill', '#SEARCH']
            }],
            currentLine: 1,
            done: false,
            lineCount: 1
        };
        this.context = new Context(this.state.elementsJson, CodeLogic.getInitialData());
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
                            <LineNumber>
                                {[index,
                                this.state.done && index == this.state.currentLine && <Tick/>]}
                            </LineNumber>
                            )
                        }
                    </LineNumberColumn>
                    {
                        this.state.lines.map(line =>

                                <Line lineId={line.id}>
                                {
                                    [line.words.map(word =>
                                        <span className={this.getClassnameByWord(word)}>{word + ' '}</span>
                                    ),
                                    this.state.currentLine == line.id && <MainInput ref="mainInput" handleInput={this.handleInput.bind(this)}/>]
                                }
                                </Line>

                        )
                    }
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
        document.body.addEventListener("awesomplete-close", function(e){
            // The popup just closed.
            this.context.forward();
            this.awesomplete.list = this.context.currentHints;
            if (this.context.current != Context.END) {
                this.awesomplete.open();
            }
        }, false);
        this.mainInputWrapper = document.querySelector('div.awesomplete');
        // this.newLine();
        this.awesomplete.list = this.context.currentHints;
        this.awesomplete.open();
    }

    handleInput() {

    }

    excludeWrongCharacters(event) {
        if (this.context.shouldExcludeCharacters) {
            let inputChar = String.fromCharCode(event.keyCode ? event.keyCode : event.which);
            if (inputChar >= 'A' && inputChar <= 'Z') {
                inputChar = inputChar.toLowerCase();
            }
            if ((this.context.allowedCharacters.indexOf(inputChar) < 0 && !((window.input.value.length == 0 || window.input.value.match(/^\d+$/) != undefined) && inputChar >= 1 && inputChar <= 9 ))
                || (this.getVisibleHints().count == 0 && window.awesomplete.filter == CodeLogic.filter)) {
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