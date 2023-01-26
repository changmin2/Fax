const webpack = require("webpack");

module.exports = {
  lintOnSave: false,
  outputDir: "../src/main/resources/static",  // 빌드 타겟 디렉토리

  devServer: {
    proxy: {
      '/fax-api': {
        // '/api' 로 들어오면 포트 8080(스프링 서버)로 보낸다
        target: 'http://localhost:8080',
        changeOrigin: true // cross origin 허용
      }
    }
  },



  configureWebpack: {
    // Set up all the aliases we use in our app.
    plugins: [
      new webpack.optimize.LimitChunkCountPlugin({
        maxChunks: 6,
      }),
    ],
  },
  pwa: {
    name: "Vue Argon Design",
    themeColor: "#172b4d",
    msTileColor: "#172b4d",
    appleMobileWebAppCapable: "yes",
    appleMobileWebAppStatusBarStyle: "#172b4d",
  },
  css: {
    // Enable CSS source maps.
    sourceMap: process.env.NODE_ENV !== "production",
    loaderOptions: {
      scss: {
        // 전역설정; scss사용할 때 변수를 모아둔 파일을 연결한다.
        data: `
        @import "@/assets/scss/bootstrap/_functions.scss";
        @import "@/assets/scss/custom/_variables.scss";
                  `,
      },
    },
  },
};
