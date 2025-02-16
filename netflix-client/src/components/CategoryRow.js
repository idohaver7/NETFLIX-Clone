import { useEffect } from 'react'
import MovieCard from './MovieCard'

export default function CategoryRow({ category, movies }) {
    useEffect(() => {
        console.log(movies)
    }, [])
    if (movies.length === 0)
        return(<></>)

    return(
        <div className="category__wrapper" id={category.name}>
            <div className='category__content container'>
                <h3>{ category.name }</h3>
                <div className="movies__grid slider">
                    {
                        movies.map(movie => {
                            return(<MovieCard key={movie._id} movie={movie} isCompact={true} />)
                        })
                    }
                </div>
            </div>
        </div>
    )
}