import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';
import Book from './Book';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default class BookList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {books: []};
    }

    componentDidMount() {
        this.getAllBooks();
    }

    getAllBooks(){
        fetch('/api/booklist')
            .then(response => response.json())
            .then(books => this.setState({books}));
    }

    render() {
        return (
            <React.Fragment>
                <Header title={'Books'}/>
                <table border="1">
                    <thead>
                    <tr>
                        <th>Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.books.map((book, i) => (
                            <tr key={i}>
                                <td>
                                    <Link to={"bookedit/" + book.id}>{book.name}</Link>
                                </td>
                            </tr>
                        ))
                    }
                    </tbody>
                </table>
            </React.Fragment>);
    }
};