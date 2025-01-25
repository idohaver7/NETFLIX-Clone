import { Link } from "react-router-dom";

export default function Header({showButtons, isLogged }) {
    return(
        <div className="header__wrapper">
            <div className="header container">
                <div className="header__logo">
                    <Link to="/">
                        <img src="../../assets/logo.png" alt="Netflix Logo" />
                    </Link>
                </div>
                <div className="header__buttons">
                    { showButtons && <Link to="/login" className="button header__button">Sign In</Link>}
                </div>
            </div>
        </div>
    )
}