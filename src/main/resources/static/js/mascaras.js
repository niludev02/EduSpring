document.addEventListener('DOMContentLoaded', function () {
    var titleCase = function (valor) {
        return valor
            .trim()
            .replace(/\s+/g, ' ')
            .toLowerCase()
            .replace(/\b([a-záàâãéèêíìîóòôõúùûç])/g, function (letra) {
                return letra.toUpperCase();
            });
    };

    document.querySelectorAll('[data-mask]').forEach(function (campo) {
        var mascara = campo.dataset.mask;

        campo.addEventListener('input', function () {
            if (mascara === 'matricula') {
                this.value = this.value
                    .replace(/[^A-Za-z0-9-]/g, '')
                    .toUpperCase()
                    .slice(0, 30);
            } else if (mascara === 'turma') {
                this.value = this.value
                    .replace(/[^A-Za-z0-9_-]/g, '')
                    .toUpperCase()
                    .slice(0, 50);
            } else if (mascara === 'idade') {
                this.value = this.value.replace(/\D/g, '').slice(0, 3);
            } else if (mascara === 'nota') {
                var valor = this.value.replace(',', '.').replace(/[^0-9.]/g, '');
                var partes = valor.split('.');
                if (partes.length > 2) valor = partes[0] + '.' + partes.slice(1).join('');
                if (valor.includes('.')) {
                    var partesNota = valor.split('.');
                    valor = partesNota[0].slice(0, 2) + '.' + partesNota[1].slice(0, 2);
                } else {
                    valor = valor.slice(0, 2);
                }
                var numero = Number(valor);
                if (valor !== '' && numero > 10) valor = '10';
                this.value = valor;
            } else if (mascara === 'nome') {
                this.value = this.value
                    .replace(/[0-9]/g, '')
                    .slice(0, this.dataset.maxlength ? Number(this.dataset.maxlength) : 150);
            } else if (mascara === 'texto') {
                this.value = this.value
                    .replace(/^\s+/, '')
                    .replace(/\s{2,}/g, ' ')
                    .slice(0, this.dataset.maxlength ? Number(this.dataset.maxlength) : 150);
            }
        });

        campo.addEventListener('blur', function () {
            if (mascara === 'nome') this.value = titleCase(this.value);
            if (mascara === 'texto') this.value = this.value.trim().replace(/\s+/g, ' ');
            if (mascara === 'idade') {
                var idade = Number(this.value);
                if (this.value !== '' && idade > 120) this.value = '120';
                if (this.value !== '' && idade < 12) this.value = '12';
            }
        });
    });

    document.querySelectorAll('.turma-checkbox').forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            var selecionadas = document.querySelectorAll('.turma-checkbox:checked');
            if (selecionadas.length > 2) {
                this.checked = false;
                alert('Um professor pode ser responsável por no máximo 2 turmas.');
            }
        });
    });
});
