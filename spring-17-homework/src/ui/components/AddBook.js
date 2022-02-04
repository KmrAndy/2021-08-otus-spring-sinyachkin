import React from 'react';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default class AddBook extends React.Component {
    constructor(props) {
        super(props);
        this.state = {name: "",
                      authors: [],
                      genres: [],
                      selectedAuthors: [],
                      selectedGenres: []
        };
        this.fieldChange = this.fieldChange.bind(this);
        this.authorsChange = this.authorsChange.bind(this);
        this.genresChange = this.genresChange.bind(this);
        this.bookAdd = this.bookAdd.bind(this);
    };

    componentDidMount() {
        this.getAllAuthors();
        this.getAllGenres();
    };

    getAllAuthors(){
        fetch('/api/authors')
            .then(response => response.json())
            .then(authors => this.setState({authors}));
    }

    getAllGenres(){
            fetch('/api/genres')
                .then(response => response.json())
                .then(genres => this.setState({genres}));
    }

    fieldChange(event) {
        this.setState({[event.target.name]: event.target.value});
    };

    authorsChange(id) {
        let selectedAuthors = this.state.selectedAuthors;
        let find = selectedAuthors.findIndex(author => author.id === id)

        if(find > -1) {
            selectedAuthors.splice(find, 1)
        } else {
            selectedAuthors.push(this.state.authors.find(author => author.id === id))
        }

        this.setState({selectedAuthors})
    };

    genresChange(id) {
        let selectedGenres = this.state.selectedGenres;
        let find = selectedGenres.findIndex(genre => genre.id === id)

        if(find > -1) {
            selectedGenres.splice(find, 1)
        } else {
            selectedGenres.push(this.state.genres.find(genre => genre.id === id))
        }

        this.setState({selectedGenres})
    };

    async bookAdd(event) {
        event.preventDefault();
        let book = {name: this.state.name,
                    authors: this.state.selectedAuthors,
                    genres: this.state.selectedGenres
        };

        this.AddBook(book);
    };

    async AddBook(book){
        await fetch('/api/books',{
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(book)
        });
        this.props.history.push('/');
    };

    render() {
            return (
                <React.Fragment>
                    <Header title={'Book Info'}/>
                    <form onSubmit={this.bookAdd}>
                        <div>
                            <label for="name-input">Name:</label>
                            <input id="name-input" name="name" type="text" value={this.state.name} onChange={this.fieldChange}/>
                        </div>
                        <p>Authors:</p>
                        <div>
                            {this.state.authors.map(author => (
                                <li>
                                    <label>
                                        <input type="checkbox"
                                               onChange={() => this.authorsChange(author.id)}
                                               selectedAuthors={this.state.selectedAuthors.includes(author.id)}/>
                                        {author.fullName}
                                    </label>
                                </li>
                                ))
                            }
                        </div>
                        <p>Genres:</p>
                        <div>
                            {this.state.genres.map(genre => (
                                <li>
                                    <label>
                                        <input type="checkbox"
                                               onChange={() => this.genresChange(genre.id)}
                                               selectedGenres={this.state.selectedGenres.includes(genre.id)}/>
                                        {genre.name}
                                    </label>
                                </li>
                                ))
                            }
                        </div>
                        <button type="submit">Add</button>
                    </form>
                    <br/>
                    <button type="button" onClick={() => this.props.history.push('/bookedit/' + this.state.book.id)}>Back</button>
                </React.Fragment>);
    }
}