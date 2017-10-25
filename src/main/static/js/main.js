var shorter = new Vue({
    el: '#shorter',
    data: {
        originalUrl: '',
        shortUrl: ''
    },
    methods: {
        getShortUrl: function () {
            axios.get('/new', {
                params: {
                    original: this.originalUrl
                }
            })
                .then(function (response) {
                    list.updateList();
                    console.log(response);
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
})

var list = new Vue({
    el: '#list',
    data: {
        links: [],
        page: 0,
        count: 10,
        hasPrevious: false,
        hasNext: true,
        numPages: 10
    },
    methods: {
        updateList: function () {
            axios.get('/all', {
                params: {
                    min: this.page * 10,
                    count: this.count
                }
            })
                .then(function (response) {
                    list.links = response.data
                })
                .catch(function (error) {
                    console.log(error);
                });
            axios.get('/allpages')
                .then(function (response) {
                    list.numPages = response.data
                    console.log()
                })
                .catch(function (error) {
                    console.log(error);
                });
            if (list.numPages > list.page + 1) {
                list.hasNext = true;
            }
            else {
                list.hasNext = false;
            }
        },
        previousPage: function () {
            list.page = list.page - 1;
            list.updateList();
            if (list.page <= 0) {
                list.hasPrevious = false;
                list.hasNext = true;
            }
        },
        nextPage: function () {

            axios.get('/allpages')
                .then(function (response) {
                    list.numPages = response.data
                    console.log()
                })
                .catch(function (error) {
                    console.log(error);
                });
            if (list.numPages > list.page + 1) {
                list.hasNext = true;
            }
            else {
                list.hasNext = false;
            }
            list.page = list.page + 1;
            list.updateList();
            list.hasPrevious = true;
        },
        remove: function (index) {
            axios({method: "delete", url: "/delete", params:{short: list.links[index].short}})
                .then(function (response) {
                    list.updateList();
                })
                .catch(function (error) {
                    console.log(error);
                });
        },
        getShortUrl: function (index) {
            return "http://localhost:8080/" + list.links[index].short;
        },
        getOriginalUrl: function (index) {
            return list.links[index].original;
        },
        getShortUrlStat: function (index) {
            return "http://localhost:8080/stat?short=" + list.links[index].short;
        }
    }, mounted() {
        this.updateList();
        axios.get('/allpages')
            .then(function (response) {
                list.numPages = response.data
                console.log()
            })
            .catch(function (error) {
                console.log(error);
            });
        if (list.numPages > list.page + 1) {
            list.hasNext = true;
        }
    }
})

