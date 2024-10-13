import { ref, onMounted, onBeforeUnmount } from "vue";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export function useStomp(url: string, accessToken: string) {
  const client = ref<Client | null>(null);
  const isConnected = ref(false);
  const messages = ref<any[]>([]);

  const connect = () => {
    client.value = new Client({
      brokerURL: url,
      connectHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
      webSocketFactory: () => new SockJS(url),
      debug: (str) => {
        console.log(new Date(), str);
      },
      onConnect: () => {
        isConnected.value = true;
        console.log("STOMP connected");
      },
      onDisconnect: () => {
        isConnected.value = false;
        console.log("STOMP disconnected");
      },
      onStompError: (frame) => {
        console.error("STOMP error:", frame);
      },
    });

    client.value.activate();
  };

  const subscribe = (destination: string, callback: (message: any) => void) => {
    if (client.value) {
      client.value.subscribe(destination, (message) => {
        callback(JSON.parse(message.body));
      });
    }
  };

  const sendMessage = (destination: string, body: any) => {
    if (client.value && isConnected.value) {
      client.value.publish({
        destination,
        body: JSON.stringify(body),
      });
    }
  };

  const disconnect = () => {
    if (client.value) {
      client.value.deactivate();
    }
  };

  onMounted(() => {
    connect();
  });

  onBeforeUnmount(() => {
    disconnect();
  });

  return {
    isConnected,
    messages,
    subscribe,
    sendMessage,
    disconnect,
  };
}
