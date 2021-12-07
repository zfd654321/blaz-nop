// 请从开者中心获取 "Client-end (Target Recognition) URL"，格式如：https://af0c1ca3b41857bd8d6b44d480601c74.cn1.crs.easyar.com:8443/search
const app = new App('https://cn1-crs.easyar.com:8443/search');
// 如果使用自定义方法获取token
// app.setToken({
//     'crsAppId': '', // 云别库的CRS AppId
//     'token': '' // APIKey+APISecret生成token
// });
// 如果使用EasyAR提供的集成环境
app.useEasyAr();
// 识别成功后的回调
app.callback = (msg) => {
    console.info(msg);
    console.info(msg.target.name)
    let name = msg.target.name
    const setting = {
        video: 'video/' + name + '.mp4',
    };
    // 可以将 setting 作为meta上传到EasyAR的云识别，使用方法如下:
    // const setting = JSON.parse(window.atob(msg.target.meta));
    playVideo(setting);
};

app.savelog = (status, targetId, name) => {
    fetch('/wxs/easyar/savelog?name=' + name + "&targetId=" + targetId + "&status=" + status + "&type=calendar", {
        method: 'POST'
    }).then(res => res.json()).then(data => {
        console.log(data)
    }).catch(err => {
        console.info(err);
        alert(`服务器忙\n${err}`);
    })
};
// 在手机上可能不会自动播放
function playVideo(setting) {
    let video = document.querySelector('#easyARVideo');
    if (video === null) {
        video = document.createElement('video');
        video.setAttribute('id', 'easyARVideo');
        video.setAttribute('controls', 'controls');
        video.setAttribute('playsinline', 'playsinline');
        video.setAttribute('preload', 'preload');
        video.setAttribute('style', 'position:absolute;top:100px;left:0;width:100%;z-index:99');
        document.querySelector('#easyAR').appendChild(video);
    }
    video.style.display = "block"
    video.setAttribute('src', setting.video);
    video.play().then(() => {}).catch((err) => {
        // 需要使用点击事件播放。
        console.info('播放视频失败');
        console.info(err);
    });
}
//# sourceMappingURL=app.js.map