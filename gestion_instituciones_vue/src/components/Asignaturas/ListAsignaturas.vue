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
        <!-- ** *-->

        <main class="action-container container-color-listar">
            <div>
                <img :src="imglotus" style="width: 155px;" alt="logo">
            </div>
            <!-- Gestion de alerta  -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>
            <!-- ** -->

            <div v-else-if="asignaturas?.length > 0">

                <!-- Logica para buscar y editar una asignatura -->

                <div class="control-section">

                    <form @submit.prevent="buscarAsignatura">
                        <div class="search-section">
                            <input class="search-input" type="text" id="codigoABuscar" v-model="codigoABuscar"
                                placeholder="Código" required>
                            <button type="submit" class="action-button"><i class="fas fa-search"></i></button>
                            <router-link to="AddAsignaturas" class="style-router-link"><button class="action-button"><i
                                        class="fas fa-plus-circle"></i><span>Añadir nueva
                                        Asignatura</span></button>
                            </router-link>
                        </div>
                    </form>
                </div>

                <!-- *** -->

                <table>
                    <thead>
                        <tr>
                            <th>CÓDIGO</th>
                            <th>NOMBRE</th>
                            <th>DESCRIPCIÓN</th>
                            <th>ACCIÓN</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="asignatura in asignaturas" :key="asignatura.id">
                            <td>{{ asignatura.codigo }}</td>
                            <td>{{ asignatura.nombre }}</td>
                            <td>{{ asignatura.descripcion }}</td>
                            <td class="action-cell">
                                <button type="submit" title="Eliminar" class="action-button"
                                    @click="confirmarBaja(asignatura.codigo)"><i class="fas fa-trash"></i>
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <!-- Logica para poder ir al componente que gestiona el añadir asignatura -->
                <div>
                    <router-link to="AddAsignaturas" class="style-router-link"><button class="action-button"><i
                                class="fas fa-plus-circle"></i><span>Añadir nueva
                                Asignatura</span></button></router-link>
                </div>
            </div>
            <p v-else>No se puede cargar la lista de asignaturas disponibles.</p>
        </main>
        <BotonSubir />
    </div>
</template>


<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';
import axios from 'axios';
import BotonSubir from '@/components/Acciones/BotonSubir.vue';


export default {
    name: 'ListAsignaturas',
    components: {
        BotonSubir
    },

    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            asignaturas: [], // Array para almacenar las asignaturas.

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

            codigoABuscar: '',
            asignaturaEncontrada: null, // Asignatura activa para la que se van a mostrar los datos para editar.
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
            router.push('/GestAsignaturas'); // Redirige al indice.
        };

        return {
            logout,
            back,
            router,
        }
    },

    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);
        this.listarAsignaturas();
    },

    methods: {

        async listarAsignaturas() {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.asignaturas = [];

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/asignaturas/listar`, { // Llamada a la API de listar asignaturas
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.asignaturas = response.data;
                console.log('LISTA DE ASIGNATURAS', this.asignaturas) // PUNTO DE CONTROL

                if (response.status === 404) {
                    this.error = true;
                    this.mensaje = await response.data; // El mensaje viene de la API
                    this.mostrarAlerta = true;
                }

            } catch (error) {
                this.error = true;
                this.mensaje = 'Fallo al cargar la lista de asignaturas, parece que hay un problema de conexión con el servidor.';
                this.mostrarAlerta = true;
                console.error('Fallo al cargar la lista de asignaturas', error); // PUNTO DE CONTROL

            }
        },
        /* Logica de gestion para la baja de la asignatura elegida */
        confirmarBaja(codAsignatura) {
            if (confirm('¿Esta seguro que quiere dar de baja esta asignatura?')) {
                this.darDeBajaAsignatura(codAsignatura);
            }
        },

        async darDeBajaAsignatura(codAsignatura) {

            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.delete(`${this.apiUrl}/${this.version}/asignaturas/baja/${codAsignatura}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    },
                    validateStatus: (status) => status >= 200 && status < 300 || status === 400 || status === 404 || status === 409, // Para que no los tome como erroes reales
                });

                this.apiResponse(response);
                this.listarAsignaturas();

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

        async buscarAsignatura() {

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/asignaturas/buscar/${this.codigoABuscar}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                this.asignaturaEncontrada = response.data;
                console.log('Asignatura encontrada para edicion: ', this.asignaturaEncontrada); // PUNTO DE CONTROL

                this.$router.push({
                    name: 'EditarAsignaturas',
                    params: { asignatura: JSON.stringify(this.asignaturaEncontrada) }
                });

            } catch (error) {

                if (error.response && error.response.status === 404) {
                    this.error = true;
                    this.mensaje = 'Fallo en la búsqueda parece que la asignatura no existe. Compruebe los datos.';
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'Posible fallo de conexion con el servidor al intentar buscar la asignatura.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error inesperado al realizar la busqueda.';
                }

                this.mostrarAlerta = true;
                this.limpiarFormulario();
            }

        },
        limpiarFormulario() {
            this.codigoABuscar = '';
            this.asignaturaEncontrada = null;
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
        },
    }
};    
</script>

<style lang="css" scoped>
.action-container {
    margin-top: 2%;
    margin-bottom: 2%;

}

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
    width: 40%;
}

.alert-danger {
    width: 60%;
}
</style>