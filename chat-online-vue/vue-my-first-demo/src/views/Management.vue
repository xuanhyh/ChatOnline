<template>
    <div class="container">
        <!-- 侧边栏 -->
        <div class="sidebar">
            <div class="left-icon" v-for="(channel, index) in channels" :key="index" @click="selectChannel(channel)">
                <img :src="channel.url" :alt="channel.name" />
            </div>
        </div>

        <div class="manager-container" :style="{ width: `${dialogContainerWidth}px` }">
            <div class="manager-header">
                <div v-for="(aspect, index) in aspects" class="aspect-label"
                    :class="{ 'aspect-label-selected': labelSelectedIndex === index }"
                    @click="labelSelectedIndex = index, labelSelectedLabel = aspect.label">
                    {{ aspect.label }}
                </div>
            </div>
            <div class="manager-body">
                <div v-if="labelSelectedLabel == '好友'">
                    好友页面
                    <div class="friends-grid">
                        <div v-for="friend in friends"
                            :class="friend.userId === this.now_select_friend_id ? 'friend-selected' : 'friends'"
                            class="friends-click" :key="friend.userId"
                            @click="selectFriend(friend.userId, friend.name)">
                            <div class="info-card">
                                <div class="avatar-and-point">
                                    <img class="friends-avatar"
                                        :src="friend.avatarUrl ? friend.avatarUrl : friendDefaultAvatar"
                                        :alt="friend.name" />
                                    <div :class="friend.state === 0 ? 'online' : 'not-online'"></div>
                                </div>
                                <span class="friends-nickname">{{ friend.name + "(" + friend.username + ")" }}</span>
                            </div>
                            <div class="selection">
                                <div class="delete-friend" @click="selectFriend(friend.userId);showDeleteFriendDialog();"></div>
                                <div class="modify-friend" @click=""></div>
                            </div>
                            <ConfirmationDialog :visible="isDeleteFriendDialogVisible" :message="deleteDialogMessage"
                                @confirm="deleteFriend()" @cancel="isDeleteFriendDialogVisible = false" />
                        </div>
                    </div>
                </div>
                <div v-if="labelSelectedLabel == '群聊'">
                    群聊页面
                    <div class="friends-grid">
                        <div v-for="group in groups"
                            :class="group.groupId === this.now_select_group_id ? 'friend-selected' : 'friends'"
                            class="friends-click" :key="group.groupId"
                            @click="selectFriend(group.groupId, group.groupName)">
                            <div class="info-card">
                                <div class="avatar-and-point">
                                    <img class="friends-avatar"
                                        :src="group.avatarUrl ? group.avatarUrl : groupDefaultAvatar"
                                        :alt="group.groupName" />
                                </div>
                                <span class="friends-nickname">{{ group.groupName }}</span>
                            </div>
                            <div class="selection">
                                <div class="quit-group"></div>
                                <div class="modify-friend"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import axios from "axios";
import AddFriend from './AddFriend'
import ConfirmationDialog from "./ConfirmationDialog.vue";

