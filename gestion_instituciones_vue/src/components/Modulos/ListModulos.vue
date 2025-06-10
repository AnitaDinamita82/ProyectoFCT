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

                <div class="control-section">
                    <form @submit.prevent="buscarModulo">
                        <div class="search-section">
                            <input class="search-input" type="text" id="codigoABuscar" v-model="codigoABuscar"
                                placeholder="Código Módulo" required>
                            <button type="submit" class="action-button"><i class="fas fa-search"></i></button>
                            <router-link to="/AddModulos" class="style-router-link"><button class="action-button"><i
                                        class="fas fa-plus-circle"></i><span>Añadir nuevo
                                        Módulo</span></button></router-link>
                        </div>
                    </form>
                </div>

                <!-- *** -->

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
                            <td class="action-cell">
                                <button type="submit" title="Gestionar Asignaturas" class="action-button"
                                    @click="gestionarAsignaciones(modulo.codigoModulo)"><i
                                        class="fas fa-network-wired"></i></button>
                                <button type="submit" title="Ver Detalles Módulo" class="action-button"
                                    @click="verModuloCompleto(modulo)"><i class="fas fa-light
                                        fa-eye"></i></button>
                                <button type="submit" title="Gestionar Alumnos" class="action-button"
                                    @click="gestionarAlumnos()"><i class=" fas fa-thin fa-people-group"></i></button>
                                <button type="submit" title="Eliminar" class="action-button"
                                    @click="confirmarBaja(modulo.codigoModulo)"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <div>
                    <!-- Logica para poder ir al componente que gestiona el añadir modulo -->
                    <router-link to="/AddModulos" class="style-router-link">
                        <button class="action-button"><i class="fas fa-plus-circle"></i><span>Añadir nuevo
                                Módulo</span></button>
                    </router-link>
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
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarModulos`, {
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
                const response = await axios.delete(`${this.apiUrl}/${this.version}/modulos/bajaModulo/${codigoModulo}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                });


                this.error = false;
                this.mostrarAlerta = true;
                this.mensaje = await response.data;
                //  this.listarModulos(); // Refrescar la lista de módulos

            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;

                if (error.response) {
                    this.mensaje = await error.response.data;
                } else {
                    if (error.request) {

                        this.mensaje = 'Intento fallido de comunicación con el servidor.'; // La aplicacion no está en ejecucion.
                    }
                }
            }
        },

        async buscarModulo() {
            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/buscarModulo/${this.codigoABuscar}`, {
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
                const response = await axios.get(`${this.apiUrl}/${this.version}/modulos/buscarModulo/${codigoModulo}`, {
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
        async gestionarAlumnos() {
            this.router.push('/ListAlumnos');
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.listarModulos(); // Refrescar la lista de módulos
        },
        limpiarFormularioBusqueda() {
            this.codigoABuscar = '';
        },
    }
};
</script>

<style lang="css" scoped>
button span {
    color: #ee7724
}

.style-router-link {
    margin-left: auto;
}

.style-router-link button {
    margin-left: auto;
}

input[type="text"] {

    width: 30%;
}

table {
    width: 1000px;
}

.alert {
    width: 80%;
}

.alert-danger {
    width: 80%;
}
</style>
