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

            <div v-else-if="alumnos?.length > 0">

                <!-- Logica para buscar y editar un alumno -->
                <form @submit.prevent="buscarAlumno">
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
                            <th>NOMBRE COMPLETO</th>
                            <th>ACCIONES</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="alumno in alumnos" :key="alumno.id">
                            <td>{{ alumno.dni }}</td>
                            <td>{{ alumno.nombre }} {{ alumno.apellido1 }} {{ alumno.apellido2 }} </td>
                            <td>
                                <button type="submit" class="action-button color-button-matricular"
                                    @click="matricular(alumno.dni)"><i
                                        class="fas fa-sharp-duotone fa-regular fa-file-pen"></i></button>
                                <button type="submit" class="action-button color-button-ver"
                                    @click="verMatriculaCompleta(alumno)"><i class="fas fa-light
                                        fa-eye"></i></button>
                                <button type="submit" class="action-button color-button-eliminar"
                                    @click="confirmarBaja(alumno.dni)"><i class="fas fa-trash"></i></button>

                            </td>

                        </tr>
                    </tbody>
                </table>
                <!-- Logica para poder ir al componente que gestiona el añadir alumno -->
                <div>
                    <router-link to="AddAlumnos" class="style-router-link"><button
                            class="action-button color-button-añadir">AÑADIR NUEVO ALUMNO</button></router-link>
                </div>
            </div>
            <p v-else>No se puede cargar la lista o no disponemos de alumnos matriculados.</p>
        </main>
    </div>
</template>


<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';
import axios from 'axios';



export default {
    name: 'ListAlumnos',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            alumnos: [], // Array de alumnos para almacenar a todos los alumnos para poderlos listar.
            asignaturas: [], // Array para almacenar las asignaturas del alumno seleccionado

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

            dniABuscar: '',
            alumnoEncontrado: null // Alumno activo para quien se van a mostrar los datos para editar*/
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
            router.push('/GestAlumnos'); // Redirige al indice.
        };

        return {
            logout,
            back,
            router
        }
    },

    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);
        this.listarAlumnos();
    },

    methods: {

        async listarAlumnos() {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.alumnos = [];

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/alumnos/listar`, { // Llamada a la API de listar alumnos
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.alumnos = response.data;
                console.log('LISTA DE ALUMNOS', this.alumnos) // PUNTO DE CONTROL

                if (response.status === 404) {
                    this.error = true;
                    this.mensaje = await response.data; // El mensaje viene de la API
                    this.mostrarAlerta = true;
                }

            } catch (error) {
                this.error = true;
                this.mensaje = 'Fallo al cargar la lista de alumnos, parece que hay un problema de conexión con el servidor.';
                this.mostrarAlerta = true;
                console.error('Fallo al cargar la lista de alumnos'); // PUNTO DE CONTROL

            }
        },

        /* Logica de gestion para la baja del alumno elegido */
        confirmarBaja(alumnoDni) {
            if (confirm('¿Esta seguro que quiere dar de baja a este alumno?')) {
                this.darDeBajaAlumno(alumnoDni);
            }
        },

        async darDeBajaAlumno(alumnoDni) {

            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.delete(`${this.apiUrl}/${this.version}/alumnos/baja/${alumnoDni}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    validateStatus: (status) => status >= 200 && status < 300 || status === 400 || status === 404 || status === 409, // Para que no los tome como erroes reales
                });

                this.apiResponse(response);
                this.listarAlumnos();

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

        async buscarAlumno() {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/alumnos/buscar/${this.dniABuscar}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.alumnoEncontrado = response.data;
                console.log('Alumno encontrado para edicion: ', this.alumnoEncontrado); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'EditarAlumnos',
                    params: { alumno: JSON.stringify(this.alumnoEncontrado) }
                });

            } catch (error) {

                if (error.response && error.response.status === 404) {
                    this.error = true;
                    this.mensaje = 'Fallo en la búsqueda parece que el/la alumn@ no existe. Compruebe los datos.';
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexion con el servidor al intentar buscar al alumn@.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al realizar la busqueda.';
                }

                this.mostrarAlerta = true;
                this.limpiarFormulario();
            }

        },
        async matricular(dniAlumno) {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/alumnos/buscar/${dniAlumno}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.alumnoEncontrado = response.data;
                console.log('Alumno encontrado para matricular: ', this.alumnoEncontrado); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'AltaMatricula',
                    params: { alumno: JSON.stringify(this.alumnoEncontrado) }
                });

            } catch (error) {

                if (error.response && error.response.status === 404) {
                    this.error = true;
                    this.mensaje = 'Fallo en la búsqueda del alumno para su matriculación. Compruebe los datos.';
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexion con el servidor al intentar matricular al alumn@.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al intentar realizar la matriculación.';
                }

                this.mostrarAlerta = true;
            }
        },
        verMatriculaCompleta(alumno) {
            this.router.push({
                name: 'VerMatricula', // Asegúrate de que esta ruta exista en tu router
                params: {
                    alumno: JSON.stringify(alumno)
                }
            });
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
        },
        limpiarFormulario() {
            this.dniABuscar = '';
            this.alumnoEncontrado = null;
        },
    }
};    
</script>

<style lang="css" scoped>
.action-container {
    margin-top: 0;
}

th:nth-child(3),
td:nth-child(3) {
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
