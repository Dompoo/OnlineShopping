<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const chats = ref([]);

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`onlineShopping-api/chats/${props.postId}`).then((response) => {
  response.data.forEach((r: any) => {
    chats.value.push(r)
  })
});
</script>

<!--TODO : 땡겨온 chats정보를 뿌리기-->
<template>

</template>
<style>

</style>