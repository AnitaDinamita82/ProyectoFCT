<!-- Manejara toda la logica de eliminar un usuario y la de buscarlo para su edicion-->

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
        <main class="action-container container-color-listar">
            <div>
                <img :src="imglotus" style="width: 185px;" alt="logo">
            </div>
            <!-- Gestion de alerta  -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>
            <!-- ** -->

            <div v-else-if="usuarios?.length > 0">

                <!-- Logica para buscar y editar un usuario -->
                <form @submit.prevent="buscarUsuario">
                    <div class="search-section">
                        <input class="search-input" type="text" id="dniABuscar" v-model="dniABuscar" placeholder="DNI"
                            required>
                        <button type="submit" class="action-button color-button-buscar"><i
                                class="fas fa-search"></i></button>
                    </div>
                </form>
                <!-- *** -->
                <table>
                    <thead>
                        <tr>
                            <th>DNI</th>
                            <th>LOGIN</th>
                            <th>ROL</th>
                            <th>ACCION</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="usuario in usuarios" :key="usuario.id">
                            <td>{{ usuario.dni }}</td>
                            <td>{{ usuario.login }}</td>
                            <td>{{ usuario.rol }}</td>
                            <td><button type="submit" class="action-button color-button-eliminar"
                                    :disabled="usuario.login === sessionUser"
                                    :class="{ 'disabled-button': usuario.login === sessionUser }"
                                    @click="confirmarBaja(usuario.dni)"><i class="fas fa-trash"></i></button></td>
                        </tr>
                    </tbody>
                </table>
                <!-- Logica para poder ir al componente que gestiona el añadir usuarios -->
                <div>
                    <router-link to="AddUsuarios" class="style-router-link"><button
                            class="action-button color-button-añadir">AÑADIR NUEVO USUARIO</button></router-link>
                </div>
            </div>
            <p v-else>No hay usuarios registrados en la base de datos.</p>
        </main>
    </div>
</template>


<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';
import axios from 'axios';



export default {
    name: 'ListUsuarios',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            usuarios: [],

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

            dniABuscar: '', // Término de busqueda
        }
    },
    setup() {

        const router = useRouter();

        const logout = () => {
            localStorage.removeItem('authToken');
            localStorage.removeItem('sessionUser');
            router.push('/');

        };
        const back = () => {
            router.push('/GestUsuarios'); // Redirige al indice.
        };

        return {
            logout,
            back,
        }
    },

    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);
        this.listarUsuarios();
    },

    methods: {

        async listarUsuarios() {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;


            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/usuarios/listar`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.usuarios = response.data;
                console.log('LISTA DE USUARIOS', this.usuarios) // PUNTO DE CONTROL

            } catch (error) {
                this.error = true;
                this.mensaje = 'Fallo al cargar la lista de usuarios, parece que hay un problema de conexión con el servidor.';
                this.mostrarAlerta = true;
                console.error('Fallo al cargar la lista de usuarios'); // PUNTO DE CONTROL

            }
        },

        /* Logica de gestion para la baja del usuario elegido */
        confirmarBaja(userDni) {
            if (confirm('¿Esta seguro que quiere dar de baja a este usuario?')) {
                this.darDeBajaUsuario(userDni);
            }
        },

        async darDeBajaUsuario(userDni) {

            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.delete(`${this.apiUrl}/${this.version}/usuarios/baja/${userDni}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    validateStatus: (status) => status >= 200 && status < 300 || status === 400 || status === 404, // Para que no los tome como erroes reales
                });

                this.apiResponse(response);
                this.listarUsuarios();

            } catch (error) {
                if (error.request) {
                    this.error = true;
                    this.mensaje = 'Intento fallido de comunicación con el servidor.'; // La aplicacion no está en ejecucion.
                    this.mostrarAlerta = true;
                }

            }
        },
        async apiResponse(response) { // Los mensajes de exito o error vienen de la api.
            if (response.status === 200) {

                this.mensaje = await response.data;
                this.error = false;

            } else if (response.status === 400 || response.status === 404) {
                this.error = true;
                this.mensaje = await response.data;

            }
            this.mostrarAlerta = true;
        },


        /* Nos buscará el objeto del usuario seleccionado para posteriormente poder editarlo */
        async buscarUsuario() {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/usuarios/buscar/${this.dniABuscar}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.usuarioEncontrado = response.data;
                console.log('Usuario encontrado para edicion: ', this.usuarioEncontrado); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'EditarUsuarios',
                    params: { usuario: JSON.stringify(this.usuarioEncontrado) }
                });
            } catch (error) {

                if (error.response && error.response.status === 404) {
                    this.error = true;
                    this.mensaje = 'Fallo en la búsqueda parece que el/la usuari@ no existe. Compruebe los datos.';
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexion con el servidor al intentar buscar al usuario.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al realizar la busqueda.';
                }

                this.mostrarAlerta = true;
                this.limpiarFormulario();
            }
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
        },
        limpiarFormulario() {
            this.dniABuscar = '';
            this.usuarioEncontrado = null;
        },
    }
};    
</script>

<style lang="css" scoped>
.header {
    margin-top: 2%;
}

.disabled-button {
    background-color: #cccccc !important;
    /* Muestra el icono de prohibido */
    cursor: not-allowed !important;
    opacity: 0.7;
}

form {
    display: flex;
    flex-direction: column;
    /* Para apilar los form-groups verticalmente */
    gap: 15px;
    /* Espacio entre cada grupo de formulario */
    width: 100%;
    /* CLAVE: Limita el ancho total del formulario, haciendo que los inputs se adapten */
    max-width: 450px;
    /* Ajusta este valor para controlar el ancho general del formulario */
    margin: auto;
    /* Centra el formulario dentro de .action-container */
}

.alert {
    width: 40%;
}

.alert-danger {
    width: 60%;
}
</style>
