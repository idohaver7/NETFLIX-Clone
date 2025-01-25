import Header from "../components/Header"
import Hero from "../components/HomePage/Hero"
import Faq from "../components/HomePage/Faq"
import Benefits from "../components/HomePage/Benefits"
import Footer from "../components/HomePage/Footer"
import { useNavigate } from "react-router-dom"
import { useState, useEffect } from "react" 

export default function HomePage() {
    const navigate = useNavigate()

    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    useEffect(() => {
        navigate('/home')
    }, [])
    
    return(
    <>
        <Header showButtons={true} />
        <Hero />
        <Benefits />
        <Faq />
        <Footer />
    </>)
}