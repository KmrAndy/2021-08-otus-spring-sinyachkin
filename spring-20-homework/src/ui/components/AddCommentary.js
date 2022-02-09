import React from 'react';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default class AddCommentary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {book: "",
                      text: ""
        };

        this.fieldChange = this.fieldChange.bind(this);
        this.commentaryAdd = this.commentaryAdd.bind(this);
    };

    componentDidMount() {
        this.getBook(this.props.match.params.bookId);
    };

    getBook(id){
        fetch('/api/books/' + id)
            .then(response => response.json())
            .then(book => this.setState({book}));
    }

    fieldChange(event) {
        this.setState({[event.target.name]: event.target.value});
    };

    async commentaryAdd(event) {
        event.preventDefault();
        let commentary = {book: {id: this.state.book.id,
                                 name: this.state.book.name,
                                 authors: this.state.book.authors,
                                 genres: this.state.book.genres},
                          text: this.state.text};

        this.AddCommentary(commentary);
    };

    async AddCommentary(commentary){
        await fetch('/api/comments',{
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(commentary)
        });
        this.props.history.push('/bookedit/' + commentary.book.id);
    };

    render() {
            return (
                <React.Fragment>
                    <Header title={'Commentary Info'}/>
                    <form onSubmit={this.commentaryAdd}>
                        <div>
                            <label for="book-input">Book:</label>
                            <input id="book-input" readOnly name="book" type="text" value={this.state.book.name}/>
                        </div>
                        <div>
                            <label for="text-input">Text:</label>
                            <input id="text-input" name="text" type="text" value={this.state.text} onChange={this.fieldChange}/>
                        </div>
                        <button type="submit">Add</button>
                    </form>
                    <br/>
                    <button type="button" onClick={() => this.props.history.push('/bookedit/' + this.state.book.id)}>Back</button>
                </React.Fragment>);
    }
}