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

            <form @submit.prevent="registrarModulo">
                <div class="form-group">
                    <input type="text" id="codigoModulo" v-model="modulo.codigoModulo" placeholder="Código Módulo"
                        required>
                </div>
                <div class="form-group">
                    <input type="text" id="nombreModulo" v-model="modulo.nombreModulo" placeholder="Nombre" required>
                </div>
                <div class="form-group">
                    <select id="curso" class="form-select" v-model="modulo.curso" required>
                        <option value="Primero">PRIMERO</option>
                        <option value="Segundo">SEGUNDO</option>
                    </select>
                </div>
                <div class="form-group">
                    <select id="grupo" class="form-select" v-model="modulo.grupo" required>
                        <option value="Presencial">PRESENCIAL</option>
                        <option value="Online">ONLINE</option>
                    </select>
                </div>
                <button type="submit" class="action-button"><i class="fas fa-plus-circle"></i><span>Añadir nuevo
                        Módulo</span>
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
    name: 'AddModulos',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,
            modulo: {
                codigoModulo: '',
                nombreModulo: '',
                curso: 'Primero',
                grupo: 'Presencial'
            },
            mensaje: '',
            error: false,
            mostrarAlerta: false,
        };
    },
    setup() {
        const router = useRouter();

        const logout = () => {
            localStorage.removeItem('authToken');
            localStorage.removeItem('sessionUser');
            router.push('/');
        };

        const back = () => {
            router.push('/GestModulos');
        };

        return {
            logout,
            back,
            router
        };
    },
    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
    },
    methods: {
        async registrarModulo() {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/modulos/altaModulo`, this.modulo, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    validateStatus: (status) => status >= 200 && status < 300 || status === 400 || status === 409
                });

                this.apiResponse(response);

                if (response.status === 200) {
                    this.limpiarFormulario();
                }

            } catch (error) {
                if (error.request) {
                    this.error = true;
                    this.mensaje = 'Intento fallido de comunicación con el servidor al registrar el módulo.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al registrar el módulo.';
                }
                this.mostrarAlerta = true;
            }
        },
        apiResponse(response) {
            if (response.status === 200) {
                this.mensaje = response.data;
                this.error = false;
            } else if (response.status === 400 || response.status === 409) {
                this.error = true;
                this.mensaje = response.data;
            }
            this.mostrarAlerta = true;
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.router.push('/ListModulos');
        },
        limpiarFormulario() {
            this.modulo = {
                codigoModulo: '',
                nombreModulo: '',
                curso: '',
                grupo: ''
            };
        }
    }
};
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