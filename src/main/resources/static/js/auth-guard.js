// auth-guard.js
(function () {
    const token = localStorage.getItem('token');
    if (!token) {
        document.body.style.display = 'none';
        document.body.innerHTML = `
            <div style="
                min-height: 100vh;
                background: #fff;
                font-family: sans-serif;
                display: flex;
                flex-direction: column;
            ">
                <!-- navbar matching your app -->
                <div style="
                    display: flex;
                    align-items: center;
                    padding: 12px 24px;
                    border-bottom: 1px solid #ddd;
                ">
                    <span style="font-size: 1.2rem; font-weight: bold; letter-spacing: 0.5px;">
                        🦌 UniConn
                    </span>
                </div>

                <!-- centered card -->
                <div style="
                    flex: 1;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                ">
                    <div style="
                        border: 1px solid #aaa;
                        border-radius: 8px;
                        width: 420px;
                        overflow: hidden;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                    ">
                        <!-- card header -->
                        <div style="
                            background: #b0c4d8;
                            padding: 10px 16px;
                            font-weight: bold;
                            font-size: 1rem;
                            border-bottom: 1px solid #aaa;
                        ">
                            Access Restricted
                        </div>

                        <!-- card body -->
                        <div style="
                            padding: 48px 32px;
                            text-align: center;
                            background: #fff;
                        ">
                            <p style="margin: 0 0 8px; font-size: 1rem; color: #333;">
                                You must be logged in to view this page.
                            </p>
                            <p style="margin: 0 0 32px; font-size: 0.875rem; color: #777;">
                                Please sign in to continue.
                            </p>
                            <a href="/login" style="
                                display: inline-block;
                                background: #b0c4d8;
                                color: #333;
                                text-decoration: none;
                                padding: 10px 40px;
                                border-radius: 999px;
                                font-size: 0.95rem;
                                border: 1px solid #aaa;
                            ">
                                Go to Login
                            </a>
                        </div>
                    </div>
                </div>
            </div>`;
        document.body.style.display = '';
    }
})();