package com.sovos.web;

import com.sovos.model.dto.VeiculosDTO;
import com.sovos.model.response.Veiculos;
import com.sovos.service.VeiculosService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/VeiculosServlet")
public class VeiculosServlet extends HttpServlet {

    public VeiculosServlet(){
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
                    cmdNewVeiculos(request, response);
                    break;
                case "add":
                    cmdAddNewVeiculos(request, response);
                    break;
                case "alter":
                    cmdAlterVeiculos(request, response);
                    break;
                case "del":
                    cmdDeleteVeiculos(request, response);
                    break;
            }
        }else{
            cmdAllVeiculos(request, response);
        }

    }

    private void cmdDeleteVeiculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VeiculosService service = new VeiculosService(loadVeiculosFromRequest(request));
        boolean exec = service.delete();
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Veiculo foi deletado" : "Erro ao tentar deletar o veiculo");
        cmdAllVeiculos(request,response);
    }

    private void cmdAlterVeiculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VeiculosDTO veiculosDTO = loadVeiculosFromRequest(request);
        VeiculosService service = new VeiculosService(veiculosDTO);
        Veiculos veiculos = service.alterVeiculos();
        boolean exec = veiculos.getVeiculoId() > 0;
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Os dados do veiculo foram  atualizados com sucesso!" : "Ocorreu algum erro na alteração dos dados!");
        cmdAllVeiculos(request, response);
    }

    private void cmdAddNewVeiculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VeiculosDTO veiculosDTO = loadVeiculosFromRequest(request);
        VeiculosService service = new VeiculosService(veiculosDTO);
        Veiculos veiculos = service.saveVeiculos();
        boolean exec = veiculos.getVeiculoId() > 0;
        request.setAttribute("status", exec);
        request.setAttribute("msg", exec ? "Os dados foram gravados com sucesso!" : "Ocorreu algum erro na gravação dos dados!");
        cmdAllVeiculos(request, response);
    }

    private void cmdNewVeiculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("title", "New Vehicle ");
        RequestDispatcher rd = request.getRequestDispatcher("/Veiculos/newV.jsp");
        rd.forward(request,response);
    }

    private void cmdAllVeiculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VeiculosService veiculosService = new VeiculosService(new VeiculosDTO());
        List<Veiculos> veiculosList = veiculosService.getAllVeiculos();

        request.setAttribute("veiculos", veiculosList);

        RequestDispatcher rd = request.getRequestDispatcher("/Veiculos/view.jsp");
        rd.forward(request,response);
    }

    private void cmdConsult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VeiculosService service = new VeiculosService(loadVeiculosFromRequest(request));
        request.setAttribute("v", service.veiculosById());

        RequestDispatcher rd = request.getRequestDispatcher("/Veiculos/alterV.jsp");
        rd.forward(request,response);
    }

    private VeiculosDTO loadVeiculosFromRequest(HttpServletRequest request) {

        VeiculosDTO veiculosDTO = new VeiculosDTO();
        veiculosDTO.setId(Integer.valueOf(request.getParameterMap().containsKey("id") ? request.getParameter("id") : "0"));
        veiculosDTO.setPlaca(request.getParameterMap().containsKey("placa") ? request.getParameter("placa") : "");
        veiculosDTO.setCor(request.getParameterMap().containsKey("cor") ? request.getParameter("cor") : "");
        veiculosDTO.setModelo(request.getParameterMap().containsKey("modelo") ? request.getParameter("modelo") : "");
        veiculosDTO.setCpfProprietario(request.getParameterMap().containsKey("cpfProprietario") ? request.getParameter("cpfProprietario") : "000.000.000-00");
        veiculosDTO.setCommand(request.getParameterMap().containsKey("cmd") ? request.getParameter("cmd") : "");
        return veiculosDTO;

    }
}
