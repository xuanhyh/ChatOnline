![](media/a1aa8d7403f2e28199e7c8b88ef5371d.emf)

目录

[一、项目背景](#一项目背景)

[二、项目开发目的与需求](#二项目开发目的与需求)

[三、开发环境](#三开发环境)

[四、开发流程图](#四开发流程图)

[五、项目结构和模板语法](#五项目结构和模板语法)

[5.1 项目结构](#51-项目结构)

[5.1.1前端项目结构](#511前端项目结构)

[5.1.2前端项目结构](#512前端项目结构)

[5.2 模板语法](#52-模板语法)

[六、系统架构设计](#六系统架构设计)

[6.1. 系统整体架构](#61-系统整体架构)

[6.2. 前端设计](#62-前端设计)

[6.3. 后端设计](#63-后端设计)

[6.4. 数据库设计](#64-数据库设计)

[七、功能实现](#七功能实现)

[7.1 登录与注册](#71-登录与注册)

[7.2 聊天管理（私聊，群聊）](#72-聊天管理私聊群聊)

[7.3 好友管理（要求可分组管理）](#73-好友管理要求可分组管理)

[八、评估和总结](#八评估和总结)

# 一、项目背景

随着互联网的快速发展，线上聊天系统已成为人们日常沟通的重要方式。为了满足用户对于即时、高效、安全的沟通需求。在线聊天系统是一种实时通信工具，允许用户通过网络进行文字、音频和视频的交流，项目旨在开发一款功能完善、易于使用的线上聊天系统。本技术文档旨在介绍在线聊天系统的架构、功能、实现细节

# 二、项目开发目的与需求

实现在线聊天系统，功能要求：

开发目的：

1.登录与注册

2.聊天管理（私聊，群聊）

可查询聊天记录，可选择下载该聊天记录到本地；

3.好友管理（要求可分组管理）

该功能实现对好友的移动，删除，重新发送验证信息等业务。

# 三、开发环境

软件：

操作系统：Windows 10

Java开发IDE：Intellij IDEA 2023.3.6

HTML/JS/CSS开发IDE：Sublime Text 3

测试浏览器：Microsoft Edge

测试服务器：Tomcat 8.0.22

硬件：

处理器：Intel® Core(TM) i5-5200U CPU @ 2.20GHz 2.19GHz

控制器：Intel® 9 Series Chipset Family SATA AHCI Controller

内存(RAM)：8.00 G

# 四、开发流程图

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/2b5cf2e3a0974c5134b36e7682068463.png)

# 五、项目结构和模板语法

## 5.1 项目结构

## 5.1.1后端项目结构

**.idea**：这是 IntelliJ IDEA 的项目配置文件夹，包含项目的配置信息、工程文件、运行配置等。它通常是由 IntelliJ IDEA 在创建项目时自动生成的。

**src**：这是源代码文件夹，包含了项目的所有源代码和资源文件。

**main**：主要的源代码目录，包含了项目的主要功能代码。

**java**：Java源代码目录，包含了Java类文件。

**com.chat**：Java包名，通常是根据项目的组织结构来命名的。

**common**：通用模块，可能包含常量、上下文、异常、属性、结果等工具类。

constant：包含项目中使用的常量。

context：提供应用程序的上下文信息。

exception：定义和处理应用程序可能抛出的异常。

properties：包含应用程序的配置属性。

result：定义和封装应用程序的返回结果。

utils：包含通用的工具类，用于提供各种实用功能。

**config**：配置类，包含Spring、MyBatis等框架的配置。

**controller**：控制器类，处理HTTP请求和响应。

**handler**：处理器类，处理各种请求。

**interceptor**：拦截器类，拦截请求和响应。

**mapper**：数据访问层的Mapper接口。

**pojo**：JavaBean类，包含DTO（数据传输对象）、Entity（实体）、VO（值对象）等。

**service**：服务接口和实现类。

**impl**：服务实现类。

**resources**：资源文件目录，包含了项目的配置文件、静态资源文件等。

**com.chat**：资源文件包目录。

**mapper**：MyBatis Mapper XML 文件目录。

**test**：测试代码目录，包含了项目的单元测试和集成测试。

**java**：测试Java源代码目录。

**com.chat**：测试用例包目录。

**target**：这是构建输出目录，包含了项目构建后生成的所有文件。

**classes**：编译输出目录，包含了编译后的所有类文件和资源文件。

**generated-sources**：自动生成的源代码目录，包含了由IDE或构建工具生成的源代码。

## 5.1.2前端项目结构

项目根目录

-   **chat-online-vue** 或 **vue-my-first-demo**（项目名称）:
    -   代表了整个项目的名称。

依赖库目录

-   **node_modules**:
    -   包含项目依赖的所有外部库和模块。通常通过npm或yarn进行安装。

静态资源目录

-   **public**:
    -   存放静态资源，如favicon.ico、robots.txt等。这些文件将直接复制到最终构建的目标目录中，不会被Webpack处理。

源代码目录

-   **src**:
    -   项目的源代码都放在这个目录下。

资源目录

-   **assets**:
    -   存放项目中使用的媒体资源，如图片、字体等。

组件目录

-   **components**:
    -   存放Vue组件的实现文件，如SearchFriends.vue、test.vue等。

视图目录

-   **views**:
    -   存放与页面视图相关的Vue组件，如AddFriend.vue、AddGroup.vue等。

入口文件

-   **App.vue**:
    -   Vue应用的主组件。

主入口文件

-   **main.js**:
    -   项目的入口文件，通常用于初始化Vue实例和配置路由等。

其他文件

-   **registerServiceWorker.js**:
    -   用于注册ServiceWorker的脚本文件，用于实现离线缓存等功能。
    -   **router.ts**:
        -   Vue Router的配置文件，用于定义和管理项目的路由。
    -   **store.js**:
        -   Vuex的状态管理文件，用于管理应用的全局状态。
    -   **websocket.js**:
        -   用于管理WebSocket连接的脚本文件，实现实时通信功能。

配置文件

-   **.browserslistrc**:
    -   用于配置Babel和Autoprefixer等工具的浏览器兼容性设置。
-   **.gitignore**:
    -   指定在版本控制中需要忽略的文件和目录。
-   **babel.config.js**:
    -   Babel的配置文件，用于设置项目中的ES6+语法转译规则。
-   **jsconfig.json**:
    -   JavaScript的配置文件，用于定义项目的类型检查、路径别名等设置。
-   **package.json**:
    -   项目的清单文件，包含项目的元数据、依赖关系、脚本命令等。
-   **package-lock.json**:
    -   锁定项目的依赖版本，确保团队成员之间的依赖一致性。
-   **README.md**:
    -   项目的说明文档，通常包含项目的介绍、安装方法、使用指南等信息。
-   **vue.config.js**:
    -   Vue CLI项目的配置文件，用于自定义构建配置和扩展Webpack选项。

## 5.2 前端模板语法

\<div class="hello"\>

\<h3\>模板语法\</h3\>

\<p\>{{ message+" hhhh" }}\</p\>

\<!-- 双花括号只能显示文本，但支持JS表达式（不是语句！） --\>

\<div v-html="innerHTML"\>\</div\>

\<!-- v-html可以让文本变为HTML --\>

\<div v-bind:id="dynamicID"\>\</div\>

\<!-- v-bind（简写为冒号）可以让属性值动态变化 id值变为了“dID”--\>

\<div :id="dynamicID2"\>\</div\>

\<button @click="increment"\>Count is: {{ count }}\</button\>

\</div\>

# 六、系统架构设计

### 6.1. 系统整体架构

**客户端-服务器（C/S）架构**

-   **客户端**：负责用户交互和界面展示，提供注册、登录、好友管理、聊天等功能的用户界面。
-   **服务器**：负责处理业务逻辑和数据存储，包括用户认证、消息路由、数据持久化等功能。

**系统组成**

-   **前端**：用户交互界面，响应用户操作并展示数据。
-   **后端**：业务逻辑处理和数据存储，通过API接口与前端通信。
-   **数据库**：存储用户信息、好友关系、聊天记录等数据。

### 6.2. 前端设计

**响应式布局**

-   使用CSS3的媒体查询（Media Queries）和流式布局（Fluid Layouts）技术，实现响应式页面设计。
-   确保页面在不同设备（如手机、平板、桌面电脑）上都能良好地展示和交互。

**技术栈**

-   HTML：构建页面结构。
-   CSS：美化页面样式。
-   JavaScript：实现页面交互逻辑。

**前端框架**

-   Vue.js：用于构建单页面应用（SPA），提供组件化开发、数据驱动视图等特性。
-   Vue Router：实现前端路由管理，支持用户在不同页面间导航。
-   Vuex：用于管理应用中的状态，实现跨组件数据共享。

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/0d946fbdcb9b6b755bad8c6912282055.png)

### 6.3. 后端设计

**微服务架构**

-   将系统拆分为多个独立的服务模块，如用户服务、聊天服务、文件服务等。
-   每个服务模块独立部署、独立扩展，降低模块间耦合度，提高系统的可扩展性和可维护性。

**技术选型**

-   Java：作为后端开发语言，提供强大的编程能力和丰富的生态支持。
-   Spring Boot：快速搭建项目，提供自动配置、内嵌容器等功能，简化开发流程。

![Spring Boot的优点](https://github.com/xuanhyh/ChatOnline/raw/master/media/d3b6e158cd1ff1b07c5174b1a68cb9dc.png)

**消息队列**

-   用于解耦系统模块、削峰填谷、提高系统吞吐量。
-   JWT：保障系统和个人隐私安全

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/14342ab943270656c0716820cc4536e8.png)

**WebSocket**
- 原理图
![](https://github.com/xuanhyh/ChatOnline/raw/master/media/websocket_image.png)

-   WebSocket协议：这是一种基于TCP的协议，旨在实现客户端与服务器间的实时通信。通过发起HTTP/HTTPS请求进行握手，建立TCP连接后，允许服务器与客户端随时相互推送信息，其API调用简单，通过创建Socket实例和事件方法（如onopen、onclose、onmessage）来管理连接和数据交换。





### 6.4. 数据库设计

在线聊天系统需要实现用户注册、登录、添加好友、发起聊天、接收消息等功能。为了满足这些功能需求，我们需要设计一个能够存储用户信息、好友关系、聊天记录等数据的数据库。本项目选择MySQL作为关系型数据库，存储用户信息、好友关系、聊天记录等数据。

**E-R图**
![](https://github.com/xuanhyh/ChatOnline/raw/master/media/er_image.png)

**表结构设计**

-   **用户表**：存储用户ID、用户名、密码（加密后）、邮箱、手机号等基本信息。



-   **好友关系表**：存储用户ID、好友ID、关系状态（如已添加、未添加、已删除）等信息。



-   **聊天记录表**：存储消息ID、发送者ID、接收者ID、消息内容、发送时间等信息。



-   **邮件表**：存储要发送到的邮箱，验证码，发送时间。



-   **好友申请表**：存储好友申请的发送方和接收方，同意情况，发送时间。



-   **群组表**：存储群组名，创建时间等。



-   **群组成员表**：存储群组的成员信息。


-   **群组申请表**：存储申请用户id，接收群组id，群组创建时间等信息。



-   **群组未读信息表**：存储群组未读消息数。



-   **好友关系表**：存储用户id，好友id共同群聊id等。



-   **好友分组表**：存储用户的好友分组情况，分组id。


-   **用户印象表**：存储用户id，对其他好友的印象，好友的id和名字。



**索引优化**

为了提高查询性能，我们需要为经常查询的字段建立索引。以下是针对表结构的索引优化方式：

1.用户表（Users）

在username字段上建立唯一索引，以确保用户名的唯一性并提高查询速度。

在email和phone字段上建立普通索引，以便通过邮箱或手机号快速查询用户信息。

2.好友关系表（Friendships）

在user_id和friend_id字段上建立复合索引，以加速根据用户ID或好友ID查询好友关系信息的速度。

在status字段上建立普通索引，以便根据关系状态筛选好友关系信息。

3.聊天记录表（ChatRecords）

在sender_id和receiver_id字段上建立复合索引，以加速根据发送者或接收者ID查询聊天记录的速度。

在send_time字段

# 七、功能实现

## 7.1 登录与注册

用户通过浏览器访问服务器时，需要输入用户名和密码登录：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/50fdad2f9c51a835abf8de0975dd2936.png)

若该用户不存在则反映账号不存在：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/789297e291f9f445d50e5a1a8ba38d64.png)

注册账号：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/3936444343ab888bf7a24e96ab7bc429.png)

登录注册好的账号，进入聊天界面：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/39317a5689d39bebcfce715103a86bc2.png)

## 7.2 聊天管理（私聊，群聊）

互发消息私聊:

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/6140babd7f80c92f864b1a6370a4e0bf.png)

发送群聊消息：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/3af3ecb239230a68b39940e6ccfdf178.png)

选择下载该聊天记录到本地：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/5ad85dc4c1b1539689cc1854d6afe12c.png)

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/91957263b860ef2ce680fa0a8c89dfab.png)

## 7.3 好友管理（要求可分组管理）

对好友的分组：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/ac9a4d7d1fc8dee79340a622423c0c2a.png)

对好友的删除：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/5ad174d62866a840c0a4144a1c05f968.png)

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/3bd647f23b6d905d84843195ef6b8a89.png)

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/dd33ab74a917152b593d658bd27286a8.png)

使用用户名查找好友或群聊并申请添加：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/78789710005945a917d44abdd2b56e17.png)

对方收到好友申请：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/350f0008ac2a75ed41fece790068f8b9.png)

加为好友并聊天：

![](https://github.com/xuanhyh/ChatOnline/raw/master/media/8e5cd22e4b50c400ac1f9551a0684206.png)

# 八、评估和总结

在该JavaWeb在线聊天系统的项目开发过程中，我们积累了许多经验教训，并在项目质量和安全性等方面有了许多的认识和体会，我们一度也遇到过许多问题：

在项目质量方面，：

1.  **需求不明确**：在项目初期，对于用户需求的理解不够深入，导致在开发过程中频繁修改需求和功能，影响了项目的质量和进度。
2.  **代码质量**：在代码编写过程中，部分代码缺乏注释和文档，可读性较差，增加了后期维护和扩展的难度。
3.  **测试不充分**：在项目上线前，测试范围不够全面，未能充分覆盖所有功能和场景，导致部分缺陷被遗漏。

    **在安全性方面：**

4.  **用户认证和授权不严格**：在用户认证和授权方面存在漏洞，可能导致未授权访问和数据泄露。
5.  **输入验证不全面**：对于用户输入的验证不够全面，存在SQL注入、跨站脚本攻击（XSS）等安全风险。
6.  **加密措施不足**：对于敏感数据的加密措施不足，可能导致数据在传输和存储过程中被窃取或篡改。

**经过长时间的合作，我们攻克了大部分技术难题，大大改善了项目**项目表现。但仍有一定改善空间，如：

-   项目质量整体可控，但部分细节仍需优化。
-   用户体验基本满足需求，但在一些边缘场景下可能存在问题。
-   项目的稳定性和可靠性得到了保证，但仍有提升空间。

通过本次JavaWeb在线聊天系统的项目开发，我们深刻认识到在项目质量、性能和安全性方面需要注意的问题。在后续的项目开发中，我们将更加注重需求明确、代码质量、测试充分等方面的工作，同时加强数据库和缓存优化等方面的研究和实践。在安全性方面，我们将进一步完善用户认证和授权机制、加强输入验证和加密措施等措施，确保系统的稳定性和安全性。
