
/* Importa las funciones necesarias de Vue Router. createWebHistory() utiliza la API de historial del navegador 
para las URLs (sin el #). */
import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue'; // Importa mi componente Login.
import IndexGestion from '../components/IndexGestion.vue';
import GestUsuarios from '../components/GestUsuarios.vue';
import GestAlumnos from '../components/GestAlumnos.vue';
import GestAsignaturas from '../components/GestAsignaturas.vue';

/* Usuarios */
import ListUsuarios from '@/components/Usuarios/ListUsuarios.vue';
import AddUsuarios from '@/components/Usuarios/AddUsuarios.vue';
import EditarUsuarios from '@/components/Usuarios/EditarUsuarios.vue';

/*   */
/* Alumnos */
import AddAlumnos from '@/components/Alumnos/AddAlumnos.vue';
import ListAlumnos from '@/components/Alumnos/ListAlumnos.vue';
import EditarAlumnos from '@/components/Alumnos/EditarAlumnos.vue';
/*   */

/* Asignaturas */
import AddAsignaturas from '@/components/Asignaturas/AddAsignaturas.vue';
import ListAsignaturas from '@/components/Asignaturas/ListAsignaturas.vue';
import EditarAsignaturas from '@/components/Asignaturas/EditarAsignaturas.vue';

/* */
/* Modulos */
import GestModulos from '@/components/GestModulos.vue';
import AddModulos from '@/components/Modulos/AddModulos.vue';
import ListModulos from '@/components/Modulos/ListModulos.vue';
import EditarModulos from '@/components/Modulos/EditarModulos.vue';
import AsignacionesAsignaturasModulos from '@/components/Modulos/AsignacionesAsignaturasModulos.vue';
import VerModuloCompleto from '@/components/Modulos/VerModuloCompleto.vue';
/* */

/* Acciones */

import Estadisticas from '@/components/Acciones/Estadisticas.vue';
import VerMatricula from '@/components/Acciones/VerMatricula.vue';
import AltaMatricula from '@/components/Acciones/AltaMatricula.vue';




/*
Array que define tus rutas. Cada ruta es un objeto con las siguientes propiedades:
    path: La URL de la ruta (ej., /login).
    name: Un nombre para la ruta (opcional, pero útil para la navegación programática).
    component: El componente Vue que se renderizará cuando se acceda a esta ruta.
    meta: Un objeto para almacenar información adicional sobre la ruta (ej., si requiere autenticación).
 */
const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/IndexGestion',
        name: 'IndexGestion',
        component: IndexGestion,

    },
    // USUARIOS //
    {
        path: '/GestUsuarios',
        name: 'GestUsuarios',
        component: GestUsuarios,
        meta: { requiresAuth: true }
    },
    {
        path: '/ListUsuarios',
        name: 'ListUsuarios',
        component: ListUsuarios,
        meta: { requiresAuth: true }
    },
    {
        path: '/AddUsuarios',
        name: 'AddUsuarios',
        component: AddUsuarios,
        meta: { requiresAuth: true }
    },
    {
        path: '/EditarUsuarios/:usuario',
        name: 'EditarUsuarios',
        component: EditarUsuarios,
        meta: { requiresAuth: true }
    },

    // ALUMNOS //  
    {
        path: '/GestAlumnos',
        name: 'GestAlumnos',
        component: GestAlumnos,
        meta: { requiresAuth: true }
    },
    {
        path: '/AddAlumnos',
        name: 'AddAlumnos',
        component: AddAlumnos,
        meta: { requiresAuth: true }
    },
    {
        path: '/ListAlumnos',
        name: 'ListAlumnos',
        component: ListAlumnos,
        meta: { requiresAuth: true }
    },
    {
        path: '/EditarAlumnos/:alumno',
        name: 'EditarAlumnos',
        component: EditarAlumnos,
        meta: { requiresAuth: true }
    },
    // ASIGNATURAS //  
    {
        path: '/GestAsignaturas',
        name: 'GestAsignaturas',
        component: GestAsignaturas,
        meta: { requiresAuth: true }
    },
    {
        path: '/AddAsignaturas',
        name: 'AddAsignaturas',
        component: AddAsignaturas,
        meta: { requiresAuth: true }
    },
    {
        path: '/ListAsignaturas',
        name: 'ListAsignaturas',
        component: ListAsignaturas,
        meta: { requiresAuth: true }
    },
    {
        path: '/EditarAsignaturas/:asignatura',
        name: 'EditarAsignaturas',
        component: EditarAsignaturas,
        meta: { requiresAuth: true }
    },

    // MODULOS //
    {
        path: '/GestModulos',
        name: 'GestModulos',
        component: GestModulos,
        meta: { requiresAuth: true }
    },
    {
        path: '/AddModulos',
        name: 'AddModulos',
        component: AddModulos,
        meta: { requiresAuth: true }
    },
    {
        path: '/ListModulos',
        name: 'ListModulos',
        component: ListModulos,
        meta: { requiresAuth: true }
    },
    {
        path: '/EditarModulos/:modulo',
        name: 'EditarModulos',
        component: EditarModulos,
        meta: { requiresAuth: true }
    },
    {
        path: '/AsignacionesAsignaturasModulos/:modulo',
        name: 'AsignacionesAsignaturasModulos',
        component: AsignacionesAsignaturasModulos,
        meta: { requiresAuth: true }
    },
    {
        path: '/VerModuloCompleto/:modulo',
        name: 'VerModuloCompleto',
        component: VerModuloCompleto,
        meta: { requiresAuth: true }
    },
    // OTRAS ACCIONES //
    {
        path: '/AltaMatricula/:alumno',
        name: 'AltaMatricula',
        component: AltaMatricula,
        meta: { requiresAuth: true }
    },
    {
        path: '/VerMatricula/:alumno',
        name: 'VerMatricula',
        component: VerMatricula,
        meta: { requiresAuth: true }
    },
    {
        path: '/Estadisticas',
        name: 'Estadisticas',
        component: Estadisticas,
        meta: { requiresAuth: true }
    },
    // Define tus otras rutas aquí
    // {
    //   path: '/gestion-usuarios',
    //   name: 'GestionUsuarios',
    //   component: GestionUsuarios,
    //   // Puedes añadir meta información para proteger rutas
    //   meta: { requiresAuth: true }
    // },

    {
        path: '/:pathMatch(.*)*', // Ruta para cualquier otra cosa, redirige a Login. Ruta comodin
        redirect: '/login'
    }
];

// Crea la instancia del router con la configuración del historial y las rutas.
const router = createRouter({
    history: createWebHistory(),
    routes
});

//Opcional: Navegation guard para proteger rutas
router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !localStorage.getItem('authToken')) {
        next('/login'); // Redirige a la página de login si no hay token y la ruta requiere autenticación
    } else {
        next(); // Permite el acceso a la ruta
    }
});

export default router;