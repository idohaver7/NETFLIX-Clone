import { Link, useNavigate } from "react-router-dom"
import { useState } from "react"
import Movies from "../components/Admin/Movies"
import Categories from "../components/Admin/Categories"

export default function AdminPage() {
    const navigate = useNavigate()

    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    return(
        <>
        <div className="header__wrapper">
            <div className="header container">
                <div className="header__logo">
                    <Link to="/">
                        <img src="../../assets/logo.png" alt="Netflix Logo" />
                    </Link>
                </div>
            </div>
        </div>

        <div className="admin__page">
            <div className="container">
                <Categories token={token}/>
                <Movies token={token} />
            </div>
        </div>
        </>
    )
}