<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const moveToEdit = () => {
  router.push({name: "edit", state: {posstId: props.postId}})
}

const moveToDelete = () => {
  router.push({name: "delete", state: {postId: props.postId}})
};

const moveToChatRoom = () => {
  router.push({name: "chat", state: {postId: props.postId}})
};

onMounted(() => {
  axios.get(`/onlineShopping-api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  });
})
</script>

<template>
  <el-row>
    <el-col>
      <h2 class="title">{{post.title}}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">2023-09-29 21:02</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{post.content}}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
        <el-button type="warning" @click="moveToDelete()">삭제</el-button>
        <el-button type="warning" @click="moveToChatRoom()">채팅</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}
.sub {
  margin-top: 10px;
  font-size: 0.78rem;

  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}
.content {
  font-size: 0.95rem;
  margin-top: 12px;
  color: #797979;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>