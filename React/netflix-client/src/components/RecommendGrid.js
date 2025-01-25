import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import MovieHero from "./MovieHero"

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
            .then(response => {
                if (response.ok)
                    return response.json()
            })
            .then(data => {
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

        </>
    )
}