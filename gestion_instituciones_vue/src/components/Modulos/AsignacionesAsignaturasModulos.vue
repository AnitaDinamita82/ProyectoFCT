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
        <!-- ** -->

        <header class="header">
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
            <h1>{{ modulo.nombreModulo }} - ({{ modulo.curso }}/{{
                modulo.grupo }})</h1>
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
        </header>
        <main class="action-container container-color-listar">

            <!-- Control de alertas -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>

            <!-- Seccion para asignar asignaturas al modulo -->
            <div v-if="modulo" class="gestion-sections">

                <div class="section-column">
                    <h3>Asignaturas en este Módulo</h3>
                    <div v-if="asignaturasEnModulo.length > 0">
                        <table>
                            <tbody>
                                <tr v-for="asignatura in asignaturasEnModulo" :key="asignatura.id">
                                    <td> {{ asignatura.nombre }} ({{ asignatura.codigo }})</td>
                                    <td class="action-cell">
                                        <button class="action-button" title="Eliminar"
                                            @click="confirmarBaja(asignatura.codigo, modulo.codigoModulo)">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <p v-else>No hay asignaturas asignadas a este módulo.</p>
                </div>
                <div class="section-column">
                    <h3>Asignaturas Disponibles</h3>
                    <div v-if="asignaturasDisponibles.length > 0">
                        <table>
                            <tbody>
                                <tr v-for="asignatura in asignaturasDisponibles" :key="asignatura.id">
                                    <td> {{ asignatura.nombre }} ({{ asignatura.codigo }})</td>
                                    <td class="action-cell">
                                        <div class="action-button" title="Añadir"
                                            @click="asignarAsignatura(modulo.codigoModulo, asignatura.codigo)">
                                            <i class="fas fa-plus-circle"></i>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <p v-else>No hay más asignaturas disponibles para añadir.</p>
                </div>
            </div>
            <p v-else>Cargando información o sin datos que mostrar.</p>
        </main>
        <BotonSubir />
    </div>
</template>
<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';
import BotonSubir from '@/components/Acciones/BotonSubir.vue';

