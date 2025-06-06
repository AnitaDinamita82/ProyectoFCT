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
                <button class="back-button" title="Vovler" @click="back"><i class="fas fa-arrow-left"></i></button>
                <button class="logout-button" title="Desconectar" @click="logout"><i
                        class="fas fa-power-off"></i></button>
            </div>
        </div>
        <!-- ** -->
        <!-- Boton Volver -->
        <div class="action-volver">

        </div>

        <main class="estadisticas-container">
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="btn-close" @click="cerrarAlerta"></button>
            </div>

            <div class="cards-container">
                <!-- Seccion para las estadisticas de Numero de Alumnos por Asignaturas -->
                <section class="estadisticas-card">
                    <h2>Número de Alumnos por Asignatura</h2>
                    <div v-if="errorAlumnosPorAsignatura" class="error-message">
                        Fallo al cargar el número de alumnos por asignatura. {{ msgErrorAPIX }}
                    </div>
                    <div v-if="alumnosPorAsignaturaPIEData.labels.length > 0">
                        <div>
                            <Pie :data="alumnosPorAsignaturaPIEData" :options="alumnosPorAsignaturaPIEOptions" />
                        </div>
                        <h3>Detalles</h3>
                        <table class="styled-table">
                            <thead>
                                <tr>
                                    <th>Código</th>
                                    <th>Asignatura</th>
                                    <th>Nº Alumnos</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(item, index) in datosDetalleGraficaAXA" :key="index">
                                    <td>{{ item.codigoAsignatura }}</td>
                                    <td>{{ item.nombreAsignatura }}</td>
                                    <td>{{ item.numeroAlumnos }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <p v-else>Cargando datos de la gráfica...</p>
                </section>

                <!-- Seccion para las estadisticas de Alumnos con mas asignaturas -->
                <section class="estadisticas-card">
                    <h2>Número de Asignaturas por Alumno</h2>
                    <div v-if="errorAlumnosConMasAsignaturas" class="error-message">
                        Fallo al cargar los alumnos con mas asignaturas. {{ msgErrorAPIXMas }}
                    </div>
                    <div v-if="alumnosConMasAsignaturasBARData.labels.length > 0">
                        <div>
                            <Bar :data="alumnosConMasAsignaturasBARData"
                                :options="alumnosConMasAsignaturasBAROptions" />
                        </div>
                        <h3>Detalles</h3>
                        <table class="styled-table">
                            <thead>
                                <tr>
                                    <th>DNI</th>
                                    <th>Nombre Completo</th>
                                    <th>Nº Asignaturas</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(alumno, index) in datosDetalleGraficaAMA" :key="index">
                                    <td>{{ alumno.dni }}</td>
                                    <td>{{ alumno.nombreAlumno }} {{ alumno.apellido1 }} {{ alumno.apellido2
                                    }}</td>
                                    <td>{{ alumno.numeroAsignaturas }}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <p v-else>Cargando datos de la gráfica...</p>
                </section>
            </div>
        </main>
    </div>
</template>

<script>

import imglotus from '@/assets/lotus.webp';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement } from 'chart.js';
import { Pie, Bar } from 'vue-chartjs';
import axios from 'axios'; // Necesitas axios para las peticiones HTTP
import { useRouter } from 'vue-router';

// Registra los elementos necesarios de Chart.js
ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement);

