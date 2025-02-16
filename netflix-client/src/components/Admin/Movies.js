import { useEffect, useState } from "react"

export default function Categories({ token }) {
    const [movies, setMovies] = useState([])
    const [loading, setLoading] = useState(true)
    const [trigger, setTrigger] = useState(0);
    const [showAddModal, setShowAddModal] = useState(false)
    const [categories, setCategories] = useState([])

    const [editMovie, setEditMovie] = useState()
    const [addMovie, setAddMovie] = useState({ 
        title: '', 
        description: '',
        image: null,
        video: null,
        category: '' 
    })

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
                console.log(data)
                setLoading(false)
            })
        }
    }, [trigger])

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

    const handleAddChange = (event) => {
        const { name, value } = event.target
        setAddMovie(prevState => ({
            ...prevState,
            [name]: value
        }))
    }

    const handleEditChange = (event) => {
        const { name, value } = event.target
        setEditMovie(prevState => ({
            ...prevState,
            [name]: value
        }))
    }

    const submitEdit = () => {
        fetch(`http://localhost:8080/api/movies/${editMovie._id}`, {
            method: "PATCH",
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token 
            },
            body: JSON.stringify(editMovie)
        })
        .then(response => {
            if (response.ok) {
                alert('Category Updated Succuesfully')
                setEditMovie()
                setTrigger(prev => prev + 1);
            } else 
                alert('Error: Server error, please try again')
        })
    }

    const submitAdd = (event) => {
        event.preventDefault();
    
        if (!addMovie.title || !addMovie.description || !addMovie.video || !addMovie.image || !addMovie.category) {
            return alert('Error: Some fields are missing');
        }

        const formData = new FormData();
        formData.append('title', addMovie.title);
        formData.append('description', addMovie.description);
        formData.append('image', addMovie.image);
        formData.append('video', addMovie.video);
        formData.append('category', addMovie.category);
    
        fetch('http://localhost:8080/api/movies', {
            method: "POST",
            headers: {
                'Authorization': token 
            },
            body: formData
        })
        .then(response => {
            if (response.ok) {
                alert('Movie Created Successfully');
                setAddMovie({ title: '', description: '', image: null, video: null, category: '' });
                setShowAddModal(false);
                setTrigger(prev => prev + 1);
            } else {
                alert('Error: Server error, please try again');
            }
        })
        .catch(error => {
            console.log('Error submitting movie:', error);
            alert('Error: Network error, please try again');
        });
    };    

    const deleteMovie = (movie_id) => {
        fetch(`http://localhost:8080/api/movies/${movie_id}`, {
            method: "DELETE",
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token 
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Movie Deleted Succuesfully')
                setTrigger(prev => prev + 1);
            } else 
                alert('Error: Server error, please try again')
        })
    }

    const onImageChange = (event) => {
        if (event.target.files[0]) {
            setAddMovie(prevState => ({
                ...prevState,
                image: event.target.files[0]
            }));
        }
    };

    const onVideoChange = (event) => {
        if (event.target.files[0]) {
            setAddMovie(prevState => ({
                ...prevState,
                video: event.target.files[0]
            }));
        }
    };

    if (loading)
        return(
            <>
                <h1>Movies</h1>
                <div className="admin__block">
                    <span class="loader"></span>
                </div>
            </>
        )

    return(
        <>
        <div className="adminBlock__header">
            <h1>Movies</h1>
            <button className="button" onClick={() => setShowAddModal(true)}>Add Movie</button>
        </div>
        <div className="admin__block movies__block">
            <div class="adminTable__headers">
                <div>Title</div>
                <div>Category</div>
                <div>Description</div>
                <div>Image Path</div>
                <div>Video Path</div>
            </div>
            {
                movies.map(movie => {
                    return(
                        <div class="adminBlock__row">
                            <div>{movie.title.slice(0,35)}</div>
                            <div>{movie.category.name}</div>
                            <div>{movie.description.slice(0,30)}...</div>
                            <div>{movie.image.slice(0,10)}</div>
                            <div>{movie.video.slice(0,10)}</div>
                            <div className="adminRow__buttons">
                                <button className="button" onClick={() => deleteMovie(movie._id)}>
                                    <svg clip-rule="evenodd" fill-rule="evenodd" stroke-linejoin="round" stroke-miterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m20.015 6.506h-16v14.423c0 .591.448 1.071 1 1.071h14c.552 0 1-.48 1-1.071 0-3.905 0-14.423 0-14.423zm-5.75 2.494c.414 0 .75.336.75.75v8.5c0 .414-.336.75-.75.75s-.75-.336-.75-.75v-8.5c0-.414.336-.75.75-.75zm-4.5 0c.414 0 .75.336.75.75v8.5c0 .414-.336.75-.75.75s-.75-.336-.75-.75v-8.5c0-.414.336-.75.75-.75zm-.75-5v-1c0-.535.474-1 1-1h4c.526 0 1 .465 1 1v1h5.254c.412 0 .746.335.746.747s-.334.747-.746.747h-16.507c-.413 0-.747-.335-.747-.747s.334-.747.747-.747zm4.5 0v-.5h-3v.5z" fill-rule="nonzero"/></svg>
                                </button>
                                <button className="button button__secondery" onClick={() => setEditMovie(movie)}>
                                    <svg clip-rule="evenodd" fill-rule="evenodd" stroke-linejoin="round" stroke-miterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m4.481 15.659c-1.334 3.916-1.48 4.232-1.48 4.587 0 .528.46.749.749.749.352 0 .668-.137 4.574-1.492zm1.06-1.061 3.846 3.846 11.321-11.311c.195-.195.293-.45.293-.707 0-.255-.098-.51-.293-.706-.692-.691-1.742-1.74-2.435-2.432-.195-.195-.451-.293-.707-.293-.254 0-.51.098-.706.293z" fill-rule="nonzero"/></svg>
                                </button>
                            </div>
                        </div>
                    )
                })
            }
        </div>

        { showAddModal && (
        <div className="adminModal__wrapper">
            <div className="admin__modal">
                <button className="close__modal" onClick={() => setShowAddModal(false)}>X</button>
                <div className="adminModal__header">
                    <h2>Add Movie</h2>
                </div>
                <form className="form__container admin__form" onSubmit={submitAdd}>
                    <input 
                        type="text"
                        name="title"
                        placeholder="Title" 
                        value={addMovie.title}
                        onChange={handleAddChange} />
                    <textarea 
                        type="text"
                        name="description"
                        placeholder="Description" 
                        value={addMovie.description}
                        onChange={handleAddChange} />
                    <label>Video</label>
                    <input 
                        type="file"
                        name="video"
                        placeholder="Video Path" 
                        onChange={onVideoChange} />
                     <label>Image</label>
                    <input 
                        type="file"
                        name="image"
                        placeholder="Image Path" 
                        onChange={onImageChange} />
                    <div className="select__container">
                        <select name="category" onChange={handleAddChange} value={addMovie.category}>
                            <option>Select Category:</option>
                            {
                                categories.map(category => {
                                    return(<option value={category._id}>{category.name}</option>)
                                })
                            }
                        </select>
                    </div>

                    <button className="button full__button">Submit</button>
                </form>
            </div>
        </div>
        )}

        { editMovie && (
        <div className="adminModal__wrapper">
            <div className="admin__modal">
                <button className="close__modal" onClick={() => setEditMovie()}>X</button>
                <div className="adminModal__header">
                    <h2>Edit Movie</h2>
                </div>
                <div className="form__container admin__form">
                <input 
                        type="text"
                        name="title"
                        placeholder="Title" 
                        value={editMovie.title}
                        onChange={handleEditChange} />
                    <textarea 
                        type="text"
                        name="description"
                        placeholder="Description" 
                        value={editMovie.description}
                        onChange={handleEditChange} />
                    <div className="select__container">
                        <select name="category" onChange={handleEditChange} value={editMovie.category._id}>
                            <option>Select Category:</option>
                            {
                                categories.map(category => {
                                    return(<option value={category._id}>{category.name}</option>)
                                })
                            }
                        </select>
                    </div>
                    <button className="button full__button" onClick={() => submitEdit()}>Submit</button>
                </div>
            </div>
        </div>
        )}
        </>
    )
}