package br.com.ks.teste.uds.ws;

import br.com.ks.teste.uds.connection.UDSConnection;
import br.com.ks.teste.uds.exception.CustomException;
import br.com.ks.teste.uds.model.Pessoa;
import br.com.ks.teste.uds.view.MainView;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Classe de serviço para persistência e consulta de Pessoa na base de dados
 *
 * @author jonny
 *
 */
@Path("/pessoa")
public class PessoaService {

    /**
     * Método responsável por salvar na base de dados o objeto do tipo Pessoa
     *
     * @param pessoa
     * @return Mensagem de retorno da operação.
     * @throws Exception
     */
    @POST
    @Path("new")
    @Consumes({MediaType.APPLICATION_JSON+ ";charset=utf-8"})
    public Response save(Pessoa pessoa) throws Exception {
        try {
            String messageResult;
            if (pessoa != null) {
                MainView.addLog("Save Pessoa: " + pessoa.toString());
                validate(pessoa);

                Connection conn = UDSConnection.getInstance().getConnection();
                PreparedStatement insertPessoa = conn.prepareStatement("insert into pessoa values(nextval('pessoa_id_seq'), ?, ?)");
                insertPessoa.setString(1, pessoa.getNome());
                insertPessoa.setString(2, pessoa.getEmail());
                int result = insertPessoa.executeUpdate();
                if (result == 1) {
                    messageResult = "Pessoa salva com sucesso.";
                } else {
                    messageResult = "Nenhuma pessoa com o identificador " + pessoa.getId() + " foi encontrada.";
                }
            } else {
                messageResult = "Identificador da pessoa é obrigatório.";
            }
            return Response.status(Response.Status.OK).entity(messageResult).build();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaService.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception("Erro ao salvar a pessoa");
        }

    }

    /**
     * Método responsável por listar na base de dados o objeto do tipo Pessoa
     *
     * @return lista de objetos do tipo pessoa.
     */
    @GET
    @Path("list")
    public Response list() {

        List<Pessoa> pessoas = new ArrayList<>();
        MainView.addLog("Listagem de pessoas");
        try {
            Connection conn = UDSConnection.getInstance().getConnection();
            PreparedStatement selectPessoa = conn.prepareStatement("select * from pessoa");
            ResultSet resultSetSelect = selectPessoa.executeQuery();
            while (resultSetSelect.next()) {
                pessoas.add(new Pessoa(resultSetSelect.getLong("id"), resultSetSelect.getString("nome"),
                        resultSetSelect.getString("email"), resultSetSelect.getDate("data_cadastro")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        MainView.addLog(pessoas.toString());
        return Response.status(Response.Status.OK).entity(new Gson().toJson(pessoas)).build();
    }

    /**
     * Método responsável por atualizar na base de dados o objeto do tipo Pessoa
     *
     * @param pessoa
     * @return Mensagem de retorno da operação
     * @throws Exception
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON+ ";charset=utf-8")
    public Response update(Pessoa pessoa) throws Exception {
        try {
            String messageResult;
            if (pessoa != null && pessoa.getId() != null) {
                MainView.addLog("Update Pessoa: " + pessoa.toString());
                validate(pessoa);

                Connection conn = UDSConnection.getInstance().getConnection();
                PreparedStatement updatePessoa = conn.prepareStatement("update pessoa set nome = ?, email = ? where id = ?");

                updatePessoa.setString(1, pessoa.getNome());
                if (pessoa.getEmail() != null) {
                    updatePessoa.setString(2, pessoa.getEmail());
                } else {
                    updatePessoa.setNull(2, Types.VARCHAR);
                }
                updatePessoa.setLong(3, pessoa.getId());

                int result = updatePessoa.executeUpdate();
                if (result == 1) {
                    messageResult = "Pessoa atualizada com sucesso.";
                } else {
                    messageResult = "Nenhuma pessoa com o identificador " + pessoa.getId() + " foi encontrada.";
                }
            } else {
                messageResult = "Identificador da pessoa é obrigatório.";
            }
            return Response.status(Response.Status.OK).entity(messageResult).build();
        } catch (CustomException ex) {
            return Response.status(Response.Status.OK).entity(ex.getMessage()).build();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar a pessoa.").build();
        }
    }

    /**
     * Método responsável por deletar na base de dados o objeto do tipo Pessoa
     *
     * @param id
     * @return Messagem se a pessoa foi excluída ou não
     * @throws Exception
     */
    @POST
    @Path("remove/{id}")
    public Response remove(@PathParam("id") Long id) throws Exception {
        try {
            MainView.addLog("Remove Pessoa: " + id);
            String messageResult;
            if (id != null) {
                Connection conn = UDSConnection.getInstance().getConnection();
                PreparedStatement deletePessoa = conn.prepareStatement("delete from pessoa where id = ?");
                deletePessoa.setLong(1, id);
                int result = deletePessoa.executeUpdate();
                if (result == 1) {
                    messageResult = "Pessoa removida com sucesso.";
                } else {
                    messageResult = "Nenhuma pessoa com o identificador " + id + " foi encontrada.";
                }
            } else {
                messageResult = "Identificador da pessoa é obrigatório.";
            }
            return Response.status(Response.Status.OK).entity(messageResult).build();
        } catch (SQLException ex) {
            Logger.getLogger(PessoaService.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao remover a pessoa.").build();
        }
    }

    private void validate(Pessoa pessoa) throws CustomException {
        if (pessoa != null) {
            if (pessoa.getNome() == null) {
                throw new CustomException("Nome da pessoa é obrigatório");
            } else if (pessoa.getNome().length() > 80) {
                throw new CustomException("Nome da pessoa é muito grande. Máximo 80 caracteres");
            }
            if (pessoa.getEmail() != null && pessoa.getEmail().length() > 80) {
                throw new CustomException("Email da pessoa é muito grande. Máximo 80 caracteres");
            }
        } else {
            throw new CustomException("Não foi possível validar a pessoa.");
        }
    }
}
