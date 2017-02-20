import React from 'react'
import Autosuggest from 'react-autosuggest'
import theme from '../../public/css/autosuggest-style.css'

class MainInput extends React.Component {
    constructor() {
        super();
        this.state = {
            value: '',
            suggestions: []
        };
    }

    render() {
        const { value, suggestions } = this.state;
        const inputProps = {
            value,
            onChange: this.onChange.bind(this),
            autoFocus: true
        };
        return (
            <Autosuggest
                theme={theme}
                suggestions={this.props.suggestions}
                onSuggestionsFetchRequested={this.onSuggestionsFetchRequested.bind(this)}
                onSuggestionsUpdateRequested={this.onSuggestionsUpdateRequested.bind(this)}
                onSuggestionsClearRequested={this.onSuggestionsClearRequested.bind(this)}
                getSuggestionValue={this.getSuggestionValue.bind(this)}
                renderSuggestion={this.renderSuggestion.bind(this)}
                shouldRenderSuggestions={this.shouldRenderSuggestions.bind(this)}
                inputProps={inputProps}/>
        )
    }

    componentDidMount() {
        this.setState({
            suggestions: this.props.getSuggestions("")
        });
    }

    onChange(event, { newValue }) {
        this.setState({
            value: newValue
        });
    }

    onSuggestionsFetchRequested({ value }) {
        console.log(`on fetch requested:`);
        console.log(this.props.getSuggestions(value));
        this.setState({
            suggestions: this.props.getSuggestions(value)
        });
        console.log(`suggestions updated to:`);
        console.log(this.state.suggestions);
    }

    onSuggestionsUpdateRequested({ value }) {
        console.log(`on fetch requested:`);
        console.log(this.props.getSuggestions(value));
        this.setState({
            suggestions: this.props.getSuggestions(value)
        });
        console.log(`suggestions updated to:`);
        console.log(this.state.suggestions);
    }



    onSuggestionsClearRequested() {
        this.setState({
            suggestions: []
        });
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