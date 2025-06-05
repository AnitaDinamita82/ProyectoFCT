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

            <!-- Seccion para matricular a un alumno -->
            <div v-if="alumno">
                <div v-if="modulosConAsignaturasDisponibles.length > 0">
                    <h3>Módulos con asignaturas disponibles para matricular</h3>

                    <div v-for="modulo in modulosConAsignaturasDisponibles" :key="modulo.codigoModulo" class="modulo-card">
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
                                                <button class="action-button color-button-matricular"
                                                    @click="confirmarEnMatricula(asignatura.codigo)"><i
                                                        class="fas fa-plus-circle"></i> MATRICULAR</button>
                                            </td>
                                        </tr>
                                    </tbody>
                            </table>                           
                        </div>
                        <p v-else>No hay asignaturas disponibles en este módulo para este alumno.</p>
                    </div>       
                        <button type="submit" class="action-button color-button-ver" @click="verMatriculaCompleta()">Ver
                            Matricula Completa</button>            
                </div>
                <p v-else>Aún no tenemos abierta la oferta para el nuevo curso o el alumno está matriculado de todas las asignaturas disponibles</p>
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

    name: 'AltaMatricula',
    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: '',

            alumno: {}, // Objeto para el alumno al que se le quiere matricular

            todasLasAsignaturas: [], // Almacenará todas las asignaturas dadas de alta en BD.
            asignaturasDisponibles: [], // Las asignaturas que aun no tiene.
            asignaturasMatriculadas: [], // Almacenará las asignaturas donde el alumno ya esté matriculado.
            modulosConAsignaturasDisponibles: [], // Almacenará modulos con sus asignaturas disponibles.

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
            route,
            router
        }
    },
    async mounted() {

        /* Usuario logeado */
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('SESSIONUSER: ', this.sessionUser); // PUNTO DE CONTROL

        if (this.route.params && this.route.params.alumno) {
            /* Alumno seleccionado */
            this.alumno = JSON.parse(this.route.params.alumno);
            console.log('ALUMNO CARGADO: ', this.alumno);  // PUNTO DE CONTROL 
            if (this.alumno && this.alumno.dni) {
                await this.cargarAsignaturasParaMatricular(this.alumno.dni);
            }
        } else {
            console.warn('No se encontró al alumno como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('ALUMNO CARGADO: ', this.alumno);  // PUNTO DE CONTROL 
            this.router.push('/ListAlumnos');
        }
    },
    methods: {

        async cargarAsignaturasParaMatricular(dniAlumno) {

            console.log("INICIANDO carga de asignaturas y módulos disponibles..."); // Debug
            this.error = false;
            this.error = false; // Restablecer estado de error
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.asignaturasDisponibles = [];
            this.modulosConAsignaturasDisponibles = []; // Reinicia la lista de módulos

            try {

                const token = localStorage.getItem('authToken');

                // 1. Obtener todas las asignaturas
                const responseTodasLasAsignaturas = await axios.get(`${this.apiUrl}/${this.version}/asignaturas/listar`, { // Llamada a la API de listar asignaturas
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.todasLasAsignaturas = await responseTodasLasAsignaturas.data;
                console.log('TODAS LA ASIGNATURAS: ', this.todasLasAsignaturas) // PUNTO DE CONTROL

                const codigosTodasAsignaturas = new Set(this.todasLasAsignaturas.map(a => a.codigo));


                // 2. Obtener todas las asignaturas de las que el alumno ya esta matriculado.

                const responseMatriculadas = await axios.get(`${this.apiUrl}/${this.version}/alumnoAsignaturaMTM/listarAsignaturasDeAlumno/${dniAlumno}`, { // Llamada a la API que muestra las asignaturas dado un DNI
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                this.asignaturasMatriculadas = responseMatriculadas.data;
                console.log('ASIGNATURAS YA MATRICULADAS:', this.asignaturasMatriculadas); // PUNTO DE CONTROL.

                const codigosAsignaturasMatriculadas = new Set(this.asignaturasMatriculadas.map(a => a.codigo));

                // 3. Obtener todos los modulos
                const responseModulos = await axios.get(`${this.apiUrl}/${this.version}/modulos/listar`, {
                    headers: {
                        'Authorization': `Bearer ${token}` 
                        
                    },
                });

                const todosLosModulos = responseModulos.data;
                console.log('TODOS LOS MODULOS:', this.asignaturasMatriculadas); // PUNTO DE CONTROL.

                // 4. Para cada módulo, obtener sus asignaturas y filtrar las disponibles
                for (const modulo of todosLosModulos) {
                    try {
                        const responseAsignaturasDeModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturas/${modulo.codigoModulo}`, {
                            headers: {
                                'Authorization': `Bearer ${token}` 
                                
                            },
                        });

                     // Filtrar asignaturas del módulo: solo las que NO están matriculadas por el alumno Y existen en la lista general de asignaturas    

                        const asignaturasDisponiblesEnModulo = responseAsignaturasDeModulo.data.filter(asignatura => !codigosAsignaturasMatriculadas.has(asignatura.codigo) && codigosTodasAsignaturas.has(asignatura.codigo));

                        if (asignaturasDisponiblesEnModulo.length > 0) {
                            this.modulosConAsignaturasDisponibles.push({
                                ...modulo, // Copia las propiedades del módulo
                                asignaturas: asignaturasDisponiblesEnModulo // Asigna solo las asignaturas disponibles
                            });
                        }
                    } catch (error) {
                        console.warn(`No se pudieron cargar las asignaturas para el módulo ${modulo.codigoModulo}:`);
                    } 
                }
        
                console.log('Módulos con asignaturas disponibles para matricular:', this.modulosConAsignaturasDisponibles); // PUNTO DE CONTROL
                console.log("FINALIZADA carga. Módulos con asignaturas disponibles:", this.modulosConAsignaturasDisponibles); // Debug
                 
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

        confirmarEnMatricula(codigoAsignatura) {
            if (confirm(`¿Estás seguro que quieres matricular a ${this.alumno.nombre} ${this.alumno.apellido1} en la asignatura con código ${codigoAsignatura}`)) {
                this.matricular(this.alumno.dni, codigoAsignatura);
            }
        },

        //Función para matricular al alumno en la asignatura
        async matricular(dniAlumno, codigoAsignatura) {

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;

            try {
                const token = localStorage.getItem('authToken');
                const response = await axios.post(`${this.apiUrl}/${this.version}/alumnoAsignaturaMTM/matricularAlumno`, {
                    dniAlumno: dniAlumno,
                    codigoAsignatura: codigoAsignatura
                }, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                });

                this.apiResponse(response);
                /// DESPUÉS de matricular, recargamos las asignaturas del alumno

                await this.cargarAsignaturasParaMatricular(dniAlumno);


            } catch (error) {
                this.error = true;
                this.mostrarAlerta = true;

                if (error.response) {
                    this.mensaje = error.response.data;
                    console.log('API ERROR:  ', this.mensaje); // PUNTO DE CONTROL
                } else {
                    if (error.request) {
                        this.mensaje = 'Fallo en la matriculacion parece que no se pudo conectar con el servidor.'
                        console.log('Fallo en operacion de matriculacion') // PUNTO DE CONTROL
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
        verMatriculaCompleta() {
            this.router.push({
                name: 'VerMatricula', // Asegúrate de que esta ruta exista en tu router
                params: {
                    alumno: JSON.stringify(this.alumno)
                }
            });
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

.container-color-matricular {
    border-left: 5px solid #c9c23d;
}

.modulo-card {
    border: 1px solid #c9c23d; /* O un color que te guste, similar a tu container-color-matricular */
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 25px; /* Espacio entre módulos */
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

.action-button.color-button-matricular {
    background-color: #c9c23d; /* Asegúrate de que el color sea consistente */
    color: white;
    padding: 8px 15px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 0.9em;
}

.action-button.color-button-matricular:hover {
    background-color: #b0a931; /* Un tono más oscuro al pasar el ratón */
}

.alert {
    width: 50%;
}

.alert-danger {
    width: 60%;
}
</style>
