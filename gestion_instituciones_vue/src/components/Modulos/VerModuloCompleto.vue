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
            <div v-if="moduloCargado && !mostrarAlerta">
                <h1>{{ modulo.nombreModulo }} ({{ modulo.codigoModulo }})</h1>
                <p><strong>Curso:</strong> {{ modulo.curso }}</p>
                <p><strong>Grupo:</strong> {{ modulo.grupo }}</p>

                <div v-if="asignaturasConAlumnos.length > 0">
                    <div v-for="asignatura in asignaturasConAlumnos" :key="asignatura.id" class="asignatura-card">
                        <h4>{{ asignatura.nombre }} ({{ asignatura.codigo }})</h4>

                        <div v-if="asignatura.alumnos && asignatura.alumnos.length > 0">
                            <h5>Alumnos Matriculados:</h5>
                            <ul>
                                <li v-for="alumno in asignatura.alumnos" :key="alumno.dni">
                                    {{ alumno.nombre }} {{ alumno.apellido1 }} {{ alumno.apellido2 }} (DNI: {{
                                        alumno.dni }})
                                </li>
                            </ul>
                        </div>
                        <p v-else>No hay alumnos matriculados en esta asignatura.</p>
                    </div>
                </div>
                <p v-else>No hay asignaturas asociadas a este módulo o no tiene alumnos matriculados.</p>
            </div>
            <p v-else-if="!mostrarAlerta">Cargando detalles del módulo o el módulo no existe...</p>
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
    name: 'VerModuloCompleto',
    components: {
        BotonSubir
    },

    data() {
        return {
            imglotus: imglotus,
            apiUrl: 'http://localhost:8080',
            version: '1.0',
            sessionUser: null,

            mensaje: '',
            error: false,
            mostrarAlerta: false,

            moduloCargado: false, // Control para saber si el modulo ya se cargó
            modulo: {}, // Objeto para modulo que se está gestionando
            asignaturasConAlumnos: [] // Estructura para la vista
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
            router.push('/ListModulos');
        };

        return {
            logout,
            back,
            route
        }
    },
    async mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');

        if (this.route.params && this.route.params.modulo) {

            /* Modulo a gestionar */
            this.modulo = JSON.parse(this.route.params.modulo);
            console.log('MODULO CARGADO: ', this.modulo); // PUNTO DE CONTROL
            this.cargarModuloCompleto(this.modulo.codigoModulo);

        } else {
            console.warn('No se encontró el modulo como parametro en la ruta.');  // PUNTO DE CONTROL 
            console.log('MODULO CARGADO: ', this.modulo);  // PUNTO DE CONTROL 
            this.router.push('/ListModulos');
        }
    },
    methods: {
        async cargarModuloCompleto(codigoModulo) {

            console.log("INICIANDO carga del módulo completo para:", codigoModulo); // PUNTO DE CONTROL
            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.moduloCargado = false;
            this.asignaturasConAlumnos = []; // Se reinicia la estructura para la vista

            try {
                const token = localStorage.getItem('authToken');

                // 1. Obtenemos todas las asignaturas que pertenecen a este módulo
                const responseAsignaturasDelModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturasModulo/${codigoModulo}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                const asignaturasDelModulo = responseAsignaturasDelModulo.data;
                console.log(`Asignaturas del módulo ${codigoModulo}:`, asignaturasDelModulo); //PUNTO DE CONTROL


                // 2. Obtenemos la lista de  alumnos asociados directamente a este módulo
                const responseAlumnosAsociadosAModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAlumnosModulo/${codigoModulo}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });

                // Convertimos la lista de objetos de alumno a un Set de sus DNI para búsquedas eficientes
                const dnisAlumnosAsociadosAModulo = new Set(responseAlumnosAsociadosAModulo.data.map(alumno => alumno.dni));
                console.log(`DNI de alumnos asociados directamente al módulo ${codigoModulo}:`, dnisAlumnosAsociadosAModulo); // PUNTO DE CONTROL

                // 3. Para cada asignatura del módulo, obtenemos sus alumnos matriculados y filtramos
                for (const asignatura of asignaturasDelModulo) {

                    const alumnosValidosParaMostrarEnEstaAsignatura = []; // Se inicializa vacio

                    try {
                        const responseAlumnosEnAsignatura = await axios.get(`${this.apiUrl}/${this.version}/matricula/obtenerAlumnosMatriculados/${asignatura.codigo}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`
                            }
                        });

                        const alumnosMatriculadosEnEstaAsignatura = responseAlumnosEnAsignatura.data;
                        console.log(`Alumnos matriculados en asignatura ${asignatura.nombre} (todos):`, alumnosMatriculadosEnEstaAsignatura); // PUNTO DE CONTROL


                        const dnisYaAgregadosParaEstaAsignatura = new Set(); // Para controlar duplicados internos

                        for (const alumno of alumnosMatriculadosEnEstaAsignatura) {
                            // Criterio de filtrado para evitar duplicidades y asegurar relevancia:
                            // El alumno DEBE estar asociado a este MÓDULO (según la relación modulos-alumnos)
                            // Y no debe haber sido agregado ya a la lista de alumnos para ESTA asignatura.
                            if (dnisAlumnosAsociadosAModulo.has(alumno.dni) && !dnisYaAgregadosParaEstaAsignatura.has(alumno.dni)) {
                                alumnosValidosParaMostrarEnEstaAsignatura.push(alumno);
                                dnisYaAgregadosParaEstaAsignatura.add(alumno.dni);
                            }
                        }

                        // *** CAMBIO CLAVE AQUI ***
                        // Siempre añade la asignatura a la estructura final,
                        // incluso si alumnosValidosParaMostrarEnEstaAsignatura está vacío.
                        this.asignaturasConAlumnos.push({
                            ...asignatura,
                            alumnos: alumnosValidosParaMostrarEnEstaAsignatura // Puede ser un array vacío
                        });

                    } catch (errorAlumnosEnAsignatura) {
                        console.warn(`No se pudieron cargar los alumnos para la asignatura ${asignatura.nombre} del módulo ${codigoModulo}:`, errorAlumnosEnAsignatura.response?.data || errorAlumnosEnAsignatura.message);
                    }
                }
                console.log('Estructura final de asignaturas con alumnos para mostrar:', this.asignaturasConAlumnos); // PUNTO DE CONTROL

                /*  if (this.asignaturasConAlumnos.length === 0 && asignaturasDelModulo.length > 0) {
                      this.mensaje = 'Este módulo tiene asignaturas, pero ningún alumno matriculado en ellas o asociado al módulo.';
                      this.error = false;
                      this.mostrarAlerta = true;
                  } else if (this.asignaturasConAlumnos.length === 0 && asignaturasDelModulo.length === 0) {
                      this.mensaje = 'Este módulo no tiene asignaturas asociadas.';
                      this.error = false;
                      this.mostrarAlerta = true;
                  }*/
                this.moduloCargado = true;
            } catch (error) {
                if (error.response) {
                    this.error = true;
                    this.mensaje = `Error al cargar los detalles del módulo: ${error.response.data || error.response.statusText}`;
                } else if (error.request) {
                    this.error = true;
                    this.mensaje = 'No se pudo conectar con el servidor para cargar los detalles del módulo.';
                } else {
                    this.error = true;
                    this.mensaje = 'Error desconocido al cargar los detalles del módulo.';
                }
                console.error('Error al cargar los detalles del módulo:', error);
                this.mostrarAlerta = true;
            }
        },
        cerrarAlerta() {
            this.mostrarAlerta = false;
            this.mensaje = '';
        },
    }

}
</script>
<style scoped>
.action-container {
    margin-top: 2%;
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

.alert {
    width: 40%;
}

.alert-danger {
    width: 60%;
}

.asignatura-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 15px;
    background-color: #f9f9f9;
}

.asignatura-card h4 {
    color: #333;
    margin-top: 0;
    margin-bottom: 10px;
}

.asignatura-card ul {
    list-style-type: disc;
    padding-left: 20px;
}

.asignatura-card li {
    margin-bottom: 5px;
}
</style>
