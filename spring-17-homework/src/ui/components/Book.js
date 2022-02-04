import React from 'react';
import {Link} from 'react-router-dom';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default class Book extends React.Component {
    constructor(props) {
        super(props);
        this.state = {id: "",
                      name: "",
                      authors: [],
                      genres: [],
                      authorsString: "",
                      genresString: "",
                      commentaries: []
        };

        this.bookChange = this.bookChange.bind(this);
        this.bookUpdate = this.bookUpdate.bind(this);
    };

    componentDidMount() {
        this.getBookInfo(this.props.match.params.id);
    };

    bookChange(event) {
        this.setState({[event.target.name]: event.target.value});
    };

    async bookUpdate(event) {
        event.preventDefault();
        let book = {id: this.state.id,
                    name: this.state.name,
                    authors: this.state.authors,
                    genres: this.state.genres
        };
        this.updateBookName(book);
    };

    getBookInfo(bookId){
        Promise.all([
                    fetch('/api/books/' + bookId),
                    fetch('/api/bookcomments/' + bookId)
                ])
                .then(([book, comms]) => Promise.all([book.json(), comms.json()]))
                .then(([book, comms]) => this.setState({id: book.id,
                                                        name: book.name,
                                                        authors: book.authors,
                                                        genres: book.genres,
                                                        authorsString: book.authorsString,
                                                        genresString: book.genresString,
                                                        commentaries: comms}));
    };

    async updateBookName(book){
        await fetch('/api/books/',{
                method: 'PUT',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(book)
        });
        this.props.history.push('/');
    };

    async deleteBook(book){
        await fetch('/api/books',{
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(book)
        });
        this.props.history.push('/');
    };

    async deleteCommentary(commentary){
        await fetch('/api/comments',{
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: commentary.id
        });

        let commentaries = this.state.commentaries;
        let find = commentaries.findIndex(comm => comm.id === commentary.id);

        if(find > -1) {
            commentaries.splice(find, 1)
        };

        this.setState({commentaries});
        this.props.history.push('/bookedit/' + commentary.book.id);
    };

    render() {
            return (
                <React.Fragment>
                    <Header title={'Book Info'}/>
                    <form onSubmit={this.bookUpdate}>
                        <div>
                            <label for="id-input">ID:</label>
                            <input id="id-input" readOnly type="text" value={this.state.id}/>
                        </div>
                        <div>
                            <label for="name-input">Name:</label>
                            <input id="name-input" name="name" type="text" value={this.state.name} onChange={this.bookChange}/>
                        </div>
                        <div>
                            <label for="authors-input">Authors:</label>
                            <input id="authors-input" readOnly name="authors" type="text" value={this.state.authorsString}/>
                        </div>
                        <div>
                            <label for="genres-input">Genres:</label>
                            <input id="genres-input" readOnly name="genres" type="text" value={this.state.genresString}/>
                        </div>
                        <button type="submit">Save</button>
                    </form>
                    <br/>
                    <button type="button" onClick={() => this.deleteBook(this.state)}>Delete</button>
                    <br/>
                    <Header title={'Commentaries'}/>
                    <table>
                        <tbody>
                        {
                            this.state.commentaries.map((commentary, i) => (
                                <tr key={i}>
                                    <td>
                                        <Link to={"/commedit/" + commentary.id}>{commentary.text}</Link>
                                    </td>
                                    <td>
                                        <button type="button" onClick={() => this.deleteCommentary(commentary)}>Delete</button>
                                    </td>
                                </tr>
                            ))
                        }
                        </tbody>
                    </table>
                    <br/>
                    <Link to={"/commadd/" + this.state.id}>
                         <button type="button">Add Commentary</button>
                     </Link>
                </React.Fragment>);
    }
}