<template>
  <div class="index-gestion-container">

    <!-- Franja superior (usuario logeado y boton de salir)-->
    <div class="top-bar">
      <div class="top-bar-left">
        <div class="icon-container-top-bar">
          <img :src="imglotus" alt="logo-top-bar" class="top-bar-logo" />
        </div>
        <p class="top-bar-text">Gest-In</p>
      </div>
      <div class="top-bar-right">
        <span class="user-display"> <i class="fas fa-thing fa-user"></i> {{ sessionUser }}</span>
        <button class="back-button" title="Volver" @click="back"><i class="fas fa-arrow-left"></i></button>
        <button class="logout-button" title="Desconectar" @click="logout"><i class="fas fa-power-off"></i></button>
      </div>
    </div>
    <!-- *** -->

    <header class="header">
      <img :src="imglotus" style="width: 100px;" alt="logo-header" />
      <h1>Bienvenido a la Gestión de Alumnos</h1>
      <img :src="imglotus" style="width: 100px;" alt="logo-header" />
    </header>
    <main class="main-container-gest">
      <router-link to="/AddAlumnos" class="action-card">
        <div class="card-top-section fondo-card-1">
          <div class="icon-container">
            <i class="fas fa-file-circle-plus card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Altas de Alumnos</p>
        </div>
      </router-link>
      <router-link to="/ListAlumnos" class="action-card">
        <div class="card-top-section fondo-card-2">
          <div class="icon-container">
            <i class="fas fa-list card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Lista de Alumnos</p>
        </div>
      </router-link>
      <router-link to="/Estadisticas" class="action-card">
        <div class="card-top-section fondo-card-4">
          <div class="icon-container">
            <i class="fas fa-regular fa-chart-line card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Estadísticas</p>
        </div>
      </router-link>
    </main>
  </div>
</template>

<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';


export default {
  name: 'GestAlumnos',
  data() {
    return {
      imglotus: imglotus,
      sessionUser: null,
    }
  },
  setup() {
    const router = useRouter();

    const logout = () => {
      localStorage.removeItem('authToken'); // Limpia el token de sesión
      localStorage.removeItem('sessionUser');
      router.push('/');
    };

    const back = () => {
      router.push('/IndexGestion'); // Redirige al indice.
    }
    return {
      logout,
      back,
    };
  },
  mounted() {
    this.sessionUser = localStorage.getItem('sessionUser');
    console.log('USER SESion', this.sessionUser);
  }
};
</script>

<style lang="css" scoped>
.fondo-card-4 {
  /* ESTADISTICAS */
  background-color: #d8363a;
}
</style>