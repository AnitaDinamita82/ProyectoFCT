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

        <header class="header">
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
            <h1>Gestión de matricula para {{ alumno.nombre }} {{ alumno.apellido1 }}
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

                    <div v-for="modulo in modulosConAsignaturasMatriculadas" :key="modulo.codigoModulo" class="modulo-card">
                        
                        <h4>Módulo: {{ modulo.nombreModulo }} ({{ modulo.codigoModulo }})</h4>
                        <p>Curso: {{ modulo.curso }} | Grupo: {{ modulo.grupo }}</p>

                        <div v-if="modulo.asignaturas && modulo.asignaturas.length > 0">
                            <h5>Asignaturas:</h5>
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
                                                <td>
                                                    <button class="action-button color-button-eliminar"
                                                        @click="confirmarBajaMatricula(asignatura.codigo,asignatura.nombre)"><i
                                                            class="fas fa-trash"></i></button>
                                                </td>
                                            </tr>
                                    </tbody>
                                </table>
                        </div>
                        <p v-else>No hay asignaturas matriculadas en este módulo para este alumno.</p>
                    </div>
                        <button type="submit" class="action-button color-button-baja" @click="confirmarBajaCompleta()">
                            Baja Completa
                        </button>
                </div>
                <p v-else>El/la alumn@ no está matriculado/a aún de ninguna asignatura o se le dio de baja la matricula.</p>
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
            router.push('/ListAlumnos');
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
                await this.cargarAsignaturasMatriculadasPorModulo(this.alumno.dni);
            }
        } else {
            console.warn('No se encontró al alumno como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('ALUMNO CARGADO: ', this.alumno);  // PUNTO DE CONTROL 
            this.router.push('/ListAlumnos');
        }
    },
    methods: {

        async cargarAsignaturasMatriculadasPorModulo(dniAlumno) {
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
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

                // 2. Obtener todos los modulos 
                const responseModulos = await axios.get(`${this.apiUrl}/${this.version}/modulos/listar`, {
                    headers: {
                        'Authorization': `Bearer ${token}`

                    },
                });

                const todosLosModulos = responseModulos.data;
                console.log('TODOS LOS MODULOS:', this.asignaturasMatriculadas); // PUNTO DE CONTROL.

                // 3. Para cada módulo, obtener sus asignaturas y filtrar las matriculadas por el alumno
                for (const modulo of todosLosModulos) {
                    try {
                        const responseAsignaturasDeModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturas/${modulo.codigoModulo}`, {
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

        confirmarBajaMatricula(codigoAsignatura,nombreAsinatura) {
            if (confirm(`¿Está seguro que desea dar de baja la asignatura ${nombreAsinatura} para el alumno ${this.alumno.nombre} ${this.alumno.apellido1} ?`)) {
                this.desMatricularDeAsignatura(this.alumno.dni, codigoAsignatura);
            }
        },

        confirmarBajaCompleta() {
            if (confirm(`¿Está seguro que desea dar de baja la matricula completa para el alumno ${this.alumno.nombre} ${this.alumno.apellido1} ?`)) {
                this.bajaCompleta(this.alumno.dni);
            }
        },

        // Funcion para desmatricular al alumno en una asignatura
        async desMatricularDeAsignatura(dniAlumno, codigoAsignatura) {
            
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {
                const token = localStorage.getItem('authToken');
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

                console.log('Respuesta de la API (desmatricula):', response); // PUNTO DE CONTROL
                this.apiResponse(response);

                // Y despues de desMatricular, regargamos de nuevo la matricula.

               await this.cargarAsignaturasMatriculadasPorModulo(dniAlumno); // Recargar datos

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
    margin-top: 0%;
}

.action-container {
    margin-top: 0;
    margin-left: 3%;
}

/* Estilo para el contenedor principal de VerMatricula */
.container-color-ver-matricula {
    border-left: 5px solid #a33939; /* Un color distinto para diferenciar de AltaMatricula */
}

/* Estilos de las tarjetas de módulo, tabla y botones (copiados de AltaMatricula y ajustados) */
.modulo-card {
    border: 1px solid #a33939; /* Borde del módulo */
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
    border: 1px solid #ddd;
    padding: 10px;
    text-align: left;
}

.modulo-card th {
    background-color: #f2f2f2;
}

.container-color-matricular {
    border-left: 5px solid #c9c23d;
}

.alert {
    width: 50%;
}

.alert-danger {
    width: 60%;
}
</style>
