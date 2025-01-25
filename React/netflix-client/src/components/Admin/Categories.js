import { useEffect, useState } from "react"

export default function Categories({ token }) {
    const [categories, setCategories] = useState([])
    const [loading, setLoading] = useState(true)
    const [trigger, setTrigger] = useState(0);
    const [showAddModal, setShowAddModal] = useState(false)
    const [addCategory, setAddCategory] = useState({ name: '', promoted: false })

    const [editCategory, setEditCategory] = useState()

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
    }, [trigger])

    const handleAddChange = (event) => {
        const { name, value } = event.target
        if (name === 'name') {
            setAddCategory(prevState => ({
                ...prevState,
                name: value
            }))
        } else if (name === 'promoted') {
            setAddCategory(prevState => ({
                ...prevState,
                promoted: event.target.checked
            }))
        }
    }

    const handleEditChange = (event) => {
        const { name, value } = event.target
        if (name === 'name') {
            setEditCategory(prevState => ({
                ...prevState,
                name: value
            }))
        } else if (name === 'promoted') {
            setEditCategory(prevState => ({
                ...prevState,
                promoted: event.target.checked
            }))
        }
    }

    const submitEdit = () => {
        console.log(editCategory)
        fetch(`http://localhost:8080/api/categories/${editCategory._id}`, {
            method: "PATCH",
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token 
            },
            body: JSON.stringify(editCategory)
        })
        .then(response => {
            if (response.ok) {
                alert('Category Updated Succuesfully')
                setEditCategory()
                setTrigger(prev => prev + 1);
            } else 
                alert('Error: Server error, please try again')
        })
    }

    const submitAdd = (event) => {
        event.preventDefault()

        if (!addCategory.name)
            return alert('Error: some field are missed')

        fetch('http://localhost:8080/api/categories', {
            method: "POST",
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token 
            },
            body: JSON.stringify(addCategory)
        })
        .then(response => {
            if (response.ok) {
                alert('Category Created Succuesfully')
                setAddCategory({ name: '', promoted: false })
                setShowAddModal(false)
                setTrigger(prev => prev + 1);
            } else 
                alert('Error: Server error, please try again')
        })
    }

    const deleteCategory = (category_id) => {
        fetch(`http://localhost:8080/api/categories/${category_id}`, {
            method: "DELETE",
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token 
            }
        })
        .then(response => {
            if (response.ok) {
                alert('Category Deleted Succuesfully')
                setTrigger(prev => prev + 1);
            } else 
                alert('Error: Server error, please try again')
        })
    }

    if (loading)
        return(
            <>
                <h1>Categories</h1>
                <div className="admin__block">
                    <span class="loader"></span>
                </div>
            </>
        )

    return(
        <>
        <div className="adminBlock__header">
            <h1>Categories</h1>
            <button className="button" onClick={() => setShowAddModal(true)}>Add Category</button>
        </div>
        <div className="admin__block">
            <div class="adminTable__headers">
                <div>Title</div>
                <div>Is Promoted</div>
            </div>
            {
                categories.map(category => {
                    return(
                        <div class="adminBlock__row">
                            <div>{category.name}</div>
                            <div>
                                {category.promoted ?
                                <svg clip-rule="evenodd" fill-rule="evenodd" stroke-linejoin="round" stroke-miterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m11.998 2.005c5.517 0 9.997 4.48 9.997 9.997 0 5.518-4.48 9.998-9.997 9.998-5.518 0-9.998-4.48-9.998-9.998 0-5.517 4.48-9.997 9.998-9.997zm-5.049 10.386 3.851 3.43c.142.128.321.19.499.19.202 0 .405-.081.552-.242l5.953-6.509c.131-.143.196-.323.196-.502 0-.41-.331-.747-.748-.747-.204 0-.405.082-.554.243l-5.453 5.962-3.298-2.938c-.144-.127-.321-.19-.499-.19-.415 0-.748.335-.748.746 0 .205.084.409.249.557z" fill-rule="nonzero"/></svg> :
                                <svg clip-rule="evenodd" fill-rule="evenodd" stroke-linejoin="round" stroke-miterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m12.002 2.005c5.518 0 9.998 4.48 9.998 9.997 0 5.518-4.48 9.998-9.998 9.998-5.517 0-9.997-4.48-9.997-9.998 0-5.517 4.48-9.997 9.997-9.997zm0 8.933-2.721-2.722c-.146-.146-.339-.219-.531-.219-.404 0-.75.324-.75.749 0 .193.073.384.219.531l2.722 2.722-2.728 2.728c-.147.147-.22.34-.22.531 0 .427.35.75.751.75.192 0 .384-.073.53-.219l2.728-2.728 2.729 2.728c.146.146.338.219.53.219.401 0 .75-.323.75-.75 0-.191-.073-.384-.22-.531l-2.727-2.728 2.717-2.717c.146-.147.219-.338.219-.531 0-.425-.346-.75-.75-.75-.192 0-.385.073-.531.22z" fill-rule="nonzero"/></svg>
                                }
                            </div>
                            <div className="adminRow__buttons">
                                <button className="button" onClick={() => deleteCategory(category._id)}>
                                    <svg clip-rule="evenodd" fill-rule="evenodd" stroke-linejoin="round" stroke-miterlimit="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="m20.015 6.506h-16v14.423c0 .591.448 1.071 1 1.071h14c.552 0 1-.48 1-1.071 0-3.905 0-14.423 0-14.423zm-5.75 2.494c.414 0 .75.336.75.75v8.5c0 .414-.336.75-.75.75s-.75-.336-.75-.75v-8.5c0-.414.336-.75.75-.75zm-4.5 0c.414 0 .75.336.75.75v8.5c0 .414-.336.75-.75.75s-.75-.336-.75-.75v-8.5c0-.414.336-.75.75-.75zm-.75-5v-1c0-.535.474-1 1-1h4c.526 0 1 .465 1 1v1h5.254c.412 0 .746.335.746.747s-.334.747-.746.747h-16.507c-.413 0-.747-.335-.747-.747s.334-.747.747-.747zm4.5 0v-.5h-3v.5z" fill-rule="nonzero"/></svg>
                                </button>
                                <button className="button button__secondery" onClick={() => setEditCategory(category)}>
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
                    <h2>Add Category</h2>
                </div>
                <form className="form__container admin__form" onSubmit={submitAdd}>
                    <input 
                        type="text"
                        name="name"
                        placeholder="Title" 
                        value={addCategory.name}
                        onChange={handleAddChange}/>
                    <div className="form__input">
                        <input
                            type="checkbox"
                            name="promoted" 
                            onChange={handleAddChange}/>
                        <label for="promoted">Promoted Category</label>
                    </div>
                    <button className="button full__button">Submit</button>
                </form>
            </div>
        </div>
        )}

        { editCategory && (
        <div className="adminModal__wrapper">
            <div className="admin__modal">
                <button className="close__modal" onClick={() => setEditCategory()}>X</button>
                <div className="adminModal__header">
                    <h2>Edit Category</h2>
                </div>
                <div className="form__container admin__form">
                    <input 
                        type="text"
                        name="name"
                        value={editCategory.name}
                        onChange={handleEditChange}
                        placeholder="Title" />
                    <div className="form__input">
                        <input
                            type="checkbox"
                            name="promoted" 
                            onChange={handleEditChange}
                            checked={editCategory.promoted} />
                        <label htmlFor="promoted">Promoted Category</label>
                    </div>
                    <button className="button full__button" onClick={() => submitEdit()}>Submit</button>
                </div>
            </div>
        </div>
        )}
        </>
    )
}