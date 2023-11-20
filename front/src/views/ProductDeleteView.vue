<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted} from "vue";
import axios from "axios";

const router = useRouter();

const props = defineProps({
  productId: {
    type: [Number, String],
    require: true,
  }
});

onMounted(() => {
  axios.delete(`/onlineShopping-api/products/${props.productId}`)
      .then(() => {
        router.replace({name: "productList"});
      });
});
</script>