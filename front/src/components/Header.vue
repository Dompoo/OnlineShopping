<template>
  <el-header class="header">
    <el-menu mode="horizontal" router>
      <el-menu-item index="/">홈</el-menu-item>
      <el-menu-item index="/postWrite">글 작성</el-menu-item>
      <el-menu-item index="/productWrite">상품 등록</el-menu-item>
      <el-menu-item index="/login">로그인</el-menu-item>
      <el-menu-item index="/signup">회원가입</el-menu-item>

    </el-menu>
  </el-header>
</template>

<script setup lang="ts"></script>

<style scoped>
.header {
  padding: 0;
  height: 60px;
}

</style>