export default {
    // eslint-disable-next-line vue/multi-word-component-names
    name: 'Estadisticas',
    components: { Pie, Bar },
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,
            // Para los pup up de alerta.
            mensaje: '',
            error: false,
            mostrarAlerta: false,

            // -- Numero de Alumnos por Asignatura (Pie) --
            datosDetalleGraficaAXA: [],
            alumnosPorAsignaturaPIEData: {
                labels: [], // // Nombre de asignaturas para que se muestren en el grafico
                datasets: [
                    {
                        backgroundColor: [], // Colores para cada sección de la tarta
                        data: [], // Número de alumnos por asignatura
                    },
                ],
            },
            alumnosPorAsignaturaPIEOptions: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { position: 'top', },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                let label = context.label || '';
                                if (label) { label += ':'; }
                                if (context.parsed !== null) { label += context.parsed + ' alumnos'; }
                                return label;
                            }
                        }
                    }
                }
            },
            // -- Numero de Alumnos con mas asingaturas (Bar) --
            datosDetalleGraficaAMA: [],
            alumnosConMasAsignaturasBARData: {
                labels: [], // // Nombre de los alumnos para que se muestren en el grafico
                datasets: [
                    {
                        //label: 'Numero de asignaturas',
                        backgroundColor: [], // Colores para cada sección de las barras
                        data: [], // Cantidad de asignaturas
                    },
                ],
            },
            alumnosConMasAsignaturasBAROptions: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                        display: false,
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                let label = context.dataset.label || '';
                                if (label) { label += ':'; }
                                if (context.parsed.y !== null) { label += context.parsed.y + ' asignaturas'; }
                                return label;
                            }
                        }
                    }
                },
                scales: {
                    x: { // Eje X para los nombres de los alumnos
                        title: {
                            display: true,
                            text: 'Alumnos'
                        },
                        // Opcional: Rotar etiquetas si los nombres son largos
                        ticks: {
                            autoSkip: false,
                            maxRotation: 45,
                            minRotation: 45
                        }
                    },
                    y: { // Eje Y para el número de asignaturas
                        beginAtZero: true,
                        title: {
                            display: true,
                            text: 'Nº de Asignaturas'
                        }
                    }
                }
            },

            // Gestion de mensajes especiales
            errorAlumnosPorAsignatura: false,
            msgErrorAPIX: '',
            errorAlumnosConMasAsignaturas: false,
            msgErrorAPIXMas: '',
        };
    },
    setup() {
        const router = useRouter();

        const logout = () => {
            localStorage.removeItem('authToken'); // Limpia el token de sesión
            localStorage.removeItem('sessionUser');
            router.push('/'); // Redirige a la página de login (asumiendo que tu ruta de login es '/')
        };

        const back = () => {
            router.push('/GestAlumnos'); // Redirige al indice.
        }
        return {
            logout,
            back,
        };
    },
    async mounted() {

        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);

        await this.fetchAlumnosPorAsignatura();
        await this.fetchAlumnosConMasAsignaturas();
    },

    methods: {
        // -- Numero de Alumnos por Asignatura (Pie) --
        async fetchAlumnosPorAsignatura() {

            try {
                console.log('He entrado en el try de fetchAlumnosPorAsignatura???'); //PUNTO DE CONTROL
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/estadisticas/alumnosPorAsignatura`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                const datos = response.data;
                this.datosDetalleGraficaAXA = datos;
                console.log("DATOS: ", datos); // PUNTO DE CONTROL

                this.alumnosPorAsignaturaPIEData.labels = datos.map(item => item.nombreAsignatura);
                this.alumnosPorAsignaturaPIEData.datasets[0].data = datos.map(item => item.numeroAlumnos);

                console.log("label", this.alumnosPorAsignaturaPIEData.labels); // PUNTO DE CONTROL
                console.log("datasets", this.alumnosPorAsignaturaPIEData.datasets[0].data); // PUNTO DE CONTROL

                // Generar colores aleatorios para la tarta
                this.alumnosPorAsignaturaPIEData.datasets[0].backgroundColor = datos.map(() => this.generateRandomColors());


            } catch (error) {

                if (error.response) {
                    this.errorAlumnosPorAsignatura = true;
                    this.msgErrorAPIX = error.response.data;
                    this.mostrarAlerta = false;
                    this.error = false;
                    console.log('Mensaje del API: ', this.msgErrorAPIX); // PUNTO DE CONTROL

                } else if (error.request) {
                    this.error = true; // Es un error
                    this.mostrarAlerta = true; // Tenemos que mostrar la alerta de error.
                    this.mensaje = 'Fallo en el servidor, la aplicacion no se está ejecutando.'
                    console.error('Fallo en el servidor, la aplicacion no se está ejecutando.');
                }

            }
        },
        // -- Numero de Alumnos con mas asingaturas (Bar) --
        async fetchAlumnosConMasAsignaturas() {

            try {
                console.log('He entrado en el try de fetchAlumnosConMasAsignaturas???'); //PUNTO DE CONTROL
                const token = localStorage.getItem('authToken');
                const response = await axios.get(`${this.apiUrl}/${this.version}/estadisticas/alumnosConMasAsignaturas`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                const datos = response.data;
                this.datosDetalleGraficaAMA = datos;
                console.log("DATOS: ", datos); // PUNTO DE CONTROL

                this.alumnosConMasAsignaturasBARData.labels = datos.map(item => item.nombreAlumno);
                this.alumnosConMasAsignaturasBARData.datasets[0].data = datos.map(item => item.numeroAsignaturas);

                console.log("label", this.alumnosConMasAsignaturasBARData.labels); // PUNTO DE CONTROL
                console.log("datasets", this.alumnosConMasAsignaturasBARData.datasets[0].data); // PUNTO DE CONTROL

                // Generar colores aleatorios para la tarta
                this.alumnosConMasAsignaturasBARData.datasets[0].backgroundColor = datos.map(() => this.generateRandomColors());


            } catch (error) {

                if (error.response) {
                    this.errorAlumnosConMasAsignaturas = true;
                    this.msgErrorAPIXMas = error.response.data;
                    this.mostrarAlerta = false;
                    this.error = false;
                    console.log('Mensaje del API: ', this.msgErrorAPIXMas); // PUNTO DE CONTROL

                } else if (error.request) {
                    this.error = true; // Es un error
                    this.mostrarAlerta = true; // Tenemos que mostrar la alerta de error.
                    this.mensaje = 'Fallo en el servidor, la aplicacion no se está ejecutando.'
                    console.error('Fallo en el servidor, la aplicacion no se está ejecutando.');
                }

            }
        },

        generateRandomColors() {
            const letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        },

        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.mensaje = '';
            this.error = false;
        },
    }

}
</script>
<style lang="css" scoped>
.estadisticas-container {
    padding: 20px;
    /*max-width: 900px;*/
    margin: 20px auto;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    font-family: 'Arial', sans-serif;
}

.cards-container {
    display: flex;
    grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
    gap: 20px;
    /* Espacio entre las celdas de la cuadrícula */
    width: 100%;
}

.estadisticas-card {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    padding: 25px;
    margin-bottom: 30px;
    border-left: 5px solid #42b983;
    /* Color de acento */
}

.estadistica-card h2 {
    color: #2c3e50;
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 1.8em;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
}

.estadistica-card h3 {
    color: #34495e;
    margin-top: 25px;
    margin-bottom: 15px;
    font-size: 1.4em;
}

/* Estilos para la tabla */

.styled-table {
    width: 100%;
    border-collapse: collapse;
    margin: 20px 0;
    font-size: 0.95em;
    min-width: 400px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
    border-radius: 8px;
    overflow: hidden;
    /* Asegura que los bordes redondeados se apliquen al contenido */
}

.styled-table thead tr {
    background-color: #009879;
    /* Color de cabecera de tabla */
    color: #ffffff;
    text-align: left;
    font-weight: bold;
}

.styled-table th,
.styled-table td {
    padding: 12px 15px;
    vertical-align: middle;
}

.styled-table tbody tr {
    border-bottom: 1px solid #dddddd;
}

.styled-table tbody tr:nth-of-type(even) {
    background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
    border-bottom: 2px solid #009879;
}

.styled-table tbody tr:hover {
    background-color: #e0f2f1;
    /* Light hover effect */
    cursor: pointer;
}

.error-message .estadistica-card p {
    text-align: center;
    padding: 20px;
    font-size: 1em;
    color: #666;
}
</style>