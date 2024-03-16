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
<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1.9rem;

    .title {
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }
      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 9px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }

  }
}
</style>