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
                <button class="logout-button" title="Desconectar" @click="logout"><i
                        class="fas fa-power-off"></i></button>
            </div>
        </div>
        <!-- *** -->

        <main class="action-container container-color-guardar">
            <div>
                <img :src="imglotus" style="width: 185px;" alt="logo">
            </div>

            <!-- Gestion de alerta y borrado de datos del fomulario -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>

            <form @submit.prevent="guardarUsuario">
                <div class="form-group">
                    <input type="text" id="dni" v-model="nuevoUsuario.dni" placeholder="DNI" required>
                </div>
                <div class="form-group">
                    <input type="text" id="login" v-model="nuevoUsuario.login" placeholder="Ej: pepito.perez" required>
                </div>
                <div class="form-group">
                    <input type="password" id="password" v-model="nuevoUsuario.password" placeholder="Contraseña"
                        required>
                </div>
                <div class="form-group">
                    <select id="rol" class="form-select" v-model="nuevoUsuario.rol" required>
                        <option value="ADMIN">ADMIN</option>
                        <option value="USER">USER</option>
                    </select>
                </div>
                <button type="submit" class="action-button"><i class="fas fa-plus-circle"></i><span>Añadir nuevo
                        Usuario</span>
                </button>
            </form>
        </main>
    </div>
</template>

<script>

import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
    name: 'AddUsuarios',
    data() {
        return {
            imglotus: imglotus,
            sessionUser: null,

            nuevoUsuario: {
                dni: '',
                login: '',
                password: '',
                rol: 'USER'
            },
            apiUrl: 'http://localhost:8080',
            version: '1.0',

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

        }
    },

    setup() {
        const router = useRouter();

        const logout = () => {
            localStorage.removeItem('authToken'); // Limpia el token de sesión
            localStorage.removeItem('sessionUser');
            router.push('/'); // Redirige a la página de login (asumiendo que tu ruta de login es '/')
        };

        const back = () => {
            router.push('/GestUsuarios'); // Redirige al indice.
        };
        return {
            logout,
            back,
            router,
        };
    },
    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);
    },
    methods: {
        async guardarUsuario() {
            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/usuarios/alta`, this.nuevoUsuario, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.error = false;
                this.mostrarAlerta = true;

                this.mensaje = response.data;

            } catch (error) {
                this.error = true;
                this.redireccion = false;

                if (error.response && error.response.status === 409) { // El usuario ya existe.
                    this.mensaje = error.response.data; // Se captura el error del backend
                } else {
                    this.mensaje = 'Fallo al dar de alta un nuevo usuario. Conexion perdida con el servidor.';
                }

                this.mostrarAlerta = true;
            }
            this.limpiarFormulario();
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.router.push('/ListUsuarios');
        },
        limpiarFormulario() {
            this.nuevoUsuario = { dni: '', login: '', password: '', rol: 'USER' };
        },
    }
}
</script>

<style lang="css" scoped>
.action-button {
    margin-left: auto;
    margin-right: auto;
}

button span {
    color: #ee7724
}

.alert {
    width: 60%;
}

.alert-danger {
    width: 60%;
}
</style>
