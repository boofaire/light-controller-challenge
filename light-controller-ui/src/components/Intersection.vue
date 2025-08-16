<script setup lang="ts">
import Road from "@/components/Road.vue";

const props = defineProps({
  intersection: {}
})
</script>

<template>
  <div class="intersection-display">
    <div class="wrapper">
      <h3>{{ intersection.name}}</h3>
    </div>
    <div class="roads" v-for="road in intersection.roads" :key="road.roadId">
      <Road :road="road"/>
    </div>
  </div>
</template>

<style scoped>

</style>
