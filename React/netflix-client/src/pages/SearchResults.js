import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import MovieCard from '../components/MovieCard';

const useQuery = () => {
    return new URLSearchParams(useLocation().search);
};

export default function SearchResults({ token }) {
    const query = useQuery()
    const keyword = query.get('q')
    const [loading, setLoading] = useState(true)
    const [results, setResults] = useState([])

    useEffect(() => {
        if (token) {
            fetch(`http://localhost:8080/api/movies/search/${keyword}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token
                  }
            })
            .then(response => response.json())
            .then(data => {
                setResults(data)
                setLoading(false)
            })
        }
    }, [keyword])

    if (loading) 
        return(
            <>
                <h3>Search Results For: {keyword}</h3>
                <div className="loading__wrapper">
                    <span class="loader"></span>
                </div>
            </>
        )
    return(
        <>
            <h3>Search Results For: {keyword}</h3>
            { results.length === 0 && <h3>No Results</h3>}
            {
                results.length > 0 && (
                    <div className="search__grid">
                        {
                            results.map(movie => {
                                return(<MovieCard key={movie._id} movie={movie} isCompact={true} />)
                            })
                        }
                    </div>
                )
            }
        </>
    )
}