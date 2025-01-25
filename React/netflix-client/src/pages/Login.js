import Header from "../components/Header"
import Footer from "../components/HomePage/Footer"
import { Link, useNavigate } from "react-router-dom"
import { useState, useEffect } from "react"

export default function Login() {
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [password, setPassowrd] = useState('')
    const [error, setError] = useState('')
    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    useEffect(() => {
        navigate('/home')
    }, [])
    

    const handleChange = (event) => {
        const { name, value } = event.target

        if (name === 'email')
            setEmail(value)
        else if (name === 'password')
            setPassowrd(value)
    }

    const handleSubmit = (event) => {
        event.preventDefault()

        if (!email || !password)
            return setError('Email or password invalid')

        fetch('http://localhost:8080/api/tokens', {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                localStorage.setItem('jwtToken', ('Bearer ' + data.token))
                navigate('/home', { replace: true })
            }
            else
                setError('Server error, Please try again')
        })
        
    }

    return(
        <>
            <Header showButtons={false} />
            <div className="sign__container">
                <div className="form__container">
                    <h1>Sign In</h1>
                    <form onSubmit={handleSubmit}>
                        <input 
                            type="text"
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
                        <button  className="button full__button">Sign In</button>
                        { error && <div className="form__error">{error}</div>}
                    </form>
                    <p>
                        New to netflix? <Link to="/register">Sign up now</Link>
                    </p>
                </div>
            </div>
            <Footer />
        </>
    )
}