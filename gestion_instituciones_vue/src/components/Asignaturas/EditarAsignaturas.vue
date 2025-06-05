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
                <button class="logout-button" @click="logout"><i class="fas fa-power-off"></i></button>
            </div>
        </div>
        <!-- ** -->
        <!-- Bloque boton volver  -->
        <div class="action-volver">
            <button class="back-button" @click="back"><i class="fas fa-arrow-left"></i></button>
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
            <div v-if="asignatura">
                <form @submit.prevent="actualizarAsignatura">
                    <div class="form-group">
                        <label for="dni">CODIGO</label>
                        <input type="text" id="codigo" v-model="asignatura.codigo" :disabled="true">
                        <small class="text-muted">El codigo no se puede modificar.</small>
                    </div>
                    <div class="form-group">
                        <label for="nombre">NOMBRE</label>
                        <input type="text" id="nombre" v-model="asignatura.nombre" required>
                    </div>
                    <div class="form-group">
                        <label for="descripcion">DESCRIPCION</label>
                        <input type="text" id="descripcion" v-model="asignatura.descripcion" required>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="action-button color-button-guardar">GUARDAR</button>
                        <button type="button" class="action-button color-button-cancelar"
                            @click="volver">CANCELAR</button>
                    </div>

                </form>
            </div>
        </main>
    </div>
</template>

<script>

import imglotus from '@/assets/lotus.webp';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

export default {
    name: 'EditarAsignaturas',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: '',

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

            asignatura: {}, // Objeto para la asignatura que se está editando.
            redireccion: false // Controlar la redireccion. Volveremos al listar asignaturas cuando se pulse el boton de guardar.
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
            router.push('/ListAsignaturas');
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

        if (this.route.params && this.route.params.asignatura) {
            this.asignatura = JSON.parse(this.route.params.asignatura);
            console.log('ASIGNATURA CARGADA: ', this.asignatura);  // PUNTO DE CONTROL 

        } else {
            console.warn('No se encontró la asignatura como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('ASIGNATURA CARGADO: ', this.alumno);  // PUNTO DE CONTROL 
            this.router.push('/ListAsignatura');
        }

    },

    methods: {

        async actualizarAsignatura() {

            try {
                const token = localStorage.getItem('authToken');

                console.log('Asignatura a actualizar ', this.asignatura); // PUNTO DE CONTROL

                const response =  await axios.put(`${this.apiUrl}/${this.version}/asignaturas/actualizar`, this.asignatura, {
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
                this.router.push('/ListAsignaturas');
            }
        },
    }
}    
</script>

<style lang="css" scoped>
.action-container {
   margin-top: 0;
}

.form-group {
    width: 65%;
    margin-bottom: 2%;
}

.alert {
    width: 40%;
}

.alert-danger {
    width: 40%;
}
</style>