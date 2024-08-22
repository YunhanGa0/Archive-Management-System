<template>
  <div>
    <div>sse测试</div>
    <div id="result"></div>
  </div>
</template>

<script>
export default {
  mounted() {
    var source = new EventSource('http://localhost:8080/sse/test/subscribe?id=1');

    source.addEventListener("msg", function(e) {
      let text = document.getElementById('result').innerText;
      text += '\n' + e.data;
      console.log(e);
      document.getElementById('result').innerText = text;
    });

    source.onerror = function(e) {
      if (e.readyState == EventSource.CLOSE) {
        let text = document.getElementById('result').innerText;
        text += '\n' + "连接关闭";
      }
    };

    source.onopen = function(event) {
      let text = document.getElementById('result').innerText;
      text += '\n 开启: ';
      console.log(event);
      document.getElementById('result').innerText = text;
    };
  }
}
</script>
