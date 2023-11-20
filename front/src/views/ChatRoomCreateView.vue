<script setup lang="ts">

import axios from "axios";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  }
});

const room = ref({
  id: 0,
})

onMounted(() => {
  axios.post(`onlineShopping-api/rooms/${props.postId}`)
      .then((response) => {
        room.value = response.data;
      })
      .then(() => {
        router.replace({name: "chatDetail", state: {roomId: room.value, postId: props.postId}})
      });
})
</script>