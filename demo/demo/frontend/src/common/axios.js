import axios from "axios";

// axios 객체 생성 export
export default axios.create({
    // 백엔드 localhost:8080
    baseURL: 'https://69a7-175-215-98-68.jp.ngrok.io/api/',
    headers: {
        'Content-Type': 'application/json'
    },
    
    withCredentials: true
});
