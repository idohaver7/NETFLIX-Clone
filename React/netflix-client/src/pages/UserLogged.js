import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"

import Header from "../components/Header"
import Footer from "../components/HomePage/Footer"
import CategoryRow from '../components/CategoryRow'
import MovieHeroVideo from "../components/MovieHeroVideo"

export default function UserLogged() {
    const navigate = useNavigate()
    const [categories, setCategories] = useState([])
    const [movies, setMovies] = useState([])
    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        if (!token)
            navigate('/login', { replace: true })
    }, [token])

    useEffect(() => {
        if (token) {
            fetch(`http://localhost:8080/api/movies/all/movies`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                  }
            })
            .then(response => response.json())
            .then(data => {
                setMovies(data)
                setLoading(false)
            })
        }
    }, [])

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

    const findCategoryMovies = (categoryId) => {
        const category_movies = movies.filter(movie => movie.category._id === categoryId)
        return category_movies
    }

    if (loading)
        return (
            <>
            <Header showButtons={false} token={token} />
                <div className="loading__wrapper">
                    <span class="loader"></span>
                </div>
            <Footer />
            </>
        )

    return(
        <>
            <Header showButtons={false} token={token} />

            {movies[0] && <MovieHeroVideo movie={movies[0]} />}
            {
                categories.map(category => {
                    return(<CategoryRow key={category.category} category={category} movies={findCategoryMovies(category._id)} />)
                })
            }
            <Footer />
        </>
    )
}