export default {
    name: 'AsignacionesAsignaturasModulos',
    components: {
        BotonSubir
    },

    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            modulo: {}, // Objeto para el módulo para el que se van a listar las asingaturas

            todasLasAsignaturas: [], // Almacenará todas las asignaturas dadas de alta en BD.
            asignaturasDisponibles: [], // Las asignaturas disponibles para asignar.
            asignaturasEnModulo: [], // Almacenará las asignaturas ya asignadas

            mensaje: '',
            error: false,
            mostrarAlerta: false, // Controla la visibilidad de la alerta

        }
    },
    setup() {

        const router = useRouter();
        const route = useRoute();

        const logout = () => {
            localStorage.removeItem('authToken');
            localStorage.removeItem('sessionUser');
            router.push('/');

        };

        const back = () => {
            router.push('/ListModulos'); // Redirige al indice.
        };

        return {
            logout,
            back,
            router,
            route
        }
    },
    async mounted() {

        /* Usuario logeado */
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('SESSIONUSER: ', this.sessionUser); // PUNTO DE CONTROL

        if (this.route.params && this.route.params.modulo) {

            /* Modulo a gestionar */
            this.modulo = JSON.parse(this.route.params.modulo);
            console.log('MODULO CARGADO: ', this.modulo); // PUNTO DE CONTROL
            await this.cargarAsignaturasParaAsignar(this.modulo.codigoModulo);

        } else {
            console.warn('No se encontró el modulo como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('MODULO CARGADO: ', this.modulo);  // PUNTO DE CONTROL 
            this.router.push('/ListModulos');
        }
    },
    methods: {

        async cargarAsignaturasParaAsignar(codigoModulo) {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.asignaturasDisponibles = [];

            try {
                const token = localStorage.getItem('authToken');

                // 1. Obtener todas las asignaturas
                const response = await axios.get(`${this.apiUrl}/${this.version}/asignaturas/listar`, { // Llamada a la API de listar asignaturas
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.todasLasAsignaturas = await response.data;
                console.log('TODAS LAS ASIGNATURAS:', this.todasLasAsignaturas); // PUNTO DE CONTROL.

                // 2. Obtener asignaturas ya asignadas a este módulo
                console.log('COD MODULO:', codigoModulo); // PUNTO DE CONTROL.
                const todasLasDelModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturasModulo/${codigoModulo}`, { // Llamada a la API de listar asignaturas
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.asignaturasEnModulo = todasLasDelModulo.data;
                console.log('ASIGNATURAS YA EN EL MODULO:', this.asignaturasEnModulo); // PUNTO DE CONTROL.

                // 3. Filtrar las asignaturas disponibles (todas - las ya en modulo).
                const codigosAsignaturasEnModulo = new Set(this.asignaturasEnModulo.map(a => a.codigo));
                this.asignaturasDisponibles = this.todasLasAsignaturas.filter(asignatura => !codigosAsignaturasEnModulo.has(asignatura.codigo));

            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;
                if (error.request) {
                    this.mensaje = 'Imposible cargar las listas de asignaturas. Posible fallo de conexion con el servidor.'
                    console.error('Error de red al cargar asignaturas:', error); //PUNTO DE CONTROL
                }
            }
        },
        async asignarAsignatura(codigoModulo, codigoAsignatura) {


            console.log('Que tengo en modulo: ', codigoModulo); // PUNTO DE CONTROL.
            console.log('Que tengo en asignaturas: ', codigoAsignatura); // PUNTO DE CONTROL.

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {

                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/modulos/asignarAsignaturasAModulo/${codigoModulo}`, {
                    codigoAsignatura: codigoAsignatura
                }, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                });
                console.log('API AsignarAsignaturas:', response.data); // PUNTO DE CONTROL.
                this.mensaje = response.data;
                this.error = false;
                this.mostrarAlerta = true;


                await this.cargarAsignaturasParaAsignar(codigoModulo); // Recargar las listas

            } catch (error) {

                this.error = true;
                this.mostrarAlerta = true;

                if (error.response && error.response.status === 409) {
                    this.mensaje = error.data;

                } else if (error.request) {
                    this.mensaje = 'Imposible asignar la asignatura. Posible fallo de conexión con el servidor.';
                    console.error('Error de red al asignar asignatura:', error);
                } else {
                    this.mensaje = 'Ocurrió un error inesperado al asignar la asignatura.';
                    console.error('Error al asignar asignatura:', error);
                }
            }
        },
        confirmarBaja(codigoAsignatura, codigoModulo) {
            this.verficarAlumnosEnAsignatura(codigoAsignatura, codigoModulo);
            /*  if (confirm('¿Estás seguro de que quieres desvincular esta asignatura del módulo?')) {
                  this.bajaAsignatura(codigoAsignatura);
              }*/
        },

        async verficarAlumnosEnAsignatura(codigoAsignatura, codigoModulo) {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            confirm(codigoAsignatura);
            confirm(codigoModulo);
            try {
                const token = localStorage.getItem('authToken');

                const response = await axios.get(`${this.apiUrl}/${this.version}/matricula/alumnosMatriculadosPorModulo/${codigoAsignatura}/${codigoModulo}`, { // Llamada a la API de listar asignaturas
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });


                console.log("Que tengo cuando salgo del metodo", response.data);
                const numeroAlumnos = response.data; // Devuelve una lista
                // console.log(`Número de alumnos matriculados en ${codigoAsignatura} para el módulo ${codigoModulo}: ${numeroAlumnos}`); // PUNTO DE CONTROL


                if (numeroAlumnos.length > 0) {
                    this.error = true;
                    this.mensaje = `No se puede desvincular la asignatura porque tiene ${numeroAlumnos.length} alumno(s) matriculado(s).`;
                    this.mostrarAlerta = true;

                } else {
                    // Si no hay alumnos, procede con la confirmación y la baja
                    if (confirm('¿Estás seguro de que quieres desvincular esta asignatura del módulo? No tiene alumnos matriculados.')) {
                        this.bajaAsignatura(codigoAsignatura);
                    }
                }

            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;

                if (error.request) {
                    this.mensaje = 'Imposible verificar los alumnos. Posible fallo de conexión con el servidor.';
                    console.error('Error de red al verificar alumnos:', error);
                }
            }
        },

        async bajaAsignatura(codigoAsignatura) {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {
                const token = localStorage.getItem('authToken');

                const url = `${this.apiUrl}/${this.version}/modulos/desasignarAsignaturasDeModulo/${this.modulo.codigoModulo}/${codigoAsignatura}`;

                const response = await axios.delete(url, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                });

                this.apiResponse(response);
                await this.cargarAsignaturasParaAsignar(this.modulo.codigoModulo); // Recargar las listas

            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;
                if (error.request) {
                    this.mensaje = 'Imposible desvincular la asignatura. Posible fallo de conexión con el servidor.';
                    console.error('Error de red al desvincular asignatura:', error);
                } else if (error.response && error.response.status === 404) {
                    this.mensaje = 'La asignatura o el módulo no se encontraron.';
                } else {
                    this.mensaje = 'Ocurrió un error inesperado al desvincular la asignatura.';
                    console.error('Error al desvincular asignatura:', error);
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
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.mensaje = '';
        },
    },
}
</script>

<style scoped lang="css">
.header {
    margin-top: 2%;
}

.gestion-sections {
    display: flex;
    gap: 30px;
    width: 90%;

}

table {
    width: 500px;
}

.section-column {
    flex: 1;
    background-color: #f9f9f9;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h3 {
    margin-top: 0;
    color: #555;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
    margin-bottom: 15px;
}

.alert {
    width: 60%;
}

.alert-danger {
    width: 60%;
}
</style>
