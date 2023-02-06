import axios from "axios";

// axios 객체 생성 export
export default axios.create({
    // 백엔드 localhost:8080
    baseURL: 'http://ec2-43-201-31-246.ap-northeast-2.compute.amazonaws.com:8080/api/',
    headers: {
        'Content-Type': 'application/json'
    },
    
    withCredentials: true
});