export default {
    name: "ChatWithFriend",
    components: {
        AddFriend,
        ConfirmationDialog,
    },
    created() {
        console.log("ChatWithFriend获取到参数", this.$route.params.userName);
        const jsonParsed = JSON.parse(sessionStorage.getItem("userInfo"));
        if (jsonParsed) {
            this.userInfo_from_store = jsonParsed;
            this.getFriendList();
            this.getGroupList();
        }
    },
    computed: {
        groupedFriends() {
            const grouped = this.groupings.map(grouping => {
                return {
                    ...grouping,//...将 grouping 对象中的所有键值对复制到新对象中
                    isExpanded: true, // 默认展开
                    friends: this.friends.filter(friend => friend.groupingId === grouping.groupingId),
                    //过滤 friends 数组，只保留 groupingId 与当前分组对象的 groupingId 相匹配的朋友。
                };
            });
            // 提取没有分组的朋友
            const ungroupedFriends = {
                userId: this.userInfo_from_store.userId,
                groupingId: -1,
                groupingName: '未分组',
                isExpanded: true, // 默认展开
                friends: this.friends.filter(friend => !friend.groupingId)
            };

            return [...grouped, ungroupedFriends];
        },
    },

    data() {
        return {
            now_select_friend_id: -1,
            now_select_group_id: -1,
            showPopup: false,
            labelSelectedIndex: -1,
            labelSelectedLabel: '好友',
            friendDefaultAvatar: require("../assets/私聊头像.png"),
            groupDefaultAvatar: require("../assets/群聊头像.png"),
            isDeleteFriendDialogVisible: false,
            deleteDialogMessage: "您确定要删除好友吗？",
            channels: [
                { name: "私聊", url: require("../assets/私聊.png") },
                { name: "群聊", url: require("../assets/群聊.png") },
                { name: "探索", url: require("../assets/探索.png") },
                { name: "管理", url: require("../assets/管理.png") },
            ],
            aspects: [
                { label: "好友" },
                { label: "群聊" },
            ],
            friends: [
                /*{
                  "userId": 2,
                  "name": "李四",
                  "username": "lisi",
                  "password": "e10adc3949ba59abbe56e057f20f883e",
                  "sex": "女",
                  "avatarUrl": "https://s2.loli.net/2024/05/20/YuRryljA9KtLQBF.jpg",
                  "state": 0,
                  "email": "234@qq.com",
                  "createTime":,
                  "updateTime":
                  }
                  */
            ],
            groups: [
                //  { "groupId": 1,"groupName": "聊天交友群","createTime": ,"updateTime": }
            ],
            userInfo_from_query: {
                id: 0,
                userName: "no",
                name: "no",
            },
            userInfo_from_store: {
                userId: 0,
                userName: "no",
                name: "no",
                token: "0",
            },
            errorMessage: "",
        };
    },
    props: {},
    mounted() {
        window.addEventListener("resize", this.checkWindowSize);
        this.checkWindowSize();

    },
    beforeDestroy() {
        window.removeEventListener("resize", this.checkWindowSize);
    },

    methods: {
        getFriendList() {
            axios({
                method: "get",
                url: "http://localhost:8080/api/user/getFriendByIdWithGroupingId",
                headers: {
                    token: this.userInfo_from_store.token,
                },
                params: {
                    id: this.userInfo_from_store.userId,
                },
            })
                .then((res) => {
                    console.log("ChatWithFriend axios收到好友列表数据:");
                    console.log(res.data);
                    this.friends = res.data.data;

                    // 获取好友数据后立即查询有无未读新消息
                    this.updateUnreadMessageNum();
                })
                .catch((error) => {
                    console.error("请求失败", error);
                    // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
                    if (error.response) {
                        this.errorMessage = `请求失败，状态码：${error.response.status}`;
                    } else {
                        this.errorMessage = "请求失败，请重试。";
                    }
                });
        },
        getGroupList() {
            axios({
                method: "get",
                url: "http://localhost:8080/api/group/getAllGroup",
                headers: {
                    token: this.userInfo_from_store.token,
                },
                params: {
                    userId: this.userInfo_from_store.userId,
                },
            })
                .then((res) => {
                    console.log("ChatWithGroup axios收到群聊列表数据:");
                    console.log(res.data);
                    this.groups = res.data.data;

                    // 获取好友数据后立即查询有无未读新消息
                    this.updateUnreadMessageNum();
                })
                .catch((error) => {
                    console.error("请求失败", error);
                    // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
                    if (error.response) {
                        this.errorMessage = `请求失败，状态码：${error.response.status}`;
                    } else {
                        this.errorMessage = "请求失败，请重试。";
                    }
                });
        },
        selectFriend(id, name) {
            this.now_select_friend_id = id;
            // this.now_chat_friend_name = name;
            console.log("选中用户，id为：" + this.now_select_friend_id);
        },
        checkWindowSize() {
            this.showBroadcast = window.innerWidth > 1000;
            const broadcastContainerWidth = this.showBroadcast ? 240 : 0;
            const availableWidth = window.innerWidth - 320 - broadcastContainerWidth;
            this.dialogContainerWidth = availableWidth;
        },

        selectChannel(channel) {
            // 处理选择头像的逻辑，例如显示选中状态等
            console.log("Selected Channel:", channel.name);
            if (channel.name === "管理") this.resetMiddle();
            else if (channel.name === "群聊") {
                this.$router.push({
                    path: "/chatWithGroup/" + this.userInfo_from_store.userName,
                });
            }
            else if (channel.name === "私聊") {
                this.$router.push({
                    path: "/chatWithFriend/" + this.userInfo_from_store.userName.userName,
                });
            }
            this.now_chat_id = 0;
        },
        resetMiddle() {
            this.friendSelected = false;
            this.chatTo = "寻找或开始新的对话";
            // this.friendClickStyle = "friends";
        },
        deleteFriend() {
            axios({
                method: 'delete',
                url: 'http://localhost:8080/api/user/delete',
                headers: {
                    token: this.userInfo_from_store.token,
                },
                params: {
                    FriendId: this.now_select_friend_id,
                }
            })
            .then(response => {
                console.log(response.data)
                if (response.data.code == 1){
                    this.isDeleteFriendDialogVisible=false;
                    this.getFriendList();//重新获取好友列表
                }
                else{

                }
            })

        },
        showDeleteFriendDialog() {
            console.log(this.isDeleteFriendDialogVisible);
            this.isDeleteFriendDialogVisible=true;
        }
    },
};
</script>



<style scoped>
.container {
    display: flex;
    /*有没有没啥区别*/
    height: 100vh;
    min-width: 720px;
}

.sidebar {
    position: fixed;
    top: 0;
    left: 0;
    width: 80px;
    /* 调整宽度 */
    height: 100%;
    background-color: #1d1d24;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    padding-top: 40px;
}

.username-label {
    margin-top: 20px;
    margin-bottom: 8px;
    color: white;
    font-size: 24px;
    font-family: "STXingkai", "Microsoft YaHei";
}

