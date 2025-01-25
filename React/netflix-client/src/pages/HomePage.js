import Header from "../components/HomePage/Header"
import Hero from "../components/HomePage/Hero"
import Faq from "../components/HomePage/Faq"
import Benefits from "../components/HomePage/Benefits"
import Footer from "../components/HomePage/Footer"

export default function HomePage() {
    return(
    <>
        <Header showButtons={true} />
        <Hero />
        <Benefits />
        <Faq />
        <Footer />
    </>)
}