<script setup lang="ts">
import {onMounted} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";

const router = useRouter();

const props = defineProps({
  roomId: {
    type: [Number, String],
    required: true,
  },
  chatMessage: {
    type: [Number, String],
    required: true,
  }
});

onMounted(() =>
    axios.post(`onlineShopping-api/chats/${props.roomId}`,
        {
          message: props.chatMessage
        })
        .then(() =>
        router.replace({name: "chatRead", state: {roomId: props.roomId}}))
)
</script>