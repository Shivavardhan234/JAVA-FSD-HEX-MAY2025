import React from "react";


function HomeScreen() {
    return (
        <div
            className="position-absolute top-0 start-0 w-100"
            style={{
                top: "70px", // offset for fixed navbar
                height: "100vh",
                backgroundColor: "rgba(144, 238, 144, 0.75)",
                overflow: "hidden",
            }}
        >
            <div className="container h-100 d-flex flex-column justify-content-between py-4">
                {/* Main content row */}
                <div className="row align-items-center flex-grow-1">
                    {/* Text section */}
                    <div className="col-md-7 mb-4 mb-md-0">
                        <div className="ps-md-4">
                            <h1 className="display-4 fw-bold mb-3">Welcome to Maverick Bank</h1>
                            <p className="lead">
                                Banking should feel effortless. With Maverick Bank you can open
                                an account in minutes, move money instantly, and keep track of
                                your savings all in one place.
                            </p>
                            <p className="lead">
                                Enjoy transparent fees, competitive rates, and 24 × 7 customer
                                support—so you can focus on what matters while we take care of
                                your finances.
                            </p>
                        </div>
                    </div>

                    {/* Image placeholder */}
                    <div className="col-md-5 text-center">
                        <div
                            className="border rounded shadow-sm p-4 h-100 d-flex justify-content-center align-items-center"
                            style={{ minHeight: "300px", backgroundColor: "white" }}
                        >
                            <img
                                src="piggy_bank.jpeg"
                                alt="Maverick Bank"
                                className="img-fluid rounded shadow"
                            />
                        </div>
                    </div>
                </div>

                {/* Footer */}
                <footer className="text-center pt-3 border-top mt-4">
                    <p className="mb-1 fw-semibold">Need help? We’re here 24 × 7</p>
                    <p className="mb-0">
                        Helpline:{" "}
                        <a className="text-decoration-none">
                            1800-123-456
                        </a>
                        {" "}| Email:{" "}
                        <a className="text-decoration-none">
                            support@maverickbank.com
                        </a>
                    </p>
                </footer>
            </div>
        </div>
    );
}

export default HomeScreen;
