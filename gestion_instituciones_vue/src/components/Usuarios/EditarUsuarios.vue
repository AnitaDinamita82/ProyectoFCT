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
        <!-- ** -->
        <main class="action-container container-color-editar">
            <div>
                <img :src="imglotus" style="width: 185px;" alt="logo">
            </div>
            <!-- Control de alertas -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>
            <!-- ** -->
            <form @submit.prevent="actualizarUsuario">
                <div class="form-group">
                    <label for="dni">DNI</label>
                    <input type="text" id="dni" v-model="usuario.dni" :disabled="true">
                    <small class="text-muted">El DNI no se puede modificar.</small>
                </div>
                <div class="form-group">
                    <label for="login">LOGIN</label>
                    <input type="text" id="login" v-model="usuario.login" :disabled="esUsuarioLogeado" required>
                    <small v-if="esUsuarioLogeado" class="text-muted">Usuario conectado, login no modificable</small>
                </div>
                <div class="form-group">
                    <label for="password">PASSWORD</label>
                    <input type="password" id="password" v-model="usuario.password" required>
                </div>
                <div class="form-group">
                    <label for="rol">ROL</label>
                    <select id="rol" v-model="usuario.rol" class="form-select">
                        <option value="ADMIN">ADMIN</option>
                        <option value="USER">USER</option>
                    </select>
                </div>
                <div class="button-group">
                    <button type="submit" title="Guardar" class="action-button"><i
                            class="fas fa-thin fa-floppy-disk"></i></button>
                    <button type="button" title="Cancelar" class="action-button" @click="back"><i
                            class="fas fa-regular fa-xmark"></i></button>
                </div>

            </form>
        </main>
    </div>
</template>

<script>

import imglotus from '@/assets/lotus.webp';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

export default {
    name: 'EditarUsuarios',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: '',

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

            usuario: {}, // Objeto para el usuario que se está editando.
            redireccion: false // Controlar la redireccion. Volveremos a listar usuarios cuando se pulse el boton de guardar
        }
    },

    setup() {
        const router = useRouter();
        const route = useRoute();

        const logout = () => {
            localStorage.removeItem('authToken'); // Limpia el token de sesión
            localStorage.removeItem('sessionUser');
            router.push('/'); // Redirige a la página de login (asumiendo que tu ruta de login es '/')
        };

        const back = () => {
            router.push('/ListUsuarios');
        };

        return {
            logout,
            back,
            route,
            router,
        };
    },

    // Propiedad computada
    computed: {
        // Devuelve true si el login del usuario que queremos modificar es el mismo que el que está conectado.
        esUsuarioLogeado() {
            return this.usuario && this.usuario.login === this.sessionUser;
        }
    },
    async mounted() {
        /* Usuario logeado */
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('SESSIONUSER: ', this.sessionUser); // PUNTO DE CONTROL

        if (this.route.params && this.route.params.usuario) {
            /* Usuario a editar */
            this.usuario = JSON.parse(this.route.params.usuario);
            console.log('USUARIO CARGADO ', this.usuario); // PUNTO DE CONTROL
        } else {
            console.warn('No se encontró el usuario como parametro en la ruta.');
            ('USUARIO CARGADO ', this.usuario); // PUNTO DE CONTROL
            this.router.push('/ListUsuarios');
        }

    },

    methods: {

        async actualizarUsuario() {

            try {
                const token = localStorage.getItem('authToken');

                console.log('Usuario a actualizar ', this.usuario); // PUNTO DE CONTROL

                const response = await axios.put(`${this.apiUrl}/${this.version}/usuarios/actualizar`, this.usuario, {
                    headers: {
                        'Authorization': `Bearer ${token}`,

                    }
                });

                this.mensaje = await response.data; // El mensaje viene de la API.
                this.error = false;
                this.mostrarAlerta = true;
                this.redireccion = true;

            } catch (error) {

                this.error = true;
                if (error.response) {
                    this.mensaje = error.response.data;
                    console.log('API ERROR:  ', this.mensaje); // PUNTO DE CONTROL


                } else {
                    if (error.request) {
                        this.mensaje = 'Operacion de actualización denegada. Posible fallo con el servidor';
                    }
                }

                this.mostrarAlerta = true;
                this.redireccion = false;
            }

        },

        cerrarAlerta() {
            this.mostrarAlerta = false;
            if (this.redireccion) {
                this.router.push('/ListUsuarios');
            }
        },
    }
}    
</script>

<style lang="css" scoped>
select {
    margin: 0;
    width: 400px;
}

.action-container {
    gap: 15%;
}

.form-group {
    width: 60%;
    margin-bottom: 2%;
}

.alert {
    width: 60%;
}

.alert-danger {
    width: 60%;
}
</style>
