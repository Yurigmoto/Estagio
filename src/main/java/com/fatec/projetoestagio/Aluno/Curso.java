package com.fatec.projetoestagio.Aluno;

import static com.fatec.projetoestagio.Aluno.Periodo.*;

public enum Curso {
    ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS,
    COMERCIO_EXTERIOR,
    DESENVOLVIMENTO_DE_PRODUTOS_PLASTICOS,
    DESENVOLVIMENTO_DE_SOFTWARE_MULTIPLATAFORMA,
    GESTAO_DE_RECURSOS_HUMANOS,
    GESTAO_EMPRESARIAL,
    LOGISTICA,
    POLIMEROS;

    // Quais períodos cada curso oferece. É esta regra que vale de verdade (o banco só recebe
    // o que passar por aqui). Mantenha igual à função periodoPermitido() do formulario.js.
    public boolean aceita(Periodo periodo, int semestre) {
        return switch (this) {
            case ANALISE_E_DESENVOLVIMENTO_DE_SISTEMAS -> periodo == VESPERTINO || periodo == NOTURNO;
            case COMERCIO_EXTERIOR -> periodo == MATUTINO
                    || (periodo == VESPERTINO && semestre <= 4)   // 1º ao 4º semestre: Tarde
                    || (periodo == NOTURNO && semestre >= 5);     // 5º e 6º semestre: Noite
            case DESENVOLVIMENTO_DE_SOFTWARE_MULTIPLATAFORMA, GESTAO_DE_RECURSOS_HUMANOS -> periodo == MATUTINO;
            case GESTAO_EMPRESARIAL -> periodo == MATUTINO || periodo == EAD;
            case LOGISTICA -> periodo == MATUTINO || periodo == NOTURNO;
            case POLIMEROS, DESENVOLVIMENTO_DE_PRODUTOS_PLASTICOS -> periodo == NOTURNO;
        };
    }
}
