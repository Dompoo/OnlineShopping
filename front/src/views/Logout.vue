<script setup lang="ts">
import { onMounted } from 'vue'

import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter();

onMounted(() => {
  axios.post("/onlineShopping-api/auth/logout")
    .then(() => {
      router.replace({name: "postList"});
    });
})
</script>