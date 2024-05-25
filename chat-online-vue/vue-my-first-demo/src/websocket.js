import { useWebSocket } from 'vue-native-websocket-vue3';

export function useChatWebSocket() {
  const { connect, send, message, status, disconnect } = useWebSocket('ws://localhost:8080/api/chat', {
    reconnection: true, // 自动重连
    reconnectionAttempts: 5, // 最大重连次数
    reconnectionDelay: 3000, // 重连间隔时间
  });

  return { connect, send, message, status, disconnect };
}
