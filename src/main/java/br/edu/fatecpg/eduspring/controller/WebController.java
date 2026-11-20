package br.edu.fatecpg.eduspring.controller;

import br.edu.fatecpg.eduspring.dto.AlunoForm;
import br.edu.fatecpg.eduspring.dto.AlunoRequest;
import br.edu.fatecpg.eduspring.dto.AlunoResponse;
import br.edu.fatecpg.eduspring.dto.DisciplinaForm;
import br.edu.fatecpg.eduspring.dto.DisciplinaRequest;
import br.edu.fatecpg.eduspring.dto.NotaForm;
import br.edu.fatecpg.eduspring.dto.TurmaForm;
import br.edu.fatecpg.eduspring.dto.TurmaRequest;
import br.edu.fatecpg.eduspring.exception.NegocioException;
import br.edu.fatecpg.eduspring.service.AlunoService;
import br.edu.fatecpg.eduspring.service.DisciplinaService;
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

    @GetMapping("/")
    public String raiz() { return "inicio"; }

    @GetMapping("/web/turmas")
    public String turmas(Model m) {
        m.addAttribute("turmas", turmas.listar());
        m.addAttribute("form", new TurmaForm());
        return "turmas";
    }

    @PostMapping("/web/turmas")
    public String criarTurma(@Valid @ModelAttribute("form") TurmaForm form, BindingResult result, Model m) {
        if (!result.hasErrors()) {
            try {
                turmas.criar(new TurmaRequest(form.getIdentificador()));
                return "redirect:/web/turmas";
            } catch (NegocioException e) {
                m.addAttribute("erro", e.getMessage());
            }
        }
        m.addAttribute("turmas", turmas.listar());
        return "turmas";
    }

    @GetMapping("/web/disciplinas")
    public String disciplinas(Model m) {
        m.addAttribute("disciplinas", disciplinas.listar());
        m.addAttribute("form", new DisciplinaForm());
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
        return "disciplinas";
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
    public String boletim(@PathVariable Long id, Model m) {
        m.addAttribute("boletim", alunos.boletim(id));
        m.addAttribute("disciplinas", disciplinas.listar());
        m.addAttribute("form", new NotaForm());
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
        return "boletim";
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
}
