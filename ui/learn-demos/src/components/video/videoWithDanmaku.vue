<template>
<!--  <video src="@/assets/video/test.mp4"  controls>-->
<!--  </video>-->
  <Danmaku
      ref="danmaku"
      class="danmaku-normal"
      :danmus="[]"
      :speeds="130"
      :channels="1"
      :loop="false"
      :top="20"
      :right="50"
      :is-suspend="true"
      :use-slot="true"
      :useSuspendSlot="true"
  >
<!--    这里对Danmaku的参数进行解释
    ref="danmaku"  弹幕组件的引用
    class="danmaku-normal"  弹幕组件的样式
    :danmus="[]"  弹幕数组，这里为空，后面通过手动add函数去实现延时加载每条弹幕
    :speeds="130"  弹幕速度，这里为130，即130ms发送一次弹幕
    :channels="1"  弹幕通道，这里为1，即单通道
    :loop="false"  是否循环播放，这里为false
    :top="20"  弹幕纵向间隔，这里为20px
    :right="50"  弹幕横向间隔，这里为50px
    :is-suspend="true"  是否显示悬停弹幕，这里为true
    :use-slot="true"  是否使用自定义弹幕内容，这里为true
    :useSuspendSlot="true"  是否使用自定义悬停弹幕内容，这里为true
-->
    <!--  自定义弹幕的显示内容  -->
    <template #dm="{ danmu,index }">
      <div>
        <div>{{ danmu.content }}</div>
      </div>
    </template>

    <!--  自定义弹幕悬停内容  这些内容会加在弹幕的后面 -->
    <template #suspend="{ danmu, index }" >
      <div class="danmu-suspend">
        <div>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          +1</div>
        <div> 👍</div>
      </div>
    </template>
  </Danmaku>


  <button @click="addDanmake">click-here</button>
</template>
<script setup>
import Danmaku from 'danmaku-vue'
import {ref, onMounted,defineProps} from "vue";


const danmus = ref([])
danmus.value = [
  {content: '弹幕大军来了', time: 0}
]
const dam =     [
    {content: 'danmu1', time: 3000},
        {content: 'danmu2', time: 5000},
        {content: 'danmu3', time: 7000},
        {content: 'danmu4', time: 1000},
        {content: 'danmu5', time: 9000}
    ]
dam.forEach(item=>{
  danmus.value.push(item)
})


//  对弹幕的操作函数
const danmaku = ref(null)
const addDanmake = () => {
  danmus.value.push({content: '弹幕',time:10000})
  danmaku.value.add({content: '弹幕',time:10000})
  console.log(danmaku.value)
}
const danmakuPause = () => {
  danmaku.value.pause()
}
const danmakuContinue = () => {
  danmaku.value.play()
}

// 因为Danmaku组件显示的弹幕不能做到和视频同步，所以手动延时加入到弹幕队列中，
// 延时为发送弹幕的时间与视频的开始播放时间的差值，记录差值于数据库，则刷新之后新的弹幕出现在对应的时间点。
 const  video = ref(null)
const delayDanmaku = ()=>{
    let startTime  =  videoBeginTime
    let playTime  = video.value.time
    let tempArr = [];
    danmus.value.filter((item,index)=>{
      return item.time <= playTime && !tempArr.includes(item.id)
    }).forEach((item,index)=>{
      tempArr.push(item.id)
      danmaku.value.add(item.content)
    })

    let intervalCheck = setInterval(()=>{
      if (tempArr.length == danmus.value.length){
        clearInterval(intervalCheck)
      }
    },100)
}

const visibleDanmus = ref([]);
onMounted(() => {
  let startTime = Date.now();
  let intervalId = setInterval(() => {
    const currentTime = Date.now() - startTime;
    const newDanmus = danmus.value.filter(danmu => danmu.time <= currentTime && !visibleDanmus.value.includes(danmu));
    newDanmus.forEach(danmu => {
      console.log(danmu)
      danmaku.value.add(danmu)
      visibleDanmus.value.push(danmu)
    });
    if (visibleDanmus.value.length === danmus.value.length) {
      clearInterval(intervalId);
    }
  }, 100);
})
</script>


<style scoped>
.danmaku-normal {
  width: 500px;
  height: 500px;
  border: 2px solid black;
  opacity: 1;
  position: relative;
}

.danmaku-large {

}

</style>


<!--
安装 danmaku
npm install danmaku-vue --save
-->

