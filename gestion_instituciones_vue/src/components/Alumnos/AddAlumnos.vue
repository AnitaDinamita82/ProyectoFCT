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

            <form @submit.prevent="guardarAlumno">
                <div class="form-group">
                    <input type="text" id="dni" v-model="nuevoAlumno.dni" placeholder="DNI" required>
                </div>
                <div class="form-group">
                    <input type="text" id="nombre" v-model="nuevoAlumno.nombre" placeholder="Nombre" required>
                </div>
                <div class="form-group">
                    <input type="text" id="apellido1" v-model="nuevoAlumno.apellido1" placeholder="Primer apellido"
                        required>
                </div>
                <div class="form-group">
                    <input type="text" id="apellido2" v-model="nuevoAlumno.apellido2" placeholder="Segundo apellido"
                        required>
                </div>
                <button type="submit" class="action-button"><i class="fas fa-plus-circle"></i><span>Añadir nuevo
                        Alumno</span>
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
    name: 'AddAlumnos',
    data() {
        return {
            imglotus: imglotus,
            sessionUser: null,

            nuevoAlumno: {
                dni: '',
                nombre: '',
                apellido1: '',
                apellido2: '',
            },
            apiUrl: 'http://localhost:8080',
            version: '1.0',

            mensaje: '',
            error: false,
            mostrarAlerta: false // Controla la visibilidad de la alerta
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
            router.push('/GestAlumnos'); // Redirige al indice.
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
        async guardarAlumno() {
            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/alumnos/alta`, this.nuevoAlumno, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.mensaje = `Se ha dado al alumno ${response.data.nombre} ${response.data.apellido1} ${response.data.apellido2} de alta con exito `
                this.error = false;
                this.mostrarAlerta = true;

            } catch (error) {
                this.error = true;

                if (error.response && error.response.status === 409) { // El usuario ya existe.
                    this.mensaje = error.response.data; // Se captura el error del backend
                } else {
                    this.mensaje = 'Error en la operación de alta, posible problema de conexiósn con el servidor';
                }

                this.mostrarAlerta = true;
            }
            this.limpiarFormulario();
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.router.push('/ListAlumnos');
        },
        limpiarFormulario() {
            this.nuevoAlumno = { dni: '', nombre: '', apellido1: '', apellido2: '' };
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