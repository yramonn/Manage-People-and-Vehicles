package com.sovos.web;

import com.sovos.model.dto.PessoaDTO;
import com.sovos.model.response.Pessoa;
import com.sovos.service.PessoaService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/PessoaServlet")
public class PessoaServlet extends HttpServlet {

    public PessoaServlet(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameterMap().containsKey("cmd")){
            String cmd = request.getParameter("cmd").trim();
            switch (cmd) {
                case "con":
                    cmdConsult(request, response);
                    break;
                case "new":
                    cmdNewPessoa(request, response);
                    break;
                case "add":
                    cmdAddNewPessoa(request, response);
                    break;
                case "alter":
                    cmdAlterPessoa(request, response);
                    break;
                case "del":
                    cmdDeletePessoa(request, response);
                    break;
            }
        }else{
            cmdAllPessoa(request, response);
        }

    }

    private void cmdDeletePessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PessoaService service = new PessoaService(loadPessoaFromRequest(request));
        boolean exec = service.delete();
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Usuário foi deletado" : "Erro ao tentar deletar o usuario");
        cmdAllPessoa(request,response);
    }

    private void cmdAlterPessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PessoaDTO pessoaDTO = loadPessoaFromRequest(request);
        PessoaService service = new PessoaService(pessoaDTO);
        Pessoa pessoa = service.alterPessoa();
        boolean exec = pessoa.getPessoaId() > 0;
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Os dados da pessoa foi atualizado com sucesso!" : "Ocorreu algum erro na alteração dos dados!");
        cmdAllPessoa(request, response);
    }

    private void cmdAddNewPessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PessoaDTO pessoaDTO = loadPessoaFromRequest(request);
        PessoaService service = new PessoaService(pessoaDTO);
        Pessoa pessoa = service.savePessoa();
        boolean exec = pessoa.getPessoaId() > 0;
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Os dados foram gravados com sucesso!" : "Ocorreu algum erro na gravação dos dados!");
        cmdAllPessoa(request, response);
    }

    private void cmdNewPessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "New People");
        RequestDispatcher rd = request.getRequestDispatcher("/pessoa/new.jsp");
        rd.forward(request,response);
    }

    private void cmdAllPessoa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PessoaService pessoaService = new PessoaService(new PessoaDTO());
        List<Pessoa> pessoaList = pessoaService.getAllPessoas();

        request.setAttribute("pessoas", pessoaList);

        RequestDispatcher rd = request.getRequestDispatcher("/pessoa/ctrl.jsp");
        rd.forward(request,response);
    }

    private void cmdConsult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PessoaService service = new PessoaService(loadPessoaFromRequest(request));
        request.setAttribute("p", service.pessoaById());

        RequestDispatcher rd = request.getRequestDispatcher("/pessoa/alter.jsp");
        rd.forward(request,response);
    }

    private PessoaDTO loadPessoaFromRequest(HttpServletRequest request) {

        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setId(request.getParameterMap().containsKey("id") ? request.getParameter("id") : "0");
        pessoaDTO.setName(request.getParameterMap().containsKey("nome") ? request.getParameter("nome") : "");
        pessoaDTO.setAddress(request.getParameterMap().containsKey("end") ? request.getParameter("end") : "");
        pessoaDTO.setCpf(request.getParameterMap().containsKey("cpf") ? request.getParameter("cpf") : "000.000.000-00");
        pessoaDTO.setCommand(request.getParameterMap().containsKey("cmd") ? request.getParameter("cmd") : "");
        return pessoaDTO;

    }
}
