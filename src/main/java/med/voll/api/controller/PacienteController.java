package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.DadosAtualizacaoMedicos;
import med.voll.api.medico.DadosDetalhamentoMedicoAtualizado;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("paciente")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPaciente dadosCadastroPaciente, UriComponentsBuilder uriBuilder){
        var paciente = new Paciente(dadosCadastroPaciente);
        repository.save(paciente);
        //Criando a URL para enviar no retorno do HTTP
        var uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosListagemPacienteAtualizado(paciente));
    }

    @GetMapping
    public Page<DadosListagemPaciente> listaPaciente(@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoPacientes dadosAtualizacaoPacientes){
        var paciente = repository.getReferenceById(dadosAtualizacaoPacientes.id());
        paciente.atualizarInformacoes(dadosAtualizacaoPacientes);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        paciente.excluir();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemPacienteAtualizado(paciente));
    }

}
