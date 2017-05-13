import React from 'react'
import Autosuggest from 'react-autosuggest'
import theme from '../../public/css/autosuggest-style.css'

class MainInput extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '',
            suggestions: this.props.getSuggestions('')
        };
    }

    render() {
        let { value, suggestions } = this.state;
        const inputProps = {
            value,
            onChange: this.onChange.bind(this),
            autoFocus: true
        };
        if (suggestions.length == 0) {
            suggestions = this.props.getSuggestions("");
        }
        //console.log(`going to render following suggestions:`);
        //suggestions.forEach(s => console.log(`\t[${s.word}]`));
        return (
            <Autosuggest
                theme={theme}
                suggestions={suggestions}
                onSuggestionsFetchRequested={this.onSuggestionsFetchRequested.bind(this)}
                onSuggestionsClearRequested={this.onSuggestionsClearRequested.bind(this)}
				onSuggestionSelected={this.props.onSuggestionSelected}
                getSuggestionValue={this.getSuggestionValue.bind(this)}
                renderSuggestion={this.renderSuggestion.bind(this)}
                shouldRenderSuggestions={this.shouldRenderSuggestions.bind(this)}
				pasteSuggestion={this.props.pasteSuggestion}
                inputProps={inputProps}/>
        )
    }

    componentDidMount() {
		//console.log(`componentDidMount()`);
        this.setState({
            suggestions: this.props.getSuggestions("")
        });
    }

    onChange(event, { newValue }) {
		//
        console.log(`onChange()`);
        console.log(newValue);
        this.setState({
            value: newValue
        });
    }

    onSuggestionsFetchRequested({ value }) {
		console.log(`onSuggestionsFetchRequested with value [${value}]`);
		const hints = this.props.getSuggestions(value);
        console.log(`hints with size ${hints.length}:`);
		hints.forEach(hint => console.log(`\t[${hint.word}]`));
        this.setState({
            suggestions: hints
        });
        console.log(`suggestions updated to:`);
        this.state.suggestions.forEach(s => console.log(`\t[${s.word}]`));
    }

    onSuggestionsClearRequested() {
		console.log(`onSuggestionsClearRequested()`);
        // this.setState({
        //     suggestions: []
        // });
    }

    shouldRenderSuggestions(value) {
        return true;
    }

    getSuggestionValue(suggestion) { return suggestion.word; }

    renderSuggestion(suggestion) {
        return (
            <div><b>{suggestion.word}</b></div>
        );
    }
}

export default MainInput