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

        <main class="action-container container-color-editar">
            <div>
                <img :src="imglotus" style="width: 185px;" alt="logo">
            </div>
            <!-- Control de alertas -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>
            <form @submit.prevent="actualizarModulo">
                <div class="form-group">
                    <label for="dni">CÓDIGO</label>
                    <input type="text" id="codigoModulo" v-model="modulo.codigoModulo" placeholder="Código Módulo"
                        required>
                    <small class="text-muted">El CODIGO no se puede modificar.</small>
                </div>
                <div class="form-group">
                    <label for="nombre">NOMBRE</label>
                    <input type="text" id="nombreModulo" v-model="modulo.nombreModulo" placeholder="Nombre" required>
                </div>
                <div class="form-group">
                    <label for="curso">CURSO</label>
                    <select id="curso" class="form-select" v-model="modulo.curso" required>
                        <option value="Primero">PRIMERO</option>
                        <option value="Segundo">SEGUNDO</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="grupo">GRUPO</label>
                    <select id="grupo" class="form-select" v-model="modulo.grupo" required>
                        <option value="Presencial">PRESENCIAL</option>
                        <option value="Online">ONLINE</option>
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

            modulo: {}, // Objeto para el módulo que se está editando
            redireccion: false // Controlar la redireccion. Volveremos al listar alumnos cuando se pulse el boton de guardar.
        }
    },
    setup() {
        const router = useRouter();
        const route = useRoute();

        const logout = () => {
            localStorage.removeItem('authToken'); // Limpia el token de sesión
            localStorage.removeItem('sessionUser');
            router.push('/');
        };

        const back = () => {
            router.push('/ListModulos');
        };

        return {
            logout,
            back,
            route,
            router
        };
    },

    async mounted() {

        /* Usuario logeado */
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('SESSIONUSER: ', this.sessionUser); // PUNTO DE CONTROL


        if (this.route.params && this.route.params.modulo) {
            /* Modulo a editar */
            this.modulo = JSON.parse(this.route.params.modulo);
            console.log('MODULO CARGADO: ', this.modulo);  // PUNTO DE CONTROL 

        } else {
            console.warn('No se encontró el modulo como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('MODULO CARGADO: ', this.Modulo);  // PUNTO DE CONTROL 
            this.router.push('/ListModulos');
        }

    },
    methods: {

        async actualizarModulo() {

            try {
                const token = localStorage.getItem('authToken');
                console.log('Modulo a actualizar ', this.modulo); // PUNTO DE CONTROL

                const response = await axios.put(`${this.apiUrl}/${this.version}/modulos/actualizarModulo`, this.modulo, {
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
                this.router.push('/ListModulos');
            }
        },
    }
}
</script>
<style scoped lang="css">
.action-container {
    gap: 15%;
}

.form-group {
    width: 65%;
    margin-bottom: 1%;
}

.alert {
    width: 60%;
}

.alert-danger {
    width: 60%;
}
</style>
