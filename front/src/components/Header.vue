<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useCookies } from 'vue3-cookies'

const isLoggedIn = ref(false)

const { cookies } = useCookies()

const checkLoginStatus = () => {

  const jwtToken = cookies.get('SESSION');
  console.log(jwtToken)

  if (jwtToken) {
    isLoggedIn.value = true;
  } else {
    isLoggedIn.value = false;
  }
};

const logout = () => {
  cookies.remove('SESSION');
  checkLoginStatus();
};

onMounted(() => {
  checkLoginStatus();
});

</script>

<template>
  <el-header class="header">
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">홈</el-menu-item>
      <el-menu-item index="/productList">상품 리스트</el-menu-item>
      <el-menu-item index="/postWrite">글 작성</el-menu-item>
      <el-menu-item index="/productWrite">상품 등록</el-menu-item>
      <el-menu-item v-show="!isLoggedIn" index="/login">로그인</el-menu-item>
      <el-menu-item v-show="!isLoggedIn" index="/signup">회원가입</el-menu-item>
      <el-menu-item v-show="isLoggedIn" @click="logout">로그아웃</el-menu-item>

    </el-menu>
  </el-header>
</template>

<style scoped>
.header {
  padding: 0;
  height: 60px;
}

</style>