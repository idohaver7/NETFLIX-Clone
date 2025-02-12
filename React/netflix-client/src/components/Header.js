import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import SearchResults from "../pages/SearchResults";

export default function Header({showButtons, token }) {
    const navigate = useNavigate();
    const [user, setUser] = useState({})
    const [categories, setCategories] = useState([])
    const [loading, setLoading] = useState(true)
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        console.log(user)
    }, [user])
    
    useEffect(() => {
        if (token) {
            fetch(`http://localhost:8080/api/categories`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                  }
            })
            .then(response => response.json())
            .then(data => {
                setCategories(data)
                setLoading(false)
            })
        }
    }, [])

    useEffect(() => {
        if (token) {
            fetch(`http://localhost:8080/api/users`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                  }
            })
            .then(response => response.json())
            .then(data => {
                setUser(data)
            })
        }
    }, [])

    useEffect(() => {
        if (searchTerm) {
            navigate(`/search?q=${encodeURIComponent(searchTerm)}`);
        }
    }, [searchTerm, navigate]);

    const handleChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const toggleTheme = () => {
        if (document.body.getAttribute('data-theme'))
            document.body.removeAttribute('data-theme')
        else
            document.body.setAttribute('data-theme', 'light')
    }

    if (loading)
        return <></>
        
    return(<>
        <div className="header__wrapper">
            <div className="header container">
                <div className="header__logo">
                    <Link to="/">
                        <img src="../../assets/logo.png" alt="Netflix Logo" />
                    </Link>
                </div>
                {
                    token && (
                        <div className="navbar">
                            {
                                categories.slice(0, 7).map(category => {
                                    return(<a href={`#${category.name}`} className="navbar__link">{category.name}</a>)
                                })
                            }
                        </div>
                    )
                }
                <div className="header__buttons">
                    { showButtons && <Link to="/login" className="button header__button">Sign In</Link>}
                    { token && (
                        <>
                            <div className="input__field">
                                <svg clip-rule="evenodd" fill-rule="evenodd" strokeLinejoin="round" strokeMiterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m15.97 17.031c-1.479 1.238-3.384 1.985-5.461 1.985-4.697 0-8.509-3.812-8.509-8.508s3.812-8.508 8.509-8.508c4.695 0 8.508 3.812 8.508 8.508 0 2.078-.747 3.984-1.985 5.461l4.749 4.75c.146.146.219.338.219.531 0 .587-.537.75-.75.75-.192 0-.384-.073-.531-.22zm-5.461-13.53c-3.868 0-7.007 3.14-7.007 7.007s3.139 7.007 7.007 7.007c3.866 0 7.007-3.14 7.007-7.007s-3.141-7.007-7.007-7.007z" fillRule="nonzero"/></svg>
                                <input className="search" value={searchTerm} onChange={handleChange} type="text" placeholder="Search movies ..."/>
                            </div>
                            {
                                user.isManager && (
                                    <Link to="/admin"><img className="userImage" src={user.profilePicture} /></Link>
                                )
                            }
                            {
                                !user.isManager && (
                                    <img className="userImage" src={user.profilePicture} />
                                )
                            }
                        </>
                    )}

                    <label class="switch">
                        <input type="checkbox" id="theme-switch" onChange={toggleTheme} />
                        <span class="slider round"></span>
                    </label>
                </div>
            </div>
        </div>

        {
            searchTerm !== '' && (
                <div className="searchModal">
                    <div className="container">
                        <SearchResults token={token} />
                    </div>
                </div>
            )
        }
        </>
    )
}