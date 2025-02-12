import { useNavigate } from "react-router-dom"

export default function MovieHero({ movie }) {
    const navigate = useNavigate()
    
    return(
        <div className="movieHero__wrapper" style={{ backgroundImage: `url('${movie.image}')` }}>
            <div className="movieHero__content container">
                <h1>{ movie.title }</h1>
                <p>{ movie.description }</p>
                <button className="button" onClick={() => navigate(`/movie/player/${movie._id}`)}>Watch Now</button>
            </div>
        </div>
    )
}

