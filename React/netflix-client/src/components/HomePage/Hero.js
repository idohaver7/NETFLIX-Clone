import { Link } from "react-router-dom";

export default function Hero() {
    return(
        <div className="hero__wrapper">
            <div className="container hero">
                <div className="hero__content">
                    <h1>Unlimited movies, TV shows, and more</h1>
                    <p>Starts at ₪32.90. Cancel anytime.</p>
                    <p>Ready to watch? Enter your email to create or restart your membership.</p>
                    <Link to="/register" className="button hero__button">Get Started ></Link>
                </div>
            </div>

            <div className="hero__seperator"></div>
        </div>
    )
}