<template>
    <div class="index-gestion-container">

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
        <div class="action-volver">
            <button class="back-button" @click="back"><i class="fas fa-arrow-left"></i></button>
        </div>

        <header class="header">
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
            <h1>Bienvenido a la Gestión de Módulos</h1>
            <img :src="imglotus" style="width: 100px;" alt="logo-header" />
        </header>
        <main class="main-container-gest">
            <router-link to="/AddModulos" class="action-card">
                <div class="card-top-section fondo-card-1">
                    <div class="icon-container">
                        <i class="fas fa-plus-square card-icon-color"></i>
                    </div>
                </div>
                <div class="card-bottom-section">
                    <p class="card-text">Altas de Módulos</p>
                </div>
            </router-link>

            <router-link to="/ListModulos" class="action-card">
                <div class="card-top-section fondo-card-2">
                    <div class="icon-container">
                        <i class="fas fa-list-alt card-icon-color"></i>
                    </div>
                </div>
                <div class="card-bottom-section">
                    <p class="card-text">Listado de Módulos</p>
                </div>
            </router-link>

        </main>
    </div>
</template>

<script>
import imglotus from '@/assets/lotus.webp';
import { useRouter } from 'vue-router';

export default {
    name: 'GestModulos',
    data() {
        return {
            imglotus: imglotus,
            sessionUser: null,
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
            router.push('/IndexGestion'); // Redirige al índice principal
        }
        return {
            logout,
            back,
        };
    },
    mounted() {
        this.sessionUser = localStorage.getItem('sessionUser');
        console.log('USER SESion', this.sessionUser);
    }
};
</script>

<style lang="css" scoped></style>