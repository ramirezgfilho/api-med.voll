package med.voll.api.paciente;

import med.voll.api.endereco.Endereco;

public record DadosListagemPacienteAtualizado(Long id, String nome, String email, String telefone, String cpf, Endereco endereco) {

    public DadosListagemPacienteAtualizado(Paciente paciente){
        this(paciente.getId(),paciente.getNome(), paciente.getEmail(),paciente.getTelefone(), paciente.getCpf(), paciente.getEndereco());
    }

}
