<template>

    <button v-if="isVisible" @click="subirArriba" class="scroll-botton" title="Volver Arriba">
        <i class="fas fa-arrow-up"></i>
    </button>
</template>

<script>
export default {
    name: 'BotonSubir',
    data() {
        return {
            isVisible: false, // Controla si el botón es visible
            umbral: 150 // Cantidad de píxeles para que el botón aparezca
        };
    },
    mounted() {
        // Añade el event listener para el scroll cuando el componente se monta
        window.addEventListener('scroll', this.ScrollUp);
    },
    beforeUnmount() {
        // Elimina el event listener antes de que el componente se desmonte para evitar fugas de memoria
        window.removeEventListener('scroll', this.ScrollUp);
    },
    methods: {
        ScrollUp() {
            // Si la posición del scroll es mayor que el umbral, hace el botón visible
            if (window.scrollY > this.umbral) {
                this.isVisible = true;
            } else {
                this.isVisible = false;
            }
        },
        subirArriba() {
            window.scrollTo({
                top: 0,
                behavior: 'smooth'
            });
        }
    }
};

</script>

<style scoped>
.scroll-botton {

    position: fixed;
    bottom: 30px;
    right: 30px;
    background-color: #b44593;
    color: white;
    border: none;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.5em;
    cursor: pointer;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    transition: opacity 0.3s, transform 0.3s;
    z-index: 1000;
}

.scroll-botton:hover {
    background-color: #dd3675;
    transform: translateY(-5px);
}
</style>