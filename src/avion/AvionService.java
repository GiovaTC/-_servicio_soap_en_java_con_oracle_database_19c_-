package avion;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class AvionService {

    @WebMethod
    public String registrarAvion(
            String modelo,
            String fabricante,
            int capacidad,
            int autonomia) {

        Avion avion = new Avion(modelo, fabricante, capacidad, autonomia);
        AvionDAO dao = new AvionDAO();
        dao.registrarAvion(avion);

        return "avion registrado correctamente en oracle 19C";
    }
}
