import Header from "../components/Header"
import Footer from "../components/HomePage/Footer"
import { Link, useNavigate } from "react-router-dom"
import { useEffect, useState } from "react"

export default function Register() {
    const [email, setEmail] = useState('')
    const [password, setPassowrd] = useState('')
    const [profile, setProfile] = useState(null)
    const [name, setName] = useState('')
    const [error, setError] = useState('')
    const navigate = useNavigate()
    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    useEffect(() => {
        if (token)
            navigate('/home')
    }, [token])
    
    useEffect(() => {
        console.log(profile)
    }, [profile])

    const handleChange = (event) => {
        const { name, value } = event.target

        if (name === 'email')
            setEmail(value)
        else if (name === 'password')
            setPassowrd(value)
        else if (name === 'name')
            setName(value)
    }

    const onFileChange = event => {
        setProfile(event.target.files[0])
    }
    const handleSubmit = (event) => {
        event.preventDefault();
    
        if (!email || !password || !profile || !name)
            return setError('Some fields are missing');
    
        const formData = new FormData();
        formData.append('email', email);
        formData.append('password', password);
        formData.append('name', name);
        formData.append('profilePicture', profile);  // Assuming 'profile' is the File object
    
        fetch('http://localhost:8080/api/users', {
            method: "POST",
            body: formData  // FormData will correctly set the Content-Type
        })
        .then(response => {
            if (response.ok) {
                alert('User Created Successfully');
                navigate('/login', { replace: true });
            } else {
                setError('Server error, Please try again');
            }
        })
        .catch(error => {
            setError('Network error, Please try again');
        });
    };    

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
                            onChange={onFileChange}
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