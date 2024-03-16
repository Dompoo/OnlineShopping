<script setup lang="ts">
import { ref } from 'vue'

import axios from 'axios'
import { useRouter } from 'vue-router'

const name = ref("")
const email = ref("")
const password = ref("")

const router = useRouter();

const signup = function () {
  axios.post("/onlineShopping-api/auth/signup", {
    name: name.value,
    email: email.value,
    password: password.value
  })
      .then(() => {
        router.replace({name: "login"});
      });
};
</script>

<template>
  <div class="mt-2">
    <el-input v-model="name" placeholder="이름"/>
  </div>

  <div class="mt-2">
    <el-input v-model="email" placeholder="이메일"/>
  </div>

  <div class="mt-2">
    <el-input v-model="password" placeholder="비밀번호"/>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="signup()">회원가입</el-button>
    </div>
  </div>
</template>

<style>

</style>
