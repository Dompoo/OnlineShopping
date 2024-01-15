<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useStore } from '@/router'

const store = useStore()
const isLoggedIn = ref(store.state.isLoggedIn)

onMounted(() => {
  isLoggedIn.value = store.state.isLoggedIn;
});

// Vuex 상태가 변경될 때마다 컴포넌트 상태를 업데이트
store.watch(() => store.state.isLoggedIn, (newValue) => {
  isLoggedIn.value = newValue;
});
</script>

<template>
  <el-header class="header">
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">홈</el-menu-item>
      <el-menu-item index="/postWrite">글 작성</el-menu-item>
      <el-menu-item index="/productWrite">상품 등록</el-menu-item>
      <el-menu-item v-show="!isLoggedIn" index="/login">로그인</el-menu-item>
      <el-menu-item v-show="!isLoggedIn" index="/signup">회원가입</el-menu-item>
      <el-menu-item v-show="isLoggedIn" index="/logout">로그아웃</el-menu-item>

    </el-menu>
  </el-header>
</template>

<style scoped>
.header {
  padding: 0;
  height: 60px;
}

</style>