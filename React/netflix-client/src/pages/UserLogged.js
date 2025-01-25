import Header from "../components/HomePage/Header"
import Footer from "../components/HomePage/Footer"
import { useState, useEffect } from "react"
export default function UserLogged() {
    const [categories, setCategories] = useState([])
    const [token, setToken] = useEffect('')

    useEffect(() => {
        setToken(localStorage.getItem('jwtToken'))
    }, [])

    useEffect(() => {
        fetch('http://localhost:8080/api/tokens')
        .then(response => response.json())
        .then(data => setCategories(data))
    }, [])

    return(
        <>
            <Header showButtons={false} />
            <Footer />
        </>
    )
}