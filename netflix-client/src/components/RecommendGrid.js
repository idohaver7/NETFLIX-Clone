import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import MovieCard from "./MovieCard"

export default function RecommendedGrid({ token, id }) {
    const navigate = useNavigate()
    const [loading, setLoading] = useState(true)
    const [recommendMovies, setRecommendMovies] = useState([])

    useEffect(() => {
        try {
            fetch(`http://localhost:8080/api/movies/${id}/recommend`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                  }
            })
            .then(response => response.json())
            .then(data => {
                console.log(data)
                setRecommendMovies(data)
                if (data)
                    setLoading(false)
            })
        } catch (err) {
            console.log(err)
        }
    }, [])

    if (loading)
        return(
            <div className="loading__wrapper">
                <span class="loader"></span>
            </div>
        )

    return(
        <>
            <div className="movies__recommend_grid">
                {
                    recommendMovies.map(movie => {
                        return(<MovieCard key={movie._id} movie={movie} isCompact={true} />)
                    })
                }
            </div>
        </>
    )
}