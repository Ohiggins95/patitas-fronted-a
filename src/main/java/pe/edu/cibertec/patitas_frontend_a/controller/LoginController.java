package pe.edu.cibertec.patitas_frontend_a.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.patitas_frontend_a.viewmodel.LoginModel;
import pe.edu.cibertec.patitas_frontend_a.viewmodel.LoginRequest;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        // Validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().isEmpty() ||
                numeroDocumento == null || numeroDocumento.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        // Hacer la solicitud al backend en formato JSON
        String url = "http://localhost:8081/autenticacion/login";
        LoginRequest loginreqDTO = new LoginRequest(tipoDocumento, numeroDocumento, password);
        LoginModel loginModel= restTemplate.postForObject(url, loginreqDTO,LoginModel.class);

        //validando respuesta del back
        if(loginModel !=null && "00".equals(loginModel.codigo())){
            model.addAttribute("loginModel", loginModel);
            return "principal";
        }else {
            model.addAttribute("loginModel", new LoginModel("01","usuario incorrecto",""));
            return "inicio";
        }




}
}
