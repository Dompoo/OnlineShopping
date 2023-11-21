<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const chats = ref([{
  id: 0,
  username: "",
  message: "",
  createdAt: "",
}]);
const chatMessage = ref([]);

const props = defineProps({
  roomId: {
    type: [Number, String],
    require: true,
  },
  postId: {
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
  router.push({name: "chatRoomExit", state: {
      roomId: props.roomId,
      postId: props.postId,
  }})
};
const sendChat = () => {
  router.push({name: "chatSend", state: {
      roomId: props.roomId,
      chatMessage: chatMessage.value
    }})
};

</script>
<!--TODO : 땡겨온 chats정보를 뿌리기 - 메시지, 유저명, 날짜-->
<template>
  <ul>
    <li v-for="chat in chats" :key="chat.id">
      <div class="username">
        {{chat.username}}
      </div>
      <div class="message">
        {{chat.message}}
      </div>
      <div class="createdAt">
        {{chat.createdAt}}
      </div>
    </li>
  </ul>
  <el-row>
    <el-col>
      <el-button type="warning" @click="sendChat">채팅 보내기</el-button>
    </el-col>
    <el-col>
      <el-button type="warning" @click="exit()">채팅방 나가기</el-button>
    </el-col>
  </el-row>
  <el-row>
    <el-col>
    <el-input v-model="chatMessage" placeholder="메시지를 입력하세요."/>
    </el-col>
  </el-row>
</template>
<style>

</style>