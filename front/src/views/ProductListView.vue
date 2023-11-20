<script setup lang="ts">

import {useRouter} from "vue-router";
import axios from "axios";
import {ref} from "vue";

const router = useRouter();

const products = ref([]);

// 상품 리스트 조회
axios.get("/onlineShopping-api/products?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    products.value.push(r)
  })
});

</script>
<template>
  <div>
    <router-link :to="{ name: 'productWrite'}"/>
  </div>
  <ul>
    <li v-for="product in products" :key="product.id">
      <div class="title">
        <router-link :to="{ name: 'productDetail', params: {productId: product.id}}">
<!--          상품 이름-->
        </router-link>
      </div>

      <div class="content">
        {{product.content}}
      </div>
    </li>

  </ul>
</template>
<style></style>