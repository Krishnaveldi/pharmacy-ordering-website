import "./Loader.css";

const Loader = ({ type = "spinner", fullScreen = false, text = "Loading..." }) => {
    const loaderContent = (
        <div className="loader-container">
            {type === "spinner" && (
                <div className="loader">
                    <div className="spinner"></div>
                    <div className="spinner"></div>
                    <div className="spinner"></div>
                </div>
            )}

            {type === "ring" && (
                <div className="spinner-ring"></div>
            )}

            {type === "double-ring" && (
                <div className="spinner-double-ring"></div>
            )}

            {type === "pulse" && (
                <div className="loader-pulse"></div>
            )}

            {text && (
                <p className="loader-text">
                    {text}
                    <span className="loader-dot">.</span>
                    <span className="loader-dot">.</span>
                    <span className="loader-dot">.</span>
                </p>
            )}
        </div>
    );

    if (fullScreen) {
        return (
            <div className="loader-full-screen">
                {loaderContent}
            </div>
        );
    }

    return loaderContent;
};

export default Loader;
