package br.com.biblioteca.servlet;

import br.com.biblioteca.dao.EmprestimoDao;
import br.com.biblioteca.dao.LivrosDao;
import br.com.biblioteca.dao.UsuariosDao;
import br.com.biblioteca.model.Emprestimo;
import br.com.biblioteca.model.Livros;
import br.com.biblioteca.model.Usuarios;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/profile"})
public class ListUserProfileDataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        listEmprestimos(req);
        listUser(req);

        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }

    protected void listEmprestimos(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Usuarios user = (Usuarios) session.getAttribute("user");
        String userId = user.getId();
        System.out.println("User ID recebido: " + userId);

        EmprestimoDao emprestimoDao = new EmprestimoDao();
        List<Emprestimo> emprestimos = emprestimoDao.findAllEmprestimosByUserId(userId);

        Map<Emprestimo, String> emprestimosComTitulos = new HashMap<>();
        LivrosDao livrosDao = new LivrosDao();

        for (Emprestimo emprestimo : emprestimos) {
            String livroId = emprestimo.getIdLivro();
            List<Livros> livrosEncontrados = livrosDao.findLivrosById(livroId);
            String livroTitulo = livrosEncontrados.get(0).getTitulo();
            emprestimosComTitulos.put(emprestimo, livroTitulo);
        }

        req.setAttribute("emprestimosComTitulos", emprestimosComTitulos);
    }

    protected void listUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Usuarios user = (Usuarios) session.getAttribute("user");
        String userId = user.getId();
        System.out.println("User ID recebido: " + userId);

        List<Usuarios> userDados = new UsuariosDao().findUserById(userId);

        req.setAttribute("user", userDados.get(0));
    }
}

