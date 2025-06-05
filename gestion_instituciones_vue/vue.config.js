const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081 // Cambia 8081 al puerto que desees utilizar
  }
})
