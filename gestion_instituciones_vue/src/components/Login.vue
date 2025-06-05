<template>

  <div>

    <main class="login-container">
      <div>
        <img :src="imglotus" style="width: 185px;" alt="logo">
        <h4>Bienvenido a Gest-In</h4>
      </div>

      <!-- Gestion de alerta y borrado de datos del fomulario -->
      <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
        {{ mensaje }}
        <button type="button" class="x-close" @click="cerrarAlerta">X</button>
      </div>

      <!-- Formulario para el acceso -->
      <form v-if="!esUnRegistro" @submit.prevent="login">
        <p>Por favor, inicie sesión en su cuenta</p>
        <div class="form-group">
          <input type="text" id="username" v-model="username" placeholder="Nombre de usuario">
        </div>
        <div class="form-group">
          <input type="password" id="password" v-model="password" placeholder="Contraseña">
        </div>
        <button type="submit" class="login-button">ACCESO</button>
        <p class="register-link">¿No tienes cuenta? <a href="#" @click.prevent="registrarse">Regístrate aquí</a></p>
      </form>
      <!-- ** -->
      <!-- Formulario para el registro -->
      <form v-else @submit.prevent="registrar">

        <p>Crea tu cuenta</p>
        <div class="form-group">
          <input type="text" id="reg-username" v-model="username" placeholder="Login de usuario" required>
        </div>
        <div class="form-group">
          <input type="password" id="reg-password" v-model="password" placeholder="Contraseña" required>
        </div>
        <div class="form-group">
          <input type="text" id="reg-dni" v-model="dni" placeholder="DNI" required>
        </div>
        <button type="submit" class="login-button">REGISTRARSE</button>
        <p class="register-link">¿Ya tienes cuenta? <a href="#" @click.prevent="registrarse">Inicia sesión</a></p>

      </form>
    </main>
    <footer>
      <img :src="imgcc" alt="Creative Commons" class="cc-image">
      <h6>@Ana Belen Madrid Garcia</h6>
    </footer>
  </div>
</template>

<script>
import imglotus from '@/assets/lotus.webp';
import axios from 'axios';
import imgcc from '@/assets/cc.png'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Login',
  component: {},
  data() {
    return {
      imglotus: imglotus,
      imgcc: imgcc,
      apiUrl: 'http://localhost:8080', // Define la URL base de tu API

      username: "",
      password: "",
      dni: '',

      mensaje: '',
      error: false,
      mostrarAlerta: false, // Controla la visibilidad de la alerta

      esUnRegistro: false, // Para controlar que formulario se debe de mostrar.

    }
  },
  methods: {
    async login() {

      try {
        const response = await axios.post(`${this.apiUrl}/auth/login`, { // Envía una petición POST al endpoint /login
          login: this.username,
          password: this.password
        });

        localStorage.setItem('sessionUser', this.username); // Guardamos en localStorage el usuario que inicio sesion.
        localStorage.setItem('authToken', response.data.token); // Y guardamos tambien el token de autenticacion.
        this.$router.push('/IndexGestion'); // Y redirigimos al usuario a la página principal de gestion

      } catch (error) {

        if (error.response && error.response.status === 403) {
          this.error = true;
          this.mensaje = 'Error de autenticación o la cuenta no existe. Verifique las credenciales.';
        } else if (error.request) {
          this.error = true;
          this.mensaje = 'Fallo en el inicio. No se ha podido conectar con el servidor';
        }

        this.mostrarAlerta = true;
        this.limpiarFormulario();
      }
    },

    async registrar() {

      try {

        const response = await axios.post(`${this.apiUrl}/auth/signup`, {
          login: this.username,
          password: this.password,
          dni: this.dni,
          rol: "USER" // Se establece rol por defecto para los nuevos registros.
        });

        if (response.status === 200 || response.status === 201) {

          this.error = false;
          this.mensaje = response.data;
          console.log('He registrado algo: ', response.status); // PUNTO DE CONTRO
          this.mostrarAlerta = true;
          this.esUnRegistro = false; // Para que vuelva al formulario de login.
        }

      } catch (error) {

        if (error.response && error.response.status === 409) {
          this.error = true;
          this.mensaje = error.response.data;
          console.log('Error en registrar???: ', this.mensaje); // PUNTO DE CONTRO


        } else {
          if (error.request) {
            this.error = true;
            this.mensaje = 'No se ha podido conectar con el servidor para registrar al usuario. Intentelo mas tarde.'
          }
        }
        this.mostrarAlerta = true;
      }

    },
    cerrarAlerta() {
      this.mostrarAlerta = false;
      this.limpiarFormulario();
    },
    limpiarFormulario() {
      this.username = ""; // Limpia el campo de usuario
      this.password = ""; // Limpia el campo de contraseña
      this.dni = "";
    },
    registrarse() {
      this.esUnRegistro = !this.esUnRegistro;
      this.cerrarAlerta();
    }
  }
};
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  width: 400px;
  padding: 30px;
  border-radius: 10px;
  box-shadow: inset 2px 2px 5px rgba(0, 0, 0, 0.2),
    /* Sombra interior superior izquierda */
    inset -2px -2px 5px rgba(255, 255, 255, 0.7),
    /* Sombra interior inferior derecha (luz) */
    3px 3px 10px rgba(0, 0, 0, 0.2),
    /* Sombra exterior inferior derecha */
    -3px -3px 10px rgba(255, 255, 255, 0.7);
  /* Sombra exterior superior izquierda (luz) */
  background-color: #f0f0f0;
  position: relative;
  /* Añadimos esto para crear un contexto de apilamiento */
  z-index: 1;
  /* Aseguramos que el login esté "detrás" de la alerta */

}

h4 {
  margin-bottom: 20px;
  color: #333;
}

.login-button {
  width: 100%;
  padding: 5px;
  background-image: linear-gradient(to right, #ee7724, #d8363a, #dd3675, #b44593);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;

  &:hover {
    background-image: linear-gradient(to right, #b44593, #dd3675, #d8363a, #ee7724);
  }
}

.register-link {
  margin-top: 15px;
  font-size: 14px;
  text-align: center;

  a {
    color: #007bff;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
}

footer {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #333;
  color: white;
  padding: 5px 0;
  width: 100%;
  margin-top: 20px;
  /* Adjust as needed for spacing from the login box */
  border-radius: 5px;
}

footer h6 {
  margin: 0 0 0 10px;
  font-size: 0.8em;
  color: #bbb;

}

.cc-image {
  width: 50px;
  height: auto;
  vertical-align: middle;

}

.alert-danger {
  width: 60%;
}
</style>
