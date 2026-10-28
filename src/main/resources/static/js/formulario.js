// ---------- Data de nascimento: coloca as barras sozinho (dd/mm/aaaa) ----------
const campoNascimento = document.getElementById('nascimento');

function formatarData(digitos, apagando) {
    let texto = digitos.slice(0, 2);
    // a barra aparece assim que o bloco fecha, mas não volta quando a pessoa está apagando
    if (digitos.length > 2 || (digitos.length === 2 && !apagando)) texto += '/' + digitos.slice(2, 4);
    if (digitos.length > 4 || (digitos.length === 4 && !apagando)) texto += '/' + digitos.slice(4, 8);
    return texto;
}

campoNascimento.addEventListener('input', (evento) => {
    const apagando = (evento.inputType || '').startsWith('delete');
    const digitos = campoNascimento.value.replace(/\D/g, '').slice(0, 8);
    campoNascimento.value = formatarData(digitos, apagando);
});

// ---------- Período: só libera o que o curso oferece ----------
// ATENÇÃO: esta regra precisa ser igual à de Curso.aceita() no Java (o servidor é quem decide de verdade).
function periodoPermitido(curso, periodo, semestre) {
    switch (curso) {
        case 'ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS':
            return periodo === 'VESPERTINO' || periodo === 'NOTURNO';
        case 'COMERCIO_EXTERIOR':
            // sem semestre escolhido ainda, libera tudo; depois o semestre filtra
            return periodo === 'MATUTINO'
                || (periodo === 'VESPERTINO' && (!semestre || semestre <= 4))
                || (periodo === 'NOTURNO' && (!semestre || semestre >= 5));
        case 'DESENVOLVIMENTO_DE_SOFTWARE_MULTIPLATAFORMA':
        case 'GESTAO_DE_RECURSOS_HUMANOS':
            return periodo === 'MATUTINO';
        case 'GESTAO_EMPRESARIAL':
            return periodo === 'MATUTINO' || periodo === 'EAD';
        case 'LOGISTICA':
            return periodo === 'MATUTINO' || periodo === 'NOTURNO';
        case 'DESENVOLVIMENTO_DE_PRODUTOS_PLASTICOS':
        case 'POLIMEROS':
            return periodo === 'NOTURNO';
        default:
            return true;   // Produtos Plásticos: regra ainda não definida
    }
}

const campoCurso = document.getElementById('curso');
const campoSemestre = document.getElementById('semestre');
const radiosPeriodo = document.querySelectorAll('input[name="periodo"]');
const dicaPeriodo = document.getElementById('dica-periodo');

function atualizarPeriodos() {
    const curso = campoCurso.value;
    const semestre = parseInt(campoSemestre.value, 10) || null;
    radiosPeriodo.forEach((radio) => {
        const liberado = curso !== '' && periodoPermitido(curso, radio.value, semestre);
        radio.disabled = !liberado;                 // desabilitado não é enviado com o formulário
        radio.closest('label').hidden = !liberado;  // e some da tela: só aparece o que o curso oferece
        if (!liberado && radio.checked) radio.checked = false;   // escolha que deixou de valer
    });
    if (curso === '') {
        dicaPeriodo.textContent = 'Selecione o curso para ver os períodos disponíveis.';
    } else if (curso === 'COMERCIO_EXTERIOR' && !semestre) {
        dicaPeriodo.textContent = 'Escolha também o semestre: no Comércio Exterior o período depende dele.';
    } else {
        dicaPeriodo.textContent = '';
    }
}

campoCurso.addEventListener('change', atualizarPeriodos);
campoSemestre.addEventListener('change', atualizarPeriodos);
campoCurso.form.addEventListener('reset', () => setTimeout(atualizarPeriodos, 0));
atualizarPeriodos();   // também roda ao abrir, para o caso do formulário voltar com erro
