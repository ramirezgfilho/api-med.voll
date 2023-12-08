package med.voll.api.controller;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("consultas")
@SecurityRequirement(name ="bearer-key")
public class ConsultaController {

    @Autowired
    private AgendaDeConsultas agendaDeConsultas;

    @Autowired
    private ConsultaRepository consultaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity agendar(@RequestBody @Valid DadosAgendamentoConsulta dados){
        var dto = agendaDeConsultas.agendar(dados);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity cancelarConsulta(@PathVariable Long id, @RequestBody @Valid DadosDetalhamentoCancelamentoConsulta dadosDetalhamentoCancelamentoConsulta){

        if (dadosDetalhamentoCancelamentoConsulta.motivoAnulacao() == ""){
            throw new ValidacaoException("O motivo do cancelamento deve ser informado");
        }
        var dto = consultaRepository.getReferenceById(id);

        var dataConsulta = dto.getData();
        var agora = LocalDateTime.now();
        var diferencaEmMinutos = Duration.between(agora, dataConsulta).toHours();

        if (diferencaEmMinutos < 24){
            throw new ValidacaoException("A consulta não pode ser cancelada com menos de 24 horas de antecedencia.");
        }

        dto.excluir();

        return ResponseEntity.noContent().build();
    }

}
