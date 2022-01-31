import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from 'react-router-dom';

import Book from './components/Book';
import AddBook from './components/AddBook';
import BookList from './components/BookList';
import Commentary from './components/Commentary';
import AddCommentary from './components/AddCommentary';

const Header = (props) => (
    <h1>{props.title}</h1>
);

export default function App() {
    return (<React.Fragment>
                <Router>
                    <Link to="/addbook">Add Book</Link>
                    {" "}
                    <Link to="/">Book List</Link>
                    <Switch>
                        <Route path="/bookedit/:id" exact component={Book}/>
                        <Route path="/addbook" exact component={AddBook}/>
                        <Route path="/commedit/:id" exact component={Commentary}/>
                        <Route path="/commadd/:bookId" exact component={AddCommentary}/>
                        <Route path="/" exact component={BookList}/>
                    </Switch>
                </Router>
            </React.Fragment>
    );
};
