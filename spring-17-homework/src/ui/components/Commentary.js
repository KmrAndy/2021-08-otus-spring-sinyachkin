import React from 'react';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default class Commentary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {id: "",
                      book: "",
                      text: ""
        };

        this.textChange = this.textChange.bind(this);
        this.textUpdate = this.textUpdate.bind(this);
    };

    componentDidMount() {
        this.findCommentaryById(this.props.match.params.id);
    };

    textChange(event) {
        this.setState({[event.target.name]: event.target.value});
    };

    async textUpdate(event) {
        event.preventDefault();
        this.updateCommentaryText(this.state);
    };

    findCommentaryById(id){
        fetch('/api/comments/' + id)
            .then(response => response.json())
            .then(commentary => this.setState({id: commentary.id,
                                               book: commentary.book,
                                               text: commentary.text}));
    }

    async updateCommentaryText(commentary){
        await fetch('/api/comments/' + commentary.id + "/" + commentary.text,{
                method: 'PUT'
        });
        this.props.history.push('/bookedit/' + commentary.book.id);
    };

    render() {
            return (
                <React.Fragment>
                    <Header title={'Commentary Info'}/>
                    <form onSubmit={this.textUpdate}>
                        <div>
                            <label for="id-input">ID:</label>
                            <input id="id-input" readOnly type="text" value={this.state.id}/>
                        </div>
                        <div>
                            <label for="name-input">Book:</label>
                            <input id="name-input" readOnly name="name" type="text" value={this.state.book.name}/>
                        </div>
                        <div>
                            <label for="text-input">Text:</label>
                            <input id="text-input" name="text" type="text" value={this.state.text} onChange={this.textChange}/>
                        </div>
                        <button type="submit">Save</button>
                    </form>
                    <br/>
                    <button type="button" onClick={() => this.props.history.push('/bookedit/' + this.state.book.id)}>Back</button>
                </React.Fragment>);
    }
}