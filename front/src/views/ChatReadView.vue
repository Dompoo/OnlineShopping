<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const chats = ref([]);

const props = defineProps({
  roomId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`onlineShopping-api/chats/${props.roomId}`).then((response) => {
  response.data.forEach((r: any) => {
    chats.value.push(r)
  })
});

const exit = () => {
  router.push({name: "exitChatRoom", state: {roomId: props.roomId}})
};

</script>
<!--TODO : 땡겨온 chats정보를 뿌리기-->
<template>
  <el-button type="warning" @click="exit()">채팅방나가기</el-button>
</template>
<style>

</style>