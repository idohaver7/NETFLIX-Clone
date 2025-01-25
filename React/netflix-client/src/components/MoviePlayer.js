import { useEffect, useState, useRef } from "react"
import { useParams, useNavigate } from "react-router-dom"

export default function MoviePlayer() {
    const navigate = useNavigate()
    const { id } = useParams()
    const [loading, setLoading] = useState(true)
    const [movie, setMovie] = useState({})
    const [token, setToken] = useState(() => {
        const token = localStorage.getItem('jwtToken')
        return token
    })

    const videoRef = useRef(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [volume, setVolume] = useState(1);
    const [currentTime, setCurrentTime] = useState(0);
    const [duration, setDuration] = useState(0);

    useEffect(() => {
        if (!token)
            navigate('/login', { replace: true })
    })

    useEffect(() => {
        fetch(`http://localhost:8080/api/movies/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token
              }
        })
        .then(response => response.json())
        .then(data => {
            setMovie(data)
            setLoading(false)
        })
    }, [id, token])

    useEffect(() => {
        const video = videoRef.current;
        if (video) {
            video.addEventListener('loadedmetadata', handleOnLoadedMetadata);
            video.addEventListener('timeupdate', handleTimeUpdate);

            return () => {
                video.removeEventListener('loadedmetadata', handleOnLoadedMetadata);
                video.removeEventListener('timeupdate', handleTimeUpdate);
            };
        }
    }, [movie]);

    useEffect(() => {
        const video = videoRef.current;
        if(video) {
            const updateTime = () => {
                setCurrentTime(video.currentTime);
            };
            video.addEventListener('timeupdate', updateTime);
            return () => {
                video.removeEventListener('timeupdate', updateTime);
            };
        }
    }, [movie]);

    const togglePlay = () => {
        const video = videoRef.current;
        if (video.paused) {
            video.play();
            setIsPlaying(true);
        } else {
            video.pause();
            setIsPlaying(false);
        }
    };

    const handleVolumeChange = (event) => {
        const newVolume = parseFloat(event.target.value);
        videoRef.current.volume = newVolume;
        setVolume(newVolume);
    };

    const toggleFullScreen = () => {
        const video = videoRef.current;
        if (!document.fullscreenElement) {
            video.requestFullscreen().catch(err => {
                alert(`Error attempting to enable full-screen mode: ${err.message} (${err.name})`);
            });
        } else {
            document.exitFullscreen();
        }
    };

    const handleOnLoadedMetadata = () => {
        const video = videoRef.current;
        setDuration(video.duration);
    };

    const handleTimeUpdate = () => {
        const video = videoRef.current;
        setCurrentTime(video.currentTime);
    };

    const handleSeek = (event) => {
        const video = videoRef.current;
        const time = parseFloat(event.target.value);
        video.currentTime = time;
        setCurrentTime(time);
    }

    const formatTime = (seconds) => {
        let minutes = Math.floor(seconds / 60);
        minutes = (minutes >= 10) ? minutes : `0${minutes}`;
        seconds = Math.floor(seconds % 60);
        seconds = (seconds >= 10) ? seconds : `0${seconds}`;
        return `${minutes}:${seconds}`;
    };

    if (loading)
        return(
            <div className="fullLoading__wrapper">
                <span class="loader"></span>
            </div>
        )

    return (
        <div className="video__container">
            <div className="close__video" onClick={() => navigate('/home')}>X</div>
            <video ref={videoRef} className="video__player" src={`/movies/video/${movie.video}`} onClick={togglePlay}>
                Your browser does not support the video tag.
            </video>
            <div className="video__controls">
                <button onClick={togglePlay}>
                    {isPlaying 
                    ? <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M11 22h-4v-20h4v20zm6-20h-4v20h4v-20z"/></svg> :
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M3 22v-20l18 10-18 10z"/></svg>}
                </button>
                <input
                className="video__seek"
                    type="range"
                    min="0"
                    max={duration}
                    value={currentTime}
                    onChange={handleSeek} />
                <span>{formatTime(currentTime)} / {formatTime(duration)}</span>
                <input className="video__volume" type="range" min="0" max="1" step="0.1" value={volume} onChange={handleVolumeChange} />
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M12 12l5-2.917v9.917h-5v-7zm7-4.083v11.083h5v-14l-5 2.917zm-9 5.25l-10 5.833h10v-5.833z"/></svg>
                <button onClick={toggleFullScreen}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M15 2h2v5h7v2h-9v-7zm9 13v2h-7v5h-2v-7h9zm-15 7h-2v-5h-7v-2h9v7zm-9-13v-2h7v-5h2v7h-9z"/></svg>
                </button>
            </div>
        </div>
    )
}