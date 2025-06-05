<template>
   <router-view v-slot="{ Component }">
    <div class="fullscreen-container" v-if="$route.path === '/login'">
      <component :is="Component" />
    </div>
    <component v-else :is="Component" />
  </router-view>
</template>

<script>
import Login from './components/Login.vue';

export default {
  name: 'App',
  components: {
    Login
  }
}
</script>

<style lang="scss">
.fullscreen-container {
  display: flex;
  justify-content: center;
  /* Centra horizontalmente */
  align-items: center;
  /* Centra verticalmente */
  min-height: 100vh;
  /* Asegura que ocupe toda la altura */
  width: 100vw;
  /* Asegura que ocupe todo el ancho (opcional, pero útil para el centrado) */
  margin: 0;
  /* Elimina márgenes por defecto */

}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
