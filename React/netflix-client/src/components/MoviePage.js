import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import MovieHero from "./MovieHero"
import RecommendedGrid from "./RecommendGrid"

export default function MoviePlayer() {
    const navigate = useNavigate()
    const { id } = useParams()
    const [loading, setLoading] = useState(true)
    const [movie, setMovie] = useState({})
    const [recommendMovies, setRecommendMovies] = useState([])
    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    useEffect(() => {
        if (!token)
            navigate('/login', { replace: true })
    })

    useEffect(() => {
        fetch(`http://localhost:8080/api/movies/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
              }
        })
        .then(response => response.json())
        .then(data => {
            setMovie(data)
            setLoading(false)
        })
    }, [])

    if (loading)
        return(
            <div className="fullLoading__wrapper">
                <span class="loader"></span>
            </div>
        )

    return(
        <div className="movie__modal">
            <div className="close__modal" onClick={() => navigate('/home')}>X</div>
            <MovieHero movie={movie} />
            <div className="recommendMovies__wrapper container">
                <h3>Recommended Movies</h3>
                <RecommendedGrid id={movie._id} token={token} />
            </div>
        </div>
    )
}