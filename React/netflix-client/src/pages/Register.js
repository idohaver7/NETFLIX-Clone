import Header from "../components/HomePage/Header"
import Footer from "../components/HomePage/Footer"
import { Link, useNavigate } from "react-router-dom"
import { useState } from "react"

export default function Register() {
    const [email, setEmail] = useState('')
    const [password, setPassowrd] = useState('')
    const [profile, setProfile] = useState()
    const [name, setName] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate()

    const handleChange = (event) => {
        const { name, value } = event.target

        if (name === 'email')
            setEmail(value)
        else if (name === 'password')
            setPassowrd(value)
        else if (name === 'name')
            setName(value)
        else if (name == 'profile_picture')
            setProfile(value)
    }

    const handleSubmit = (event) => {
        event.preventDefault()

        if (!email || !password || !profile || !name)
            return setError('Some fields are missing')

        fetch('http://localhost:8080/api/users', {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password, name, profile })
        })
        .then(response => {
            if (response.ok) {
                alert.alert('User Created Succuesfully')
                navigate('/login', { replace: true })
            } else 
            setError('Server error, Please try again')
        })
    }

    return(
        <>
            <Header showButtons={false} />
            <div className="sign__container">
                <div className="form__container">
                    <h1>Sign Up</h1>
                    <form onSubmit={handleSubmit}>
                        <input 
                            type="email"
                            name="email"
                            onChange={handleChange}
                            value={email}
                            placeholder="Email" />
                        <input
                            type="password"
                            name="password"
                            onChange={handleChange}
                            value={password}
                            placeholder="Password" />
                        <input
                            type="text"
                            name="name"
                            onChange={handleChange}
                            value={name}
                            placeholder="Name" />
                        <label for="profile_picture">Profile Picture</label>
                        <input
                            type="file"
                            name="profile_picture"
                            onChange={handleChange}
                            value={profile}
                            placeholder="Profile Picture" />
                        <button  className="button full__button">Sign Up</button>
                        { error && <div className="form__error">{error}</div>}
                    </form>
                    <p>
                        Already have account? <Link to="/login">Sign In</Link>
                    </p>
                </div>
            </div>
            <Footer />
        </>
    )
}