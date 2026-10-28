// Quando o CEP tem 8 dígitos, consulta a API pública ViaCEP e preenche Cidade e Estado (UF).
// Se a consulta falhar (CEP inexistente ou sem internet), libera os dois campos para digitar à mão.
const campoCep = document.getElementById('cep');
const campoCidade = document.getElementById('cidade');
const campoEstado = document.getElementById('estado');
const saida = document.getElementById('cidade-cep');
let ultimoCep = '';

function definirLocal(cidade, estado, editavel) {
    campoCidade.value = cidade;
    campoEstado.value = estado;
    campoCidade.readOnly = !editavel;
    campoEstado.readOnly = !editavel;
}

async function mostrarCidade() {
    const cep = campoCep.value.replace(/\D/g, '');
    if (cep.length !== 8) {
        ultimoCep = '';
        saida.textContent = '';
        definirLocal('', '', false);
        return;
    }
    if (cep === ultimoCep) return;   // já consultado
    ultimoCep = cep;
    saida.textContent = 'Buscando cidade...';
    definirLocal('', '', false);
    try {
        const resposta = await fetch('https://viacep.com.br/ws/' + cep + '/json/');
        const dados = await resposta.json();
        if (cep !== ultimoCep) return;   // a pessoa já mudou o CEP enquanto buscava
        if (dados.erro) {
            saida.textContent = 'CEP não encontrado. Digite a cidade e o estado.';
            definirLocal('', '', true);
        } else {
            saida.textContent = '';
            definirLocal(dados.localidade, dados.uf, false);
        }
    } catch (erro) {
        if (cep === ultimoCep) {
            saida.textContent = 'Não foi possível consultar o CEP agora. Digite a cidade e o estado.';
            definirLocal('', '', true);
        }
    }
}

campoCep.addEventListener('input', mostrarCidade);
campoCep.addEventListener('blur', mostrarCidade);
// O reset do navegador restaura os valores só depois do evento, por isso o setTimeout.
campoCep.form.addEventListener('reset', () => { ultimoCep = ''; setTimeout(mostrarCidade, 0); });
mostrarCidade();   // quando o formulário volta com erro, o CEP já vem preenchido
