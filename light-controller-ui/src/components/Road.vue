<script setup lang="ts">
import Light from "@/components/Light.vue";

const props = defineProps({
  road: {default:{name:'jool'}}
})
</script>

<template>
  <div class="road">
    <div class="wrapper">
      <h4>{{ road.name }}</h4>
    </div>
    <span class="lights" v-for="light in road.lights" :key="light.lightId">
      <Light :light="light"/>
    </span>
  </div>
</template>

<style scoped>

</style>
