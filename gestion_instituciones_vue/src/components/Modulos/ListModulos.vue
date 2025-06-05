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

            <div v-else-if="modulos?.length > 0">

                <!-- Logica para buscar y editar un modulo -->
                <form @submit.prevent="buscarModulo">
                    <div class="search-section">
                        <input class="search-input" type="text" id="codigoABuscar" v-model="codigoABuscar"
                            placeholder="Código Módulo" required>
                        <button type="submit" class="action-button color-button-buscar"><i
                                class="fas fa-search"></i></button>
                    </div>
                </form>

                <table>
                    <thead>
                        <tr>
                            <th>CÓDIGO</th>
                            <th>NOMBRE</th>
                            <th>CURSO</th>
                            <th>GRUPO</th>
                            <th>ACCIONES</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="modulo in modulos" :key="modulo.id">
                            <td>{{ modulo.codigoModulo }}</td>
                            <td>{{ modulo.nombreModulo }}</td>
                            <td>{{ modulo.curso }}</td>
                            <td>{{ modulo.grupo }}</td>
                            <td>
                                <button type="submit" class="action-button color-button-asignaciones"
                                    @click="gestionarAsignaciones(modulo.codigoModulo)"><i
                                        class="fas fa-network-wired"></i></button>
                                <button type="submit" class="action-button color-button-ver"
                                    @click="verModuloCompleto(modulo)"><i class="fas fa-light
                                        fa-eye"></i></button>
                                <button type="submit" class="action-button color-button-eliminar"
                                    @click="confirmarBaja(modulo.codigoModulo)"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div>
                    <!-- Logica para poder ir al componente que gestiona el añadir modulo -->
                    <router-link to="/AddModulos" class="style-router-link"><button
                            class="action-button color-button-añadir">AÑADIR NUEVO MÓDULO</button></router-link>
                </div>
            </div>
            <p v-else>No se puede cargar la lista o no disponemos de módulos registrados.</p>
        </main>
    </div>
</template>

<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';
import axios from 'axios';

export default {
    name: 'ListModulos',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            modulos: [], // Array para almacenar todos los módulos
            moduloSeleccionado: null, // Módulo para el que se gestionan asignaciones
            moduloAEditar: null, // Módulo que se está editando

            dniAlumnoAAsignar: '',
            codigoAsignaturaAAsignar: '',

            mensaje: '',
            error: false,
            mostrarAlerta: false,

            codigoABuscar: '',
            moduloEncontrado: null // Modulo activo para quien se van a mostrar los datos para editar*/
            // mostrarModalAsignaciones: false,
            // mostrarModalEdicion: false,
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
            router.push('/GestModulos');
        };

        return {
            logout,
            back,
            router
        }
    },
    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        this.listarModulos();
    },
    methods: {
        // --- Métodos de Gestión de Módulos (CRUD) ---

        async listarModulos() {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.modulos = [];

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/listar`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.modulos = response.data;
                console.log('LISTA DE MÓDULOS', this.modulos); // PUNTO DE CONTROL

                if (response.status === 404) {
                    this.error = true;
                    this.mensaje = await response.data; // El mensaje viene de la API
                    this.mostrarAlerta = true;
                }

            } catch (error) {
                this.error = true;
                this.mensaje = 'Fallo al cargar la lista de módulos, parece que hay un problema de conexión con el servidor.';
                this.mostrarAlerta = true;
                console.error('Fallo al cargar la lista de módulos', error);
            }
        },

        /* Logica de gestion para la baja del modulo elegido */

        confirmarBaja(codigoModulo) {
            if (confirm('¿Está seguro que quiere dar de baja este módulo?')) {
                this.darDeBajaModulo(codigoModulo);
            }
        },
        async darDeBajaModulo(codigoModulo) {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.delete(`${this.apiUrl}/${this.version}/modulos/baja/${codigoModulo}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    validateStatus: (status) => status >= 200 && status < 300 || status === 400 || status === 404 || status === 409,
                });

                this.apiResponse(response);
                this.listarModulos(); // Refrescar la lista de módulos

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

            } else if (response.status === 400 || response.status === 404 || response.status === 409) {
                this.error = true;
                this.mensaje = await response.data;

            }
            this.mostrarAlerta = true;
        },
        async buscarModulo() {
            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/buscar/${this.codigoABuscar}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.moduloEncontrado = response.data;
                console.log('Modulo encontrado para edicion: ', this.moduloEncontrado); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'EditarModulos',
                    params: { modulo: JSON.stringify(this.moduloEncontrado) }
                });


            } catch (error) {
                if (error.response && error.response.status === 404) {
                    this.error = true;
                    this.mensaje = 'Fallo en la búsqueda, el módulo no existe. Compruebe el código.';
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexión con el servidor al intentar buscar el módulo.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al realizar la búsqueda.';
                }
                this.mostrarAlerta = true;
                this.limpiarFormularioBusqueda();
            }
        },
        async gestionarAsignaciones(codigoModulo) {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/buscar/${codigoModulo}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.moduloEncontrado = response.data;
                console.log('Modulo encontrado para gestionar: ', this.moduloEncontrado); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'AsignacionesAsignaturasModulos',
                    params: { modulo: JSON.stringify(this.moduloEncontrado) }
                });


            } catch (error) {

                if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexión con el servidor al intentar buscar el módulo.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al realizar la búsqueda.';
                }
                this.mostrarAlerta = true;
            }
        },
        async verModuloCompleto(modulo) {
            this.$router.push({
                name: 'VerModuloCompleto', // Este será el nombre de la ruta que definiremos
                params: { modulo: JSON.stringify(modulo) }
            });
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
        },
        limpiarFormularioBusqueda() {
            this.codigoABuscar = '';
        },
    }
};
</script>

<style lang="css" scoped>
.action-container {
    margin-top: 0;
}

th:nth-child(5),
td:nth-child(5) {
    width: 150px;
    /* Un ancho suficiente para 3 iconos de 32px + gaps. Ajusta si es necesario. */
    min-width: 120px;
    /* Para asegurar que no se haga demasiado pequeña en pantallas pequeñas */
    text-align: center;
    /* Centra el contenido en la cabecera */

    /* Aquí es donde convertimos el TD en un contenedor flexbox */
    display: flex;
    justify-content: center;
    /* Centra horizontalmente los botones dentro de la celda */
    align-items: center;
    /* Alinea verticalmente los botones */
    gap: 8px;
    /* Espacio entre cada botón (8px es un buen punto de partida) */
    flex-wrap: nowrap;
    /* Evita que los botones salten de línea si el espacio es justo */
}

.alert {
    width: 40%;
}

.alert-danger {
    width: 60%;
}
</style>
