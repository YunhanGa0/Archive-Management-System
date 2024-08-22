<template>
  <div id="app">
    <div class="content">
      <HelloWorld/>
      <Learn />
    </div>
  </div>
</template>

<script>
import Learn from "./components/archiveManagement.vue";
import HelloWorld from "./components/HelloWorld.vue";


export default {
  name: 'App',
  components: {
    //HelloWorld,
    Learn
  }
}
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  display: flex;
}

.content {
  flex: 1;
  padding: 20px;
}

#app {
  display: flex;
}
</style>
