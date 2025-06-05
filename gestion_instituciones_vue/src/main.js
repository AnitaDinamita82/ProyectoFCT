import { createApp } from 'vue'
import App from './App.vue'
import router from './routes'; // Importa tu instancia del router
import '@fortawesome/fontawesome-free/css/all.css';


// Importa los estilos de Bootstrap
import 'bootstrap/dist/css/bootstrap.css'
// Importa los scripts de Bootstrap (y sus dependencias)
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
//Importamos el archivo de estilos
import './styles/styles.css'

const app = createApp(App);

app.use(router); // Usa el router en tu aplicación

app.mount('#app');
