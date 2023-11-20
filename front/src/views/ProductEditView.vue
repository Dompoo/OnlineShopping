<script setup lang="ts">

import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import axios from "axios";

const router = useRouter();

const product = ref({
  id: 0,
  productName: "",
  price: 0,
});

const props = defineProps({
  productId: {
    type: [Number, String],
    require: true,
  },
});

const edit = () => {
  axios.patch(`/onlineShopping-api/products/${props.productId}`,
      {
        productName: product.value.productName,
        price: product.value.price,
      }).then(() => {
        router.replace({name: "productDetail", params: {productId: props.productId}})
  });
};

onMounted(() => {
  axios.get(`/onlineShopping-api/products/${props.productId}`).then((response) => {
    product.value = response.data;
  });
});
</script>
<template>
  <div class="mt-2">
    <el-input v-model="product.productName"/>
  </div>

  <div class="mt-2">
    <el-input v-model="product.price"/>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정완료</el-button>
  </div>
</template>
<style>

</style>