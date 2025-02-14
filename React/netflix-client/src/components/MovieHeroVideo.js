import { useNavigate } from "react-router-dom"

export default function MovieHeroVideo({ movie }) {
    const navigate = useNavigate()
    
    return (
        <div className="movieHero__wrapper">
            <video autoPlay loop muted className="background__video" src={`http://localhost:8080/video/${movie.video}`}>
                Your browser does not support the video tag.
            </video>
            <div className="movieHero__content container">
                <h1>{movie.title}</h1>
                <p>{movie.description}</p>
                <button className="button" onClick={() => navigate(`/movie/player/${movie._id}`)}>Watch Now</button>
            </div>
        </div>
    );
}

