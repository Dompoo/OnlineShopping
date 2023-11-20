<script setup lang="ts">
import {ref} from "vue";

import axios from 'axios';
import {useRouter} from "vue-router";

const productName = ref("")
const price = ref("")

const router = useRouter();

const write = function () {
  axios.post("/onlineShopping-api/products", {
    productName: productName.value,
    price: price.value
  })
      .then(() => {
        router.replace({name: "productList"});
      });
};
</script>

<template>
  <div class="mt-2">
    <el-input v-model="productName" placeholder="상품 이름을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <el-input v-model="price" placeholder="가격을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="primary" @click="write()">상품 등록</el-button>
    </div>
  </div>
</template>

<style>

</style>
