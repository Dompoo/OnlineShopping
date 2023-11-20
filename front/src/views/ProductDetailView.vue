<script setup lang="ts">

import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import axios from "axios";

const router = useRouter();

const props = defineProps({
  productId: {
    type: [Number, String],
    required: true,
  },
});

const product = ref({
  id: 0,
  productName: "",
  price: 0,
});

const moveToEdit = () => {
  router.push({name: "productEdit", state: {productId: props.productId}})
};

const moveToDelete = () => {
  router.push({name: "productDelete", state: {productId: props.productId}})
};

onMounted(() => {
  axios.get(`/onlineShopping-api/products/${props.productId}`).then((response) => {
    product.value = response.data;
  });
});
</script>
<template>
  <el-row>
    <el-col>
      <h2 class="title">{{product.productName}}</h2>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{product.price}}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
        <el-button type="warning" @click="moveToDelete()">삭제</el-button>
      </div>
    </el-col>
  </el-row>
</template>
<style></style>
