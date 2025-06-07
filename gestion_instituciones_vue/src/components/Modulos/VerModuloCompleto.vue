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

                <div v-if="modulo.asignaturas && modulo.asignaturas.length > 0">
                    <div v-for="asignatura in modulo.asignaturas" :key="asignatura.id" class="asignatura-card">
                        <h4>{{ asignatura.nombre }} ({{ asignatura.codigo }})</h4>
                        <div v-if="asignatura.alumnosMatriculados && asignatura.alumnosMatriculados.length > 0">
                            <h5>Alumnos Matriculados:</h5>
                            <ul>
                                <li v-for="alumno in asignatura.alumnosMatriculados" :key="alumno.dni">
                                    {{ alumno.nombre }} {{ alumno.apellido1 }} {{ alumno.apellido2 }} (DNI: {{
                                        alumno.dni }})
                                </li>
                            </ul>
                        </div>
                        <p v-else>No hay alumnos matriculados en esta asignatura.</p>
                    </div>
                </div>
                <p v-else>No hay asignaturas asociadas a este módulo.</p>
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

            moduloCargado: false,
            modulo: {}, // Para cargar todos los datos del modulo.
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

            this.error = false;
            this.mensaje = '';
            this.mostrarAlerta = false;
            this.moduloCargado = false;

            try {
                const token = localStorage.getItem('authToken');

                //1. Obtener el modulo en cuestión

                const responseModulo = await axios.get(`${this.apiUrl}/${this.version}/modulos/buscarModulo/${codigoModulo}`, {
                    headers: { 'Authorization': `Bearer ${token}` },

                });

                if (responseModulo.status === 404) {
                    this.error = true;
                    this.mensaje = 'No se ha encontrado el modulo indicado';
                    this.mostrarAlerta = true;
                    return;
                }

                this.modulo = responseModulo.data;
                console.log('MODULO COMPLETO', this.modulo); // PUNTO DE CONTROL

                //2. Obtener las asignaturas del modulo
                const responseAsignaturas = await axios.get(`${this.apiUrl}/${this.version}/modulos/listarAsignaturasModulo/${codigoModulo}`, {
                    headers: { 'Authorization': `Bearer ${token}` },
                });

                console.log('ASIGNATURAS DEL MODULO??: ', responseAsignaturas.data); // PUNTO DE CONTROL
                if (responseAsignaturas.status === 200) {

                    console.log('entreeee'); // PUNTO DE CONTROL
                    this.modulo.asignaturas = responseAsignaturas.data;
                    console.log('ASIGNATURAS DEL MODULO: ', this.modulo.asignaturas); // PUNTO DE CONTROL

                    // 3. Obtener alumnos para cada asignatura
                    for (const asignatura of this.modulo.asignaturas) {
                        try {
                            const responseAlumnos = await axios.get(`${this.apiUrl}/${this.version}/matricula/obtenerAlumnosMatriculados/${asignatura.codigo}`, {
                                headers: { 'Authorization': `Bearer ${token}` },
                            });

                            if (responseAlumnos.status === 200) {
                                asignatura.alumnosMatriculados = responseAlumnos.data;
                                console.log('ALUMNOS EN LA ASIGNATURA: ', this.modulo.asignaturas); // PUNTO DE CONTROL
                            } else {
                                asignatura.alumnosMatriculados = []; // No hay alumnos o error 404
                            }
                        } catch (err) {
                            console.error(`Error al cargar alumnos para asignatura ${asignatura.codigoAsignatura}:`, err);
                            asignatura.alumnosMatriculados = []; // En caso de error de red
                        }
                    }
                } else {
                    this.modulo.asignaturas = []; // No hay asignaturas o error 404
                }
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
