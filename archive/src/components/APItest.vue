<template>
  <div>{{ data }}</div>
</template>

<script>
export default {
  data() {
    return {
      data: ""
    };
  },
  methods: {
    async name() {
      try {
        const { data: res } = await this.$http.get('/archives');
        this.data = res;
      } catch (error) {
        console.error('API请求失败:', error);
      }
    }
  },
  created() {
    this.name();
  }
};

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
