<script setup lang="ts">

import {ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

axios.get(`/onlineShopping-api/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/onlineShopping-api/posts/${props.postId}`,
      {
        title: post.value.title,
        content: post.value.content
      }).then(() => {
        router.replace({name: "postList"});
      });
};


</script>

<template>
  <div class="mt-2">
    <el-input v-model="post.title"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>
</template>

<style>

</style>
