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
        <!-- *** -->

        <header class="header">
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
            <h1>Matricula de {{ alumno.nombre }} {{ alumno.apellido1 }}
                {{ alumno.apellido2 }}</h1>
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
        </header>
        <main class="action-container container-color-matricular">
            <!-- Control de alertas -->
            <div v-if="mostrarAlerta" class="alert" :class="{ 'alert-success': !error, 'alert-danger': error }">
                {{ mensaje }}
                <button type="button" class="x-close" @click="cerrarAlerta">X</button>
            </div>
            <div v-if="alumno">
                <div v-if="modulosConAsignaturasMatriculadas.length > 0">
                    <h3>Módulos con asignaturas matriculadas</h3>
                    <div class="action-style">
                        <button type="submit" class="action-button" @click="confirmarBajaCompleta()">
                            <i class="fas fa-trash"></i><span>Baja Completa </span>
                        </button>
                    </div>
                    <div v-for="modulo in modulosConAsignaturasMatriculadas" :key="modulo.codigoModulo"
                        class="modulo-card">

                        <h4>Módulo: {{ modulo.nombreModulo }} ({{ modulo.codigoModulo }})</h4>
                        <p>Curso: {{ modulo.curso }} | Grupo: {{ modulo.grupo }}</p>

                        <div v-if="modulo.asignaturas && modulo.asignaturas.length > 0">
                            <table>
                                <thead>
                                    <tr>
                                        <th>CÓDIGO</th>
                                        <th>NOMBRE ASIGNATURA</th>
                                        <th>DESCRIPCIÓN</th>
                                        <th>ACCIÓN</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="asignatura in modulo.asignaturas" :key="asignatura.id">
                                        <td>{{ asignatura.codigo }}</td>
                                        <td>{{ asignatura.nombre }}</td>
                                        <td>{{ asignatura.descripcion }}</td>
                                        <td class="action-cell">
                                            <button class="action-button" title="Eliminar"
                                                @click="confirmarBajaMatricula(asignatura.codigo, asignatura.nombre, modulo.codigoModulo)"><i
                                                    class="fas fa-trash"></i></button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <p v-else>No hay asignaturas matriculadas en este módulo para este alumno.</p>
                    </div>
                    <div class="action-style">
                        <button type="submit" class="action-button" @click="confirmarBajaCompleta()">
                            <i class="fas fa-trash"></i><span>Baja Completa </span>
                        </button>
                    </div>
                </div>
                <p v-else>El/la alumn@ no está matriculado/a aún de ninguna asignatura o se le dio de baja la matricula.
                </p>
            </div>
            <p v-else>Cargando información o sin datos que mostrar.</p>
        </main>
    </div>
</template>
<script>

import imglotus from '@/assets/lotus.webp';
import { useRouter, useRoute } from 'vue-router';
import axios from 'axios';

