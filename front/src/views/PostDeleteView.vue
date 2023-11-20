<script setup lang="ts">

import {onMounted} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();


const props = defineProps({
  postId: {
    type: [Number, String],
    require: true,
  },
});

onMounted(() => {
  axios.delete(`/onlineShopping-api/posts/${props.postId}`)
      .then(() => {
        router.replace({name: "postList"});
      });
})
</script>