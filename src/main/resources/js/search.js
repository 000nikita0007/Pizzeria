document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('#elastic').oninput = function () {
        let val = this.value.toUpperCase().trim();
        let elasticItems = document.querySelectorAll('.elastic tr')
        if (val !== '') {
            elasticItems.forEach(function (elem) {
                if (elem.innerText.toUpperCase().search(val) === -1) {
                    elem.classList.add('hide');
                } else {
                    elem.classList.remove('hide');
                }
            });
        } else {
            elasticItems.forEach(function (elem) {
                elem.classList.remove('hide');
            });
        }
    }
});