.left-icon {
    width: 60px;
    /* 调整头像大小 */
    height: 60px;
    /* 调整头像大小 */
    border-radius: 50%;
    /* 圆形头像 */
    overflow: hidden;
    cursor: pointer;
    transition: background-color 0.5s ease, border-radius 0.3s ease;
}

.left-icon:hover {
    border-radius: 16px;
    /* 鼠标悬浮时将头像变为圆角矩形 */
    background-color: green;
}

/* 
.left-icon + .left-icon {
  margin-top: 0px;
} */

.left-icon img {
    max-width: 50px;
    height: auto;
    object-fit: cover;
}


.manager-container {
    flex: 1;
    top: 0;
    height: 100%;
    width: cal(100%-80px);
    margin-left: 80px;
    background-color: #383d4d;
    /* #313338 */
    color: #fff;
    display: flex;
    flex-direction: column;
    /* 垂直排列子元素 */
    border-left: #3d4e54 solid 1px;
}

.manager-header {
    height: 20px;
    padding: 10px;
    background-color: #2f3241;
    border-bottom-left-radius: 8px;
    color: #e0e0e0;
    font-weight: bold;
    border-bottom: #2c4d5d solid 1px;
    display: flex;
    flex-direction: row;
    justify-content: center;
}

.manager-body {
    flex: 1;
    padding: 10px;
    height: 80%;
    min-height: 200px;
}

.friends-grid {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    /* 每行三个项目 */
    gap: 40px 120px;
    /* 项目之间的间距 */
}

.aspect-label {
    padding: 0 16px 28px 16px;
    margin: 0 4px;
}

.aspect-label:hover {
    border-bottom: #e0e0e0 solid 4px;
}

.aspect-label-selected {
    padding: 0 16px 28px 16px;
    border-bottom: #e0e0e0 solid 4px;
    margin: 0 4px;
}


.friends {
    width: 100%;
    display: flex;
    justify-content: space-between;
    /* 左右分布 */
    align-items: center;
    /* 垂直居中 */
    transition: background-color 0.2s ease;
    padding: 12px;
    box-sizing: border-box;
    /* 使用border-box以包含padding和border */
    border-radius: 16px;
    background-color: #636a7e80;
}

.friends:hover {
    background-color: #5959c9;
}

.friend-selected {
    width: 100%;
    display: flex;
    justify-content: space-between;
    /* 左右分布 */
    align-items: center;
    /* 垂直居中 */
    background-color: #5959c9;
    padding: 12px;
    box-sizing: border-box;
    /* 使用border-box以包含padding和border */
    border-radius: 16px;
}

.info-card {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.friends-avatar {
    width: 50px;
    /* 调整头像大小 */
    height: 50px;
    /* 调整头像大小 */
    border-radius: 50%;
    /* 圆形头像 */
    overflow: hidden;
    /*头像容器内部元素超出边界时隐藏 */
    cursor: pointer;
    /*当鼠标悬停在头像上时显示为指针 */
    transition: background-color 0.5s ease, border-radius 0.3s ease;
    margin: 0 20px;
}

.friends-avatar:hover {
    background-color: rgb(115, 136, 254);
}

.avatar-and-point {
    width: 120px;
    height: 50px;
    position: relative;
    /* 确保子元素的定位是相对于该容器的 */
    display: flex;
    flex-direction: row;
    align-items: center;
}

.friends-avatar img {
    top: 0;
    left: 0;
    position: absolute;
    /* 绝对定位 */
    max-width: 50px;
    height: auto;
    object-fit: cover;
    z-index: 1;
}

.friends-nickname {
    font-size: 16px;
    /* 设置合适大小的字体 */
    color: #ebebeb;
    /* 白色字体 */
}

.friends-click {
    cursor: pointer;
}


.online {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    margin: 8px;
    background-color: #00ff11;
}

.not-online {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    margin: 8px;
    background-color: #222222;
}

.selection {
    display: flex;
    flex-direction: row;
}

.delete-friend {
    border-radius: 50%;
    width: 40px;
    height: 40px;
    background-image: url("../assets/删除.png");
    background-size: contain;
    background-position: center;
    background-repeat: no-repeat;
    background-color: #4590ff;
    margin: 8px;
}

.delete-friend:hover {
    background-color: #3184ff;
}

.modify-friend {
    border-radius: 50%;
    width: 40px;
    height: 40px;
    background-image: url("../assets/更多.png");
    background-size: contain;
    background-position: center;
    background-repeat: no-repeat;
    background-color: #4590ff;
    margin: 8px;
}

.modify-friend:hover {
    background-color: #3184ff;
}

.quit-group {
    border-radius: 50%;
    width: 40px;
    height: 40px;
    background-image: url("../assets/退出.png");
    background-size: contain;
    background-position: center;
    background-repeat: no-repeat;
    background-color: #4590ff;
    margin: 8px;
}

.quit-group:hover {
    background-color: #3184ff;
}
</style>