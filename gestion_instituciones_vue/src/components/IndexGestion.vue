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
        <button class="logout-button" @click="logout">SALIR</button>
      </div>
    </div>
    <!-- *** -->
    <header class="header">
      <img :src="imglotus" style="width: 100px;" alt="logo-header" />
      <h1>sistema de GESTión integral en INstituciones</h1>
      <img :src="imglotus" style="width: 100px;" alt="logo-header" />
    </header>
    <main class="main-container-gest">
      <router-link to="/GestUsuarios" class="action-card">
        <div class="card-top-section fondo">
          <div class="icon-container">
            <i class="fas fa-user card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Gestión de Usuarios</p>
        </div>
      </router-link>

      <router-link to="/GestAlumnos" class="action-card">
        <div class="card-top-section fondo">
          <div class="icon-container">
            <i class="fas fa-user-graduate card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Gestión de Alumnos</p>
        </div>
      </router-link>

      <router-link to="/GestAsignaturas" class="action-card">
        <div class="card-top-section fondo">
          <div class="icon-container">
            <i class="fas fa-book card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Gestión de Asignaturas</p>
        </div>
      </router-link>

      <router-link to="/GestModulos" class="action-card">
        <div class="card-top-section fondo">
          <div class="icon-container">
            <i class="fas fa-cube card-icon-color"></i>
          </div>
        </div>
        <div class="card-bottom-section">
          <p class="card-text">Gestión de Módulos</p>
        </div>
      </router-link>

    </main>
  </div>
</template>

<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';

export default {
  name: 'IndexGestion',
  data() {
    return {
      imglotus: imglotus,
      sessionUser: null, // Para recuperar el login del usuario conectado.
    }
  },
  setup() {
    const router = useRouter();

    const logout = () => {
      localStorage.removeItem('authToken'); // Limpia el token de sesión
      localStorage.removeItem('sessionUser');
      router.push('/'); // Redirige a la página de login
    };

    return {
      logout,
    };
  },
  mounted() {
    this.sessionUser = localStorage.getItem('sessionUser');
    console.log('USER SESion', this.sessionUser);
  }

}
</script>

<style lang="css" scoped>
.header {
  margin-top: 5%;
}

.fondo {

  /*Degradado para esta sección */
  background-image: linear-gradient(to right, #ee7724, #d8363a, #dd3675, #b44593);

  &:hover {
    background-image: linear-gradient(to right, #b44593, #dd3675, #d8363a, #ee7724);
  }
}
</style>
