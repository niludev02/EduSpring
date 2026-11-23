package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.AlunoForm;
import br.edu.fatecpg.eduspring.dto.AlunoRequest;
import br.edu.fatecpg.eduspring.dto.AlunoResponse;
import br.edu.fatecpg.eduspring.dto.DisciplinaForm;
import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.NotaForm;
import br.edu.fatecpg.eduspring.dto.ProfessorForm;
import br.edu.fatecpg.eduspring.dto.ProfessorRequest;
import br.edu.fatecpg.eduspring.dto.TurmaForm;
import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.service.AlunoService;
import br.edu.fatecpg.eduspring.service.DisciplinaService;
import br.edu.fatecpg.eduspring.service.ProfessorService;
import br.edu.fatecpg.eduspring.service.TurmaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final TurmaService turmas;
    private final DisciplinaService disciplinas;
    private final AlunoService alunos;
    private final ProfessorService professores;

    @GetMapping("/")
    public String raiz() { return "inicio"; }

    @GetMapping("/web/turmas")
    public String turmas(@RequestParam(required = false) Long editar, Model m) {
        m.addAttribute("turmas", turmas.listar());
        TurmaForm form = new TurmaForm();
        if (editar != null) {
            var turma = turmas.buscarPorId(editar);
            form.setIdentificador(turma.identificador());
            form.setProfessorId(turma.professorId());
        }
        m.addAttribute("form", form);
        m.addAttribute("editarId", editar);
        m.addAttribute("professoresDisponiveis", turmas.professoresDisponiveis(editar));
        return "turmas";
    }

    @PostMapping("/web/turmas")
    public String criarTurma(@Valid @ModelAttribute("form") TurmaForm form, BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                turmas.criar(new TurmaRequest(form.getIdentificador(), form.getProfessorId()));
                return "redirect:/web/turmas";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("turmas", turmas.listar());
        m.addAttribute("editarId", null);
        m.addAttribute("professoresDisponiveis", turmas.professoresDisponiveis(null));
        return "turmas";
    }

    @PostMapping("/web/turmas/{id}/editar")
    public String salvarEdicaoTurma(@PathVariable Long id, @Valid @ModelAttribute("form") TurmaForm form,
                                    BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                turmas.atualizar(id, new TurmaRequest(form.getIdentificador(), form.getProfessorId()));
                return "redirect:/web/turmas";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("turmas", turmas.listar());
        m.addAttribute("editarId", id);
        m.addAttribute("professoresDisponiveis", turmas.professoresDisponiveis(id));
        return "turmas";
    }

    @PostMapping("/web/turmas/{id}/remover")
    public String removerTurma(@PathVariable Long id, Model m) {
        try {
            turmas.remover(id);
            return "redirect:/web/turmas";
        } catch (NegocioException e) {
            m.addAttribute("erro", e.getMessage());
            m.addAttribute("turmas", turmas.listar());
            m.addAttribute("form", new TurmaForm());
            m.addAttribute("editarId", null);
            m.addAttribute("professoresDisponiveis", turmas.professoresDisponiveis(null));
            return "turmas";
        }
    }

    @GetMapping("/web/disciplinas")
    public String disciplinas(@RequestParam(required = false) Long editar, Model m) {
        m.addAttribute("disciplinas", disciplinas.listar());
        DisciplinaForm form = new DisciplinaForm();
        if (editar != null) {
            var disciplina = disciplinas.buscarPorId(editar);
            form.setNome(disciplina.nome());
        }
        m.addAttribute("form", form);
        m.addAttribute("editarId", editar);
        return "disciplinas";
    }

    @PostMapping("/web/disciplinas")
    public String criarDisciplina(@Valid @ModelAttribute("form") DisciplinaForm form, BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                disciplinas.criar(new DisciplinaRequest(form.getNome()));
                return "redirect:/web/disciplinas";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("disciplinas", disciplinas.listar());
        m.addAttribute("editarId", null);
        return "disciplinas";
    }

    @PostMapping("/web/disciplinas/{id}/editar")
    public String salvarEdicaoDisciplina(@PathVariable Long id, @Valid @ModelAttribute("form") DisciplinaForm form,
                                         BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                disciplinas.atualizar(id, new DisciplinaRequest(form.getNome()));
                return "redirect:/web/disciplinas";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("disciplinas", disciplinas.listar());
        m.addAttribute("editarId", id);
        return "disciplinas";
    }

    @PostMapping("/web/disciplinas/{id}/remover")
    public String removerDisciplina(@PathVariable Long id, Model m) {
        try {
            disciplinas.remover(id);
            return "redirect:/web/disciplinas";
        } catch (NegocioException e) {
            m.addAttribute("erro", e.getMessage());
            m.addAttribute("disciplinas", disciplinas.listar());
            m.addAttribute("form", new DisciplinaForm());
            m.addAttribute("editarId", null);
            return "disciplinas";
        }
    }

    @GetMapping("/web/alunos")
    public String alunos(@RequestParam(required = false) String nome,
                         @RequestParam(required = false) String turma,
                         @RequestParam(required = false) String ordem, Model m) {
        m.addAttribute("alunos", alunos.listar(nome, turma, ordem));
        m.addAttribute("turmas", turmas.listar());
        m.addAttribute("nome", nome);
        m.addAttribute("turma", turma);
        m.addAttribute("ordem", ordem);
        return "alunos";
    }

    @GetMapping("/web/alunos/novo")
    public String novoAluno(Model m) {
        m.addAttribute("form", new AlunoForm());
        return formAluno(null, m);
    }

    @PostMapping("/web/alunos/novo")
    public String salvarNovo(@Valid @ModelAttribute("form") AlunoForm form, BindingResult result, Model m) {
        return salvarAluno(null, form, result, m);
    }

    @GetMapping("/web/alunos/{id}/editar")
    public String editarAluno(@PathVariable Long id, Model m) {
        AlunoResponse a = alunos.buscarPorId(id);
        AlunoForm form = new AlunoForm();
        form.setMatricula(a.matricula());
        form.setNome(a.nome());
        form.setIdade(a.idade());
        form.setTurmaId(a.turmaId());
        m.addAttribute("form", form);
        return formAluno(id, m);
    }

    @PostMapping("/web/alunos/{id}/editar")
    public String salvarEdicao(@PathVariable Long id, @Valid @ModelAttribute("form") AlunoForm form,
                               BindingResult result, Model m) {
        return salvarAluno(id, form, result, m);
    }

    @PostMapping("/web/alunos/{id}/remover")
    public String removerAluno(@PathVariable Long id) {
        alunos.remover(id);
        return "redirect:/web/alunos";
    }

    @GetMapping("/web/alunos/{id}/boletim")
    public String boletim(@PathVariable Long id,
                          @RequestParam(required = false) Long disciplina,
                          Model m) {
        m.addAttribute("boletim", alunos.boletim(id));
        m.addAttribute("disciplinas", disciplinas.listar());
        NotaForm form = new NotaForm();
        if (disciplina != null) {
            form.setDisciplinaId(disciplina);
            alunos.boletim(id).notas().stream()
                    .filter(n -> n.disciplinaId().equals(disciplina))
                    .findFirst()
                    .ifPresent(n -> form.setValor(n.valor()));
        }
        m.addAttribute("form", form);
        m.addAttribute("disciplinaEdicao", disciplina);
        return "boletim";
    }

    @PostMapping("/web/alunos/{id}/notas")
    public String lancarNota(@PathVariable Long id, @Valid @ModelAttribute("form") NotaForm form,
                             BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                alunos.lancarNota(id, form.getDisciplinaId(), form.getValor());
                return "redirect:/web/alunos/" + id + "/boletim";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("boletim", alunos.boletim(id));
        m.addAttribute("disciplinas", disciplinas.listar());
        m.addAttribute("disciplinaEdicao", form.getDisciplinaId());
        return "boletim";
    }

    @PostMapping("/web/alunos/{id}/notas/{disciplinaId}/remover")
    public String removerNota(@PathVariable Long id, @PathVariable Long disciplinaId) {
        alunos.removerNota(id, disciplinaId);
        return "redirect:/web/alunos/" + id + "/boletim";
    }

    @GetMapping("/web/professores")
    public String professores(@RequestParam(required = false) Long editar, Model m) {
        ProfessorForm form = new ProfessorForm();
        if (editar != null) {
            var professor = professores.buscarPorId(editar);
            form.setNome(professor.nome());
            form.setDisciplinaId(professor.disciplinaId());
            form.setTurmaIds(professor.turmaIds());
        }
        m.addAttribute("form", form);
        m.addAttribute("editarId", editar);
        m.addAttribute("professores", professores.listar());
        m.addAttribute("disciplinasDisponiveis", professores.disciplinasDisponiveis(editar));
        m.addAttribute("turmasDisponiveis", professores.turmasDisponiveis(editar));
        return "professores";
    }

    @PostMapping("/web/professores")
    public String criarProfessor(@Valid @ModelAttribute("form") ProfessorForm form,
                                 BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                professores.criar(new ProfessorRequest(
                        form.getNome(), form.getDisciplinaId(), form.getTurmaIds()));
                return "redirect:/web/professores";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        preencherProfessorForm(form, null, m);
        return "professores";
    }

    @PostMapping("/web/professores/{id}/editar")
    public String salvarEdicaoProfessor(@PathVariable Long id,
                                        @Valid @ModelAttribute("form") ProfessorForm form,
                                        BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                professores.atualizar(id, new ProfessorRequest(
                        form.getNome(), form.getDisciplinaId(), form.getTurmaIds()));
                return "redirect:/web/professores";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        preencherProfessorForm(form, id, m);
        return "professores";
    }

    @PostMapping("/web/professores/{id}/remover")
    public String removerProfessor(@PathVariable Long id) {
        professores.remover(id);
        return "redirect:/web/professores";
    }

    @GetMapping("/web/resumo")
    public String resumo() { return "resumo"; }

    @ExceptionHandler(NegocioException.class)
    public String erro(NegocioException e, Model m) {
        m.addAttribute("mensagem", e.getMessage());
        return "erro";
    }

    private String salvarAluno(Long id, AlunoForm form, BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                var req = new AlunoRequest(form.getMatricula(), form.getNome(), form.getIdade(), form.getTurmaId());
                if (id == null) alunos.criar(req); else alunos.atualizar(id, req);
                return "redirect:/web/alunos";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        return formAluno(id, m);
    }

    private String formAluno(Long id, Model m) {
        m.addAttribute("turmas", turmas.listar());
        m.addAttribute("titulo", id == null ? "Novo aluno" : "Editar aluno");
        m.addAttribute("acao", id == null ? "/web/alunos/novo" : "/web/alunos/" + id + "/editar");
        return "aluno-form";
    }

    private void preencherProfessorForm(ProfessorForm form, Long editarId, Model m) {
        m.addAttribute("form", form);
        m.addAttribute("editarId", editarId);
        m.addAttribute("professores", professores.listar());
        m.addAttribute("disciplinasDisponiveis", professores.disciplinasDisponiveis(editarId));
        m.addAttribute("turmasDisponiveis", professores.turmasDisponiveis(editarId));
    }
}
