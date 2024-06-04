<template>
    <div v-if="visible" class="modal-overlay">
      <div class="modal-content">
        <p>{{ message }}</p>
        <div class="button-group">
          <button class="confirm-button" @click="confirm">确认</button>
          <button class="cancel-button" @click="cancel">取消</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "ConfirmationDialog",
    props: {
      visible: {
        type: Boolean,
        default: false
      },
      message: {
        type: String,
        default: "Are you sure?"
      }
    },
    methods: {
      confirm() {
        this.$emit("confirm");
      },
      cancel() {
        this.$emit("cancel");
      }
    }
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.05);
    backdrop-filter: blur(0px); /* 添加模糊效果 暂时没加*/
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .modal-content {
    background-color: #2c2c2c; /* 深灰色背景 */
    padding: 20px;
    border-radius: 10px;
    text-align: center;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    color: #ffffff;
  }
  
  .button-group {
    margin-top: 20px;
  }
  
  .confirm-button {
    background-color: #ff4c4c; /* 浅灰色按钮 */
    border: none;
    color: #000;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 5px;
    margin: 0 10px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .cancel-button {
    background-color: #a9a9a9; /* 浅灰色按钮 */
    border: none;
    color: #000;
    padding: 10px 20px;
    font-size: 16px;
    border-radius: 5px;
    margin: 0 10px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }
  
  button:hover {
    background-color: #888; /* 按钮悬停时颜色 */
  }
  </style>
  