export default {
    name: 'VerMatricula',
    components: {},
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: '',

            alumno: {}, // Objeto para el alumno del que depende la matricula.
            asignaturasMatriculadas: [], // Almacenará las asignaturas donde el alumno ya esté matriculado.
            modulosConAsignaturasMatriculadas: [], // Nueva propiedad para módulos con asignaturas matriculadas

            mensaje: '',
            error: false,
            mostrarAlerta: false,
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
            router.go(-1);
        };

        return {
            logout,
            back,
            route
        }
    },
    async mounted() {

        /* Usuario logeado */
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('SESSIONUSER: ', this.sessionUser); // PUNTO DE CONTROL

        if (this.route.params && this.route.params.alumno) {
            /* Alumno matriculado */
            this.alumno = JSON.parse(this.route.params.alumno);
            console.log('ALUMNO CARGADO: ', this.alumno);  // PUNTO DE CONTROL 

            if (this.alumno && this.alumno.dni) {
                await this.cargarMatriculaDetallada(this.alumno.dni);
            }
        } else {
            console.warn('No se encontró al alumno como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('ALUMNO CARGADO: ', this.alumno);  // PUNTO DE CONTROL 
            this.router.push('/ListAlumnos');
        }
    },
    methods: {

        async cargarMatriculaDetallada(dniAlumno) {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.matriculaDetallada = [];
            this.asignaturasMatriculadas = [];
            this.modulosConAsignaturasMatriculadas = []; // Reinicia la lista de módulos

            try {
                const token = localStorage.getItem('authToken');

                // 1. Obtener todas las asignaturas de las que el alumno ya esta matriculado 
                const responseMatriculadas = await axios.get(`${this.apiUrl}/${this.version}/alumnoAsignaturaMTM/listarAsignaturasDeAlumno/${dniAlumno}`, { // Llamada a la API que muestra las asignaturas dado un DNI
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.asignaturasMatriculadas = responseMatriculadas.data;
                console.log('ASIGNATURAS YA MATRICULADAS:', this.asignaturasMatriculadas); // PUNTO DE CONTROL.
                const codigosAsignaturasMatriculadas = new Set(this.asignaturasMatriculadas.map(a => a.codigo));

                // 2. Obtener solo los modulos en los que el alumno está
                const responseModulosAlumno = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarModulosDeAlumno/${dniAlumno}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`

                    },
                });

                console.log('ASIGNATURAS MATRICULADAS DE ALUMNO:', this.asignaturasMatriculadas); // PUNTO DE CONTROL.

                const modulosDelAlumno = responseModulosAlumno.data;
                console.log('MODULOS DEL ALUMNO:', modulosDelAlumno); // PUNTO DE CONTROL.

                // 3. Para cada módulo, al cual el alumno está directamente asociado por modulos - alumnos
                // obtendremos sus asignaturas modulo - asignaturas
                // y filtraremos por las que el alumno tiene matriculadas alumnos - asignaturas

                for (const modulo of  modulosDelAlumno) {
                    try {
                        const responseAsignaturasDeModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturasModulo/${modulo.codigoModulo}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`

                            },
                        });

                        // Filtrar asignaturas del módulo: solo las que están matriculadas por el alumno 
                        const asignaturasMatriculadasEnModulo = responseAsignaturasDeModulo.data.filter(asignatura => codigosAsignaturasMatriculadas.has(asignatura.codigo));

                        if (asignaturasMatriculadasEnModulo.length > 0) {
                            this.modulosConAsignaturasMatriculadas.push({
                                ...modulo, // Copia las propiedades del módulo
                                asignaturas: asignaturasMatriculadasEnModulo // Asigna solo las asignaturas matriculadas
                            });
                        }
                    } catch (error) {
                        console.warn(`No se pudieron cargar las asignaturas para el módulo ${modulo.codigoModulo}:`);
                    }

                }
            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;
                if (error.request) {
                    this.mensaje = 'Imposible cargar la lista de asignaturas. Posible fallo de conexion con el servidor.'
                    console.error('Error de red al cargar asignaturas:', error); //PUNTO DE CONTROL
                } else {
                    this.mensaje = 'Error al cargar los datos de matrícula.';
                }
                console.error('Error general al cargar datos de matrícula:', error);
            }

        },

        confirmarBajaMatricula(codigoAsignatura, nombreAsignatura,codigoModulo) {
            if (confirm(`¿Está seguro que desea dar de baja la asignatura ${nombreAsignatura} para el alumno ${this.alumno.nombre} ${this.alumno.apellido1} ?`)) {
                this.desMatricularDeAsignatura(this.alumno.dni, codigoAsignatura,codigoModulo);
            }
        },

        confirmarBajaCompleta() {
            if (confirm(`¿Está seguro que desea dar de baja la matricula completa para el alumno ${this.alumno.nombre} ${this.alumno.apellido1} ?`)) {
                this.bajaCompleta(this.alumno.dni);
            }
        },

        // Funcion para desmatricular al alumno en una asignatura
        async desMatricularDeAsignatura(dniAlumno, codigoAsignatura, codigoModulo) {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {
                const token = localStorage.getItem('authToken');

                // 1. Damos de baja la asignatura alumnos - asignaturas
                const response = await axios.delete(`${this.apiUrl}/${this.version}/alumnoAsignaturaMTM/desmatricularAlumno`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    data: {
                        dniAlumno: dniAlumno,
                        codigoAsignatura: codigoAsignatura
                    }
                });

                // 2. Quitamos la referencia del alumno en el modulo modulos - alumnos 
                const responseMensaje = await axios.delete(`${this.apiUrl}/${this.version}/modulos/desasignarAlumnosDeModulo/${codigoModulo}/${dniAlumno}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                });

                console.log('Desasigna bien al alumno del modulo:', responseMensaje.data); // PUNTO DE CONTROL
                this.apiResponse(response);

                // Y despues de desMatricular, regargamos de nuevo la matricula.

                await this.cargarMatriculaDetallada(dniAlumno); // Recargar datos

            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;

                if (error.response) {
                    this.mensaje = error.response.data;
                    console.log('API ERROR:  ', this.mensaje); // PUNTO DE CONTROL
                } else {
                    if (error.request) {
                        this.mensaje = 'Fallo en el intento de dar de baja una asignatura, parece que no se pudo conectar con el servidor.'
                        console.log('Fallo en operacion de desMatriculacion') // PUNTO DE CONTROL
                    }
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
        },
    }
}
</script>
<style lang="css" scoped>
.header {
    margin-top: 2%;
}

.action-container {
    margin: auto;
}

.action-button span {
    color: #a54a4a;
}

.action-style button {
    margin-left: auto;
}

/* Estilo para el contenedor principal de VerMatricula */
.container-color-ver-matricula {
    border-left: 5px solid #a33939;
    /* Un color distinto para diferenciar de AltaMatricula */
}

/* Estilos de las tarjetas de módulo, tabla y botones (copiados de AltaMatricula y ajustados) */
.modulo-card {
    border: 1px solid #a33939;
    /* Borde del módulo */
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 25px;
    background-color: #fefefe;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.modulo-card h4 {
    color: #333;
    margin-top: 0;
    margin-bottom: 15px;
    font-size: 1.4em;
    border-bottom: 1px solid #eee;
    padding-bottom: 10px;
}

.modulo-card h5 {
    color: #555;
    margin-top: 20px;
    margin-bottom: 10px;
    font-size: 1.2em;
}

.modulo-card table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
}

.modulo-card th,
.modulo-card td {
    padding: 10px;
    text-align: center;
}

.container-color-matricular {
    border-left: 5px solid #c9c23d;
}

.alert {
    width: 60%;
}

.alert-danger {
    width: 60%;
}
</style>
