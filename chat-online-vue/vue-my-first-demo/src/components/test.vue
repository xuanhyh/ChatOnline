<template>
    <p>{{ chengpin.title }}</p>
</template>

<script>
import axios from "axios"
import querystring from "querystring"

export default {
    name: 'test',
    data(){
        return {
            chengpin:{}
        }
    },
    mounted() {
        // axios({
        //     method:"get",
        //     url:"http://iwenwiki.com/api/blueberrypai/getChengpinDetails.php",
        // }).then(res =>{
        //     console.log(res.data);
        //     this.chengpin=res.data.chengpinDetails[0]
        // }).catch(error => {
        // console.error('请求失败', error);
        // // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
        // });

        axios({
            method:"post",
            url:"http://iwenwiki.com/api/blueberrypai/login.php",
            data: querystring.stringify({
                user_id: "iwen@qq.com",
                password: "iwen123",
                verification_code: "crfvw"
            })
        }).then(res =>{
            console.log(res.data);
        }).catch(error => {
        console.error('请求失败', error);
        // 在这里可以进行错误处理，比如给用户提示或者进行其他操作
        });
    }
}
</script>