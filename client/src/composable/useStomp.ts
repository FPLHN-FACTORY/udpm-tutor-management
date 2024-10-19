import { DATE_FORMAT, WEBSOCKET_TOPIC } from "@/constants/common";
import { Client, StompSubscription } from "@stomp/stompjs";
import dayjs from "dayjs";
import SockJS from "sockjs-client";
import { onBeforeUnmount, onMounted, ref } from "vue";

export function useStomp({
  url,
  accessToken,
  domain,
}: {
  url: string;
  accessToken: string;
  domain: string;
}) {
  const client = ref<Client | null>(null);
  const isConnected = ref(false);
  const messages = ref<any[]>([]);
  const subscriptions = ref<StompSubscription[]>([]);

  const connect = () => {
    return new Promise<void>((resolve, reject) => {
      client.value = new Client({
        brokerURL: `ws://${domain}/ws`,
        connectHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
        webSocketFactory: () => new SockJS(url),
        debug: (str) => {
          console.log(dayjs().format(DATE_FORMAT.DATE_TIME), str);
        },
        onWebSocketError: (event) => {
          console.error("WebSocket error:", event);
          reject(event);
        },
        onConnect: () => {
          isConnected.value = true;
          resolve();
        },
        onDisconnect: () => {
          isConnected.value = false;
        },
        onStompError: (frame) => {
          console.error("STOMP error:", frame);
          reject(frame);
        },
      });

      client.value.activate();
    });
  };

  const subscribe = (destination: string, callback: (message: any) => void) => {
    if (!(client.value && isConnected.value)) {
      console.warn("Client is not connected or not initialized");
      return;
    }

    const subscription = client.value.subscribe(destination, (message) => {
      const parsedMessage = JSON.parse(message.body);
      messages.value.push(parsedMessage);
      callback(parsedMessage);
    });

    subscriptions.value.push(subscription);
  };

  const sendMessage = (destination: string, body: any) => {
    if (client.value && isConnected.value) {
      client.value.publish({
        destination,
        body: JSON.stringify(body),
      });
    } else {
      console.warn("Client is not connected or not initialized");
    }
  };

  const disconnect = () => {
    if (!client.value) {
      return;
    }

    subscriptions.value.forEach((subscription) => subscription.unsubscribe());
    subscriptions.value = [];
    client.value.deactivate();
  };

  onMounted(() => {
    connect()
      .then(() => {
        subscribe(WEBSOCKET_TOPIC.NOTIFICATION, (message) => {
          messages.value.push(message);
        });
      })
      .catch((error) => {
        console.error("Failed to connect:", error);
      });
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
