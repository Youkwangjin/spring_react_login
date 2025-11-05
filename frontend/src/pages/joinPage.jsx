import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

const BACKEND_API_BASE_URL = process.env.REACT_APP_BACKEND_API_BASE_URL;

function JoinPage() {

    const navigate = useNavigate();
    
    // 회원가입 변수
    const [userEmail, setUserEmail] = useState("");
    const [isUserEmailValid, setIsUserEmailValid] = useState(null); 
    const [userPassword, setUserPassword] = useState("");
    const [userNm, setUserNm] = useState("");
    const [error, setError] = useState("");

    // username 입력창 변경 이벤트
    useEffect(() => {

        const checkUserEmail = async () => {

            if(userEmail.length < 4) {
                setIsUserEmailValid(null);
                return;
            }

            try {
                const res = await fetch(`${BACKEND_API_BASE_URL}/api/v1/user/exist`, {
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    credentials: "include",
                    body: JSON.stringify({userEmail}),
                });

                const exist = await res.json();
                setIsUserEmailValid(!exist);
            } catch {
                setIsUserEmailValid(null);
            }
        };

        const delay = setTimeout(checkUserEmail, 300);
        return () => clearTimeout(delay);
    }, [userEmail]);

    // 회원가입 이벤트
    const handleSignUp = async (e) => {

        e.preventDefault();
        setError("");


        if (userEmail.length < 4 ||
            userPassword.length < 4 ||
            userNm.trim() === ""
        ) {
            setError("입력값을 다시 확인해주세요. (모든 항목은 필수이며, ID/비밀번호는 최소 4자입니다.)");
            return;
        }

        try {
            const res = await fetch(`${BACKEND_API_BASE_URL}/api/v1/users`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "include",
                body: JSON.stringify({ userEmail, userPassword, userNm}),
            });

            if (!res.ok) throw new Error("회원가입 실패");
            navigate("/login");

        } catch {
            setError("회원가입 중 오류가 발생했습니다. 관리자에게 문의하세요.")
        }
    }

    // html 코드
    return (
        <div>
            <h1>회원 가입</h1>

            <form onSubmit={handleSignUp}>
                <label>아이디</label>
                <input
                    type="text"
                    placeholder="아이디 (4자 이상)"
                    value={userEmail}
                    onChange={(e) => setUserEmail(e.target.value)}
                    required
                    minLength={4}
                />
                {userEmail.length >= 4 && isUserEmailValid === false && (
                    <p>이미 사용 중인 이메일입니다.</p>
                )}
                {userEmail.length >= 4 && isUserEmailValid === true && (
                    <p>사용 가능한 이메일입니다.</p>
                )}

                <label>비밀번호</label>
                <input
                    type="password"
                    placeholder="비밀번호 (4자 이상)"
                    value={userPassword}
                    onChange={(e) => setUserPassword(e.target.value)}
                    required
                    minLength={4}
                />

                <label>이름</label>
                <input
                    type="text"
                    placeholder="닉네임"
                    value={userNm}
                    onChange={(e) => setUserNm(e.target.value)}
                    required
                />
                {error && <p>{error}</p>}
                <button type="submit" disabled={isUserEmailValid !== true}>회원가입</button>
            </form>
        </div>
    );
}

export default JoinPage;