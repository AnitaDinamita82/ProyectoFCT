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
                <button class="logout-button" title="Cancelar" @click="logout"><i class="fas fa-power-off"></i></button>
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

            <form @submit.prevent="guardarAsignatura">
                <div class="form-group">
                    <input type="text" id="codigo" v-model="nuevaAsignatura.codigo" placeholder="Código" required>
                </div>
                <div class="form-group">
                    <input type="text" id="nombre" v-model="nuevaAsignatura.nombre" placeholder="Nombre" required>
                </div>
                <div class="form-group">
                    <input type="text" id="descripcion" v-model="nuevaAsignatura.descripcion" placeholder="Descripción">
                </div>
                <button type="submit" class="action-button"><i class="fas fa-plus-circle"></i><span>Añadir nueva
                        asignatura</span>
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
    name: 'AddAsignaturas',
    data() {
        return {
            imglotus: imglotus,
            sessionUser: null,

            nuevaAsignatura: {
                codigo: '',
                nombre: '',
                descripcion: '',
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
            router.push('/GestAsignaturas'); // Redirige al indice.
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
        async guardarAsignatura() {
            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/asignaturas/alta`, this.nuevaAsignatura, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.mensaje = `Se ha dado la asignatura ${response.data.nombre} con codigo ${response.data.codigo} de alta con exito `
                this.error = false;
                this.mostrarAlerta = true;

            } catch (error) {
                this.error = true;
                console.log("que tengo en respose: ", error.response);
                if (error.response && error.response.status === 409) { // La asignatura ya existe
                    console.log("La asignatura ya existe");
                    this.mensaje = error.response.data; // Se captura el error del backend
                    this.mostrarAlerta = true;
                } else {
                    this.mensaje = 'Error en la operación de alta, posible problema de conexiósn con el servidor';
                    this.mostrarAlerta = true;
                }

            }
            this.limpiarFormulario();

        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.router.push('/ListAsignaturas');
        },
        limpiarFormulario() {
            this.nuevaAsignatura = { codigo: '', nombre: '', descripcion: '' };
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