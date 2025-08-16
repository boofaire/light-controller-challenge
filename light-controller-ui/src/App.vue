<script setup lang="ts">
  import {onMounted, ref} from 'vue'
  import axios from 'axios'
  import Intersection from "@/components/Intersection.vue";

  const intersectionList = ref<object>([])
  const selectedIntersectionId = ref<number>(0)
  const currentIntersection = ref<object>({})
  const intersectionMonitorId = ref<number>(0)

  const stopMonitoring = async () => {
    if(intersectionMonitorId.value !== 0) {
      clearInterval(intersectionMonitorId.value);
    }
  }

  const monitorIntersection = async () => {
    await stopMonitoring();
    await retrieveSelectedIntersection();
    intersectionMonitorId.value = setInterval(retrieveSelectedIntersection, 1000);
  }

  const retrieveSelectedIntersection = async () => {
    await axios.get(`http://localhost:8080/intersections/${selectedIntersectionId.value}`)
          .then((response) => {
            currentIntersection.value = response.data;
            console.log(response.data)
            if(!response.data.cycling) {
              stopMonitoring();
            }
          })
          .catch(console.error)

  }

  const startSelectedIntersection = async () => {
    await axios.post(`http://localhost:8080/intersections/start`, { intersectionId: selectedIntersectionId.value })
        .then((response) => {
            console.log(response.data)
        })
        .catch(console.error)
    await monitorIntersection();
  }

  const stopSelectedIntersection = async () => {
    await stopMonitoring();
    await axios.post(`http://localhost:8080/intersections/stop`, { intersectionId: selectedIntersectionId.value })
        .then((response) => {
            console.log(response.data)
        })
        .catch(console.error)
    await retrieveSelectedIntersection();
  }

  const createDefault = async () => {
    const name = prompt("Enter an intersection nam to create an intersection with the default configuration.");
    if(name) {
      await axios.post('http://localhost:8080/intersections/createDefault', { name: name })
          .then(() => {
            getIntersectionSummaries()
          })
          .catch(console.error)
    }
  }

 const getIntersectionSummaries = async () => {
   await axios.get('http://localhost:8080/intersections/summaries')
      .then((response) => {
        intersectionList.value = response.data
      })
      .catch(console.error)
  }

  onMounted(async () => {
    await getIntersectionSummaries();
  })
</script>

<template>
  <header>
    <div class="wrapper">
      <h1>Intersection Light Controller</h1>
    </div>
  </header>

    <main>
        <div class="intersection-controller">
            <label>
                Select Intersection
                <select class="intersection-select" v-model="selectedIntersectionId"
                        @change="monitorIntersection">
                    <option v-for="intersection in intersectionList" :key="intersection.intersectionId"
                            :value="intersection.intersectionId" :label="intersection.name"/>
                </select>
            </label>
            <div class="intersection-display">
                <Intersection :intersection="currentIntersection"/>
            </div>

            <button class="action-button" id="start-cycling" type="button" @click="startSelectedIntersection">
                Start Cycle
            </button>

            <button class="action-button" id="stop-cycling" type="button" @click="stopSelectedIntersection">
                Stop Cycle
            </button>

            <button class="action-button" type="button" @click="createDefault">
                Create Intersection
            </button>

        </div>
    </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    margin: calc(var(--section-gap) / 4);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.light-controller {
  display: grid;
  place-items: center;
  gap: 1rem;

  .light {
    display: grid;
    gap: .5rem;
  }

}

input[type='radio'].red {
  accent-color: #cc3232;
  color: #cc3232;
}

input[type='radio'].yellow {
  accent-color: #e7b416;
}

input[type='radio'].green {
  accent-color: #2dc937;
}
</style>
