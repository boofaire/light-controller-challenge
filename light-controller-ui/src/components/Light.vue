<script setup lang="ts">
import {ref} from "vue";

const props = defineProps({
  light: {}
})
const color = ref<string>('green')
</script>

<template>
  <div class="light">
    <div class="wrapper">
      <h6>{{ light.name}}</h6>
    </div>
    <svg width="22" height="22">
      <circle :class="light.activeColor"  cx="11" cy="11" r="10" stroke="black" stroke-width="1"/>
    </svg>
  </div>
</template>

<style scoped>

circle.OFF {
  fill: none
}

circle.RED {
  fill: #cc3232
}

circle.YELLOW {
  fill: #e7b416;
}

circle.GREEN {
  fill:  #2dc937;
}
</style>
