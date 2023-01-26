export default {
    makeDateStr(year, month, day, type){ // type + / . 2022/07/24 2022.11.24
        return year + type + ( ( month < 10 ) ? '0' + month : month ) + type + ( ( day < 10 ) ? '0' + day : day );
    },
    makeTimeStr(hour, minute, second, type){ // type + / . 2022/07/24 2022.11.24
        return hour + type + ( ( minute < 60 ) ? '0' + minute : minute ) + type + ( ( second < 60 ) ? '0' + second : second );
    },
    // 정수 난수 생성
    getRandomInt(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min; //최댓값은 제외, 최솟값은 포함
      }

}