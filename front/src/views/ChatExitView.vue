<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted} from "vue";
import axios from "axios";

const router = useRouter();

const props = defineProps({
  roomId: {
    type: [Number, String],
    required: true,
  },
  postId: {
    type: [Number, String],
    required: true,
  },
});

onMounted(() => {
  axios.delete(`/onlineShopping-api/chats/${props.roomId}`)
      .then(() =>
          router.replace({name: "postDetail", state: {postId: props.postId}}));
});
</script>