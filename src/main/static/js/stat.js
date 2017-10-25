var stat = new Vue({
    el: '#stat',
    data: {
        json: []
        },
    methods: {
        getUrlInfo: function () {
            axios.get('/stat/json', {
                params: {
                    short: getParam("short")
                }
            })
                .then(function (response) {
                    stat.json = response.data;
                    console.log(stat.json);
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }, mounted() {
        this.getUrlInfo();
    }
})


function getParam(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}