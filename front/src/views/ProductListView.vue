<script setup lang="ts">

import {useRouter} from "vue-router";
import axios from "axios";
import {ref} from "vue";

const router = useRouter();

const products = ref([{
  id: 0,
  productName: "",
  price: 0,
}]);

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
      <div class="productName">
        <router-link :to="{ name: 'productDetail', params: {productId: product.id}}">
          {{product.productName}}
        </router-link>
      </div>

      <div class="price">
        {{product.price}}
      </div>
    </li>

  </ul>
</template>
<style></style>